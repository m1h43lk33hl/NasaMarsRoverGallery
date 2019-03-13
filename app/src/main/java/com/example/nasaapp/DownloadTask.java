package com.example.nasaapp;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nasaapp.Utilities.JSONParser;
import com.example.nasaapp.Utilities.NetworkUtils;

import java.io.IOException;
import java.util.List;

public class DownloadTask extends AsyncTask<Void, Void, Boolean> {

    private MainActivity mainActivity;
    private List<RoverItem> roverItemList;
    private String roverName;
    private String roverPhotoCount;
    private ProgressBar mProgressBarLoader;


    public DownloadTask(MainActivity mainActivity, String roverName, ProgressBar progressBarLoader)
    {
        this.mainActivity = mainActivity;
        this.roverName = roverName;
        this.mProgressBarLoader = progressBarLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Set progressbar visible
        mProgressBarLoader.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try{

            // Return false if either one of the downloads failed
            if(NetworkUtils.buildManifestUrl(roverName)== null || NetworkUtils.buildRoverUrl(roverName) == null)
                return false;

            // Set photo count for rover
            this.roverPhotoCount = JSONParser.getRoverTotalImages(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildManifestUrl(roverName)));

            // Get response from API, then parse response
            this.roverItemList = JSONParser.getRoverItemList(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildRoverUrl(roverName)));

            // Set the list in MainActivity
            mainActivity.setRoverItemList(this.roverItemList);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        // Result succeeded
        if (result) {
            // Populate the recycleView with results
            mainActivity.populateRecyclerView();

            // Show success toast message
            Toast.makeText(this.mainActivity,mainActivity.getBaseContext().getString(R.string.toast_download_succes) + this.roverItemList.size() + "/" + this.roverPhotoCount, Toast.LENGTH_SHORT).show();
        } else
        {
            // Show fail toast message
            Toast.makeText(this.mainActivity, mainActivity.getBaseContext().getString(R.string.toast_download_fail), Toast.LENGTH_SHORT).show();
        }

        // Set progressbar invisible
        mProgressBarLoader.setVisibility(View.INVISIBLE);

    }
}
