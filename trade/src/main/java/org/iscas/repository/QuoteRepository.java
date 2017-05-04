package org.iscas.repository;

import org.iscas.entity.Quote;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

//import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by andyren on 2016/8/25.
 */
@Repository
public interface QuoteRepository extends BaseRepository<Quote, String>{

    @Transactional
    Quote findBySymbol(String symbol);

    @Transactional
    List<Quote> findByCompanyName(String companyName);

    @Transactional
    List<Quote> findByVolume(double volume);

    @Transactional
    List<Quote> findByPrice(BigDecimal price);

    @Transactional
    List<Quote> findByOpen1(BigDecimal open1);

    @Transactional
    List<Quote> findByLow(BigDecimal low);

    @Transactional
    List<Quote> findByHigh(BigDecimal high);

    @Transactional
    List<Quote> findByChange1(BigDecimal change1);

    @Transactional
    List<Quote> findBySymbolAndCompanyName(String symbol, String companyName);

}
