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

    /**是否返回文件*/
    private Boolean withFile = false;

    public static DmoRequestBuilder builder() {
        return new DmoRequestBuilder();
    }

    public static class DmoRequestBuilder {
        private DmoRequest request = new DmoRequest();
        public DmoRequestBuilder node(NodeCreator creator) {
            QueryNode queryNode = creator.create(this);
            request.setNode(queryNode);
            return this;
        }

        public DmoRequestBuilder pageNumber(int pageNumber) {
            request.setPageNumber(pageNumber);
            return this;
        }

        public DmoRequestBuilder pageSize(int pageSize) {
            request.setPageSize(pageSize);
            return this;
        }

        public DmoRequestBuilder page(int pageNumber, int pageSize) {
            request.setPageSize(pageSize);
            request.setPageNumber(pageNumber);
            return this;
        }

        public QueryNodeBuilder nodeBuilder() {
            return new QueryNodeBuilder();
        }

        public DmoRequest build() {
            return request;
        }
    }

    public static class QueryNodeBuilder {
        private QueryNode queryNode = new QueryNode();

        public QueryNodeBuilder type(NodeType nodeType) {
            queryNode.setType(nodeType);
            return this;
        }

        public QueryNodeBuilder data(NodeData nodeData) {
            queryNode.setData(nodeData);
            return this;
        }

        public QueryNodeBuilder child(NodeCreator2 creator2) {
            QueryNode queryNode2 = creator2.create(this);
            queryNode.getChildren().add(queryNode2);
            return this;
        }

        public QueryNodeBuilder nodeBuilder() {
            return new QueryNodeBuilder();
        }

        public QueryNode build() {
            return queryNode;
        }
    }

    @FunctionalInterface
    public interface NodeCreator {
        QueryNode create(DmoRequestBuilder builder);
    }

    @FunctionalInterface
    public interface NodeCreator2 {
        QueryNode create(QueryNodeBuilder builder);
    }


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

    public Boolean getWithFile() {
        return withFile;
    }

    public void setWithFile(Boolean withFile) {
        this.withFile = withFile;
    }
}
