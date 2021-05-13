package ar.edu.itba.paw.webapp.dto.users;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.UsersController;
import ar.edu.itba.paw.webapp.dto.validators.Past;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;

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
    @NotNull
    @Past
    private LocalDate birthday;
    private URI userUri;
    private URI userPictureUri;
    private URI changePasswordUri;

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

    public LocalDate getBirthday() {
        return birthday;
    }

    public URI getUserUri() {
        return userUri;
    }

    public URI getUserPictureUri() {
        return userPictureUri;
    }

    public URI getChangePasswordUri() {
        return changePasswordUri;
    }

    public UserDto() {
        //For Jackson
    }

    public User toUser() {
        LocalDate birthday = this.birthday;

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
        userDto.birthday = user.getBirthday();

        userDto.userUri = uriInfo.getBaseUriBuilder()
                                 .path( UsersController.class )
                                 .path( UsersController.class, "get" )
                                 .build( user.getId() );
        userDto.userPictureUri = uriInfo.getBaseUriBuilder()
                                        .path( UsersController.class )
                                        .path( UsersController.class, "getPicture" )
                                        .build( user.getId() );
        userDto.changePasswordUri = uriInfo.getBaseUriBuilder()
                                           .path( UsersController.class )
                                           .path( UsersController.class, "changePassword" )
                                           .build( user.getId() );

        return userDto;
    }
}
