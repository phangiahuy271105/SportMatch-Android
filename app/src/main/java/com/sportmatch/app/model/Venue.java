package com.sportmatch.app.model;

import java.util.Locale;

public final class Venue {
    private final String id;
    private final String name;
    private final String district;
    private final String address;
    private final String description;
    private final int openingHour;
    private final int closingHour;
    private final String rules;
    private final String[] amenities;
    private final int imageResId;

    public Venue(String id, String name, String district, String address,
                 String description, int openingHour, int closingHour,
                 String rules, String[] amenities, int imageResId) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.address = address;
        this.description = description;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.rules = rules;
        this.amenities = amenities;
        this.imageResId = imageResId;
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
        return String.format(Locale.US, "%02d:00 – %02d:00", openingHour, closingHour);
    }

    public int getOpeningHour() {
        return openingHour;
    }

    public int getClosingHour() {
        return closingHour;
    }

    public String getRules() {
        return rules;
    }

    public String[] getAmenities() {
        return amenities;
    }

    public int getImageResId() {
        return imageResId;
    }
}
