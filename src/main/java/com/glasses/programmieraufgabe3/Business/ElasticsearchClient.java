package com.glasses.programmieraufgabe3.Business;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-05
 */
public class ElasticsearchClient {
    private String host;
    private int port;
    private TransportClient client;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public ElasticsearchClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        
        openConnection();
    }
    
    public void openConnection() throws UnknownHostException, Exception {
        System.out.println("Open connection to " + this.host + ":" + this.port + ".");
        
        if(this.client == null) {
            // Connection startup.
            this.client = new PreBuiltTransportClient(Settings.EMPTY)
                                         .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(this.host), this.port));
        } else {
            throw new Exception("Connection still open.");
        }
    }
    
    public IndexResponse index(String json, String index, String type, String id) {
        System.out.println("Indexing index '" + index + "', type '" + type + "', id '" + id + "'.");
        
        // Index json and get response.
        IndexResponse response = client.prepareIndex(index, type, id)
                                       .setSource(json)
                                       .get();
        
        return response;
    }
    
    public SearchResponse search(String index, String type, String fieldToSearchIn, String searchTerms, List<String> relevantIds) {
        // Query for Elasticsearch.
        // For more information Elasticsearch API query building: https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search.html
        /*
        SearchResponse response = this.client.prepareSearch(index)
                                             .setTypes(type)
                                             .setQuery(QueryBuilders.termsQuery(fieldToSearchIn, searchTerms))
                                             .get();
        */
        
        // Convert relevantIds array to JSON array.
        String relevantIdsString = "[";
        for(int i=0; i<relevantIds.size(); i++) {
            // If not the first entry, add a comma.
            if(i>0) {
                relevantIdsString += ", ";
            }
            
            // Add relevant id.
            relevantIdsString += "\"" + relevantIds.get(i) + "\"";
        }
        relevantIdsString += "]";
        
        // Generate search script.
        String searchScript = "{\n" +
                              "  \"query\": {\n" +
                              "    \"bool\": {\n" +
                              "      \"should\": {\n" +
                              "        \"match\": {\n" +
                              "          \"content\": \"" + searchTerms + "\"\n" +
                              "        }\n" +
                              "      },\n" +
                              "      \"filter\": {\n" +
                              "        \"terms\": {\n" +
                              "          \"_id\": " + relevantIdsString + "\n" +
                              "\n" +
                              "        }\n" +
                              "      }\n" +
                              "    }\n" +
                              "  }\n" +
                              "}\n" +
                              "'";
        
        // Search in Elasticsearch.
        SearchResponse response = new SearchTemplateRequestBuilder(client)
                                        .setScript(searchScript)
                                        .setScriptType(ScriptType.INLINE)
                                        .setRequest(new SearchRequest())
                                        .get()
                                        .getResponse();
        
        return response;
    }
    
    public void closeConnection() throws Exception {
        System.out.println("Close connection to " + this.host + ":" + this.port + ".");
        
        if(this.client != null) {
            // Connection shutdown.
            this.client.close();
        } else {
            throw new Exception("No connection to close.");
        }
    }
}
