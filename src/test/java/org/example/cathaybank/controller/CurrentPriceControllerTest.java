package org.example.cathaybank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cathaybank.entity.CurrencyMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 06:30
 * @Description:
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@DisplayName("PointControllerTest")
@Tag("PointControllerTest")
@AutoConfigureMockMvc
@Rollback
@Transactional
public class CurrentPriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 測試獲取當前價格
     * @throws Exception
     */
    @Test
    public void testGetCurrentPrice() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/getCurrentprice").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("回傳值===" + contentAsString);
    }

    /**
     * 測試獲取當前價格
     *
     * @throws Exception
     */
    @Test
    public void testGetCurrentPriceV2() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/getCurrentpriceV2").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updated").exists())
                .andReturn(); // 確保返回的JSON對象中有updated字段

        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("回傳值===" + contentAsString);
    }

    /**
     * 測試新增幣種
     *
     * @throws Exception
     */
    @Test
    public void testAddCurrencyMapper() throws Exception {
        CurrencyMapper currencyMapper = new CurrencyMapper();
        currencyMapper.setCurrencyCode("JPY");
        currencyMapper.setCurrencyName("日圓");

        MvcResult mvcResult = mockMvc.perform(post("/api/add").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(currencyMapper)))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("回傳值===" + contentAsString);
    }

    /**
     * 測試更新幣種
     *
     * @throws Exception
     */
    @Test
    public void testUpdateCurrencyMapper() throws Exception {
        CurrencyMapper currencyMapper = new CurrencyMapper();
        currencyMapper.setCurrencyCode("ETH");
        currencyMapper.setCurrencyName("Ethereum");

        long testId = 42;

        MvcResult mvcResult = mockMvc.perform(put("/api/updateCurrencyById/" + testId).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(currencyMapper)).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
        // 從MvcResult中獲取響應內容
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("回傳值===" + contentAsString);
    }

    /**
     * 測試獲取幣種
     *
     * @throws Exception
     */
    @Test
    public void testGetCurrencyByInput() throws Exception {
        String testInput = "USD";
        MvcResult mvcResult = mockMvc.perform(get("/api/getCurrency").param("getCurrencyInput", testInput).contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("回傳值===" + contentAsString);
    }

    /**
     * 測試獲取所有幣種
     *
     * @throws Exception
     */
    @Test
    public void testGetAllCurrency() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/getAllCurrency").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("回傳值===" + contentAsString);
    }

    /**
     * 測試刪除幣種
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCurrencyById() throws Exception {
        long testId = 42;

        MvcResult mvcResult = mockMvc.perform(delete("/api/deleteCurrencyById/" + testId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // 從MvcResult中獲取響應內容
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("回傳值===" + contentAsString);

    }

}
