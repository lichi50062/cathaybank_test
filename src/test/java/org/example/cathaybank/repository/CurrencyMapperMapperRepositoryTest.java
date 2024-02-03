package org.example.cathaybank.repository;

import org.example.cathaybank.entity.CurrencyMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author: Lichi
 * @Date:2024/02/03/上午 04:44
 * @Description:
 */


@DataJpaTest
public class CurrencyMapperMapperRepositoryTest {

        @Autowired
        private CurrencyMapperRepository currencyMapperRepository;

        @Test
        public void testCreateCurrencyMapper() {
                CurrencyMapper currencyMapper = new CurrencyMapper();
                currencyMapper.setCurrencyCode("USD");
                currencyMapper.setCurrencyName("US Dollar");

                currencyMapperRepository.save(currencyMapper);

                assertThat(currencyMapper.getId()).isNotNull();
        }

        @Test
        public void testReadCurrencyMapper() {
                CurrencyMapper currencyMapper = new CurrencyMapper();
                currencyMapper.setCurrencyCode("EUR");
                currencyMapper.setCurrencyName("Euro");

                currencyMapperRepository.save(currencyMapper);

                CurrencyMapper foundCurrencyMapper = currencyMapperRepository.findById(currencyMapper.getId()).orElse(null);

                assertThat(foundCurrencyMapper).isNotNull();
                assertThat(foundCurrencyMapper.getCurrencyCode()).isEqualTo("EUR");
        }

        @Test
        public void testUpdateCurrencyMapper() {
                CurrencyMapper currencyMapper = new CurrencyMapper();
                currencyMapper.setCurrencyCode("JPY");
                currencyMapper.setCurrencyName("Japanese Yen");

                currencyMapperRepository.save(currencyMapper);

                currencyMapper.setCurrencyName("New Japanese Yen");
                currencyMapperRepository.save(currencyMapper);

                CurrencyMapper updatedCurrencyMapper = currencyMapperRepository.findById(currencyMapper.getId()).orElse(null);

                assertThat(updatedCurrencyMapper).isNotNull();
                assertThat(updatedCurrencyMapper.getCurrencyName()).isEqualTo("New Japanese Yen");
        }

        @Test
        public void testDeleteCurrencyMapper() {
                CurrencyMapper currencyMapper = new CurrencyMapper();
                currencyMapper.setCurrencyCode("GBP");
                currencyMapper.setCurrencyName("British Pound");

                currencyMapperRepository.save(currencyMapper);

                currencyMapperRepository.deleteById(currencyMapper.getId());

                CurrencyMapper deletedCurrencyMapper = currencyMapperRepository.findById(currencyMapper.getId()).orElse(null);

                assertThat(deletedCurrencyMapper).isNull();
        }
}
