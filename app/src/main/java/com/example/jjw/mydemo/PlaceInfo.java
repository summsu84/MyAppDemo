package com.example.jjw.mydemo;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by JJW on 2016-09-12.
 */
public class PlaceInfo implements Serializable {
    private String name;
    private String intro;
    private String description;
    private String lat;
    private String lon;
    private String rate;

    private  boolean isChecked;
    private  String imageSrc;

/*    public PlaceInfo(String _city, String _desc, boolean _ischecked, String _imageSrc)
    {
        city = _city;
        desc = _desc;
        isChecked = _ischecked;
        imageSrc = _imageSrc;
    }*/

    public PlaceInfo(String name, String intro, String description, String lat, String lon, String rate, boolean isChecked, String imageSrc) {
        this.name = name;
        this.intro = intro;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.rate = rate;
        this.isChecked = isChecked;
        this.imageSrc = imageSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

}
