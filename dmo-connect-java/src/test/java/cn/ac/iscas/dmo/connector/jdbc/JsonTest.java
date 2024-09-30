package cn.ac.iscas.dmo.connector.jdbc;

import cn.ac.iscas.dmo.connector.jdbc.model.A;
import cn.ac.iscas.dmo.connector.jdbc.model.B;
import cn.ac.iscas.dmo.connector.jdbc.model.TestModel;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

public class JsonTest {
    private TestModel testModel;
    private String testModelJson;

    @BeforeEach
    void before() {
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
        // 测试解析
        Reader reader = new StringReader(testModelJson);
        Map<String, Object> result = new HashMap<>();
        JsonReader jsonReader = new JsonReader(reader);

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if (key.equals("a")) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key1 = jsonReader.nextName();
                    if ("c1".equals(key1)) {
                        int x1 = jsonReader.nextInt();
                    }
                    if ("c2".equals(key1)) {
                        String x2 = jsonReader.nextString();
                    }
                    if ("bs".equals(key1)) {
                        System.out.println(jsonReader.nextString());
                    }
                }

                jsonReader.endObject();
            }
        }
        jsonReader.endObject();

    }
}
