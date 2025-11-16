package helpers;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ffmpeg {
    private static final long FFMPEG_TIMEOUT_SECONDS = 3000; 
    private static final long FFPROBE_TIMEOUT_SECONDS = 10;
    private static String FFMPEG_PATH = null;
    private static String FFPROBE_PATH = null;
    
    // Bitrate constants
    private static final int MAXIMUM_BITRATE_720P = 5 * 1000000; // 5Mbps
    private static final int MAXIMUM_BITRATE_1080P = 8 * 1000000; // 8Mbps
    private static final int MAXIMUM_BITRATE_1440P = 16 * 1000000; // 16Mbps
    
    // Resolution class
    public static class Resolution {
        public int width;
        public int height;
        
        public Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    
    // Bitrate class
    public static class Bitrate {
        public int b720;
        public int b1080;
        public int b1440;
        public int original;
        
        public Bitrate(int b720, int b1080, int b1440, int original) {
            this.b720 = b720;
            this.b1080 = b1080;
            this.b1440 = b1440;
            this.original = original;
        }
    }
    
    private static String getFfmpegPath() {
        if (FFMPEG_PATH != null) {
            return FFMPEG_PATH;
        }
        
        String[] possiblePaths = {
            "lib/ffmpeg/ffmpeg.exe",
            System.getProperty("user.dir") + "/lib/ffmpeg/ffmpeg.exe",
            "ffmpeg"
        };
        
        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                FFMPEG_PATH = f.getAbsolutePath();
                System.out.println("Found ffmpeg at: " + FFMPEG_PATH);
                return FFMPEG_PATH;
            }
        }
        
        FFMPEG_PATH = "ffmpeg";
        System.out.println("Using system ffmpeg (must be in PATH)");
        return FFMPEG_PATH;
    }
    
    private static String getFfprobePath() {
        if (FFPROBE_PATH != null) {
            return FFPROBE_PATH;
        }
        
        String[] possiblePaths = {
            "lib/ffmpeg/ffprobe.exe",
            System.getProperty("user.dir") + "/lib/ffmpeg/ffprobe.exe",
            "ffprobe"
        };
        
        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                FFPROBE_PATH = f.getAbsolutePath();
                System.out.println("Found ffprobe at: " + FFPROBE_PATH);
                return FFPROBE_PATH;
            }
        }
        
        FFPROBE_PATH = "ffprobe";
        System.out.println("Using system ffprobe (must be in PATH)");
        return FFPROBE_PATH;
    }
    
    /**
     * Kiểm tra video có audio không
     */
    public static boolean checkVideoHasAudio(String filePath) {
        try {
            String ffprobeExe = getFfprobePath();
            
            ProcessBuilder pb = new ProcessBuilder(
                ffprobeExe,
                "-v", "error",
                "-select_streams", "a:0",
                "-show_entries", "stream=codec_type",
                "-of", "default=nw=1:nk=1",
                filePath
            );
            
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            
            boolean finished = p.waitFor(FFPROBE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                p.destroyForcibly();
                return false;
            }
            
            return output.toString().trim().equals("audio");
            
        } catch (Exception e) {
            System.err.println("Error checking audio: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lấy bitrate của video
     */
    public static int getBitrate(String filePath) {
        try {
            String ffprobeExe = getFfprobePath();
            
            ProcessBuilder pb = new ProcessBuilder(
                ffprobeExe,
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=bit_rate",
                "-of", "default=nw=1:nk=1",
                filePath
            );
            
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            
            boolean finished = p.waitFor(FFPROBE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                p.destroyForcibly();
                return 0;
            }
            
            String result = output.toString().trim();
            if (result.isEmpty() || result.equals("N/A")) {
                return 0;
            }
            
            return Integer.parseInt(result);
            
        } catch (Exception e) {
            System.err.println("Error getting bitrate: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Lấy resolution của video
     */
    public static Resolution getResolution(String filePath) {
        try {
            String ffprobeExe = getFfprobePath();
            
            ProcessBuilder pb = new ProcessBuilder(
                ffprobeExe,
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height",
                "-of", "csv=s=x:p=0",
                filePath
            );
            
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            
            boolean finished = p.waitFor(FFPROBE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                p.destroyForcibly();
                return new Resolution(0, 0);
            }
            
            String[] parts = output.toString().trim().split("x");
            if (parts.length == 2) {
                int width = Integer.parseInt(parts[0]);
                int height = Integer.parseInt(parts[1]);
                return new Resolution(width, height);
            }
            
            return new Resolution(0, 0);
            
        } catch (Exception e) {
            System.err.println("Error getting resolution: " + e.getMessage());
            return new Resolution(0, 0);
        }
    }
    
    /**
     * Tính width dựa trên height và giữ tỷ lệ
     * Width phải là số chẵn
     */
    private static int getWidth(int height, Resolution resolution) {
        int width = Math.round((float) height * resolution.width / resolution.height);
        return width % 2 == 0 ? width : width + 1;
    }
    
    /**
     * Encode video với resolution tối đa 720p
     */
    private static boolean encodeMax720(String inputPath, String outputPath, 
                                       String outputSegmentPath, Resolution resolution, 
                                       Bitrate bitrate, boolean isHasAudio) {
        try {
            String ffmpegExe = getFfmpegPath();
            
            List<String> args = new ArrayList<>();
            args.add(ffmpegExe);
            args.add("-y");
            args.add("-i");
            args.add(inputPath);
            args.add("-preset");
            args.add("veryslow");
            args.add("-g");
            args.add("48");
            args.add("-crf");
            args.add("17");
            args.add("-sc_threshold");
            args.add("0");
            args.add("-map");
            args.add("0:0");
            
            if (isHasAudio) {
                args.add("-map");
                args.add("0:1");
            }
            
            args.add("-s:v:0");
            args.add(getWidth(720, resolution) + "x720");
            args.add("-c:v:0");
            args.add("libx264");
            args.add("-b:v:0");
            args.add(String.valueOf(bitrate.b720));
            args.add("-c:a");
            args.add("copy");
            args.add("-var_stream_map");
            args.add(isHasAudio ? "v:0,a:0" : "v:0");
            args.add("-master_pl_name");
            args.add("master.m3u8");
            args.add("-f");
            args.add("hls");
            args.add("-hls_time");
            args.add("6");
            args.add("-hls_list_size");
            args.add("0");
            args.add("-hls_segment_filename");
            args.add(outputSegmentPath);
            args.add(outputPath);
            
            return executeFFmpeg(args);
            
        } catch (Exception e) {
            System.err.println("Error encoding 720p: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Encode video với resolution tối đa 1080p (720p + 1080p)
     */
    private static boolean encodeMax1080(String inputPath, String outputPath,
                                        String outputSegmentPath, Resolution resolution,
                                        Bitrate bitrate, boolean isHasAudio) {
        try {
            String ffmpegExe = getFfmpegPath();
            
            List<String> args = new ArrayList<>();
            args.add(ffmpegExe);
            args.add("-y");
            args.add("-i");
            args.add(inputPath);
            args.add("-preset");
            args.add("veryslow");
            args.add("-g");
            args.add("48");
            args.add("-crf");
            args.add("17");
            args.add("-sc_threshold");
            args.add("0");
            
            if (isHasAudio) {
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:1");
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:1");
            } else {
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:0");
            }
            
            // 720p stream
            args.add("-s:v:0");
            args.add(getWidth(720, resolution) + "x720");
            args.add("-c:v:0");
            args.add("libx264");
            args.add("-b:v:0");
            args.add(String.valueOf(bitrate.b720));
            
            // 1080p stream
            args.add("-s:v:1");
            args.add(getWidth(1080, resolution) + "x1080");
            args.add("-c:v:1");
            args.add("libx264");
            args.add("-b:v:1");
            args.add(String.valueOf(bitrate.b1080));
            
            args.add("-c:a");
            args.add("copy");
            args.add("-var_stream_map");
            args.add(isHasAudio ? "v:0,a:0 v:1,a:1" : "v:0 v:1");
            args.add("-master_pl_name");
            args.add("master.m3u8");
            args.add("-f");
            args.add("hls");
            args.add("-hls_time");
            args.add("6");
            args.add("-hls_list_size");
            args.add("0");
            args.add("-hls_segment_filename");
            args.add(outputSegmentPath);
            args.add(outputPath);
            
            return executeFFmpeg(args);
            
        } catch (Exception e) {
            System.err.println("Error encoding 1080p: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Encode video với resolution tối đa 1440p (720p + 1080p + 1440p)
     */
    private static boolean encodeMax1440(String inputPath, String outputPath,
                                        String outputSegmentPath, Resolution resolution,
                                        Bitrate bitrate, boolean isHasAudio) {
        try {
            String ffmpegExe = getFfmpegPath();
            
            List<String> args = new ArrayList<>();
            args.add(ffmpegExe);
            args.add("-y");
            args.add("-i");
            args.add(inputPath);
            args.add("-preset");
            args.add("veryslow");
            args.add("-g");
            args.add("48");
            args.add("-crf");
            args.add("17");
            args.add("-sc_threshold");
            args.add("0");
            
            if (isHasAudio) {
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:1");
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:1");
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:1");
            } else {
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:0");
            }
            
            // 720p stream
            args.add("-s:v:0");
            args.add(getWidth(720, resolution) + "x720");
            args.add("-c:v:0");
            args.add("libx264");
            args.add("-b:v:0");
            args.add(String.valueOf(bitrate.b720));
            
            // 1080p stream
            args.add("-s:v:1");
            args.add(getWidth(1080, resolution) + "x1080");
            args.add("-c:v:1");
            args.add("libx264");
            args.add("-b:v:1");
            args.add(String.valueOf(bitrate.b1080));
            
            // 1440p stream
            args.add("-s:v:2");
            args.add(getWidth(1440, resolution) + "x1440");
            args.add("-c:v:2");
            args.add("libx264");
            args.add("-b:v:2");
            args.add(String.valueOf(bitrate.b1440));
            
            args.add("-c:a");
            args.add("copy");
            args.add("-var_stream_map");
            args.add(isHasAudio ? "v:0,a:0 v:1,a:1 v:2,a:2" : "v:0 v:1 v:2");
            args.add("-master_pl_name");
            args.add("master.m3u8");
            args.add("-f");
            args.add("hls");
            args.add("-hls_time");
            args.add("6");
            args.add("-hls_list_size");
            args.add("0");
            args.add("-hls_segment_filename");
            args.add(outputSegmentPath);
            args.add(outputPath);
            
            return executeFFmpeg(args);
            
        } catch (Exception e) {
            System.err.println("Error encoding 1440p: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Encode video với resolution gốc (720p + 1080p + original)
     */
    private static boolean encodeMaxOriginal(String inputPath, String outputPath,
                                            String outputSegmentPath, Resolution resolution,
                                            Bitrate bitrate, boolean isHasAudio) {
        try {
            String ffmpegExe = getFfmpegPath();
            
            List<String> args = new ArrayList<>();
            args.add(ffmpegExe);
            args.add("-y");
            args.add("-i");
            args.add(inputPath);
            args.add("-preset");
            args.add("veryslow");
            args.add("-g");
            args.add("48");
            args.add("-crf");
            args.add("17");
            args.add("-sc_threshold");
            args.add("0");
            
            if (isHasAudio) {
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:1");
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:1");
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:1");
            } else {
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:0");
                args.add("-map");
                args.add("0:0");
            }
            
            // 720p stream
            args.add("-s:v:0");
            args.add(getWidth(720, resolution) + "x720");
            args.add("-c:v:0");
            args.add("libx264");
            args.add("-b:v:0");
            args.add(String.valueOf(bitrate.b720));
            
            // 1080p stream
            args.add("-s:v:1");
            args.add(getWidth(1080, resolution) + "x1080");
            args.add("-c:v:1");
            args.add("libx264");
            args.add("-b:v:1");
            args.add(String.valueOf(bitrate.b1080));
            
            // Original stream
            args.add("-s:v:2");
            args.add(resolution.width + "x" + resolution.height);
            args.add("-c:v:2");
            args.add("libx264");
            args.add("-b:v:2");
            args.add(String.valueOf(bitrate.original));
            
            args.add("-c:a");
            args.add("copy");
            args.add("-var_stream_map");
            args.add(isHasAudio ? "v:0,a:0 v:1,a:1 v:2,a:2" : "v:0 v:1 v:2");
            args.add("-master_pl_name");
            args.add("master.m3u8");
            args.add("-f");
            args.add("hls");
            args.add("-hls_time");
            args.add("6");
            args.add("-hls_list_size");
            args.add("0");
            args.add("-hls_segment_filename");
            args.add(outputSegmentPath);
            args.add(outputPath);
            
            return executeFFmpeg(args);
            
        } catch (Exception e) {
            System.err.println("Error encoding original: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Execute FFmpeg command
     */
    private static boolean executeFFmpeg(List<String> args) {
        try {
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            // Đọc output
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[FFmpeg] " + line);
                }
            }
            
            boolean finished = p.waitFor(FFMPEG_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            
            if (!finished) {
                System.err.println("FFmpeg timeout!");
                p.destroyForcibly();
                return false;
            }
            
            int exitCode = p.exitValue();
            return exitCode == 0;
            
        } catch (Exception e) {
            System.err.println("Error executing FFmpeg: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * HÀM CHÍNH: Encode HLS với multiple video streams
     * @param inputPath Đường dẫn video gốc
     * @return true nếu encode thành công
     */
    public static boolean encodeHLSWithMultipleVideoStreams(String inputPath, String outputDir) {
        try {
          
            System.out.println("Input: " + inputPath);
            
            // Lấy thông tin video
            int bitrate = getBitrate(inputPath);
            Resolution resolution = getResolution(inputPath);
            boolean isHasAudio = checkVideoHasAudio(inputPath);
            
            System.out.println("Bitrate: " + bitrate);
            System.out.println("Resolution: " + resolution.width + "x" + resolution.height);
            System.out.println("Has Audio: " + isHasAudio);
            
            if (resolution.height == 0 || resolution.width == 0) {
                System.err.println("Invalid resolution!");
                return false;
            }
            
           
            int bitrate720 = Math.min(bitrate, MAXIMUM_BITRATE_720P);
            int bitrate1080 = Math.min(bitrate, MAXIMUM_BITRATE_1080P);
            int bitrate1440 = Math.min(bitrate, MAXIMUM_BITRATE_1440P);
            
            Bitrate bitrateConfig = new Bitrate(bitrate720, bitrate1080, bitrate1440, bitrate);
            
            Path outputDirPath = Paths.get(outputDir);
            String outputSegmentPath = outputDirPath.resolve("v%v").resolve("fileSequence%d.ts").toString();
            String outputPath = outputDirPath.resolve("v%v").resolve("prog_index.m3u8").toString();
            
            System.out.println("Output segment: " + outputSegmentPath);
            System.out.println("Output playlist: " + outputPath);
            
            
            boolean success = false;
            
            if (resolution.height <= 720) {
                System.out.println("Encoding mode: Max 720p");
                success = encodeMax720(inputPath, outputPath, outputSegmentPath, 
                                      resolution, bitrateConfig, isHasAudio);
            } else if (resolution.height <= 1080) {
                System.out.println("Encoding mode: Max 1080p");
                success = encodeMax1080(inputPath, outputPath, outputSegmentPath, 
                                       resolution, bitrateConfig, isHasAudio);
            } else if (resolution.height <= 1440) {
                System.out.println("Encoding mode: Max 1440p");
                success = encodeMax1440(inputPath, outputPath, outputSegmentPath, 
                                       resolution, bitrateConfig, isHasAudio);
            } else {
                System.out.println("Encoding mode: Original quality");
                success = encodeMaxOriginal(inputPath, outputPath, outputSegmentPath, 
                                           resolution, bitrateConfig, isHasAudio);
            }
            
            if (success) {
                System.out.println("✅ HLS Encoding completed successfully!");
            } else {
                System.err.println("❌ HLS Encoding failed!");
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error in HLS encoding: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Generate thumbnail (giữ nguyên hàm cũ)
     */
    public static boolean generateThumbnailWithFfmpeg(String inputPath, String outputPath) {
        try {
            String ffmpegExe = getFfmpegPath();
            
            ProcessBuilder pb = new ProcessBuilder(
                ffmpegExe,
                "-y",
                "-i", inputPath,
                "-ss", "00:00:01.000",
                "-vframes", "1",
                "-vf", "scale=320:-1",
                "-q:v", "2",
                outputPath
            );
            
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[FFmpeg] " + line);
                }
            }
            
            boolean finished = p.waitFor(FFMPEG_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            
            if (!finished) {
                System.err.println("FFmpeg timeout!");
                p.destroyForcibly();
                return false;
            }
            
            int exitCode = p.exitValue();
            
            if (exitCode == 0) {
                System.out.println("Thumbnail created successfully: " + outputPath);
                return true;
            } else {
                System.err.println("FFmpeg exited with code: " + exitCode);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Error running FFmpeg: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}