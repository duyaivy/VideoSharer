package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Bean.Video;

public class videoDAO {
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	PreparedStatement pstm = null;

	public videoDAO() {
		conn = (Connection) ConnectDatabase.getMySQLConnection();
	}

	public Video createVideo(int authorId, String title, String des) {
		Connection conn = null;
		String sql = "INSERT INTO video (author_id, title, description) VALUES (?, ?, ?)";
		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstm.setInt(1, authorId);
				pstm.setString(2, title);
				pstm.setString(3, des);

				int affected = pstm.executeUpdate();
				if (affected == 0) {
					return null;
				}
				rs = pstm.getGeneratedKeys();
				int newId = -1;
				if (rs.next()) {
					newId = rs.getInt(1);
				}
				return this.getVideoByID(newId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Video getVideoByID(int id) {
		Connection conn = null;
		String sql = "SELECT * FROM video WHERE video_id = ?";
		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = conn.prepareStatement(sql)) {
				pstm.setInt(1, id);
				rs = pstm.executeQuery();
				if (rs.next()) {
					Video video = new Video();
					video.setVideoId(rs.getInt("video_id"));
					video.setAuthorId(rs.getInt("author_id"));
					video.setPath(rs.getString("path"));
					video.setImg(rs.getString("img"));
					video.setTitle(rs.getString("title"));
					video.setDescription(rs.getString("description"));
					video.setCreateAt(rs.getTimestamp("create_at"));
					video.setStatus(rs.getString("status"));
					video.setView(rs.getLong("view"));
					return video;
				}
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean updateView(int videoId, long view) {
		String sql = "UPDATE video SET view = ? WHERE video_id = ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setLong(1, view);
			pstm.setInt(2, videoId);

			return pstm.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incrementView(int videoId) {
		String sql = "UPDATE video SET view = view + 1 WHERE video_id = ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setInt(1, videoId);

			return pstm.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateVideoPath(int videoId, String path, String img) {
		String sql = "UPDATE video SET path = ?, img = ? WHERE video_id = ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setString(1, path);
			pstm.setString(2, img);
			pstm.setInt(3, videoId);

			return pstm.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateVideoInfo(int videoId, String title, String description, String status) {
		String sql = "UPDATE video SET title = ?, description = ?, status = ? WHERE video_id = ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setString(1, title);
			pstm.setString(2, description);
			pstm.setString(3, status);
			pstm.setInt(4, videoId);

			return pstm.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateVideoStatus(int videoId, String status) {
		String sql = "UPDATE video SET status = ? WHERE video_id = ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setString(1, status);
			pstm.setInt(2, videoId);

			return pstm.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	public int countByAuthor(int authorId) {
		String sql = "SELECT COUNT(*) FROM video WHERE author_id = ? AND is_delete = 0";
		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setInt(1, authorId);
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	public List<Video> getVideosByAuthor(int authorId, int offset, int limit) {
		List<Video> list = new ArrayList<>();
		String sql = "SELECT * FROM video " + "WHERE author_id = ? AND is_delete = 0 " + "ORDER BY create_at DESC "
				+ "LIMIT ? OFFSET ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setInt(1, authorId);
			pstm.setInt(2, limit);
			pstm.setInt(3, offset);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					Video video = new Video();
					video.setVideoId(rs.getInt("video_id"));
					video.setAuthorId(rs.getInt("author_id"));
					video.setPath(rs.getString("path"));
					video.setImg(rs.getString("img"));
					video.setTitle(rs.getString("title"));
					video.setDescription(rs.getString("description"));
					video.setCreateAt(rs.getTimestamp("create_at"));
					video.setStatus(rs.getString("status"));
					video.setView(rs.getLong("view"));
					list.add(video);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public boolean updateVideoInfoBasic(int videoId, String title, String description) {
		String sql = "UPDATE video SET title = ?, description = ? WHERE video_id = ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setString(1, title);
			pstm.setString(2, description);
			pstm.setInt(3, videoId);

			return pstm.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Xóa mềm
	public boolean softDelete(int videoId, int authorId) {
		String sql = "UPDATE video SET is_delete = 1 WHERE video_id = ? AND author_id = ?";
		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setInt(1, videoId);
			pstm.setInt(2, authorId);
			return pstm.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public ArrayList<Video> getTrendingVideo(int page, int size) {
		ArrayList<Video> ls = new ArrayList<>();

		String sql = "SELECT * FROM video " + "WHERE is_delete = 0 " + "ORDER BY view DESC " + "LIMIT ? OFFSET ?";

		try (Connection conn = ConnectDatabase.getMySQLConnection();
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			int offset = (page - 1) * size;
			pstm.setInt(1, size);
			pstm.setInt(2, offset);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					Video video = new Video();
					video.setVideoId(rs.getInt("video_id"));
					video.setAuthorId(rs.getInt("author_id"));
					video.setPath(rs.getString("path"));
					video.setImg(rs.getString("img"));
					video.setTitle(rs.getString("title"));
					video.setDescription(rs.getString("description"));
					video.setCreateAt(rs.getTimestamp("create_at"));
					video.setStatus(rs.getString("status"));
					video.setView(rs.getLong("view"));

					if (!"done".equals(video.getStatus()))
						continue;

					ls.add(video);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ls;
		}
		return ls;
	}

	public ArrayList<Video> getLastestVideo(int limit, int page) {
	    ArrayList<Video> videos = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;

	    try {
	        conn = ConnectDatabase.getMySQLConnection();

	        String sql = "SELECT v.*, u.name AS author_name "
	                   + "FROM video v "
	                   + "JOIN user u ON v.author_id = u.id "
	                   + "WHERE v.status IN ('ready','done','pending','processing') "
	                   + "AND v.is_delete = 0 "
	                   + "ORDER BY v.create_at DESC "
	                   + "LIMIT ?, ?";  

	        int offset = (page - 1) * limit;
	        if (offset < 0) offset = 0;

	        pstm = conn.prepareStatement(sql);
	        pstm.setInt(2, offset);
	        pstm.setInt(1, limit);

	        rs = pstm.executeQuery();

	        while (rs.next()) {
	            Video video = new Video();
	            video.setVideoId(rs.getInt("video_id"));
	            video.setAuthorId(rs.getInt("author_id"));
	            video.setTitle(rs.getString("title"));
	            video.setDescription(rs.getString("description"));
	            video.setImg(rs.getString("img"));
	            video.setCreateAt(rs.getTimestamp("create_at"));
	            video.setStatus(rs.getString("status"));
	            video.setPath(rs.getString("path"));
	            video.setView(rs.getLong("view"));
	            video.setAuthorName(rs.getString("author_name"));

	            videos.add(video);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstm != null) pstm.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return videos;
	}
	public List<Video> searchVideos(String keyword) {
	    List<Video> videos = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
	    
	    try {
	        conn = ConnectDatabase.getMySQLConnection();
	        
	        String sql = "SELECT v.*, u.name as author_name " +
	                     "FROM video v " +
	                     "JOIN user u ON v.author_id = u.id " +
	                     "WHERE v.status = 'done' " +
	                     "AND (v.title LIKE ? OR v.description LIKE ? OR u.name LIKE ?) " +
	                     "ORDER BY v.create_at DESC " +
	                     "LIMIT 50";
	        
	        pstm = conn.prepareStatement(sql);
	        String searchPattern = "%" + keyword + "%";
	        pstm.setString(1, searchPattern);
	        pstm.setString(2, searchPattern);
	        pstm.setString(3, searchPattern);
	        
	        rs = pstm.executeQuery();
	        
	        while (rs.next()) {
	            Video video = new Video();
	            video.setVideoId(rs.getInt("video_id"));
	            video.setAuthorId(rs.getInt("author_id"));
	            video.setTitle(rs.getString("title"));
	            video.setDescription(rs.getString("description"));
	            video.setImg(rs.getString("img"));
	            video.setCreateAt(rs.getTimestamp("create_at"));
	            video.setStatus(rs.getString("status"));
	            video.setPath(rs.getString("path"));
	            video.setView(rs.getLong("view"));
	            video.setAuthorName(rs.getString("author_name"));
	            
	            videos.add(video);
	        }
	        
	    } catch (Exception e) {
	        System.err.println("❌ Error searching videos!");
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstm != null) pstm.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    return videos;
	}
}
