package ar.edu.itba.paw.model;

public class TripUsers {

    private final long id;
    private final long tripId;
    private final long userId;
    private final long userRoleId;

    public TripUsers(long id, long tripId, long userId, long userRoleId) {
        this.id = id;
        this.tripId = tripId;
        this.userId = userId;
        this.userRoleId = userRoleId;
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

    public long getUserRoleId() {
        return userRoleId;
    }
}
