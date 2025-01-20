package cn.ac.iscas.dmo.api.sdk.model.dataview;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @version 1.0
 * @author zhuquanwen
 * @date 2024/12/3 13:54
 */
/**数据视图查询-通用查询*/
public class GeneralQueryRequest {
    /**条件*/
    private List<AbstractCondition> conditions;

    /**排序*/
    private List<OrderBy> orderByList;

    /**每页条数*/
    private Integer pageSize;

    /**页码*/
    private Integer pageNumber;

    public List<AbstractCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<AbstractCondition> conditions) {
        this.conditions = conditions;
    }

    public List<OrderBy> getOrderByList() {
        return orderByList;
    }

    public void setOrderByList(List<OrderBy> orderByList) {
        this.orderByList = orderByList;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public static class GeneralQueryRequestBuilder {
        private final GeneralQueryRequest generalQueryRequest = new GeneralQueryRequest();
        public GeneralQueryRequest build() {
            return generalQueryRequest;
        }

        public GeneralQueryRequestBuilder queryCondition(QueryCondition queryCondition) {
            if (generalQueryRequest.conditions == null) {
                generalQueryRequest.conditions = new ArrayList<>();
            }
            generalQueryRequest.conditions.add(queryCondition);
            return this;
        }

        public GeneralQueryRequestBuilder condition(AbstractCondition condition) {
            if (generalQueryRequest.conditions == null) {
                generalQueryRequest.conditions = new ArrayList<>();
            }
            generalQueryRequest.conditions.add(condition);
            return this;
        }

        public GeneralQueryRequestBuilder conditions(List<AbstractCondition> conditions) {
            generalQueryRequest.conditions = conditions;
            return this;
        }

        public GeneralQueryRequestBuilder orderBy(List<OrderBy> orderByList) {
            generalQueryRequest.orderByList = orderByList;
            return this;
        }

        public GeneralQueryRequestBuilder pageSize(Integer pageSize) {
            generalQueryRequest.pageSize = pageSize;
            return this;
        }

        public GeneralQueryRequestBuilder pageNumber(Integer pageNumber) {
            generalQueryRequest.pageNumber = pageNumber;
            return this;
        }

    }
}
