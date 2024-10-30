package cn.ac.iscas.dmo.api.sdk.model;

/**
 * @author lirenshen
 * @version 1.0
 * @date 2022/3/16 13:50
 * @since jdk11
 * 节点操作符
 * TODO 短语匹配、Geo、Graph Fuzzy等
 */
public enum NodeOperator {

    IN("IN", "IN", "包含"),

    NOT_IN("NOT IN", "NOT_IN", "不包含"),

    LIKE("LIKE", "LIKE", "模糊匹配"),

    LIKE_LEFT("LIKE LEFT", "LIKE_LEFT", "模糊匹配(右加百分号)"),

    LIKE_RIGHT("LIKE RIGHT", "LIKE_RIGHT", "模糊匹配(左加百分号)"),

    NOT_LIKE("NOT LIKE", "NOT_LIKE", "模糊匹配的非"),

    EQ("=", "EQ", "等于"),

    NE("<>", "NE", "不等于"),

    GT(">", "GT", "大于"),

    GE(">=", "GE", "大于等于"),

    LT("<", "LT", "小于"),

    LE("<=", "LE", "小于等于"),

    IS_NULL("IS NULL", "IS_NULL", "为空"),

    IS_NOT_NULL("IS NOT NULL", "IS_NOT_NULL", "不为空"),

    EXISTS("EXISTS", "EXISTS", "存在"),

    NOT_EXISTS("NOT EXISTS", "NOT_EXISTS", "不存在"),

    BETWEEN("BETWEEN", "BETWEEN", "两值之间"),

    NOT_BETWEEN("NOT BETWEEN", "NOT_BETWEEN", "不在两值之间"),

    //前缀匹配
    PREFIX("prefix", "PREFIX", "前缀匹配"),

    //正则匹配
    REGEXP("regexp", "REGEXP", "正则表达式匹配");


    NodeOperator(String operator, String operatorName, String description) {
        this.operator = operator;
        this.operatorName = operatorName;
        this.description = description;
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

    private String operator;

    private String operatorName;

    private String description;



}
