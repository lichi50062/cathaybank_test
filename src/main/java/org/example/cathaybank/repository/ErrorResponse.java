package org.example.cathaybank.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 04:56
 * @Description: 錯誤回傳
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
}
