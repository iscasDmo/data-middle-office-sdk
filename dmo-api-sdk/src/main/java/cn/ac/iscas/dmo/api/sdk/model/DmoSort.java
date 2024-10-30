package cn.ac.iscas.dmo.api.sdk.model;

/**
 * @author lirenshen
 * @version 1.0
 * @date 2022/3/16 14:04
 * @since jdk11
 * 排序实体
 */

public class DmoSort {

    /**
     * 排序字段
     */
    private String column;

    /**
     * 排序方式:ASC、DESC
     */
    private DmoSortOrder sortOrder = DmoSortOrder.ASC;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public DmoSortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(DmoSortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
