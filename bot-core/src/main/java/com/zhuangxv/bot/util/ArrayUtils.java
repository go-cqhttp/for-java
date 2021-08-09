package com.zhuangxv.bot.util;

/**
 * @author xiaoxu
 * @since 2021/8/9 4:45 下午
 */
public class ArrayUtils {

    public static boolean contain(long[] sources, long value) {
        for (long source : sources) {
            if (source == value) {
                return true;
            }
        }
        return false;
    }

    public static boolean contain(Long[] sources, Long value) {
        for (Long source : sources) {
            if (source.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contain(Object[] sources, Object value) {
        for (Object source : sources) {
            if (source == value) {
                return true;
            }
        }
        return false;
    }

}
