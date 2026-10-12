package com.sportmatch.app.model;

public final class Court {
    private final String id;
    private final String venueId;
    private final String sportId;
    private final String name;
    private final int pricePerHour;

    public Court(String id, String venueId, String sportId, String name, int pricePerHour) {
        this.id = id;
        this.venueId = venueId;
        this.sportId = sportId;
        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    public String getId() {
        return id;
    }

    public String getVenueId() {
        return venueId;
    }

    public String getSportId() {
        return sportId;
    }

    public String getName() {
        return name;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }
}
