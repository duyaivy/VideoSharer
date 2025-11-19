package helpers;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ViewPath {
    private static final String PREFIX = "/";
    private static final String SUFFIX = ".jsp";

    public static String resolve(String viewName) {
        return PREFIX + viewName + SUFFIX;
    }
    public static Path getOriginalPath() {
    	String projectPath = "D:\\HOCKY4\\LapTrinhMang\\BaiTapNhom"; 
        Path uploadBase = Paths.get(projectPath, "WebContent", "uploads", "original");
        return uploadBase;
    }
    public static Path getHlsPath() {
    	String projectPath = "D:\\HOCKY4\\LapTrinhMang\\BaiTapNhom"; 
        Path uploadBase = Paths.get(projectPath, "WebContent", "uploads", "hls");
        return uploadBase;
    }
}	
