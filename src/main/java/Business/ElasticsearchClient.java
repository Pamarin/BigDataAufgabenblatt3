package Business;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
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
        System.out.println("Indexing JSON.");
        
        // Index json and get response.
        IndexResponse response = client.prepareIndex(index, type, id)
                                       .setSource(json)
                                       .get();
        
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
