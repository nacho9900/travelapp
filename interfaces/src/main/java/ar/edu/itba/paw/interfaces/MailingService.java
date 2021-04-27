package ar.edu.itba.paw.interfaces;

import java.util.Locale;

public interface MailingService
{
    void welcomeAndVerificationEmail( String name, String email, String token, String frontendUrl, Locale locale );

    void sendPasswordRecoveryEmail( String name, String email, String token, String redirectUrl, Locale locale );

    void sendNewJoinRequestEmail( String userName, String adminName, String email, long tripId, String frontendUrl,
                                  Locale locale );

    void exitTripEmail( String userName, String adminName, String email, long tripId, String tripName, String frontendUrl, Locale locale );

    void requestAcceptedEmail( String userName, String email, long tripId, String tripName, String frontendUrl,
                               Locale locale );

    void newMemberEmail( String userName, String memberName, String email, long tripId, String tripName, String frontendUrl, Locale locale );
}
