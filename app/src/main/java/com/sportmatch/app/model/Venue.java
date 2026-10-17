package com.sportmatch.app.model;

public final class Venue {
    private final String id;
    private final String name;
    private final String district;
    private final String address;
    private final String description;
    private final String openingHours;
    private final String rules;
    private final String[] amenities;

    public Venue(String id, String name, String district, String address,
                 String description, String openingHours, String rules, String[] amenities) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.address = address;
        this.description = description;
        this.openingHours = openingHours;
        this.rules = rules;
        this.amenities = amenities;
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

    public String getDescription() {
        return description;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getRules() {
        return rules;
    }

    public String[] getAmenities() {
        return amenities;
    }
}
