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
    // Server settings.
    static final String HOST = "localhost";
    static final int PORT = 9300;
    
    // File settings.
    static final String FILE_DOCUMENTS = "/home/jean/Schreibtisch/Big Data/Aufgabenblatt 3/assignment1-data/documents.json";
    static final String FILE_QUERIES = "/home/jean/Schreibtisch/Big Data/Aufgabenblatt 3/assignment1-data/queries.tsv";
    static final String FILE_JUDGEMENTS = "/home/jean/Schreibtisch/Big Data/Aufgabenblatt 3/assignment1-data/judgments.tsv";
    
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
            
            // Connect to Elasticsearch node.
            ElasticsearchClient client = new ElasticsearchClient(HOST, PORT);

            // Start the appriate part.
            switch(args[0]) {
                case INDEX_DOCUMENTS:
                    indexDocuments(client);
                    break;
                case PROCESS_QUERIES:
                    processQueries(client);
                    break;
                default:
                    System.out.println("Fehler: '" + command + "' ist kein gültiger Befehl.");
            }
            
            // Close connection.
            client.closeConnection();
        } else {
            System.out.println("Fehler: Parameter Befehl fehlt: " + INDEX_DOCUMENTS + " oder " + PROCESS_QUERIES);
        }
    }
    
    private static void indexDocuments(ElasticsearchClient client) throws Exception {
        System.out.println("Dokumente indexieren.");
        
        // Access file.
        FileReader reader = new FileReader(FILE_DOCUMENTS);
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
    }
    
    private static ArrayList<Query> loadQueries() throws IOException {
        System.out.println("Load queries from file.");
        
        // List for queries.
        ArrayList<Query> queries = new ArrayList<>();
        
        // Access queries file.
        FileReader readerQueries = new FileReader((FILE_QUERIES));
        // Read queries file.
        readerQueries.read(false);
        System.out.println("Gefundene Queries: " + readerQueries.getLines().size());
        
        // Fill list with queries.
        for(String line : readerQueries.getLines()) {
            // Parse from tabulator-separated line to Query class.
            Query query = Query.parse(line);
            
            // Add to queries list.
            queries.add(query);
        }
        
        return queries;
    }
    
    private static ArrayList<Judgement> loadJudgements() throws IOException {
        System.out.println("Load judgements from file.");
        
        // List for judgements.
        ArrayList<Judgement> judgements = new ArrayList<>();
        // Access judgements file.
        FileReader readerJudgements = new FileReader((FILE_JUDGEMENTS));
        // Read judgements file.
        readerJudgements.read(false);
        System.out.println("Gefundene Judgements: " + readerJudgements.getLines().size());
        
        // Fill list with judgements.
        // Remove non-relevant judgements.
        for(String line : readerJudgements.getLines()) {
            // Parse from tabulator-separated line to Judgements class.
            Judgement judgement = Judgement.parse(line);
            
            /*
            // Add to judgements list, if relevant.
            if(judgement.isRelevance()) {
                // Add to judgments list.
                judgements.add(judgement);
            }
            */
            
            // Add to judgments list.
            judgements.add(judgement);
        }
        
        return judgements;
    }
    
    private static void processQueries(ElasticsearchClient client) throws IOException, Exception {
        System.out.println("Queries verarbeiten.");
        
        // Load queries and judgements.
        ArrayList<Query> queries = loadQueries();
        ArrayList<Judgement> judgements = loadJudgements();
        
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
    }
}
