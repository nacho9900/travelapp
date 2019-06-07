package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Optional;

@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String firstname, String lastname, String email, String password, Calendar birthday, String nationality) {
       final User user = new User(firstname, lastname, email, password, birthday, nationality);
       em.persist(user);
       return user;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.of(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }


    //TODO
    @Override
    public Optional<UserRole> getUserRole(long userId, long tripId) {
        return Optional.empty();
    }
}
