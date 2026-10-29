package models;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String favoriteSport;
    private String gender; // "Nam" hoặc "Nữ"

    public User() {
        this.gender = "Nam";
    }

    // Constructor 5 tham số (Tự hiểu mặc định là Nam)
    public User(String id, String email, String fullName, String phoneNumber, String favoriteSport) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.favoriteSport = favoriteSport;
        this.gender = "Nam";
    }

    // Constructor 6 tham số (Đầy đủ cả Giới tính)
    public User(String id, String email, String fullName, String phoneNumber, String favoriteSport, String gender) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.favoriteSport = favoriteSport;
        this.gender = (gender != null && !gender.isEmpty()) ? gender : "Nam";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFavoriteSport() { return favoriteSport; }
    public void setFavoriteSport(String favoriteSport) { this.favoriteSport = favoriteSport; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}