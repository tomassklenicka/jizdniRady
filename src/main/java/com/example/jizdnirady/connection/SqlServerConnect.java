package com.example.jizdnirady.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;

@Service
public class SqlServerConnect {
    static String address;
    static String password;
    static String user;
    static String databaseName;

    Connection conn = null;
    private static Logger logger = LoggerFactory.getLogger(SqlServerConnect.class);

    @Inject
    public SqlServerConnect(@Value("${sqlServerConnect.adress}")String address,
                            @Value("${sqlServerConnect.password}")String password,
                            @Value("${sqlServerConnect.user}")String user,
                            @Value("${sqlServerConnect.databaseName}")String databaseName) {
        this.address = address;
        this.password = password;
        this.user = user;
        this.databaseName = databaseName;
    }

    public static Connection ConnectDB() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(address + ";" +databaseName+";"+user+";"+password);
            return conn;
        } catch (Exception e) {
            logger.error("error connecting to neo4j {}", e);
            return null;
        }
    }

}

