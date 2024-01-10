package my_library2.sub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class insertRandomData_member {
	// 테이블 데이터 삭제 후 AI 1로 변경, ALTER TABLE your_table_name AUTO_INCREMENT = 1;

	public static void main(String[] args) {
		String jdbcUrl = "jdbc:mysql://localhost:3306/my_library";
		String dbUsername = "root";
		String dbPassword = "1234";

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
			// 수동 커밋 모드로 전환
			connection.setAutoCommit(false); 
			// 생성 후 인서트 메소드
			generateAndInsertMembers(connection, 50);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void generateAndInsertMembers(Connection connection, int count) throws SQLException {
		Random random = new Random();
		String insertQuery = "INSERT INTO member (id, pw, phone, name) VALUES (?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

		String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
		String phoneNumberPrefix = "010";
		String[] firstNames = { "김", "이", "박", "최", "정", "강", "조", "윤", "임" };

		String[] customNames = { "가온", "가람", "다솜", "초롱", "나봄", "루리", "단비", "마루", "별하", "새론", "지예", "초은", "하람", "은정",
				"정미", "상호", "진이", "이솔", "다슬", "노을", "가람", "보미", "우솔", "찬솔", "힘찬", "지니" };

		for (int i = 0; i < count; i++) {
			String id = generateRandomString(chars, 8);
			String pw = generateRandomString(chars, 8);
			String phoneNumber = phoneNumberPrefix + generateRandomString("0123456789", 8);
			String name = firstNames[random.nextInt(firstNames.length)]
					+ customNames[random.nextInt(customNames.length)];

			preparedStatement.setString(1, id);
			preparedStatement.setString(2, pw);
			preparedStatement.setString(3, phoneNumber);
			preparedStatement.setString(4, name);
			preparedStatement.executeUpdate();
		}

		// 넣고 커밋
		connection.commit();
	}

	private static String generateRandomString(String characters, int length) {
		Random random = new Random();
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			stringBuilder.append(characters.charAt(randomIndex));
		}

		return stringBuilder.toString();
	}
}