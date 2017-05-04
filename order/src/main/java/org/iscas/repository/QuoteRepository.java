package org.iscas.repository;

import org.iscas.entity.Quote;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by andyren on 2016/8/25.
 */
@Repository
public interface QuoteRepository extends BaseRepository<Quote, String>{

    Quote findBySymbol(String symbol);

    List<Quote> findByCompanyName(String companyName);

    List<Quote> findByVolume(double volume);

    List<Quote> findByPrice(BigDecimal price);

    List<Quote> findByOpen1(BigDecimal open1);

    List<Quote> findByLow(BigDecimal low);

    List<Quote> findByHigh(BigDecimal high);

    List<Quote> findByChange1(BigDecimal change1);

    List<Quote> findBySymbolAndCompanyName(String symbol, String companyName);

}
