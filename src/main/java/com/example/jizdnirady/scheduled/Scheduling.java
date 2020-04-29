package com.example.jizdnirady.scheduled;

import com.example.jizdnirady.entity.TripStop;
import com.example.jizdnirady.services.MyNeo4jDbService;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;

@Service
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
public class Scheduling {

    private static MyNeo4jDbService myNeo4jDbService = null;


    //VALUES for unzip method
    private static String source;
    private static String destination;

    //values for download method
    private static File folder;
    private static String url;

    private static Logger logger = LoggerFactory.getLogger(Scheduling.class);

    //values for Neo4JConnect
    static String SqlAdress;
    static String SqlPassword;
    static String SqlUser;

    static String neo4jSource;

    static String destinationTransferFile = "guide_transfers.txt";

    static final HashMap<String, String> mapaCypher = new HashMap<>();


    public Scheduling(
            @Value("${update.unzip.source}") String source
            , @Value("${update.unzip.destination}") String destination
            , @Value("${update.download.folder}") String folder
            , @Value("${update.download.website}") String website
            , @Value("${neo4jConnect.adress}") String SqlAdress
            , @Value("${neo4jConnect.password}") String password
            , @Value("${neo4jConnect.user}") String SqlUser
            , @Value("${neo4j.folder}") String neo4jSource,
            MyNeo4jDbService myNeo4jDbService) {
        this.source = source;
        this.destination = destination;
        this.folder = new File(folder);
        this.url = website;
        this.SqlAdress = SqlAdress;
        this.SqlPassword = password;
        this.SqlUser = SqlUser;
        this.neo4jSource = neo4jSource;
        this.myNeo4jDbService = myNeo4jDbService;
        mapaCypher.put("loadTripStop",
                "LOAD CSV WITH HEADERS " +
                        "FROM \"file:///D:/Onedrive/Plocha/jizdnirady/GTFS/stop_times.txt\" " +
                        "AS row FIELDTERMINATOR ',' " +
                        "CREATE (t:TripStop {stop_id: row.stop_id, trip_id: row.trip_id, arrival_time: row.arrival_time, departure_time: row.departure_time, stop_sequence:row.stop_sequence});");
        mapaCypher.put("loadTrips",
                "LOAD CSV WITH HEADERS " +
                        "FROM \"file:///D:/Onedrive/Plocha/jizdnirady/GTFS/trips.txt\" " +
                        "AS row FIELDTERMINATOR ',' " +
                        "create (a:Trip{trip_id:row.trptrip_id, service_id:row.service_id})");
        mapaCypher.put("patriKTripu",
                ":auto USING PERIODIC COMMIT 100 " +
                        "LOAD CSV WITH HEADERS " +
                        "FROM \"file:///D:/Onedrive/Plocha/jizdnirady/GTFS/trips.txt\"  " +
                        "AS row FIELDTERMINATOR  \",\"  " +
                        "MERGE (a:Trip{trip_id:row.trip_id}) " +
                        "with a " +
                        "Match (b:TripStop{trip_id:a.trip_id})  " +
                        "create (a)-[r:PatriKTripu]->(b)");
        mapaCypher.put("pokracujeDo",
                "MATCH (a:TripStop) " +
                        "With a " +
                        "Match(b:TripStop{trip_id:a.trip_id}) " +
                        "Where id(b)=(id(a)+ 1) " +
                        "create (a)-[r:PokracujeDo]->(b)");
        mapaCypher.put("prestup1",
                "MATCH (a:TripStop) " +
                        "With a " +
                        "Match(b:TripStop{trip_id:a.trip_id})" +
                        " Where id(b)=(id(a)+ 1) " +
                        "create (a)-[r:PokracujeDo]->(b)");
        mapaCypher.put("prestup2",
                ":auto USING PERIODIC COMMIT 50000 " +
                        "LOAD CSV WITH HEADERS " +
                        "FROM \"file:///D:/Onedrive/Plocha/jizdnirady/firstLast.txt\" " +
                        "AS row FIELDTERMINATOR ',' " +
                        "Match (a:TripStop)" +
                        " where id(a)=tointeger(row.first) " +
                        "Match(b:TripStop) " +
                        "where id(b)=tointeger(row.last) " +
                        "create (a)-[r:Prestup]->(b),(b)-[x:Prestup]->(a)");
        mapaCypher.put("indexTripStop",
                "CREATE INDEX ON :TripStop(trip_id)");
        mapaCypher.put("indexStop",
                "CREATE INDEX ON :trip(trip_id)");

    }

    @Scheduled(cron = "${update.cron}")
    public void updateDatabase() throws InterruptedException {
        downloadFile();
        if (unzip() == true) {
          InsertMainValues();
        } else {
            //v případě nepodaření unzipu program čeka 20 min a zkusí to znova
            logger.info("rescheduling data import in 20 mins");
            wait(600000);
            updateDatabase();
        }
        InsertMainValues();
    }

    public void startDB() {
        myNeo4jDbService.startDB();
    }

    public static boolean unzip() {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
            Thread.sleep(10000);
            File f = new File(source);
            if (f.delete()) {
                logger.info("unzip proběhl úspěšně, GTS.zip smazán");
                return true;
            }
        } catch (ZipException | InterruptedException e) {
            logger.error("unzip error {}", e);
            return false;
        }

        logger.warn("GTFS.zip nebyl smazán, ale unzip proběhl");
        return true;
    }

    public static void downloadFile() {
        try {
            URL website = new URL(url);

            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("PID_GTFS.zip");
            logger.info("staženo");
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                } else {
                    fileEntry.delete();
                }
            }
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void vlozTripStop() {
        FileInputStream inputStream = null;
        Scanner sc = null;
        String row;
        try {
            System.out.println(myNeo4jDbService.getGraphDb());
            try (Transaction tx = myNeo4jDbService.getGraphDb().beginTx()) {
                inputStream = new FileInputStream("GTFS/stop_times.txt");
                sc = new Scanner(inputStream, "UTF-8");
                row = sc.nextLine();
                Node n;
                Node lastN;
                Relationship rel;
                String[] arr;

                while (sc.hasNext()) {

                    row = sc.nextLine();
                    arr = row.split(",");

                    n = tx.createNode();
                    n.addLabel(MyNeo4jDbService.Labels.TripStop);
                    n.setProperty("arrival_time", arr[1]);
                    n.setProperty("departure_time", arr[2]);
                    n.setProperty("stop_id", arr[3]);
                    n.setProperty("trip_id", arr[0]);
                }
                tx.commit();
                logger.info("nodes added");
            }
        } catch (Exception e) {
            logger.error("Exceprion creating TripStop nodes{}", e);
        }
    }

    private static void vytvorPokracujeRelationships() {
        FileInputStream inputStream = null;
        Scanner sc = null;
        String row;

        try (Transaction tx = myNeo4jDbService.getGraphDb().beginTx()) {
            inputStream = new FileInputStream("GTFS/stop_times.txt");
            sc = new Scanner(inputStream, "UTF-8");
            row = sc.nextLine();
            Relationship rel;
            String[] arr;

            row = sc.nextLine();
            arr = row.split(",");
            int x = 0;
            String lastTripID = arr[0];
            while (sc.hasNextLine()) {
                x++;
                row = sc.nextLine();
                arr = row.split(",");

                if (lastTripID.equals(arr[0])) {
                    rel = tx.getNodeById(x - 1).createRelationshipTo(tx.getNodeById(x), MyNeo4jDbService.RelTypes.POKRACUJE_DO);
                } else {
                    lastTripID = arr[0];
                }
            }
            tx.commit();
            logger.info("pokracujeDo Relationships added");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setridStop_timesByDeparture_time() {

        TripStop stop;

        ArrayList<ArrayList> listTripStopWrapper = new ArrayList<>();
        ArrayList<TripStop> listTripStop;
        ArrayList<String> checkList = new ArrayList<>();
        ArrayList<Integer> firstLastList = new ArrayList<>();

        FileInputStream inputStream = null;
        Scanner sc = null;
        String row;
        try {
            inputStream = new FileInputStream("GTFS/stop_times.txt");
            sc = new Scanner(inputStream, "UTF-8");
            row = sc.nextLine();
            String[] arr;
            //System.out.println(row);
            int x = 0;
            while (sc.hasNextLine()) {
                row = sc.nextLine();
                arr = row.split(",");
                stop = new TripStop(x, arr[1], arr[2], arr[0], arr[3]);
                if (checkList.contains(stop.getStop_id())) {
                    listTripStopWrapper.get(checkList.indexOf(stop.getStop_id())).add(stop);
                } else {
                    checkList.add(stop.getStop_id());
                    listTripStop = new ArrayList<>();
                    listTripStop.add(stop);
                    listTripStopWrapper.add(listTripStop);
                }
                x++;
            }
            for (ArrayList list :
                    listTripStopWrapper) {
                Collections.sort(list);
            }
            try {
                File file = new File(destinationTransferFile);
                if (file.createNewFile() == false) {
                    file.delete();
                    file.createNewFile();
                }
                FileWriter writer = new FileWriter(destinationTransferFile);
                writer.write("id,arrival_time,departure_time,trip_id,stop_id,previous_id");
                writer.write("\n");

                int previous_id = -1;
                for (ArrayList<TripStop> list :
                        listTripStopWrapper) {
                    for (TripStop tripStop :
                            list) {
                        if (previous_id == -1) {
                            firstLastList.add(tripStop.getId());
                        } else {
                            writer.write(tripStop.getId() + "," + tripStop.getArrival_time() + "," + tripStop.getDeparture_time() + "," + tripStop.getTrip_id() + "," + tripStop.getStop_id() + "," + previous_id);
                            writer.write("\n");
                        }
                        previous_id = tripStop.getId();
                    }
                    firstLastList.add(previous_id);
                    previous_id = -1;
                }
                writer.close();
                logger.info("success creating transfer_guide.txt");
            } catch (Exception e) {
                logger.error("csv create error {}", e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            File file = new File("firstLast.txt");
            if (file.createNewFile() == false) {
                file.delete();
                file.createNewFile();
            }
            FileWriter writer2 = new FileWriter("firstLast.txt");
            writer2.write("first,last");
            writer2.write("\n");

            for (int i = 0; i < firstLastList.size(); i += 2) {
                writer2.write(firstLastList.get(i) + "," + firstLastList.get(i + 1));
                writer2.write("\n");
            }
            writer2.close();
            logger.info("success creating firstLast.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void setridStop_timesByDeparture_time2() {

        TripStop stop;

        ArrayList<ArrayList> listTripStopWrapper = new ArrayList<>();
        ArrayList<TripStop> listTripStop;
        ArrayList<String> checkList = new ArrayList<>();
        ArrayList<Integer> firstLastList = new ArrayList<>();

        FileInputStream inputStream = null;
        Scanner sc = null;
        String row;
        try {
            inputStream = new FileInputStream("GTFS/stop_times.txt");
            sc = new Scanner(inputStream, "UTF-8");
            row = sc.nextLine();
            String[] arr;
            //System.out.println(row);
            int x = 0;
            while (sc.hasNextLine()) {
                row = sc.nextLine();
                arr = row.split(",");
                stop = new TripStop(x, arr[1], arr[2], arr[0], arr[3]);
                if (checkList.contains(stop.getStop_id())) {
                    listTripStopWrapper.get(checkList.indexOf(stop.getStop_id())).add(stop);
                } else {
                    checkList.add(stop.getStop_id());
                    listTripStop = new ArrayList<>();
                    listTripStop.add(stop);
                    listTripStopWrapper.add(listTripStop);
                }
                x++;
            }
            for (ArrayList list :
                    listTripStopWrapper) {
                Collections.sort(list);
            }
            Relationship rel;
            try (Transaction tx = myNeo4jDbService.getGraphDb().beginTx()) {

                int previous_id = -1;
                for (ArrayList<TripStop> list :
                        listTripStopWrapper) {
                    for (TripStop tripStop :
                            list) {
                        if (previous_id == -1) {
                            firstLastList.add(tripStop.getId());
                        } else {
                            rel = tx.getNodeById(tripStop.getId()).createRelationshipTo(tx.getNodeById(previous_id), MyNeo4jDbService.RelTypes.PRESTUP);
                            rel = tx.getNodeById(previous_id).createRelationshipTo(tx.getNodeById(tripStop.getId()), MyNeo4jDbService.RelTypes.PRESTUP);
                        }
                        previous_id = tripStop.getId();
                    }
                    firstLastList.add(previous_id);
                    previous_id = -1;
                }
                tx.commit();
                logger.info("success creating Prestup relationships");
            } catch (Exception e) {
                logger.error("Exception creating Prestup relationships", e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Relationship rel;
        try (Transaction tx = myNeo4jDbService.getGraphDb().beginTx()) {
            for (int i = 0; i < firstLastList.size(); i += 2) {
                rel = tx.getNodeById(firstLastList.get(i)).createRelationshipTo(tx.getNodeById(firstLastList.get(i + 1)), MyNeo4jDbService.RelTypes.PRESTUP);
                rel = tx.getNodeById(firstLastList.get(i + 1)).createRelationshipTo(tx.getNodeById(firstLastList.get(i)), MyNeo4jDbService.RelTypes.PRESTUP);
            }
            logger.info("success creating firstLast Relationships");
        } catch (Exception e) {
            logger.error("error creating firstLast relationships", e);
        }


    }

    private static void vytvorPrestupy() {
        FileInputStream inputStream = null;
        Scanner sc = null;
        String row;

        try (Transaction tx = myNeo4jDbService.getGraphDb().beginTx()) {
            inputStream = new FileInputStream("guide_transfers.txt");
            sc = new Scanner(inputStream, "UTF-8");
            row = sc.nextLine();
            Relationship rel;
            String[] arr;

            while (sc.hasNextLine()) {
                row = sc.nextLine();
                arr = row.split(",");

                rel = tx.getNodeById(Integer.parseInt(arr[0])).createRelationshipTo(tx.getNodeById(Integer.parseInt(arr[5])), MyNeo4jDbService.RelTypes.PRESTUP);
            }
            tx.commit();
            logger.info("PRESTUP1 Relationships added");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        try (Transaction tx = myNeo4jDbService.getGraphDb().beginTx()) {
            inputStream = new FileInputStream("firstLast.txt");
            sc = new Scanner(inputStream, "UTF-8");
            row = sc.nextLine();
            Relationship rel;
            String[] arr;

            while (sc.hasNextLine()) {
                row = sc.nextLine();
                arr = row.split(",");

                rel = tx.getNodeById(Integer.parseInt(arr[0])).createRelationshipTo(tx.getNodeById(Integer.parseInt(arr[1])), MyNeo4jDbService.RelTypes.PRESTUP);

            }
            tx.commit();
            logger.info("PRESTUP2 Relationships added");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    private static void InsertMainValues() {

        vlozTripStop();
        vytvorPokracujeRelationships();
        vytvorPrestupy();
    }

    public static MyNeo4jDbService getMyNeo4jDbService() {
        return myNeo4jDbService;
    }

    public static void setMyNeo4jDbService(MyNeo4jDbService myNeo4jDbService) {
        Scheduling.myNeo4jDbService = myNeo4jDbService;
    }

    public static String getSource() {
        return source;
    }

    public static void setSource(String source) {
        Scheduling.source = source;
    }

    public static String getDestination() {
        return destination;
    }

    public static void setDestination(String destination) {
        Scheduling.destination = destination;
    }

    public static File getFolder() {
        return folder;
    }

    public static void setFolder(File folder) {
        Scheduling.folder = folder;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Scheduling.url = url;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Scheduling.logger = logger;
    }

    public static String getSqlAdress() {
        return SqlAdress;
    }

    public static void setSqlAdress(String sqlAdress) {
        SqlAdress = sqlAdress;
    }

    public static String getSqlPassword() {
        return SqlPassword;
    }

    public static void setSqlPassword(String sqlPassword) {
        SqlPassword = sqlPassword;
    }

    public static String getSqlUser() {
        return SqlUser;
    }

    public static void setSqlUser(String sqlUser) {
        SqlUser = sqlUser;
    }

    public static String getNeo4jSource() {
        return neo4jSource;
    }

    public static void setNeo4jSource(String neo4jSource) {
        Scheduling.neo4jSource = neo4jSource;
    }

    public static String getDestinationTransferFile() {
        return destinationTransferFile;
    }

    public static void setDestinationTransferFile(String destinationTransferFile) {
        Scheduling.destinationTransferFile = destinationTransferFile;
    }
}


