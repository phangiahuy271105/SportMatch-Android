package com.sportmatch.app.model;

public class Post {
    private String id;
    private String authorName;
    private String sport;
    private String location;
    private String time;
    private int currentAccepted;
    private int maxPlayers;
    private String description;
    private String status;

    public Post(String id, String authorName, String sport, String location, String time, int currentAccepted, int maxPlayers, String description, String status) {
        this.id = id;
        this.authorName = authorName;
        this.sport = sport;
        this.location = location;
        this.time = time;
        this.currentAccepted = currentAccepted;
        this.maxPlayers = maxPlayers;
        this.description = description;
        this.status = status;
    }

    public String getId() { return id; }
    public String getAuthorName() { return authorName; }
    public String getSport() { return sport; }
    public String getLocation() { return location; }
    public String getTime() { return time; }
    public int getCurrentAccepted() { return currentAccepted; }
    public int getMaxPlayers() { return maxPlayers; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
}