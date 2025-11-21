package workers;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import helpers.Common;
import helpers.Ffmpeg;
import helpers.ViewPath;
import model.Bean.Video;
import model.Bean.VideoQueue;
import model.DAO.VideoQueueDAO;
import model.DAO.videoDAO;
import websocket.VideoStatusSocket;

public class VideoQueueWorker implements Runnable {
	private volatile boolean running = true;
	private static final int MAX_RETRY = 3;

	@Override
	public void run() {

		VideoQueueDAO queueDAO = new VideoQueueDAO();
		videoDAO vdDAO = new videoDAO();

		while (running) {
			try {
				VideoQueue task = queueDAO.getNextPendingTask();

				if (task == null) {
					Thread.sleep(5000);
					continue;
				}

				// processing
				queueDAO.updateStatus(task.getQueueId(), "processing");
				vdDAO.updateVideoStatus(task.getVideoId(), "processing");
				sendStatus(task.getVideoId(), "processing");

				Video video = vdDAO.getVideoByID(task.getVideoId());
				if (video == null) {
					System.err.println(" not found: " + task.getVideoId());
					queueDAO.updateStatus(task.getQueueId(), "failed");
					vdDAO.updateVideoStatus(task.getVideoId(), "failed");
					sendStatus(task.getVideoId(), "failed");
					continue;
				}

				String videoPath = video.getPath();
				Path fullVideoPath = ViewPath.getOriginalPath().getParent().getParent().resolve(videoPath);

				System.out.println("Input video: " + fullVideoPath.toAbsolutePath());

				String videoFolder = Paths.get(videoPath).getName(Paths.get(videoPath).getNameCount() - 2).toString();
				Path hlsOutputDir = ViewPath.getHlsPath().resolve(videoFolder);

				System.out.println("HLS output: " + hlsOutputDir.toAbsolutePath());

				boolean success = Ffmpeg.encodeHLSWithMultipleVideoStreams(fullVideoPath.toAbsolutePath().toString(),
						hlsOutputDir.toAbsolutePath().toString());

				if (success) {

					Common.deleteVideoOriginal(fullVideoPath);

					String hlsPath = "uploads/hls/" + videoFolder + "/master.m3u8";
					vdDAO.updateVideoPath(task.getVideoId(), hlsPath, video.getImg());

					queueDAO.updateStatus(task.getQueueId(), "done");
					vdDAO.updateVideoStatus(task.getVideoId(), "done");
					sendStatus(task.getVideoId(), "done");

				} else {

					if (task.getRetryCount() < MAX_RETRY) {
						queueDAO.incrementCount(task.getQueueId());
						queueDAO.updateStatus(task.getQueueId(), "pending");
						vdDAO.updateVideoStatus(task.getVideoId(), "pending");
						sendStatus(task.getVideoId(), "pending");

					} else {
						queueDAO.updateStatus(task.getQueueId(), "failed");
						vdDAO.updateVideoStatus(task.getVideoId(), "failed");
						sendStatus(task.getVideoId(), "failed");
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

	// ========== gửi JSON status qua WebSocket ==========
	private void sendStatus(int videoId, String status) {
		try {
			JsonObject obj = new JsonObject();
			obj.addProperty("videoId", videoId);
			obj.addProperty("status", status);

			String json = new Gson().toJson(obj);
			VideoStatusSocket.broadcast(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
