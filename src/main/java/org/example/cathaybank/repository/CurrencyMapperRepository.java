package org.example.cathaybank.repository;

import org.example.cathaybank.entity.CurrencyMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

/**
 * @Author: Lichi
 * @Date:2024/02/03/上午 03:01
 * @Description: 貨幣對應表Repository
 */

public interface CurrencyMapperRepository extends JpaRepository<CurrencyMapper, Long>, JpaSpecificationExecutor<CurrencyMapper> {

    Optional<CurrencyMapper> findByCurrencyCode(String currencyCode);
}
