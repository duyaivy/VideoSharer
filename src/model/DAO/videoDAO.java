package model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
		String sql = "INSERT INTO video (author_id, title, description) " + "VALUES (?, ?, ?)";
		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
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
		String sql = "select * from video where video_id= ?";
		try {
			conn = ConnectDatabase.getMySQLConnection();
			try (PreparedStatement pstm = (PreparedStatement) conn.prepareStatement(sql)) {
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
	         PreparedStatement pstm =(PreparedStatement) conn.prepareStatement(sql)) {

	        pstm.setLong(1, view);
	        pstm.setInt(2, videoId);

	        return pstm.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean updateVideoPath(int videoId, String path, String img) {
	    String sql = "UPDATE video SET path = ?, img = ? WHERE video_id = ?";

	    try (Connection conn = ConnectDatabase.getMySQLConnection();
	         PreparedStatement pstm =(PreparedStatement) conn.prepareStatement(sql)) {

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
	         PreparedStatement pstm = (PreparedStatement)conn.prepareStatement(sql)) {

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
	public boolean updateVideoStatus(int videoId,  String status) {
	    String sql = "UPDATE video SET status = ? WHERE video_id = ?";

	    try (Connection conn = ConnectDatabase.getMySQLConnection();
	         PreparedStatement pstm = (PreparedStatement)conn.prepareStatement(sql)) {

	       
	        pstm.setString(1, status);
	        pstm.setInt(2, videoId);

	        return pstm.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public List<Video> getLatestVideos(int limit) {
	    List<Video> videos = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
	    
	    try {
	        conn = ConnectDatabase.getMySQLConnection();
	        String sql = "SELECT v.*, u.name as author_name " +
	                     "FROM video v " +
	                     "JOIN user u ON v.author_id = u.id " +
	                     "WHERE v.status IN ('ready', 'done', 'pending', 'processing') " +  // ⭐ SỬA DÒNG NÀY
	                     "ORDER BY v.create_at DESC " +
	                     "LIMIT ?";
	        
	        System.out.println("✅ videoDAO: Executing query...");
	        pstm = conn.prepareStatement(sql);
	        pstm.setInt(1, limit);
	        rs = pstm.executeQuery();

	        int count = 0;
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
	            count++;
	        }
	        

	    } catch (Exception e) {
	        System.err.println("❌ videoDAO: ERROR!");
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
	// ===== LẤY VIDEO TRENDING (NHIỀU VIEW NHẤT) =====
	public List<Video> getTrendingVideos(int limit) {
	    List<Video> videos = new ArrayList<>();
	    try {
	        if (conn == null)
	            conn = ConnectDatabase.getMySQLConnection();

	        String sql = "SELECT v.*, u.name as author_name " +
	                     "FROM video v " +
	                     "JOIN user u ON v.author_id = u.id " +
	                     "WHERE v.IN ('ready', 'done', 'pending', 'processing') " +
	                     "ORDER BY v.view DESC " +
	                     "LIMIT ?";
	        
	        pstm = conn.prepareStatement(sql);
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
	        System.err.println("Lỗi khi lấy video trending!");
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstm != null) pstm.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return videos;
	}

	public void incrementView(int videoId) {
	    try {
	        if (conn == null)
	            conn = ConnectDatabase.getMySQLConnection();

	        String sql = "UPDATE video SET view = view + 1 WHERE video_id = ?";
	        pstm = conn.prepareStatement(sql);
	        pstm.setInt(1, videoId);
	        pstm.executeUpdate();

	    } catch (Exception e) {
	        System.err.println("Lỗi khi tăng view!");
	        e.printStackTrace();
	    } finally {
	        try {
	            if (pstm != null) pstm.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}


}
