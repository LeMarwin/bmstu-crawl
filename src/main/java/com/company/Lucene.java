package com.company;

import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Created by lemarwin on 01.06.15.
 * Index building via Lucene framework
 */
public class Lucene {
    public Lucene(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    private String baseFolder;

    private IndexWriter getIndexWriter(String subfolder) throws IOException {
        Directory indexDir = FSDirectory.open(new File(baseFolder+subfolder).toPath());
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        return new IndexWriter(indexDir, config);
    }

    public void buildIndexes(String subfolder) throws IOException {
        IndexWriter indexWriter = getIndexWriter(subfolder);
        JsonParser jsonParser = new JsonParser();
        File dir = new File(baseFolder+subfolder);
        File[] files = dir.listFiles();
        if(files!=null) {
            for (File jfile : files)
            {
                String extension = FilenameUtils.getExtension(jfile.getAbsolutePath());
                if (extension.equals("json")) try {
                    Object element = new JSONObject(FileUtils.readFileToString(jfile));
                    JSONObject jsonObject = (JSONObject) element;
                    System.out.println("Indexing " + subfolder + " " + jsonObject.getString("name"));
                    indexObject(indexWriter,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No files found");
        }
        indexWriter.close();
    }

    public void indexObject(IndexWriter indexWriter, JSONObject jsonObject) throws JSONException, IOException {
        Document doc = new Document();
        for(int i = 0; i<jsonObject.names().length();i++)
        {
            String key = jsonObject.names().getString(i);
            Object object = jsonObject.get(key);
            String value;
            if(key.equals("time")) {
                value = deserializeLDT((JSONObject) object);            //Да, это ужасно и так делать нельзя
            }
            else {
                value = (String) object;                                //Но если очень надо...
            }
            doc.add(new StringField(key, value, Field.Store.YES));
            System.out.println(key + "\t" + value);
        }
        indexWriter.addDocument(doc);
    }

    private String deserializeLDT(JSONObject ldt) throws JSONException {
        JSONObject date = ldt.getJSONObject("date");
        JSONObject time = ldt.getJSONObject("time");
        int year = date.getInt("year");
        Month month = Month.of(date.getInt("month"));
        int dayOfMonth = date.getInt("day");
        int hour = time.getInt("hour");
        int minute = time.getInt("minute");
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
        return localDateTime.toString();
    }


}
