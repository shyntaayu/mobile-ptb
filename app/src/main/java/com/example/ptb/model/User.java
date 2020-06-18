
package com.example.ptb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class User {

    @SerializedName("created_time")
    private Long createdTime;
    @Expose
    private String email;
    @Expose
    private String foto;
    @SerializedName("full_name")
    private String fullName;
    @Expose
    private String password;
    @SerializedName("user_name")
    private String userName;

    public User(){}
    public User(Long createdTime, String email, String foto, String fullName, String password, String userName) {
        this.createdTime = createdTime;
        this.email = email;
        this.foto = foto;
        this.fullName = fullName;
        this.password = password;
        this.userName = userName;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
