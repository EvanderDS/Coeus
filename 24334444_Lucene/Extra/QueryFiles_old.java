package org.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
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
import org.example.CustomAnalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class QueryFiles_old {

    public static void main(String[] args) throws Exception {
        // Paths to index and queries
        String index = "D:\\24334444_Lucene\\index";  // Your index directory path
        String resultsPath = "D:\\24334444_Lucene\\results\\my-results.txt";  // Output results file path
        String queriesPath = "D:\\24334444_Lucene\\data\\cran\\cran.qry";  // Queries file path

        // Create a reader to read the index
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        PrintWriter writer = new PrintWriter(resultsPath, StandardCharsets.UTF_8);
        IndexSearcher searcher = new IndexSearcher(reader);

        // Set the similarity method
        searcher.setSimilarity(new BM25Similarity());  // BM25 scoring method

        // Use the CustomAnalyzer for the same preprocessing as documents
        Analyzer analyzer = new CustomAnalyzer();

        // Query parser for multiple fields
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] {"title", "author", "bibliography", "content"}, analyzer);

        // Read in the queries
        BufferedReader buffer = Files.newBufferedReader(Paths.get(queriesPath), StandardCharsets.UTF_8);

        StringBuilder queryString = new StringBuilder();
        int queryNumber = 1;  // Start with the first query
        String line;
        boolean first = true;

        System.out.println("Reading in queries and creating search results.");

        // Loop through all 225 queries
        while ((line = buffer.readLine()) != null) {
            if (line.startsWith(".I")) {
                // Process the previous query
                if (!first) {
                    Query query = parser.parse(QueryParser.escape(queryString.toString()));

                    // Add print statement to indicate which query is being processed
                    System.out.println("Processing query number: " + queryNumber + " with query: " + queryString.toString().trim());

                    performSearch(searcher, writer, queryNumber, query);
                    queryNumber++;
                } else {
                    first = false;
                }
                queryString = new StringBuilder();
            } else if (!line.startsWith(".")) {
                queryString.append(" ").append(line);
            }
        }

        // Process the last query
        Query query = parser.parse(QueryParser.escape(queryString.toString()));

        // Add print statement for the last query
        System.out.println("Processing last query number: " + queryNumber + " with query: " + queryString.toString().trim());

        performSearch(searcher, writer, queryNumber, query);

        // Close the writer and reader
        writer.close();
        reader.close();

        System.out.println("Search complete and results saved.");
    }

    // Performs search and records the top 50 documents for each query
    public static void performSearch(IndexSearcher searcher, PrintWriter writer, int queryNumber, Query query) throws IOException {
        // Limit results to the top 50
        TopDocs results = searcher.search(query, 50);
        ScoreDoc[] hits = results.scoreDocs;

        for (int i = 0; i < hits.length; i++) {
            Document doc = searcher.doc(hits[i].doc);
            // Assuming 'doc' has a method to get the unique document ID (you may need to adjust this based on your document structure)
            String docId = doc.get("id"); // Replace with the actual field name for the document ID
            writer.println(String.format(Locale.ENGLISH, "%d Q0 %s %d %.4f EXP",
                    queryNumber, docId, i + 1, hits[i].score));
        }
    }
}
