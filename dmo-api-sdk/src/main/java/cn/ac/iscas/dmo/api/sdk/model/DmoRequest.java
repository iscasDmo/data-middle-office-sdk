package cn.ac.iscas.dmo.api.sdk.model;

import java.util.List;
import java.util.Set;

/**
 * @author lirenshen
 * @version 1.0
 * @date 2022/3/16 13:56
 * @since jdk11
 * 请求条件
 */

public class DmoRequest {

    /**
     * 页码
     */
    private int pageNumber;

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 查询条件
     */
    private QueryNode node;

    /**
     * 全文检索条件
     * */
    private QueryNode fulltextNode;

    /**
     * 统计条件
     */
    private List<DmoStatistics> statistics;

    /**
     * 排序条件
     */
    private List<DmoSort> sorts;

    /**
     * 显示的列
     */
    private Set<String> showColumns;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public QueryNode getNode() {
        return node;
    }

    public void setNode(QueryNode node) {
        this.node = node;
    }

    public QueryNode getFulltextNode() {
        return fulltextNode;
    }

    public void setFulltextNode(QueryNode fulltextNode) {
        this.fulltextNode = fulltextNode;
    }

    public List<DmoStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<DmoStatistics> statistics) {
        this.statistics = statistics;
    }

    public List<DmoSort> getSorts() {
        return sorts;
    }

    public void setSorts(List<DmoSort> sorts) {
        this.sorts = sorts;
    }

    public Set<String> getShowColumns() {
        return showColumns;
    }

    public void setShowColumns(Set<String> showColumns) {
        this.showColumns = showColumns;
    }
}
