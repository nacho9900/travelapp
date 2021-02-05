package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Place;

public class PlaceDto
{
    private Long id;
    private String googleId;
    private String name;
    private String address;
    private double latitude;
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
        return new Place(this.id, this.googleId, this.name,this.latitude, this.longitude, this.address);
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
