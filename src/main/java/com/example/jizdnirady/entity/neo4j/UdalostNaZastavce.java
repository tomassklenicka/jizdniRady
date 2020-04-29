package com.example.jizdnirady.entity.neo4j;


/*
@NodeEntity
public class UdalostNaZastavce {

    @Id
    @GeneratedValue
    private long eventID;

    private String tripID;
    private String stopID;

    private String casPrijezdu;
    private String casOdjezdu;

    @Relationship(type = "TRANSFER", direction = Relationship.UNDIRECTED)
    private UdalostNaZastavce dalsiUdalostNaZastavce;

    @Relationship(type = "TRANSFER", direction = Relationship.UNDIRECTED)
    private UdalostNaZastavce predchoziUdalostNaZastavce;

    @Relationship(type = "NEXT_STOP", direction = Relationship.OUTGOING)
    private UdalostNaZastavce dalsiZastavkaNaTrase;

    @Relationship(type = "NEXT_STOP", direction = Relationship.INCOMING)
    private UdalostNaZastavce predchoziUdalostNaTrase;

    public UdalostNaZastavce() {
    }

    public long getEventID() {
        return eventID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public void setCasPrijezdu(String casPrijezdu) {
        this.casPrijezdu = casPrijezdu;
    }

    public void setCasOdjezdu(String casOdjezdu) {
        this.casOdjezdu = casOdjezdu;
    }

    public UdalostNaZastavce getDalsiUdalostNaZastavce() {
        return dalsiUdalostNaZastavce;
    }

    public void setDalsiUdalostNaZastavce(UdalostNaZastavce dalsiUdalostNaZastavce) {
        this.dalsiUdalostNaZastavce = dalsiUdalostNaZastavce;
    }

    public UdalostNaZastavce getPredchoziUdalostNaZastavce() {
        return predchoziUdalostNaZastavce;
    }

    public void setPredchoziUdalostNaZastavce(UdalostNaZastavce predchoziUdalostNaZastavce) {
        this.predchoziUdalostNaZastavce = predchoziUdalostNaZastavce;
    }

    public UdalostNaZastavce getDalsiZastavkaNaTrase() {
        return dalsiZastavkaNaTrase;
    }

    public void setDalsiZastavkaNaTrase(UdalostNaZastavce dalsiZastavkaNaTrase) {
        this.dalsiZastavkaNaTrase = dalsiZastavkaNaTrase;
    }

    public UdalostNaZastavce getPredchoziUdalostNaTrase() {
        return predchoziUdalostNaTrase;
    }

    public void setPredchoziUdalostNaTrase(UdalostNaZastavce predchoziUdalostNaTrase) {
        this.predchoziUdalostNaTrase = predchoziUdalostNaTrase;
    }


    public UdalostNaZastavce(String casPrijezdu, String casOdjezdu, String tripID, String stopID) {
        this.eventID = Long.parseLong(null);
        this.casPrijezdu = casPrijezdu;
        this.casOdjezdu = casOdjezdu;
        this.tripID = tripID;
        this.stopID = stopID;

    }
}
*/