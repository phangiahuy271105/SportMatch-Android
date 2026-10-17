package com.sportmatch.app.data;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/** Ngày, giờ và giá dùng chung trên các màn đặt sân. */
public final class BookingTime {
    private static final TimeZone HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");

    private BookingTime() { }

    public static Calendar now() {
        return Calendar.getInstance(HCM);
    }

    public static String today() {
        return toIsoDate(now());
    }

    public static String tomorrow() {
        Calendar calendar = now();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return toIsoDate(calendar);
    }

    public static String toIsoDate(Calendar calendar) {
        return String.format(Locale.US, "%04d-%02d-%02d",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String displayDate(String isoDate) {
        if (isoDate == null || isoDate.length() != 10) return "";
        String dayMonthYear = isoDate.substring(8, 10) + "/" + isoDate.substring(5, 7)
                + "/" + isoDate.substring(0, 4);
        if (isoDate.equals(today())) return "Hôm nay, " + dayMonthYear;
        if (isoDate.equals(tomorrow())) return "Ngày mai, " + dayMonthYear;
        return dayMonthYear;
    }

    public static String displaySlot(int startHour) {
        return String.format(Locale.US, "%02d:00 – %02d:00", startHour, startHour + 1);
    }

    public static String displayPrice(int price) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"))
                .format(price) + " đ";
    }

    public static boolean hasStarted(String isoDate, int startHour) {
        return slotTime(isoDate, startHour) <= System.currentTimeMillis();
    }

    public static boolean hasEnded(String isoDate, int startHour) {
        return slotTime(isoDate, startHour + 1) <= System.currentTimeMillis();
    }

    private static long slotTime(String isoDate, int hour) {
        try {
            Calendar calendar = now();
            calendar.set(Integer.parseInt(isoDate.substring(0, 4)),
                    Integer.parseInt(isoDate.substring(5, 7)) - 1,
                    Integer.parseInt(isoDate.substring(8, 10)), hour, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
            return 0; // Ngày sai được coi là đã qua, không cho đặt.
        }
    }
}
