package cn.ac.iscas.dmo.api.sdk.service.impl;

import cn.ac.iscas.dmo.api.sdk.exception.DmoApiSdkException;
import cn.ac.iscas.dmo.api.sdk.http.OkHttpCustomClient;
import cn.ac.iscas.dmo.api.sdk.http.OkHttpProps;
import cn.ac.iscas.dmo.api.sdk.model.*;
import cn.ac.iscas.dmo.api.sdk.model.dataview.GeneralQueryRequest;
import cn.ac.iscas.dmo.api.sdk.model.tdengine.TdEngineSaveRequest;
import cn.ac.iscas.dmo.api.sdk.model.tree.TreeNode;
import cn.ac.iscas.dmo.api.sdk.service.IDmoApi;
import cn.ac.iscas.dmo.api.sdk.util.CheckUtils;
import cn.ac.iscas.dmo.api.sdk.util.JsonUtils;
import cn.ac.iscas.dmo.api.sdk.util.SignApiUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

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
     * 高级动态SQL url
     * */
    private final static String ADVANCE_DYNAMIC_SQL_URL = "/dmo/data-service/advance_dynamic_sql";

    /**
     * 字典查询的URL
     */
    private final static String DIC_SEARCH_URL = "/dmo/dic-service/search";

    /**
     * 参数查询的URL
     */
    private final static String PARAM_SEARCH_URL = "/dmo/param-service/search";

    /**
     * 高级查询的URL
     */
    private final static String ADVANCE_SEARCH_URL = "/dmo/data-service/advance_select";

    /**
     * 高级新增的URL
     */
    private final static String ADVANCE_ADD_URL = "/dmo/data-service/advance_insert";

    /**
     * 高级修改的URL
     */
    private final static String ADVANCE_EDIT_URL = "/dmo/data-service/advance_update";

    /**
     * 高级删除的URL - 拷贝自数据中台
     */
    private final static String ADVANCE_DELETE_URL = "/dmo/data-service/advance_delete";

    /**
     * 数据视图查询
     */
    private final static String DATA_VIEW_SEARCH_URL = "/dmo/data-service/data_view_search";

    /**
     * 表关系URL
     * */
    private final static String TABLE_RELATION_SELECT_URL = "/dmo/data-service/table_relation_select";

    /**
     * 关联数据查询
     */
    private final static String LINK_DATA_SEARCH_URL = "/dmo/data-service/link_data_select";

    /**
     * 高级文件下载
     * */
    private final static String ADVANCE_DOWNLOAD_FILE_URL = "/dmo/file-service/advance_download";

    /**
     * 数据模型查询
     * */
    private final static String DATA_MODEL_SELECT_URL = "/dmo/data-service/data_model_select";

    /**
     * 关联文件上传
     * */
    private final static String LINK_FILE_UPLOAD_URL = "/dmo/file-service/link_file_upload";

    /**
     * 动态生成树
     * */
    private final static String DYNAMIC_TREE_URL = "/dmo/data-service/dynamic_tree";

    /**
     * 关联文件删除
     * */
    private final static String LINK_FILE_DELETE_URL = "/dmo/file-service/link_file_delete";

    /**文件全文检索*/
    private static final String FILE_FULLTEXT_SEARCH_URL = "/dmo/file-service/file_fulltext_search";


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
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<SearchResult>>() {
        });
    }

    @Override
    public ResponseEntity<List<Map<String, Object>>> add(String url, List<Map<String, Object>> items, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
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
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<List<Map<String, Object>>>>() {
        });
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
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Void>>() {
        });
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
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Void>>() {
        });
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
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Void>>() {
        });
    }

    @Override
    public ResponseEntity<Object> dynamicSql(String url, String sql, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        CheckUtils.checkNone(sql, "sql不能为空");
        String jsonBody = "";
//        String sqlParam = "sql=" + sql;
        String encodeParam;
        try {
            if (!isURLEncoded(sql)) {
                sql = URLEncoder.encode(sql, "UTF-8").replace("+", "%20");
            }
            encodeParam = "sql=" + sql;
        } catch (UnsupportedEncodingException e) {
            throw new DmoApiSdkException("编码出错:" + e.getMessage(), e);
        }

        Map<String, String> header = createHeader(authenticationType, "POST", url + "?" + encodeParam, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url + "?sql=" + sql, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<Object> advanceDynamicSql(String datasourceName, String sql, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return advanceDynamicSql(ADVANCE_DYNAMIC_SQL_URL, datasourceName, sql, authenticationType);
    }

    public ResponseEntity<Object> advanceDynamicSql(String url, String datasourceName, String sql, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        CheckUtils.checkNone(sql, "sql不能为空");
        String jsonBody = "";
        if (!isURLEncoded(sql)) {
            try {
                sql = URLEncoder.encode(sql, "UTF-8").replace("+", "%20");
            } catch (UnsupportedEncodingException e) {
                throw new DmoApiSdkException("编码出错:" + e.getMessage(), e);
            }
        }
        AdvanceParam advanceParam = getAdvanceParam(Arrays.asList("datasourceName", "sql"), Arrays.asList(datasourceName, sql));
        Map<String, String> header = createHeader(authenticationType, "POST", url + advanceParam.paramString, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url + advanceParam.oriParamString, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<Object> customSql(String url, List<Object> data, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        CheckUtils.checkNone(data, "data");
        String jsonBody = JsonUtils.toJson(data);
        Map<String, String> header = createHeader(authenticationType, "POST", url, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<List<Dic>> searchDic(String businessName, String dicType, String dicName) throws DmoApiSdkException {
        return searchDic(DIC_SEARCH_URL, businessName, dicType, dicName);
    }

    public ResponseEntity<List<Dic>> searchDic(String url, String businessName, String dicType, String dicName) throws DmoApiSdkException {
        // TODO 暂时字典查询没有验证
        DataServiceAuthenticationType authenticationType = DataServiceAuthenticationType.NONE;

        CheckUtils.checkAuthorizationType(authenticationType, this);
        String jsonBody = "";
        List<String> params = new ArrayList<>();
        List<String> oriParams = new ArrayList<>();
        try {
            if (businessName != null) {
                params.add("businessName=" + URLEncoder.encode(businessName, "UTF-8").replace("+", "%20"));
                oriParams.add("businessName=" + businessName);
            }
            if (dicType != null) {
                params.add("dicType=" + URLEncoder.encode(dicType, "UTF-8").replace("+", "%20"));
                oriParams.add("dicType=" + dicType);
            }
            if (dicName != null) {
                params.add("dicName=" + URLEncoder.encode(dicName, "UTF-8").replace("+", "%20"));
                oriParams.add("dicName=" + dicName);
            }
        } catch (UnsupportedEncodingException e) {
            throw new DmoApiSdkException("编码出错:" + e.getMessage(), e);
        }
        String paramString = "";
        String oriParamString = "";
        if (!params.isEmpty()) {
            paramString = "?" + String.join("&", params);
            oriParamString = "?" + String.join("&", oriParams);
        }

        Map<String, String> header = createHeader(authenticationType, "GET", url + paramString, jsonBody);
        String res;
        try {
            res = httpClient.doGet(dmoEndpoint + url + oriParamString, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<List<Dic>>>() {
        });
    }

    @Override
    public ResponseEntity<List<Param>> searchParam(String businessName, String paramKey, String paramName) throws DmoApiSdkException {
        return searchParam(PARAM_SEARCH_URL, businessName, paramKey, paramName);
    }

    public ResponseEntity<List<Param>> searchParam(String url, String businessName, String paramKey, String paramName) throws DmoApiSdkException {
        // TODO 暂时参数查询没有验证
        DataServiceAuthenticationType authenticationType = DataServiceAuthenticationType.NONE;

        CheckUtils.checkAuthorizationType(authenticationType, this);
        String jsonBody = "";
        List<String> params = new ArrayList<>();
        List<String> oriParams = new ArrayList<>();
        try {
            if (businessName != null) {
                params.add("businessName=" + URLEncoder.encode(businessName, "UTF-8").replace("+", "%20"));
                oriParams.add("businessName=" + businessName);
            }
            if (paramKey != null) {
                params.add("paramKey=" + URLEncoder.encode(paramKey, "UTF-8").replace("+", "%20"));
                oriParams.add("paramKey=" + paramKey);
            }
            if (paramName != null) {
                params.add("paramName=" + URLEncoder.encode(paramName, "UTF-8").replace("+", "%20"));
                oriParams.add("paramName=" + paramName);
            }
        } catch (UnsupportedEncodingException e) {
            throw new DmoApiSdkException("编码出错:" + e.getMessage(), e);
        }
        String paramString = "";
        String oriParamString = "";
        if (!params.isEmpty()) {
            paramString = "?" + String.join("&", params);
            oriParamString = "?" + String.join("&", oriParams);
        }

        Map<String, String> header = createHeader(authenticationType, "GET", url + paramString, jsonBody);
        String res;
        try {
            res = httpClient.doGet(dmoEndpoint + url + oriParamString, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<List<Param>>>() {
        });
    }

    @Override
    public ResponseEntity<SearchResult> advanceSearch(String datasourceName, String tableName, DmoRequest request, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return advanceSearch(ADVANCE_SEARCH_URL, datasourceName, tableName, request, authenticationType);
    }

    public ResponseEntity<SearchResult> advanceSearch(String url, String datasourceName, String tableName, DmoRequest request, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        if (request == null) {
            request = new DmoRequest();
            request.setPageNumber(1);
            request.setPageSize(10);
        }
        String jsonBody = JsonUtils.toJson(request);

        AdvanceParam advanceParam = getAdvanceParam(datasourceName, tableName);
        Map<String, String> header = createHeader(authenticationType, "POST", url + advanceParam.paramString, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url + advanceParam.oriParamString, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<SearchResult>>() {
        });
    }

    @Override
    public ResponseEntity<List<Map<String, Object>>> advanceAdd(String datasourceName, String tableName,
                                           List<Map<String, Object>> items, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return advanceAdd(ADVANCE_ADD_URL, datasourceName, tableName, items, authenticationType);
    }

    public ResponseEntity<List<Map<String, Object>>> advanceAdd(String url, String datasourceName, String tableName,
                                           List<Map<String, Object>> items, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);

        String jsonBody = JsonUtils.toJson(items);

        AdvanceParam advanceParam = getAdvanceParam(datasourceName, tableName);

        Map<String, String> header = createHeader(authenticationType, "POST", url + advanceParam.paramString, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url + advanceParam.oriParamString, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<List<Map<String, Object>>>>(){});
    }

    @Override
    public ResponseEntity<Void> tdEngineAdd(String url, String datasourceName, String tableName, List<TdEngineSaveRequest> items, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        String jsonBody = JsonUtils.toJson(items);
        AdvanceParam advanceParam = getAdvanceParam(datasourceName, tableName);
        Map<String, String> header = createHeader(authenticationType, "POST", url + advanceParam.paramString, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url + advanceParam.oriParamString, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<Void> advanceEdit(String datasourceName, String tableName, List<UpdateEntity> updateEntities, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return advanceEdit(ADVANCE_EDIT_URL, datasourceName, tableName, updateEntities, authenticationType);
    }


    public ResponseEntity<Void> advanceEdit(String url, String datasourceName, String tableName, List<UpdateEntity> updateEntities, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        String jsonBody = JsonUtils.toJson(updateEntities);
        AdvanceParam advanceParam = getAdvanceParam(datasourceName, tableName);
        Map<String, String> header = createHeader(authenticationType, "PUT", url + advanceParam.paramString, jsonBody);
        String res;
        try {
            res = httpClient.doPut(dmoEndpoint + url + advanceParam.oriParamString, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<Void> advanceDelete(String datasourceName, String tableName,
                                              List<Map<String, Object>> deleteEntities,
                                              DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return advanceDelete(ADVANCE_DELETE_URL, datasourceName, tableName, deleteEntities, authenticationType);
    }

    public ResponseEntity<Void> advanceDelete(String url, String datasourceName, String tableName,
                                              List<Map<String, Object>> deleteEntities,
                                              DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        String jsonBody = JsonUtils.toJson(deleteEntities);
        AdvanceParam advanceParam = getAdvanceParam(datasourceName, tableName);
        Map<String, String> header = createHeader(authenticationType, "PUT", url + advanceParam.paramString, jsonBody);
        String res;
        try {
            res = httpClient.doPut(dmoEndpoint + url + advanceParam.oriParamString, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<Map<String, Object>> dataViewSearch(GeneralQueryRequest request, String fileName, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return dataViewSearch(DATA_VIEW_SEARCH_URL, request, fileName, authenticationType);
    }


    public ResponseEntity<Map<String, Object>> dataViewSearch(String url, GeneralQueryRequest request, String fileName, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        String jsonBody = JsonUtils.toJson(request);
        AdvanceParam advanceParam = getAdvanceParam(Collections.singletonList("fileName"), Collections.singletonList(fileName));
        Map<String, String> header = createHeader(authenticationType, "POST", url + advanceParam.paramString, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url + advanceParam.oriParamString, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Map<String, Object>>>() {
        });
    }

    @Override
    public ResponseEntity<List<TableRelationVO>> tableRelationSelect(String datasourceName, String tableName,
                                                                     DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return tableRelationSelect(TABLE_RELATION_SELECT_URL, datasourceName, tableName, authenticationType);
    }

    public ResponseEntity<List<TableRelationVO>> tableRelationSelect(String url, String datasourceName, String tableName,
                                                                     DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        AdvanceParam advanceParam = getAdvanceParam(datasourceName, tableName);
        Map<String, String> header = createHeader(authenticationType, "GET", url + advanceParam.paramString, "");
        String res;
        try {
            String tableRelationSelectUrl = dmoEndpoint + url + advanceParam.oriParamString;
            res = httpClient.doGet(tableRelationSelectUrl, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<List<TableRelationVO>>>() {
        });
    }

    @Override
    public ResponseEntity<SearchResult> linkDataSelect(LinkDataRequest linkDataRequest, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return linkDataSelect(LINK_DATA_SEARCH_URL, linkDataRequest, authenticationType);
    }

    public ResponseEntity<SearchResult> linkDataSelect(String url, LinkDataRequest linkDataRequest, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        String jsonBody = JsonUtils.toJson(linkDataRequest);
        Map<String, String> header = createHeader(authenticationType, "POST", url, jsonBody);
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url, header, jsonBody);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<SearchResult>>() {
        });
    }

    @Override
    public void advanceFileDownload(String datasourceName, String filePath,
                                    OutputStream os, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        advanceFileDownload(ADVANCE_DOWNLOAD_FILE_URL, datasourceName, filePath, os, authenticationType);
    }


    public void advanceFileDownload(String url, String datasourceName, String filePath,
                                    OutputStream os, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        if (datasourceName == null) {
            throw new DmoApiSdkException("datasourceName不能为空");
        }
        if (filePath == null) {
            throw new DmoApiSdkException("filePath不能为空");
        }
        if (os == null) {
            throw new DmoApiSdkException("os不能为空");
        }
        CheckUtils.checkAuthorizationType(authenticationType, this);
        AdvanceParam advanceParam = getAdvanceParam(Arrays.asList("datasourceName", "filePath"), Arrays.asList(datasourceName, filePath));
        Map<String, String> header = createHeader(authenticationType, "GET", url + advanceParam.paramString, "");
        try {
            httpClient.doDownload(dmoEndpoint + url + advanceParam.oriParamString, header, os);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
    }

    @Override
    public ResponseEntity<Void> fileUpload(String url, String parentPath, boolean append,
                                           List<OkHttpCustomClient.UploadInfo> uploadInfos,
                                           DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        if (DataServiceAuthenticationType.SIGN == authenticationType) {
            throw new DmoApiSdkException("文件上传接口暂不支持签名校验方式");
        }
        if (parentPath == null) {
            throw new DmoApiSdkException("parentPath不能为空");
        }
        if (uploadInfos == null || uploadInfos.isEmpty()) {
            throw new DmoApiSdkException("uploadInfos不能为空");
        }
        CheckUtils.checkAuthorizationType(authenticationType, this);

        for (OkHttpCustomClient.UploadInfo uploadInfo : uploadInfos) {
            // 强制将表单的key设置为files
            uploadInfo.setFormKey("files");
        }
        AdvanceParam advanceParam = getAdvanceParam(Arrays.asList("parentPath", "append"),
                Arrays.asList(parentPath, String.valueOf(append)));
        Map<String, String> header = createHeader(authenticationType, "POST", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doUpload(dmoEndpoint + url + advanceParam.oriParamString, header, uploadInfos, new HashMap<>());
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public void fileDownload(String url, String filePath, OutputStream os,
                             DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        if (filePath == null) {
            throw new DmoApiSdkException("filePath不能为空");
        }
        if (os == null) {
            throw new DmoApiSdkException("os不能为空");
        }
        CheckUtils.checkAuthorizationType(authenticationType, this);
        AdvanceParam advanceParam = getAdvanceParam(Collections.singletonList("filePath"), Collections.singletonList(filePath));
        Map<String, String> header = createHeader(authenticationType, "GET", url + advanceParam.paramString, "");
        try {
            httpClient.doDownload(dmoEndpoint + url + advanceParam.oriParamString, header, os);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
    }

    @Override
    public void fileDownloads(String url, String filePath, OutputStream os, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        if (filePath == null) {
            throw new DmoApiSdkException("filePath不能为空");
        }
        if (os == null) {
            throw new DmoApiSdkException("os不能为空");
        }
        CheckUtils.checkAuthorizationType(authenticationType, this);
        AdvanceParam advanceParam = getAdvanceParam(Collections.singletonList("filePaths"), Collections.singletonList(filePath));
        Map<String, String> header = createHeader(authenticationType, "GET", url + advanceParam.paramString, "");
        try {
            httpClient.doDownload(dmoEndpoint + url + advanceParam.oriParamString, header, os);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
    }

    @Override
    public ResponseEntity<List<FileInfo>> fileLs(String url, String filePath, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        if (filePath == null) {
            throw new DmoApiSdkException("filePath不能为空");
        }
        CheckUtils.checkAuthorizationType(authenticationType, this);
        AdvanceParam advanceParam = getAdvanceParam(Collections.singletonList("filePath"), Collections.singletonList(filePath));
        Map<String, String> header = createHeader(authenticationType, "GET", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doGet(dmoEndpoint + url + advanceParam.oriParamString, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<List<FileInfo>>>() {
        });
    }

    @Override
    public ResponseEntity<Void> fileMkdirs(String url, String dirPath, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        if (dirPath == null) {
            throw new DmoApiSdkException("dirPath不能为空");
        }
        CheckUtils.checkAuthorizationType(authenticationType, this);
        AdvanceParam advanceParam = getAdvanceParam(Collections.singletonList("dirPath"), Collections.singletonList(dirPath));
        Map<String, String> header = createHeader(authenticationType, "PUT", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doPut(dmoEndpoint + url + advanceParam.oriParamString, header, new HashMap<>());
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<Void> fileRm(String url, String filePath, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        if (filePath == null) {
            throw new DmoApiSdkException("filePath不能为空");
        }
        CheckUtils.checkAuthorizationType(authenticationType, this);
        AdvanceParam advanceParam = getAdvanceParam(Collections.singletonList("filePath"), Collections.singletonList(filePath));
        Map<String, String> header = createHeader(authenticationType, "DELETE", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doDelete(dmoEndpoint + url + advanceParam.oriParamString, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<Void> fileCp(String url, String oriFilePath, String newFilePath, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        if (oriFilePath == null) {
            throw new DmoApiSdkException("oriFilePath不能为空");
        }
        if (newFilePath == null) {
            throw new DmoApiSdkException("newFilePath不能为空");
        }

        CheckUtils.checkAuthorizationType(authenticationType, this);
        AdvanceParam advanceParam = getAdvanceParam(Arrays.asList("oriFilePath", "newFilePath"), Arrays.asList(oriFilePath, newFilePath));
        Map<String, String> header = createHeader(authenticationType, "POST", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doPost(dmoEndpoint + url + advanceParam.oriParamString, header, new HashMap<>());
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public ResponseEntity<Void> fileMv(String url, String oriFilePath, String newFilePath, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return fileCp(url, oriFilePath, newFilePath, authenticationType);
    }

    @Override
    public ResponseEntity<Map<String, Object>> dataModelSelect(String datasourceName, String tableName, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return dataModelSelect(DATA_MODEL_SELECT_URL, datasourceName, tableName, authenticationType);
    }

    public ResponseEntity<Map<String, Object>> dataModelSelect(String url, String datasourceName, String tableName, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);

        AdvanceParam advanceParam = getAdvanceParam(datasourceName, tableName);

        Map<String, String> header = createHeader(authenticationType, "GET", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doGet(dmoEndpoint + url + advanceParam.oriParamString, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<Map<String, Object>>>() {
        });
    }

    @Override
    public ResponseEntity<Void> linkFileUpload(String datasourceName, String tableName, String parentPath, String refValues, List<OkHttpCustomClient.UploadInfo> uploadInfos, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return linkFileUpload(LINK_FILE_UPLOAD_URL, datasourceName,
                tableName, parentPath, refValues, uploadInfos, authenticationType);
    }

    public ResponseEntity<Void> linkFileUpload(String url, String datasourceName, String tableName, String parentPath, String refValues, List<OkHttpCustomClient.UploadInfo> uploadInfos, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);
        if (parentPath == null) {
            parentPath = "/";
        }

        AdvanceParam advanceParam = getAdvanceParam(Arrays.asList("datasourceName", "tableName", "parentPath", "refValues"),
                Arrays.asList(datasourceName, tableName, parentPath, refValues));

        Map<String, String> header = createHeader(authenticationType, "POST", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doUpload(dmoEndpoint + url + advanceParam.oriParamString, header, uploadInfos, new HashMap<>());
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public <T> ResponseEntity<List<TreeNode<T>>> dynamicTree(String datasourceName, String tableName,
                                                             String labelKey, String valueKey, String pvalueKey,
                                                             String sortKey, Class<T> tClass, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return dynamicTree(DYNAMIC_TREE_URL, datasourceName, tableName, labelKey, valueKey,
                pvalueKey, sortKey, tClass, authenticationType);
    }


    public <T> ResponseEntity<List<TreeNode<T>>> dynamicTree(String url, String datasourceName, String tableName,
                                                             String labelKey, String valueKey, String pvalueKey,
                                                             String sortKey, Class<T> tClass, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);

        AdvanceParam advanceParam = getAdvanceParam(Arrays.asList("datasourceName", "tableName", "labelKey", "valueKey",
                "pvalueKey", "sortKey"), Arrays.asList(datasourceName, tableName, labelKey, valueKey, pvalueKey, sortKey));

        Map<String, String> header = createHeader(authenticationType, "GET", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doGet(dmoEndpoint + url + advanceParam.oriParamString, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<ResponseEntity<List<TreeNode<T>>>>() {
        });
    }

    @Override
    public ResponseEntity<Void> linkFileDelete(String datasourceName, String tableName, String filePath,
                                               String refValues, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return linkFileDelete(LINK_FILE_DELETE_URL, datasourceName, tableName, filePath,
                refValues, authenticationType);
    }

    public ResponseEntity<Void> linkFileDelete(String url, String datasourceName, String tableName, String filePath,
                                               String refValues, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);

        AdvanceParam advanceParam = getAdvanceParam(Arrays.asList("datasourceName", "tableName", "filePath", "refValues"),
                Arrays.asList(datasourceName, tableName, filePath, refValues));

        Map<String, String> header = createHeader(authenticationType, "DELETE", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doDelete(dmoEndpoint + url + advanceParam.oriParamString, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, ResponseEntity.class);
    }

    @Override
    public TableResponse<FileFulltextVO> fileFulltextSearch(String datasourceName, FileFulltextSearchType searchType, String query, Integer pageNumber, Integer pageSize, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        return fileFulltextSearch(FILE_FULLTEXT_SEARCH_URL, datasourceName, searchType,
                query, pageNumber, pageSize, authenticationType);
    }

    @Override
    public TableResponse<FileFulltextVO> fileFulltextSearch(String url, String datasourceName, FileFulltextSearchType searchType, String query, Integer pageNumber, Integer pageSize, DataServiceAuthenticationType authenticationType) throws DmoApiSdkException {
        CheckUtils.checkAuthorizationType(authenticationType, this);

        pageNumber = pageNumber == null ? 1 : pageNumber;
        pageSize = pageSize == null ? 10 : pageSize;

        AdvanceParam advanceParam = getAdvanceParam(Arrays.asList("datasourceName", "query", "pageNumber", "pageSize"),
                Arrays.asList(datasourceName, query, String.valueOf(pageNumber), String.valueOf(pageSize)));

        Map<String, String> header = createHeader(authenticationType, "GET", url + advanceParam.paramString, "");
        String res;
        try {
            res = httpClient.doGet(dmoEndpoint + url + advanceParam.oriParamString, header);
        } catch (IOException e) {
            throw new DmoApiSdkException(e);
        }
        return JsonUtils.fromJson(res, new TypeReference<TableResponse<FileFulltextVO>>() {
        });
    }

    private Map<String, String> createHeader(DataServiceAuthenticationType type, String httpMethod,
                                             String url, String body) throws DmoApiSdkException {
        Map<String, String> headers = new HashMap<>();
        if (type == DataServiceAuthenticationType.SIMPLE) {
            String token = IDmoApi.DYNAMIC_TOKEN.get();
            if (Objects.nonNull(token)) {
                headers.put("Authorization", token);
            } else {
                headers.put("Authorization", this.token);
            }
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

    private static class AdvanceParam {
        public final String paramString;
        public final String oriParamString;

        public AdvanceParam(String paramString, String oriParamString) {
            this.paramString = paramString;
            this.oriParamString = oriParamString;
        }
    }

    private static AdvanceParam getAdvanceParam(String datasourceName, String tableName) throws DmoApiSdkException {
        List<String> params = new ArrayList<>();
        List<String> oriParams = new ArrayList<>();
        try {
            if (datasourceName != null) {
                params.add("datasourceName=" + URLEncoder.encode(datasourceName, "UTF-8").replace("+", "%20"));
                oriParams.add("datasourceName=" + datasourceName);
            }
            if (tableName != null) {
                params.add("tableName=" + URLEncoder.encode(tableName, "UTF-8").replace("+", "%20"));
                oriParams.add("tableName=" + tableName);
            }
        } catch (UnsupportedEncodingException e) {
            throw new DmoApiSdkException("编码出错:" + e.getMessage(), e);
        }
        String paramString = "";
        String oriParamString = "";
        if (!params.isEmpty()) {
            paramString = "?" + String.join("&", params);
            oriParamString = "?" + String.join("&", oriParams);
        }
        return new AdvanceParam(paramString, oriParamString);
    }

    private AdvanceParam getAdvanceParam(List<String> keys, List<String> values) throws DmoApiSdkException {
        List<String> params = new ArrayList<>();
        List<String> oriParams = new ArrayList<>();
        try {
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = values.get(i);
                String encodeValue = value;
                if (!isURLEncoded(value) && Objects.nonNull(value)) {
                    encodeValue = URLEncoder.encode(value, "UTF-8").replace("+", "%20");
                }
                encodeValue = encodeValue == null ? "" : encodeValue;
                value = value == null ? "" : value;
                params.add(key + "=" + encodeValue);
                oriParams.add(key + "=" + value);
            }
        } catch (UnsupportedEncodingException e) {
            throw new DmoApiSdkException("编码出错:" + e.getMessage(), e);
        }
        String paramString = "";
        String oriParamString = "";
        if (!params.isEmpty()) {
            paramString = "?" + String.join("&", params);
            oriParamString = "?" + String.join("&", oriParams);
        }
        return new AdvanceParam(paramString, oriParamString);
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

    public boolean isURLEncoded(String str) {
        try {
            String decoded = URLDecoder.decode(str, "UTF-8");
            return !str.equals(decoded);
        } catch (Exception ignore) {
            return false;
        }
    }
}
