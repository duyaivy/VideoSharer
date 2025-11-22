package model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

import model.Bean.Comment;


public class commentDAO {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	PreparedStatement pstm = null;

	public commentDAO() {
		conn = (Connection) ConnectDatabase.getMySQLConnection();

	}

	public ArrayList<Comment> getCommentByVideoId(int vd_id, int page, int size) {
		Connection conn = null;
		ArrayList<Comment> ls = new ArrayList<>();
		String sql = "select c.*, a.email, a.name  from comment c join `user` a on a.id = c.user_id where c.video_id = ? order by c.create_at DESC limit  ? offset  ?";
		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS)) {
				int offset = (page - 1) * size;

				pstm.setInt(1, vd_id);
				pstm.setInt(2, size);
				pstm.setInt(3, offset);

				try (ResultSet rs = pstm.executeQuery()) {
					while (rs.next()) {
						Comment c = new Comment();
						c.setCommentId(rs.getInt("comment_id"));
						c.setVideoId(rs.getInt("video_id"));
						c.setUserId(rs.getInt("user_id"));
						c.setMessage(rs.getString("message"));
						c.setCreateAt(rs.getTimestamp("create_at"));

						c.setUserEmail(rs.getString("email"));
						c.setUserName(rs.getString("name"));

						ls.add(c);
					}
				}

				return ls;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ls;
		}
	}

	public Comment getCommentById(int id) {
		Connection conn = null;

		String sql = "select c.*, a.email, a.name  from comment c join `user` a on a.id = c.user_id where c.comment_id = ?";
		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql)) {

				pstm.setInt(1, id);

				try (ResultSet rs = pstm.executeQuery()) {
					if (rs.next()) {
						Comment c = new Comment();
						c.setCommentId(rs.getInt("comment_id"));
						c.setVideoId(rs.getInt("video_id"));
						c.setUserId(rs.getInt("user_id"));
						c.setMessage(rs.getString("message"));
						c.setCreateAt(rs.getTimestamp("create_at"));

						c.setUserEmail(rs.getString("email"));
						c.setUserName(rs.getString("name"));
						return c;

					}
				}

				return null;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int writeComment(int video_id, String message, int user_id) {
		Connection conn = null;
		String sql = "INSERT INTO `comment` (`video_id`, `user_id`, `message`) VALUES (?,?,?)";

		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS)) {
				pstm.setInt(1, video_id);
				pstm.setInt(2, user_id);
				pstm.setString(3, message);

				int affectedRows = pstm.executeUpdate();

				if (affectedRows > 0) {

					rs = pstm.getGeneratedKeys();

					if (rs.next()) {
						return rs.getInt(1);
					}
				}
				return -1;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int getTotalCommentByVideoId(int videoId) {
		String sql = "SELECT COUNT(*) FROM comment WHERE video_id = ?";
		int total = 0;

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setInt(1, videoId);

			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					total = rs.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return total;
	}
}