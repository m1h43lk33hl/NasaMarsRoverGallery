package com.example.nasaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.nasaapp.Utilities.LanguageUtils;
import com.example.nasaapp.Utilities.RoverNames;

import java.util.ArrayList;
import java.util.List;

// TODO
// DONE: Implicit intent
// LoaderManager

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private NasaRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBarLoader;

    private List<RoverItem> roverItemList = new ArrayList<>();
    private String langPreferenceString;
    private String roverPreferenceString;

    private String ROVER_LIST_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initiate recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.main_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Set recyclerView divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Initiate progressbar
        this.mProgressBarLoader = findViewById(R.id.progress_bar_loader);

        // Initiate adapter
        mAdapter = new NasaRecyclerViewAdapter(this, roverItemList);
        mRecyclerView.setAdapter(mAdapter);

        // Setup shared preferences
        setupSharedPreferences();

        // Load the recyclerView
        loadRecyclerView(savedInstanceState);
    }

    /**
     * Main method to load the recycler view, either from saved state, or make a new call.
     */
    private void loadRecyclerView(Bundle savedInstanceState)
    {
        if(savedInstanceState != null && savedInstanceState.containsKey(ROVER_LIST_KEY))
        {
            roverItemList = savedInstanceState.getParcelableArrayList(ROVER_LIST_KEY);
            populateRecyclerView();
        }
        else
        {
            // Execute downloadTask to get results from API, with preference as it's the first execution.
            new DownloadTask(this, roverPreferenceString, mProgressBarLoader).execute();
        }
    }

    /**
     * Populates the recyclerView with items
     */
    public void populateRecyclerView()
    {
        mAdapter.setRoverItemList(roverItemList);

        // Logic for the listener
        mAdapter.setOnItemClickListener(new NasaRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RoverItem roverItem) {

                // Get class reference
                Class detailActivity = DetailActivity.class;

                // Create intent
                Intent startDetailActivity = new Intent(MainActivity.this, detailActivity);

                // Add object to intent
                startDetailActivity.putExtra("roverItemList", (Parcelable) roverItem);

                // Start activity
                startActivity(startDetailActivity);
            }
        });
    }

    /**
     * Set roverItemsList, this is needed to provide access to the async download task
     *
     * @param roverItemList
     */
    public void setRoverItemList(List<RoverItem> roverItemList)
    {
        this.roverItemList = roverItemList;
    }

    /**
     * Setup shared preferences
     */
    private void setupSharedPreferences()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // Rover settings
        roverPreferenceString = sharedPreferences.getString(getString(R.string.pref_rover_list_key), getString(R.string.rover_name_curiosity)).toLowerCase();

        // Language settings
        if(sharedPreferences.getBoolean(getString(R.string.pref_lang_check_key), true))
        {
            // Use device settings
            LanguageUtils.setLanguage(Resources.getSystem().getConfiguration().locale.getLanguage(), this.getBaseContext());
            langPreferenceString = Resources.getSystem().getConfiguration().locale.getLanguage();
        }
        else
        {
            // Use preference settings
            LanguageUtils.setLanguage(sharedPreferences.getString(getString(R.string.pref_lang_list_key), getString(R.string.pref_lang_label_en)), this.getBaseContext());
            langPreferenceString = sharedPreferences.getString(getString(R.string.pref_lang_list_key), getString(R.string.pref_lang_label_en));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_lang_list_key)) || key.equals(getString(R.string.pref_lang_check_key))) {

            String previousLangString = langPreferenceString;

            // Language settings
            if (sharedPreferences.getBoolean(getString(R.string.pref_lang_check_key), true)) {
                // Use device settings
                LanguageUtils.setLanguage(Resources.getSystem().getConfiguration().locale.getLanguage(), this.getBaseContext());
                langPreferenceString = Resources.getSystem().getConfiguration().locale.getLanguage();


            } else {
                // Use preference settings
                LanguageUtils.setLanguage(sharedPreferences.getString(getString(R.string.pref_lang_list_key), getString(R.string.pref_lang_label_en)), this.getBaseContext());
                langPreferenceString = sharedPreferences.getString(getString(R.string.pref_lang_list_key), getString(R.string.pref_lang_label_en));

            }

            if(!previousLangString.equals(langPreferenceString))
                // Recreate to refresh window
                recreate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Get selected menu item
        int id = item.getItemId();

        switch (id)
        {
            case R.id.drop_down_curiosity:
                new DownloadTask(this, RoverNames.MARS_ROVER_CURIOSITY, mProgressBarLoader).execute();
                break;

            case R.id.drop_down_opportunity:
                new DownloadTask(this, RoverNames.MARS_ROVER_OPPORTUNITY, mProgressBarLoader).execute();
                break;

            case R.id.drop_down_spirit:
                new DownloadTask(this, RoverNames.MARS_ROVER_SPIRIT, mProgressBarLoader).execute();
                break;

            case R.id.menu_settings_item:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save instance of roverItemList and cast to parcelable arrayList
        outState.putParcelableArrayList(ROVER_LIST_KEY, (ArrayList<? extends Parcelable>) this.roverItemList);
    }
}
