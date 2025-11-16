package workers;

import helpers.Common;
import helpers.Ffmpeg;
import helpers.ViewPath;
import model.Bean.Video;
import model.Bean.VideoQueue;
import model.DAO.VideoQueueDAO;
import model.DAO.videoDAO;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoQueueWorker implements Runnable {
	private volatile boolean running = true;
	private static final int MAX_RETRY = 3;

	@Override
	public void run() {
		System.out.println("🚀 VideoEncodingWorker started!");

		VideoQueueDAO queueDAO = new VideoQueueDAO();
		videoDAO vdDAO = new videoDAO();

		while (running) {
			try {
				VideoQueue task = queueDAO.getNextPendingTask();

				if (task == null) {
					Thread.sleep(5000);
					continue;
				}

				queueDAO.updateStatus(task.getQueueId(), "processing");
				vdDAO.updateVideoStatus(task.getVideoId(), "processing");

				Video video = vdDAO.getVideoByID(task.getVideoId());
				if (video == null) {
					System.err.println(" not found: " + task.getVideoId());
					queueDAO.updateStatus(task.getQueueId(), "failed");
					continue;
				}

				String videoPath = video.getPath();
				Path fullVideoPath = ViewPath.getOriginalPath().getParent().getParent().resolve(videoPath);

				System.out.println("Input video: " + fullVideoPath.toAbsolutePath());

				String videoFolder = Paths.get(videoPath).getName(Paths.get(videoPath).getNameCount() - 2).toString();
				Path hlsOutputDir = ViewPath.getHlsPath().resolve(videoFolder);

				System.out.println("HLS output: " + hlsOutputDir.toAbsolutePath());

				// Encode HLS
				boolean success = Ffmpeg.encodeHLSWithMultipleVideoStreams(fullVideoPath.toAbsolutePath().toString(),
						hlsOutputDir.toAbsolutePath().toString());

				if (success) {

					// delete
					Common.deleteVideoOriginal(fullVideoPath);

					String hlsPath = "uploads/hls/" + videoFolder + "/master.m3u8";
					vdDAO.updateVideoPath(task.getVideoId(), hlsPath, video.getImg());

					queueDAO.updateStatus(task.getQueueId(), "done");
					vdDAO.updateVideoStatus(task.getVideoId(), "done");

				} else {

					if (task.getRetryCount() < MAX_RETRY) {
						queueDAO.incrementCount(task.getQueueId());
						queueDAO.updateStatus(task.getQueueId(), "pending");

					} else {
						queueDAO.updateStatus(task.getQueueId(), "failed");
						vdDAO.updateVideoStatus(task.getVideoId(), "failed");

					}
				}

			} catch (Exception e) {

				e.printStackTrace();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}

		System.out.println("stopped!");
	}

	public void stop() {
		running = false;
	}
}