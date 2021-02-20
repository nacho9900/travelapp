package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "user_pictures")
public class UserPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_picture_id_seq")
    @SequenceGenerator(sequenceName = "user_picture_id_seq", name = "user_picture_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "image")
    private byte[] picture;

    @Column(name = "name")
    private String name;

    //////////////////////

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    ///////////////////////

    public UserPicture(User user, String name, byte[] picture) {
        this.name = name;
        this.picture = picture;
        this.user = user;
    }

    protected UserPicture() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public void setPicture( byte[] picture ) {
        this.picture = picture;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setUser( User user ) {
        this.user = user;
    }
}
