package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import model.Bean.User;

public class userDAO {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement pstm = null;

    // Constructor
    public userDAO() {
        conn = ConnectDatabase.getMySQLConnection();
    }

    // ===== METHOD CŨ - LOGIN (GIỮ NGUYÊN) =====
    public boolean login(User u) {
        try {
            if (conn == null)
                conn = ConnectDatabase.getMySQLConnection();

            String sql = "select * from user where email=? and password_hash=?";
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, u.getEmail());
            pstm.setString(2, u.getPasswordHash());

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (pstm != null) {
                    pstm.close();
                    pstm = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // ===== LẤY USER THEO EMAIL =====
    public User getUserByEmail(String email) {
        User user = null;
        try {
            if (conn == null)
                conn = ConnectDatabase.getMySQLConnection();

            String sql = "SELECT * FROM user WHERE email = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, email);

            rs = pstm.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreateAt(rs.getTimestamp("create_at"));
                user.setUpdateAt(rs.getTimestamp("update_at"));
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy user theo email!");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (pstm != null) {
                    pstm.close();
                    pstm = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    // ===== LẤY USER THEO ID =====
    public User getUserById(int id) {
        User user = null;
        try {
            if (conn == null)
                conn = ConnectDatabase.getMySQLConnection();

            String sql = "SELECT * FROM user WHERE id = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);

            rs = pstm.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreateAt(rs.getTimestamp("create_at"));
                user.setUpdateAt(rs.getTimestamp("update_at"));
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy user theo ID!");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (pstm != null) {
                    pstm.close();
                    pstm = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    // ===== TẠO USER MỚI (ĐĂNG KÝ) =====
    public boolean createUser(User user) {
        try {
            if (conn == null)
                conn = ConnectDatabase.getMySQLConnection();

            String sql = "INSERT INTO user (name, email, password_hash, create_at, update_at) " +
                         "VALUES (?, ?, ?, NOW(), NOW())";
            
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPasswordHash());

            int rows = pstm.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi tạo user mới!");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                    pstm = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ===== CẬP NHẬT USER =====
    public boolean updateUser(User user) {
        try {
            if (conn == null)
                conn = ConnectDatabase.getMySQLConnection();

            String sql = "UPDATE user SET name = ?, email = ?, update_at = NOW() WHERE id = ?";
            
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getEmail());
            pstm.setInt(3, user.getId());

            int rows = pstm.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật user!");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                    pstm = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ===== XÓA USER =====
    public boolean deleteUser(int id) {
        try {
            if (conn == null)
                conn = ConnectDatabase.getMySQLConnection();

            String sql = "DELETE FROM user WHERE id = ?";
            
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);

            int rows = pstm.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi xóa user!");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                    pstm = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ===== ĐÓNG KẾT NỐI =====
    public void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstm != null) {
                pstm.close();
                pstm = null;
            }
            if (st != null) {
                st.close();
                st = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}