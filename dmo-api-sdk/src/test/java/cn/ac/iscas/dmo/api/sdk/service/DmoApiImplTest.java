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
import java.net.URLEncoder;
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
    private final static String TEST_DYNAMIC_SQL_URL = "/dmo/data-service/基础数据/基础数据库/DYNAMIC_SQL/1742802442317";

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
     * 表关系URL
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

    /**文件全文检索*/
    private static final String TEST_FILE_FULLTEXT_SEARCH_URL = "/dmo/file-service/file_fulltext_search";

    /**
     * 普通认证的TOKEN
     */
    private final static String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRlIjoxNzM5MTUyMDYzLCJleHAiOjIyNzcxNjQ4NjYsImlhdCI6MTczOTE1MjA2MywicGVybWFuZW50bHkiOiJ0cnVlIiwidXNlcm5hbWUiOiJhZG1pbiJ9.smnMVgqoSE4mGviCHv6QspKlpVtsPjgLjn4XMPGjnfk";

    /**
     * 签名模式的appId
     */
    private final static String APP_ID = "b30ff719-a666-4004-a3b5-68a63b053efc";

    /**
     * 签名模式的appSecret
     */
    private final static String APP_SECRET = "MIIEugIBADANBgkqhkiG9w0BAQEFAASCBKQwggSgAgEAAoIBAQCzVwnD/dGjk4B6m26PLZjY0A" +
            "3TuAbZjcUrT7oEjmf4+z1ku9EQlrIz46lX7Rx3TFIoifXCBzganjo9w+BsGxaWqu0P7MJkHJHoYsGaSrefaX/500lDx1oLGFgdKxeijtwSl" +
            "4aT2lt91vli6ZCZdacWbiyetJV6RffFlZGKfTlkZiTvHjri/yK2HZWXbbvC4XFA33yFPkQTsEhPYagtlU4wrWnmPbMLN/vQpTXdGEW5kBXp" +
            "cle+3BHfnGnpkn3+40tTHLtiJMmX836BBoqgkCSthD9SWnOy7z1bhW4ju8aycFUMd8WaIAlCwOjfpFm8TmEGYzqiw6wnn7SEAHDfjLSHAgMBAA" +
            "ECggEAMRD2rIdEtI7WH3VoJccxYtxldBAM27fUgpmRCM8XdkDy1p0mQXYhcl3uxo4qdB9O+SVptji4alvxDlYJkDpHD1jbfDhoJPDGIM" +
            "DoPDkGDRjVuTnL+T3RDmkOLojMMh8I6hNH8CrVTlQjLiM9+yZ73QCut4srvGTpOcdGpw/ExIlRe7zliv3EOikrEmUWiMPAAooy7JcAA" +
            "E3Qd1eKNmDGimZejmieSYVv1orydP5Wc6wv9JmhFxSM4AZwB0S2bkkEekGCnwbfVDaJXmXsOIvr12eQpkvomQloyaDcIwx2+8d3UKKQ1" +
            "7yXhjruNS7rdeSFsySzFesIUHEt0C6cp4YIWQKBgQC7yErOUcuKnuDRwlDLqaNIwoHNJVM2PH1D9oQA7uVx0ssoBzOpM0Ym1ZgVhfcBuG" +
            "BuREoE3/dODEI0XzBvqjkAV5tUxXJTG9GlwRqKhMVCzI5GjYDIvH1OPbxF80yCo+163GPMfTN9X6N1J0JCBGGW22+F661oAorVgbEfoeUG" +
            "SQKBgQD0fZr11p7oG/UgA2XeGqb22rHQmeDPC2SMYvlfOykelvdIBVQdGggDihW20w9tR8HRQJuq7izuMUr5dWJ+6FukcpJUELcGyOrvUOP" +
            "RW5pkE43PgmcUTxJUCtoWDRbwRhJ5VC4UXTdBjgBTvpeNWFaehtXVnZt8HMeDR29HjxGkTwKBgBaNfKaA3mA8qqWmn8uGCS1Q5wP7ap4F" +
            "78l8zufJyVa9SVb6XnMAzHkXiBd1qm/TdAlqPDWPrIT8JetiT0/p8sSewPIScKOYoLo2kfh277YRGTlR/1icgrQl5/nCubqq83ehBn9l" +
            "vgxdsyT73RJOsmXK3v8HiiEGxSPVWq+A4x+5AoGAcMs94Wr5dsXnKbTHm1cabkpovH8xWvO7mJTUzV2C0+z7+ksTCjYHOnLLH8Rxmo" +
            "3/WeV/q7UkH2O4XiwMm9Xg6ZY98fCgL84jrPLZl622JbNrui9EFPeB0ioLgR7a1VDOlxECDT0QD/tOJVSJoJj/pTU5k8qZgAHa/bLI" +
            "IPBdSDsCf29ZpM9T6XB0TWcur2yXj0ENXnxOA+/ZxhQJ7ypjFwF49NoomXdHnUBD6LZ/7i/S7aG3Mj0UcoRs0314rEMp/1WZHNlkXnjH" +
            "z2GOgzeq4sEkYVW7wgFK/lrU8LcrywXZoaHv67Z8ne//nazgz5Tvw0z/Kx66ZQzVue+utHbLj2c=";


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

        ResponseEntity<List<Map<String, Object>>> res = dmoApi1.add(TEST_ADD_URL, items, DataServiceAuthenticationType.NONE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 新增-普通认证
     */
    @Test
    public void testAdd2() throws DmoApiSdkException {
        List<Map<String, Object>> items = createAddItems();

        ResponseEntity<List<Map<String, Object>>> res = dmoApi2.add(TEST_ADD_URL, items, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 新增-签名认证
     */
    @Test
    public void testAdd3() throws DmoApiSdkException {
        List<Map<String, Object>> items = createAddItems();

        ResponseEntity<List<Map<String, Object>>> res = dmoApi3.add(TEST_ADD_URL, items, DataServiceAuthenticationType.SIGN);
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
        String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRlIjoxNzQxODY0MDQ0LCJleHAiOjE4M" +
                "DYwNTkyNDcsImlhdCI6MTc0MTg2NDA0NCwicGVybWFuZW50bHkiOiJ0cnVlIiwidXNlcm5hbWUiOiJhZG1pbiJ9.XY" +
                "HfYBudmZLNI2M3_plREdpauzxidw-KtZ871KtYJrk";
        String sql = createDynamicSql();
        ResponseEntity<Object> res;
        try {
            IDmoApi.setDynamicToken(TOKEN);
            res = dmoApi2.dynamicSql(TEST_DYNAMIC_SQL_URL, sql, DataServiceAuthenticationType.SIMPLE);
        } finally {
            IDmoApi.removeDynamicToken();
        }
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
     * 字典查询-不认证
     */
    @Test
    public void testDic2() throws DmoApiSdkException {
        ResponseEntity<List<Dic>> res = dmoApi1.searchDic(null, "sys.backup.status", null);
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
     * 参数查询-不认证
     */
    @Test
    public void testParam2() throws DmoApiSdkException {
        ResponseEntity<List<Param>> res = dmoApi1.searchParam(null, "sys.super.user.default.pwd", null);
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
     * 高级查询-普通认证
     */
    @Test
    public void testAdvanceSearch3() throws DmoApiSdkException {
        DmoRequest request = createSearchRequest();
        ResponseEntity<SearchResult> res = dmoApi2.advanceSearch(
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
        ResponseEntity<List<Map<String, Object>>> res = dmoApi2.advanceAdd(TEST_ADVANCE_ADD_URL,
                "mysql-dmo", "ods_test03", addItems, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 高级新增-普通认证
     */
    @Test
    public void testAdvanceAdd3() throws DmoApiSdkException {
        List<Map<String, Object>> addItems = createAddItems();
        ResponseEntity<List<Map<String, Object>>> res = dmoApi2.advanceAdd(
                "student", "ods_dweg", addItems, DataServiceAuthenticationType.SIMPLE);
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
     * 高级修改-普通认证
     */
    @Test
    public void testAdvanceEdit3() throws DmoApiSdkException {
        List<UpdateEntity> updateEntities = createUpdateEntities();
        ResponseEntity<Void> res = dmoApi2.advanceEdit(
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
     * 高级删除-普通认证
     */
    @Test
    public void testAdvanceDelete3() throws DmoApiSdkException {
        List<Map<String, Object>> deleteEntities = createDeleteEntities();
        ResponseEntity<Void> res = dmoApi2.advanceDelete(
                "mysql-dmo", "ods_test03", deleteEntities, DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 高级动态SQL-普通认证
     */
    @Test
    public void testAdvanceDynamicSql() throws DmoApiSdkException {
        ResponseEntity<Object> res = dmoApi2.advanceDynamicSql(TEST_ADVANCE_DYNAMIC_SQL_URL,
                "地理信息库", "select grid_number, lon_center, lat_center, lon_top_right, lat_top_right, lon_top_left, lat_top_left, lon_bottom_right, lat_bottom_right, lon_bottom_left, lat_bottom_left from \tpublic.grid_2m_8035 g where g.lon_top_right > 79.08551411883477 and g.lat_top_right > 32.50041814133521 \tand g.lon_bottom_left < 79.09616588116523 \tand g.lat_bottom_left < 32.509461858664785   order by grid_number desc  limit 10 offset 1", DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }

    /**
     * 高级动态SQL-普通认证
     */
    @Test
    public void testAdvanceDynamicSql2() throws DmoApiSdkException {
        ResponseEntity<Object> res = dmoApi2.advanceDynamicSql(
                "地理信息库", "select grid_number, lon_center, lat_center, lon_top_right, lat_top_right, lon_top_left, lat_top_left, lon_bottom_right, lat_bottom_right, lon_bottom_left, lat_bottom_left from \tpublic.grid_2m_8035 g where g.lon_top_right > 79.08551411883477 and g.lat_top_right > 32.50041814133521 \tand g.lon_bottom_left < 79.09616588116523 \tand g.lat_bottom_left < 32.509461858664785   order by grid_number desc  limit 10 offset 1", DataServiceAuthenticationType.SIMPLE);
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
     * 高级文件下载
     */
    @Test
    public void testAdvanceFileDownload2() throws DmoApiSdkException, IOException {
        File file = new File("c:/tmp/test.avi");
        try (OutputStream os = new FileOutputStream(file)) {
            dmoApi2.advanceFileDownload( "local-file", "/视频/test.avi", os,
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
     * 数据视图查询
     */
    @Test
    public void testDataViewSearch2() throws DmoApiSdkException, IOException {
        GeneralQueryRequest req = new GeneralQueryRequest.GeneralQueryRequestBuilder()
                .pageNumber(1).pageSize(10).build();
        ResponseEntity<Map<String, Object>> res = dmoApi2.dataViewSearch(req,
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
     * 关联数据查询
     */
    @Test
    public void testLinkDataSearch2() throws DmoApiSdkException, IOException {
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
        ResponseEntity<SearchResult> res = dmoApi2.linkDataSelect(
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
     * 获取表关系
     */
    @Test
    public void testTableRelationSelect2() throws DmoApiSdkException {
        ResponseEntity<List<TableRelationVO>> relations = dmoApi2.tableRelationSelect(
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

    /**
     * 数据模型查询
     * */
    @Test
    public void dataModelSelect2() throws DmoApiSdkException {
        ResponseEntity<Map<String, Object>> res = dmoApi2.dataModelSelect(
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
     * 关联文件上传
     */
    @Test
    public void testLinkFileUpload2() throws DmoApiSdkException, FileNotFoundException {
        List<OkHttpCustomClient.UploadInfo> uploadInfos = createUploadInfos();
        ResponseEntity<Void> res = dmoApi2.linkFileUpload( "101-mysql-dmo",
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
     * 动态生成树
     */
    @Test
    public void testDynamicTree2() throws DmoApiSdkException, FileNotFoundException {
        ResponseEntity<List<TreeNode<Map>>> res = dmoApi2.dynamicTree("161-JDZC", "EQUIPMENT",
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

    /**
     * 关联文件删除
     */
    @Test
    public void testLinkFileDelete2() throws DmoApiSdkException {
        ResponseEntity<Void> res = dmoApi2.linkFileDelete("101-mysql-dmo",
                "dict_data", "/data_ref_files/101-mysql-dmo/dict_data/1/rocketmq_grpc.docx", "1", DataServiceAuthenticationType.SIMPLE);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatus().longValue(), 200L);
    }
    
    /**
     * 文件全文检索
     * */
    @Test
    public void testFileFulltextSearch() throws DmoApiSdkException {
        TableResponse<FileFulltextVO> res = dmoApi2.fileFulltextSearch(TEST_FILE_FULLTEXT_SEARCH_URL,
                null,
                FileFulltextSearchType.all,
                "的", 1, 10, DataServiceAuthenticationType.SIMPLE);
        System.out.println(res);
    }

    /**
     * 文件全文检索
     * */
    @Test
    public void testFileFulltextSearch2() throws DmoApiSdkException {
        TableResponse<FileFulltextVO> res = dmoApi2.fileFulltextSearch(
                null,
                FileFulltextSearchType.all,
                "的", 1, 10, DataServiceAuthenticationType.SIMPLE);
        System.out.println(res);
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
        item1.put("name", "011");
        Map<String, Object> item2 = new HashMap<>();
        item2.put("name", "012");
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
//        return "SELECT * FROM BASEDATA.MIL_TROOP_RESOURCE";
        String sql = "SELECT tr.ID, tr.TROOP_TYPE, tr.LEVEL, tr.POSITION, tr.QUANTITY, tr.SKILL_LEVEL, tr.IS_OPERATOR, tr.TAGS, u.NAME AS UNIT_NAME \n" +
                "FROM BASEDATA.MIL_TROOP_RESOURCE tr \n" +
                "LEFT JOIN BASEDATA.MIL_UNIT u ON tr.UNIT_ID = u.ID WHERE tr.DELETE_FLAG = 0 \n" +
                "AND u.DELETE_FLAG = 0 \n" +
                "AND tr.TROOP_TYPE IN ('步兵') \n" +
                "AND tr.LEVEL IN ('上等兵') \n" +
                "AND tr.POSITION IN ('步枪手') \n" +
                "AND tr.QUANTITY = 20 \n" +
                "AND tr.SKILL_LEVEL IN ('B') \n" +
                "AND tr.IS_OPERATOR = 0 \n" +
                "AND u.NAME LIKE '%维修三排%' \n" +
                "LIMIT 100 OFFSET 0 ";
//        try {
//            sql = URLEncoder.encode(sql, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
        return sql;
    }

    private List<Object> createCustomData() {
        return Arrays.asList(1);
    }

}