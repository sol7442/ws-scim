package com.wession.scim.test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.commons.dbcp2.BasicDataSource;

import com.wession.common.RESTClient;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class DBCPTest {
	
    private static DBCPTest     datasource;
    private BasicDataSource ds;

    private DBCPTest() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("imHR");
        ds.setPassword("1234");
        ds.setUrl("jdbc:mysql://182.162.143.19:3306/WessionIM");
       
     // the settings below are optional -- dbcp can work with defaults
        ds.setMinIdle(5);
        ds.setMaxIdle(20);
        ds.setMaxOpenPreparedStatements(180);

    }

    public static DBCPTest getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DBCPTest();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
    
    public static void main(String[] args) throws PropertyVetoException, SQLException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBCPTest.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from T_HR1");
             while (resultSet.next()) {
                 System.out.println("employeeid: " + resultSet.getString("id"));
                 System.out.println("employeename: " + resultSet.getString("Name"));
             }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        
        RESTClient client = new RESTClient();
        JSONObject json = client.get("http://localhost:5000/scim/v2.0/Users?attributes=externalId&count=2000");
        JSONArray Resources = (JSONArray) json.get("Resources");
        Iterator itor = Resources.iterator();
        while (itor.hasNext()) {
        	JSONObject user = (JSONObject) itor.next();
        	System.out.println(user.getAsString("id"));
        }
        

    }
}
