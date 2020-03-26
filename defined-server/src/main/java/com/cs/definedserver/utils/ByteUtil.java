package com.cs.definedserver.utils;

/**
 * @Author wei
 * @Time 2020/3/21
 * @Description
 */
public class ByteUtil {

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
     * 获取 byte 数组 16 进制字符串
     *
     * @param data byte 数组
     * @return
     */
    public static String getByteArrayString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
            r.append(" ,");
        }
        r.deleteCharAt(r.length() - 1);
        return r.toString();
    }

}
