package my_library2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminClass {

	Scanner scanner = new Scanner(System.in);

	// 5. 관리자 모드 [완료]
	public void manageAdmin() {
		String adminID;
		String adminPW;

		System.out.println();
		System.out.println("*** 일산 도서관 관리자 모드 메뉴 ***");
		System.out.println();
		System.out.printf("> 관리자 ID를 입력하시오 :");
		adminID = scanner.nextLine();
		System.out.printf("> 관리자 PW를 입력하시오 :");
		adminPW = scanner.nextLine();

		// DB 연결
		DB_Connect.connectDB();

		try {
			// SQL 쿼리: 관리자 정보 확인
			String query = "SELECT * FROM admin WHERE id = ? AND pw = ?";
			PreparedStatement preparedStatement = DB_Connect.getConnection().prepareStatement(query);
			preparedStatement.setString(1, adminID);
			preparedStatement.setString(2, adminPW);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				// 관리자 인증 성공
				System.out.println();
				System.out.println("관리자로 로그인하였습니다.");

				int choice;
				while (true) {
					System.out.println();
					System.out.println("1. 도서 조회");
					System.out.println("2. 도서 등록");
					System.out.println("3. 도서 삭제");
					System.out.println("4. 대출 현황 조회");
					System.out.println("5. 회원 조회");
					System.out.println("6. 회원 삭제");
					System.out.println("7. 관리자 등록");
					System.out.println("8. 관리자 삭제");
					System.out.println("9. 프로그램 종료");
					System.out.println("0. 메인 메뉴 돌아가기");
					System.out.printf("> 메뉴를 입력하시오 : ");

					choice = scanner.nextInt();
					scanner.nextLine(); // 개행 문자 처리

					if (choice == 1) {
						Menu menu = new Menu();
						menu.searchBook();
					} else if (choice == 2) {
						registerBook();
					} else if (choice == 3) {
						deleteBook();
					} else if (choice == 4) {
						checkRental();
					} else if (choice == 5) {
						searchMember();
					} else if (choice == 6) {
						deleteMemberShip();
					} else if (choice == 7) {
						registerAdmin();
					} else if (choice == 8) {
						deleteAdmin();
					} else if (choice == 9) {
						System.out.println();
						System.out.println("프로그램을 종료합니다.");
						System.exit(1);
					} else if (choice == 0) {
						return;
					} else {
						System.out.println();
						System.out.println("잘못된 선택입니다. 다시 선택하세요.");
					}
				}
			} else {
				System.out.println();
				System.out.println("관리자 인증 실패. 메인 메뉴로 돌아갑니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// DB 연결 해제
			DB_Connect.disconnectDB();
		}
	}

	// 5. 관리자 모드 - 2. 도서 등록 메소드 [완료/]
	public void registerBook() {
		String title;
		String writer;
		String company;
		String location;
		String isbn;
		PreparedStatement preparedStatement = null; // PreparedStatement 초기화
		int adminChoice;
		
		
		boolean retry = true;
		while(retry) {
			System.out.println();
			System.out.println("*** 일산 도서관 관리자 서적 등록 메뉴 ***");
			System.out.println();
			System.out.println("서적 제목을 입력하십시오:");
			title = scanner.nextLine();
			
			System.out.println("저자를 입력하십시오:");
			writer = scanner.nextLine();
			
			System.out.println("출판사를 입력하십시오:");
			company = scanner.nextLine();
			
			System.out.println("책 위치를 입력하십시오:");
			location = scanner.nextLine();
			
			System.out.println("ISBN을 입력하십시오:");
			isbn = scanner.nextLine();
			// MySQL 데이터베이스 연결
			DB_Connect.connectDB(); // 데이터베이스 연결 메소드 호출
			
			try {
				// SQL 쿼리: 서적 등록
				String query = "INSERT INTO books (title, writer, company, location,isbn) VALUES (?, ?, ?, ?, ?)";
				preparedStatement = DB_Connect.getConnection().prepareStatement(query);
				preparedStatement.setString(1, title);
				preparedStatement.setString(2, writer);
				preparedStatement.setString(3, company);
				preparedStatement.setString(4, location);
				preparedStatement.setString(5, isbn);
				
				int rowsInserted = preparedStatement.executeUpdate();
				
				if (rowsInserted > 0) {
					System.out.println();
					System.out.println("서적이 성공적으로 등록되었습니다.");
				} else {
					System.out.println();
					System.out.println("서적 등록에 실패했습니다.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				DB_Connect.disconnectDB(); // 데이터베이스 연결 해제 메소드 호출
			}
			
			System.out.println("1. 추가 등록");
			System.out.println("2. 나가기");
			System.out.printf(">메뉴를 입력하시오 :");
			adminChoice = scanner.nextInt();
			scanner.nextLine();
			
			if(1 == adminChoice) {
				retry = true;
			} else if(2 == adminChoice) {
				retry = false;
			} else {
				System.out.println("잘못된 메뉴입니다.");
				retry = false;
			}
			
		}
		
		


	}

	// 5. 관리자 모드 - 3. 도서 삭제 메소드 [완료/]
	public void deleteBook() {
		int bookDelete;
		PreparedStatement preparedStatement = null; // PreparedStatement 초기화
		
		boolean retry = true;
		while(retry) {
			try {
				System.out.println();
				System.out.println("*** 일산 도서관 관리자 서적 삭제 메뉴 ***");
				// 스캐너로 book_id 입력 받기
				System.out.println();
				System.out.println("> 삭제할 책의 book_id를 입력하세요 :");
				int bookId = scanner.nextInt();
				scanner.nextLine(); // 개행 문자 처리

				DB_Connect.connectDB();

				// SQL 쿼리: book_id와 일치하는 행 삭제
				String deleteQuery = "DELETE FROM books WHERE book_id = ?";

				preparedStatement = DB_Connect.getConnection().prepareStatement(deleteQuery);

				// book_id 값을 바인딩
				preparedStatement.setInt(1, bookId);

				// DELETE 쿼리 실행
				int rowsAffected = preparedStatement.executeUpdate();

				if (rowsAffected > 0) {
					System.out.println();
					System.out.println("도서가 성공적으로 삭제되었습니다.");
				} else {
					System.out.println();
					System.out.println("도서 삭제에 실패했습니다. book_id가 존재하지 않습니다.");
				}
			} catch (

			SQLException e) {
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				DB_Connect.disconnectDB();
			}
			
			System.out.println("1. 추가 삭제");
			System.out.println("2. 나가기 ");
			System.out.printf("> 메뉴를 입력하시오 :");
			bookDelete = scanner.nextInt();
			scanner.nextLine();
			
			if(1 == bookDelete) {
				retry = true;
			} else if (2 == bookDelete) {
				retry = false;
			} else {
				System.out.println("잘못된 메뉴번호 입니다.");
				retry = false;
			}
			
			
		}
		
		
	}

	// 5. 관리자 모드 - 4. 대출 현황 조회 메소드 [완료/]
	public void checkRental() {
		
		int memberRental;
		
		boolean retry = true;
		while(retry) {
			try {
				// DB 연결
				DB_Connect.connectDB();

				// SQL 쿼리: rental, member, books 테이블 조인하여 대출 현황 조회 (포함된 컬럼: rental_id,
				// rental_date, rental_due_date)
				String query = "SELECT rental.rental_id, member.id AS user_id, books.title, rental.rental_date, rental.rental_due_date "
						+ "FROM rental " + "INNER JOIN member ON rental.member_id = member.member_id "
						+ "INNER JOIN books ON rental.book_id = books.book_id";

				PreparedStatement preparedStatement = DB_Connect.getConnection().prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery();

				System.out.println("*** 일산 도서관 관리자 대출 현황 조회 메뉴 ***");
				System.out.println();
				System.out.println("대출 번호 | 사용자 ID | 도서 제목 | 대출 일자 | 반납 기한");
				while (resultSet.next()) {
					int rentalId = resultSet.getInt("rental_id");
					String userId = resultSet.getString("user_id");
					String bookTitle = resultSet.getString("title");
					String rentalDate = resultSet.getString("rental_date");
					String rentalDueDate = resultSet.getString("rental_due_date");
					System.out.println(
							rentalId + " | " + userId + " | " + bookTitle + " | " + rentalDate + " | " + rentalDueDate);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// DB 연결 해제
				DB_Connect.disconnectDB();
			}
			System.out.println("1. 추가 대출조회");
			System.out.println("2. 나가기");
			System.out.println(">메뉴를 입력하시오");
			
			memberRental = scanner.nextInt();
			scanner.nextLine();
			
			if(1 == memberRental) {
				retry = true;
			}else if (2 == memberRental) {
				retry = false;
			}else {
				System.out.println("잘못된 조회입니다.");
				retry = false;
			}
		}
		
		

	}

	// 5. 관리자 모드 - 5. 회원 조회 [완료/]
	public void searchMember() {
		String memberName;
		int memberSearch;
		
		
		boolean retry = true;
		while(retry) {
			System.out.println();
			System.out.println("*** 일산 도서관 관리자 회원 조회 메뉴 ***");
			System.out.println();
			System.out.println("> 조회할 회원 이름을 입력하시오 :");
			memberName = scanner.nextLine();

			// 입력 받은 값과 데이터베이스의 도서 제목 모두에서 공백을 제거
			String searchMember = memberName.replace(" ", "");

			// DB 연결
			DB_Connect.connectDB();

			// SQL 쿼리
			String query = "SELECT * FROM member " + "WHERE REPLACE(member.name, ' ', '') LIKE CONCAT('%', ?, '%')";
			PreparedStatement preparedStatement = null;

			try {
				preparedStatement = DB_Connect.getConnection().prepareStatement(query);

				// 제목에 와일드카드(%)를 추가하여 부분 일치 검색
				preparedStatement.setString(1, searchMember);

				ResultSet resultSet = preparedStatement.executeQuery();

				boolean found = false; // 검색 결과를 표시하는 플래그 추가

				while (resultSet.next()) {
					found = true; // 결과가 있음을 표시
					int member_id = resultSet.getInt("member_id");
					String id = resultSet.getString("id");
					String phone = resultSet.getString("phone");
					String create_date = resultSet.getString("create_date");
					String name = resultSet.getString("name");

					System.out.println("회원 이름: " + name + " | 회원 번호: " + member_id + " | 회원 ID: " + id + " | 회원 핸드폰: "
							+ phone + " | 가입일: " + create_date);
				}

				if (!found) {
					System.out.println();
					System.out.println("일치하는 회원을 찾을 수 없습니다.");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// DB 종료
				DB_Connect.disconnectDB();
			}
			System.out.println("1. 추가 회원조회");
			System.out.println("2. 나가기");
			System.out.printf("메뉴를 입력하시오");
			
			memberSearch = scanner.nextInt();
			scanner.nextLine();
			
			if(1 == memberSearch) {
				retry = true;
			} else if (2 == memberSearch) {
				retry = false;
			} else {
				System.out.println("잘못된 메뉴입니다");
				retry = false;
		}
			
		}
		
	}

	// 5. 관리자 모드 - 6. 회원 삭제 메소드 [완료/]
	public void deleteMemberShip() {
		
		PreparedStatement preparedStatement = null; // PreparedStatement 초기화
		int memberDelete;
		boolean retry = true;
		while(retry) {
			try {
				System.out.println();
				System.out.println("*** 일산 도서관 관리자 회원 삭제 메뉴 ***");
				// 스캐너로 book_id 입력 받기
				System.out.println();
				System.out.println("> 삭제할 회원의 id를 입력하세요 :");
				String memberId = scanner.nextLine();
				
				DB_Connect.connectDB();
				
				// SQL 쿼리: book_id와 일치하는 행 삭제
				String deleteMemberQuery = "DELETE FROM member WHERE id = ?";
				
				preparedStatement = DB_Connect.getConnection().prepareStatement(deleteMemberQuery);
				
				// book_id 값을 바인딩
				preparedStatement.setString(1, memberId);
				
				// DELETE 쿼리 실행
				int rowsMemberAffected = preparedStatement.executeUpdate();
				
				if (rowsMemberAffected > 0) {
					System.out.println();
					System.out.println("회원이 성공적으로 삭제되었습니다.");
				} else {
					System.out.println();
					System.out.println("회원 삭제에 실패했습니다.ID가 존재하지 않습니다.");
				}
			} catch (
					
					SQLException e) {
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					DB_Connect.disconnectDB();
				}
				System.out.println("1. 추가 회원삭제");
				System.out.println("2. 나가기");
				System.out.printf(">메뉴를 입력하시오 :");
				memberDelete = scanner.nextInt();
				scanner.nextLine();
				if(1 == memberDelete) {
					retry = true;
				}else if(2 == memberDelete) {
					retry = false;
				}else {
					System.out.println("잘못된 메뉴입니다.");
					retry = false;
				}
			
			}
			
		}
		


	}

	// 5. 관리자 모드 - 7. 관리자 등록 [완료/]
	public void registerAdmin() {
		
		String adminID;
		String adminPW;
		int registerAdmin;
		
		boolean retry = true;
		while(retry) {
			// DB 연결
			DB_Connect.connectDB();

			// 입력 받기
			System.out.println();
			System.out.println("*** 일산 도서관 관리자 등록 메뉴 ***");
			System.out.println();
			System.out.printf("> 관리자 ID를 입력하시오 :");
			adminID = scanner.nextLine();

			// 중복 체크 메소드를 통해 id 중복 확인
			if (isAdminIDUnique(adminID)) {
				System.out.printf("> 관리자 PW를 입력하시오 :");
				adminPW = scanner.nextLine();

				// DB 삽입
				String admin = "INSERT INTO admin (id, pw) VALUES (?, ?)";

				PreparedStatement preparedStatement;
				try {
					preparedStatement = DB_Connect.getConnection().prepareStatement(admin);

					preparedStatement.setString(1, adminID);
					preparedStatement.setString(2, adminPW);

					int rowsInserted = preparedStatement.executeUpdate();

					if (rowsInserted > 0) {
						System.out.println();
						System.out.println("관리자 추가를 성공했습니다");
					} else {
						System.out.println();
						System.out.println("문제가 발생하여 관리자 추가에 실패했습니다");
					}
					
					preparedStatement.close();

				} catch (SQLException e) {
					System.out.println();
					System.out.println("관리자 등록에 실패했습니다");
					e.printStackTrace();
				} finally {
					DB_Connect.disconnectDB();
				}
			} else {
				System.out.println();
				System.out.println("이미 존재하는 ID입니다. 다른 ID를 선택해주세요.");
			}
			System.out.println("1. 추가 관리자 등록");
			System.out.println("2. 나가기");
			System.out.printf(">메뉴를 입력하시오 :");
			
			registerAdmin = scanner.nextInt();
			scanner.nextLine();
			
			if(1 == registerAdmin) {
				retry = true;
			}else if(2 == registerAdmin) {
				retry = false;
			}else {
				retry = false;
			}
			

		}
		
	}

	// 5. 관리자 모드 - 8. 관리자 삭제 메소드 [완료/]
	public void deleteAdmin() {
		PreparedStatement preparedStatement = null; // PreparedStatement 초기화
		int adminDelete;
		boolean retry = true;
		while(retry) {
			try {
				System.out.println();
				System.out.println("*** 일산 도서관 관리자 삭제 메뉴 ***");
				// 스캐너로 book_id 입력 받기
				System.out.println();
				System.out.println("> 삭제할 관리자 id를 입력하세요 :");
				String adminId = scanner.nextLine();
				
				DB_Connect.connectDB();
				
				// SQL 쿼리: admin id와 일치하는 행 삭제
				String deleteAdminQuery = "DELETE FROM admin WHERE id = ?";
				
				preparedStatement = DB_Connect.getConnection().prepareStatement(deleteAdminQuery);
				
				preparedStatement.setString(1, adminId);
				
				// DELETE 쿼리 실행
				int rowsMemberAffected = preparedStatement.executeUpdate();
				
				if (rowsMemberAffected > 0) {
					System.out.println();
					System.out.println();
					System.out.println("관리자가 성공적으로 삭제되었습니다.");
				} else {
					System.out.println();
					System.out.println("관리자 삭제에 실패했습니다. ID가 존재하지 않습니다.");
				}
			} catch (
					
					SQLException e) {
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					DB_Connect.disconnectDB();
				}
			}
			System.out.println("1. 추가 관리자 삭제");
			System.out.println("2. 나가기");
			System.out.printf(">메뉴를 입력하시오 :");
			
			adminDelete = scanner.nextInt();
			scanner.nextLine();
			
			if(1 == adminDelete) {
				retry = true;
			}else if (2 == adminDelete) {
				retry = false;
			}else {
				System.out.println("잘못된 메뉴입니다.");
				retry = false;
			}
			
		}
		
		

	}

	// 5. 관리자 모드 - 관리자 id 중복 체크 [완료]
	private boolean isAdminIDUnique(String adminID) {
		String query = "SELECT id FROM admin WHERE id = ?";
		try {
			PreparedStatement preparedStatement = DB_Connect.getConnection().prepareStatement(query);
			preparedStatement.setString(1, adminID);
			ResultSet resultSet = preparedStatement.executeQuery();

			// If there is a result, it means the ID already exists in the database
			if (resultSet.next()) {
				preparedStatement.close();
				return false;
			}

			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}
}
