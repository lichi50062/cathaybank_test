package org.example.cathaybank.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: Lichi
 * @Date:2024/02/03/上午 03:10
 * @Description: coindesk API response
 */
@Data
public class BitcoinPriceResponse {
    private Time time;
    private String disclaimer;
    private String chartName;
    @JsonProperty("bpi")
    private Bpi bpi;

    @Data
    public static class Time {
        private String updated;
        private String updatedISO;
        private String updateduk;
    }

    @Data
    public static class Bpi {
        @JsonProperty("USD")
        private Currency usd;
        @JsonProperty("GBP")
        private Currency gbp;
        @JsonProperty("EUR")
        private Currency eur;

        @Data
        public static class Currency {
            private String code;
            private String symbol;
            private String rate;
            private String description;
            private float rate_float;
        }
    }
}

