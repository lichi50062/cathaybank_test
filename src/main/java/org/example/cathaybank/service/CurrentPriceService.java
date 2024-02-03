package org.example.cathaybank.service;

import org.apache.commons.lang3.StringUtils;
import org.example.cathaybank.entity.CurrencyMapper;
import org.example.cathaybank.repository.CurrencyMapperRepository;
import org.example.cathaybank.response.BitcoinPriceResponse;
import org.example.cathaybank.response.CurrentBitcoinPriceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @Author: Lichi
 * @Date:2024/02/03/上午 02:39
 * @Description:
 */
@Service
public class CurrentPriceService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CurrencyMapperRepository currencyMapperRepository;

    @Value("${api.coindesk.v1.bpi}")
    private String url;

    /**
     * 發送請求到 https://api.coindesk.com/v1/bpi/currentprice.json
     */
    public ResponseEntity<BitcoinPriceResponse> getCurrentBitcoinPrice() {
        try {
            BitcoinPriceResponse response = restTemplate.getForObject(url, BitcoinPriceResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (HttpClientErrorException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 取得https://api.coindesk.com/v1/bpi/currentprice.json後進行格式轉換
     *
     * @return
     */
    public ResponseEntity<CurrentBitcoinPriceV2> getCurrentBitcoinPriceV2() {
        try {
            ResponseEntity<BitcoinPriceResponse> responseEntity = getCurrentBitcoinPrice();
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK) || responseEntity.getBody() == null) {
                return ResponseEntity.status(responseEntity.getStatusCode()).build();
            }
            BitcoinPriceResponse bitcoinPriceResponse = responseEntity.getBody();
            CurrentBitcoinPriceV2 currentBitcoinPriceV2 = new CurrentBitcoinPriceV2();
            List<CurrentBitcoinPriceV2.Bpi> bpiList = new ArrayList<>();

            List<String> currencyCodes = Arrays.asList("USD", "EUR", "GBP");
            for (String code : currencyCodes) {
                CurrencyMapper currencyMapper = currencyMapperRepository.findByCurrencyCode(code).orElse(null);
                if (currencyMapper != null) {
                    BitcoinPriceResponse.Bpi.Currency currency = getCurrencyFromResponse(bitcoinPriceResponse, code);
                    if (currency != null) {
                        bpiList.add(new CurrentBitcoinPriceV2.Bpi(code, currencyMapper.getCurrencyName(), currency.getRate()));
                    }
                }
            }
            LocalDateTime updated = convertToCustomFormat(bitcoinPriceResponse.getTime().getUpdated());
            LocalDateTime updateduk = convertToCustomFormat(bitcoinPriceResponse.getTime().getUpdateduk());
            LocalDateTime getUpdatedISO = convertToCustomFormat(bitcoinPriceResponse.getTime().getUpdatedISO());
            currentBitcoinPriceV2.setUpdated(updated.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            currentBitcoinPriceV2.setUpdatedISO(updateduk.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            currentBitcoinPriceV2.setUpdateduk(getUpdatedISO.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            currentBitcoinPriceV2.setBpiList(bpiList);
            return ResponseEntity.ok(currentBitcoinPriceV2);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private BitcoinPriceResponse.Bpi.Currency getCurrencyFromResponse(BitcoinPriceResponse response, String code) {
        switch (code) {
            case "USD":
                return response.getBpi().getUsd();
            case "EUR":
                return response.getBpi().getEur();
            case "GBP":
                return response.getBpi().getGbp();
            default:
                return null;
        }
    }

    /**
     * 將日期格式轉換成自定義格式
     *
     * @param inputDate
     * @return
     */
    public static LocalDateTime convertToCustomFormat(String inputDate) {
        DateTimeFormatter inputFormatter;
        if (inputDate.contains("UTC")) {
            inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm:ss z", Locale.ENGLISH);
        } else if (inputDate.contains("GMT")) {
            inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' HH:mm z", Locale.ENGLISH);
        } else {
            inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(inputDate, inputFormatter);
        return zonedDateTime.withZoneSameInstant(java.time.ZoneId.of("Asia/Taipei")).toLocalDateTime();
    }

    /**
     * 動態模糊查詢幣別名稱或代碼
     * @param queryCurrencyInput
     * @return
     */
    public static Specification<CurrencyMapper> nameOrCodeContains(String queryCurrencyInput) {
            return (root, query, criteriaBuilder) -> {
                if (StringUtils.isBlank(queryCurrencyInput)) {
                    return criteriaBuilder.conjunction();
                }
                Predicate namePredicate = criteriaBuilder.like(root.get("currencyName"), "%" + queryCurrencyInput + "%");
                Predicate codePredicate = criteriaBuilder.like(root.get("currencyCode"), "%" + queryCurrencyInput + "%");

                return criteriaBuilder.or(namePredicate, codePredicate);
            };
    }
}

