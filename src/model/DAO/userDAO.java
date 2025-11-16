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

	public userDAO() {
		conn = (Connection) ConnectDatabase.getMySQLConnection();

	}

	public boolean login(User u) {
		try {

			if (conn == null)
				conn = ConnectDatabase.getMySQLConnection();

			String sql = "select * from admin where username=? and password =?";
			pstm = (PreparedStatement) conn.prepareStatement(sql);

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

}
