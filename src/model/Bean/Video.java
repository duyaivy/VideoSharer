package model.Bean;

import java.sql.Timestamp;

public class Video {
    private int videoId;
    private int authorId;
    private String title;
    private String description;
    private String img;
    private Timestamp createAt;
    private String status; // pending, processing, ready, failed
    private String path;
    private long view;
    private String authorName;

    public Video() {
    }

    // Constructor đầy đủ
    public Video(int videoId, int authorId, String title, String description,
            String img, Timestamp createAt, String status, String path, long view) {
        this.videoId = videoId;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.img = img;
        this.createAt = createAt;
        this.status = status;
        this.path = path;
        this.view = view;
    }

    public Video(int authorId, String title, String description, String img,
            String status, String path) {
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.img = img;
        this.status = status;
        this.path = path;
        this.view = 0;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getView() {
        return view;
    }

    public void setView(long view) {
        this.view = view;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    // Method tiện ích
    public void incrementView() {
        this.view++;
    }

    public boolean isPending() {
        return "pending".equals(this.status);
    }

    public boolean isProcessing() {
        return "processing".equals(this.status);
    }

    public boolean isReady() {
        return "ready".equals(this.status);
    }

    public boolean isFailed() {
        return "failed".equals(this.status);
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoId=" + videoId +
                ", authorId=" + authorId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", view=" + view +
                ", createAt=" + createAt +
                '}';
    }
}