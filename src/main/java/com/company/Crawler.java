package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attribute;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by lemarwin on 01.06.15.
 * Main class to do the crawling
 */
public class Crawler {
    public Crawler(){

    }
    private List<String> visitedVenues = new ArrayList<String>();
    private List<String> visitedEvents = new ArrayList<String>();

    public List<Venue> getVenues() {
        return venues;
    }

    public List<Event> getEvents() {
        return events;
    }

    private List<Venue> venues = new ArrayList<Venue>();
    private List<Event> events = new ArrayList<Event>();
    private String mainDomain = "http://ponominalu.ru";

    public void crawlCategory(String cat)
    {
        Document doc = null;

        try {
            doc = Jsoup.connect(mainDomain + "/category/" + cat).timeout(10000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void parseEvent(String url, String category)
    {
        if(!visitedEvents.contains(url))
        {
            Document doc;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            visitedEvents.add(url);
            String name;
            LocalDateTime time;
            String venue;
            String venueURL;

            Element venueNode = doc.select("span[itemprop=location] > a[itemprop=url]").first();
            name = doc.select("div.eventShortInfo h1 span[itemprop=name]").first().text();
            time = LocalDateTime.parse(doc.select("time[itemprop=startDate]").first().attr("datetime"));
            venue = venueNode.attr("title");
            venueURL = mainDomain+venueNode.attr("href");
            events.add(new Event(name, url, time, venue, venueURL, category));
        }
    }
}
