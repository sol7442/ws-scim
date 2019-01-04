package com.wession.scim.agent.site.intf;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;

import com.wession.common.WessionLog;
import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.DataSource;
import com.wession.scim.controller.ServiceProviderConfig;

import net.minidev.json.JSONObject;

public abstract class AbstractDBAdaptor { 
	
	protected static String _agent_db_id= "";
	protected static String _agent_db_ssoid= "";
	protected static String _agent_db_scimId= "";
	
	protected static String _table_name = "";
	protected static String _table_fields = "";
	protected static String _insert_fields = "";
	
	private static Logger systemLog;
	private static Logger processLog;
	private static Logger provisionLog;
	private static Logger schedulerLog;
	
	private static WessionLog wlog = new WessionLog();
	
	
	public AbstractDBAdaptor() {
    	ServiceProviderConfig conf = ServiceProviderConfig.getInstance();
    	JSONObject config = conf.getDBConfig();
    	JSONObject db_sync = (JSONObject) config.get("sync");
    	
    	_agent_db_id = db_sync.getAsString("app-id-field");
    	_agent_db_ssoid = db_sync.getAsString("sso-id-field");
    	_agent_db_scimId = db_sync.getAsString("scim-id-field");
    	
    	_table_name = db_sync.getAsString("table-name");
    	_table_fields = db_sync.getAsString("fields-name");
    	_insert_fields = db_sync.getAsString("insert-fields");
    	
		// Log Setting
		systemLog = wlog.getSystemLog();
		processLog = wlog.getProcessLog();
		provisionLog = wlog.getProvisionLog();
		schedulerLog = wlog.getScheduleLog();
    	
	}
	
	public static void writeLogger(String loggerName, String loggerLevel, String loggerData) {
		if ("system".equals(loggerName)) {
			if ("info".equals(loggerLevel)) {
				systemLog.info(loggerData);
			} else {
				systemLog.debug(loggerData);
			}
		} else if ("provision".equals(loggerName)) {
			if ("info".equals(loggerLevel)) {
				provisionLog.info(loggerData);
			} else {
				provisionLog.debug(loggerData);
			}
		}
	}
	
	protected Connection getConnection() {
		try {
			return DataSource.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DataMap getAccount(String agent_db_id) {
		String sql = " SELECT * FROM " + _table_name + " WHERE " + _agent_db_id + "=? ";
		return getAccount(agent_db_id, sql);
	}

	public DataMap getAccount(String agent_db_id, String sql) {
		DataMap user = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, agent_db_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = setHashmap(rs);
			}
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		} finally {
			try { rs.close();rs=null; } catch (Exception e) {}
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		return user;
	}

	public DataMap getAccountById(String externalId) {
		String sql = " SELECT * FROM " + _table_name + " WHERE " + _agent_db_ssoid + "=? ";
		DataMap user = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, externalId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = setHashmap(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
		return user;
		
	}
	
	public DataMap getAccountByScimId(String scimid) {
		String sql = " SELECT * FROM " + _table_name + " WHERE " + _agent_db_scimId + "=? ";
		DataMap user = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, scimid);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				user = setHashmap(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
		return user;
		
	}
	public ArrayList<String> getAccountIDList() {
		ArrayList<String> arry = new ArrayList<String>();
		ArrayList <DataMap> all_arry = getAccountList();
		if (all_arry != null) {
			for (DataMap data: all_arry) {
				String id = data.getAsString(_agent_db_id);
				if (id != null) arry.add(id);
			}
		}
		
		return arry.size() > 0 ? arry : null;
	}
	
	public ArrayList<DataMap> getAccountList() {
		String sql = " SELECT * FROM " + _table_name + " ";
		return getAccountList(sql);
	}
	
	public ArrayList<DataMap> getAccountList(String sql) {
		ArrayList <DataMap> arry = new ArrayList <DataMap> ();
		DataMap user = null;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				user = setHashmap(rs);
				arry.add(user);
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
	
	public void insertAccount(DataMap abs_scim_account) {
	
		String [] insert_field = _insert_fields.split(",");
		String insert_params = "";
		for (int i=0; i<insert_field.length; i++) {
			insert_params = insert_params + "?, ";
		}
		insert_params = insert_params.substring(0, insert_params.length()-2);
		
		String sql = " INSERT INTO " + _table_name + " (" + _insert_fields + ") VALUES ( " + insert_params + " ) ";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			
			for (int i=0; i<insert_field.length; i++) {
				setPstmt(i+1, pstmt, abs_scim_account, insert_field[i].trim());
			}

			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
	}

	public boolean updateAccount(DataMap abs_scim_account, DataMap db_account) {
		// 2개의 차이점을 찾아서 업데이트 처리함
		boolean isChanged = false;
		DataMap diff = diffDataMap(abs_scim_account, db_account);
		
		String sql_update_part = "";
		Set <String> keys = diff.keySet();
		for (String key: keys) {
			sql_update_part = sql_update_part + key + "=?, "; 
		}
		
		if (sql_update_part.length() > 2) {
			sql_update_part = sql_update_part.substring(0, sql_update_part.length()-2);
		} else {
			// 문제가 있음 - 변경된 것이 없다
			return isChanged;
		}
		
		String sql_update = " UPDATE " + _table_name + " SET " + sql_update_part + " WHERE " + _agent_db_id + "=? ";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql_update);
			
			int idx = 1;
			for (String key: keys) {
				pstmt.setObject(idx, diff.get(key));
				idx++;
			}
			pstmt.setObject(idx, db_account.get(_agent_db_id));
			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
		return isChanged;
	}
	
	public void deleteAccount(Object agent_db_id) {
		String sql = "DELETE FROM  " + _table_name + " WHERE " + _agent_db_id + "=? ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setObject(1, agent_db_id);
			pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
	}

	public void deleteAccount(DataMap del_user) {
		String id = del_user.getAsString(_agent_db_id);
		if (id != null)
			deleteAccount(id);
	}

	public ArrayList <DataMap> findGhostAccount(ArrayList <String> scim_member_list) {
		ArrayList <DataMap> arry = getAccountList();
		Iterator itor = arry.iterator();
		while (itor.hasNext()) {
			DataMap user_map = (DataMap) itor.next();
			String id = (String) user_map.get(_agent_db_id);
			if (scim_member_list.contains(id)) {
				itor.remove();
			}
		}
		return arry;
		
	}
	
	private DataMap setHashmap(ResultSet rs) {
		DataMap user = new DataMap();
		if (rs == null) return null;
		int cols;
		try {
			cols = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= cols; i++) {
				String field_name =  rs.getMetaData().getColumnLabel(i);
				Object rs_obj = rs.getObject(i);
				if (rs_obj != null) {
					user.put(field_name, rs.getObject(i));
//					System.out.println(field_name + ":" + rs.getObject(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (user.size() == 0) return null;
		return user;
	}

	private void setPstmt(int idx, PreparedStatement pstmt, DataMap scim_account, String field) throws SQLException {
		if (pstmt !=null)
			pstmt.setObject(idx, scim_account.get(field));
	}

	private DataMap diffDataMap(DataMap abs_scim_account, DataMap db_account) {
		DataMap diff = new DataMap();
		Set <String> keys = abs_scim_account.keySet();
		for (String key: keys) {
			Object scim = abs_scim_account.get(key);
			Object db = db_account.get(key);
			if (scim != null && !scim.equals(db) && !_agent_db_id.equals(key)) {
				diff.put(key, scim);
			}
		}
		return diff;
	}
}
