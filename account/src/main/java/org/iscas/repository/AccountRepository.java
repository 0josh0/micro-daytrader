package org.iscas.repository;

import org.iscas.entity.Account;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by andyren on 2016/8/27.
 */
@Repository
public interface AccountRepository extends BaseRepository<Account,Integer>{

//    Account findByAccountID

    @Transactional
    Account findByAccountID(Integer accountId);

    @Transactional
    Account findByProfileUserID(String profileUserID);

    @Transactional
    List<Account> findByCreationDate(Date creationDate);

    @Transactional
    List<Account> findByOpenBalance(BigDecimal openBalance);

    @Transactional
    List<Account> findByLogoutCount(int logoutCount);

    @Transactional
    List<Account> findByBalance(BigDecimal balance);

    @Transactional
    List<Account> findByLastLogin(Date lastLogin);

    @Transactional
    List<Account> findByLoginCount(int loginCount);

}
