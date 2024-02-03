package org.example.cathaybank.exception;

/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 05:58
 * @Description:
 */

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
