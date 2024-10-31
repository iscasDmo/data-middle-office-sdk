package cn.ac.iscas.dmo.api.sdk.model;


import java.util.List;

/**
 * 字典
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/10/18 14:14
 */
public class Dic {
    /**
     * 字典名
     */
    private String dicName;

    /**
     * 字典编号
     */
    private String dicCode;

    /**
     * 所属业务
     */
    private String businessName;

    /**
     * 字典值
     */
    private List<DicValue> dicValues;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private String updateTime;

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<DicValue> getDicValues() {
        return dicValues;
    }

    public void setDicValues(List<DicValue> dicValues) {
        this.dicValues = dicValues;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
