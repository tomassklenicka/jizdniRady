package com.example.jizdnirady.services;

import org.neo4j.configuration.Config;
import org.neo4j.configuration.GraphDatabaseSettings;
import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory.DEFAULT_DATABASE_NAME;

@Service
public class MyNeo4jDbService {

    private static String directory;
    private static File databaseDirectory;
    private static DatabaseManagementService managementService;
    private static  GraphDatabaseService graphDb;
    private String dbName;

    private static Logger logger = LoggerFactory.getLogger(MyNeo4jDbService.class);


    public MyNeo4jDbService(
              @Value("${myNeo4jDbService.db.directory}") String directory
            , @Value("${myNeo4jDbService.db.name}") String dbName) {
        this.directory = directory;
        databaseDirectory = new File(directory);
        this.dbName = dbName;
    }


    private static void registerShutdownHook(final DatabaseManagementService managementService) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                managementService.shutdown();
            }
        });
    }


    public void startDB() {
        logger.info("starting neo4j Db");
        managementService = new DatabaseManagementServiceBuilder(databaseDirectory)
                .setConfig(GraphDatabaseSettings.pagecache_memory, "1024M")
                .setConfig( BoltConnector.enabled, true )
                .setConfig( BoltConnector.listen_address, new SocketAddress( "localhost", 7687 ) )
                //.setConfig( GraphDatabaseSettings.string_block_size, 60 )
                //.setConfig( GraphDatabaseSettings.array_block_size, 300 )
                .build();
        graphDb = managementService.database(dbName);
        registerShutdownHook(managementService);
    }

    public enum RelTypes implements RelationshipType
    {
        PRESTUP,
        PATRI_K_TRIPU,
        POKRACUJE_DO,
        JEZDI_V_DEN
    }

    public enum Labels implements Label{
        TripStop
    }

    public DatabaseManagementService getManagementService() {
        return managementService;
    }

    public void setManagementService(DatabaseManagementService managementService) {
        this.managementService = managementService;
    }

    public GraphDatabaseService getGraphDb() {
        return graphDb;
    }

    public void setGraphDb(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public File getDatabaseDirectory() {
        return databaseDirectory;
    }

    public void setDatabaseDirectory(File databaseDirectory) {
        this.databaseDirectory = databaseDirectory;
    }
}
