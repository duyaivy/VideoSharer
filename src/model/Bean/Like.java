package model.Bean;

import java.sql.Timestamp;

/**
 * Like Bean - Đại diện cho bảng like trong database
 */
public class Like {
    private int likeId;
    private int videoId;
    private int userId;
    private String type; // like, dislike
    private Timestamp createAt;

  
    public Like() {
    }

    // Constructor đầy đủ
    public Like(int likeId, int videoId, int userId, String type, Timestamp createAt) {
        this.likeId = likeId;
        this.videoId = videoId;
        this.userId = userId;
        this.type = type;
        this.createAt = createAt;
    }

    // Constructor cho thêm like mới (không có likeId)
    public Like(int videoId, int userId, String type) {
        this.videoId = videoId;
        this.userId = userId;
        this.type = type;
    }

    // Getters và Setters
    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    // Method tiện ích
    public boolean isLike() {
        return "like".equals(this.type);
    }

    public boolean isDislike() {
        return "dislike".equals(this.type);
    }

    public void toggleType() {
        this.type = isLike() ? "dislike" : "like";
    }

    @Override
    public String toString() {
        return "Like{" +
                "likeId=" + likeId +
                ", videoId=" + videoId +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}