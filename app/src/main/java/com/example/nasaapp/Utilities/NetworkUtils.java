package com.example.nasaapp.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String NASA_API_KEY = "uv5c0ib7TLHcZ6GfqcrA3TgM5QvM0tnuAZU9pTIe";

    private final static String NASA_BASE_URL = "https://api.nasa.gov/mars-photos/api/v1";
    private final static String NASA_URL_PART_ROVERS = "rovers";
    private final static  String NASA_URL_PART_PHOTOS = "photos";
    private final static String NASA_PARAM_NAME_SOL="sol";
    private final static String NASA_PARAM_NAME_API="api_key";
    private final static  String NASA_URL_PART_MANIFESTS = "manifests";


    /**
     * Build manifest url
     *
     * @param roverName
     * @return
     */
    public static URL buildManifestUrl(String roverName)
    {
        Uri builtUri = Uri.parse(NASA_BASE_URL).buildUpon()
                .appendPath(NASA_URL_PART_MANIFESTS)
                .appendPath(roverName)
                .appendQueryParameter(NASA_PARAM_NAME_API, NASA_API_KEY)
                .build();

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds rover Item url
     *
     * @param roverName
     * @return
     */
    public static URL buildRoverUrl(String roverName)
    {
        Uri builtUri = Uri.parse(NASA_BASE_URL).buildUpon()
                .appendPath(NASA_URL_PART_ROVERS)
                .appendPath(roverName)
                .appendPath(NASA_URL_PART_PHOTOS)
                .appendQueryParameter(NASA_PARAM_NAME_SOL, "1000")
                .appendQueryParameter(NASA_PARAM_NAME_API, NASA_API_KEY)
                .build();

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Returns response from URL
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

        try
        {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }



}
