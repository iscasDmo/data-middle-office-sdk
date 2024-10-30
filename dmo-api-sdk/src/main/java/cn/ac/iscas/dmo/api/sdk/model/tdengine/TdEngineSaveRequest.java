package cn.ac.iscas.dmo.api.sdk.model.tdengine;

import java.util.List;
import java.util.Map;

/**
 * tdengine保存数据的实体
 * @author zhuquanwen
 * @version 1.0
 * @date 2023/9/7 9:34
 */

public class TdEngineSaveRequest {

    /**
     * 子表名
     * */
    private String tbname;

    /**
     * 标签信息，可以为空
     * */
    private List<TdEngineSaveTag> tags;

    /**
     * 数据值, 支持多个
     * */
    private List<Map<String, Object>> datas;

    public String getTbname() {
        return tbname;
    }

    public void setTbname(String tbname) {
        this.tbname = tbname;
    }

    public List<TdEngineSaveTag> getTags() {
        return tags;
    }

    public void setTags(List<TdEngineSaveTag> tags) {
        this.tags = tags;
    }

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        this.datas = datas;
    }
}
