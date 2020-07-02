
package com.example.ptb.model;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Rating {

    @Expose
    private Long createdtime;
    @Expose
    private String rating;
    @Expose
    private String tbID;
    @Expose
    private String userID;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
    public Rating(){}
    public Rating(Long createdtime, String rating, String tbID, String userID) {
        this.createdtime = createdtime;
        this.rating = rating;
        this.tbID = tbID;
        this.userID = userID;
    }

    public Long getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Long createdtime) {
        this.createdtime = createdtime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTbID() {
        return tbID;
    }

    public void setTbID(String tbID) {
        this.tbID = tbID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
