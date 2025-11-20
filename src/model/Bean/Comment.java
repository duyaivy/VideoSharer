package model.Bean;

import java.sql.Timestamp;

public class Comment {
    private int commentId;
    private int videoId;
    private int UserId;
    private String message;
    private Timestamp createAt;
    
    private String userName;

    private String userEmail;
    // Constructor mặc định
    public Comment() {
    }

    // Constructor đầy đủ
    public Comment(int commentId, int videoId, int userId,
            String message, Timestamp createAt) {
        this.commentId = commentId;
        this.videoId = videoId;
        this.UserId = userId;
        this.message = message;
        this.createAt = createAt;
    }

   
    public Comment(int videoId, int userId, String message) {
        this.videoId = videoId;
        this.UserId = userId;
        this.message = message;
    }

    
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
        return UserId;
    }

    public void setUserId(int userId) {
        this.UserId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String n) {
        this.userName = n;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String e) {
        this.userEmail = e;
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

    public boolean isEmpty() {
        return message == null || message.trim().isEmpty();
    }

    public int getMessageLength() {
        return message != null ? message.length() : 0;
    }

}