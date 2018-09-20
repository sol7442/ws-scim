package com.wession.scim.test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import net.minidev.json.JSONObject;

import java.sql.PreparedStatement;

public class DemoGW {
	
	private Connection con;
	private String DBuser;
	private String DBpasswd;
	private String Database;

	public DemoGW() {
		Database = "WessionIM2";
		DBuser = "imHR2";
		DBpasswd = "1234";
	}

	public Connection getConnection() {
		try {
			return DataSource_GW.getInstance().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://182.162.143.19:3306/" + Database + "?autoReconnect=true", DBuser, DBpasswd);
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return con;
		*/
		return null;
	}

	public void insertAccount(DemoGWBean scim_account) {
		String sql = " INSERT INTO WessionIM2.T_GWAccount (email, Name, title, telephone, deptname, companyID) VALUES (?, ?, ?, ?, ?, ?) ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			String email = scim_account.getEmail();
			String Name = scim_account.getName();
			String title = scim_account.getTitle();
			String telephone = scim_account.getTelephone();
			String deptname = scim_account.getDeptname();
			String companyID = scim_account.getCompanyID();
			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			pstmt.setString(2, Name);
			pstmt.setString(3, title);
			pstmt.setString(4, telephone);
			pstmt.setString(5, deptname);
			pstmt.setString(6, companyID);
			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
	}

	public void updateAccount(DemoGWBean scim_account) {

		String sql = " UPDATE WessionIM2.T_GWAccount SET title=?, telephone=?, deptname=? WHERE email=? ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			String email = scim_account.getEmail();
			String title = scim_account.getTitle();
			String telephone = scim_account.getTelephone();
			String deptname = scim_account.getDeptname();
			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, telephone);
			pstmt.setString(3, deptname);
			pstmt.setString(4, email);
			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
	}

	public void deleteAccount(String email) {
		String sql = "DELETE FROM  WessionIM2.T_GWAccount WHERE email=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
	}

	public DemoGWBean getAccount(String email) {
		DemoGWBean bean = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			String sql = " SELECT email, Name, title, telephone, deptname, companyID FROM WessionIM2.T_GWAccount WHERE email=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DemoGWBean();
				bean.setEmail(rs.getString("email"));
				bean.setName(rs.getString("Name"));
				bean.setTitle(rs.getString("title"));
				bean.setTelephone(rs.getString("telephone"));
				bean.setDeptname(rs.getString("deptname"));
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
			String sql = " SELECT id, Name, title, telephone, companyID FROM WessionIM2.T_GWAccount WHERE companyID=?";
			pstmt = conn.prepareStatement(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
	}

	public ArrayList <DemoGWBean> getAccountList() {
		ArrayList <DemoGWBean> arry = new ArrayList <DemoGWBean> ();
		DemoGWBean bean = null;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			String sql = " SELECT email, Name, title, telephone, deptname, companyID FROM WessionIM2.T_GWAccount ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				bean = new DemoGWBean();
				bean.setEmail(rs.getString("email"));
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

	public ArrayList <DemoGWBean> findGhostAccount(ArrayList <String> scim_member_list) {
		ArrayList <DemoGWBean> arry = getAccountList();
		if (arry == null) arry = new ArrayList<DemoGWBean>();
		Iterator itor = arry.iterator();
		while (itor.hasNext()) {
			DemoGWBean db_bean = (DemoGWBean) itor.next();
			String id = db_bean.getEmail();
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
