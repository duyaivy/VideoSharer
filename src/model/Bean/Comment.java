package model.Bean;

import java.sql.Timestamp;

/**
 * Comment Bean - Đại diện cho bảng comment trong database
 */
public class Comment {
    private int commentId;
    private int videoId;
    private int userId;
    private String message;
    private Timestamp createAt;

    // Constructor mặc định
    public Comment() {
    }

    // Constructor đầy đủ
    public Comment(int commentId, int videoId, int userId,
            String message, Timestamp createAt) {
        this.commentId = commentId;
        this.videoId = videoId;
        this.userId = userId;
        this.message = message;
        this.createAt = createAt;
    }

    // Constructor cho thêm comment mới (không có commentId)
    public Comment(int videoId, int userId, String message) {
        this.videoId = videoId;
        this.userId = userId;
        this.message = message;
    }

    // Getters và Setters
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    // Method tiện ích
    public boolean isEmpty() {
        return message == null || message.trim().isEmpty();
    }

    public int getMessageLength() {
        return message != null ? message.length() : 0;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", videoId=" + videoId +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}