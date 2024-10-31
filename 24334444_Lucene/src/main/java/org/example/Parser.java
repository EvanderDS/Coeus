package org.example;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static List<Document> parse(String filePath) throws IOException {
        List<Document> documents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder authorBuilder = new StringBuilder();
            StringBuilder biblioBuilder = new StringBuilder();
            StringBuilder wordsBuilder = new StringBuilder();
            String docId = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(".I")) {
                    if (docId != null) {
                        documents.add(createLuceneDocument(docId, titleBuilder.toString(), authorBuilder.toString(), biblioBuilder.toString(), wordsBuilder.toString()));
                    }
                    docId = line.substring(3).trim();
                    titleBuilder.setLength(0);
                    authorBuilder.setLength(0);
                    biblioBuilder.setLength(0);
                    wordsBuilder.setLength(0);
                }
                else if (line.startsWith(".T")) {
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        titleBuilder.append(line).append(" ");
                    }
                }
                else if (line.startsWith(".A")) {
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        authorBuilder.append(line).append(" ");
                    }
                }
                else if (line.startsWith(".B")) {
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        biblioBuilder.append(line).append(" ");
                    }
                }
                else if (line.startsWith(".W")) {
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        wordsBuilder.append(line).append(" ");
                    }
                }
            }
            if (docId != null) {
                documents.add(createLuceneDocument(docId, titleBuilder.toString(), authorBuilder.toString(), biblioBuilder.toString(), wordsBuilder.toString()));
            }
        }
        return documents;
    }

    private static Document createLuceneDocument(String docId, String title, String author, String biblio, String words) {
        Document document = new Document();
        document.add(new StringField("docid", docId, Field.Store.YES));
        document.add(new TextField("title", title, Field.Store.YES));
        document.add(new TextField("author", author, Field.Store.YES));
        document.add(new TextField("biblio", biblio, Field.Store.YES));
        document.add(new TextField("words", words, Field.Store.YES));
        return document;
    }
}
