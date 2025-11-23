package helpers;

import java.io.File;
import java.nio.file.Paths;

public class PathHelper {
	private static final String PREFIX_JSP = "/";
	private static final String SUFFIX_JSP = ".jsp";

	// Load từ .env
	private static final String PROJECT_PATH =  "/home/ubuntu/VideoSharer";
	private static final String PREFIX_JAVA =  "/VideoSharer/";

	// Cache ffmpeg paths
	private static String FFMPEG_PATH = null;
	private static String FFPROBE_PATH = null;

	public static String resolve(String viewName) {
		return PREFIX_JSP + viewName + SUFFIX_JSP;
	}

	public static java.nio.file.Path getOriginalPath() {
		return Paths.get(PROJECT_PATH, "WebContent", "uploads", "original");
	}

	public static java.nio.file.Path getHlsPath() {
		return Paths.get(PROJECT_PATH, "WebContent", "uploads", "hls");
	}

	public static String getWatchLink(int id) {
		return PREFIX_JAVA + "watch?id=" + id;
	}

	public static String getFfmpegPath() {
		if (FFMPEG_PATH != null) {
			return FFMPEG_PATH;
		}

		java.nio.file.Path ffmpegPath = Paths.get(PROJECT_PATH, "WebContent", "WEB-INF", "lib", "ffmpeg.exe");
		File ffmpegFile = ffmpegPath.toFile();

		if (ffmpegFile.exists() && ffmpegFile.isFile()) {
			FFMPEG_PATH = ffmpegFile.getAbsolutePath();
			return FFMPEG_PATH;
		}

		FFMPEG_PATH = "ffmpeg";
		return FFMPEG_PATH;
	}

	public static String getFfprobePath() {
		if (FFPROBE_PATH != null) {
			return FFPROBE_PATH;
		}

		java.nio.file.Path ffprobePath = Paths.get(PROJECT_PATH, "WebContent", "WEB-INF", "lib", "ffprobe.exe");
		File ffprobeFile = ffprobePath.toFile();

		if (ffprobeFile.exists() && ffprobeFile.isFile()) {
			FFPROBE_PATH = ffprobeFile.getAbsolutePath();
			return FFPROBE_PATH;
		}

		// Fallback: System PATH
		FFPROBE_PATH = "ffprobe";
		return FFPROBE_PATH;
	}
}