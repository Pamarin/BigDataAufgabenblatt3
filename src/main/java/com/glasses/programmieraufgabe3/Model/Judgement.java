package com.glasses.programmieraufgabe3.Model;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Judgement {
    private long id;
    private String documentId;
    private boolean relevance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public boolean isRelevance() {
        return relevance;
    }

    public void setRelevance(boolean relevance) {
        this.relevance = relevance;
    }
    
    public Judgement(long id, String documentId, boolean relevance) {
        this.id = id;
        this.documentId = documentId;
        this.relevance = relevance;
    }
    
    public static Judgement parse(String tsvLine) {
        // Split line by tabulator.
        String[] cells = tsvLine.split("\t");
        
        // Create new Judgement object and return it.
        return new Judgement(Integer.parseInt(cells[0]), cells[1], "1".equals(cells[2]));
    }

    @Override
    public String toString() {
        return "Judgement{" + "id=" + id + ", documentId=" + documentId + ", relevance=" + relevance + '}';
    }
}
