package ar.edu.itba.paw.webapp.dto.users;

import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UserDto
{
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String biography;
    private String nationality;
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
        LocalDate birthday = this.birthday.toInstant()
                                          .atZone( ZoneId.systemDefault() )
                                          .toLocalDate();

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
