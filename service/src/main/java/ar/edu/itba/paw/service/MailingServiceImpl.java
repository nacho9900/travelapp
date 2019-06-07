package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import org.simplejavamail.MailException;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MailingServiceImpl implements MailingService {

    private static final Integer PORT = 587;
    private static final String EMAIL_SERVER = "smtp.gmail.com";
    private static final String EMAIL_NAME = "meet.travel.paw@gmail.com";
    private static final String EMAIL_PASS = "power123321";

    @Async
    @Override
    public void sendRegisterMail(String emailName, String name, String lastname) {

        String htmlFormat = String.format("<h1>%s welcome to Meet and Travel!</h1>" +
                "<h3>Your account has been successfully created</h3>" +
                "<p>Join and create trips to start travelling the world while meeting new and exciting people!", name);

        try {
            Email email = EmailBuilder.startingBlank()
                    .to(name + " " + lastname, emailName)
                    .from("Meet and Travel", "meet.travel.paw@gmail.com")
                    .withSubject("Welcome to Meet and Travel")
                    .withHTMLText(htmlFormat)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer(EMAIL_SERVER, PORT, EMAIL_NAME , EMAIL_PASS)
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria() // turns off email validation
                    .withProperty("mail.smtp.sendpartial", "true")
                    .buildMailer();

            mailer.sendMail(email, true);

        } catch (MailException ignored) {

        }
    }
}