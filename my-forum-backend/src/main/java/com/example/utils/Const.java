package com.example.utils;

/**
 * Used to storage constants
 *
 * @author Jay Wong
 * @date 2023/10/31
 */
public class Const {
    // Jwt token
    public static final String JWT_BLACK_LIST_PREFIX = "jwt:blacklist:";
    public final static String JWT_FREQUENCY = "jwt:frequency:";

    // email verify code
    public static final String VERIFY_EMAIL_LIMIT_PREFIX = "verify:email:limit:";
    public static final String VERIFY_EMAIL_DATA_PREFIX = "verify:email:data:";

    // filter order
    public static final int CORS_ORDER = -102;
    public static final int LIMIT_ORDER = -101;

    // usr role
    public static final String ROLE_DEFAULT = "user";

    // request frequency limit
    public static final String FLOW_LIMIT_COUNTER = "flow:limit:counter:";
    public static final String FLOW_LIMIT_BLOCK = "flow:limit:block:";

    // request custom attribute
    public static final String ATTR_USER_ID = "id";

    // forum related
    public final static String FORUM_WEATHER_CACHE = "weather:cache:";
    public final static String FORUM_IMAGE_COUNTER = "forum:image:";
    public final static String FORUM_TOPIC_CREATE_COUNTER = "forum:topic:create:";
    public final static String FORUM_TOPIC_COMMENT_COUNTER = "forum:topic:comment:";
    public final static String FORUM_TOPIC_PREVIEW_CACHE = "topic:preview:";
}
