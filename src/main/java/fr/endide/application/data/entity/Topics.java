package fr.endide.application.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Entity
@Table(name = "topics")
public class Topics extends AbstractEntity {
    String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topics", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<Message> messages = new ArrayList<>(); 

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    } 

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    

}
