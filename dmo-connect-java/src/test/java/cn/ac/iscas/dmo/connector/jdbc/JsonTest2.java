package cn.ac.iscas.dmo.connector.jdbc;

import cn.ac.iscas.dmo.connector.jdbc.model.A;
import cn.ac.iscas.dmo.connector.jdbc.model.B;
import cn.ac.iscas.dmo.connector.jdbc.model.TestModel;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/29 9:33
 */

public class JsonTest2 {
    private TestModel testModel;
    private String testModelJson;

    @Before
    public void before() {
        testModel = new TestModel();
        B b = new B();
        b.setC3(0.5);
        b.setC4(0.01f);
        Map<String, Object> map = new HashMap<>();
        map.put("xxx", 111);
        b.setC5(map);
        List<B> bs = new ArrayList<>();
        bs.add(b);

        A a = new A();
        a.setC1(1);
        a.setC2("lalala");
        a.setBs(bs);

        testModel.setA(a);

        Gson gson = new Gson();
        testModelJson = gson.toJson(testModel);
        System.out.println(testModelJson);
    }

    @Test
    public void test() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(new StringReader(testModelJson));
        JsonToken jsonToken = parser.nextToken();
        while (jsonToken != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            if ("a".equals(fieldName)) {
                jsonToken = parser.nextToken();
                while (jsonToken != JsonToken.END_OBJECT) {
                    String fieldName2 = parser.getCurrentName();
                    if ("bs".equals(fieldName2)) {
                        jsonToken = parser.nextToken();
                        while (jsonToken != JsonToken.END_ARRAY) {
                            String text = parser.getText();
                            System.out.println(text);
                        }
                    }
                    jsonToken = parser.nextToken();
                }
            }
            jsonToken = parser.nextToken();


//            if (current == JsonToken.START_OBJECT) {
//                // 开始处理一个新的JSON对象
//
//            } else if (current == JsonToken.END_OBJECT) {
//                // 结束处理当前的JSON对象
//            } else if (current == JsonToken.FIELD_NAME) {
//                // 获取字段名
//            } else if (current == JsonToken.START_ARRAY) {
//                // 开始处理一个新的JSON数组
//            } else if (current == JsonToken.END_ARRAY) {
//                // 结束处理当前的JSON数组
//            } else if (current == JsonToken.VALUE_STRING) {
//                // 获取字符串值
//            }
//            // 其他的处理逻辑...
        }


//        // 测试解析
//        Reader reader = new StringReader(testModelJson);
//        Map<String, Object> result = new HashMap<>();
//        JsonReader jsonReader = new JsonReader(reader);
//
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()) {
//            String key = jsonReader.nextName();
//            if (key.equals("a")) {
//                jsonReader.beginObject();
//                while (jsonReader.hasNext()) {
//                    String key1 = jsonReader.nextName();
//                    if ("c1".equals(key1)) {
//                        int x1 = jsonReader.nextInt();
//                    }
//                    if ("c2".equals(key1)) {
//                        String x2 = jsonReader.nextString();
//                    }
//                    if ("bs".equals(key1)) {
//                        System.out.println(jsonReader.nextString());
//                    }
//                }
//
//                jsonReader.endObject();
//            }
//        }
//        jsonReader.endObject();

    }

    @Test
    public void test2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(new StringReader(testModelJson));
        JsonNode jsonNode1 = jsonNode.get("a");
        ArrayNode arrayNode = (ArrayNode) jsonNode1.get("bs");
        for (int i = 0; i < arrayNode.size(); i++) {

        }
        System.out.println(arrayNode);
    }

}
