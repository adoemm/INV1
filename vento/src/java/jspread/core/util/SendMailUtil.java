package jspread.core.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author JeanPaul
 */
public final class SendMailUtil {

    private static final String version = "V0.5";
    private static Properties props = null;
    private static Transport transport = null;
    private static Session session = null;

    public static void initialize() throws NoSuchProviderException {
        //hotmail
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.smtp.starttls.enable", "true");
//        props.setProperty("mail.smtp.host", "smtp.live.com");
//        props.setProperty("mail.smtp.port", "587");
//        props.setProperty("mail.smtp.auth", "true");
//        props.setProperty("mail.smtp.user", "unimatrix845@hotmail.com");

        //gmail
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.smtp.starttls.enable", "true");
//        props.setProperty("mail.smtp.host", "smtp.gmail.com");
//        props.setProperty("mail.smtp.port", "587");
//        props.setProperty("mail.smtp.auth", "true");
//        props.setProperty("mail.smtp.user", "cecytem.v.urrutia@gmail.com");


        props = new Properties();

        props.setProperty("mail.transport.protocol", PageParameters.getParameter("mail.transport.protocol").toString());
        props.setProperty("mail.smtp.auth", PageParameters.getParameter("mail.smtp.auth").toString());
        props.setProperty("mail.smtp.starttls.enable", PageParameters.getParameter("mail.smtp.starttls.enable").toString());
        props.setProperty("mail.smtp.host", PageParameters.getParameter("mail.smtp.host").toString());
        props.setProperty("mail.smtp.port", PageParameters.getParameter("mail.smtp.port").toString());
        props.setProperty("mail.smtp.user", PageParameters.getParameter("mail.smtp.user").toString());
        //session = Session.getDefaultInstance(props, null);
        //transport = session.getTransport(PageParameters.getParameter("mail.transport.protocol").toString());
    }

    private static boolean sendMailito(LinkedList<String> recipient, String subject, String text) {
        boolean success = false;
        try {
            session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PageParameters.getParameter("mail.smtp.user").toString(), PageParameters.getParameter("mail.smtp.password").toString());
                }
            });
            transport = session.getTransport(PageParameters.getParameter("mail.transport.protocol").toString());

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(PageParameters.getParameter("mail.smtp.user").toString()));

            Iterator it = recipient.iterator();
            while (it.hasNext()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(it.next().toString()));
            }
            message.setSubject(subject);
            message.setText(text);

            if (!transport.isConnected()) {
                openConnection();
            }

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            success = true;
            //System.out.println("Correo Enviado exitosamente!");
        } catch (Exception ex) {
            Logger.getLogger(SendMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    public static String sendMail(LinkedList<String> recipient, String subject, String text) {
        String success = "false";
        try {
            session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PageParameters.getParameter("mail.smtp.user").toString(), PageParameters.getParameter("mail.smtp.password").toString());
                }
            });
            transport = session.getTransport(PageParameters.getParameter("mail.transport.protocol").toString());
            transport.connect(PageParameters.getParameter("mail.smtp.user").toString(), PageParameters.getParameter("mail.smtp.password").toString());

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(PageParameters.getParameter("mail.smtp.user").toString()));

            Iterator it = recipient.iterator();
            while (it.hasNext()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(it.next().toString()));
            }

            message.setSubject(subject);
            message.setText(text);

            if (!transport.isConnected()) {
                openConnection();
            }
            transport.sendMessage(message, message.getAllRecipients());
            //Transport.send(message);
            transport.close();
            success = "true";
        } catch (Exception ex) {
            success = "error";
            Logger.getLogger(SendMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    public static String sendMail(LinkedList<String> recipient, String subject, String text, LinkedList<String> filesToSend) {
        String success = "false";
        try {
            session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PageParameters.getParameter("mail.smtp.user").toString(), PageParameters.getParameter("mail.smtp.password").toString());
                }
            });
            transport = session.getTransport(PageParameters.getParameter("mail.transport.protocol").toString());
            transport.connect(PageParameters.getParameter("mail.smtp.user").toString(), PageParameters.getParameter("mail.smtp.password").toString());

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(PageParameters.getParameter("mail.smtp.user").toString()));

            Iterator<String> it = recipient.iterator();
            while (it.hasNext()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(it.next().toString()));
            }

            Multipart multipart = new MimeMultipart();
            String fileName = null;
            it = filesToSend.iterator();
            while (it.hasNext()) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                fileName = it.next();
                DataSource source = new FileDataSource(PageParameters.getParameter("mail.attachments.path") + fileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);
            }

            message.setSubject(subject);
            message.setText(text);
            message.setContent(multipart);

            if (!transport.isConnected()) {
                openConnection();
            }
            transport.sendMessage(message, message.getAllRecipients());
            //Transport.send(message);
            transport.close();
            success = "true";
        } catch (Exception ex) {
            success = "error";
            Logger.getLogger(SendMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    private static void openConnection() throws MessagingException {
        transport.connect(PageParameters.getParameter("mail.smtp.user").toString(), PageParameters.getParameter("mail.smtp.password").toString());
    }

    public static void closeConnection() throws MessagingException {
        transport.close();
    }
}
