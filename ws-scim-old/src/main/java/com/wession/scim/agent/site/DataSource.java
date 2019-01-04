package com.wession.scim.agent.site;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.wession.scim.controller.ServiceProviderConfig;

import net.minidev.json.JSONObject;

public class DataSource {
	private static DataSource datasource;
    private BasicDataSource ds;
    
    private DataSource() throws IOException, SQLException, PropertyVetoException {
    	ServiceProviderConfig conf = ServiceProviderConfig.getInstance();
    	JSONObject config = conf.getDBConfig();
    	JSONObject db_connect = (JSONObject) config.get("connect");
    	JSONObject db_pool = (JSONObject) config.get("pool");
    	
        ds = new BasicDataSource();
        System.out.println("jdbc-class=="+db_connect.getAsString("jdbc-class"));
        System.out.println("uid=="+db_connect.getAsString("uid"));
        System.out.println("passwd=="+db_connect.getAsString("passwd"));
        System.out.println("database=="+db_connect.getAsString("database"));
        System.out.println("jdbc=="+db_connect.getAsString("jdbc-url") + db_connect.getAsString("database"));
        
        ds.setDriverClassName(db_connect.getAsString("jdbc-class"));
        ds.setUsername(db_connect.getAsString("uid"));
        ds.setPassword(db_connect.getAsString("passwd"));
        ds.setUrl(db_connect.getAsString("jdbc-url") + db_connect.getAsString("database"));
       
        ds.setMinIdle((int) db_pool.getAsNumber("min"));
        ds.setMaxIdle((int) db_pool.getAsNumber("max"));
        ds.setMaxOpenPreparedStatements((int) db_pool.getAsNumber("openPstms"));

    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        } else {
            return datasource;
        }
    }
    
    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
}
