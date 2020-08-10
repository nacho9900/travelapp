package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto
{
    private Long id;
    private String email;
    private URI userPicture;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public UserDto()
    {

    }

    public static UserDto fromUser( User user, UriInfo uriInfo )
    {
        UserDto userDto = new UserDto();

        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.userPicture = uriInfo
                .getAbsolutePathBuilder()
                .path( String.valueOf( user.getId() ) )
                .path( "picture" )
                .build(  );

        return userDto;
    }

    public URI getUserPicture()
    {
        return userPicture;
    }

    public void setUserPicture( URI userPicture )
    {
        this.userPicture = userPicture;
    }
}
