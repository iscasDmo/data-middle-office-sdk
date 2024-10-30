package cn.ac.iscas.dmo.api.sdk.model;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 引用Datasong中的结构 add by zqw 2022-03-07
 * <p>
 * 一个统计单元的取值
 */
@SuppressWarnings("ALL")
public class StatisticItem implements Serializable {
    /**
     * 统计单元的key，比如sum min 或者 red 、green等
     * 如果是范围统计则是每个范围取值的下边界，比如10、20、30等
     * 如果是时间统计则是每个时间段的下边界，如1503054204000等
     */
    private Object key;

    /**
     * key的格式化表式
     */
    private String keyAsString;

    /**
     * 统计结果 double 或 integer
     **/
    private Object value;

    //	/**没有嵌套统计忽略这个变量！如果有嵌套统计，对应的下一级统计结果
//	 每一个统计项下可以嵌套多个其他的统计**/
    private List<StatisticResult> children;

    /**
     * 随统计信息一起返回的列信息
     */
    private Map<String, Object> properties;

    public StatisticItem() {

    }

    public StatisticItem(Object key, long value) {
        this.key = key;
        this.setValue(value);
    }

    public StatisticItem(Object key, double value) {
        this.key = key;
        this.setValue(value);
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
        if (keyAsString == null && key != null) {
            keyAsString = key.toString();
        }
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<StatisticResult> getChildren() {
        return children;
    }

    public void setChildren(List<StatisticResult> children) {
        this.children = children;
    }

    public String getKeyAsString() {
        return keyAsString;
    }

    public void setKeyAsString(String keyAsString) {
        this.keyAsString = keyAsString;
    }


    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }


}
