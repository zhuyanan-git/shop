package com.qfant.utils;

import java.util.UUID;

/**
 * 生成UUID字符串
 * @author luxiaolu
 *
 */
public final class UUIDUtils {
    private UUIDUtils() {

    }

    /**
     * 生成32位UUID
     * 即：不包含字符-的UUID
     * @return
     * @param 
     * @return String
     */
    public static String getUUID32Bits() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成36位UUID
     * 即：包含字符-的UUID
     * @return
     * @param 
     * @return String
     */
    public static String getUUID36Bits() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成指定数目的32位uuid
     * @param count 
     * @return String[]
     */
    public static String[] getUUID32Bits(Integer count) {
        return getUUID(count, 32);
    }

    /**
     * 生成指定数目的36位uuid
     * @param count 指定数目
     * @return String[]
     */
    public static String[] getUUID36Bits(Integer count) {
        return getUUID(count, 36);
    }

    /**
     * 生成指定数目的指定位数的uuid
     * @param count 指定数目
     * @param bits
     * @return String[]
     */
    private static String[] getUUID(Integer count, int bits) {
        String[] uuids = new String[count];
        for (int i = 0; i < count; i++) {
            if (bits == 32) {
                uuids[i] = getUUID32Bits();
            } else if (bits == 36) {
                uuids[i] = getUUID36Bits();
            }
        }
        return uuids;
    }

}
