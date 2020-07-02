package com.example.ptb.model;

public class Penilaian {
    private String username;
    private String komentar;
    private String rating;
    private String userID;
    private String tblID;
    private long createdtime;
    private String key;


//    public Penilaian(String user, String komen, Float rating, String userID, String tblID) {
//        this.user = user;
//        this.komen = komen;
//        this.rating = rating;
//        this.userID = userID;
//        this.tblID = tblID;
//    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTblID() {
        return tblID;
    }

    public void setTblID(String tblID) {
        this.tblID = tblID;
    }

    public String getUser() {
        return username;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public String getKomen() {
        return komentar;
    }

    public void setKomen(String komentar) {
        this.komentar = komentar;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(long createdtime) {
        this.createdtime = createdtime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
