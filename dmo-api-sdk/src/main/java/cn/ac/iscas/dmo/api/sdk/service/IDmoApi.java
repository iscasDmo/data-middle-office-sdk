package cn.ac.iscas.dmo.api.sdk.service;

import cn.ac.iscas.dmo.api.sdk.exception.DmoApiSdkException;
import cn.ac.iscas.dmo.api.sdk.model.*;
import cn.ac.iscas.dmo.api.sdk.model.tdengine.TdEngineSaveRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/10/24 17:03
 */

public interface IDmoApi {

    /**
     * 通用查询
     *
     * @param url                接口URL，从数据中台获取
     * @param request            请求条件
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/29
     * @since jdk1.8
     */
    ResponseEntity<SearchResult> generalSearch(String url, DmoRequest request, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 新增数据(通用)
     *
     * @param url                接口URL，从数据中台获取
     * @param items              新增的数据
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/29
     * @since jdk1.8
     */
    ResponseEntity<Void> add(String url, List<Map<String, Object>> items, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 新增数据(tdengine类型数据库专用)
     *
     * @param url                接口URL，从数据中台获取
     * @param items              新增的数据
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/29
     * @since jdk1.8
     */
    ResponseEntity<Void> tdEngineAdd(String url, List<TdEngineSaveRequest> items, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 修改数据
     *
     * @param url                接口URL，从数据中台获取
     * @param updateEntities     修改的数据
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/29
     * @since jdk1.8
     */
    ResponseEntity<Void> edit(String url, List<UpdateEntity> updateEntities, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 删除数据
     *
     * @param url                接口URL，从数据中台获取
     * @param deleteEntities     修改的数据
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/30
     * @since jdk1.8
     */
    ResponseEntity<Void> delete(String url, List<Map<String, Object>> deleteEntities, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 动态SQL
     *
     * @param url                接口URL，从数据中台获取
     * @param sql     sql语句
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/30
     * @since jdk1.8
     */
    ResponseEntity<Object> dynamicSql(String url, String sql, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

}
