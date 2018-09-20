package com.wession.scim.agent.site;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.intf.AbstractBean;
import com.wession.scim.agent.site.intf.AbstractDBAdaptor;

import net.minidev.json.JSONObject;

public class DemoIMSAdpator extends AbstractDBAdaptor{

	public DemoIMSAdpator() {
		super();

//		_agent_db_id= "id";
//		_agent_db_ssoid= "companyID";
//		_agent_db_scimId= "scimID";
//		
//		_table_name = "WessionIM1.T_IMSAccount";
//		_table_fields = "id, Name, title, telephone, companyID, scimID";
//		_insert_fields = "id, Name, title, telephone, companyID, scimID";
	}
	
	// public DataMap getAccount(String agent_db_id)
	// public DataMap getAccountById(String externalId)
	// public DataMap getAccountByScimId(String scimid)
	// public ArrayList<DataMap> getAccountList()
	// public void insertAccount(DataMap abs_scim_account)
	// public void updateAccount(DataMap abs_scim_account, DataMap db_account)
	// public void deleteAccount(Object agent_db_id)
	// public void deleteAccount(DataMap del_user)
	// public ArrayList <DataMap> findGhostAccount(ArrayList <String> scim_member_list)
	
	public JSONObject dropOut(String agent_db_id) {
		String table_name = "";
		String dropout_field_name = "";
		String dropout_field_data = "";
		String userid_field_name = "";

		
		String sql = "update from " + table_name + " set " + dropout_field_name + "=? where " + userid_field_name + "=?" ;
		
		JSONObject ret = new JSONObject();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dropout_field_data);
			pstmt.setString(2, agent_db_id);
			pstmt.executeQuery();
			
			// 정상처리 되었다면
			ret.put("CODE", "00");
			ret.put("MESSAGE", "Update Success");
			
		} catch (SQLException sqe) {
			ret.put("CODE", "91");
			ret.put("MESSAGE", "SQL Exception " + sqe.getMessage());
			
		} catch (Exception e) {
			ret.put("CODE", "99");
			ret.put("MESSAGE", "Exception " + e.getMessage());
			
		} finally {
			try { pstmt.close();pstmt=null; } catch (Exception e) {}
			try { conn.close(); conn=null; } catch (Exception e) {}
		}
		
		return null;
	}
}
