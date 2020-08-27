package com.cs.common.util;

import com.cs.common.zk.ServerInfo;

public class NRpcServiceUtil {

    public static String getServiceName(Class<?> interfaceClass, String version) {
//        if (version != null && version.equals("")) {
//            return interfaceClass.getName() + "." + version;
//        }
        return interfaceClass.getName();
    }
}
