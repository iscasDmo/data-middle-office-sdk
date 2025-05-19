package cn.ac.iscas.dmo.api.sdk.model;



import java.io.Serializable;
import java.util.List;

/**
 * @author zhuquanwen
 * @date 2017/12/25 16:54
 **/
public class TableResponseData<T> implements Serializable{

    /**返回总条目*/
    protected Long rows;
    /**返回的具体数据，是个集合*/
    private List<T> data;


    public static <T> TableResponseData<T> wrapper(Long rows, List<T> data) {
        TableResponseData<T> responseData = new TableResponseData<>();
        responseData.setData(data);
        responseData.setRows(rows);
        return responseData;
    }

    public static <T> TableResponseDataBuilder<T> builder() {
        return new TableResponseDataBuilder<>();
    }

    public static class TableResponseDataBuilder<T> {
        private TableResponseData<T> trd = new TableResponseData<>();

        public TableResponseDataBuilder<T> setRows(Long rows) {
            trd.rows = rows;
            return this;
        }

        public TableResponseDataBuilder<T> setData(List<T> data) {
            trd.data = data;
            return this;
        }

        public TableResponseData<T> build() {
            return trd;
        }
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        this.rows = rows;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
