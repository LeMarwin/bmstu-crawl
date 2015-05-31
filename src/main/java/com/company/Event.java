package com.company;

import java.time.LocalDateTime;

/**
 * Created by lemarwin on 01.06.15.
 * Holds all the data about an Event
 */
public class Event {

    private String name;

    public LocalDateTime getTime() {
        return time;
    }

    private LocalDateTime time;
    private String venue;
    private String venueURL;
    private Category category;
    private String eventURL;

    public Event(){

    }

    public Event(String name, String eventURL, LocalDateTime time, String venue, String venueURL, Category category){
        this.name = name;
        this.eventURL = eventURL;
        this.time = time;
        this.venue = venue;
        this.venueURL = venueURL;
        this.category = category;
    }

    public Event(String name, String eventURL, LocalDateTime time, String venue, String venueURL, String category)
    {
        this.name = name;
        this.eventURL = eventURL;
        this.time = time;
        this.venue = venue;
        this.venueURL = venueURL;
        this.category = Event.categoryFromString(category);
    }

    public String getEventURL() {
        return eventURL;
    }


    public enum Category{
        CONCERT, THEATRE, SPORT, FESTIVAL, NONE
    }

    public static Category categoryFromString(String category)
    {
        if(category.equals("concerts") || category.equals("Концерт"))
            return Category.CONCERT;
        if(category.equals("theatre") || category.equals("Театр"))
            return Category.THEATRE;
        if(category.equals("sport") || category.equals("Спорт"))
            return Category.SPORT;
        if(category.equals("festivals") || category.equals("Фестиваль"))
            return Category.FESTIVAL;
        return Category.NONE;
    }

    public String getName() {
        return  name;
    }

    public String getVenue() {
        return venue;
    }

    public String getVenueURL() {
        return venueURL;
    }

    public Category getCategory(){
        return category;
    }

    public String getCategoryString() {
        String res;
        switch (category) {
            case CONCERT:
                res = "Концерт";
                break;
            case THEATRE:
                res = "Театр";
                break;
            case SPORT:
                res = "Спорт";
                break;
            case FESTIVAL:
                res = "Фестиваль";
                break;
            default:
                res = "Something is very wrong";
        }
        return res;
    }

}
