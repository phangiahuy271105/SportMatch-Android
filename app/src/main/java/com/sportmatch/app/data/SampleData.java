package com.sportmatch.app.data;

import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** One shared source of sample venues and courts for the first app screens. */
public final class SampleData {
    public static final String SPORT_FOOTBALL = "football_mini";
    public static final String SPORT_BADMINTON = "badminton";

    private static final List<Venue> VENUES = Collections.unmodifiableList(Arrays.asList(
            new Venue("venue_1", "Sân bóng Bình Thạnh", "Bình Thạnh", "Bình Thạnh, TP.HCM"),
            new Venue("venue_2", "Sân mini Linh Trung", "Thủ Đức", "Thủ Đức, TP.HCM"),
            new Venue("venue_3", "Sân cầu lông Tân Bình", "Tân Bình", "Tân Bình, TP.HCM"),
            new Venue("venue_4", "Sân cầu lông Quận 7", "Quận 7", "Quận 7, TP.HCM")
    ));

    private static final List<Court> COURTS = Collections.unmodifiableList(Arrays.asList(
            new Court("court_1", "venue_1", SPORT_FOOTBALL, "Sân số 1", 220000),
            new Court("court_2", "venue_2", SPORT_FOOTBALL, "Sân số 1", 180000),
            new Court("court_3", "venue_3", SPORT_BADMINTON, "Sân số 1", 90000),
            new Court("court_4", "venue_4", SPORT_BADMINTON, "Sân số 1", 80000)
    ));

    private SampleData() {
    }

    public static List<Venue> getVenues() {
        return VENUES;
    }

    public static Court getFirstCourtForVenue(String venueId) {
        for (Court court : COURTS) {
            if (court.getVenueId().equals(venueId)) {
                return court;
            }
        }
        return null;
    }
}
