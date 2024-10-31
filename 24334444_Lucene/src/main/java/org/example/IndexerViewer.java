package org.example;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.FSDirectory;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class IndexerViewer {

    public static void main(String[] args) {
        String indexPath = "D:\\24334444_Lucene\\index";
        try {
            FSDirectory directory = FSDirectory.open(Paths.get(indexPath));
            IndexReader reader = DirectoryReader.open(directory);
            int numDocs = reader.numDocs();

            System.out.printf("Total number of indexed documents: %d\n", numDocs);

            // Prompt to display titles
            Scanner scanner = new Scanner(System.in);
            System.out.print("Do you want to display the title names? (yes/no): ");
            String showTitles = scanner.nextLine();

            if (showTitles.equalsIgnoreCase("yes")) {
                for (int i = 0; i < numDocs; i++) {
                    Document doc = reader.document(i);
                    String title = doc.get("title");
                    System.out.printf("Title [%d]: %s\n", i, title);
                }
            }

            // Prompt to display authors
            System.out.print("Do you want to display the authors and their corresponding index numbers? (yes/no): ");
            String showAuthors = scanner.nextLine();

            if (showAuthors.equalsIgnoreCase("yes")) {
                for (int i = 0; i < numDocs; i++) {
                    Document doc = reader.document(i);
                    String author = doc.get("author");
                    System.out.printf("Author [%d]: %s\n", i, author);
                }
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("An error occurred while accessing the index: " + e.getMessage());
        }
    }
}
