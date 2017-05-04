package org.iscas.repository;

import org.iscas.entity.Holding;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by andyren on 2016/8/27.
 */
@Repository
public interface HoldingRepository extends BaseRepository<Holding, Integer>{

    Holding findByHoldingID(Integer holdingID);

    List<Holding> findByQuantity(double quantity);

    List<Holding> findByPurchaseDate(Date purchaseDate);

    List<Holding> findByPurchasePrice(BigDecimal purchasePrice);

    List<Holding> findByQuoteSymbol(String quoteSymbol);

    List<Holding> findByAccountID(Integer accountID);
}
