package model.BO;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import helpers.Ffmpeg;
import helpers.ViewPath;
import model.Bean.Video;
import model.DAO.VideoQueueDAO;
import model.DAO.videoDAO;

public class videoBO {
	private videoDAO dao;
	private static videoBO bo = null;

	public videoBO() {
		dao = new videoDAO();
	}

	// ================== UPLOAD VIDEO ==================
	public boolean uploadVideo(int authorId, String title, String des, Part part) {

		Video vd = dao.createVideo(authorId, title, des);
		if (vd == null) {
			return false;
		}

		int videoId = vd.getVideoId();
		Path videoDir = ViewPath.getOriginalPath().resolve("video_" + videoId);

		try {
			// tạo thư mục lưu file
			Files.createDirectories(videoDir);

			// tên file upload
			String submittedFileName = getSubmittedFileName(part);
			if (submittedFileName == null || submittedFileName.isEmpty()) {
				submittedFileName = "video_" + videoId + ".mp4";
			}

			String safeFileName = Paths.get(submittedFileName).getFileName().toString();
			Path videoFile = videoDir.resolve(safeFileName);

			// copy file lên server
			try (InputStream in = part.getInputStream()) {
				long bytes = Files.copy(in, videoFile);
				System.out.println("Upload ok " + bytes + " bytes");
			}

			// set status đang xử lý
			dao.updateVideoStatus(videoId, "processing");

			// tạo thumbnail
			String thumbFileName = "thumb.jpg";
			Path thumbFile = videoDir.resolve(thumbFileName);

			boolean ffmpegOk = Ffmpeg.generateThumbnailWithFfmpeg(videoFile.toAbsolutePath().toString(),
					thumbFile.toAbsolutePath().toString());

			if (!ffmpegOk) {
				dao.updateVideoInfo(videoId, vd.getTitle(), vd.getDescription(), "failed");
				return false;
			}

			// lưu path tương đối vào DB
			String relativeVideoPath = Paths.get("uploads/original/video_" + videoId, safeFileName).toString()
					.replace("\\", "/");
			String relativeImgPath = Paths.get("uploads/original/video_" + videoId, thumbFileName).toString()
					.replace("\\", "/");

			dao.updateVideoPath(videoId, relativeVideoPath, relativeImgPath);

			// thêm vào queue để encode HLS
			VideoQueueDAO queueDAO = new VideoQueueDAO();
			boolean addedToQueue = queueDAO.addToQueue(videoId);

			if (addedToQueue) {
				System.out.println("✅ Video added to encoding queue: " + videoId);
				dao.updateVideoStatus(videoId, "pending");
			} else {
				System.err.println("❌ Failed to add to queue");
				dao.updateVideoStatus(videoId, "failed");
				return false;
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				dao.updateVideoInfo(videoId, vd.getTitle(), vd.getDescription(), "failed");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return false;
		}
	}

	// singleton (nếu project có dùng)
	public static videoBO getInstance() {
		if (bo == null)
			bo = new videoBO();
		return bo;
	}

	// ================== TRENDING / XEM VIDEO ==================
	public ArrayList<Video> getVideoTrending(int page, int size) {
		return dao.getTrendingVideo(page, size);
	}

	public Video getVideoById(int id) {
		return dao.getVideoByID(id);
	}

	public Video watchVideo(int id) {
		Video vd = dao.getVideoByID(id);
		if (vd != null) {
			dao.incrementView(id);
			vd.setView(vd.getView() + 1);
		}
		return vd;
	}

	// ================== MANAGE VIDEO (TASK 2) ==================

	// đếm tổng số video của 1 author (chỉ lấy is_delete = 0 trong DAO)
	public int countVideosByAuthor(int authorId) {
		return dao.countByAuthor(authorId);
	}

	
	public List<Video> getVideosByAuthorWithPaging(int authorId, int page, int pageSize) {
		if (page < 1)
			page = 1;
		int offset = (page - 1) * pageSize;
		return dao.getVideosByAuthor(authorId, offset, pageSize);
	}


	public boolean updateBasicInfo(int videoId, String title, String description) {
		return dao.updateVideoInfoBasic(videoId, title, description);
	}

	// delete mềm: đổi is_delete = 1
	public boolean softDeleteVideo(int videoId, int authorId) {
		return dao.softDelete(videoId, authorId);
	}

	// ================== HELPER ==================
	private String getSubmittedFileName(Part part) {
		if (part == null)
			return null;
		String header = part.getHeader("content-disposition");
		if (header == null)
			return null;
		for (String cd : header.split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
				return fileName;
			}
		}
		return null;
	}
}
