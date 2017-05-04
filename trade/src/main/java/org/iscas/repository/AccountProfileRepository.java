package org.iscas.repository;

import org.iscas.entity.AccountProfile;
import org.springframework.stereotype.Repository;

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

    AccountProfile findByUserID(String userID);

    List<AccountProfile> findByPasswd(String passwd);

    AccountProfile findByUserIDAndPasswd(String userID, String passwd);

    List<AccountProfile> findByAddress(String address);

    List<AccountProfile> findByEmail(String email);

    List<AccountProfile> findByCreditCard(String creditCard);

    List<AccountProfile> findByFullName(String fullName);

}
