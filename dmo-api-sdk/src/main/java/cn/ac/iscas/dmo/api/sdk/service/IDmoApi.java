package cn.ac.iscas.dmo.api.sdk.service;

import cn.ac.iscas.dmo.api.sdk.exception.DmoApiSdkException;
import cn.ac.iscas.dmo.api.sdk.model.*;
import cn.ac.iscas.dmo.api.sdk.model.tdengine.TdEngineSaveRequest;

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
     * @param sql                sql语句
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/30
     * @since jdk1.8
     */
    ResponseEntity<Object> dynamicSql(String url, String sql, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 自定义SQL
     *
     * @param url                接口URL，从数据中台获取
     * @param data               数据
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/30
     * @since jdk1.8
     */
    ResponseEntity<Object> customSql(String url, List<Object> data, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 字典查询
     *
     * @param url          接口URL，从数据中台获取
     * @param businessName 业务应用名 可以为空
     * @param dicType      字典类型  可以为空
     * @param dicName      字典名 可以为空
     * @return 响应结果
     * @date 2024/10/30
     * @since jdk1.8
     */
    ResponseEntity<List<Dic>> searchDic(String url, String businessName, String dicType, String dicName) throws DmoApiSdkException;

    /**
     * +
     * 参数查询
     *
     * @param url          接口URL，从数据中台获取
     * @param businessName 业务应用名 可以为空
     * @param paramKey     参数key  可以为空
     * @param paramName    参数名称 可以为空
     * @return 响应结果
     * @date 2024/10/30
     * @since jdk1.8
     */
    ResponseEntity<List<Param>> searchParam(String url, String businessName, String paramKey, String paramName) throws DmoApiSdkException;

    /**
     * 高级查询
     *
     * @param url                接口URL，从数据中台获取
     * @param datasourceName     数据源名
     * @param tableName          表名
     * @param request            请求条件
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/29
     * @since jdk1.8
     */
    ResponseEntity<SearchResult> advanceSearch(String url, String datasourceName, String tableName,
                                               DmoRequest request, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 高级新增
     *
     * @param url                接口URL，从数据中台获取
     * @param datasourceName     数据源名
     * @param tableName          表名
     * @param items              新增的数据
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/11/05
     * @since jdk1.8
     */
    ResponseEntity<Void> advanceAdd(String url, String datasourceName, String tableName, List<Map<String, Object>> items,
                                    DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;


    /**
     * 新增数据(tdengine类型数据库专用)
     *
     * @param url                接口URL，从数据中台获取
     * @param datasourceName     数据源名
     * @param tableName          表名
     * @param items              新增的数据
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/10/29
     * @since jdk1.8
     */
    ResponseEntity<Void> tdEngineAdd(String url, String datasourceName, String tableName, List<TdEngineSaveRequest> items,
                                     DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;


    /**
     * 高级修改
     *
     * @param url                接口URL，从数据中台获取
     * @param datasourceName     数据源名
     * @param tableName          表名
     * @param updateEntities     修改的实体
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/11/05
     * @since jdk1.8
     */
    ResponseEntity<Void> advanceEdit(String url, String datasourceName, String tableName, List<UpdateEntity> updateEntities,
                                     DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;

    /**
     * 高级删除
     *
     * @param url                接口URL，从数据中台获取
     * @param datasourceName     数据源名
     * @param tableName          表名
     * @param deleteEntities     删除的数据信息
     * @param authenticationType 接口鉴权方式，不鉴权/普通鉴权/签名鉴权
     * @return 响应结果
     * @date 2024/11/05
     * @since jdk1.8
     */
    ResponseEntity<Void> advanceDelete(String url, String datasourceName, String tableName, List<Map<String, Object>> deleteEntities,
                                     DataServiceAuthenticationType authenticationType) throws DmoApiSdkException;
}
