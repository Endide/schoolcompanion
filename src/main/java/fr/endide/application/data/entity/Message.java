package fr.endide.application.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "messages")
public class Message extends AbstractEntity {
    @Column(length=2000)
    String text;
    String author;
    Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topics_id")
    private Topics topics;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }

}
