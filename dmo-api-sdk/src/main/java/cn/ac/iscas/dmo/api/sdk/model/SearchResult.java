package cn.ac.iscas.dmo.api.sdk.model;

import java.util.List;
import java.util.Map;

/**
 * 引用Datasong中的结构 add by zqw 2022-03-08
 * Created by ZQM on 2016/5/30.
 */

@SuppressWarnings("ALL")
public class SearchResult {
    /**
     * 符合条件的条目总数
     */
    private long total;


    //具体条目
    private List<Map<String, Object>> items;

    //分片结果
    private List<StatisticResult> statistic;

    //private List<MetaColumn> headers;
    private List<String> columnNames;

    private List<Map<String, String>> tableHeader;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public List<StatisticResult> getStatistic() {
        return statistic;
    }

    public void setStatistic(List<StatisticResult> statistic) {
        this.statistic = statistic;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<Map<String, String>> getTableHeader() {
        return tableHeader;
    }

    public void setTableHeader(List<Map<String, String>> tableHeader) {
        this.tableHeader = tableHeader;
    }


}
