package cn.ac.iscas.dmo.api.sdk.model.dataview;


import java.util.ArrayList;
import java.util.List;

/**
 * 与条件
 * @version 1.0
 * @author zhuquanwen
 * @date 2024/11/20 17:00
 */
public class AndCondition extends LogicAbstractCondition {
    /**条件类型*/
    private ConditionType type = ConditionType.AND;

    @Override
    public ConditionType getType() {
        return type;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }


    public static class AndConditionBuilder {
        private final AndCondition condition = new AndCondition();

        public AndCondition build() {
            return condition;
        }

        public AndConditionBuilder subConditions(List<AbstractCondition> subConditions) {
            condition.subConditions = subConditions;
            return this;
        }

        public AndConditionBuilder subCondition(AbstractCondition subCondition) {
            if (condition.subConditions == null) {
                condition.subConditions = new ArrayList<AbstractCondition>();
            }
            condition.subConditions.add(subCondition);
            return this;
        }
    }
}
