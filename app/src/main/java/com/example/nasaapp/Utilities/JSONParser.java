package com.example.nasaapp.Utilities;

import android.util.Log;

import com.example.nasaapp.RoverItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    /**
     * Return a list of rover items
     *
     * @param result
     * @return
     */
    public static List<RoverItem> getRoverItemList(String result)
    {
        List<RoverItem> roverItemList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArrayPhotos =  jsonObject.getJSONArray("photos");

            // For each roverItem in list do...
            for (int roverItemCount = 0; roverItemCount < jsonArrayPhotos.length(); roverItemCount++)
            {
                RoverItem roverItem = new RoverItem();

                JSONObject jsonPhotoObject = jsonArrayPhotos.getJSONObject(roverItemCount);

                // Regex string to replace http with https
                String imageSource = "https://" + jsonPhotoObject.getString("img_src").replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");

                roverItem.setRoverName(jsonPhotoObject.getJSONObject("rover").getString("name"));
                roverItem.setEarthDate(jsonPhotoObject.getString("earth_date"));
                roverItem.setFullCameraName(jsonPhotoObject.getJSONObject("camera").getString("full_name"));
                roverItem.setImageID(jsonPhotoObject.getString("id"));
                roverItem.setSolDay(jsonPhotoObject.getString("sol"));
                roverItem.setImageSource(imageSource);

                roverItemList.add(roverItem);
            }

            // Loop through JSON array and build object
            return roverItemList;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Return the total images of the rover
     *
     * @param result
     * @return
     */
    public static String getRoverTotalImages(String result)
    {
        try {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getJSONObject("photo_manifest").getString("total_photos");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

}
