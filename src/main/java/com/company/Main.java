package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lemarwin on 01.06.15.
 * Main
 */
public class Main {
    public static void main(String[] args)
    {
        List<String> categories = new ArrayList<String>();
        categories.add("concerts");
        categories.add("theatre");
        categories.add("sport");
        categories.add("festivals");
        Crawler crawler = new Crawler();
        //crawler.crawlMultiple(categories,2,"output");

        Lucene lucene = new Lucene("output");
        try {
            lucene.buildIndexes("/venues/");
            lucene.buildIndexes("/events/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
