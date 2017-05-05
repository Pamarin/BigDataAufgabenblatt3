package com.glasses.programmieraufgabe3;

import com.glasses.programmieraufgabe3.Business.ElasticsearchClient;
import com.glasses.programmieraufgabe3.Business.FileReader;
import com.glasses.programmieraufgabe3.Model.Document;
import java.io.IOException;



/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Main {
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Willkommen bei Big Data - Programmieraufgabe 3!");
        
        // Connect to Elasticsearch node.
        ElasticsearchClient client = new ElasticsearchClient("localhost", 9300);
        
        // Access file.
        FileReader reader = new FileReader("/home/jean/Schreibtisch/Big Data/Aufgabenblatt 3/assignment1-data/documents.json");
        // Read file.
        reader.read(true);
        System.out.println("Gefundene Zeilen: " + reader.getLines().size());
        
        // Iterate through every line.
        for(String line : reader.getLines()) {
            // Parse from JSON to Document class.
            Document document = Document.parse(line);
            
            // Index document.
            client.index(document.toJSON(), "documents", "document", document.getId());
        }
        
        // Close connection.
        client.closeConnection();
    }
}
