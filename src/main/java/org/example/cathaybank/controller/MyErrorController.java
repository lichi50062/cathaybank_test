package org.example.cathaybank.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 05:10
 * @Description: 網頁錯誤重導回首頁
 */

@Controller
public class MyErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "redirect:/";
    }

}
