package ar.edu.itba.paw.model;

public class UserPicture {

    private final long id;
    private byte[] picture;
    private final long userId;

    public UserPicture(long id, byte[] picture, long userId) {
        this.id = id;
        this.picture = picture;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public long getUserId() {
        return userId;
    }
}
