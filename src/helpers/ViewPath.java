package helpers;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ViewPath {
    private static final String PREFIX_JSP = "/";
    private static final String SUFFIX_JSP = ".jsp";

    private static final String PREFIX_JAVA = "/VideoSharer/";
   
    public static String resolve(String viewName) {
        return PREFIX_JSP + viewName + SUFFIX_JSP;
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
    public static String getWatchLink(int id) {
    	String idString = Integer.toString(id);
    	 return PREFIX_JAVA + "watch?id=" + idString;
    }
}	
