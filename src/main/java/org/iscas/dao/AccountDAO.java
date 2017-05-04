package org.iscas.dao;

import org.iscas.entity.Account;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by andyren on 2016/6/27.
 */
@Repository
@Transactional
public class  AccountDAO{

     @PersistenceContext
     private EntityManager entityManager;

     public void create(Account account){
          entityManager.persist(account);
          return;
     }

     public void delete(Account account){
          if(entityManager.contains(account)){
               entityManager.remove(account);
          }else{
               entityManager.remove(entityManager.merge(account));
          }
          return;
     }

     public List<Account> getAll(){
          return entityManager.createQuery("from Account").getResultList();
     }

     public void update(Account account){
          entityManager.merge(account);
          return;
     }

}
