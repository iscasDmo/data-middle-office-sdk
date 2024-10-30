package cn.ac.iscas.dmo.api.sdk.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据修改的实体
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2022/7/12 9:40
 * @since jdk11
 */

public class UpdateEntity {
    /**
     * 修改的数据
     */
    private Map<String, Object> data = new LinkedHashMap<>();

    /**
     * 需要将值设置为空的数据
     */
    private List<String> nullCols = new ArrayList<>();

    /**
     * 修改的依据列，比如 update test set c3 = 3 where c1 = 1 and c2 = 2 中的 c1和c2
     */
    private Map<String, Object> updateBy = new LinkedHashMap<>();

    public UpdateEntity combineDataAndNull() {
        data = new LinkedHashMap<>(data);
        nullCols.forEach(col -> data.put(col, null));
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public List<String> getNullCols() {
        return nullCols;
    }

    public void setNullCols(List<String> nullCols) {
        this.nullCols = nullCols;
    }

    public Map<String, Object> getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Map<String, Object> updateBy) {
        this.updateBy = updateBy;
    }
}
