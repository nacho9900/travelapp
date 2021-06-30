package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenDao;
import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PasswordRecoveryTokenHibernateDao implements PasswordRecoveryTokenDao
{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<PasswordRecoveryToken> findByUserId( long userId ) {
        final TypedQuery<PasswordRecoveryToken> query = em.createQuery(
                "select t from User as u inner join u.passwordRecoveryToken as t where u.id = :userId",
                PasswordRecoveryToken.class );
        query.setParameter( "userId", userId );
        return query.getResultList().stream().findFirst();
    }

    @Override
    public PasswordRecoveryToken update( PasswordRecoveryToken token ) {
        return em.merge( token );
    }

    @Override
    public PasswordRecoveryToken create( UUID token, LocalDateTime expiresIn, User user ) {
        PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken( token, expiresIn, user );
        em.persist( passwordRecoveryToken );
        return passwordRecoveryToken;
    }

    @Override
    public Optional<PasswordRecoveryToken> findByToken( UUID token ) {
        final TypedQuery<PasswordRecoveryToken> query = em.createQuery(
                "select t from PasswordRecoveryToken as t inner join fetch t.user as u where t.token = :token",
                PasswordRecoveryToken.class );
        query.setParameter( "token", token );
        return query.getResultList().stream().findFirst();
    }
}
