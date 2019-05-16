package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import org.simplejavamail.MailException;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.stereotype.Service;


@Service
public class MailingServiceImpl implements MailingService {


    @Override
    public boolean sendRegisterMail(String emailName, String name, String lastname) {

        try {
            Email email = EmailBuilder.startingBlank()
                    .to(name + " " + lastname, emailName)
                    .from("Meet and Travel", "meet.travel.paw@gmail.com")
                    .withSubject("Welcome to Meet and Travel")
                    /*.withHTMLText("<img src='cid:wink1'><b>We should meet up!</b><img src='cid:wink2'>")*/
                    .withPlainText("testing mail service")
                    //.signWithDomainKey(privateKeyData, "somemail.com", "selector")
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer("smtp.gmail.com", 587, "meet.travel.paw@gmail.com", "power123321")
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria() // turns off email validation
                    .withProperty("mail.smtp.sendpartial", "true")
                    .withDebugLogging(true)
                    .buildMailer();

            mailer.sendMail(email, true);

        } catch (MailException ex) {
            return false;
        }


        return true;
    }
}