package fr.endide.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message extends AbstractEntity{
    String text;
    String topic;
    String studentMail;
    Instant date;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public String getStudentMail() {
        return studentMail;
    }
    public void setStudentMail(String studentMail) {
        this.studentMail = studentMail;
    }
    public Instant getDate() {
        return date;
    }
    public void setDate(Instant date) {
        this.date = date;
    }

}
