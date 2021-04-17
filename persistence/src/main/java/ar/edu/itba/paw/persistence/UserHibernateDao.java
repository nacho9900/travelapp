package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserHibernateDao implements UserDao
{
    @PersistenceContext
    private EntityManager em;

    @Override
    public User create( String firstname, String lastname, String email, String password, LocalDate birthday,
                        String nationality, String biography, UUID verificationToken ) {
        final User user = new User( firstname, lastname, email, password, birthday, nationality, biography );
        user.setVerificationToken( verificationToken );
        em.persist( user );
        return user;
    }

    @Override
    public User update( User u ) {
        return em.merge( u );
    }

    @Override
    public Optional<User> findByVerificationToken( UUID verificationToken ) {
        TypedQuery<User> query = em.createQuery( "from User as u where u.verificationToken = :token", User.class );
        query.setParameter( "token", verificationToken );
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findById( long id ) {
        return Optional.ofNullable( em.find( User.class, id ) );
    }

    @Override
    public Optional<User> findByUsername( String email ) {
        final TypedQuery<User> query = em.createQuery( "from User as u where u.email = :email", User.class );
        query.setParameter( "email", email );
        return query.getResultList().stream().findFirst();
    }


}
