package com.capstone.maphdev.triviapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    // BASE
    private final static String BASE_URL = "https://opentdb.com/api.php";

    // ENDPOINT
    private final static String AMOUNT_QUERY = "amount";
    private final static String CATEGORY_QUERY = "category";
    private final static String TYPE_QUERY = "type";
    private final static String TYPE_PARAM = "multiple";
    private final static String ENCODING_QUERY = "encode";
    private final static String ENCODING_PARAM = "base64";

    // get URL for "i" number of random questions
    public static URL buildUrlGeneralQuestions(int i) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(AMOUNT_QUERY, Integer.toString(i))
                .appendQueryParameter(TYPE_QUERY, TYPE_PARAM)
                .appendQueryParameter(ENCODING_QUERY, ENCODING_PARAM)
                .build();

        return getURLfromUri(builtUri);
    }

    // get URL for "amount" number of questions belonging to "category" id
    public static URL buildUrlbyCategoryAndAmount(int category, int amount){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(AMOUNT_QUERY, Integer.toString(amount))
                .appendQueryParameter(CATEGORY_QUERY, Integer.toString(category))
                .appendQueryParameter(TYPE_QUERY, TYPE_PARAM)
                .appendQueryParameter(ENCODING_QUERY, ENCODING_PARAM)
                .build();

        return getURLfromUri(builtUri);
    }

    // helper
    private static URL getURLfromUri(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // get URL response
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner sc = new Scanner(in);
            sc.useDelimiter("\\A");

            boolean hasInput = sc.hasNext();
            if (hasInput) {
                return sc.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // check if the Network is available
    public static boolean isNetworkAvailable(Context context){
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo networkInfo = cm.getNetworkInfo(networkType);
                if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
