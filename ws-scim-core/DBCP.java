package com.wowsanta.scim.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class DBCP {

	private String poolName;
	
	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValiationQuery() {
		return valiationQuery;
	}

	public void setValiationQuery(String valiationQuery) {
		this.valiationQuery = valiationQuery;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	private String driverName;
	private String jdbcUrl;
	private String userName;
	private String password;
	private String valiationQuery = "select 1";
	private long timeBetweenEvictionRunsMillis = 1000L*60*1;
	private boolean testWhileIdle = true;
	private int minIdle = 5;
	private int maxTotal = 50;
	
	public static void close(Connection con, PreparedStatement pstmt, ResultSet rs){
        try{
            try{
                if(rs != null){rs.close();}
            }catch(Exception e){}
            
            try{
                if(pstmt != null){ pstmt.close();}
            }catch(Exception e){}

            try{
                if(con != null){con.close();}
            }catch(Exception e){}
            
        }catch(Exception e){}
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setUp() throws ClassNotFoundException, SQLException {
		Class.forName(this.driverName);
		
		ConnectionFactory cf = new DriverManagerConnectionFactory(this.jdbcUrl,this.userName,this.password);
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, null);
		pcf.setValidationQuery(this.valiationQuery);
		
		GenericObjectPoolConfig pcfg = new GenericObjectPoolConfig();
		pcfg.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);// 
		pcfg.setTestWhileIdle(this.testWhileIdle);
		pcfg.setMinIdle(this.minIdle);
		pcfg.setMaxTotal(this.maxTotal);
		
		GenericObjectPool<PoolableConnection> cp = new GenericObjectPool<PoolableConnection>(pcf,pcfg);
		pcf.setPool(cp);
		
		Class.forName(PoolingDriver.class.getCanonicalName());
		
		PoolingDriver driver = (PoolingDriver)DriverManager.getDriver("jdbc:apache:commons:dbcp:");
		driver.registerPool(this.poolName,cp);
	}
	
	public static DBCP load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		
		return gson.fromJson(reader,DBCP.class); 
	}
	
	public void save(String file_name) throws IOException {
	
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(
						new File(file_name)),StandardCharsets.UTF_8);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(this,writer);
		writer.flush();
		writer.close();
	}

	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}
