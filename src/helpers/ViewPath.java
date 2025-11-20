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
		String projectPath = "F:\\\\App Downloads\\sts-4.27.0.RELEASE\\VideoSharer";
		Path uploadBase = Paths.get(projectPath, "WebContent", "uploads", "original");
		return uploadBase;
	}

	public static Path getHlsPath() {
		String projectPath = "F:\\App Downloads\\sts-4.27.0.RELEASE\\VideoSharer";
		Path uploadBase = Paths.get(projectPath, "WebContent", "uploads", "hls");
		return uploadBase;
	}

	public static String getWatchLink(int id) {
		String idString = Integer.toString(id);
		return PREFIX_JAVA + "watch?id=" + idString;
	}
}
