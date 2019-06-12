package ar.edu.itba.paw.interfaces;

public interface MailingService {

    public void sendRegisterMail(String email, String name, String lastname);
    public void sendJoinTripMail(String email, String adminName, String tripName, String firstname, String lastname);
}
