package org.iscas.repository;

import org.iscas.entity.AccountProfile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by andyren on 2016/8/27.
 * 注意在Query中使用的是Hibernate，只要在Entity中映射好了，这里的Query中都是使用EntityBean中的属性；
 * 与Hibernate的Query是相同的。
 * import org.springframework.data.jpa.repository.JpaRepository;
 * public interface AccountProfileRepository extends JpaRepository<AccountProfile, String>
 */
@Repository
public interface AccountProfileRepository extends BaseRepository<AccountProfile, String> {

    @Transactional
    AccountProfile findByUserID(String userID);

    @Transactional
    List<AccountProfile> findByPasswd(String passwd);

    @Transactional
    AccountProfile findByUserIDAndPasswd(String userID, String passwd);

    @Transactional
    List<AccountProfile> findByAddress(String address);

    @Transactional
    List<AccountProfile> findByEmail(String email);

    @Transactional
    List<AccountProfile> findByCreditCard(String creditCard);

    @Transactional
    List<AccountProfile> findByFullName(String fullName);

}
