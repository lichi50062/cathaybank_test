package org.example.cathaybank.exception;

/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 05:58
 * @Description: 資源找不到例外
 */

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
