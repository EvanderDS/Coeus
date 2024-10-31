package org.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class QueryFiles {

    public static void main(String[] args) throws Exception {
        String index = "D:\\24334444_Lucene\\index";
        String queriesPath = "D:\\24334444_Lucene\\data\\cran\\cran.qry";

        // Create a index
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));

        // Similarity methods
        BM25Similarity bm25Similarity = new BM25Similarity();
        ClassicSimilarity classicSimilarity = new ClassicSimilarity();
        CustomTFIDFSimilarity tfidfSimilarity = new CustomTFIDFSimilarity();
        LMDirichletSimilarity lmDirichletSimilarity = new LMDirichletSimilarity();

        // Define similarity names for output files
        String[] similarityNames = {
                "BM25",
                "Classic",
                "TFIDF", // Custom TF-IDF
                "LMDirichlet"
        };

        // Use the CustomAnalyzer
        Analyzer analyzer = new CustomAnalyzer();

        // Query parser for multiple fields
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"title", "author", "bibliography", "content"}, analyzer);

        System.out.println("Reading in queries and creating search results.");

        // Loop through all 225 queries
        for (int i = 0; i < similarityNames.length; i++) {
            // Set the similarity method
            IndexSearcher searcher = new IndexSearcher(reader);
            switch (i) {
                case 0:
                    searcher.setSimilarity(bm25Similarity);
                    break;
                case 1:
                    searcher.setSimilarity(classicSimilarity);
                    break;
                case 2:
                    searcher.setSimilarity(tfidfSimilarity);
                    break;
                case 3:
                    searcher.setSimilarity(lmDirichletSimilarity);
                    break;
            }

            String resultsPath = "D:\\24334444_Lucene\\results\\FifthItr\\" + similarityNames[i] + "-results.txt";
            try (PrintWriter writer = new PrintWriter(resultsPath);
                 BufferedReader buffer = Files.newBufferedReader(Paths.get(queriesPath), StandardCharsets.UTF_8)) {

                StringBuilder queryString = new StringBuilder();
                int queryNumber = 1;
                String line;

                while ((line = buffer.readLine()) != null) {
                    if (line.startsWith(".I")) {
                        if (!queryString.isEmpty()) {
                            Query query = parser.parse(QueryParser.escape(queryString.toString()));
                            System.out.println("Processing query number: " + queryNumber + " with query: " + queryString.toString().trim());
                            performSearch(searcher, writer, queryNumber, query);
                            queryNumber++;
                        }
                        queryString.setLength(0);
                    } else if (!line.startsWith(".")) {
                        queryString.append(" ").append(line);
                    }
                }

                // Process the last query if it exists
                if (!queryString.isEmpty()) {
                    Query query = parser.parse(QueryParser.escape(queryString.toString()));
                    performSearch(searcher, writer, queryNumber, query);
                }
            }
        }

        reader.close();

        System.out.println("Search complete and results saved in results/FifthItr/ folder.");
    }

    public static void performSearch(IndexSearcher searcher, PrintWriter writer, int queryNumber, Query query) throws IOException {
        // The top 50
        TopDocs results = searcher.search(query, 50);
        ScoreDoc[] hits = results.scoreDocs;

        for (int i = 0; i < hits.length; i++) {
            Document doc = searcher.doc(hits[i].doc);
            String docId = doc.get("id");
            writer.println(String.format(Locale.ENGLISH, "%d Q0 %s %d %.4f EXP",
                    queryNumber, docId, i + 1, hits[i].score));
        }
    }
}
