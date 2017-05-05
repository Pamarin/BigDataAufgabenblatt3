package com.glasses.programmieraufgabe3.Model;

import java.util.Date;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Document {
    private long id;
    private String title;
    private Date pubDate;
    private String content;

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
    
    public Document(long id, String title, Date pubDate, String content) {
        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Document{" + "id=" + id + ", title=" + title + ", pubDate=" + pubDate + ", content=" + content + '}';
    }
}
