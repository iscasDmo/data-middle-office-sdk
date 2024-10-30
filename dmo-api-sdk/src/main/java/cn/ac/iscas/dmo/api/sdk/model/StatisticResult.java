package cn.ac.iscas.dmo.api.sdk.model;

import java.io.Serializable;
import java.util.List;

/**
 * 统计结果，代表一项统计的结果
 * <p>
 * 对简单查询统计，推荐用SimpleSearchDataResponse中 Statistic
 */
@SuppressWarnings("ALL")
public class StatisticResult implements Serializable {
    /**
     * 用于标识该结果对应的统计条件是哪种类型的
     */
    private String typeName;

    /**
     * 别名
     */
    private String alias;

    /**
     * 统计结果
     **/
    private List<StatisticItem> result;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<StatisticItem> getResult() {
        return result;
    }

    public void setResult(List<StatisticItem> result) {
        this.result = result;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
