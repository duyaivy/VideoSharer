package workers;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import helpers.Common;
import helpers.Ffmpeg;
import helpers.PathHelper;
import model.BO.queueBO;
import model.BO.videoBO;
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

		

		while (running) {
			try {
				VideoQueue task = queueBO.getInstance().getNextPendingTask();

				if (task == null) {
					Thread.sleep(5000);
					continue;
				}

				// processing
				queueBO.getInstance().updateStatus(task.getQueueId(), "processing");
				videoBO.getInstance().updateVideoStatus(task.getVideoId(), "processing");
				sendStatus(task.getVideoId(), "processing");

				Video video = videoBO.getInstance().getVideoById(task.getVideoId());
				if (video == null) {
					System.err.println(" not found: " + task.getVideoId());
					queueBO.getInstance().updateStatus(task.getQueueId(), "failed");
					videoBO.getInstance().updateVideoStatus(task.getVideoId(), "failed");
					sendStatus(task.getVideoId(), "failed");
					continue;
				}

				String videoPath = video.getPath();
				Path fullVideoPath = PathHelper.getOriginalPath().getParent().getParent().resolve(videoPath);

				System.out.println("Input video: " + fullVideoPath.toAbsolutePath());

				String videoFolder = Paths.get(videoPath).getName(Paths.get(videoPath).getNameCount() - 2).toString();
				Path hlsOutputDir = PathHelper.getHlsPath().resolve(videoFolder);

				System.out.println("HLS output: " + hlsOutputDir.toAbsolutePath());

				boolean success = Ffmpeg.encodeHLSWithMultipleVideoStreams(fullVideoPath.toAbsolutePath().toString(),
						hlsOutputDir.toAbsolutePath().toString());

				if (success) {

					Common.deleteVideoOriginal(fullVideoPath);

					String hlsPath = "uploads/hls/" + videoFolder + "/master.m3u8";
					videoBO.getInstance().updateVideoPath(task.getVideoId(), hlsPath, video.getImg());

					queueBO.getInstance().updateStatus(task.getQueueId(), "done");
					videoBO.getInstance().updateVideoStatus(task.getVideoId(), "done");
					sendStatus(task.getVideoId(), "done");

				} else {

					if (task.getRetryCount() < MAX_RETRY) {
						queueBO.getInstance().incrementCount(task.getQueueId());
						queueBO.getInstance().updateStatus(task.getQueueId(), "pending");
						videoBO.getInstance().updateVideoStatus(task.getVideoId(), "pending");
						sendStatus(task.getVideoId(), "pending");

					} else {
						queueBO.getInstance().updateStatus(task.getQueueId(), "failed");
						videoBO.getInstance().updateVideoStatus(task.getVideoId(), "failed");
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

	private void sendStatus(int videoId, String status) {
		try {
			int userId = videoBO.getInstance().getAuthorIdByVideoId(videoId);
			JsonObject obj = new JsonObject();
			obj.addProperty("videoId", videoId);
			obj.addProperty("status", status);

			String json = new Gson().toJson(obj);
			
			VideoStatusSocket.broadcast(Integer.toString(userId),json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
