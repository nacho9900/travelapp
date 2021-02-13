package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Place;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class PlaceDto
{
    private Long id;
    @NotNull
    @NotBlank
    private String googleId;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String address;
    @Max( 90 )
    @Min( -90 )
    private double latitude;
    @Max( 180 )
    @Min( -180 )
    private double longitude;

    public PlaceDto() {
        //ForJackson
    }

    public Long getId() {
        return id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Place toPlace() {
        if(this.id != null) {
            return new Place( this.id, this.googleId, this.name, this.latitude, this.longitude, this.address );
        } else {
            return new Place( this.googleId, this.name, this.latitude, this.longitude, this.address );
        }
    }

    public static PlaceDto fromPlace( Place place ) {
        PlaceDto placeDto = new PlaceDto();

        placeDto.id = place.getId();
        placeDto.googleId = place.getGoogleId();
        placeDto.address = place.getAddress();
        placeDto.name = place.getName();
        placeDto.latitude = place.getLatitude();
        placeDto.longitude = place.getLongitude();

        return placeDto;
    }
}
