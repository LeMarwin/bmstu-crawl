package com.company;

/**
 * Created by lemarwin on 01.06.15.
 * Class to hold the data abount a Venue -- a place
 */
public class Venue {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    private String name;
    private String address;
    private String description;
    private String venueUrl;
    private String workTime;

    public Venue(){

    }

    public Venue(String name, String address, String description, String url, String workTime) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.venueUrl = url;
        this.workTime = workTime;
    }

}
