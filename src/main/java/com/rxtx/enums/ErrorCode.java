package com.rxtx.enums;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2021/8/17 15:56
 */
public enum ErrorCode {

    /**
     * 鉴权
     */
    AUTHORIZATION_CHECK_FAIL(401, "authorization.check.fail", ""),
    AUTHORIZATION_IS_OVERDUE(401, "authorization.is.overdue", ""),
    AUTHORIZATION_INVALID(401, "authorization.invalid", ""),
    AUTHORIZATION_EMPTY(401, "authorization.empty", ""),
    /**
     * 该用户无相关资源操作权限
     */
    RESOURCE_AUTH_FAIL(32000001, "resource.authority.error"),

    /**
     * 参数错误
     */
    PARAM_ERROR(32000002, "param.error"),

    /**
     * 参数超过范围
     */
    PARAM_OUT_OF_RANGE(32000003, "param.range.error"),

    /**
     * 错误的枚举值
     */
    ENUM_VALUE_ERROR(32000004, "enum.value.error"),

    /**
     * 字段不符合要求，仅限中英文字母、数字、中划线和下划线，且长度在4-32之间
     */
    PARAM_PATTERN_INCOMPATIBLE(32000005, "param.pattern.incompatible"),

    /**
     * 参数不能为空
     */
    PARAM_NULL_ERROR(32000006, "param.null.error"),

    /**
     * 资源配置初始化失败
     */
    RESOURCE_INIT_ERROR(32000007, "resource.init.error"),

    /**
     * 参数小于最小值
     */
    PARAM_MIN_ERROR(32000008, "param.min"),

    PARAM_MAX_ERROR(32100008, "param.max"),

    DATA_EXIST(32000009, "data.exist"),

    INSERT_ERROR(31000001, "insert.error"),

    SELECT_ERROR(31000002, "select.error"),

    SELECT_EMPTY(31000002, "select.empty"),

    UPDATE_ERROR(31000003, "update.error"),

    DELETE_ERROR(31000004, "delete.error"),

    SIGN_ERROR(31000005, "signTime.error"),
    CLOCK_ERROR(31000006, "clock.error"),

    /**
     * 参数不在枚举范围中
     */
    NOT_IN_ENUM(32000010, "not.in.enum"),

    /**
     * 资源不存在
     */
    RESOURCE_NOT_EXIST(32000012, "resource.not.exist"),

    /**
     * 鉴权 32000060 - 32000079
     */
    USER_EXIST(32000060, "user.exist"),
    USER_NOT_EXIST(32000060, "user.not.exist"),
    USER_DISABLE(32000060, "user.disable"),
    OLD_PASSWORD_ERROR(32000060, "old.password.error"),
    PASSWORD_SAME(32000060, "password.same"),

    /**
     * OpenApi签名校验
     */
    OPENAPI_VERIFY_FAIL(32000100, "openapi.verify.fail"),

    /**
     * 字典
     */
    DIC_TYPE_ALREADY_EXIST(32000121, "dic.type.already.exist"),
    DIC_TYPE_NOT_EXIST(32000122, "dic.type.not.exist"),
    /**
     * 其他
     */
    FILE_UPLOAD_ERROR(32100001, "file.upload.error"),
    FILE_DELETE_ERROR(32100001, "file.delete.error"),
    USER_NOT_LOGIN_IN(32100002, "user.not.login.in"),
    SECRET_NOT_EXIST(32100004, "secret.not.exist"),
    SYNC_ERROR(32100005, "sync.error"),
    RESOURCE_USE(3210008, "resource.use"),
    TIME_WRONG(3210009, "time.wrong"),
    CACHE_ERROR(3222222, "cache.error"),
    ORDER_EXIST(3222223, "order.exist"),

    GROUP_APPLY(3200200, "group.apply"),
    GROUP_UNCREATE(3200202, "group.uncreate"),
    ORDER_HAD_REFUSE(3200201, "order.had.refuse"),

    PASSWORD_ERROR(32000029, "password.error"),

    /**
     * 开放接口调用返回错误
     */
    OPENAPI_ERROR(4000000, "openapi.error"),
    GAODE_API_ERROR(5000000, "gaode.api.error"),
    KDNIAO_API_ERROR(6000000, "kdniao.api.error"),
    PERMISSION_FAILED(32100003, "permission.failed"),


    //micro.app.type.use.error
    MICRO_APP_TYPE_USE_ERROR(32300001,"micro.app.type.use.error"),
    //micro.app.name.exist
    MICRO_APP_NAME_EXIST(32300002,"micro.app.name.exist"),
    //micro.app.cate.config.exist
    MICRO_APP_CATE_CONFIG_EXIST(32300003,"micro.app.name.exist"),
    CATEGORY_ID_EXIST(32300003,"category.id.exist"),
    //app.name.exist
    APP_NAME_EXIST(32300003,"app.name.exist"),
    //banner.title.exist
    BANNER_TITLE_EXIST(32300004,"banner.title.exist"),
    ROOT_ERROR(32300005,"root.error"),

    IAM_ROLE_EXIST(32000006, "iam.role.exist"),
    IAM_PERM_PARENT_ERROR(32000007, "iam.perm.parent.error"),
    SPECIAL_EXIST(32000006, "special.exist"),
    LEADER_SCHEDULE_EXIST(32000009, "schedule.exist"),
    IMPORT_ERROR(3999998, "import.error"),

    //external organization
    SUPPID_NOT_SAME(3200101, "suppid.should.not.same"),
    USER_OR_PWD_ERROR(3200102, "user.or.pwd.error");



    private Integer code;

    private String message;

//    @Value("${cas.serviceUrlPrefix}")
    private String url;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    ErrorCode(Integer code, String message, String url) {
        this.code = code;
        this.message = message;
        this.url = "http://mbtt.wzmtr.com:4443/eip/admin/logout?service=http://mbtt.wzmtr.com:4443/eip/admin/cas";
    }

    public static String messageOf(Integer code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code.equals(code)) {
                return errorCode.message;
            }
        }
        return "";
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public String url() {
        return this.url;
    }

}
