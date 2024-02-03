package org.example.cathaybank.controller;

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
public class IndexController {

    @Autowired
    CurrencyMapperRepository currencyMapperRepository;

    @GetMapping("/index")
    public String home() {
        return "index";
    }

}
