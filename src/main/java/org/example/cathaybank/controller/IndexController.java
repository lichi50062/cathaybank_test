package org.example.cathaybank.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.cathaybank.repository.CurrencyMapperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: Lichi
 * @Date:2024/02/03/上午 02:51
 * @Description: 首頁controller
 */
@Controller
@Slf4j
public class IndexController {

    @Autowired
    CurrencyMapperRepository currencyMapperRepository;

    @GetMapping("/index")
    public String home() {
        log.info("進入首頁");
        return "index";
    }

}
