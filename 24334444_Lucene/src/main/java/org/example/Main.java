package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.print("Hello and welcome!\n");

        try {
            String datasetPath = "D:\\24334444_Lucene\\data\\cran";
            String indexPath = "D:\\24334444_Lucene\\index";

            // Index the documents
            Indexer.indexFiles(datasetPath + "\\cran.all.1400", indexPath);

            System.out.println("Documents indexed successfully!");
            IndexerViewer.main(args);
        } catch (Exception e) {
            System.err.println("An error occurred while using Lucene: " + e.getMessage());
        }

        System.out.println("Starting Lucene Search...");

        try {
            QueryFiles.main(args);
            /* QueryFiles_old.main(args); */
        } catch (Exception e) {
            System.err.println("An error occurred during search: " + e.getMessage());
        }
    }
}
