package com.example.nasaapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private ImageView mDetailImageView;
    private TextView mRoverNameTextView;
    private TextView mSolDayTextView;
    private TextView mEarthDateTextView;
    private TextView mFullCameraName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get roverItem back
        Intent intent = getIntent();
        RoverItem roverItem = (RoverItem) intent.getParcelableExtra("roverItemList");

        // Initiate components
        mDetailImageView = findViewById(R.id.detail_iv);
        mRoverNameTextView = findViewById(R.id.tv_rover_name_var);
        mSolDayTextView = findViewById(R.id.tv_sol_day_var);
        mEarthDateTextView = findViewById(R.id.tv_earth_date_var);
        mFullCameraName = findViewById(R.id.tv_full_camera_name_var);

        // Set components
        setComponents(roverItem);
    }

    /**
     * Set visual components of the screen
     *
     * @param roverItem
     */
    private void setComponents(final RoverItem roverItem)
    {
        // Load Image with picasso
        Picasso.with(DetailActivity.this).load(roverItem.getImageSource())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mDetailImageView);

        // Add onclickListener for implicit intent
        mDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareItem(roverItem.getImageSource());
            }
        });

        // Check if orientation is landscape mode
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            mRoverNameTextView.setText(roverItem.getRoverName());
            mSolDayTextView.setText(roverItem.getSolDay());
            mEarthDateTextView.setText(roverItem.getEarthDate());
            mFullCameraName.setText(roverItem.getFullCameraName());
        }
        else
        {
            mFullCameraName.setText(roverItem.getFullCameraName());
        }
    }

    /**
     * Share image using implicit intent via Picasso
     *
     * @param url
     */
    public void shareItem(String url) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent intent = new Intent(Intent.ACTION_SEND);


                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));

                // Start activity
                startActivity(Intent.createChooser(intent, "Share Image"));
            }

            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    /**
     * Creates bmp file and returns local uri
     *
     * @param bmp
     * @return
     */
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;

        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
