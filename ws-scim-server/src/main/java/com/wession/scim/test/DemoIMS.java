package com.wession.scim.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import net.minidev.json.JSONObject;

import java.sql.PreparedStatement;

public class DemoIMS {
	
	private Connection con;
	private String DBuser;
	private String DBpasswd;
	private String Database;

	public DemoIMS() {
		Database = "WessionIM1";
		DBuser = "imHR1";
		DBpasswd = "1234";
	}

	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://182.162.143.19:3306/" + Database, DBuser, DBpasswd);
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return con;
	}

	public void insertAccount(DemoIMSBean scim_account) {
		String sql = " INSERT INTO WessionIM1.T_IMSAccount (id, Name, title, telephone, companyID) VALUES (?, ?, ?, ?, ?) ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			String id = scim_account.getId();
			String Name = scim_account.getName();
			String title = scim_account.getTitle();
			String telephone = scim_account.getTelephone();
			String companyID = scim_account.getCompanyID();
			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, Name);
			pstmt.setString(3, title);
			pstmt.setString(4, telephone);
			pstmt.setString(5, companyID);
			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
	}

	public void updateAccount(DemoIMSBean scim_account) {

		String sql = " UPDATE WessionIM1.T_IMSAccount SET title=?, telephone=? WHERE id=? ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			String id = scim_account.getId();
			String title = scim_account.getTitle();
			String telephone = scim_account.getTelephone();
			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, telephone);
			pstmt.setString(3, id);
			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
	}

	public void deleteAccount(String id) {
		String sql = "DELETE FROM  WessionIM1.T_IMSAccount WHERE id=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
	}

	public DemoIMSBean getAccount(String id) {
		DemoIMSBean bean = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			String sql = " SELECT id, Name, title, telephone, companyID FROM WessionIM1.T_IMSAccount WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DemoIMSBean();
				bean.setId(rs.getString("id"));
				bean.setName(rs.getString("Name"));
				bean.setTitle(rs.getString("title"));
				bean.setTelephone(rs.getString("telephone"));
				bean.setCompanyID(rs.getString("companyID"));
				
				int cols = rs.getMetaData().getColumnCount();
//				for (int i = 1; i <= cols; i++) {
//					System.out.println(i + ")" + rs.getMetaData().getColumnLabel(i) + ":" + rs.getString(rs.getMetaData().getColumnLabel(i)));
//				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { rs.close();rs=null; } catch (Exception e) {}
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		return bean;
	}

	public void getAccountByExId(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			String sql = " SELECT id, Name, title, telephone, companyID FROM WessionIM1.T_IMSAccount WHERE companyID=?";
			pstmt = conn.prepareStatement(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
	}

	public ArrayList <DemoIMSBean> getAccountList() {
		ArrayList <DemoIMSBean> arry = new ArrayList <DemoIMSBean> ();
		DemoIMSBean bean = null;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			String sql = " SELECT id, Name, title, telephone, companyID FROM WessionIM1.T_IMSAccount ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				bean = new DemoIMSBean();
				bean.setId(rs.getString("id"));
				bean.setName(rs.getString("Name"));
				bean.setTitle(rs.getString("title"));
				bean.setTelephone(rs.getString("telephone"));
				bean.setCompanyID(rs.getString("companyID"));
				
				arry.add(bean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { rs.close();rs=null; } catch (Exception e) {}
			try { stmt.close();stmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
		return arry.size()>0?arry:null;
	}

	public ArrayList <DemoIMSBean> findGhostAccount(ArrayList <String> scim_member_list) {
		ArrayList <DemoIMSBean> arry = getAccountList();
		Iterator itor = arry.iterator();
		while (itor.hasNext()) {
			DemoIMSBean db_bean = (DemoIMSBean) itor.next();
			String id = db_bean.getId();
			if (scim_member_list.contains(id)) {
				itor.remove();
			}
		}
		return arry;
		
	}
	
	public JSONObject getAccountToJSON(String id) {
		return null;
	}

}
