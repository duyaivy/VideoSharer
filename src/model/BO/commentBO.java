package model.BO;

import java.util.ArrayList;

import model.Bean.Comment;
import model.DAO.commentDAO;



public class commentBO {
	commentDAO dao;
	static commentBO bo = null;

	public commentBO() {
		dao = new commentDAO();
	}

	public static commentBO getInstance() {
		if (bo == null)
			bo = new commentBO();
		return bo;
	}
	
	public Comment writeComment(int userID, int videoID, String message) {
		int id = dao.writeComment(videoID, message, userID);
		
		if(id==-1) {
			return null;
		}
		return dao.getCommentById(id);
	}
	public ArrayList<Comment> getCommentByVideoId(int vd_id, int page, int size) {
		return dao.getCommentByVideoId(vd_id, page, size);
	}
	
	public int getTotalPageByVideoId(int videoId, int size) {
        int totalComment = dao.getTotalCommentByVideoId(videoId);
        return (int) Math.ceil((double) totalComment / size);
    }
}