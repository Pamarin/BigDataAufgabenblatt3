package com.glasses.programmieraufgabe3.Model;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Judgement {
    private long queryId;
    private String documentId;
    private boolean relevance;

    public long getQueryId() {
        return queryId;
    }

    public void setQueryId(long queryId) {
        this.queryId = queryId;
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
    
    public Judgement(long queryId, String documentId, boolean relevance) {
        this.queryId = queryId;
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
        return "Judgement{" + "queryId=" + queryId + ", documentId=" + documentId + ", relevance=" + relevance + '}';
    }
}
