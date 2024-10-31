package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Indexer {

    private Indexer() {}

    public static void indexFiles(String docsPath, String indexPath) {
        final Path docDir = Paths.get(docsPath);
        if (!Files.isReadable(docDir)) {
            System.out.println("Document directory '" + docDir.toAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }

        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            CustomAnalyzer analyzer = new CustomAnalyzer();

            // Create IndexWriter
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);

            // Index the files
            indexFiles(writer, docDir);
            writer.close();

        } catch (IOException e) {
            System.out.println("Caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    static Document createDocument(String id, String title, String author, String bib, String content) {
        Document doc = new Document();
        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("author", author, Field.Store.YES));
        doc.add(new TextField("bibliography", bib, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));
        return doc;
    }

    static void indexFiles(IndexWriter writer, Path file) throws IOException {
        try (InputStream stream = Files.newInputStream(file)) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String id = "";
            StringBuilder title = new StringBuilder();
            StringBuilder author = new StringBuilder();
            StringBuilder bib = new StringBuilder();
            StringBuilder content = new StringBuilder();
            String state = "";
            boolean first = true;
            String line;

            System.out.println("Indexing documents.");

            while ((line = buffer.readLine()) != null) {
                switch (line.substring(0, 2)) {
                    case ".I":
                        if (!first) {
                            Document d = createDocument(id, title.toString(), author.toString(), bib.toString(), content.toString());
                            writer.addDocument(d);
                        } else {
                            first = false;
                        }
                        title = new StringBuilder(); author = new StringBuilder(); bib = new StringBuilder(); content = new StringBuilder();
                        id = line.substring(3);
                        break;
                    case ".T":
                    case ".A":
                    case ".B":
                    case ".W":
                        state = line;
                        break;
                    default:
                        switch (state) {
                            case ".T": title.append(line).append(" "); break;
                            case ".A": author.append(line).append(" "); break;
                            case ".B": bib.append(line).append(" "); break;
                            case ".W": content.append(line).append(" "); break;
                        }
                }
            }
            Document d = createDocument(id, title.toString(), author.toString(), bib.toString(), content.toString());
            writer.addDocument(d);
        }
    }
}
