package cn.ac.iscas.dmo.api.sdk.util;

import cn.ac.iscas.dmo.api.sdk.exception.DmoApiSdkException;
import cn.ac.iscas.dmo.api.sdk.model.DataServiceAuthenticationType;
import cn.ac.iscas.dmo.api.sdk.service.impl.DmoApiImpl;

import java.util.Objects;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/10/29 15:45
 */

public class CheckUtils {
    private CheckUtils() {
    }

    public static void checkBlank(String str, String message) throws DmoApiSdkException {
        if (str == null || str.isEmpty()) {
            throw new DmoApiSdkException(message);
        }
    }

    public static void checkNone(Object obj, String message) throws DmoApiSdkException {
        if (obj == null) {
            throw new DmoApiSdkException(message);
        }
    }

    public static void checkAuthorizationType(DataServiceAuthenticationType authenticationType,
                                              DmoApiImpl api) throws DmoApiSdkException {
//        if (authenticationType == DataServiceAuthenticationType.SIMPLE && Objects.isNull(api.getToken())) {
//            throw new DmoApiSdkException("鉴权方式为[普通模式],但token未设置，可在DmoApiImpl类的构造函数中设置token");
//        } else
            if (authenticationType == DataServiceAuthenticationType.SIGN &&
                (Objects.isNull(api.getAppId()) || Objects.isNull(api.getAppSecret()))) {
            throw new DmoApiSdkException("鉴权方式为[签名模式],但appId或appSecret为空，可在DmoApiImpl的构造函数中设置");
        }

    }

}
