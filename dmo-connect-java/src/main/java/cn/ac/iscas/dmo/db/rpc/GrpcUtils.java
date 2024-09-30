package cn.ac.iscas.dmo.db.rpc;

import cn.ac.iscas.dmo.db.rpc.execute.StreamServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/30 13:48
 */

public class GrpcUtils {
    private static volatile ManagedChannel channel;
    private static volatile StreamServiceGrpc.StreamServiceBlockingStub stub;


    private GrpcUtils() {
    }

    public static ManagedChannel getManageChannel(String host, int port) {
        if (channel == null) {
            synchronized (GrpcUtils.class) {
                if (channel == null) {
                    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                }
            }
        }
        return channel;
    }

    public static StreamServiceGrpc.StreamServiceBlockingStub getStub(String host, int port) {
        ManagedChannel manageChannel = getManageChannel(host, port);
        if (stub == null) {
            synchronized (GrpcUtils.class) {
                if (stub == null) {
                    stub = StreamServiceGrpc.newBlockingStub(manageChannel);
                }
            }
        }
        return stub;
    }
}
