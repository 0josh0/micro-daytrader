package org.iscas.repository;

import org.iscas.entity.Holding;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by andyren on 2016/8/27.
 */
@Repository
public interface HoldingRepository extends BaseRepository<Holding, Integer>{

    @Transactional
    Holding findByHoldingID(Integer holdingID);

    @Transactional
    List<Holding> findByQuantity(double quantity);

    @Transactional
    List<Holding> findByPurchaseDate(Date purchaseDate);

    @Transactional
    List<Holding> findByPurchasePrice(BigDecimal purchasePrice);

    @Transactional
    List<Holding> findByQuoteSymbol(String quoteSymbol);

    @Transactional
    List<Holding> findByAccountID(Integer accountID);
}
