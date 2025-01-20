package cn.ac.iscas.dmo.api.sdk.model;

import java.util.Map;

/**
 * 关联数据查询请求体
 * @version 1.0
 * @author zhuquanwen
 * @date 2025/1/15 17:18
 */
public class LinkDataRequest {
    /**数据库*/
    private String datasourceName;

    /**查询条件*/
    private DmoRequest request;

    /**当前一条数据*/
    private Map<String, Object> item;

    /**要查询的表关系*/
    private TableRelationVO relation;

    public static class LinkDataRequestBuilder {
        private final LinkDataRequest request = new LinkDataRequest();

        public LinkDataRequest build() {
            return request;
        }

        public LinkDataRequestBuilder datasourceName(String datasourceName) {
            request.datasourceName = datasourceName;
            return this;
        }

        public LinkDataRequestBuilder request(DmoRequest dr) {
            request.request = dr;
            return this;
        }

        public LinkDataRequestBuilder item(Map<String, Object> item) {
            request.item = item;
            return this;
        }

        public LinkDataRequestBuilder relation(TableRelationVO relation) {
            request.relation = relation;
            return this;
        }
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public DmoRequest getRequest() {
        return request;
    }

    public void setRequest(DmoRequest request) {
        this.request = request;
    }

    public Map<String, Object> getItem() {
        return item;
    }

    public void setItem(Map<String, Object> item) {
        this.item = item;
    }

    public TableRelationVO getRelation() {
        return relation;
    }

    public void setRelation(TableRelationVO relation) {
        this.relation = relation;
    }
}
