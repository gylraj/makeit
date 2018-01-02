package com.gylraj.makeit.Utils;

/**
 * Created by mac on 02/01/2018.
 */

public class Utils {
    public static String setColorAlpha(int percentage, String colorCode){
        double decValue = ((double)percentage / 100) * 255;
        String rawHexColor = colorCode.replace("#","");
        StringBuilder str = new StringBuilder(rawHexColor);

        if(Integer.toHexString((int)decValue).length() == 1)
            str.insert(0, "#0" + Integer.toHexString((int)decValue));
        else
            str.insert(0, "#" + Integer.toHexString((int)decValue));
        return str.toString();
    }
}
