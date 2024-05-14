package com.webChat.result;
import lombok.*;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public enum CodeEnum {

    OK(200, "成功"),

    FAIL(500, "失败"),

    TOKEN_IS_EMPTY(901, "请求Token参数为空"),

    TOKEN_IS_ERROR(902, "请求Token有误"),

    TOKEN_IS_EXPIRED(903, "请求Token已过期"),

    TOKEN_IS_NONE_MATCH(904, "请求Token不匹配"),

    USER_LOGOUT_SUCCESS(200, "退出成功"),

    USER_LOGOUT_FAIL(500, "退出成功"),

    DATA_ACCESS_EXCEPTION(500,"数据库操作失败"),

    ACCESS_DENTED(500,"您没有该权限"),

    IP_IP2REGION_FAIL(500,"根据ip2region解析ip地址失败"),


    ;

    //结果码
    private int code;

    //结果信息
    @NonNull
    private String msg;


}
