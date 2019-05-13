package ar.edu.itba.paw.model;

public class TripUser {

    private final long id;
    private final long tripId;
    private final long userId;
    private final UserRole userRole;

    public TripUser(long id, long tripId, long userId, UserRole userRole) {
        this.id = id;
        this.tripId = tripId;
        this.userId = userId;
        this.userRole = userRole;
    }

    public long getId() {
        return id;
    }

    public long getTripId() {
        return tripId;
    }

    public long getUserId() {
        return userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }
}
