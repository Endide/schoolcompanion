package fr.endide.application.data.entity;

public class Message {
    String text;
    String topic;
    Student author;

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
    public Student getAuthor() {
        return author;
    }
    public void setAuthor(Student author) {
        this.author = author;
    }
}
