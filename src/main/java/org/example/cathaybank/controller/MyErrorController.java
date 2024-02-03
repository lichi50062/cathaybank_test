package org.example.cathaybank.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 05:10
 * @Description: 錯誤處理controller
 */

@Controller
@Slf4j
public class MyErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
      log.info("進入錯誤頁面");
        return "redirect:/";
    }

}
