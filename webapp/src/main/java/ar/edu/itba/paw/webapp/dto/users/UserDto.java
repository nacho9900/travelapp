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
    private Date birthday;
    private String biography;
    private String nationality;
    private URI userPicture;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public URI getUserPicture() {
        return userPicture;
    }

    public void setUserPicture( URI userPicture ) {
        this.userPicture = userPicture;
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

    public static UserDto fromUser( User user, UriInfo uriInfo ) {
        UserDto userDto = new UserDto();

        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.firstname = user.getFirstname();
        userDto.lastname = user.getLastname();
        userDto.nationality = user.getNationality();
        userDto.biography = user.getBiography();

        userDto.userPicture = uriInfo.getAbsolutePathBuilder()
                                     .path( String.valueOf( user.getId() ) )
                                     .path( "picture" )
                                     .build();

        return userDto;
    }
}
