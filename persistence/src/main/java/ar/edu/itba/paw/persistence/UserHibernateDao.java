package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String firstname, String lastname, String email, String password, LocalDate birthday, String nationality, String biography) {
       final User user = new User(firstname, lastname, email, password, birthday, nationality, biography);
       em.persist(user);
       return user;
    }

    @Override
    public boolean update(User u) {
        return em.merge(u) != null;
    }

    @Override
    public List<User> listUsers( int page, int pageSize )
    {
        final TypedQuery<User> query = em.createQuery( "from User as u", User.class );
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }


}
