package com.wession.scim.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class DemoAccount {
	private static final String FamilyNames = "김구이박정김이전유정홍천방김박이최최채황나남동라김박이장방성서안어주김박이장조표하강장우민강전";
	private static final String Name = "미마문면연안강승중용종희태준남순성민명준준철주섭선상리용성성기민일은준상한택은미주우신장철형수홍은창철수정대희연동준서훈영호영연호삼미숙강태";
	private static final String ABC = "ABCDEFGHIJKLMNOPQR"; 
	
	public static Connection getConnection() {
		String Database = "WessionIM";
		String DBuser = "imHR";
		String DBpasswd = "1234";
		
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://182.162.143.19:3306/" + Database, DBuser, DBpasswd);
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return con;
	}
	
	public static String getRandomName() {
		Random generator = new Random();
		int num1, num2, num3;
		num1 = generator.nextInt(FamilyNames.length());
		num2 = generator.nextInt(Name.length());
		num3 = generator.nextInt(Name.length());
		return (FamilyNames.charAt(num1) + "" + Name.charAt(num2) + Name.charAt(num3));
	}
	
	public static String getRandomDeptcode() {
		Random generator = new Random();
		int num1;
		num1 = generator.nextInt(dept.length);
		return dept[num1];
	}
	
	public static String getRandomTitle() {
		Random generator = new Random();
		int num1;
		num1 = generator.nextInt(title.length);
		return title[num1];
	}
	
	public static String getRandomPhone() {
		Random generator = new Random();
		int num1, num2, num3, num4, num5, num6;
		num1 = generator.nextInt(10);
		num2 = generator.nextInt(10);
		num3 = generator.nextInt(10);
		num4 = generator.nextInt(10);
		num5 = generator.nextInt(10);
		num6 = generator.nextInt(10);
		return num1 + "" + num2 + "-" + num3 + "" + num4 + "" + num5  + "" + num6;
	}
	
	public static String getRandomUserID() {
		Random generator = new Random();
		int num1, num2, num3, num4, num5, num6;
		num1 = generator.nextInt(10);
		num2 = generator.nextInt(10);
		num3 = generator.nextInt(10);
		num4 = generator.nextInt(10);
		num5 = generator.nextInt(1000);
		num6 = generator.nextInt(1000);
		String id = ((num1*num2+num4) % 10) + "" + num1 + "" +  ((num2*num3+num5) % 10) + num2 + "" +  ((num3*num1+num6) % 10) + num3 + String.format("%01d", num4); 
		//String id = ((num1*num2) % 10) + "" + num1 + "" +  ((num2*num3) % 10) + num2 + "" +  ((num3*num1) % 10) + num3 ;
		return "WS" + id;
	}
	
	private static final String [] dept = {"1001", "1302", "1301", "1101", "1102", "1003", "1002"};
	private static final String [] title = {"이사", "부장", "차장", "차장", "과장", "과장", "과장", "대리", "대리", "대리", "대리", "사원", "사원", "사원", "사원", "사원", "사원", "임시", "임시"};
	
	public static void main(String [] args) {
		int startId = 500001;
			String sql_insert = "insert into T_HR1(id, Name, DeptCode, Title, ims, email, phone) values (?, ?, ?, ?, ?, ?, ?)";
			String sql_select = "select count(id) from T_HR1 where id=?";
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			
			try {
				
				conn = getConnection();
				pstmt = conn.prepareStatement(sql_insert);
				pstmt2 = conn.prepareStatement(sql_select);
				
				for (int i=0; i<2000; i++) {
					
					pstmt.clearParameters();
					String name = getRandomName();
					String uid = getRandomUserID();
					String deptcode = getRandomDeptcode();
					String title = getRandomTitle();
					String ims = uid.replaceAll("WS", "BO");
					String email = uid + "@wession.com";
					String phone = getRandomPhone();
					
					if ("임시".equals(title)) {
						email = null;
						phone = null;
						if (i % 3 != 1) ims = null;
					}
					
					if ("이사".equals(title)) {
						if (i % 3 == 1) ims = null;
					}
					
					pstmt2.setString(1, uid);
					ResultSet rs = pstmt2.executeQuery();
					int count = 0;
					while (rs.next()) {
						count = rs.getInt(1);
					}
					if (count == 0) {
						pstmt.setString(1, uid);
						pstmt.setString(2, name);
						pstmt.setString(3, deptcode);
						pstmt.setString(4, title);
						pstmt.setString(5, ims);
						pstmt.setString(6, email);
						pstmt.setString(7, phone);
						pstmt.execute();
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { pstmt.close();pstmt=null; } catch (Exception e) {}
				try { conn.close(); conn=null; } catch (Exception e) {}
			}
			
		
	}
}
