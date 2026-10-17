package com.sportmatch.app.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.sportmatch.app.model.Booking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** Lưu lịch đặt mẫu trên máy bằng SharedPreferences có sẵn của Android. */
public final class BookingStore {
    private static final String KEY_BOOKINGS = "bookings";
    private final SharedPreferences preferences;

    public BookingStore(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences("sportmatch_demo", Context.MODE_PRIVATE);
    }

    public List<Booking> getBookingsForUser(String userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : readAll()) {
            if (booking.getUserId().equals(userId)) result.add(booking);
        }
        return result;
    }

    public Booking getBooking(String bookingId, String userId) {
        for (Booking booking : getBookingsForUser(userId)) {
            if (booking.getId().equals(bookingId)) return booking;
        }
        return null;
    }

    public boolean isSlotBooked(String courtId, String date, int startHour) {
        for (Booking booking : readAll()) {
            if (booking.getCourtId().equals(courtId)
                    && booking.getPlayDate().equals(date)
                    && booking.getStartHour() == startHour
                    && Booking.CONFIRMED.equals(booking.getStatus())) {
                return true;
            }
        }
        return false;
    }

    public boolean save(Booking newBooking) {
        if (isSlotBooked(newBooking.getCourtId(), newBooking.getPlayDate(),
                newBooking.getStartHour())) return false;

        JSONArray array = readArray();
        JSONObject item = new JSONObject();
        try {
            item.put("id", newBooking.getId());
            item.put("userId", newBooking.getUserId());
            item.put("courtId", newBooking.getCourtId());
            item.put("playDate", newBooking.getPlayDate());
            item.put("startHour", newBooking.getStartHour());
            item.put("customerName", newBooking.getCustomerName());
            item.put("customerPhone", newBooking.getCustomerPhone());
            item.put("note", newBooking.getNote());
            item.put("totalPrice", newBooking.getTotalPrice());
            item.put("status", newBooking.getStatus());
        } catch (JSONException e) {
            return false;
        }
        array.put(item);
        return preferences.edit().putString(KEY_BOOKINGS, array.toString()).commit();
    }

    private List<Booking> readAll() {
        List<Booking> result = new ArrayList<>();
        JSONArray array = readArray();
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.optJSONObject(i);
            if (item == null) continue;
            result.add(new Booking(
                    item.optString("id"), item.optString("userId"),
                    item.optString("courtId"), item.optString("playDate"),
                    item.optInt("startHour"), item.optString("customerName"),
                    item.optString("customerPhone"), item.optString("note"),
                    item.optInt("totalPrice"), item.optString("status")
            ));
        }
        return result;
    }

    private JSONArray readArray() {
        try {
            return new JSONArray(preferences.getString(KEY_BOOKINGS, "[]"));
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
}
