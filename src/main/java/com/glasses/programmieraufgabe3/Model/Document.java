package com.glasses.programmieraufgabe3.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Document {
    private String id;
    private String title;
    @JsonProperty(value = "pub-date")
    private Date pubDate;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public Document() {}
    
    public Document(String id, String title, Date pubDate, String content) {
        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.content = content;
    }
    
    public static Document parse(String json) throws IOException {
        // Create Jackson mapper for parsing.
        ObjectMapper mapper = new ObjectMapper();
        
        // Parse JSON to Document type.
        Document document = mapper.readValue(json, Document.class);
        
        return document;
    }
    
    public String toJSON() throws JsonProcessingException {
        // Create Jackson mapper for parsing.
        ObjectMapper mapper = new ObjectMapper();
        
        // Parse Document type to JSON.
        String json = mapper.writeValueAsString(this);
        
        return json;
    }

    @Override
    public String toString() {
        return "Document{" + "id=" + id + ", title=" + title + ", pubDate=" + pubDate + ", content=" + content + '}';
    }
}
