package cn.ac.iscas.dmo.connector.jdbc.statement;

import cn.ac.iscas.dmo.connector.conf.HostInfo;
import cn.ac.iscas.dmo.connector.jdbc.ConnectionImpl;
import cn.ac.iscas.dmo.connector.jdbc.ResultSetImpl;
import cn.ac.iscas.dmo.connector.util.JdkSerializableUtils;
import cn.ac.iscas.dmo.connector.util.JsonUtils;
import cn.ac.iscas.dmo.db.rpc.execute.ExecuteRequest;
import cn.ac.iscas.dmo.db.rpc.execute.ExecuteResponse;
import cn.ac.iscas.dmo.db.rpc.execute.StreamServiceGrpc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * 查询处理器
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 14:46
 */

public class ExecuteQuery {

    public static void execute(ResultSetImpl rs, String sql) throws SQLException, IOException {
        ConnectionImpl connection = (ConnectionImpl) rs.getStatement().getConnection();
        Map<String, Object> mapValue = doExecute(connection, sql);
        List<Map<String, Object>> values = new ArrayList<>();
        if (mapValue != null) {
            Object o = mapValue.get("data");
            if (o instanceof Integer) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put(String.valueOf(o), o);
                values = new ArrayList<>();
                values.add(map2);
            } else {
                values = (List<Map<String, Object>>) o;
            }
            List<Map<String, Object>> metas = (List<Map<String, Object>>) mapValue.get("metas");
            if (metas != null) {
                Map<Integer, String> headerMapping = new HashMap<>(16);
                for (int i = 0; i < metas.size(); i++) {
                    headerMapping.put(i, (String) metas.get(i).get("columnName"));
                }
                if (headerMapping.isEmpty() && !values.isEmpty()) {
                    Map<String, Object> map = values.getFirst();
                    if (map.containsKey("@@Identity")) {
                        Object generateId = map.get("@@Identity");
                        headerMapping.put(0, "@@Identity");
                        Map<String, Object> meta = new HashMap<>();
                        meta.put("columnName", "@@Identity");
                        // todo
                        meta.put("columnType", -5);
                        metas.add(meta);
                    }
                }
                rs.setHeaderMapping(headerMapping);
                rs.setMetas(metas);
            }
        }
        rs.setCacheData(values);
    }

//    public static Map<String, Object> doExecute(ConnectionImpl connection, String sql) throws SQLException, IOException {
//        String token = connection.getToken();
//        HostInfo origHostInfo = connection.getOrigHostInfo();
//        String sqlServiceUrl = connection.getSqlServiceUrl();
//        String useSsl = connection.getUseSsl();
//        OkHttpCustomClient httpClient = connection.getHttpClient();
//        Map<String, String> header = new HashMap<>();
//        if (Objects.nonNull(token) && !token.isEmpty()) {
//            header.put("Authorization", token);
//        }
//        String protocol = "https://";
//        if ("false".equals(useSsl)) {
//            protocol = "http://";
//        }
//        sqlServiceUrl = protocol + origHostInfo.getHost() + ":" + origHostInfo.getPort() + sqlServiceUrl;
//
//        sql = sql.replace("\n", " ").replace("\t", " ");
//        sql = URLEncoder.encode(sql, "UTF-8");
//        Response response = httpClient.doPostWithRes(sqlServiceUrl + "?sql=" + sql, header, new HashMap<>());
//        int code = response.code();
//        if (code != 200) {
//            String message = response.body().string();
//            if (message != null && message.length() > 200) {
//                message = message.substring(0, 200) + "...";
//            }
//            throw new SQLException("查询出错:" + message);
//        }
////        String string = response.body().string();
////        Map<String, Object> map = (Map<String, Object>) JSON.parse(string);
////        Map<String, Object> map = JsonUtils.fromJson(response.body().bytes(), Map.class);
//        Map<String, Object> map = analysisRes(response.body().bytes());
//        return map;
//    }

    public static Map<String, Object> doExecute(ConnectionImpl connection, String sql) throws SQLException, IOException {
        String token = connection.getToken();
        HostInfo origHostInfo = connection.getOrigHostInfo();
        String datasourceType = connection.getDatasourceType();
        String datasourceName = connection.getDatasourceName();
        StreamServiceGrpc.StreamServiceBlockingStub stub = connection.getStub();
        ExecuteRequest request = ExecuteRequest.newBuilder()
                .setDatasourceName(datasourceName)
                .setDatasourceType(datasourceType)
                .setSql(sql)
                .setToken(token == null ? "" : token)
                .build();
        Iterator<ExecuteResponse> executes;
        try {
            executes = stub.execute(request);
        } catch (Exception e) {
            throw new SQLException("数据转化出错:" + e.getMessage(), e);
        }
        // 目前只有一个返回值
        if (executes.hasNext()) {
            ExecuteResponse executeResponse = executes.next();
            if (executeResponse.getStatus() != 200) {
                throw new SQLException("数据转化出错:" + executeResponse.getError());
            }
            ByteString data = executeResponse.getData();
            byte[] byteArray = data.toByteArray();
            try {
                return JdkSerializableUtils.deserialize(byteArray);
            } catch (Exception e) {
                throw new SQLException("数据转化出错:" + e.getMessage(), e);
            }
        }
        throw new SQLException("查询出错，未获取到查询结果");
    }


//    private static Map<String, Object> analysisRes(byte[] bytes) throws IOException {
//        Map<String, Object> map = new HashMap<>();
//        ObjectMapper mapper = JsonUtils.getMapper();
//        JsonNode root = mapper.readTree(bytes);
//        map.put("status", getIntFromNode(root, "status"));
//        map.put("message", getStrFromNode(root, "message"));
//        map.put("desc", getStrFromNode(root, "desc"));
//        map.put("stackTrace", getStrFromNode(root, "stackTrace"));
//
//        Map<String, Object> mapV = new HashMap<>();
//
//        List<Map<String, Object>> metaV = new ArrayList<>();
//        JsonNode valueNode = root.get("value");
//        ArrayNode metaArrayNode = (ArrayNode) valueNode.get("metas");
//        String metaText = metaArrayNode.toString();
//        if (Objects.nonNull(metaText)) {
//            metaV = JsonUtils.fromJson(metaText, new TypeReference<List<Map<String, Object>>>() {
//            });
//        }
//        mapV.put("metas", metaV);
//
//        ArrayNode dataArrayNode = (ArrayNode) valueNode.get("data");
//        List<Map<String, Object>> dataList = new ArrayList<>();
//        for (int i = 0; i < dataArrayNode.size(); i++) {
//            JsonNode jsonNode = dataArrayNode.get(i);
//            Iterator<String> fieldIterator = jsonNode.fieldNames();
//            Map<String, Object> mapValue = new HashMap<>();
//            while (fieldIterator.hasNext()) {
//                String field = fieldIterator.next();
//                JsonNode vNode = jsonNode.get(field);
//                Object value = convertValue(vNode);
//                mapValue.put(field, value);
//            }
//            dataList.add(mapValue);
//        }
//        mapV.put("data", dataList);
//        map.put("value", mapV);
//
//        return map;
//    }

//    private static Object convertValue(JsonNode jsonNode) {
//        Object value = null;
//        if (jsonNode instanceof BigIntegerNode) {
//            BigIntegerNode bigIntegerNode = (BigIntegerNode) jsonNode;
//            String text = bigIntegerNode.asText();
//            if (!"".equals(text) && text != null) {
//                value = Long.parseLong(text);
//            }
//        } else if (jsonNode instanceof BooleanNode) {
//            BooleanNode boolNode = (BooleanNode) jsonNode;
//            String text = boolNode.asText();
//            if (!"".equals(text) && text != null) {
//                value = Boolean.parseBoolean(text);
//            }
//        } else if (jsonNode instanceof DecimalNode) {
//            DecimalNode decimalNode = (DecimalNode) jsonNode;
//            String text = decimalNode.asText();
//            if (!"".equals(text) && text != null) {
//                value = Double.parseDouble(text);
//            }
//        } else if (jsonNode instanceof FloatNode) {
//            FloatNode floatNode = (FloatNode) jsonNode;
//            String text = floatNode.asText();
//            if (!"".equals(text) && text != null) {
//                value = Float.parseFloat(text);
//            }
//        } else if (jsonNode instanceof DoubleNode) {
//            DoubleNode doubleNode = (DoubleNode) jsonNode;
//            String text = doubleNode.asText();
//            if (!"".equals(text) && text != null) {
//                value = Double.parseDouble(text);
//            }
//        } else if (jsonNode instanceof IntNode) {
//            IntNode intNode = (IntNode) jsonNode;
//            String text = intNode.asText();
//            if (!"".equals(text) && text != null) {
//                value = Integer.parseInt(text);
//            }
//        } else if (jsonNode instanceof LongNode) {
//            LongNode longNode = (LongNode) jsonNode;
//            String text = longNode.asText();
//            if (!"".equals(text) && text != null) {
//                value = Long.parseLong(text);
//            }
//        } else if (jsonNode instanceof NullNode) {
//            value = null;
//        } else if (jsonNode instanceof ObjectNode) {
//            ObjectNode objectNode = (ObjectNode) jsonNode;
//            value = objectNode.asText();
//        } else if (jsonNode instanceof ShortNode) {
//            ShortNode shortNode = (ShortNode) jsonNode;
//            String text = shortNode.asText();
//            if (!"".equals(text) && text != null) {
//                value = Short.parseShort(text);
//            }
//        } else if (jsonNode instanceof TextNode) {
//            TextNode textNode = (TextNode) jsonNode;
//            value = textNode.asText();
//        }
//        return value;
//    }

//    private static String getStrFromNode(JsonNode node, String key) {
//        JsonNode valueNode = node.get(key);
//        if (Objects.nonNull(valueNode)) {
//            return valueNode.asText();
//        }
//        return null;
//    }
//
//    private static Integer getIntFromNode(JsonNode node, String key) {
//        JsonNode valueNode = node.get(key);
//        if (Objects.nonNull(valueNode)) {
//            return valueNode.asInt();
//        }
//        return null;
//    }

}
