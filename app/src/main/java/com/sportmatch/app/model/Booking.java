package com.sportmatch.app.model;

public final class Booking {
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PAY_AT_VENUE = "PAY_AT_VENUE";
    public static final String PAYMENT_NOT_SELECTED = "NOT_SELECTED";
    public static final String UNPAID = "UNPAID";

    private final String id;
    private final String userId;
    private final String courtId;
    private final String playDate; // yyyy-MM-dd
    private final int startHour;   // Mỗi lượt chơi kéo dài 1 giờ
    private final String customerName;
    private final String customerPhone;
    private final String note;
    private final int totalPrice;
    private final String status;
    private final String paymentMethod;
    private final String paymentStatus;

    public Booking(String id, String userId, String courtId, String playDate, int startHour,
                   String customerName, String customerPhone, String note, int totalPrice,
                   String status) {
        this(id, userId, courtId, playDate, startHour, customerName, customerPhone,
                note, totalPrice, status, PAYMENT_NOT_SELECTED, UNPAID);
    }

    public Booking(String id, String userId, String courtId, String playDate, int startHour,
                   String customerName, String customerPhone, String note, int totalPrice,
                   String status, String paymentMethod, String paymentStatus) {
        this.id = id;
        this.userId = userId;
        this.courtId = courtId;
        this.playDate = playDate;
        this.startHour = startHour;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.note = note;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getCourtId() { return courtId; }
    public String getPlayDate() { return playDate; }
    public int getStartHour() { return startHour; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public String getNote() { return note; }
    public int getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
}
