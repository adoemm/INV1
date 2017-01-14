/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.LinkedList;
import javax.mail.NoSuchProviderException;
import jspread.core.util.DeliveryMailNotification;
import jspread.core.util.MailAccount;
import jspread.core.util.PageParameters;
import jspread.core.util.SendMailUtil;

/**
 *
 * @author VIOX
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchProviderException {
        /*
         PageParameters.getSingleInstance();
         PageParameters.addParameter("mail.transport.protocol", "smtp");
         PageParameters.addParameter("mail.smtp.starttls.enable", "true");
         PageParameters.addParameter("mail.smtp.host", "smtp.gmail.com");
         PageParameters.addParameter("mail.smtp.port", "587");
         PageParameters.addParameter("mail.smtp.auth", "true");
         PageParameters.addParameter("mail.smtp.user", "cecytem.v.urrutia@gmail.com");
         PageParameters.addParameter("mail.smtp.password", "nexusone128");
         PageParameters.addParameter("mail.attachments.path", "c:/");
         PageParameters.addParameter("mail.maximumAttachmentSize", "25");//this value is in Mb

         SendMailUtil.initialize();
         LinkedList recipient = new LinkedList();
         recipient.add("unimatrix845@hotmail.com");

         LinkedList filesToSend = new LinkedList();
         filesToSend.add("dna.jpg");
         filesToSend.add("variables hola mundo.xlsx");
         filesToSend.add("cleanup.rar");
         filesToSend.add("dna.png");

         //SendMailUtil.sendMail(recipient, "Hola Multipart mail", "Enviando correo multipart con 3 archivos", "dna.jpg");
         SendMailUtil.sendMail(recipient, "Hola Multipart mail", "Enviando correo multipart con 4 archivos, el password para el rar es lalala", filesToSend);
         * 
         * 
         * 
         */

        PageParameters.getSingleInstance();
        PageParameters.addParameter("mail.attachments.path", "c:/correo/");
        PageParameters.addParameter("mail.maximumAttachmentSize", "25");//this value is in Mb

        LinkedList<MailAccount> mailAccounts = new LinkedList();

        MailAccount ma1 = new MailAccount();
        ma1.setMail_maximumAttachmentSize(PageParameters.getParameter("mail.maximumAttachmentSize"));
        ma1.setMail_smtp_auth("true");
        ma1.setMail_smtp_host("smtp.gmail.com");
        ma1.setMail_smtp_port("587");
        ma1.setMail_smtp_starttls_enable("true");
        ma1.setMail_smtp_user("cecytem.v.urrutia@gmail.com");
        ma1.setMail_smtp_password("nexusone128");
        ma1.setMail_transport_protocol("smtp");
        mailAccounts.add(ma1);

        MailAccount ma2 = new MailAccount();
        ma2.setMail_maximumAttachmentSize(PageParameters.getParameter("mail.maximumAttachmentSize"));
        ma2.setMail_smtp_auth("true");
        ma2.setMail_smtp_host("smtp.live.com");
        ma2.setMail_smtp_port("587");
        ma2.setMail_smtp_starttls_enable("true");
        ma2.setMail_smtp_user("unimatrix845@hotmail.com");
        ma2.setMail_smtp_password("gogoyubari64++");
        ma2.setMail_transport_protocol("smtp");
        mailAccounts.add(ma2);

        MailAccount ma3 = new MailAccount();
        ma3.setMail_maximumAttachmentSize(PageParameters.getParameter("mail.maximumAttachmentSize"));
        ma3.setMail_smtp_auth("true");
        ma3.setMail_smtp_host("smtp.gmail.com");
        ma3.setMail_smtp_port("587");
        ma3.setMail_smtp_starttls_enable("true");
        ma3.setMail_smtp_user("unimatrix845@gmail.com");
        ma3.setMail_smtp_password("gogoyubari64++");
        ma3.setMail_transport_protocol("smtp");
        mailAccounts.add(ma3);


        LinkedList recipient = new LinkedList();
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("yeah_86@live.com.mx");
        recipient.add("alfawaveradio@live.com");
        recipient.add("corpmalo@hotmail.com");
        recipient.add("j_cg_lez@hotmail.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        recipient.add("alfawaveradio@live.com");
        


        LinkedList filesToSend = new LinkedList();
        filesToSend.add("dna.jpg");
        filesToSend.add("cylon.jpg");

        DeliveryMailNotification dmn = new DeliveryMailNotification();

        dmn.sendMailNotification(mailAccounts, 5, recipient, "Probando MailBomber", "Estimados amigos, aqui les dejo unos cuantos correo para probar mi mail bomber. Salu2.", filesToSend);
    }
}
