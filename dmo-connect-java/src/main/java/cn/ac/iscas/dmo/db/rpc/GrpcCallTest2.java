package cn.ac.iscas.dmo.db.rpc;

import cn.ac.iscas.dmo.connector.util.JdkSerializableUtils;
import cn.ac.iscas.dmo.db.rpc.execute.ExecuteRequest;
import cn.ac.iscas.dmo.db.rpc.execute.ExecuteResponse;
import cn.ac.iscas.dmo.db.rpc.execute.StreamServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/30 11:09
 */

public class GrpcCallTest2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        testCall1();
    }


    /**
     * 测试使用grpc发布服务，服务端流式
     */
    public static void testCall1() throws IOException, InterruptedException {
        ExecuteRequest request = ExecuteRequest.newBuilder()
                .setDatasourceName("达梦-055")
                .setDatasourceType("dameng8")
                .setSql("SELECT 1")
                .build();
        //客户端调用
//        ManagedChannel managedChannel = GrpcClientUtils.getManagedChannel("127.0.0.1", 8888);
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("127.0.0.1", 8888)
                .usePlaintext().build();
        StreamServiceGrpc.StreamServiceBlockingStub stub = StreamServiceGrpc.newBlockingStub(managedChannel);
        for (int i = 0; i < 10; i++) {
            Iterator<ExecuteResponse> executes = stub.execute(request);
            while (executes.hasNext()) {
                ExecuteResponse response = executes.next();
                ByteString data = response.getData();
                byte[] byteArray = data.toByteArray();
                try {
                    Serializable deserialize = JdkSerializableUtils.deserialize(byteArray);
                    System.out.println(deserialize);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            TimeUnit.SECONDS.sleep(1);
        }

    }
}
