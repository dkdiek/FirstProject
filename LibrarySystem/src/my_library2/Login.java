package my_library2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
	Scanner scanner = new Scanner(System.in);
	
	public int login() {
		String membershipID;
		String membershipPW;
		int member_id = -1;
		
		DB_Connect.connectDB(); 
		
		
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
				 
				member_id =  resultSet.getInt("member_id");
				
			}else {
				System.out.println();
				System.out.println("회원 인증 실패. 메인 메뉴로 돌아갑니다.");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB_Connect.disconnectDB();
		}
		
		// 로그인 성공시 member_id return / 실패시 null
		return member_id; 
	}
	

	
	public void loginMenu() {
		
		int member_id = login();
		int menu;
		
		while(-1 != member_id) {
			
			System.out.println();
			System.out.println("****로그인 메뉴****");
			System.out.println("1. 마이페이지");
			System.out.println("2. 도서조회");
			System.out.println("3. 대출관리");
			System.out.println("4. 로그아웃");
			System.out.print("메뉴를 선택하세요 : ");
			menu = scanner.nextInt();
			scanner.nextLine();
			
			if(menu == 1) {
				mypage(member_id);
			}else if(menu == 2) {
				new Menu().searchBook(member_id);
			}else if (menu == 3) {
				rentalManage(member_id);
			}else if (menu == 4) {
				System.out.println("로그아웃 되었습니다.");
				break;
			}else {
				
			}
			
		}
		
	}
	
	public void loginMenu(int member_id) {
		
		int menu;
		
		while(-1 != member_id) {
			
			System.out.println();
			System.out.println("****로그인 메뉴****");
			System.out.println("1. 마이페이지");
			System.out.println("2. 도서조회");
			System.out.println("3. 대출관리");
			System.out.println("4. 로그아웃");
			System.out.print("메뉴를 선택하세요 : ");
			menu = scanner.nextInt();
			scanner.nextLine();
			
			if(menu == 1) {
				mypage(member_id);
			}else if(menu == 2) {
				new Menu().searchBook(member_id);
			}else if (menu == 3) {
				rentalManage(member_id);
			}else if (menu == 4) {
				System.out.println("로그아웃 되었습니다.");
				break;
			}else {
				
			}
			
		}
		
	}
	
	public void mypage(int member_id) {
		
		int menu;
		boolean returnFlag = false;
		
		while(!returnFlag) {
			System.out.println();
			System.out.println("****마이페이지 메뉴****");
			System.out.println("1. 이전 페이지");
			System.out.println("2. 비밀번호 변경");
			//System.out.println("3. 연락처 변경");
			//System.out.println("4. 회원 탈퇴");
			System.out.println("메뉴를 선택하세요 : ");
			menu = scanner.nextInt();
			scanner.nextLine();
			
			if(menu == 1) {
				break;
			}else if (menu == 2) {
				String userPW;
				System.out.print("현재 비밀번호를 다시 입력하세요. : ");
				userPW = scanner.nextLine();
				
				// DB 연결
				DB_Connect.connectDB();
				
				// SQL 쿼리: 회원 정보 확인
				String getPW = "SELECT pw FROM member WHERE member_id = ?";
				
				try {
					PreparedStatement preparedStatement = DB_Connect.getConnection().prepareStatement(getPW);
					preparedStatement.setInt(1, member_id);
					ResultSet resultSet = preparedStatement.executeQuery();
					
					if (resultSet.next()) {
						if(resultSet.getString("pw").equals(userPW)) {
							String newPw;
							String newPw2;
							
							System.out.print("변경할 비밀번호 입력 : ");
							newPw = scanner.nextLine();
							System.out.print("비밀번호 확인 : ");
							newPw2 = scanner.nextLine();
							
							if(newPw.equals(newPw2)) {
								String pwChangeQuery = "UPDATE member set pw = ? where member_id = ?";
								
								PreparedStatement changePwStatement = DB_Connect.getConnection().prepareStatement(pwChangeQuery);
								changePwStatement.setString(1, newPw);
								changePwStatement.setInt(2, member_id);
								
								int rowsAffected = changePwStatement.executeUpdate();
								
								if(rowsAffected > 0 ) {
									System.out.println();
									System.out.println("비밀번호가 변경되었습니다.");
								}else{
									System.out.println();
									System.out.println("오류가 발생했습니다. 다시 시도해주세요.");
								}
							}else {
								System.out.println("비밀번호가 일치하지 않습니다. 다시 시도해주세요.");
							}
							
						}
						
					}else {
						System.out.println("잘못 입력하셨습니다. 다시 시도해주세요.");
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					DB_Connect.disconnectDB();
				}
				
				
			}else if (menu == 3) {
				
			}else if (menu == 4){
				
			}else {
				
			}
		}
		
	}
	
	public void rentalManage(int member_id) {
		int menu;
		
		System.out.println();
		System.out.println("****대출관리 메뉴****");
		System.out.println("1. 이전 페이지");
		System.out.println("2. 대출 목록 조회 ");
		System.out.println("3. 도서 반납 ");
		//System.out.println("3. 대출 기록 조회 ");
		System.out.println("메뉴를 선택하세요 : ");
		menu = scanner.nextInt();
		scanner.nextLine();
		
		if(menu == 1) {
			return;
		}else if (menu == 2) {
			
			try {
				
				// DB 연결
				DB_Connect.connectDB();
				
				// 대출 목록 출력 
				String getRentalListQuery = "SELECT * FROM rental, books "
										+ "where member_id = ? and rental.book_id = books.book_id";
				PreparedStatement getRentalListStatement = DB_Connect.getConnection().prepareStatement(getRentalListQuery);
				getRentalListStatement.setInt(1,member_id);
				
				ResultSet rentalList = getRentalListStatement.executeQuery();
				
				while(rentalList.next()) {
					System.out.println("책 번호: " + rentalList.getInt("book_id") 
					+ " | 책 제목: " + rentalList.getString("title") 
					+ " | 대출일 : " + rentalList.getString("rental_date") 
					+ " | 반납예정일 : " + rentalList.getString("rental_due_date"));
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				DB_Connect.disconnectDB();
			}
			
		}else if (menu == 3) {
			int bookNum;
			
			boolean repeat = true;
			while(repeat) {
				try {
					
					// DB 연결
					DB_Connect.connectDB();
					
					// 대출 목록 출력 
					String getRentalListQuery = "SELECT * FROM rental, books "
											+ "where member_id = ? and rental.book_id = books.book_id";
					PreparedStatement getRentalListStatement = DB_Connect.getConnection().prepareStatement(getRentalListQuery);
					getRentalListStatement.setInt(1,member_id);
					
					ResultSet rentalList = getRentalListStatement.executeQuery();
					
					while(rentalList.next()) {
						System.out.println("책 번호: " + rentalList.getInt("book_id") 
						+ " | 책 제목: " + rentalList.getString("title") 
						+ " | 대출일 : " + rentalList.getString("rental_date") 
						+ " | 반납예정일 : " + rentalList.getString("rental_due_date"));
					}
					
					
					System.out.print("반납할 도서 번호를 입력하세요. : ");
					bookNum = scanner.nextInt();
					scanner.nextLine();
					
					// rental 테이블에서 해당 member_id(회원 id로 불러옴)과 입력한 book_id를 가지는 row 삭제
					String returnBookQuery = "DELETE FROM rental WHERE member_id = ? AND book_id = ?";
					PreparedStatement returnBookStatement = DB_Connect.getConnection().prepareStatement(returnBookQuery);
					returnBookStatement.setInt(1, member_id);
					returnBookStatement.setInt(2, bookNum);

					int rowsAffected = returnBookStatement.executeUpdate();

					if (rowsAffected > 0) {
						System.out.println();
						System.out.println("도서가 반납되었습니다.");
					} else {
						System.out.println();
						System.out.println("반납 처리 중 오류가 발생했습니다. 입력한 정보를 확인하세요.");
					}
					
					System.out.println();
					System.out.println("1. 추가 반납");
					System.out.println("2. 이전페이지");
					System.out.print("메뉴를 선택해주세요. : ");
					int isDone = scanner.nextInt();
					
					if(isDone == 2) {
						repeat = false;
					}else {
						
					}
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					DB_Connect.disconnectDB();
				}
			}
			
			
			
		}else if (menu == 4){
			
		}else {
			
		}
		
	}
	
	
	
	
	
	
}