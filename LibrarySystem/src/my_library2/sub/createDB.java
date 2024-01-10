package my_library2.sub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class createDB {

	public static void main(String[] args) {
		String jdbcUrl = "jdbc:mysql://localhost:3306/";
		String username = "root";
		String password = "1234";
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(jdbcUrl, username, password);
			String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS my_library";

			statement = connection.createStatement();

			statement.executeUpdate(createDatabaseSQL);
			System.out.println("데이터베이스가 성공적으로 생성되었습니다.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}