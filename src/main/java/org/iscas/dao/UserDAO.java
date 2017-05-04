package org.iscas.dao;

import org.iscas.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by andyren on 2016/6/28.
 */
@Repository
@Transactional
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * save the user to database
     * @param user
     */
    public void create(User user){
        entityManager.persist(user);
        return;
    }

    public void delete(User user){
        if(entityManager.contains(user)){
            entityManager.remove(user);
        }else{
            entityManager.remove(entityManager.merge(user));
        }
        return;
    }

    public List<User> getAll(){
        return entityManager.createQuery("from User").getResultList();
    }

    public User getByEmail(String email){
        return (User)entityManager.createQuery(
                "from User where email = :email")
                .setParameter("email", email)
                .getSingleResult();

    }

    public User getById(long id){
        return entityManager.find(User.class, id);
    }

    public void update(User user){
        entityManager.merge(user);
        return;
    }
}
