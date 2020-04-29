package com.example.jizdnirady.entity;

public class TripStop implements Comparable {
    String arrival_time;
    String departure_time;
    String trip_id;
    String stop_id;
    int id;



    public TripStop(int id, String arrival_time, String departure_time, String trip_id, String stop_id) {
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.trip_id = trip_id;
        this.stop_id = stop_id;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getStop_id() {
        return stop_id;
    }

    public void setStop_id(String stop_id) {
        this.stop_id = stop_id;
    }

    @Override
    public int compareTo(Object o) {
        String[] arrBString = ((TripStop) o).getDeparture_time().split(":");
        String[] arrAString = this.departure_time.split(":");

        final int[] arrB = new int[arrBString.length];
        for (int i = 0; i < arrBString.length; i++) {
            arrB[i] = Integer.parseInt(arrBString[i]);
        }
        final int[] arrA = new int[arrAString.length];
        for (int i = 0; i < arrAString.length; i++) {
            arrA[i] = Integer.parseInt(arrAString[i]);
        }

        if (arrA[0] > arrB[0]) {
            return 1;
        } else if (arrA[0] < arrB[0]) {
            return -1;
        } else if (arrA[1] > arrB[1]) {
            return 1;
        } else if (arrA[1] < arrB[1]) {
            return -1;
        } else if (arrA[2] > arrB[2]) {
            return 1;
        } else if (arrA[2] < arrB[2]) {
            return -1;
        } else return 0;

    }
}
