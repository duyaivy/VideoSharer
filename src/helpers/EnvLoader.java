package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    private static Map<String, String> envVariables = new HashMap<>();
    private static boolean isLoaded = false;

    public static void load() {
        if (isLoaded) {
            return;
        }

        String[] possiblePaths = {
            ".env",
            "../.env",
            System.getProperty("user.dir") + "/.env",
            System.getProperty("catalina.base") + "/conf/.env"
        };

        File envFile = null;
        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                envFile = f;
                break;
            }
        }

        if (envFile == null) {
            loadDefaults();
            isLoaded = true;
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int separatorIndex = line.indexOf('=');
                if (separatorIndex > 0) {
                    String key = line.substring(0, separatorIndex).trim();
                    String value = line.substring(separatorIndex + 1).trim();
                    
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    envVariables.put(key, value);
                }
            }
            
            isLoaded = true;
            
        } catch (IOException e) {
            loadDefaults();
            isLoaded = true;
        }
    }

    private static void loadDefaults() {
        envVariables.put("PROJECT_PATH", "D:/ECLIPSE2020/VideoSharer");
        envVariables.put("FFMPEG_DIR", "D:/ECLIPSE2020/VideoSharer/WebContent/WEB-INF/lib");
        envVariables.put("BASE_URL", "/VideoSharer/");
        envVariables.put("DB_HOST", "localhost");
        envVariables.put("DB_PORT", "3306");
        envVariables.put("DB_NAME", "video_sharer");
        envVariables.put("DB_USER", "root");
        envVariables.put("DB_PASSWORD", "");
    }

    public static String get(String key) {
        if (!isLoaded) {
            load();
        }
        return envVariables.get(key);
    }

    public static String get(String key, String defaultValue) {
        if (!isLoaded) {
            load();
        }
        return envVariables.getOrDefault(key, defaultValue);
    }

    public static boolean has(String key) {
        if (!isLoaded) {
            load();
        }
        return envVariables.containsKey(key);
    }
}
