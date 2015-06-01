package com.company;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by lemarwin on 01.06.15.
 * Index building via Lucene framework
 */
public class Lucene {
    public Lucene() {

    }

    private IndexWriter indexWriter = null;
    private String baseFolder;

    public Lucene(String baseFolder) throws IOException {
        Directory indexDir = FSDirectory.open(new File(baseFolder).toPath());
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        indexWriter = new IndexWriter(indexDir, config);
    }

    public void buildIndexes(String subfolder)
    {
        JsonParser jsonParser = new JsonParser();
        File dir = new File(baseFolder+subfolder);
        File[] files = dir.listFiles();
        if(files!=null) {
            for (File jfile : files)
            {
                String extension = FilenameUtils.getExtension(jfile.getAbsolutePath());
                System.out.println(extension);
                if (extension.equals("json")) {
                    try {
                        JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(jfile));
                        if(subfolder.equals("/venues/")) {
                            indexVenue(jsonObject);
                        }
                        if (subfolder.equals("/events/")) {
                            indexEvent(jsonObject);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void indexVenue(JsonObject venue){
        System.out.println("Indexing venue " + venue.getAsJsonPrimitive("name").toString());
    }

    public void indexEvent(JsonObject event){
        System.out.println("Indexing event " + event.getAsJsonPrimitive("name").toString());
    }


}
