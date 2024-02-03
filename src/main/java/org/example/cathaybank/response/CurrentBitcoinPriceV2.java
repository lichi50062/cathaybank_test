package org.example.cathaybank.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 04:46
 * @Description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentBitcoinPriceV2 {

    /**
     * 更新時間updated
     */
    private String updated;

    /**
     * 更新時間updatedISO
     */
    private String updatedISO;

    /**
     * 更新時間updateduk
     */
    private String updateduk;

    /**
     * 貨幣代碼code與貨幣名稱
     */
    private List<Bpi> bpiList;

    @Data
    @AllArgsConstructor
    public static class Bpi {

        /**
         * 貨幣代碼
         */
        private String currencyCode;

        /**
         * 貨幣名稱
         */
        private String currencyName;

        /**
         * 貨幣價格
         */
        private String rate;


    }
}
