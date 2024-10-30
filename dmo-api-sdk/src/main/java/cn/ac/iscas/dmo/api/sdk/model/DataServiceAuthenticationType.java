package cn.ac.iscas.dmo.api.sdk.model;


/**
 * API认证方式
 * @author zhuquanwen
 * @version 1.0
 * @date 2022/6/1 13:50
 * @since jdk11
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public enum DataServiceAuthenticationType {

    /**
     * 不认证
     * */
    NONE("NONE", "不认证"),

    /**
     * 普通模式
     * */
    SIMPLE("SIMPLE", "普通模式"),

    /**
     * 签名模式
     * */
    SIGN("SIGN", "签名模式");

    private final String name;
    private final String desc;

    DataServiceAuthenticationType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
