package org.example.cathaybank.controller;

import org.apache.commons.lang3.StringUtils;
import org.example.cathaybank.entity.CurrencyMapper;
import org.example.cathaybank.exception.ResourceNotFoundException;
import org.example.cathaybank.repository.CurrencyMapperRepository;
import org.example.cathaybank.response.BitcoinPriceResponse;
import org.example.cathaybank.response.CurrentBitcoinPriceV2;
import org.example.cathaybank.service.CurrentPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 2:30
 * @Description: 錯誤回傳
 */

@RestController
@RequestMapping("/api")
public class CurrentPriceController {

    @Autowired
    private CurrentPriceService currentPriceService;

    @Autowired
    private CurrencyMapperRepository currencyMapperRepository;

    /**
     * 查詢https://api.coindesk.com/v1/bpi/currentprice.json
     * @return
     */
    @GetMapping("/getCurrentprice")
    public ResponseEntity<BitcoinPriceResponse> getBitcoinPrice() {

        ResponseEntity<BitcoinPriceResponse> response = currentPriceService.getCurrentBitcoinPrice();
        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new ResourceNotFoundException("無法獲取匯率資訊");
        }
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    /**
     * 轉換格式後API
     * @return
     */
    @GetMapping("/getCurrentpriceV2")
    public ResponseEntity<CurrentBitcoinPriceV2> getBitcoinRate() {

        ResponseEntity<CurrentBitcoinPriceV2> currentBitcoinPriceV2 = currentPriceService.getCurrentBitcoinPriceV2();
        if (currentBitcoinPriceV2.getStatusCode() != HttpStatus.OK) {
            throw new ResourceNotFoundException("無法獲取匯率資訊");
        }

        return new ResponseEntity<>(currentBitcoinPriceV2.getBody(), HttpStatus.OK);
    }

    /**
     * 新增幣別
     * @param currencyMapper
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<CurrencyMapper> add(@RequestBody CurrencyMapper currencyMapper) {
        if (StringUtils.isBlank(currencyMapper.getCurrencyCode()) || StringUtils.isBlank(currencyMapper.getCurrencyName())) {
            throw new IllegalArgumentException("傳入參數有誤");
        }
        currencyMapper.getCurrencyCode().toUpperCase();
        CurrencyMapper currencyMapperObj = currencyMapperRepository.save(currencyMapper);
        return new ResponseEntity<>(currencyMapperObj, HttpStatus.CREATED);
    }

    /**
     * 修改幣別
     * @param id
     * @param currencyMapper
     */
    @PutMapping("/updateCurrencyById/{id}")
    public ResponseEntity<CurrencyMapper> update(@PathVariable Long id, @RequestBody CurrencyMapper currencyMapper) {
        if (StringUtils.isBlank(currencyMapper.getCurrencyCode()) || StringUtils.isBlank(currencyMapper.getCurrencyName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CurrencyMapper updatedCurrencyMapper = currencyMapperRepository.findById(id).map(existingCurrencyMapper -> {
            existingCurrencyMapper.setCurrencyCode(currencyMapper.getCurrencyCode());
            existingCurrencyMapper.setCurrencyName(currencyMapper.getCurrencyName());
            return currencyMapperRepository.save(existingCurrencyMapper);
        }).orElseThrow(() -> new ResourceNotFoundException("更新失敗 " + id));

        return new ResponseEntity<>(updatedCurrencyMapper, HttpStatus.OK);
    }

    /**
     * 查詢幣別
     * @param getCurrencyInput
     * @return
     */
    @GetMapping("/getCurrency")
    public ResponseEntity<List<CurrencyMapper>> getCurrency(@RequestParam(value = "getCurrencyInput") String getCurrencyInput) {
        Specification<CurrencyMapper> spec = currentPriceService.nameOrCodeContains(getCurrencyInput);
        List<CurrencyMapper> results = currencyMapperRepository.findAll(spec);
        if (results.isEmpty()) {
            return new ResponseEntity(results, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(results);
    }

    /**
     * 查詢所有幣別
     */
    @GetMapping("/getAllCurrency")
    public ResponseEntity<List<CurrencyMapper>> getAll() {
        List<CurrencyMapper> currencyMappers = currencyMapperRepository.findAll();
        if (currencyMappers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(currencyMappers, HttpStatus.OK);
    }

    /**
     * 刪除幣別
     * @param id
     */
    @DeleteMapping("/deleteCurrencyById/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            currencyMapperRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("刪除失敗 " + id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

