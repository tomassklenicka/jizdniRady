package com.example.jizdnirady.services;

import com.example.jizdnirady.scheduled.Scheduling;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;


@Service
public class SearchRouteService {
    private static Logger logger = LoggerFactory.getLogger(SearchRouteService.class);
    @Autowired
    Scheduling scheduling;

    MyNeo4jDbService myNeo4jDbService;

    String odjezdovaStaniceID;
    String cilovaStanice;
    public SearchRouteService() {
        this.myNeo4jDbService = scheduling.getMyNeo4jDbService();
    }

    public void VyhledejPodleOdjezdu(String odjezdovyCas, String odjezdovaStanice, String cilovaStanice){
        ArrayList<Integer> odjezdoveNody=new ArrayList<>();
        try (Transaction tx = myNeo4jDbService.getGraphDb().beginTx();
            Result result = tx.execute( "MATCH (n:TripStop {stop_id: '"+odjezdovaStanice+"'}) RETURN id(n)" )
            ){
            while ( result.hasNext() ) {
                odjezdoveNody.add(Integer.parseInt(result.resultAsString()));
            }
        }catch (Exception e){
            logger.error("error neo4j transaction {}", e);
        }
    }

}
