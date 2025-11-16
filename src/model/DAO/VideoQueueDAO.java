package model.DAO;

import java.sql.*;

import model.Bean.VideoQueue;

public class VideoQueueDAO {
    Connection conn;
    
    public VideoQueueDAO() {
    	conn = (Connection) ConnectDatabase.getMySQLConnection();
    }
    
    
    public boolean addToQueue(int videoId) {
        String sql = "insert into video_queue (video_id) values (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, videoId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public VideoQueue getNextPendingTask() {
        String sql = "select * from video_queue where status = 'pending' ORDER BY create_at asc LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                VideoQueue queue = new VideoQueue();
                queue.setQueueId(rs.getInt("queue_id"));
                queue.setVideoId(rs.getInt("video_id"));
                queue.setStatus(rs.getString("status"));
                queue.setRetryCount(rs.getInt("retry_count"));
                queue.setCreateAt(rs.getTimestamp("create_at"));
                return queue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
   
    public boolean updateStatus(int queueId, String status) {
        String sql = "update video_queue set status = ? where queue_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, queueId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
  
    public boolean incrementCount(int queueId) {
        String sql = "update video_queue set retry_count = retry_count +1 where queue_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, queueId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
  
   
    
    
    public int countPendingTasks() {
        String sql = "SELECT COUNT(*) FROM video_queue WHERE status = 'pending'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}