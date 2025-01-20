package cn.ac.iscas.dmo.api.sdk.model;

import java.util.List;

/**
 * 表关系
 * @version 1.0
 * @author zhuquanwen
 * @date 2025/1/15 10:46
 */
public class TableRelationVO {

    /**表名*/
    private String table;

    /**关联表名*/
    private String targetTable;

//    @Schema(title = "关联表别名", description = "关联表别名")
//    private String targetTableAlias;

    /**是否通过中间表关联*/
    private Boolean middleTableRelation = false;

    /**中间表*/
    private String middleTable;

//    @Schema(title = "中间表别名", description = "中间表别名")
//    private String middleTableAlias;

    /**字段*/
    private List<String> cols;

    /**关联字段*/
    private List<String> targetCols;

    /**中间表字段*/
    private List<String> middleCols;

    /**中间表关联字段*/
    private List<String> targetMiddleCols;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public Boolean getMiddleTableRelation() {
        return middleTableRelation;
    }

    public void setMiddleTableRelation(Boolean middleTableRelation) {
        this.middleTableRelation = middleTableRelation;
    }

    public String getMiddleTable() {
        return middleTable;
    }

    public void setMiddleTable(String middleTable) {
        this.middleTable = middleTable;
    }

    public List<String> getCols() {
        return cols;
    }

    public void setCols(List<String> cols) {
        this.cols = cols;
    }

    public List<String> getTargetCols() {
        return targetCols;
    }

    public void setTargetCols(List<String> targetCols) {
        this.targetCols = targetCols;
    }

    public List<String> getMiddleCols() {
        return middleCols;
    }

    public void setMiddleCols(List<String> middleCols) {
        this.middleCols = middleCols;
    }

    public List<String> getTargetMiddleCols() {
        return targetMiddleCols;
    }

    public void setTargetMiddleCols(List<String> targetMiddleCols) {
        this.targetMiddleCols = targetMiddleCols;
    }
}
