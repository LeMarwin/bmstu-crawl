package com.company;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

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
        int c = 1;
        switch (c) {
            case 0 :
                runCrawler();
                break;
            case 1:
                runIndexing();
                runSearch();
                break;
            case 2:
                runSearch();
                break;
        }
    }

    private static void runCrawler() {
        List<String> categories = new ArrayList<String>();
        categories.add("concerts");
        categories.add("theatre");
        categories.add("sport");
        categories.add("festivals");
        Crawler crawler = new Crawler();
        crawler.crawlMultiple(categories,2,"output");
    }

    private static void runIndexing() {
        Lucene lucene = new Lucene("output");
        try {
            lucene.buildIndexes("/venues/");
            lucene = new Lucene("output");
            lucene.buildIndexes("/events/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runSearch()
    {
        try {
            Search search = new Search("output/venues/index");
            TopDocs topDocs = search.performSearch("ЦДК*", 5);
            // obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
            ScoreDoc[] hits = topDocs.scoreDocs;
            // retrieve each matching document from the ScoreDoc array
            System.out.println(hits.length);
            for (int i = 0; i < hits.length; i++) {
                Document doc = Search.getDocument(hits[i].doc);
                String theatreRep = doc.get("name");
                System.out.println(theatreRep);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
