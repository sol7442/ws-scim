package com.wession.scim.test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.wession.common.DataSource;

public class DemoHR {
	private String DBuser;
	private String DBpasswd;
	private String Database;

	public DemoHR() {
		Database = "WessionIM";
		DBuser = "imHR";
		DBpasswd = "1234";
	}
	
	public static void main(String [] args) {
		DemoHR demo = new DemoHR();
		
		System.out.println(demo.getSCIMAttrName("hr_id"));
		System.out.println(demo.getDBFieldName("externalId"));
		
		System.out.println(demo.getAccount("WS001101"));
	}

	public Connection getConnection() {
		try {
			return DataSource.getInstance().getConnection();
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
		return null;
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
	}
	
	public DemoHRBean getAccount(String id) {
		ArrayList <DemoHRBean> list_one = getAccountList(id);
		if (list_one == null) {
			return null;
		} else {
			return list_one.get(0);
		}
	}

	public ArrayList<String> getAccountIDList() {
		ArrayList<String> arry = new ArrayList<String>();
		
		DemoHRBean bean = new DemoHRBean();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			
			String sql = "select id hr_id, Name name, HR.DeptCode deptcode, DEPT.DeptName deptname, title, ims ims_id, email, phone "
					+ " from T_HR1 HR, T_DeptInfo DEPT where HR.DeptCode = DEPT.DeptCode";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				arry.add(rs.getString("hr_id"));
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		} finally {
			try {rs.close();rs = null;} catch (Exception e) {}
			try {pstmt.close();pstmt = null;} catch (Exception e) {}
			try {conn.close();conn = null;} catch (Exception e) {}
		}
		
		return arry.size() > 0 ? arry : null;
	}
	
	public ArrayList<DemoHRBean> getAccountList() {
		return getAccountList(null);
	}

	private ArrayList <DemoHRBean> getAccountList(String id) {
		ArrayList <DemoHRBean> arry = new ArrayList <DemoHRBean> ();
		
		DemoHRBean bean = new DemoHRBean();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement  pstmt = null;
		try {
			conn = getConnection();
			
			String sql = "select id hr_id, Name name, HR.DeptCode deptcode, DEPT.DeptName deptname, title, ims ims_id, email, phone " +  
					 " from T_HR1 HR, T_DeptInfo DEPT where HR.DeptCode = DEPT.DeptCode";
			
			String sql_1 = "select id hr_id, Name name, HR.DeptCode deptcode, DEPT.DeptName deptname, title, ims ims_id, email, phone " +  
						 " from T_HR1 HR, T_DeptInfo DEPT where HR.DeptCode = DEPT.DeptCode and id = ?";
			
			if (id == null) {
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
			} else {
				pstmt = conn.prepareStatement(sql_1);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
			}
			
			while (rs.next()) {
				bean.setHr_id(rs.getString("hr_id"));
				bean.setUserName(rs.getString("name"));
				bean.setDeptCode(rs.getString("deptcode"));
				bean.setDeptName(rs.getString("deptname"));
				bean.setTitle(rs.getString("title"));
				bean.setIms_id(rs.getString("ims_id"));
				bean.setEmail(rs.getString("email"));
				bean.setPhone(rs.getString("phone"));
				
				arry.add(bean);
			}
			
			rs.close();
			pstmt.close();
			conn.close();

		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		} finally {
			try { rs.close();rs=null; } catch (Exception e) {}
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
		return arry.size()>0?arry:null;
	}
	
	private String getSCIMAttrName(String db_field) {
		if (db_field == null) return null;
		for (int i=0; i<scim_mapper.length; i++) {
			String scimattr = scim_mapper[i][0];
			String dbfield = scim_mapper[i][1];
			if (db_field.equals(dbfield)) {
				return scimattr;
			}
		}
		return null;
	}
	
	private String getDBFieldName(String scim_attr_name) {
		if (scim_attr_name == null) return null;
		for (int i=0; i<scim_mapper.length; i++) {
			String scimattr = scim_mapper[i][0];
			String dbfield = scim_mapper[i][1];
			if (scim_attr_name.equals(scimattr)) {
				return dbfield;
			}
		}
		return null;
	}
	
	private static String [][] scim_mapper = {
			{"externalId", "hr_id"}, 
			{"displayName", "name"}, 
			{"deptcode", "deptcode"},
			{"title", "title"},
			{"ims.value", "ims_id"},
			{"emails.value", "email"},
			{"phoneNumbers.value", "phone"}
	};
}
