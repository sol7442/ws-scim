package com.wession.scim.test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ResultSetToBean {
	public static void main(String [] args) throws SQLException, IOException, PropertyVetoException {
		System.out.println("Start");
		String email = "beauty@wession.com";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DataSource_GW.getInstance().getConnection();
			String sql = " SELECT email, Name, deptname, title, telephone, companyID FROM WessionIM2.T_GWAccount WHERE email=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();

			ArrayList keys = new ArrayList();
			keys.add("email");
			keys.add("Name");
			keys.add("deptname");
			keys.add("title");
			keys.add("telephone");
			keys.add("companyID");

			String insert_sql_inner = "";
			Iterator<String> itor =  keys.iterator();
			while (itor.hasNext()) {
				String key = itor.next();
				insert_sql_inner = insert_sql_inner + key + ", ";
			}
			System.out.println(insert_sql_inner.substring(0, insert_sql_inner.length()-2));
			
			while (rs.next()) {
				LinkedHashMap<String, Object> map = convert(rs);
				System.out.println(map.toString());

				itor =  map.keySet().iterator();		
				while (itor.hasNext()) {
					Object obj = map.get(itor.next());
					if (obj instanceof String) {
						System.out.println("String : " + obj);
					}
				}

				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { rs.close();rs=null; } catch (Exception e) {}
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
	}
	
	public static LinkedHashMap<String, Object> convert(ResultSet rs) throws JsonParseException, JsonMappingException, IOException, SQLException {
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    int cols = rs.getMetaData().getColumnCount();
		for (int i = 1; i <= cols; i++) {
			map.put(rs.getMetaData().getColumnLabel(i) , rs.getObject(rs.getMetaData().getColumnLabel(i)));
		}
	    return map;
	}
}
