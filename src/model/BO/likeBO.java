package model.BO;

import model.DAO.likeDAO;

public class likeBO {
	likeDAO dao;
	static likeBO bo = null;

	public likeBO() {
		dao = new likeDAO();
	}

	public static likeBO getInstance() {
		if (bo == null)
			bo = new likeBO();
		return bo;
	}

	public int getLikeCountByVideoId(int id) {
		return dao.getLikeOrDisLikeCountByVideoId(id, "like");
	}

	public int getDisLikeCountByVideoId(int id) {
		return dao.getLikeOrDisLikeCountByVideoId(id, "dislike");
	}

	public boolean likeVideo(int vd_id, int user_id) {
		boolean check = dao.isUserLikeOrDislike(vd_id, "like", user_id);
		boolean checkLike = dao.isUserLikeOrDislike(vd_id, "dislike", user_id);
		if(checkLike) {
			dao.removeLikeOrDisLike(vd_id, "dislike", user_id);
		}
		if (!check) {
			return dao.changeStatusLike(vd_id, "like", user_id);
		}
		return false;
	}

	public boolean unLikeVideo(int vd_id, int user_id) {
		return dao.removeLikeOrDisLike(vd_id, "like", user_id);
	}

	public boolean disLikeVideo(int vd_id, int user_id) {
		boolean check = dao.isUserLikeOrDislike(vd_id, "dislike", user_id);
		boolean checkLike = dao.isUserLikeOrDislike(vd_id, "like", user_id);
		if(checkLike) {
			 dao.removeLikeOrDisLike(vd_id, "like", user_id);
		}
		if (!check) {
			return dao.changeStatusLike(vd_id, "dislike", user_id);

		}

		return false;
	}

	public boolean unDislikeVideo(int vd_id, int user_id) {
		return dao.removeLikeOrDisLike(vd_id, "dislike", user_id);
	}
}