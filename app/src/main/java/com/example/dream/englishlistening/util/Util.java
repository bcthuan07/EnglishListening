package com.example.dream.englishlistening.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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

    public static final boolean downloadFile(String link, String uri) {
        try {
            File file = new File(uri);
            HttpRequest request = HttpRequest.get(link);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            request.receive(out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
