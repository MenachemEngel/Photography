package com.example.photography.database;

import java.io.Serializable;
import java.util.Date;

public class Event {
    private String name;
    private String eventType;
    private Hall hall;
    private long id;
    private Date date;
    private boolean active;
    private String owner;

    public Event(){}

    public Event(String name, String eventType, Hall hall, long id, Date date) {
        this.name = name;
        this.eventType = eventType;
        this.hall = hall;
        this.id = id;
        this.date = date;
    }

    public Event(String name, String eventType, Hall hall, Date date, String owner) {
        this.name = name;
        this.eventType = eventType;
        this.hall = hall;
        this.date = date;
        String idCode = this.name + this.eventType + this.hall.toString() + this.date.toString();
        this.id =  System.identityHashCode(idCode);
        this.active = true;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void end(){
        active = false;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}