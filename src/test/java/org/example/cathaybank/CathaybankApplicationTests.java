package org.example.cathaybank;

import org.example.cathaybank.entity.CurrencyMapper;
import org.example.cathaybank.repository.CurrencyMapperRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 06:00
 * @Description:
 */

@SpringBootTest
@Rollback
class CathaybankApplicationTests {

	@Test
	void contextLoads() {
	}  @Autowired
	private CurrencyMapperRepository currencyMapperRepository;

	@Test
	public void testCreateCurrencyMapper() {
		CurrencyMapper currencyMapper = new CurrencyMapper();
		currencyMapper.setCurrencyCode("USD");
		currencyMapper.setCurrencyName("美金");

		currencyMapperRepository.save(currencyMapper);

		assertThat(currencyMapper.getId()).isNotNull();
	}

	@Test
	public void testReadCurrencyMapper() {
		CurrencyMapper currencyMapper = new CurrencyMapper();
		currencyMapper.setCurrencyCode("EuroTESTCode");
		currencyMapper.setCurrencyName("EuroTESTName");

		currencyMapperRepository.save(currencyMapper);

		CurrencyMapper foundCurrencyMapper = currencyMapperRepository.findById(currencyMapper.getId()).orElse(null);

		assertThat(foundCurrencyMapper).isNotNull();
		assertThat(foundCurrencyMapper.getCurrencyCode()).isEqualTo("EuroTESTCode");
		assertThat(foundCurrencyMapper.getCurrencyName()).isEqualTo("EuroTESTName");
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
