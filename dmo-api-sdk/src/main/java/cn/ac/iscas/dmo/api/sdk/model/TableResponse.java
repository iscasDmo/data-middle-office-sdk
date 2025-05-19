package cn.ac.iscas.dmo.api.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuquanwen
 * @date 2017/12/25 16:40
 **/

public class TableResponse<T> extends ResponseEntity<TableResponseData<T>> implements Serializable {
    public TableResponse() {
    }

    public TableResponse(Integer status, String message) {
        super(status, message);
    }


    public static TableResponse emptyResponse() {
        TableResponseData responseData = new TableResponseData();
        responseData.setRows(0L);
        TableResponse response = new TableResponse();
        response.setValue(responseData);
        return response;
    }

    public static <T> TableResponse<T> ok(long total, List<T> datas) {
        TableResponse<T> response = new TableResponse<>();
        TableResponseData<T> responseData = TableResponseData.wrapper(total, datas);
        response.setValue(responseData);
        return response;
    }

    public List<T> records() {
        if (this.getValue() != null) {
            TableResponseData<T> tableResponseData = this.getValue();
            return tableResponseData.getData() != null ? tableResponseData.getData() : new ArrayList<>(0);
        }
        return new ArrayList<>(0);
    }

}
