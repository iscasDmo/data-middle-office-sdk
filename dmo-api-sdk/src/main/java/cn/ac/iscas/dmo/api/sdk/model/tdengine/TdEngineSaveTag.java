package cn.ac.iscas.dmo.api.sdk.model.tdengine;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2023/9/7 9:44
 */
public class TdEngineSaveTag {
    /**
     * 标签
     * */
    private String key;

    /**
     * 值
     * */
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
