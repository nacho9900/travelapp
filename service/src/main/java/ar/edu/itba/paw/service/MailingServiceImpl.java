package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import org.simplejavamail.MailException;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;


@Service
@Transactional
public class MailingServiceImpl implements MailingService
{
    private static final Integer PORT = 587;
    private static final String EMAIL_SERVER = "smtp.gmail.com";
    private static final String EMAIL_NAME = "travelapp.inegro@gmail.com";
    private static final String EMAIL_PASS = "D2SpMKqesDtAbLp";

    private static final String NOTIFICATION_EMAIL = "templates/notification-template-email.html";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Async
    @Override
    public void welcomeAndVerificationEmail( String name, String email, String token, String frontendUrl ) {
        String subject = "Welcome to TravelApp: Confirm your email";
        String message = "Thank you for signing up for TravelApp. To complete and confirm your registration for TravelApp" +
                         ", you’ll need to verify your email address. To do so, please click the button below:";
        String btnText = "Confirm your Email";
        String btnHref = frontendUrl + "/verify/" + token;
        sendNotification( name, email, subject, subject, message, btnHref, btnText );
    }

    @Async
    @Override
    public void sendPasswordRecoveryEmail( String name, String email, String token, String frontEndUrl ) {
        String subject = "Password Recovery";
        String message = "We have received your request to reset your password. Please click the link below to complete the reset:";
        String btnText = "Reset My Password";
        String btnHref = frontEndUrl + "/recovery/" + token;
        sendNotification( name, email, subject, subject, message, btnHref, btnText );
    }

    @Async
    @Override
    public void sendNewJoinRequestEmail( String userName, String adminName, String email, long tripId,
                                         String frontendUrl ) {
        String subject = "New Member Request";
        String title = "New Member Request";
        String message = userName + " would like to become a new member of one of your trips!";
        String btnHref = frontendUrl + "/trip/" + tripId;
        String btnText = "Go to the Trip";
        sendNotification( adminName, email, subject, title, message, btnHref, btnText );
    }

    @Async
    @Override
    public void exitTripEmail( String userName, String adminName, String email, long tripId, String tripName,
                               String frontendUrl ) {
        String subject = "Member Exit";
        String title = "Member Exit";
        String message = userName + " has exit your trip \"" + tripName + "\"";
        String btnHref = frontendUrl + "/trip/" + tripId;
        String btnText = "Go to the Trip";
        sendNotification( adminName, email, subject, title, message, btnHref, btnText );
    }

    @Async
    @Override
    public void requestAcceptedEmail( String userName, String email, long tripId, String tripName,
                                      String frontendUrl ) {
        String subject = "Member Request Accepted";
        String title = "Member Request Accepted";
        String message ="you has been accepted on \"" + tripName + "\", go to the trip and start meeting your partners!";
        String btnHref = frontendUrl + "/trip/" + tripId;
        String btnText = "Go to the Trip";
        sendNotification( userName, email, subject, title, message, btnHref, btnText );
    }

    @Async
    @Override
    public void newMemberEmail( String userName, String memberName, String email, long tripId, String tripName, String frontendUrl ) {
        String subject = "There's a new Member!";
        String title = "There's a new Member!";
        String message = userName + " has join your trip \"" + tripName + "\"";
        String btnHref = frontendUrl + "/trip/" + tripId;
        String btnText = "Go to the Trip";
        sendNotification( memberName, email, subject, title, message, btnHref, btnText );
    }

    private void sendNotification( String receiverName, String receiverEmail, String subject, String title,
                                   String message, String btnHref, String btnText ) {
        final Context context = new Context( Locale.ENGLISH );
        context.setVariable( "title", title );
        context.setVariable( "message", message );
        context.setVariable( "btnHref", "http://" + btnHref );
        context.setVariable( "btnText", btnText );
        String html = htmlTemplateEngine.process( NOTIFICATION_EMAIL, context );
        sendMail( receiverName, receiverEmail, html, subject );
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