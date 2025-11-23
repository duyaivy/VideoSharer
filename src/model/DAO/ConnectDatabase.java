package model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import helpers.EnvLoader;

public class ConnectDatabase {
    
    public static Connection getMySQLConnection() {
        
//        String host = EnvLoader.get("DB_HOST", "localhost");
//        String port = EnvLoader.get("DB_PORT", "3306");
//        String dbName = EnvLoader.get("DB_NAME", "video_sharer");
//        String username = EnvLoader.get("DB_USER", "root");
//        String password = EnvLoader.get("DB_PASSWORD", "123456");
//        
        String host = "localhost";
        String port = "3306";
        String dbName = "video_sharer";
        String username = "root";
        String password = "123456";
        
       
        String dbURL = String.format(
            "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC",
            host, port, dbName
        );
        

        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");
            
          
            Connection conn = DriverManager.getConnection(dbURL, username, password);   
            System.out.println("ket noi thah cong");
            return conn;
            
        } catch (ClassNotFoundException e) {
           
            e.printStackTrace();
        } catch (SQLException e) {
           
            e.printStackTrace();
        }
        
        return null;
    }
    

}