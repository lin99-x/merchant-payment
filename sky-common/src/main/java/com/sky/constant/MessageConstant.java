package com.sky.constant;

/**
 * Error message constants
 */
public class MessageConstant {
    public static final String PASSWORD_ERROR = "Incorrect password";
    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String ACCOUNT_LOCKED = "Account is locked";
    public static final String UNKNOWN_ERROR = "Unknown error";
    public static final String USER_NOT_LOGIN = "User not logged in";
    public static final String CATEGORY_BE_RELATED_BY_SETMEAL = "Category is associated with setmeal, cannot delete";
    public static final String CATEGORY_BE_RELATED_BY_DISH = "Category is associated with dish, cannot delete";
    public static final String SHOPPING_CART_IS_NULL = "Shopping cart is empty, cannot place order";
    public static final String ADDRESS_BOOK_IS_NULL = "User address is empty, cannot place order";
    public static final String LOGIN_FAILED = "Login failed";
    public static final String UPLOAD_FAILED = "File upload failed";
    public static final String SETMEAL_ENABLE_FAILED = "Setmeal contains unsold dishes, cannot enable";
    public static final String PASSWORD_EDIT_FAILED = "Password modification failed";
    public static final String DISH_ON_SALE = "Dishes on sale cannot be deleted";
    public static final String SETMEAL_ON_SALE = "Setmeals on sale cannot be deleted";
    public static final String DISH_BE_RELATED_BY_SETMEAL = "Dish is associated with setmeal, cannot delete";
    public static final String ORDER_STATUS_ERROR = "Order status error";
    public static final String ORDER_NOT_FOUND = "Order not found";
    public static final String ORDER_CANNOT_CHECKOUT = "Order cannot be checked out";
    public static final String PAYMENT_NOT_FOUND = "Payment not found";
    public static final String PAYMENT_CANNOT_BE_REFUNDED = "Payment cannot be refunded";
    public static final String REFUND_AMOUNT_EXCEEDS_PAYMENT_AMOUNT = "Refund amount exceeds payment amount";
    public static final String PAYMENT_NOT_BELONG_TO_CURRENT_MERCHANT = "Payment does not belong to current merchant";
    public static final String REFUND_NOT_FOUND = "Refund not found";
    public static final String ONLY_PENDING_REFUND_CAN_BE_CANCELED = "Only pending refunds can be canceled";
    public static final String ORDER_CANNOT_CANCEL = "Order cannot be canceled, can only cancel pending orders";
}