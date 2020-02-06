package pl.edu.agh.zarzecze.gradebook.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MailSender {

    static final String username = System.getenv("EMAIL_LOGIN");
    static final String password = System.getenv("EMAIL_PASSWORD");

    public static void sendPassword(String email, String firstname, String lastname, String generatedPassword) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            String messageContent = String.format("Hello, %s %s. \r\n\r\nHere is your gradebook password: %s",
                    firstname, lastname, generatedPassword);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gradebook.agh@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Gradebook password.");
            message.setContent(messageContent, "text/plain; charset=UTF-8");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
