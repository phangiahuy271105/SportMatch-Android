package com.sportmatch.app.model;

public final class Venue {
    private final String id;
    private final String name;
    private final String district;
    private final String address;

    public Venue(String id, String name, String district, String address) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDistrict() {
        return district;
    }

    public String getAddress() {
        return address;
    }
}
