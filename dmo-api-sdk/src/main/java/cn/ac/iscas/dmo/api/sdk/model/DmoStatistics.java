package cn.ac.iscas.dmo.api.sdk.model;

import java.util.List;

/**
 * @author lirenshen
 * @version 1.0
 * @date 2022/3/16 13:56
 * @since jdk11
 */
public class DmoStatistics {

    private String column;

    private String alias;

    private StatisticsOperator operator;

    private List<String> statisticsColumns;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public StatisticsOperator getOperator() {
        return operator;
    }

    public void setOperator(StatisticsOperator operator) {
        this.operator = operator;
    }

    public List<String> getStatisticsColumns() {
        return statisticsColumns;
    }

    public void setStatisticsColumns(List<String> statisticsColumns) {
        this.statisticsColumns = statisticsColumns;
    }

}
