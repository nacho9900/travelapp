package ar.edu.itba.paw.model;
import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(sequenceName = "users_id_seq", name = "users_id_seq", allocationSize = 1)
    private long id;

    @Column(length = 100, nullable = false)
    private String firstname;

    @Column(length = 100, nullable = false)
    private String lastname;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar birthday;

    ///////////////todo checkear esto

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "createdBy")
    private List<Trip> createdTrips;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<Trip> trips;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserPicture profilePicture;

    /////////////

    @Column(length = 100, nullable = false)
    private String nationality;

    public User(long id, String firstname, String lastname, String email, String password, Calendar birthday, String nationality) {
        this(firstname, lastname, email, password, birthday, nationality);
        this.id = id;
    }

    public User(String firstname, String lastname, String email, String password, Calendar birthday, String nationality) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.nationality = nationality;
    }

    /* package */ User() {
        // Just for Hibernate
    }

    public List<Trip> getCreatedTrips() {
        return createdTrips;
    }

    public void setCreatedTrips(List<Trip> createdTrips) {
        this.createdTrips = createdTrips;
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public UserPicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(UserPicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                firstname.equals(user.firstname) &&
                lastname.equals(user.lastname) &&
                email.equals(user.email) &&
                password.equals(user.password) &&
                birthday.equals(user.birthday) &&
                nationality.equals(user.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, password, birthday, nationality);
    }

    @Override
    public String toString() {
        return "User: email, " + firstname + " " + lastname;
    }
}
