package com.company;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attribute;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    Integer venueCount = 0;
    Integer eventCount = 0;

    private String mainDomain = "http://ponominalu.ru";

    public void crawlCategory(String cat, Integer pages, String outputFolder) {
        new File(outputFolder + "/venues/").mkdirs();
        new File(outputFolder + "/events/").mkdirs();
        Document doc = null;
        for (Integer pageCount = 1; pageCount <= pages; pageCount++) {
            try {
                doc = Jsoup.connect(mainDomain + "/category/" + cat + "?page=" + pageCount.toString()).timeout(10000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements venueNodes = null;
            if (doc != null) {
                venueNodes = doc.select(".eventVenue > a");
                Elements eventNodes = doc.select(".eventTitle > a");
                Gson gson = new Gson();
                for (Element el : venueNodes) {
                    Venue venue = parseVenue(mainDomain + el.attr("href"));
                    if (venue != null) {
                        try {
                            PrintWriter writer = new PrintWriter(outputFolder +
                                    "/venues/" + venueCount.toString() + ".json", "UTF-8");
                            writer.println(gson.toJson(venue));
                            writer.close();
                            venueCount++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                for (Element el : eventNodes) {
                    Event event = parseEvent(mainDomain + el.attr("href"), cat);
                    if (event != null) {
                        try {
                            PrintWriter writer = new PrintWriter(outputFolder +
                                    "/events/" + eventCount.toString() + ".json", "UTF-8");
                            writer.println(gson.toJson(event));
                            writer.close();
                            eventCount++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void crawlMultiple(List<String> categories, Integer pages, String outputFolder)
    {
        for(String cat : categories)
        {
            this.crawlCategory(cat,pages,outputFolder);
        }
    }
    public Event parseEvent(String url, String category)
    {
        Event res = null;
        if(!visitedEvents.contains(url))
        {
            Document doc;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
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
            System.out.println(eventCount.toString() + "\tParsed event: " + name);
            res = new Event(name, url, time, venue, venueURL, category);
        }
        return res;
    }

    public Venue parseVenue(String url)
    {
        Venue res = null;
        if(!visitedVenues.contains(url))
        {
            Document doc;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            visitedVenues.add(url);

            String name;
            String address;
            String description;
            String workTime;

            Elements wt = doc.select("[id=vremya_raboti]");
            Elements desc = doc.select("div.content[itemprop=description]");
            if(!wt.isEmpty()) {
                workTime = wt.first().text();
            }
            else {
                workTime = "Время работы не указано";
            }
            if(!desc.isEmpty()) {
                description = desc.first().text();
            }
            else {
                description = "Нет описания";
            }
            address = doc.select("span[itemprop=streetAddress]").first().text().replace("Адрес: ","");
            name = doc.select("article.venuePage > h1").text().replace("Билеты в ","");
            System.out.println(venueCount.toString() + "\tParsed venue: " + name);
            res = new Venue(name, address, description, url, workTime);
        }
        return res;
    }
}
