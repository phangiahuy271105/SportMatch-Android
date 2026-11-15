package com.sportmatch.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.sportmatch.app.data.BookingStore;
import com.sportmatch.app.model.Booking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Dùng vùng lưu riêng để không thay đổi lịch demo trên emulator. */
@RunWith(AndroidJUnit4.class)
public class BookingPaymentTest {
    private Context context;
    private SharedPreferences preferences;

    @Before
    public void prepare() {
        Context target = InstrumentationRegistry.getInstrumentation().getTargetContext();
        preferences = target.getSharedPreferences("booking_payment_test", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        context = new ContextWrapper(target) {
            @Override public Context getApplicationContext() { return this; }
            @Override public SharedPreferences getSharedPreferences(String name, int mode) {
                return preferences;
            }
        };
    }

    @After
    public void cleanUp() {
        preferences.edit().clear().commit();
    }

    @Test
    public void paymentSurvivesReloadAndKeepsOwnershipAndSlotLock() {
        BookingStore store = new BookingStore(context);
        Booking booking = new Booking("SM-test", "a@example.test", "court_1_1",
                "2030-01-01", 8, "Demo", "0900000000", "", 220000,
                Booking.CONFIRMED, Booking.PAY_AT_VENUE, Booking.UNPAID);
        assertTrue(store.save(booking));
        BookingStore reloaded = new BookingStore(context);
        Booking saved = reloaded.getBooking("SM-test", "a@example.test");
        assertNotNull(saved);
        assertEquals(Booking.PAY_AT_VENUE, saved.getPaymentMethod());
        assertEquals(Booking.UNPAID, saved.getPaymentStatus());
        assertEquals(220000, saved.getTotalPrice());
        assertNull(reloaded.getBooking("SM-test", "b@example.test"));
        assertTrue(reloaded.isSlotBooked("court_1_1", "2030-01-01", 8));
        assertFalse(reloaded.save(booking));
    }

    @Test
    public void oldBookingWithoutPaymentFieldsCanStillBeRead() {
        String oldJson = "[{\"id\":\"old\",\"userId\":\"demo_user_1\","
                + "\"courtId\":\"court_1_1\",\"playDate\":\"2020-01-01\","
                + "\"startHour\":8,\"totalPrice\":220000,\"status\":\"CONFIRMED\"}]";
        preferences.edit().putString("bookings", oldJson).commit();
        Booking saved = new BookingStore(context).getBooking("old", "demo_user_1");
        assertNotNull(saved);
        assertEquals(Booking.PAYMENT_NOT_SELECTED, saved.getPaymentMethod());
        assertEquals(Booking.UNPAID, saved.getPaymentStatus());
        assertEquals(220000, saved.getTotalPrice());
    }

    @Test
    public void pastBookingRemainsUnpaid() {
        BookingStore store = new BookingStore(context);
        assertTrue(store.save(new Booking("past", "demo_user_1", "court_1_1",
                "2020-01-01", 8, "Demo", "0900000000", "", 220000,
                Booking.CONFIRMED, Booking.PAY_AT_VENUE, Booking.UNPAID)));
        assertEquals(Booking.UNPAID,
                new BookingStore(context).getBooking("past", "demo_user_1").getPaymentStatus());
    }
}
