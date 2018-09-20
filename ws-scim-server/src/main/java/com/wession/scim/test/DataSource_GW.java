package com.wession.scim.test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSource_GW {
	private static DataSource_GW datasource;
    private BasicDataSource ds;

    private DataSource_GW() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("imHR2");
        ds.setPassword("1234");
        ds.setUrl("jdbc:mysql://182.162.143.19:3306/WessionIM2");
       
     // the settings below are optional -- dbcp can work with defaults
        ds.setMinIdle(5);
        ds.setMaxIdle(20);
        ds.setMaxOpenPreparedStatements(180);

    }

    public static DataSource_GW getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource_GW();
            return datasource;
        } else {
            return datasource;
        }
    }
    
    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
}
