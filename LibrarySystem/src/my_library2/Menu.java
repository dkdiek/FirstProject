package my_library2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
	String membershipID;
	String membershipPW;
	Scanner scanner = new Scanner(System.in);

	// 메인 메뉴 [완료]
	public void showMenu() {
		int showMenuInput;

		try {
			System.out.println();
			System.out.println("*** 일산 도서관 대출 시스템 ***");
			System.out.println("1. 도서 조회");
			System.out.println("2. 로그인");
			System.out.println("3. 회원 가입");
			System.out.println("4. 관리자 모드");
			System.out.println("5. 프로그램 종료");
			System.out.printf("> 메뉴를 입력하시오 : ");
			showMenuInput = scanner.nextInt();
			scanner.nextLine();

			if (showMenuInput == 1) {
				searchBook();
			} else if (showMenuInput == 2) {
				new Login().loginMenu();
			} else if (showMenuInput == 3) {
				joinMembership();
			} else if (showMenuInput == 4) {
				AdminClass ac = new AdminClass();
				ac.manageAdmin();
			} else if (showMenuInput == 5) {
				System.out.println();
				System.out.println("프로그램을 종료합니다.");
				System.exit(1);
				// 메뉴에 없는 번호 누르는 경우
			} else {
				System.out.println("잘못된 메뉴 번호입니다.");
			}
			// 문자 입력 시 exception 잡기
		} catch (InputMismatchException e) {
			System.out.println("잘못된 입력입니다.");
			scanner.nextLine();
		}

	}

	// 1. 도서 조회 [완료/]
	public void searchBook() {
		int userChoice;
		int bookScanner;

		boolean retry = true;
		while (retry) {
			try {
				System.out.println();
				System.out.println("*** 일산 도서관 서적 조회 메뉴 ***");
				System.out.println("1. 제목 검색");
				System.out.println("2. 저자 검색");
				System.out.println("3. 뒤로가기");
				System.out.printf("> 메뉴를 입력하시오 : ");
				bookScanner = scanner.nextInt();
				scanner.nextLine();

				if (bookScanner == 1) {
					searchBookTitle();
				} else if (bookScanner == 2) {
					searchBookWriter();
				} else if (bookScanner == 3) {
					break;
				} else {
					System.out.println("잘못된 메뉴입니다");
				}

				System.out.println("1. 추가 조회");
				System.out.println("2. 나가기");
				System.out.println("3. 도서 대출");
				System.out.printf("> 메뉴를 입력하시오 :");

				userChoice = scanner.nextInt();
				scanner.nextLine();

				if (userChoice == 1) {
					retry = true;
				} else if (userChoice == 2) {
					retry = false;
				} else if (userChoice == 3) {
					int memberId = new Login().login();
					rentBook(memberId);
					new Login().loginMenu(memberId);
					retry = false;
				} else {
					System.out.println("잘못된 메뉴입니다");
					retry = false;
				}
			} catch (InputMismatchException e) {
				// 입력 오류 처리
				System.out.println("입력 오류가 발생했습니다. 숫자를 입력하세요.");
				scanner.nextLine(); // 버퍼 비우기
			} catch (Exception e) {
				// 다른 예외 처리
				System.out.println("오류가 발생했습니다: " + e.getMessage());
			}
		}
	}

	public void searchBook(int member_id) {

		int userChoice;
		int bookScannar;

		boolean retry = true;
		while (retry) {
			System.out.println();
			System.out.println("*** 일산 도서관 서적 조회 메뉴 ***");
			System.out.println("1. 제목 검색");
			System.out.println("2. 저자 검색");
			System.out.println("3. 뒤로가기");
			System.out.printf("> 메뉴를 입력하시오 : ");
			bookScannar = scanner.nextInt();
			scanner.nextLine();

			if (bookScannar == 1) {
				searchBookTitle();
			}
			if (bookScannar == 2) {
				searchBookWriter();
			}
			if (bookScannar == 3) {
				break;
			}
			System.out.println("1. 추가 조회");
			System.out.println("2. 나가기");
			System.out.println("3. 도서 대출");
			System.out.printf(">메뉴를 입력하시오 :");

			userChoice = scanner.nextInt();
			scanner.nextLine();

			if (1 == userChoice) {
				retry = true;
			} else if (2 == userChoice) {
				retry = false;
			} else if (3 == userChoice) {
				rentBook(member_id);
			} else {
				System.out.println("잘못된 메뉴입니다");
				retry = false;
			}

		}

	}

	// 1. - 1. 도서 제목 조회 [완료]
	public void searchBookTitle() {
		String bookTitle;

		System.out.println();
		System.out.println("*** 일산 도서관 서적 제목 조회 메뉴 ***");
		System.out.println();
		System.out.printf("> 조회할 서적의 제목을 입력하시오 :");
		bookTitle = scanner.nextLine();

		// 입력 받은 값과 데이터베이스의 도서 제목 모두에서 공백을 제거
		String searchTitle = bookTitle.replace(" ", "");

		// DB 연결
		DB_Connect.connectDB();

		// SQL 쿼리
		String query = "SELECT books.*, rental.book_id AS rented FROM books "
				+ "LEFT JOIN rental ON books.book_id = rental.book_id AND rental.return_date IS NULL "
				+ "WHERE REPLACE(books.title, ' ', '') LIKE CONCAT('%', ?, '%')";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DB_Connect.getConnection().prepareStatement(query);

			// 제목에 와일드카드(%)를 추가하여 부분 일치 검색
			preparedStatement.setString(1, searchTitle);

			ResultSet resultSet = preparedStatement.executeQuery();

			boolean found = false; // 도서를 찾았는지 여부를 표시하는 플래그

			while (resultSet.next()) {
				found = true; // 도서를 찾음
				int book_id = resultSet.getInt("book_id");
				String title = resultSet.getString("title");
				String writer = resultSet.getString("writer");
				String company = resultSet.getString("company");
				String location;

				// 도서의 대출 정보를 확인 (DB의 rental.book_id가 null이 아닌 경우에 대출 중인 도서)
				if (resultSet.getObject("rented") != null) {
					location = "대출 불가";
				} else {
					location = resultSet.getString("location");
				}

				System.out.println("책 번호: " + book_id + " | 책 제목: " + title + " | 저자: " + writer + " | 출판사: " + company
						+ " | 책 위치: " + location);
			}

			if (!found) {
				System.out.println();
				System.out.println("해당 도서를 찾을 수 없습니다.");
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

	}

	// 1. - 2.도서 제목 조회 [저자]
	public void searchBookWriter() {

		String bookWriter;

		System.out.println();
		System.out.println("*** 일산 도서관 서적 저자 조회 메뉴 ***");
		System.out.println();
		System.out.printf("> 조회할 서적의 저자를 입력하시오 :");
		bookWriter = scanner.nextLine();

		// 입력 받은 값과 데이터베이스의 도서 저자 모두에서 공백을 제거
		String searchWriter = bookWriter.replace(" ", "");

		// DB 연결
		DB_Connect.connectDB();

		// SQL 쿼리
		String query = "SELECT books.*, rental.book_id AS rented FROM books "
				+ "LEFT JOIN rental ON books.book_id = rental.book_id AND rental.return_date IS NULL "
				+ "WHERE REPLACE(books.writer, ' ', '') LIKE CONCAT('%', ?, '%')";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DB_Connect.getConnection().prepareStatement(query);

			// 저자에 와일드카드(%)를 추가하여 부분 일치 검색
			preparedStatement.setString(1, searchWriter);

			ResultSet resultSet = preparedStatement.executeQuery();

			boolean found = false; // 도서를 찾았는지 여부를 표시하는 플래그

			System.out.println();
			while (resultSet.next()) {
				found = true; // 도서를 찾음
				int book_id = resultSet.getInt("book_id");
				String title = resultSet.getString("title");
				String writer = resultSet.getString("writer");
				String company = resultSet.getString("company");
				String location;

				// 도서의 대출 정보를 확인 (DB의 rental.book_id가 null이 아닌 경우에 대출 중인 도서)
				if (resultSet.getObject("rented") != null) {
					location = "대출 불가";
				} else {
					location = resultSet.getString("location");
				}

				System.out.println("책 번호: " + book_id + " | 책 제목: " + title + " | 저자: " + writer + " | 출판사: " + company
						+ " | 책 위치: " + location);
			}

			if (!found) {
				System.out.println();
				System.out.println("해당 도서를 찾을 수 없습니다.");
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

	}

	// 2. 도서 대출 [완료/]
	public void rentBook(int member_id) {
	    String bookID;
	    int userRental;
	    boolean retry = true;

	    while (retry) {
	        // DB 연결
	        DB_Connect.connectDB();

	        try {
	            // 1 - 대출 가능 수량 확인
	            // rental 테이블에서 member_id로 return_date가 null값이 몇 개인지 확인
	            String countRentedBooksQuery = "SELECT COUNT(*) FROM rental WHERE member_id = ? AND return_date IS NULL";
	            PreparedStatement countRentedBooksStatement = DB_Connect.getConnection().prepareStatement(countRentedBooksQuery);
	            countRentedBooksStatement.setInt(1, member_id);
	            ResultSet rentedBooksResult = countRentedBooksStatement.executeQuery();

	            if (rentedBooksResult.next()) {
	                int rentedBooksCount = rentedBooksResult.getInt(1);
	                // member_id로 return_date가 null인 컬럼이 5개 이상이면 대출 불가 안내
	                if (rentedBooksCount >= 5) {
	                    System.out.println("5권 이상 대출 중입니다. 더 이상 대출할 수 없습니다.");
	                } else {
	                    // 2 - 연체 유무 확인
	                    // return_due_date가 지났고 return_date가 null인 값이 있는지 확인
	                    String checkOverdueBooksQuery = "SELECT COUNT(*) FROM rental " +
	                            "WHERE member_id = ? AND return_date IS NULL AND rental_due_date < ?";
	                    PreparedStatement checkOverdueBooksStatement = DB_Connect.getConnection().prepareStatement(checkOverdueBooksQuery);
	                    checkOverdueBooksStatement.setInt(1, member_id);
	                    checkOverdueBooksStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
	                    ResultSet overdueBooksResult = checkOverdueBooksStatement.executeQuery();
	                    // 연체 유 - 대출 불가 안내
	                    if (overdueBooksResult.next()) {
	                        int overdueBooksCount = overdueBooksResult.getInt(1);
	                        if (overdueBooksCount > 0) {
	                            System.out.println("연체된 도서가 있어 대출이 불가합니다.");
	                        // 연체 무 - 대출 진행
	                        } else {
	                            System.out.println("대출하실 도서 번호를 입력하시오: ");
	                            bookID = scanner.nextLine();

	                            // 도서 대출 가능 여부 확인
	                            String checkBookAvailabilityQuery = "SELECT COUNT(*) FROM rental WHERE book_id = ? AND return_date IS NULL";
	                            PreparedStatement checkBookAvailabilityStatement = DB_Connect.getConnection().prepareStatement(checkBookAvailabilityQuery);
	                            checkBookAvailabilityStatement.setString(1, bookID);
	                            ResultSet bookAvailabilityResult = checkBookAvailabilityStatement.executeQuery();

	                            // 도서가 books 테이블에 존재하지 않는 경우
	                            String checkBookExistenceQuery = "SELECT COUNT(*) FROM books WHERE book_id = ?";
	                            PreparedStatement checkBookExistenceStatement = DB_Connect.getConnection().prepareStatement(checkBookExistenceQuery);
	                            checkBookExistenceStatement.setString(1, bookID);
	                            ResultSet bookExistenceResult = checkBookExistenceStatement.executeQuery();
	                            
	                            if (bookExistenceResult.next() && bookExistenceResult.getInt(1) == 0) {
	                                System.out.println("해당 도서가 존재하지 않습니다.");
	                            } else if (bookAvailabilityResult.next() && bookAvailabilityResult.getInt(1) > 0) {
	                                System.out.println("해당 도서는 이미 대출 중입니다.");
	                            } else {
	                                // 도서 대출 처리
	                                // rental_due_date를 현재 시각에서 7일 후로 설정
	                                LocalDateTime now = LocalDateTime.now();
	                                LocalDateTime dueDate = now.plusDays(7);

	                                // member_id 값을 이용하여 rental 테이블에 도서 대출 정보 삽입
	                                String insertRentalQuery = "INSERT INTO rental (member_id, book_id, rental_due_date) VALUES (?, ?, ?)";
	                                PreparedStatement insertRentalStatement = DB_Connect.getConnection().prepareStatement(insertRentalQuery);
	                                insertRentalStatement.setInt(1, member_id);
	                                insertRentalStatement.setString(2, bookID);
	                                insertRentalStatement.setTimestamp(3, Timestamp.valueOf(dueDate));

	                                // 인서트 후 추가 행 수를 rowsAffected에 담는다
	                                int rowsAffected = insertRentalStatement.executeUpdate();

	                                if (rowsAffected > 0) {
	                                    System.out.println("도서가 대출되었습니다.");
	                                } else {
	                                    System.out.println("대출 처리 중 오류가 발생했습니다.");
	                                }
	                            }
	                        }
	                    } else {
	                        System.out.println("연체 도서 확인 중 오류가 발생했습니다.");
	                    }
	                }
	            } else {
	                System.out.println("대출 가능 수량 확인 중 오류가 발생했습니다.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (InputMismatchException e) {
	            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
	            scanner.next(); // 버퍼 비우기
	        } finally {
	            DB_Connect.disconnectDB();
	        }

	        // 추가 대출 여부 확인
	        System.out.println();
	        System.out.println("1. 추가 대출");
	        System.out.println("2. 나가기");
	        System.out.printf("> 메뉴를 입력하시오: ");
	        userRental = scanner.nextInt();
	        scanner.nextLine();

	        if (userRental == 1) {
	            retry = true;
	        } else if (userRental == 2) {
	            retry = false;
	        } else {
	            System.out.println("잘못된 메뉴 번호입니다");
	        }
	    }
	}

	// 3. 도서 반납 [완료/]
	public void returnBook() {
		String membershipID;
		String membershipPW;
		String bookID;
		int userReturn;

		boolean retry = true;
		while (retry) {
			// DB 연결
			DB_Connect.connectDB();

			// 입력 받기
			System.out.println();
			System.out.println("*** 일산 도서관 도서 반납 메뉴 ***");
			System.out.println();
			System.out.printf("> ID를 입력하시오 :");
			membershipID = scanner.nextLine();
			System.out.printf("> PW를 입력하시오 :");
			membershipPW = scanner.nextLine();

			try {
				// SQL 쿼리: 회원 정보 확인
				String query = "SELECT * FROM member WHERE id = ? AND pw = ?";
				PreparedStatement preparedStatement = DB_Connect.getConnection().prepareStatement(query);
				preparedStatement.setString(1, membershipID);
				preparedStatement.setString(2, membershipPW);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					System.out.println();
					System.out.println("로그인 되었습니다.");

					// 대출 목록 출력
					String getRentalListQuery = "SELECT * FROM rental, books "
							+ "where member_id = (SELECT member_id FROM member WHERE id = ?) and rental.book_id = books.book_id";
					PreparedStatement getRentalListStatement = DB_Connect.getConnection()
							.prepareStatement(getRentalListQuery);
					getRentalListStatement.setString(1, membershipID);

					ResultSet rentalList = getRentalListStatement.executeQuery();

					while (rentalList.next()) {
						System.out.println(
								"책 번호: " + rentalList.getInt("book_id") + " | 책 제목: " + rentalList.getString("title")
								// + " | 저자: " + rentalList.getString("writer")
								// + " | 출판사: " + rentalList.getString("company")
										+ " | 대출일 : " + rentalList.getString("rental_date") + " | 반납예정일 : "
										+ rentalList.getString("rental_due_date"));
					}

					System.out.println("반납하실 도서 번호를 입력하시오 :");
					bookID = scanner.nextLine();

					// rental 테이블에서 해당 member_id(회원 id로 불러옴)과 입력한 book_id를 가지는 컬럼 삭제
					String returnBookQuery = "DELETE FROM rental WHERE member_id = (SELECT member_id FROM member WHERE id = ?) AND book_id = ?";
					PreparedStatement returnBookStatement = DB_Connect.getConnection()
							.prepareStatement(returnBookQuery);
					returnBookStatement.setString(1, membershipID);
					returnBookStatement.setString(2, bookID);

					int rowsAffected = returnBookStatement.executeUpdate();

					if (rowsAffected > 0) {
						System.out.println();
						System.out.println("도서가 반납되었습니다.");
					} else {
						System.out.println();
						System.out.println("반납 처리 중 오류가 발생했습니다. 입력한 정보를 확인하세요.");
					}
				} else {
					System.out.println();
					System.out.println("회원 인증 실패. 메인 메뉴로 돌아갑니다.");
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB_Connect.disconnectDB();
			}
			System.out.println("1. 추가 반납");
			System.out.println("2. 나가기");
			System.out.printf(">메뉴를 입력하시오 :");

			userReturn = scanner.nextInt();
			scanner.nextLine();

			if (1 == userReturn) {
				retry = true;
			} else if (2 == userReturn) {
				retry = false;
			} else {
				retry = false;
			}
		}

	}

	// 4. 회원 가입 [완료/]
	public void joinMembership() {
		String membershipID;
		String membershipPW;
		String membershipName;
		String membershipPhone;
		int userJoin;

		boolean retry = true;
		while (retry) {
			// DB 연결
			DB_Connect.connectDB();

			// 입력 받기
			System.out.println();
			System.out.println("*** 일산 도서관 회원 가입 메뉴 ***");
			System.out.println();
			System.out.printf("> ID를 입력하시오 :");
			membershipID = scanner.nextLine();

			// 중복 체크 메소드를 통해 id 중복 확인
			if (isMembershipIDUnique(membershipID)) {
				System.out.printf("> PW를 입력하시오 :");
				membershipPW = scanner.nextLine();

				System.out.printf("> 이름을 입력하시오 : ");
				membershipName = scanner.nextLine();

				System.out.printf("> 전화번호를 입력하시오 : ");
				membershipPhone = scanner.nextLine();

				// DB 삽입
				String member = "INSERT INTO member (id, pw, name, phone) VALUES (?, ?, ?, ?)";

				PreparedStatement preparedStatement;
				try {
					preparedStatement = DB_Connect.getConnection().prepareStatement(member);

					preparedStatement.setString(1, membershipID);
					preparedStatement.setString(2, membershipPW);
					preparedStatement.setString(3, membershipName);
					preparedStatement.setString(4, membershipPhone);

					int rowsInserted = preparedStatement.executeUpdate();

					if (rowsInserted > 0) {
						System.out.println();
						System.out.println("회원가입을 성공했습니다");
					} else {
						System.out.println();
						System.out.println("회원가입에 실패했습니다");
					}

					preparedStatement.close();

				} catch (SQLException e) {
					System.out.println();
					System.out.println("회원가입에 실패했습니다");
					e.printStackTrace();
				} finally {
					DB_Connect.disconnectDB();
				}
			} else {
				System.out.println();
				System.out.println("이미 존재하는 ID입니다. 다른 ID를 선택해주세요.");
			}
			System.out.println("1. 추가 생성");
			System.out.println("2. 나가기");
			System.out.printf(">메뉴를 입력하시오 :");

			userJoin = scanner.nextInt();
			scanner.nextLine();

			if (1 == userJoin) {
				retry = true;
			} else if (2 == userJoin) {
				retry = false;
			} else {
				retry = false;
			}

		}

	}

	// 5. 회원 가입 - id 중복 체크 [완료]
	private boolean isMembershipIDUnique(String membershipID) {
		String query = "SELECT id FROM member WHERE id = ?";
		try {
			PreparedStatement preparedStatement = DB_Connect.getConnection().prepareStatement(query);
			preparedStatement.setString(1, membershipID);
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
