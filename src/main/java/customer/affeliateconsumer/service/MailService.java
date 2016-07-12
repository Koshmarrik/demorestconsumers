package customer.affeliateconsumer.service;

import customer.affeliateconsumer.ParameterName;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by roman rasskazov on 31.05.2015.
 */
public class MailService {

    private static Logger log = Logger.getLogger(MailService.class);

    private static Session getSession(){
        Properties props = new Properties();
        props.put("mail.smtp.host", MongoService.getParameter(ParameterName.SMTP_HOST)); //SMTP Host
        props.put("mail.smtp.port", MongoService.getParameter(ParameterName.SMTP_PORT)); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.ssl.trust", MongoService.getParameter(ParameterName.SMTP_HOST)); //trust ssl certificate
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MongoService.getParameter(ParameterName.SMTP_SENT_FROM_MAIL), MongoService.getParameter(ParameterName.SMTP_AUTH_PWD));
            }
        };
        return Session.getInstance(props, auth);
    }

    /**
     * Produce MimeMessage pattern
     */
    private static MimeMessage getMimeMessage(Session session) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setSentDate(new Date());
        return msg;
    }

    public static void sendMail(String subject, String body){
        try
        {
            Session session = getSession();
            MimeMessage msg = getMimeMessage(session);
            msg.setFrom(new InternetAddress(MongoService.getParameter(ParameterName.SMTP_SENT_FROM_MAIL), MongoService.getParameter(ParameterName.SMTP_FROM)));
            msg.setReplyTo(InternetAddress.parse(MongoService.getParameter(ParameterName.SMTP_SENT_FROM_MAIL), false));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(MongoService.getParameter(ParameterName.SMTP_SENT_TO_MAIL), false));

            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");

            Transport.send(msg);
            log.debug("Mail Service: email sent");
        }
        catch (Exception e) {
            log.error("MailService: can't send mail");
            log.error(e.getMessage(), e);
        }
    }

}
