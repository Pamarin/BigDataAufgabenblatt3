package com.glasses.programmieraufgabe3.Model;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Query {
    private long id;
    private String title;
    private String description;
    private String narrative;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    @Override
    public String toString() {
        return "Query{" + "id=" + id + ", title=" + title + ", description=" + description + ", narrative=" + narrative + '}';
    }
}
