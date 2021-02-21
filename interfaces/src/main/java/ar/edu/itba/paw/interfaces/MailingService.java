package ar.edu.itba.paw.interfaces;

import java.util.Locale;

public interface MailingService
{
    void welcomeAndVerificationEmail( String name, String email, String token, String frontendUrl );

    void sendPasswordRecoveryEmail( String name, String email, String token, String redirectUrl );

    void sendNewJoinRequestEmail( String userName, String adminName, String email, long tripId, String frontendUrl );

    void exitTripEmail( String userName, String adminName, String email, long tripId, String tripName,
                        String frontendUrl );

    void requestAcceptedEmail( String userName, String email, long tripId, String tripName, String frontendUrl );

    void newMemberEmail( String userName, String memberName, String email, long tripId, String tripName,
                         String frontendUrl );
}
