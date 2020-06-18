
package com.example.ptb.model;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Tambalban {

    @Expose
    private String alamat;
    @Expose
    private Long createdtime;
    @Expose
    private String fotobengkel;
    @Expose
    private Boolean fulltime;
    @Expose
    private String jambuka;
    @Expose
    private String jamtutup;
    @Expose
    private String latitude;
    @Expose
    private String longitude;
    @Expose
    private String nama;
    @Expose
    private Boolean status;
    @Expose
    private Boolean tubles;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public Tambalban(){}

    public Tambalban(String alamat, Long createdtime, String fotobengkel, Boolean fulltime, String jambuka, String jamtutup, String latitude, String longitude, String nama, Boolean status, Boolean tubles) {
        this.alamat = alamat;
        this.createdtime = createdtime;
        this.fotobengkel = fotobengkel;
        this.fulltime = fulltime;
        this.jambuka = jambuka;
        this.jamtutup = jamtutup;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nama = nama;
        this.status = status;
        this.tubles = tubles;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Long getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Long createdtime) {
        this.createdtime = createdtime;
    }

    public String getFotobengkel() {
        return fotobengkel;
    }

    public void setFotobengkel(String fotobengkel) {
        this.fotobengkel = fotobengkel;
    }

    public Boolean getFulltime() {
        return fulltime;
    }

    public void setFulltime(Boolean fulltime) {
        this.fulltime = fulltime;
    }

    public String getJambuka() {
        return jambuka;
    }

    public void setJambuka(String jambuka) {
        this.jambuka = jambuka;
    }

    public String getJamtutup() {
        return jamtutup;
    }

    public void setJamtutup(String jamtutup) {
        this.jamtutup = jamtutup;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getTubles() {
        return tubles;
    }

    public void setTubles(Boolean tubles) {
        this.tubles = tubles;
    }

}
