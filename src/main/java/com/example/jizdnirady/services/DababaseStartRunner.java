package com.example.jizdnirady.services;


import com.example.jizdnirady.scheduled.Scheduling;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
public class DababaseStartRunner {

    @Autowired
    Scheduling scheduling;

    private static Logger logger = LoggerFactory.getLogger(DababaseStartRunner.class);;

    public DababaseStartRunner() {
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        logger.info("started DatabaseRunner ");
        scheduling.startDB();
        try {
            scheduling.updateDatabase();
        } catch (InterruptedException e) {
            logger.error("error updating neo4j database {}", e);
        }
    }



}
