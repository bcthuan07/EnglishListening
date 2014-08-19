package com.example.dream.englishlistening.util;

/**
 * Created by bcthuan07 on 8/9/2014.
 */
public final class Util {
    public static final String convertTime(int miliseconds) {
        int seconds = (miliseconds / 1000) % 60;
        int minutes = (miliseconds / 1000) / 60;
        String retVal = minutes < 10 ? ("0" + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds))
                : (+minutes + ":" + (seconds < 10 ? "0" + seconds : seconds));
        return retVal;
    }


}
