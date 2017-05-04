package org.iscas.repository;

import org.iscas.entity.Orders;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by andyren on 2016/8/27.
 * BaseRepository equal to CrudRepository
 */
@Repository
public interface OrdersRepository extends BaseRepository<Orders, Integer>{

    @Transactional
    Orders findByOrderID(Integer orderID);

    @Transactional
    List<Orders> findByOrderType(String orderType);

    @Transactional
    List<Orders> findByOrderStatus(String orderStatus);

    @Transactional
    List<Orders> findByOpenDate(Date openDate);

    @Transactional
    List<Orders> findByCompletionDate(Date completionDate);

    @Transactional
    List<Orders> findByQuantity(Double quantity);

    @Transactional
    List<Orders> findByPrice(BigDecimal price);

    @Transactional
    List<Orders> findByOrderFee(BigDecimal orderFee);

    @Transactional
    List<Orders> findByQuoteSymbol(String quoteSymbol);

    @Transactional
    List<Orders> findByAccountID(Integer accountID);

    @Transactional
    List<Orders> findByHoldingID(Integer holdingID);

    @Transactional
    List<Orders> findByAccountIDAndOrderStatus(String accountID, String orderStatus);


    /* update the order status to completed and closed
       @Query("update orders o set o.orderStatus = 'completed' where o.orderStatus = 'closed' and o.account.profile.userID  = :userID")
       List<Orders> completeClosedOrders(String userID);
    */

}
