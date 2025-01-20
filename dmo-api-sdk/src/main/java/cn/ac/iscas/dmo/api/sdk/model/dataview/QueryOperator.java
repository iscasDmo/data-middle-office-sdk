package cn.ac.iscas.dmo.api.sdk.model.dataview;

/**
 * 参考原来的NodeOperator
 * @version 1.0
 * @author zhuquanwen
 * @date 2024/11/20 17:19
 */

public enum QueryOperator {
    EQ("=", "EQ", "等于", 1, ""),

    NE("<>", "NE", "不等于", 1, ""),

    GT(">", "GT", "大于", 1, ""),

    GE(">=", "GE", "大于等于", 1, ""),

    LT("<", "LT", "小于", 1, ""),

    LE("<=", "LE", "小于等于", 1, ""),

    LIKE("LIKE", "LIKE", "模糊匹配", 1, "自动在输入值前后添加%"),

    LIKE_LEFT("LIKE LEFT", "LIKE_LEFT", "模糊匹配(右加百分号)", 1, "自动在输入值后添加%"),

    LIKE_RIGHT("LIKE RIGHT", "LIKE_RIGHT", "模糊匹配(左加百分号)", 1, "自动在输入值前添加%"),

    NOT_LIKE("NOT LIKE", "NOT_LIKE", "模糊匹配的非", 1, "自动在输入值前后添加%"),

    NOT_LIKE_LEFT("NOT LIKE LEFT", "NOT_LIKE_LEFT", "模糊匹配的非(右加百分号)", 1, "自动在输入值后添加%"),

    NOT_LIKE_RIGHT("NOT LIKE RIGHT", "NOT_LIKE_RIGHT", "模糊匹配的非(左加百分号)", 1, "自动在输入值前添加%"),

    BETWEEN("BETWEEN", "BETWEEN", "两值之间", 2, ""),

    NOT_BETWEEN("NOT BETWEEN", "NOT_BETWEEN", "不在两值之间", 2, ""),

    IN("IN", "IN", "包含", 1, "多个值使用逗号分隔"),

    NOT_IN("NOT IN", "NOT_IN", "不包含", 1, "多个值使用逗号分隔"),

    IS_NULL("IS NULL", "IS_NULL", "为空", 0, ""),

    IS_NOT_NULL("IS NOT NULL", "IS_NOT_NULL", "不为空", 0, "");


//    EXISTS("EXISTS", "EXISTS", "存在"),
//
//    NOT_EXISTS("NOT EXISTS", "NOT_EXISTS", "不存在"),

//    //前缀匹配
//    PREFIX("prefix", "PREFIX", "前缀匹配"),
//
//    //正则匹配
//    REGEXP("regexp", "REGEXP", "正则表达式匹配");


    QueryOperator(String operator, String operatorName, String description, Integer inputCount, String prompt) {
        this.operator = operator;
        this.operatorName = operatorName;
        this.description = description;
        this.inputCount = inputCount;
        this.prompt = prompt;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInputCount() {
        return inputCount;
    }

    public void setInputCount(Integer inputCount) {
        this.inputCount = inputCount;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    private String operator;

    private String operatorName;

    private String description;

    private Integer inputCount;

    private String prompt;

}
