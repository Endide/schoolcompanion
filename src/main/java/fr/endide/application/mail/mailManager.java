package fr.endide.application.mail;


import org.apache.commons.mail.EmailException;

public class mailManager {
    public static void sendMessage(String key, String destination) throws EmailException {
        System.out.println("Sending mail to " + destination);
        System.out.println("Key is " + key);
    }
}
