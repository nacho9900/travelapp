package ar.edu.itba.paw.webapp.dto.users;

import ar.edu.itba.paw.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UserDto
{
    @NotNull
    private Long id;
    private String email;
    @NotBlank
    @Size( max = 100 )
    private String firstname;
    @NotBlank
    @Size( max = 100 )
    private String lastname;
    @Size( max = 500 )
    private String biography;
    @NotBlank
    @Size( max = 100 )
    private String nationality;
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd" )
    @NotNull
    @Past
    private Date birthday;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBiography() {
        return biography;
    }

    public String getNationality() {
        return nationality;
    }

    public Date getBirthday() {
        return birthday;
    }

    public UserDto() {
        //For Jackson
    }

    public User toUser() {
        LocalDate birthday = this.birthday.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();

        return new User( this.id, this.firstname, this.lastname, this.email, null, birthday, this.nationality,
                         this.biography );
    }

    public static UserDto fromUser( User user ) {
        UserDto userDto = new UserDto();

        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.firstname = user.getFirstname();
        userDto.lastname = user.getLastname();
        userDto.nationality = user.getNationality();
        userDto.biography = user.getBiography();
        userDto.birthday = java.sql.Date.valueOf( user.getBirthday() );

        return userDto;
    }
}
