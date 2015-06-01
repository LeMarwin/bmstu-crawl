package com.company;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
/**
 * Created by lemarwin on 01.06.15.
 * The tiniest web-server evar
 */
public class Sparkling {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
        get("/events/:query/:num", (request1, response1) -> {
            String query = request1.params(":query");
            int n = Integer.parseInt(request1.params(":num"));
            Search searcher = new Search("/output/events/index");
            TopDocs res = searcher.performSearch(query,n);
            List<Event> events = new ArrayList<>();
            for (ScoreDoc hit : res.scoreDocs) {
                Document doc = searcher.getDocument(hit.doc);
                events.add(new Event(doc));
            }
            return events;
        }, new JsonTransformer());

        get("/events/", (request1, response1) -> {
            String query = request1.queryParams("query");
            int n = Integer.parseInt(request1.queryParams("num"));
            Search searcher = new Search("/output/events/index");
            TopDocs res = searcher.performSearch(query,n);
            System.out.println(res.totalHits);
            List<Event> events = new ArrayList<>();
            for (ScoreDoc hit : res.scoreDocs) {
                Document doc = searcher.getDocument(hit.doc);
                events.add(new Event(doc));
            }
            return events;
        }, new JsonTransformer());
        get("/*", (request, response) -> "No such page. Sorry");
    }
}
