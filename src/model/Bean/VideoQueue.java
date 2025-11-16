package model.Bean;

import java.sql.Timestamp;


public class VideoQueue {
    private int queueId;
    private int videoId;
    private String status; 
    private int retryCount;
    private Timestamp createAt;

    
    public VideoQueue() {
    }

  
    public VideoQueue(int queueId, int videoId, String status,
            int retryCount, Timestamp createAt) {
        this.queueId = queueId;
        this.videoId = videoId;
        this.status = status;
        this.retryCount = retryCount;
        this.createAt = createAt;
    }

    
    public VideoQueue(int videoId, String status) {
        this.videoId = videoId;
        this.status = status;
        this.retryCount = 0;
    }

   
    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    // Method tiện ích
    public void incrementRetry() {
        this.retryCount++;
    }

    public boolean canRetry() {
        return this.retryCount < 3; // Tối đa retry 3 lần
    }

    public boolean isPending() {
        return "pending".equals(this.status);
    }

    public boolean isProcessing() {
        return "processing".equals(this.status);
    }

    public boolean isDone() {
        return "done".equals(this.status);
    }

    public boolean isFailed() {
        return "failed".equals(this.status);
    }

    @Override
    public String toString() {
        return "VideoQueue{" +
                "queueId=" + queueId +
                ", videoId=" + videoId +
                ", status='" + status + '\'' +
                ", retryCount=" + retryCount +
                ", createAt=" + createAt +
                '}';
    }
}