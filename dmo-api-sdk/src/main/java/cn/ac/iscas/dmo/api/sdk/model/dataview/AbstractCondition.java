package cn.ac.iscas.dmo.api.sdk.model.dataview;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 *
 * @version 1.0
 * @author zhuquanwen
 * @date 2024/11/20 16:45
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = AndCondition.class, name = "AND"),
        @JsonSubTypes.Type(value = OrCondition.class, name = "OR"),
//        @JsonSubTypes.Type(value = NotCondition.class, name = "NOT"),
        @JsonSubTypes.Type(value = QueryCondition.class, name = "QUERY")
})
public abstract class AbstractCondition {
    /**条件类型*/
    protected ConditionType type;

    public ConditionType getType() {
        return type;
    }

    public void setType(ConditionType type) {
        this.type = type;
    }
}
