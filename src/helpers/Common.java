package helpers;

import java.io.File;
import java.nio.file.Path;

public class Common {

	public static boolean  deleteVideoOriginal(Path fullVideoPath) {
		try {
			File originalVideo = fullVideoPath.toFile();
			if (originalVideo.exists() && originalVideo.delete()) {
				System.out.println("Delete success: " + fullVideoPath);
				return true;
			} else {
				System.err.println("Failed to delete original video: " + fullVideoPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
}
