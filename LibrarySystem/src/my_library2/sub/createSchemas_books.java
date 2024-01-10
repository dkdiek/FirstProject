package my_library2.sub;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import my_library2.DB_Connect;

public class createSchemas_books {

	public static void main(String[] args) {
		
		DB_Connect.connectDB();
		
		Connection conn = DB_Connect.getConnection();
		
        Statement statement = null;

        String sql = "CREATE TABLE IF NOT EXISTS books ("
                + "book_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "title VARCHAR(255) NOT NULL,"
                + "writer VARCHAR(255) NOT NULL,"
                + "company VARCHAR(255) NOT NULL,"
                + "isbn VARCHAR(255) NOT NULL,"
                + "location VARCHAR(255) NOT NULL,"
                + "add_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB";
		
        try {
			statement = conn.createStatement();
            statement.executeUpdate(sql); // 테이블 생성 쿼리 실행
            System.out.println("테이블이 성공적으로 생성되었습니다.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		DB_Connect.disconnectDB();
	}

}
