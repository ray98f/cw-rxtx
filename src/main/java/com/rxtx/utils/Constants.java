package com.rxtx.utils;

import java.util.regex.Pattern;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/14 16:52
 */
public class Constants {

    public static final long DAY_MILL = 86400000L;

    public static final String EMAIL_PATTERN = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    public static final String PHONE_NUM_PATTERN = "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-DD HH:mm:ss";

    public static final long PER_MINUTE_MILLS = 60000;

    public static final int SUCCESS = 0;

    public static final Pattern SMS_CONTENT_PATTERN_1 = Pattern.compile("\\{(.*?)}");

    public static final Pattern SMS_CONTENT_PATTERN_2 = Pattern.compile("#(.*?)#");

    public static final String AUTHORIZATION = "Authorization";

    public static int DATA_NOT_DELETED = 0;

    public static String ZTE_NAME = "zte";

    public static final String OPENAPI_URL = "/api/v1/file/downLoadImg";

    public static final String SWAGGER = "swagger";

    public static final Integer NOT_RELATED = 0;

    public static final Integer ALREADY_RELATED = 1;

    public static final String EMPTY = "";

    public static final String COMMA_EN = ",";

    public static final String PERCENT_SIGN = "%";

    public static final String ROLE_STRING = "role";

    public static final String APP_KEY_STRING = "appKey";

    public static final String APP_SECRET_STRING = "appSecret";

    public static final String TOKEN_STRING = "token";

    public static final String UNDER_LINE = "_";

    public static final Integer ONE = 1;

    public static final Integer TWO = 2;

    public static final String XLS = "xls";

    public static final String XLSX = "xlsx";
}
