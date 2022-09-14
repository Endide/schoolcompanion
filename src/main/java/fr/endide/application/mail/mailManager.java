package fr.endide.application.mail;


import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class mailManager {
    public static void sendMessage(String key, String destinataire) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setSSLOnConnect(true);
        email.setAuthentication("your-account-name@gmail.com", "your-password");
        email.setFrom(destinataire);
        email.setSubject("SchoolCompanion");
        email.setHtmlMsg("Votre Mot de passe" + key);
        email.send();
    }
}
