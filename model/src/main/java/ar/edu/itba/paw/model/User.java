package ar.edu.itba.paw.model;
import java.util.Calendar;
import java.util.Objects;

public class User {

    private final long id;
    private final String firstname;
    private final String lastname;
    private final String email;
    private String password;
    private final Calendar birthday;
    private final String nationality;

    public User(long id, String firstname, String lastname, String email, String password, Calendar birthday, String nationality) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.nationality = nationality;
    }


    public long getId() {
        return id;
    }

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

    public void setPassword(String password) {
        this.password = password;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public String getNationality() {
        return nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                firstname.equals(user.firstname) &&
                lastname.equals(user.lastname) &&
                email.equals(user.email) &&
                password.equals(user.password) &&
                birthday.equals(user.birthday) &&
                nationality.equals(user.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, password, birthday, nationality);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

}
