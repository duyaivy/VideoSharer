package model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class likeDAO {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	PreparedStatement pstm = null;

	public likeDAO() {
		conn = (Connection) ConnectDatabase.getMySQLConnection();

	}

	public int getLikeOrDisLikeCountByVideoId(int id, String type) {
		Connection conn = null;
		String sql = "select count(*) from `like` where video_id = ? and type = ?";
		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS)) {
				pstm.setInt(1, id);
				pstm.setString(2, type);
				rs = pstm.executeQuery();
				if (rs.next()) {
					return rs.getInt(1);
				}
				return 0;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean changeStatusLike(int video_id, String type, int user_id) {
		Connection conn = null;
		String sql = "insert into `like` ( `video_id`, `user_id`, `type`) VALUES ( ?,?,?)";

		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql)) {
				pstm.setInt(1, video_id);
				pstm.setInt(2, user_id);
				pstm.setString(3, type);

				return pstm.executeUpdate() > 0;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isUserLikeOrDislike(int video_id, String type, int user_id) {
		Connection conn = null;
		String sql = "select * from `like` where video_id = ? and type = ? and user_id = ?";

		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql)) {
				pstm.setInt(1, video_id);
				pstm.setInt(3, user_id);
				pstm.setString(2, type);

				rs = pstm.executeQuery();

				if (rs.next()) {
					return true;
				}
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeLikeOrDisLike(int video_id, String type, int user_id) {
		String sql = "delete from `like` where video_id = ? and type = ? and user_id = ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql)) {

			pstm.setInt(1, video_id);
			pstm.setString(2, type);
			pstm.setInt(3, user_id);

			return pstm.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}