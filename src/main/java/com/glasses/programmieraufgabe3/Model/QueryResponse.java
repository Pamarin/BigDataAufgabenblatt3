package com.glasses.programmieraufgabe3.Model;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-06
 */
public class QueryResponse {
    private Query query;
    private SearchResponse response;
    private int classificationTP;
    private int classificationFP;
    private int classificationFN;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }

    public int getClassificationTP() {
        return classificationTP;
    }

    public void setClassificationTP(int classificationTP) {
        this.classificationTP = classificationTP;
    }

    public int getClassificationFP() {
        return classificationFP;
    }

    public void setClassificationFP(int classificationFP) {
        this.classificationFP = classificationFP;
    }

    public int getClassificationFN() {
        return classificationFN;
    }

    public void setClassificationFN(int classificationFN) {
        this.classificationFN = classificationFN;
    }
    
    public void addClassificationTP() {
        this.classificationTP++;
    }
    
    public void addClassificationFP() {
        this.classificationFP++;
    }
    
    public void addClassificationFN() {
        this.classificationFN++;
    }
    
    public double calculatePrecision() {
        // Zähler.
        double numerator = (double)this.classificationTP;
        // Nenner.
        double denuminator = (double)this.classificationTP + (double)this.classificationFP;
        
        // Counter null-division.
        if(denuminator > 0.0) {
            return numerator / denuminator;
        } else {
            return 0.0;
        }
    }
    
    public double calculateRecall() {
        // Zähler.
        double numerator = (double)this.classificationTP;
        // Nenner.
        double denuminator = (double)this.classificationTP + (double)this.classificationFN;
        
        // Counter null-division.
        if(denuminator > 0.0) {
            return numerator / denuminator;
        } else {
            return 0.0;
        }
    }
    
    public QueryResponse(Query query, SearchResponse response) {
        // Remember params.
        this.query = query;
        this.response = response;
        
        // Init classifications.
        this.classificationFN = 0;
        this.classificationFP = 0;
        this.classificationTP = 0;
    }

    @Override
    public String toString() {
        SearchHit[] searchHits = this.response.getHits().getHits();
        String searchHitsString = "[";
        for(int i=0; i<searchHits.length; i++) {
            if(i>0) {
                searchHitsString += ", ";
            }
            searchHitsString += searchHits[i].getId();
        }
        searchHitsString += "]";
        
        return "QueryResponse{" + "query=" + query.getTitle() +
                                ", tp=" + classificationTP +
                                ", fp=" + classificationFP +
                                ", fn=" + classificationFN +
                                ", precision=" + calculatePrecision() +
                                ", recall=" + calculateRecall() +
                                ", hits=" + this.response.getHits().getHits().length +
                                ", documentIds=" + searchHitsString +
                                '}';
    }
}
