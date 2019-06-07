package ar.edu.itba.paw.model;


import javax.persistence.*;

@Entity
@Table(name = "trip_users")
public class TripUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_users_id_seq")
    @SequenceGenerator(sequenceName = "trip_users_id_seq", name = "trip_users_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public TripUser(long id, Trip trip, User user, UserRole userRole) {
        this(trip, user, userRole);
        this.id = id;
    }

    public TripUser(Trip trip, User user, UserRole userRole) {

        this.trip = trip;
        this.user = user;
        this.userRole = userRole;
    }

    /* package */ TripUser() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
