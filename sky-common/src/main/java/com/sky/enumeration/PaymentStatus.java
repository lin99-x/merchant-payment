package com.sky.enumeration;

/**
 * PaymentStatus Enum
 */
public enum PaymentStatus {
    /**
     * Pending payment status
     */
    PENDING,

    /**
     * Successful payment status
     */
    SUCCEEDED,

    /**
     * Cancelled payment status
     */
    CANCELLED,

    /**
     * Failed payment status
     */
    FAILED,

    /**
     * Processing payment status
     */
    PROCESSING,

    /**
     * Refunded payment status
     */
    REFUNDED
}
