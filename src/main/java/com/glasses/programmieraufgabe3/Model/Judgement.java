package com.glasses.programmieraufgabe3.Model;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Judgement {
    private long id;
    private long documentId;
    private boolean relevance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }

    public boolean isRelevance() {
        return relevance;
    }

    public void setRelevance(boolean relevance) {
        this.relevance = relevance;
    }
    
    public Judgement(long id, long documentId, boolean relevance) {
        this.id = id;
        this.documentId = documentId;
        this.relevance = relevance;
    }

    @Override
    public String toString() {
        return "Judgement{" + "id=" + id + ", documentId=" + documentId + ", relevance=" + relevance + '}';
    }
}
