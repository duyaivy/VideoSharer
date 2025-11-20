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
	
	public boolean writeComment(int userID, int videoID, String message) {
		return dao.writeComment(videoID, message, userID);
	}
	public ArrayList<Comment> getCommentByVideoId(int vd_id, int page, int size) {
		return dao.getCommentByVideoId(vd_id, page, size);
	}
	
	public int getTotalPageByVideoId(int videoId, int size) {
        int totalComment = dao.getTotalCommentByVideoId(videoId);
        return (int) Math.ceil((double) totalComment / size);
    }
}