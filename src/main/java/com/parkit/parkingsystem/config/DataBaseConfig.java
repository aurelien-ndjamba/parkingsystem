package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

import java.util.Properties;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    public Connection getConnection() throws ClassNotFoundException,  SQLException, SecurityException {
    	FileInputStream fileInputStream = null;
    	
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("jasypt"); // could be got from web, env variable...
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setIvGenerator(new RandomIvGenerator());
        
        Properties props = new EncryptableProperties(encryptor);
        
    	String datasourceDriver = props.getProperty("datasource.driver");
        String datasourceUrl = props.getProperty("datasource.url");
        String datasourceUsername = props.getProperty("datasource.username");
        String datasourcePassword = props.getProperty("datasource.password");
        String datasourcePath = props.getProperty("datasource.Path");
        
        try {
        fileInputStream = new FileInputStream(datasourcePath);
        props.load(fileInputStream);
        
        logger.info("Create DB connection");
        Class.forName(datasourceDriver);
        
    	} catch(IOException ex) {
    		logger.error("Database connection error",ex);
    	} finally{
    		try {
    			if (fileInputStream != null)
        			fileInputStream.close();
    		} catch (IOException ex) {
    			logger.error("Database connection error",ex);
    		}
    		return DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword);	
    	}
        
        
        
//        		"jdbc:mysql://localhost:3306/prod?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC","root","rootroot");
    }

    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
