package cn.ac.iscas.dmo.api.sdk.service.impl;

import cn.ac.iscas.dmo.api.sdk.exception.DmoApiSdkException;
import cn.ac.iscas.dmo.api.sdk.http.OkHttpCustomClient;
import cn.ac.iscas.dmo.api.sdk.http.OkHttpProps;
import cn.ac.iscas.dmo.api.sdk.model.*;
import cn.ac.iscas.dmo.api.sdk.model.tdengine.TdEngineSaveRequest;
import cn.ac.iscas.dmo.api.sdk.service.IDmoApi;
import cn.ac.iscas.dmo.api.sdk.util.CheckUtils;
import cn.ac.iscas.dmo.api.sdk.util.JsonUtils;
import cn.ac.iscas.dmo.api.sdk.util.SignApiUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/10/25 17:34
 */

public class DmoApiImpl implements IDmoApi {
    /**
     * http客户端
     */
    private final OkHttpCustomClient httpClient;
    /**
     * 数据中台连接信息，例:http://192.168.50.49:3282
     */
    private final String dmoEndpoint;
    /**
     * 数据中台的token，普通模式使用
     */
    private String token;
    /**
     * 数据中台的appId，签名模式使用
     */
    private String appId;
    /**
     * 数据中台的私钥，签名模式使用
     */
    private String appSecret;

    /**
     * 数据中台API的实现类
     *
     * @param dmoEndpoint 数据中台连接信息，不可为空
     * @date 2024/10/29
     * @since jdk1.8
     */
    public DmoApiImpl(String dmoEndpoint) {
        this.dmoEndpoint = dmoEndpoint;
        httpClient = new OkHttpCustomClient(new OkHttpProps());
    }

    /**
     * 数据中台API的实现类
     *
     * @param dmoEndpoint 数据中台连接信息，不可为空
     * @param props       连接数据中台的连接池信息，可以为空，使用默认值
     * @date 2024/10/29
     * @since jdk1.8
     */
    public DmoApiImpl(String dmoEndpoint, OkHttpProps props) {
        this.dmoEndpoint = dmoEndpoint;
        httpClient = new OkHttpCustomClient(props);
    }

    /**
     * 数据中台API的实现类
     *
     * @param dmoEndpoint 数据中台连接信息，不可为空
     * @param props       连接数据中台的连接池信息，可以为空，使用默认值
     * @param token       数据中台的token，普通鉴权模式使用，可以为空
     * @param appId       数据中台注册的appId,签名鉴权模式使用，可以为空
     * @param appSecret   数据中台注册的appSecret，签名鉴权模式使用，可以为空
     * @date 2024/10/29
     * @since jdk1.8
     */
    public DmoApiImpl(String dmoEndpoint, OkHttpProps props,
                      String token, String appId, String appSecret) {
        this.dmoEndpoint = dmoEndpoint;
        this.token = token;
        this.appId = appId;
        this.appSecret = appSecret;
        httpClient = new OkHttpCustomClient(props);
    }


    @Override
    public ResponseEntity<SearchResult> generalSearch(String url, DmoRequest request,
                                                      DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        if (request == null) {
            request = new DmoRequest();
            request.setPageNumber(1);
            request.setPageSize(10);
        }
        String jsonBody = JsonUtils.toJson(request);
        Map<String, String> header = createHeader(authenticationType, "POST", url, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<SearchResult>>() {});
    }

    @Override
    public ResponseEntity<Void> add(String url, List<Map<String, Object>> items, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        CheckUtils.checkNone(items, "items不能为空");
        String jsonBody = JsonUtils.toJson(items);
        Map<String, String> header = createHeader(authenticationType, "POST", url, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Void>>() {});
    }

    @Override
    public ResponseEntity<Void> tdEngineAdd(String url, List<TdEngineSaveRequest> items, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        CheckUtils.checkNone(items, "items不能为空");
        String jsonBody = JsonUtils.toJson(items);
        Map<String, String> header = createHeader(authenticationType, "POST", url, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Void>>() {});
    }

    @Override
    public ResponseEntity<Void> edit(String url, List<UpdateEntity> updateEntities, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        CheckUtils.checkNone(updateEntities, "updateEntities不能为空");
        String jsonBody = JsonUtils.toJson(updateEntities);
        Map<String, String> header = createHeader(authenticationType, "PUT", url, jsonBody);
        String res;
        try {
            res = httpClient.doPut(dmoEndpoint + url, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Void>>() {});
    }

    @Override
    public ResponseEntity<Void> delete(String url, List<Map<String, Object>> deleteEntities, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        CheckUtils.checkNone(deleteEntities, "deleteEntities不能为空");
        String jsonBody = JsonUtils.toJson(deleteEntities);
        Map<String, String> header = createHeader(authenticationType, "PUT", url, jsonBody);
        String res;
        try {
            res = httpClient.doPut(dmoEndpoint + url, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Void>>() {});
    }

    private Map<String, String> createHeader(DataServiceAuthenticationType type, String httpMethod,
                                             String url, String body) throws DmoApiSdkException {
        Map<String, String> headers = new HashMap<>();
        if (type == DataServiceAuthenticationType.SIMPLE) {
            headers.put("Authorization", token);
        } else if (type == DataServiceAuthenticationType.SIGN) {
            try {
                String token = SignApiUtils.createToken(appId, httpMethod, url, body,
                        UUID.randomUUID().toString(), System.currentTimeMillis(), appSecret);
                headers.put("Authorization", token);
            } catch (Exception e) {
                throw new DmoApiSdkException("生成签名请求头出错:" + e.getMessage(), e);
            }
        }
        return headers;
    }


    public String getToken() {
        return token;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }
}
