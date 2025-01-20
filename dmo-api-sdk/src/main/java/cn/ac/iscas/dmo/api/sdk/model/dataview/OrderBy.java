package cn.ac.iscas.dmo.api.sdk.model.dataview;



/**
 *
 * @version 1.0
 * @author zhuquanwen
 * @date 2024/11/20 17:20
 */
public class OrderBy {
    /**排序列*/
    private String columnIdentity;

    /**排序方式(DESC、ASC)*/
    private SortOrder sortOrder = SortOrder.ASC;

    public String getColumnIdentity() {
        return columnIdentity;
    }

    public void setColumnIdentity(String columnIdentity) {
        this.columnIdentity = columnIdentity;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public OrderBy(String columnIdentity, SortOrder sortOrder) {
        this.columnIdentity = columnIdentity;
        this.sortOrder = sortOrder;
    }

    public OrderBy() {
    }
}
