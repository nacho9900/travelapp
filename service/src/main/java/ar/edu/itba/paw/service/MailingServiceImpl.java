package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import org.simplejavamail.MailException;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;


@Service
@Transactional //TODO: Make this package scoped
public class MailingServiceImpl implements MailingService
{
    private static final Integer PORT = 587;
    private static final String EMAIL_SERVER = "smtp.gmail.com";
    private static final String EMAIL_NAME = "travelapp.inegro@gmail.com";
    private static final String EMAIL_PASS = "D2SpMKqesDtAbLp";

    private static final String WELCOME_EMAIL = "welcome";
    private static final String PASSWORD_RECOVERY_EMAIL = "password-recovery";
    private static final String JOIN_REQUEST_NEW_EMAIL = "join-request-new";
    private static final String MEMBER_EXIT_EMAIL = "member-exit";
    private static final String JOIN_REQUEST_ACCEPTED = "join-request-accepted";
    private static final String MEMBER_NEW_EMAIL = "member-new";

    //TODO: CHANGE IN PROD
    private static final String FRONTEND_URL = "localhost:8080";
    //    private static final String FRONTEND_URL = "http://pawserver.it.itba.edu.ar/paw-2019a-4";

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    private MessageSource messageSource;

    @Async
    @Override
    public void welcomeAndVerificationEmail( String name, String email, String token, Locale locale ) {
        String btnHref = FRONTEND_URL + "/verify/" + token;
        final Context context = new Context( locale );
        context.setVariable( "btnHref", "http://" + btnHref );
        context.setVariable( "name", name );
        String html = htmlTemplateEngine.process( WELCOME_EMAIL, context );
        sendMail( name, email, html, messageSource.getMessage( "welcome.subject", null, locale ) );
    }

    @Async
    @Override
    public void sendPasswordRecoveryEmail( String name, String email, String token, Locale locale ) {
        String btnHref = FRONTEND_URL + "/recovery/" + token;
        final Context context = new Context( locale );
        context.setVariable( "btnHref", "http://" + btnHref );
        String html = htmlTemplateEngine.process( PASSWORD_RECOVERY_EMAIL, context );
        sendMail( name, email, html, messageSource.getMessage( "password_recovery.subject", null, locale ) );
    }

    @Async
    @Override
    public void sendNewJoinRequestEmail( String userName, String adminName, String email, long tripId, Locale locale ) {

        String btnHref = FRONTEND_URL + "/trip/" + tripId;
        final Context context = new Context( locale );
        context.setVariable( "btnHref", "http://" + btnHref );
        context.setVariable( "name", userName );
        String html = htmlTemplateEngine.process( JOIN_REQUEST_NEW_EMAIL, context );
        sendMail( adminName, email, html, messageSource.getMessage( "join_request_new.subject", null, locale ) );
    }

    @Async
    @Override
    public void exitTripEmail( String userName, String adminName, String email, long tripId, String tripName,
                               Locale locale ) {
        String btnHref = FRONTEND_URL + "/trip/" + tripId;
        final Context context = new Context( locale );
        context.setVariable( "btnHref", "http://" + btnHref );
        context.setVariable( "name", userName );
        context.setVariable( "trip", tripName );
        String html = htmlTemplateEngine.process( MEMBER_EXIT_EMAIL, context );
        sendMail( adminName, email, html, messageSource.getMessage( "exit_trip.subject", null, locale ) );
    }

    @Async
    @Override
    public void requestAcceptedEmail( String userName, String email, long tripId, String tripName, Locale locale ) {
        String btnHref = FRONTEND_URL + "/trip/" + tripId;
        final Context context = new Context( locale );
        context.setVariable( "btnHref", "http://" + btnHref );
        context.setVariable( "trip", tripName );
        String html = htmlTemplateEngine.process( JOIN_REQUEST_ACCEPTED, context );
        sendMail( userName, email, html, messageSource.getMessage( "join_request_accepted.subject", null, locale ) );
    }

    @Async
    @Override
    public void newMemberEmail( String userName, String memberName, String email, long tripId, String tripName,
                                Locale locale ) {
        String btnHref = FRONTEND_URL + "/trip/" + tripId;
        final Context context = new Context( locale );
        context.setVariable( "btnHref", "http://" + btnHref );
        context.setVariable( "name", memberName );
        context.setVariable( "trip", tripName );
        String html = htmlTemplateEngine.process( MEMBER_NEW_EMAIL, context );
        sendMail( userName, email, html, messageSource.getMessage( "member_new.subject", null, locale ) );
    }

    private void sendMail( String receiverName, String receiverEmail, String html, String subject ) {
        try {
            Email email = EmailBuilder.startingBlank()
                                      .to( receiverName, receiverEmail )
                                      .from( "TravelApp", "meet.travel.paw@gmail.com" )
                                      .withSubject( subject )
                                      .withHTMLText( html )
                                      .buildEmail();

            Mailer mailer = MailerBuilder.withSMTPServer( EMAIL_SERVER, PORT, EMAIL_NAME, EMAIL_PASS )
                                         .withTransportStrategy( TransportStrategy.SMTP_TLS )
                                         .withSessionTimeout( 10 * 1000 )
                                         .clearEmailAddressCriteria()
                                         .withProperty( "mail.smtp.sendpartial", "true" )
                                         .buildMailer();

            mailer.sendMail( email, true );
        }
        catch ( MailException ignored ) {

        }
    }
}