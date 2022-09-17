package fr.endide.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cards")
public class Cards extends AbstractEntity{
    String email;
    String name;
    String description;
    String link;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

}
