package cn.ac.iscas.dmo.api.sdk.model.dataview;

import java.util.List;

/**
 * 逻辑运算条件
 * @version 1.0
 * @author zhuquanwen
 * @date 2024/11/20 17:04
 */
public class LogicAbstractCondition extends AbstractCondition {
    /**子查询条件*/
    protected List<AbstractCondition> subConditions;

    public List<AbstractCondition> getSubConditions() {
        return subConditions;
    }

    public void setSubConditions(List<AbstractCondition> subConditions) {
        this.subConditions = subConditions;
    }
}
