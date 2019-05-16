package ar.edu.itba.paw.interfaces;

public interface MailingService {

    public boolean sendRegisterMail(String email, String name, String lastname);
}
