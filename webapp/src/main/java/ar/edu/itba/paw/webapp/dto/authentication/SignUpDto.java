package ar.edu.itba.paw.webapp.dto.authentication;

import ar.edu.itba.paw.webapp.dto.validators.PasswordConstraint;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
    private Date birthday;

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

    public Date getBirthday() {
        return birthday;
    }

    public LocalDate getBirthdayLocalDate() {
        return getBirthday().toInstant()
                            .atZone( ZoneId.systemDefault() )
                            .toLocalDate();
    }

    public void setBirthday( Date birthday ) {
        this.birthday = birthday;
    }
}
