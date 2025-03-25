package cn.ac.iscas.dmo.api.sdk.service;

import cn.ac.iscas.dmo.api.sdk.exception.DmoApiSdkException;
import cn.ac.iscas.dmo.api.sdk.http.OkHttpCustomClient;
import cn.ac.iscas.dmo.api.sdk.http.OkHttpProps;
import cn.ac.iscas.dmo.api.sdk.model.*;
import cn.ac.iscas.dmo.api.sdk.model.dataview.GeneralQueryRequest;
import cn.ac.iscas.dmo.api.sdk.model.tree.TreeNode;
import cn.ac.iscas.dmo.api.sdk.service.impl.DmoApiImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.util.*;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/10/28 16:46
 */

@RunWith(JUnit4.class)
public class DmoApiImplTest {

    /**
     * 通用查询的URL - 拷贝自数据中台
     */
    private final static String TEST_GENERAL_SEARCH_URL = "/dmo/data-service/主题域1/mysql-dmo/ods_test03/COMMON_SELECT/1730185115000";

    /**
     * 新增的URL - 拷贝自数据中台
     */
    private final static String TEST_ADD_URL = "/dmo/data-service/主题域1/mysql-dmo/ods_test03/INSERT/1730191311529";

    /**
     * 修改的URL - 拷贝自数据中台
     */
    private final static String TEST_EDIT_URL = "/dmo/data-service/主题域1/mysql-dmo/ods_test03/UPDATE/1730193456839";

    /**
     * 删除的URL - 拷贝自数据中台
     */
    private final static String TEST_DELETE_URL = "/dmo/data-service/主题域1/mysql-dmo/ods_test03/DELETE/1730255582758";

    /**
     * 动态SQL的URL - 拷贝自数据中台
     */
    private final static String TEST_DYNAMIC_SQL_URL = "/dmo/data-service/主题域1/mysql-dmo/DYNAMIC_SQL/1729737225913";

    /**
     * 自定义SQL的URL - 拷贝自数据中台
     */
    private final static String TEST_CUSTOM_SQL_URL = "/dmo/data-service/主题域1/mysql-dmo/EXECUTESQL/23";

    /**
     * 字典查询的URL - 拷贝自数据中台
     */
    private final static String TEST_DIC_SEARCH_URL = "/dmo/dic-service/search";

    /**
     * 参数查询的URL - 拷贝自数据中台
     */
    private final static String TEST_PARAM_SEARCH_URL = "/dmo/param-service/search";

    /**
     * 高级查询的URL - 拷贝自数据中台
     */
    private final static String TEST_ADVANCE_SEARCH_URL = "/dmo/data-service/advance_select";

    /**
     * 高级新增的URL - 拷贝自数据中台
     */
    private final static String TEST_ADVANCE_ADD_URL = "/dmo/data-service/advance_insert";

    /**
     * 高级修改的URL - 拷贝自数据中台
     */
    private final static String TEST_ADVANCE_EDIT_URL = "/dmo/data-service/advance_update";


    /**
     * 高级动态SQL - 拷贝自数据中台
     */
    private final static String TEST_ADVANCE_DYNAMIC_SQL_URL = "/dmo/data-service/advance_dynamic_sql";

    /**
     * 数据视图查询 - 拷贝自数据中台
     */
    private final static String TEST_DATA_VIEW_SEARCH_URL = "/dmo/data-service/data_view_search";

    /**
     * 关联数据查询 - 拷贝自数据中台
     */
    private final static String TEST_RELATION_DATA_SEARCH_URL = "/dmo/data-service/link_data_select";

    /**
     * 高级删除的URL - 拷贝自数据中台
     */
    private final static String TEST_ADVANCE_DELETE_URL = "/dmo/data-service/advance_delete";

    /**
     * 测试文件上传
     * */
    private final static String TEST_UPLOAD_FILE_URL = "/dmo/file-service/主题域1/local-file/upload";

    /**
     * 单个文件下载
     * */
    private final static String TEST_DOWNLOAD_FILE_URL = "/dmo/file-service/主题域1/local-file/download";


    /**
     * 高级文件下载
     * */
    private final static String TEST_ADVANCE_DOWNLOAD_FILE_URL = "/dmo/file-service/advance_download";

    /**
     * 多文件下载
     * */
    private final static String TEST_DOWNLOADS_FILE_URL = "/dmo/file-service/主题域1/local-file/downloads";

    /**
     * 获取文件列表
     * */
    private final static String TEST_LS_FILE_URL = "/dmo/file-service/主题域1/local-file/ls";

    /**
     * 创建文件夹
     * */
    private final static String TEST_MKDIRS_FILE_URL = "/dmo/file-service/主题域1/local-file/mkdirs";

    /**
     * 删除文件
     * */
    private final static String TEST_RM_FILE_URL = "/dmo/file-service/主题域1/local-file/rm";

    /**
     * 复制文件
     * */
    private final static String TEST_CP_FILE_URL = "/dmo/file-service/主题域1/local-file/cp";

    /**
     * 移动或重命名文件
     * */
    private final static String TEST_MV_FILE_URL = "/dmo/file-service/主题域1/local-file/mv";

    /**
     * 移动或重命名文件
     * */
    private final static String TEST_TABLE_RELATION_SELECT_URL = "/dmo/data-service/table_relation_select";

    /**
     * 数据模型查询
     * */
    private final static String TEST_DATA_MODEL_SELECT_URL = "/dmo/data-service/data_model_select";

    /**
     * 关联文件上传
     * */
    private final static String TEST_LINK_FILE_UPLOAD_URL = "/dmo/file-service/link_file_upload";

    /**
     * 动态生成树
     * */
    private final static String TEST_DYNAMIC_TREE_URL = "/dmo/data-service/dynamic_tree";

    /**
     * 关联文件删除
     * */
    private final static String TEST_LINK_FILE_DELETE_URL = "/dmo/file-service/link_file_delete";

    /**
     * 普通认证的TOKEN
     */
    private final static String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRlIjoxNzM5MTUyMDYzLCJleHAiOjIyNzcxNjQ4NjYs" +
            "ImlhdCI6MTczOTE1MjA2MywicGVybWFuZW50bHkiOiJ0cnVlIiwidXNlcm5hbWUiOiJhZG1pbiJ9.smnMVgqoSE4mGviCHv6QspKlpVtsPjgLjn4XMPGjnfk";

    /**
     * 签名模式的appId
     */
    private final static String APP_ID = "18449783-dd52-42aa-8e7e-5b9f9c3c8418";

    /**
     * 签名模式的appSecret
     */
    private final static String APP_SECRET = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDHIaIbkrKQZGm" +
            "P15ecUkxI1ozy99Q5W4irvWOSeU57VSkI0m4AbVa/dVfFt6MwO9rExg4Sri3ppeuACyAxS32TKVEdnvwf308q/QEB/44i" +
            "8bwdB9Lj+B8HucwS2GpChJzbJdoZteaB8K8vc+xpp76clpSvDbswjhYXH4fe+ocdRsyciQSYfcFAighb9+Y" +
            "5nywLXebfapZSKi54a9wQxgDAwYxHXX7hs+7B0jwfO03Ju8btCNd8JC9Y67WUFs+PYIIwJ7NYdE3od8KstJh3jtiOhLAuX" +
            "Jk92ydImVXazlScQdvdiX6016ezEZVN/1ioUAWG6wmIWX0eDsEUZTdnqH3tAgMBAAECggEAH4xdkX7BWYz/CHIArBy" +
            "Bx3xuYkQPVHMDCSp9LwSXAYUxdhtYoCqydqzRlhqM+SRSeaBjNGRyE7VcxTF+DTFHmNwP5OB+U1JeyZHB6h4h17lysyq9" +
            "J670w5WVFltBIn6+Fx49GpuuBGIsbxkvmGGiGk7lCYLkaEHqkAlHznTkmSeJ0RSjbJUxa9KZCcr87YLyMAZYQFKs08ssKsXq" +
            "NIhzXyCeLhxkg6ZsJtm28yLef1nj6gugl1yKKkt1Zapapq7rIyTmar5pzR4UnbETWrt3lKV0kXZxd2RbGeEiuEOkhZQO7RBuu" +
            "FEfFvccOBCDr35vA35Z90MaqDk0+rubkn5hhQKBgQDfGKF3kyrPUniEuM5MhVbYyjnsEjSRXLPiJ9JxHcDMDu7ZfY5URDy" +
            "dIi/37Et5QjKv+n1JNAL8nzMfNPPgBVyySDQcfmTqhzjU1LUIVCB4o2vhlY071T+Go92qUOjRKBMNrY6pJ3DtRnoF3JkoY" +
            "RuxPubACK7LjMcS14GeOmsxVwKBgQDkgCuWjp3GYztVbEc+UWREfIXhcGioj/qzOqEJcx14KblR8o0MlVseHHG5anD2B/UZg2J" +
            "KhzhpK1f+dwJh5Cqshgr5QuzIZ+GQhw+vzRER4VtlkNJAV/g/LHtBMFaQqfFNPDNAcODpanEquF1khfmP7NsNLP6ZUqhsrgtP" +
            "pqUsWwKBgQCf7QQyt6jEnhYO7nvUlvE+zxjehd5xJcc9o9ZTiEShA0NtXXgeKBxbeBpy20zvS+kSBbqyMzrA5U68XDVGrhFZLD" +
            "FPC7xD81NmHmT353avhFbdi1uHPMMhPW61nPKInPWrpHLUB5YIQbFPamfQbmxvjuvTVWYYPH6onxwn1TDqWQKBgQC/61sw8IZaK" +
            "9yZCgHouKoTz9cPG7/73DqAMT0y16on1DugNa38pLHMX62xQ1Kvgs19aVm0KVDbHJmi1Eh++gpRr6Al7e09Aet13cryKLYuo26F" +
            "pxbBDmImhXDctwHuLZ7L9UVC2cw+oLV+YS2oEdMj7JerwF/zXXazGZfMhbZzwwKBgQCZQHUk9AIuhl8auqOk/TWhECVBjkFpF" +
            "tc+4y3JC3p2Y+LbVKi0J946Mk1/3dhDHbsYRu+BBCePPM58cr9MJtUVXtXBkxSqSkT95znPKRuVahRP8ECSvtsMYbxi8EyS7f8" +
            "iYpTKWclrrfAxVTuvjMcW8ToNhZpCZ2+a8ixpR0ElDA==";

    IDmoApi dmoApi1;
    IDmoApi dmoApi2;
    IDmoApi dmoApi3;

    @Before
    public void setUp() {
        // 无需鉴权的模式
        dmoApi1 = new DmoApiImpl("http://192.168.50.49:3282");

        // 普通鉴权模式(无需鉴权模式的API也可以调用)
        dmoApi2 = new DmoApiImpl("http://192.168.50.49:3282", new OkHttpProps(),
                TOKEN, null, null);

        // 签名鉴权模式 + 普通鉴权模式 (无需鉴权模式的API、普通鉴权的API、签名鉴权的API都可以调用)
        dmoApi3 = new DmoApiImpl("http://192.168.50.49:3282", new OkHttpProps(),
                TOKEN, APP_ID, APP_SECRET);
    }

    /**
     * 通用查询-不认证
     */
    @Test
    public void testGeneralSearch1() throws DmoApiSdkException {
        DmoRequest request = createSearchRequest();
        ResponseEntity<SearchResult> res = dmoApi1.generalSearch(TEST_GENERAL_SEARCH_URL, request, DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getValue());
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }


    /**
     * 通用查询-普通认证
     */
    @Test
    public void testGeneralSearch2() throws DmoApiSdkException {
        DmoRequest request = createSearchRequest();
        ResponseEntity<SearchResult> res = dmoApi2.generalSearch(TEST_GENERAL_SEARCH_URL, request, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getValue());
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 通用查询-签名认证
     */
    @Test
    public void testGeneralSearch3() throws DmoApiSdkException {
        DmoRequest request = createSearchRequest();
        ResponseEntity<SearchResult> res = dmoApi3.generalSearch(TEST_GENERAL_SEARCH_URL, request, DataServiceAuthenticationType.SIGN);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getValue());
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 新增-不认证
     */
    @Test
    public void testAdd1() throws DmoApiSdkException {
        List<Map<String, Object>> items = createAddItems();

        ResponseEntity<Void> res = dmoApi1.add(TEST_ADD_URL, items, DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 新增-普通认证
     */
    @Test
    public void testAdd2() throws DmoApiSdkException {
        List<Map<String, Object>> items = createAddItems();

        ResponseEntity<Void> res = dmoApi2.add(TEST_ADD_URL, items, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 新增-签名认证
     */
    @Test
    public void testAdd3() throws DmoApiSdkException {
        List<Map<String, Object>> items = createAddItems();

        ResponseEntity<Void> res = dmoApi3.add(TEST_ADD_URL, items, DataServiceAuthenticationType.SIGN);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 编辑-不认证
     */
    @Test
    public void testEdit1() throws DmoApiSdkException {
        List<UpdateEntity> updateEntities = createUpdateEntities();
        ResponseEntity<Void> res = dmoApi1.edit(TEST_EDIT_URL, updateEntities, DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 编辑-普通认证
     */
    @Test
    public void testEdit2() throws DmoApiSdkException {
        List<UpdateEntity> updateEntities = createUpdateEntities();
        ResponseEntity<Void> res = dmoApi2.edit(TEST_EDIT_URL, updateEntities, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 编辑-签名认证
     */
    @Test
    public void testEdit3() throws DmoApiSdkException {
        List<UpdateEntity> updateEntities = createUpdateEntities();
        ResponseEntity<Void> res = dmoApi3.edit(TEST_EDIT_URL, updateEntities, DataServiceAuthenticationType.SIGN);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 删除-不认证
     */
    @Test
    public void testDelete1() throws DmoApiSdkException {
        List<Map<String, Object>> deleteEntities = createDeleteEntities();
        ResponseEntity<Void> res = dmoApi1.delete(TEST_DELETE_URL, deleteEntities, DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }


    /**
     * 删除-普通认证
     */
    @Test
    public void testDelete2() throws DmoApiSdkException {
        List<Map<String, Object>> deleteEntities = createDeleteEntities();
        ResponseEntity<Void> res = dmoApi2.delete(TEST_DELETE_URL, deleteEntities, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 删除-签名认证
     */
    @Test
    public void testDelete3() throws DmoApiSdkException {
        List<Map<String, Object>> deleteEntities = createDeleteEntities();
        ResponseEntity<Void> res = dmoApi3.delete(TEST_DELETE_URL, deleteEntities, DataServiceAuthenticationType.SIGN);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 执行动态SQL-不认证
     */
    @Test
    public void testDynamicSql1() throws DmoApiSdkException {
        String sql = createDynamicSql();
        ResponseEntity<Object> res = dmoApi1.dynamicSql(TEST_DYNAMIC_SQL_URL, sql, DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }


    /**
     * 执行动态SQL-普通认证
     */
    @Test
    public void testDynamicSql2() throws DmoApiSdkException {
        String sql = createDynamicSql();
        ResponseEntity<Object> res = dmoApi2.dynamicSql(TEST_DYNAMIC_SQL_URL, sql, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 执行动态SQL-签名认证
     */
    @Test
    public void testDynamicSql3() throws DmoApiSdkException {
        String sql = createDynamicSql();
        ResponseEntity<Object> res = dmoApi3.dynamicSql(TEST_DYNAMIC_SQL_URL, sql, DataServiceAuthenticationType.SIGN);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 自定义SQL-不认证
     */
    @Test
    public void testCustomSql1() throws DmoApiSdkException {
        List<Object> data = createCustomData();
        ResponseEntity<Object> res = dmoApi1.customSql(TEST_CUSTOM_SQL_URL, data, DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 自定义SQL-普通认证
     */
    @Test
    public void testCustomSql2() throws DmoApiSdkException {
        List<Object> data = createCustomData();
        ResponseEntity<Object> res = dmoApi2.customSql(TEST_CUSTOM_SQL_URL, data, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 自定义SQL-签名认证
     */
    @Test
    public void testCustomSql3() throws DmoApiSdkException {
        List<Object> data = createCustomData();
        ResponseEntity<Object> res = dmoApi3.customSql(TEST_CUSTOM_SQL_URL, data, DataServiceAuthenticationType.SIGN);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 字典查询-不认证
     */
    @Test
    public void testDic1() throws DmoApiSdkException {
        ResponseEntity<List<Dic>> res = dmoApi1.searchDic(TEST_DIC_SEARCH_URL, null, "sys.backup.status", null);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 参数查询-不认证
     */
    @Test
    public void testParam1() throws DmoApiSdkException {
        ResponseEntity<List<Param>> res = dmoApi1.searchParam(TEST_PARAM_SEARCH_URL, null, "sys.super.user.default.pwd", null);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 高级查询-普通认证
     */
    @Test
    public void testAdvanceSearch2() throws DmoApiSdkException {
        DmoRequest request = createSearchRequest();
        ResponseEntity<SearchResult> res = dmoApi2.advanceSearch(TEST_ADVANCE_SEARCH_URL,
                "mysql-dmo", "ods_test03", request, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getValue());
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 高级新增-普通认证
     */
    @Test
    public void testAdvanceAdd2() throws DmoApiSdkException {
        List<Map<String, Object>> addItems = createAddItems();
        ResponseEntity<Void> res = dmoApi2.advanceAdd(TEST_ADVANCE_ADD_URL,
                "mysql-dmo", "ods_test03", addItems, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 高级修改-普通认证
     */
    @Test
    public void testAdvanceEdit2() throws DmoApiSdkException {
        List<UpdateEntity> updateEntities = createUpdateEntities();
        ResponseEntity<Void> res = dmoApi2.advanceEdit(TEST_ADVANCE_EDIT_URL,
                "mysql-dmo", "ods_test03", updateEntities, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 高级删除-普通认证
     */
    @Test
    public void testAdvanceDelete2() throws DmoApiSdkException {
        List<Map<String, Object>> deleteEntities = createDeleteEntities();
        ResponseEntity<Void> res = dmoApi2.advanceDelete(TEST_ADVANCE_DELETE_URL,
                "mysql-dmo", "ods_test03", deleteEntities, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 高级动态SQL-普通认证
     */
    @Test
    public void testAdvanceDynamicSql() throws DmoApiSdkException {
        List<Map<String, Object>> addItems = createAddItems();
        ResponseEntity<Object> res = dmoApi2.advanceDynamicSql(TEST_ADVANCE_DYNAMIC_SQL_URL,
                "student", "SELECT * FROM COURSE", DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 文件上传
     */
    @Test
    public void testFileUpload() throws DmoApiSdkException, FileNotFoundException {
        List<OkHttpCustomClient.UploadInfo> uploadInfos = createUploadInfos();
        ResponseEntity<Void> res = dmoApi2.fileUpload(TEST_UPLOAD_FILE_URL, "/", false, uploadInfos,
                DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 单个文件下载
     */
    @Test
    public void testFileDownload() throws DmoApiSdkException, IOException {
        File file = new File("c:/tmp/public.pgp");
        try (OutputStream os = new FileOutputStream(file)) {
            dmoApi2.fileDownload(TEST_DOWNLOAD_FILE_URL, "/public.pgp", os,
                    DataServiceAuthenticationType.NONE);
        }
    }

    /**
     * 多文件下载
     */
    @Test
    public void testFileDownloads() throws DmoApiSdkException, IOException {
        File file = new File("c:/tmp/test.zip");
        try (OutputStream os = new FileOutputStream(file)) {
            dmoApi2.fileDownloads(TEST_DOWNLOADS_FILE_URL, "/my-test", os,
                    DataServiceAuthenticationType.NONE);
        }
    }

    /**
     * 高级文件下载
     */
    @Test
    public void testAdvanceFileDownload() throws DmoApiSdkException, IOException {
        File file = new File("c:/tmp/test.avi");
        try (OutputStream os = new FileOutputStream(file)) {
            dmoApi2.advanceFileDownload(TEST_ADVANCE_DOWNLOAD_FILE_URL, "local-file", "/视频/test.avi", os,
                    DataServiceAuthenticationType.SIMPLE);
        }
    }

    /**
     * 数据视图查询
     */
    @Test
    public void testDataViewSearch() throws DmoApiSdkException, IOException {
        GeneralQueryRequest req = new GeneralQueryRequest.GeneralQueryRequestBuilder()
                .pageNumber(1).pageSize(10).build();
        ResponseEntity<Map<String, Object>> res = dmoApi2.dataViewSearch(TEST_DATA_VIEW_SEARCH_URL, req,
                "data_view_template_simple.xml",
                DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 关联数据查询
     */
    @Test
    public void testLinkDataSearch() throws DmoApiSdkException, IOException {
        TableRelationVO tableRelationVO = new TableRelationVO();
        tableRelationVO.setTable("ods_TEST_PARENT");
        tableRelationVO.setTargetTable("ods_TEST_CHILD");
        tableRelationVO.setMiddleTableRelation(true);
        tableRelationVO.setMiddleTable("ods_TEST_MIDDLE");
        tableRelationVO.setCols(Collections.singletonList("ID"));
        tableRelationVO.setTargetCols(Collections.singletonList("ID"));
        tableRelationVO.setMiddleCols(Collections.singletonList("PID"));
        tableRelationVO.setTargetMiddleCols(Collections.singletonList("CID"));
        LinkDataRequest linkDataRequest = new LinkDataRequest.LinkDataRequestBuilder()
                .request(new DmoRequest.DmoRequestBuilder().page(1, 10).build())
                .datasourceName("161-dm-DMO")
                .item(new HashMap<String, Object>() {{
                    put("ID", 1);
                    put("NAME", "李四");
                }})
                .relation(tableRelationVO)
                .build();
        ResponseEntity<SearchResult> res = dmoApi2.linkDataSelect(TEST_RELATION_DATA_SEARCH_URL,
                linkDataRequest, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }


    /**
     * 获取文件列表
     */
    @Test
    public void testFileLs() throws DmoApiSdkException, IOException {
        ResponseEntity<List<FileInfo>> res = dmoApi2.fileLs(TEST_LS_FILE_URL, "/", DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getValue());
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 创建文件夹
     */
    @Test
    public void testFileMkdirs() throws DmoApiSdkException, IOException {
        ResponseEntity<Void> res = dmoApi2.fileMkdirs(TEST_MKDIRS_FILE_URL, "/TEST-CREATE-DIR", DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 删除文件
     */
    @Test
    public void testFileDelete() throws DmoApiSdkException, IOException {
        ResponseEntity<Void> res = dmoApi2.fileRm(TEST_RM_FILE_URL, "/TEST-CREATE-DIR", DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 复制文件
     */
    @Test
    public void testFileCp() throws DmoApiSdkException, IOException {
        ResponseEntity<Void> res = dmoApi2.fileCp(TEST_CP_FILE_URL, "/my-test",
                "/my-test2", DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 移动或重命名文件
     */
    @Test
    public void testFileMv() throws DmoApiSdkException, IOException {
        ResponseEntity<Void> res = dmoApi2.fileCp(TEST_MV_FILE_URL, "/my-test2",
                "/my-test3", DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 获取表关系
     */
    @Test
    public void testTableRelationSelect() throws DmoApiSdkException {
        ResponseEntity<List<TableRelationVO>> relations = dmoApi2.tableRelationSelect(TEST_TABLE_RELATION_SELECT_URL,
                "161-dm-DMO",
                "ods_TEST_PARENT",
                DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(relations);
        Assert.assertEquals(relations.getStatus().longValue(), 200L);
    }

    /**
     * 数据模型查询
     * */
    @Test
    public void dataModelSelect() throws DmoApiSdkException {
        ResponseEntity<Map<String, Object>> res = dmoApi2.dataModelSelect(TEST_DATA_MODEL_SELECT_URL,
                "student",
                "course",
                DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    private static List<OkHttpCustomClient.UploadInfo> createUploadInfos() throws FileNotFoundException {
        OkHttpCustomClient.UploadInfo<InputStream> uploadInfo = new OkHttpCustomClient.UploadInfo<>();
        uploadInfo.setFormKey("files");
        uploadInfo.setData(new FileInputStream("C:\\文档资料\\701\\rocketmq+grpc.docx"));
        uploadInfo.setFileName("rocketmq_grpc.docx");
        return Arrays.asList(uploadInfo);
    }

    /**
     * 关联文件上传
     */
    @Test
    public void testLinkFileUpload() throws DmoApiSdkException, FileNotFoundException {
        List<OkHttpCustomClient.UploadInfo> uploadInfos = createUploadInfos();
        ResponseEntity<Void> res = dmoApi2.linkFileUpload(TEST_LINK_FILE_UPLOAD_URL, "101-mysql-dmo",
                "dict_data", "/", "1", uploadInfos, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 动态生成树
     */
    @Test
    public void testDynamicTree() throws DmoApiSdkException, FileNotFoundException {
        ResponseEntity<List<TreeNode<Map>>> res = dmoApi2.dynamicTree(TEST_DYNAMIC_TREE_URL, "161-JDZC", "EQUIPMENT",
                "NAME", "ID", "PID", "ORDER_BY", Map.class, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 关联文件删除
     */
    @Test
    public void testLinkFileDelete() throws DmoApiSdkException {
        ResponseEntity<Void> res = dmoApi2.linkFileDelete(TEST_LINK_FILE_DELETE_URL, "101-mysql-dmo",
                "dict_data", "/data_ref_files/101-mysql-dmo/dict_data/1/rocketmq_grpc.docx", "1", DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    private static DmoRequest createSearchRequest() {
        // 相当于查询 select * from xxx where id > 1 and name like '%三'
        DmoRequest request = DmoRequest.builder()
                .page(1, 10)
                .node(n -> n.nodeBuilder()
                        .type(NodeType.AND)
                        .child(x -> x.nodeBuilder()
                                .type(NodeType.LEAF)
                                .data(new NodeData().setParam("id").setOperator(NodeOperator.GT).setValue(Collections.singletonList(1)))
                                .build())
                        .child(x -> x.nodeBuilder()
                                .type(NodeType.LEAF)
                                .data(new NodeData().setParam("name").setOperator(NodeOperator.LIKE_RIGHT).setValue(Collections.singletonList("三")))
                                .build())
                        .build())
                .build();
        request.setPageNumber(1);
        request.setPageSize(10);
        return request;
    }

    private static List<Map<String, Object>> createAddItems() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("name", "李四");
        Map<String, Object> item2 = new HashMap<>();
        item2.put("name", "赵六");
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        return items;
    }

    private static List<UpdateEntity> createUpdateEntities() {
        List<UpdateEntity> updateEntities = new ArrayList<>();
        UpdateEntity updateEntity = new UpdateEntity();
        updateEntity.setUpdateBy(new HashMap<String, Object>() {{
            put("id", 17);
        }});
        updateEntity.setData(new HashMap<String, Object>() {{
            put("name", "大李四");
        }});
        updateEntities.add(updateEntity);
        return updateEntities;
    }

    private List<Map<String, Object>> createDeleteEntities() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<String, Object>() {{
            put("id", 18);
        }};
        list.add(item1);
        return list;
    }

    private String createDynamicSql() {
        return "SELECT * FROM dict_data";
    }

    private List<Object> createCustomData() {
        return Arrays.asList(1);
    }

}