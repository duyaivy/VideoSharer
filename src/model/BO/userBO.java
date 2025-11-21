package model.BO;

import model.Bean.User;
import model.DAO.userDAO;

public class userBO {
	userDAO dao;
	static userBO bo = null;

	public userBO() {
		dao = new userDAO();
	}

	public static userBO getInstance() {
		if (bo == null)
			bo = new userBO();
		return bo;
	}

	public User getUserByEmail(String email) {
		return dao.getUserByEmail(email);
	}

	public boolean createUser(User user) {
		return dao.createUser(user);
	}

	public boolean updateUser(User u) {
		return dao.updateUser(u);
	}

}