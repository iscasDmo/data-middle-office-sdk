package cn.ac.iscas.dmo.api.sdk.model;



import java.util.ArrayList;
import java.util.List;

/**
 * @author lirenshen
 * @version 1.0
 * @date 2022/3/16 15:17
 * @since jdk11
 * 查询节点
 */

public class QueryNode {

    /**
     * 节点类型
     */
    private NodeType type;

    /**
     * 节点数据
     */
    private NodeData data;

    /**
     * 子节点
     */
    private List<QueryNode> children = new ArrayList<QueryNode>();

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public NodeData getData() {
        return data;
    }

    public void setData(NodeData data) {
        this.data = data;
    }

    public List<QueryNode> getChildren() {
        return children;
    }

    public void setChildren(List<QueryNode> children) {
        this.children = children;
    }
}
