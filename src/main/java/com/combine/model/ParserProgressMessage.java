package com.combine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class ParserProgressMessage {

    private String id;
    private String username;
    private Date date;
    private double progress;
    private String description;
    private Date started;
    private Date finished;

    //helper props
    @JsonIgnore
    private double distance;

    private ParserProgressMessage() {
    }

    public ParserProgressMessage(String id, String username, Date date, double progress, String description, Date started, Date finished) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.progress = progress;
        this.description = description;
        this.started = started;
        this.finished = finished;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public double getDistance() {
        return distance;
    }

    public ParserProgressMessage finish() {
        this.setProgress(100);
        this.setFinished(new Date());
        return this;
    }

    public ParserProgressMessage init(double distance) {
        this.distance = distance;
        return this;
    }

    public ParserProgressMessage with(Date date, double progress, String description) {
        this.date = date;
        this.progress = progress;
        this.description = description;
        return this;
    }

    public ParserProgressMessage with(double current, double total, String description) {
        this.date = new Date();
        this.progress = this.progress + (((current / total) * 10.0) * (this.distance / 100.0));
        System.out.println("progress: " + progress);
        this.description = description;
        return this;
    }
}
