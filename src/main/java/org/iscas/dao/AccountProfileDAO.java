package org.iscas.dao;

import org.iscas.entity.Account;
import org.iscas.entity.AccountProfile;
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
public class AccountProfileDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(AccountProfile accountProfile){
        entityManager.persist(accountProfile);
        return;
    }

    public void delete(AccountProfile accountProfile){
        if(entityManager.contains(accountProfile)){
            entityManager.remove(accountProfile);
        }else{
            entityManager.remove(entityManager.merge(accountProfile));
        }
        return;
    }

    public List<Account> getAll(){
        return entityManager.createQuery("from AccountProfile").getResultList();
    }

    public void update(AccountProfile accountProfile){
        entityManager.merge(accountProfile);
        return;
    }
}
