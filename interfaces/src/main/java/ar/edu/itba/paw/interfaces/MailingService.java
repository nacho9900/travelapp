package ar.edu.itba.paw.interfaces;

import java.util.Locale;

public interface MailingService
{
    void welcomeAndVerificationEmail( String name, String email, String token, Locale locale );

    void sendPasswordRecoveryEmail( String name, String email, String token, Locale locale );

    void sendNewJoinRequestEmail( String userName, String adminName, String email, long tripId, Locale locale );

    void exitTripEmail( String userName, String adminName, String email, long tripId, String tripName, Locale locale );

    void requestAcceptedEmail( String userName, String email, long tripId, String tripName, Locale locale );

    void newMemberEmail( String userName, String memberName, String email, long tripId, String tripName, Locale locale );
}
