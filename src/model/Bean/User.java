package model.Bean;

import java.sql.Timestamp;

/**
 * User Bean - Đại diện cho bảng user trong database
 */
public class User {
    private int id;
    private String name;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String passwordHash;
    private String email;

    // Constructor mặc định
    public User() {
    }

    // Constructor đầy đủ
    public User(int id, String name, Timestamp createAt, Timestamp updateAt,
            String passwordHash, String email) {
        this.id = id;
        this.name = name;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    // Constructor cho đăng ký user mới (không có id)
    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}