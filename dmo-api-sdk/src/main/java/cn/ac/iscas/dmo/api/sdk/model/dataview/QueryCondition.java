package cn.ac.iscas.dmo.api.sdk.model.dataview;


import java.util.List;

/**
 * 查询条件
 * @version 1.0
 * @author zhuquanwen
 * @date 2024/11/20 17:01
 */

public class QueryCondition extends AbstractCondition {
    /**类型*/
    private ConditionType type = ConditionType.QUERY;

    /**参数名称，一般对应表的列名*/
    private String param;

    /**操作符*/
    private QueryOperator operator;

    /**参数值*/
    private List<Object> value;

    @Override
    public ConditionType getType() {
        return type;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public QueryOperator getOperator() {
        return operator;
    }

    public void setOperator(QueryOperator operator) {
        this.operator = operator;
    }

    public List<Object> getValue() {
        return value;
    }

    public void setValue(List<Object> value) {
        this.value = value;
    }

    public QueryCondition(ConditionType type, String param, QueryOperator operator, List<Object> value) {
        this.type = type;
        this.param = param;
        this.operator = operator;
        this.value = value;
    }

    public QueryCondition() {
    }

    public static class QueryConditionBuilder {
        private final QueryCondition condition = new QueryCondition();

        public QueryCondition build() {
            return condition;
        }

        public QueryConditionBuilder param(String param) {
            condition.param = param;
            return this;
        }

        public QueryConditionBuilder operator(QueryOperator operator) {
            condition.operator = operator;
            return this;
        }

        public QueryConditionBuilder value(List<Object> value) {
            condition.value = value;
            return this;
        }
    }

}
