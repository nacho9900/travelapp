package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import org.simplejavamail.MailException;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;


@Service
@Transactional
public class MailingServiceImpl implements MailingService {

    private static final Integer PORT = 587;
    private static final String EMAIL_SERVER = "smtp.gmail.com";
    private static final String EMAIL_NAME = "meet.travel.paw@gmail.com";
    private static final String EMAIL_PASS = "power123321";

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    private static final String REGISTER_TEMPLATE = "templates/registerMail.html";

    @Async
    @Override
    public void sendRegisterMail(String emailName, String name, String lastname) {
        try {
            final Context ctx = new Context();
            ctx.setVariable("email", emailName);
            ctx.setVariable("name", name);
            ctx.setVariable("lastname", lastname);
            String html = htmlTemplateEngine.process(REGISTER_TEMPLATE, ctx);

            Email email = EmailBuilder.startingBlank()
                    .to(name + " " + lastname, emailName)
                    .from("Meet and Travel", "meet.travel.paw@gmail.com")
                    .withSubject("Welcome to Meet and Travel")
                    .withHTMLText(html)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer(EMAIL_SERVER, PORT, EMAIL_NAME , EMAIL_PASS)
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria()
                    .withProperty("mail.smtp.sendpartial", "true")
                    .buildMailer();

            mailer.sendMail(email, true);

        } catch (MailException ignored) {

        }
    }
}