package com.teremok.influence.util;

/**
 * Created by Alexx on 19.12.13.
 */
public class SizeOfUtil {

    public static String measure(final Object object) {

        long size = 0;
        return  new String(object.getClass().getSimpleName() + " size = " + size + " bytes");
    }
}


