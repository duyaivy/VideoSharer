package model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectDatabase {

    public static Connection getMySQLConnection() {
        String dbURL = "jdbc:mysql://localhost:3306/video_sharer"
                     + "?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String password = "";

        try {
            // Driver CŨ
        	Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = (Connection) DriverManager.getConnection(dbURL, username, password);
            System.out.println("Ket noi thah cong");
            return conn;

        } catch (ClassNotFoundException e) {
            System.err.println("Loi");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Loi");
            System.err.println("Message: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }

        return null;
    }
}
