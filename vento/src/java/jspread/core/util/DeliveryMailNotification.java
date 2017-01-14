/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jspread.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import jspread.core.db.QUID;

/**
 *
 * @author VIOX
 */
public class DeliveryMailNotification {

    private final HashMap<String, String> hp = new HashMap();

    public void sendMailNotification(LinkedList<MailAccount> mailAccounts, int mailsPerAccount, LinkedList<String> recipient, String subject, String text, LinkedList<String> filesToSend) {
        MailAccount ma = null;
        Session session = null;
        Transport transport = null;
        MimeMessage message = null;
        Multipart multiPart = null;
        String fileName = null;
        Iterator<String> it = null;
        int countAccount = 0;
        int aux = 0;
        int errorCount = 0;
        String status = "";
        Iterator<MailAccount> accounts = mailAccounts.iterator();
        b:
        while (aux < recipient.size()) {
            try {
                if (!accounts.hasNext()) {
                    accounts = mailAccounts.iterator();
                }
                ma = accounts.next();

                System.out.println("from: " + ma.getMail_smtp_user());
                System.out.println("to: " + recipient.get(aux));
                System.out.println("aux: " + aux);

                Properties props = new Properties();
                props.setProperty("mail.transport.protocol", ma.getMail_transport_protocol());
                props.setProperty("mail.smtp.auth", ma.getMail_smtp_auth());
                props.setProperty("mail.smtp.starttls.enable", ma.getMail_smtp_starttls_enable());
                props.setProperty("mail.smtp.host", ma.getMail_smtp_host());
                props.setProperty("mail.smtp.port", ma.getMail_smtp_port());
                props.setProperty("mail.smtp.user", ma.getMail_smtp_user());
                hp.put("mail.smtp.user", ma.getMail_smtp_user());
                hp.put("mail.smtp.password", ma.getMail_smtp_password());


                session = null;
                session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(hp.get("mail.smtp.user"), hp.get("mail.smtp.password").toString());
                    }
                });
                transport = null;
                transport = session.getTransport(ma.getMail_transport_protocol());
                transport.connect(ma.getMail_smtp_user(), ma.getMail_smtp_password());

                message = null;
                message = new MimeMessage(session);

                message.setFrom(new InternetAddress(ma.getMail_smtp_user()));
                countAccount = 0;
                while (countAccount < mailsPerAccount && (aux + countAccount) < recipient.size()) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.get(aux + countAccount)));
                    countAccount++;
                }

                multiPart = null;
                it = null;
                fileName = null;
                multiPart = new MimeMultipart();

                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(text, "utf-8");
                multiPart.addBodyPart(textPart);

                it = filesToSend.iterator();
                while (it.hasNext()) {
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    fileName = it.next();
                    DataSource source = new FileDataSource(PageParameters.getParameter("mail.attachments.path") + fileName);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(fileName);
                    multiPart.addBodyPart(messageBodyPart);
                }

                message.setSubject(subject);
                message.setContent(multiPart);

                System.out.println("Enviando");
                transport.sendMessage(message, message.getAllRecipients());
                //Transport.send(message);
                transport.close();
                aux = aux + countAccount;
                System.out.println("Correos Enviados");
            } catch (Exception ex) {
                Logger.getLogger(DeliveryMailNotification.class.getName()).log(Level.SEVERE, null, ex);
                errorCount++;
                System.out.println("DeliveryMailNotification Error count: " + errorCount);
                if (errorCount > 20) {
                    status = "error";
                    break b;
                }
            }
        }
    }
}
/*
 * MimeBodyPart textPart = new MimeBodyPart();
 textPart.setText(text, "utf-8");

 MimeBodyPart htmlPart = new MimeBodyPart();
 htmlPart.setContent(html, "text/html; charset=utf-8");

 multiPart.addBodyPart(textPart); <-- first
 multiPart.addBodyPart(htmlPart); <-- second
 message.setContent(multiPart);
 * 
 */
