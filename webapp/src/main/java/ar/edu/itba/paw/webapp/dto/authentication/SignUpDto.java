package ar.edu.itba.paw.webapp.dto.authentication;

import ar.edu.itba.paw.webapp.dto.validators.PasswordConstraint;
import ar.edu.itba.paw.webapp.dto.validators.Past;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SignUpDto
{
    @NotNull
    @NotBlank
    private String firstname;
    @NotNull
    @NotBlank
    private String lastname;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @PasswordConstraint
    private String password;
    @NotNull
    @NotBlank
    private String nationality;
    @NotNull
    @Past
    private LocalDate birthday;

    public SignUpDto() {
        //For JSON
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname( String firstname ) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname( String lastname ) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality( String nationality ) {
        this.nationality = nationality;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday( LocalDate birthday ) {
        this.birthday = birthday;
    }
}
