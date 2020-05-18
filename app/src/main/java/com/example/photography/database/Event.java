package com.example.photography.database;

public class Event {
    String name;
    String eventType;
    Hall hall;

    public Event(){}

    public Event(String name, String eventType, Hall hall) {
        this.name = name;
        this.eventType = eventType;
        this.hall = hall;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}
