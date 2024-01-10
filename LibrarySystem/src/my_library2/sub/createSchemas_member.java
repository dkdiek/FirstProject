package my_library2.sub;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import my_library2.DB_Connect;

public class createSchemas_member {

	public static void main(String[] args) {
		
		DB_Connect.connectDB();
		
		Connection conn = DB_Connect.getConnection();
		
        Statement statement = null;

        String sql = "CREATE TABLE IF NOT EXISTS member ("
                + "member_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "id VARCHAR(255) NOT NULL,"
                + "pw VARCHAR(255) NOT NULL,"
                + "phone VARCHAR(255) NOT NULL,"
                + "create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "name VARCHAR(255) NOT NULL"
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
