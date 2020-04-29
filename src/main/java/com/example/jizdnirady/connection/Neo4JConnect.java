package com.example.jizdnirady.connection;




import org.neo4j.driver.internal.spi.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.sql.DriverManager;


public class Neo4JConnect {
    static String adress;
    static String password;
    static String user;

    @Inject
    public Neo4JConnect(String adress,
                        String password,
                        String user) {
        this.adress = adress;
        this.password = password;
        this.user = user;
    }

    private static Logger logger = LoggerFactory.getLogger(Neo4JConnect.class);

    Connection conn = null;

    public static Connection ConnectDB() {
        try {
            //System.out.println(adress + user + password);
            Connection conn = (Connection) DriverManager.getConnection(adress, user, password);
            logger.info("connected to neo4j instance");
            return conn;

        } catch (Exception e) {
            logger.error("error connecting to neo4j {}", e);
            return null;
        }
    }
}
