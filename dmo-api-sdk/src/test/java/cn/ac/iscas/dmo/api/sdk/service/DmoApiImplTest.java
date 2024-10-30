package cn.ac.iscas.dmo.api.sdk.service;

import cn.ac.iscas.dmo.api.sdk.exception.DmoApiSdkException;
import cn.ac.iscas.dmo.api.sdk.http.OkHttpProps;
import cn.ac.iscas.dmo.api.sdk.model.*;
import cn.ac.iscas.dmo.api.sdk.service.impl.DmoApiImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * */
    private final static String TEST_ADD_URL = "/dmo/data-service/主题域1/mysql-dmo/ods_test03/INSERT/1730191311529";

    /**
     * 修改的URL - 拷贝自数据中台
     * */
    private final static String TEST_EDIT_URL = "/dmo/data-service/主题域1/mysql-dmo/ods_test03/UPDATE/1730193456839";

    /**
     * 删除的URL - 拷贝自数据中台
     * */
    private final static String TEST_DELETE_URL = "/dmo/data-service/主题域1/mysql-dmo/ods_test03/DELETE/1730255582758";

    /**
     * 普通认证的TOKEN
     */
    private final static String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRlIjoxNzEyOTAzMDE4LCJle" +
            "HAiOjE4MzUyNDgzMzQsImlhdCI6MTcxMjkwMzAxOCwicGVybWFuZW50bHkiOiJ0cnVlIiwidXNlcm5hbWUiOiJhZG1pbiJ9" +
            ".IBkQMAzdpgFZj313q5yDs6JzrMRvIpTMV2OLVFtwFuw";

    /**
     * 签名模式的appId
     * */
    private final static String APP_ID = "18449783-dd52-42aa-8e7e-5b9f9c3c8418";

    /**
     * 签名模式的appSecret
     * */
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


    private static DmoRequest createSearchRequest() {
        DmoRequest request = new DmoRequest();
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
        updateEntity.setUpdateBy(new HashMap<String, Object>(){{put("id", 17);}});
        updateEntity.setData(new HashMap<String, Object>(){{put("name", "大李四");}});
        updateEntities.add(updateEntity);
        return updateEntities;
    }

    private List<Map<String, Object>> createDeleteEntities() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<String, Object>(){{
            put("id", 18);
        }};
        list.add(item1);
        return list;
    }

}