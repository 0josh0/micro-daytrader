package org.iscas.repository;

import org.iscas.entity.Orders;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by andyren on 2016/8/27.
 * BaseRepository equal to CrudRepository
 */
@Repository
public interface OrdersRepository extends BaseRepository<Orders, Integer>{

    Orders findByOrderID(Integer orderID);

    List<Orders> findByOrderType(String orderType);

    List<Orders> findByOrderStatus(String orderStatus);

    List<Orders> findByOpenDate(Date openDate);

    List<Orders> findByCompletionDate(Date completionDate);

    List<Orders> findByQuantity(Double quantity);

    List<Orders> findByPrice(BigDecimal price);

    List<Orders> findByOrderFee(BigDecimal orderFee);

    List<Orders> findByQuoteSymbol(String quoteSymbol);

    List<Orders> findByAccountID(Integer accountID);

    List<Orders> findByHoldingID(Integer holdingID);

    List<Orders> findByAccountIDAndOrderStatus(String accountID, String orderStatus);

    /* update the order status to completed and closed
       @Query("update orders o set o.orderStatus = 'completed' where o.orderStatus = 'closed' and o.account.profile.userID  = :userID")
       List<Orders> completeClosedOrders(String userID);
    */

}
