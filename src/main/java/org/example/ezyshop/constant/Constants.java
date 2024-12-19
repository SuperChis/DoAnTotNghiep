package org.example.ezyshop.constant;

public class Constants {
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "10";
    public static final String SORT_BY = "id";
    public static final String SORT_DIR = "asc";
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String[] PUBLIC_URLS = { "/v3/api-docs/**", "/swagger-ui/**", "/api/register/**", "/api/login" };
    public static final String[] USER_URLS = { "/api/public/**" };
    public static final String[] ADMIN_URLS = { "/api/admin/**" };
    public static final double DISCOUNT_FIVE_PERCENT = 0.05;
    public static final double DISCOUNT_TEN_PERCENT = 0.1;
}
