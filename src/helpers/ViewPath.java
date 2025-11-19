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
		String projectPath = "F:\\App Downloads\\sts-4.27.0.RELEASE\\VideoSharer";
		Path uploadBase = Paths.get(projectPath, "WebContent", "uploads", "original");
		return uploadBase;
	}

	public static Path getHlsPath() {
		String projectPath = "F:\\App Downloads\\sts-4.27.0.RELEASE\\VideoSharer";
		Path uploadBase = Paths.get(projectPath, "WebContent", "uploads", "hls");
		return uploadBase;
	}
}
