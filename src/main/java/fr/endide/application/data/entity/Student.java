package fr.endide.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Student extends AbstractEntity {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String schoolLevel;
    @JsonIgnore
    private String hashedPassword;
    private String role;
    @Lob
    @Column(length = 1000000)
    private byte[] profilePicture;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String name) {
        this.firstName = name;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String name) {
        this.lastName = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSchoolLevel() {
        return schoolLevel;
    }
    public void setSchoolLevel(String schoolLevel) {
        this.schoolLevel = schoolLevel;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public String getRoles() {
        return role;
    }
    public void setRoles(String role) {
        this.role = role;
    }
    public byte[] getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

}
