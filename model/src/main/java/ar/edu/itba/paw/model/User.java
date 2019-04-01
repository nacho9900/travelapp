package ar.edu.itba.paw.model;

import java.util.Objects;

public class User {

    private final String firstname;
    private final String lastname;
    private final String email;
    private final long id;
    private String password;

    public User(String firstname, String lastname, String email, String password, long id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public long getId() { return id; }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return firstname.equals(user.firstname) &&
                lastname.equals(user.lastname) &&
                email.equals(user.email) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, email, password);
    }
}
