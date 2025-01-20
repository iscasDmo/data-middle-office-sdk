package cn.ac.iscas.dmo.api.sdk.model.dataview;

import java.util.ArrayList;
import java.util.List;

/**
 * 或条件
 * @version 1.0
 * @author zhuquanwen
 * @date 2024/11/20 17:00
 */
public class OrCondition extends LogicAbstractCondition {
    /**条件类型*/
    private ConditionType type = ConditionType.OR;

    @Override
    public ConditionType getType() {
        return type;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }

    public static class OrConditionBuilder {
        private final OrCondition condition = new OrCondition();

        public OrCondition build() {
            return condition;
        }

        public OrCondition.OrConditionBuilder subConditions(List<AbstractCondition> subConditions) {
            condition.subConditions = subConditions;
            return this;
        }

        public OrCondition.OrConditionBuilder subCondition(AbstractCondition subCondition) {
            if (condition.subConditions == null) {
                condition.subConditions = new ArrayList<>();
            }
            condition.subConditions.add(subCondition);
            return this;
        }
    }

}
