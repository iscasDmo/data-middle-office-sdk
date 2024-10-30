package cn.ac.iscas.dmo.api.sdk.model;

import java.util.List;

/**
 * @author lirenshen
 * @version 1.0
 * @date 2022/3/16 13:49
 * @since jdk11
 * 节点数据
 */

public class NodeData {

    /**
     * 参数名称
     * 参数名称，一般对应表的列名
     */
    private String param;

    /**
     * 操作符
     */
    private NodeOperator operator;

    /**
     * 参数值
     */
    private List<Object> value;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public NodeOperator getOperator() {
        return operator;
    }

    public void setOperator(NodeOperator operator) {
        this.operator = operator;
    }

    public List<Object> getValue() {
        return value;
    }

    public void setValue(List<Object> value) {
        this.value = value;
    }

}
