package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table( name = "password_recovery_token" )
public class PasswordRecoveryToken
{
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
                     generator = "password_recovery_token_id_seq" )
    @SequenceGenerator( sequenceName = "password_recovery_token_id_seq",
                        name = "password_recovery_token_id_seq",
                        allocationSize = 1 )
    private long id;
    @Column( nullable = false )
    private UUID token;
    @Column( nullable = false )
    private LocalDateTime expiresIn;
    @Column( nullable = false )
    private boolean used;

    /////////////////

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    /////////////////


    public long getId() {
        return id;
    }

    public UUID getToken() {
        return token;
    }

    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    public User getUser() {
        return user;
    }

    public void setToken( UUID token ) {
        this.token = token;
    }

    public void setExpiresIn( LocalDateTime expiresIn ) {
        this.expiresIn = expiresIn;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed( boolean used ) {
        this.used = used;
    }

    public PasswordRecoveryToken() {
        //For Hibernate
    }

    public PasswordRecoveryToken( UUID token, LocalDateTime expiresIn, User user ) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.user = user;
    }
}
