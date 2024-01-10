package my_library2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connect {

    private static Connection conn = null;

    // DB 연결 메소드
    public static void connectDB() {
        try {
            // JDBC Driver 등록
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 연결하기
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/my_library", "root", "1234");
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() {
    	return conn;
    }

    // DB 연결 해제 메소드
    public static void disconnectDB() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 