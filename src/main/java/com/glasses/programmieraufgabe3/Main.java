package com.glasses.programmieraufgabe3;

import com.glasses.programmieraufgabe3.Business.ElasticsearchClient;
import com.glasses.programmieraufgabe3.Business.FileReader;
import com.glasses.programmieraufgabe3.Model.Document;
import com.glasses.programmieraufgabe3.Model.Judgement;
import com.glasses.programmieraufgabe3.Model.Query;
import java.io.IOException;
import java.util.ArrayList;
import org.elasticsearch.action.search.SearchResponse;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Main {
    // List of possible commands given through the command line.
    static final String INDEX_DOCUMENTS = "index_documents";
    static final String PROCESS_QUERIES = "process_queries";
    
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("#####################################################");
        System.out.println("#                                                   #");
        System.out.println("#  Willkommen bei Big Data - Programmieraufgabe 3!  #");
        System.out.println("#                                                   #");
        System.out.println("#####################################################");
        System.out.println("");
        
        if(args.length > 0) {
            // Get the command from the command line.
            String command = args[0];
            
            // Start the appriate part.
            switch(args[0]) {
                case INDEX_DOCUMENTS:
                    indexDocuments();
                    break;
                case PROCESS_QUERIES:
                    processQueries();
                    break;
                default:
                    System.out.println("Fehler: '" + command + "' ist kein gültiger Befehl.");
            }
        } else {
            System.out.println("Fehler: Parameter Befehl fehlt: " + INDEX_DOCUMENTS + " oder " + PROCESS_QUERIES);
        }
    }
    
    private static void indexDocuments() throws Exception {
        System.out.println("Dokumente indexieren.");
        
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
    
    private static void processQueries() throws IOException, Exception {
        System.out.println("Queries verarbeiten.");
        
        // Lists for queries and judgements.
        ArrayList<Query> queries = new ArrayList<>();
        ArrayList<Judgement> judgements = new ArrayList<>();
        
        // Access queries file.
        FileReader readerQueries = new FileReader(("/home/jean/Schreibtisch/Big Data/Aufgabenblatt 3/assignment1-data/queries.tsv"));
        // Read queries file.
        readerQueries.read(false);
        System.out.println("Gefundene Queries: " + readerQueries.getLines().size());
        
        // Access judgements file.
        FileReader readerJudgements = new FileReader(("/home/jean/Schreibtisch/Big Data/Aufgabenblatt 3/assignment1-data/judgments.tsv"));
        // Read judgements file.
        readerJudgements.read(false);
        System.out.println("Gefundene Judgements: " + readerJudgements.getLines().size());
        
        // Fill list with queries.
        for(String line : readerQueries.getLines()) {
            // Parse from tabulator-separated line to Query class.
            Query query = Query.parse(line);
            
            // Add to queries list.
            queries.add(query);
        }
        
        // Fill list with judgements.
        // Remove non-relevant judgements.
        for(String line : readerJudgements.getLines()) {
            // Parse from tabulator-separated line to Judgements class.
            Judgement judgement = Judgement.parse(line);
            
            // Add to judgements list, if relevant.
            if(judgement.isRelevance()) {
                // Add to judgments list.
                judgements.add(judgement);
            }
        }
        
        System.out.println("Relevante Judgements: " + judgements.size());
        
        // Connect to Elasticsearch node.
        ElasticsearchClient client = new ElasticsearchClient("localhost", 9300);
        
        // Define search terms.
        String searchTerms = "International Organized Crime";
        
        // Define the indices.
        ArrayList<String> relevantIds = new ArrayList<>();
        relevantIds.add("CR93E-1282");
        relevantIds.add("CR93E-3103");
        relevantIds.add("CR93E-5796");
        
        // Search in Elasticsearch.
        SearchResponse response = client.search("documents", "document", "title", searchTerms, relevantIds);
        System.out.println(response);
        
        // Close connection.
        client.closeConnection();
    }
}
