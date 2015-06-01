package com.company;

import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class Search {
    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    /** Creates a new instance of SearchEngine */
    public Search(String indexFolder) throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(System.getProperty("user.dir")
                + indexFolder).toPath())));
        parser = new QueryParser("content", new RussianAnalyzer());
    }

    public TopDocs performSearch(String queryString, int n)
            throws IOException, ParseException {
        Query query = parser.parse(queryString);
        return searcher.search(query, n);
    }

    public Document getDocument(int docId)
            throws IOException {
        return searcher.doc(docId);
    }
}