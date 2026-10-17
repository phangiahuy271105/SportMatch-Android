package com.sportmatch.app.data;

import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Dữ liệu hư cấu để nhóm thử app trước khi kết nối dữ liệu chung. */
public final class SampleData {
    public static final String SPORT_FOOTBALL = "football_mini";
    public static final String SPORT_BADMINTON = "badminton";

    private static final List<Venue> VENUES = Arrays.asList(
            new Venue("venue_1", "Sân bóng Bình Thạnh", "Bình Thạnh",
                    "Bình Thạnh, TP.HCM",
                    "Cụm sân bóng đá mini có hai sân cỏ nhân tạo. Sân số 1 phù hợp trận 5 người; sân số 2 rộng hơn cho nhóm 7 người.",
                    "06:00 – 23:00",
                    "Mang giày phù hợp mặt cỏ nhân tạo. Giữ vệ sinh và đến trước giờ chơi 10 phút.",
                    new String[]{"Đèn chiếu sáng", "Bãi giữ xe", "Khu chờ", "Nước uống", "Thuê bóng"}),
            new Venue("venue_2", "Sân mini Linh Trung", "Thủ Đức",
                    "Linh Trung, Thủ Đức, TP.HCM",
                    "Khu sân mini dành cho các nhóm bạn ở Thủ Đức. Có sân 5 người cho trận ngắn và sân 7 người cho đội đông hơn.",
                    "15:00 – 22:00",
                    "Không dùng giày đinh sắt trên mặt cỏ. Tự giữ đồ cá nhân và trả sân đúng giờ.",
                    new String[]{"Đèn buổi tối", "Bãi giữ xe", "Ghế nghỉ", "Nước uống", "Thuê bóng"}),
            new Venue("venue_3", "Sân cầu lông Tân Bình", "Tân Bình",
                    "Tân Bình, TP.HCM",
                    "Cụm sân cầu lông trong nhà với mặt thảm PVC. Hai sân phù hợp đánh đơn hoặc đánh đôi, có khu ngồi chờ giữa các lượt.",
                    "06:00 – 22:00",
                    "Mang giày đế không để lại vệt trên thảm. Không mang thức ăn vào khu vực thi đấu.",
                    new String[]{"Sân trong nhà", "Thảm PVC", "Bãi giữ xe", "Khu chờ", "Thuê vợt"}),
            new Venue("venue_4", "Sân cầu lông Quận 7", "Quận 7",
                    "Quận 7, TP.HCM",
                    "Địa điểm cầu lông trong nhà ở Quận 7. Sân số 1 dùng sàn gỗ, sân số 2 dùng thảm PVC để người chơi chọn theo sở thích.",
                    "07:00 – 23:00",
                    "Mang giày cầu lông đế mềm. Giữ vệ sinh mặt sân và thu dọn đồ sau khi chơi.",
                    new String[]{"Sân trong nhà", "Sàn gỗ", "Thảm PVC", "Bãi giữ xe", "Nước uống"})
    );

    private static final List<Court> COURTS = Arrays.asList(
            new Court("court_1_1", "venue_1", SPORT_FOOTBALL, "Sân số 1 (5 người)",
                    "Cỏ nhân tạo, phù hợp trận 5 người", 220000),
            new Court("court_1_2", "venue_1", SPORT_FOOTBALL, "Sân số 2 (7 người)",
                    "Cỏ nhân tạo, không gian rộng cho 7 người", 270000),
            new Court("court_2_1", "venue_2", SPORT_FOOTBALL, "Sân số 1 (5 người)",
                    "Cỏ nhân tạo, trận 5 người", 180000),
            new Court("court_2_2", "venue_2", SPORT_FOOTBALL, "Sân số 2 (7 người)",
                    "Cỏ nhân tạo, trận 7 người", 230000),
            new Court("court_3_1", "venue_3", SPORT_BADMINTON, "Sân số 1 (thảm PVC)",
                    "Sân trong nhà, thảm PVC", 90000),
            new Court("court_3_2", "venue_3", SPORT_BADMINTON, "Sân số 2 (thảm PVC)",
                    "Sân trong nhà, thảm PVC", 90000),
            new Court("court_4_1", "venue_4", SPORT_BADMINTON, "Sân số 1 (sàn gỗ)",
                    "Sân trong nhà, sàn gỗ", 80000),
            new Court("court_4_2", "venue_4", SPORT_BADMINTON, "Sân số 2 (thảm PVC)",
                    "Sân trong nhà, thảm PVC", 85000)
    );

    private SampleData() { }

    public static List<Venue> getVenues() {
        return VENUES;
    }

    public static Venue getVenueById(String id) {
        for (Venue venue : VENUES) {
            if (venue.getId().equals(id)) return venue;
        }
        return null;
    }

    public static Court getCourtById(String id) {
        for (Court court : COURTS) {
            if (court.getId().equals(id)) return court;
        }
        return null;
    }

    public static List<Court> getCourtsForVenue(String venueId) {
        List<Court> result = new ArrayList<>();
        for (Court court : COURTS) {
            if (court.getVenueId().equals(venueId)) result.add(court);
        }
        return result;
    }

    public static Court getFirstCourtForVenue(String venueId) {
        for (Court court : COURTS) {
            if (court.getVenueId().equals(venueId)) return court;
        }
        return null;
    }
}
