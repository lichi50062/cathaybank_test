package org.example.cathaybank.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

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
     * 貨幣代碼code與匯率
     */
    private Map<String, BigDecimal> codeAndRate;
}
