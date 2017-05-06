package com.glasses.programmieraufgabe3;

import com.glasses.programmieraufgabe3.Business.ElasticsearchClient;
import com.glasses.programmieraufgabe3.Business.FileReader;
import com.glasses.programmieraufgabe3.Model.Document;
import com.glasses.programmieraufgabe3.Model.Judgement;
import com.glasses.programmieraufgabe3.Model.Query;
import com.glasses.programmieraufgabe3.Model.QueryResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

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
    public static final String PROCESS_QUERIES_BOOLEAN = "boolean";
    public static final String PROCESS_QUERIES_TF_IDF = "tfidf";
    public static final String PROCESS_QUERIES_BM25 = "bm25";
    
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("#####################################################");
        System.out.println("#                                                   #");
        System.out.println("#  Willkommen bei Big Data - Programmieraufgabe 3!  #");
        System.out.println("#                                                   #");
        System.out.println("#####################################################");
        System.out.println("");
        
        // Check if one argument has been given.
        if(args.length > 0) {
            // Get the first command from the command line.
            String command = args[0];
            
            // Connect to Elasticsearch node.
            ElasticsearchClient client = new ElasticsearchClient(HOST, PORT);

            // Start the appriate part.
            switch(args[0]) {
                case INDEX_DOCUMENTS:
                    // Index the documents.
                    indexDocuments(client);
                    break;
                case PROCESS_QUERIES:
                    // Check if two arguments have been given.
                    if(args.length > 1) {
                        // Get the second command from command line.
                        String algorithm = args[1];
                        // Process the queries.
                        processQueries(client, algorithm);
                    } else {
                        System.out.println("Fehler: Dem ersten Parameter " + PROCESS_QUERIES + " fehlt noch der zweite Parameter Suchart: [" + PROCESS_QUERIES_BOOLEAN + ", " + PROCESS_QUERIES_TF_IDF + ", " + PROCESS_QUERIES_BM25 + "]");
                    }
                    break;
                default:
                    System.out.println("Fehler: '" + command + "' ist kein gültiger Befehl.");
            }
            
            // Close connection.
            client.closeConnection();
        } else {
            System.out.println("Fehler: Parameter Befehl fehlt: [" + INDEX_DOCUMENTS + ", " + PROCESS_QUERIES + "]");
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
            
            // Add to judgments list.
            judgements.add(judgement);
        }
        
        return judgements;
    }
    
    private static Judgement findJudgement(ArrayList<Judgement> judgements, String documentId, long queryId) {
        Judgement foundJudgement = null;
        
        // Search for appropriate judgement.
        for(Judgement judgement : judgements) {
            // Check if same query id.
            if(queryId == judgement.getQueryId()) {
                // Check if same document id.
                if(documentId.equals(judgement.getDocumentId())) {
                    // Remember judgement.
                    foundJudgement = judgement;
                    break;
                }
            }
        }
        
        return foundJudgement;
    }
    
    private static void processQueries(ElasticsearchClient client, String searchAlgorithm) throws IOException, Exception {
        System.out.println("Queries verarbeiten.");
        
        // Load queries and judgements.
        ArrayList<Query> queries = loadQueries();
        ArrayList<Judgement> judgements = loadJudgements();
        
        // Remember Elasticsearch results from every query.
        ArrayList<QueryResponse> responses = new ArrayList<>();
        
        // Execute every query and collect the responses.
        for(Query query : queries) {
            // Query Elasticsearch and get response.
            SearchResponse response = client.search("documents", "document", "content", query.getTitle(), searchAlgorithm);
            
            // Create query response.
            QueryResponse queryResponse = new QueryResponse(query, response);
            
            // Get hits from response.
            SearchHit[] searchHits = response.getHits().getHits();
            
            // Compare every hit.
            for(SearchHit searchHit : searchHits) {
                // Remember document id.
                String documentId = searchHit.getId();
                // Remember query id.
                long queryId = query.getId();
                // Find appropriate judgement.
                Judgement judgement = findJudgement(judgements, documentId, queryId);
                
                // Cound classifications.
                if(judgement == null) {
                    // Add +1 to FN classification.
                    queryResponse.addClassificationFN();
                } else {
                    if(judgement.isRelevance()) {
                        // Add +1 to TP classification.
                        queryResponse.addClassificationTP();
                    } else {
                        // Add +1 to FP classification.
                        queryResponse.addClassificationFP();
                    }
                }
            }
            
            // Add query and response to list.
            responses.add(queryResponse);
        }
        
        // Output.
        System.out.println("Anzahl Responses: " + responses.size());
        for(QueryResponse response : responses) {
            System.out.println(response);
        }
        System.out.println("MAP (Mean Average Precision): " + QueryResponse.calculateMeanAveragePrecision(responses));
    }
}
