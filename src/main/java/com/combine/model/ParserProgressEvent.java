package com.combine.model;

import java.util.Date;

public class ParserProgressEvent {

    private String id;
    private String username;
    private Date date;
    private long progress;
    private String description;
    private Date started;
    private Date finished;

    public ParserProgressEvent() {
    }

    public ParserProgressEvent(String id, Date date, long progress, String description) {
        this.id = id;
        this.date = date;
        this.progress = progress;
        this.description = description;
    }

    public ParserProgressEvent(String id, String username, Date date, long progress, String description, Date started, Date finished) {
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

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
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
}
