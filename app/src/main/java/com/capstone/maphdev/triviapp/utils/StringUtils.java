package com.capstone.maphdev.triviapp.utils;


import android.util.Base64;

public class StringUtils {
    public static String base64ToUTF8(String string){
        byte[] tmp = Base64.decode(string, Base64.DEFAULT);
        String result = "";
        try{
            result = new String(tmp, "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
