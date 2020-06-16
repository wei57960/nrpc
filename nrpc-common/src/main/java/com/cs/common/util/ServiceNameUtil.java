package com.cs.common.util;

public class ServiceNameUtil {

    public static String getServiceName(Class<?> interfaceClass, String version) {
//        if (version != null && version.equals("")) {
//            return interfaceClass.getName() + "." + version;
//        }
        return interfaceClass.getName();

    }
}
