
package com.example.ptb.model;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class User {

    @Expose
    private Long createdtime;
    @Expose
    private String email;
    @Expose
    private String foto;
    @Expose
    private String fullname;
    @Expose
    private String password;
    @Expose
    private String username;

    public User(){}

    public User(Long createdtime, String email, String foto, String fullname, String password, String username) {
        this.createdtime = createdtime;
        this.email = email;
        this.foto = foto;
        this.fullname = fullname;
        this.password = password;
        this.username = username;
    }

    public Long getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Long createdtime) {
        this.createdtime = createdtime;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
