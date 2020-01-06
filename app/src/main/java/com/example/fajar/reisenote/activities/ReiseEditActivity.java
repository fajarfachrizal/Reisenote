package com.example.fajar.reisenote.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.fajar.reisenote.BuildConfig;
import com.example.fajar.reisenote.R;
import com.example.fajar.reisenote.adapter.SwipeableWithButtonExampleAdapter;
import com.example.fajar.reisenote.contentprovider.ReiseContract;
import com.example.fajar.reisenote.data.ReiseData;
import com.example.fajar.reisenote.utils.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.werdpressed.partisan.rundo.RunDo;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_DESC;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_FAV;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_IMAGE;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_LAST_UPDATED;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_POEM;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_STARRED;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_STORY;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_TITLE;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.CONTENT_URI;

public class ReiseEditActivity extends AppCompatActivity implements RunDo.TextLink {

    private final static String USER_INPUTTED_TITLE = "USER_INPUTTED_TITLE";
    private final static String USER_INPUTTED_DESC = "USER_INPUTTED_DESC";
    private final static String LOG_TAG_UI = "LOG_TAG_UI";
    @BindView(R.id.reise_add_description)
    EditText reiseDescEditText;
    @BindView(R.id.reise_add_title)
    EditText reiseTitleEditText;
    @BindView(R.id.tv_retrive)
    TextView tvRetrive;
    @BindView(R.id.btn_select_activity)
    TextView btn_select;
    @BindView(R.id.image_load2)
    ImageView imgRetrive;
    boolean isEditReise = false;
    ReiseData reiseData;
    private RunDo runDo;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_reise_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("");
        ButterKnife.bind(this);

        runDo = RunDo.Factory.getInstance(getSupportFragmentManager());
        runDo.setQueueSize(200);
        runDo.setTimerLength(200);

        if (getIntent().getExtras() == null)
            return;
        reiseData = (ReiseData) getIntent().getExtras().get(Constant.reiseData);

        if (reiseData != null) {
            reiseTitleEditText.setText(reiseData.getReiseTitle());
            reiseDescEditText.setText(reiseData.getReiseDesc());
            tvRetrive.setText(reiseData.getPathImg());
            btn_select.setVisibility(View.GONE);
            tvRetrive.setVisibility(View.GONE);

        }
        tvRetrive.setVisibility(View.GONE);

        if (getIntent().getExtras().getBoolean(Constant.isEditReise)) {
            isEditReise = true;
        }

        MobileAds.initialize(this, BuildConfig.ADDMOB_APP_ID_STRING);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4198204502697083/7548757461"); //test unit
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        if (savedInstanceState != null) {
            Log.d(LOG_TAG_UI, "Main activity onCreate savedInstanceState is not null.");
            // Retrieve activity instance state data.
            String email = savedInstanceState.getString(USER_INPUTTED_TITLE);
            // Set the original email data in EditText view component.
            reiseTitleEditText.setText(email);
        } else {
            Log.d(LOG_TAG_UI, "Main activity onCreate savedInstanceState is null.");
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String title = reiseTitleEditText.getText().toString();
        outState.putString(USER_INPUTTED_TITLE, title);

        String desc = reiseDescEditText.getText().toString();
        outState.putString(USER_INPUTTED_DESC, desc);
        Log.d(LOG_TAG_UI, "input onSaveInstanceState.");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String title = savedInstanceState.getString(USER_INPUTTED_TITLE);
        String desc = savedInstanceState.getString(USER_INPUTTED_DESC);
        Log.d(LOG_TAG_UI, "Main activity onRestoreInstanceState.");
    }


    public void selectActivity(View view) {

        Intent intent = new Intent(ReiseEditActivity.this, UploadImage.class);
        ReiseEditActivity.this.startActivity(intent);
        reiseTitleEditText.setEnabled(true);
        reiseDescEditText.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_reise, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String value = null;
        Intent intent = getIntent();
        value = intent.getStringExtra("url_image");
        tvRetrive.setText(value);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.undo_reise:
                if (reiseTitleEditText.isFocused()) {
                    Toast.makeText(this, R.string.undo_unavailable, Toast.LENGTH_SHORT).show();
                    break;
                }
                runDo.undo();
                break;
            case R.id.save_reise:

                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                String title = reiseTitleEditText.getText().toString();
                String desc = reiseDescEditText.getText().toString();
                String path = tvRetrive.getText().toString();
                //cant be empty

                if (title.isEmpty()) {
                    Toast.makeText(this, R.string.those_cant_empty, Toast.LENGTH_SHORT).show();
                    break;
                }

                ContentValues contentValues = new ContentValues();

                if (isEditReise) {
                    if (reiseData != null) {
                        String value2 = reiseData.getPathImg();

                        contentValues.put(COLUMN_TITLE, title);
                        contentValues.put(COLUMN_DESC, desc);
                        contentValues.put(COLUMN_LAST_UPDATED, new Date().getTime()); //TODO change to int
                        contentValues.put(COLUMN_FAV, reiseData.getIsFav());
                        contentValues.put(COLUMN_STARRED, reiseData.getIsStarred());
                        contentValues.put(COLUMN_STORY, reiseData.getIsStory());
                        contentValues.put(COLUMN_POEM, reiseData.getIsPoem());
                        contentValues.put(ReiseContract.ReiseEntry._ID, reiseData.getId());

                        Uri uri = CONTENT_URI.buildUpon().appendPath(reiseData.getId() + "").build();

                        int reiseUpdated = getContentResolver().update(uri, contentValues, null, null);

                        if (reiseUpdated > 0) {
                            reiseData.setReiseTitle(title);
                            reiseData.setReiseDesc(desc);
                            reiseData.setReiseLastUpdate(SwipeableWithButtonExampleAdapter
                                    .getDateStringfromMilliseconds(new Date().getTime()));

                            setResult(RESULT_OK, new Intent().putExtra(Constant.reiseData, reiseData));

                            Toast.makeText(this, reiseUpdated + R.string.reise_updated, Toast.LENGTH_SHORT).show();

                        } else {
                            setResult(RESULT_CANCELED);
                            Toast.makeText(this, R.string.unable_update_diary, Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {

                    contentValues.put(COLUMN_TITLE, title);
                    contentValues.put(COLUMN_DESC, desc);
                    contentValues.put(COLUMN_LAST_UPDATED, new Date().getTime());
                    contentValues.put(COLUMN_IMAGE, value);
                    Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);
                    if (uri != null)
                        Toast.makeText(this, R.string.saved_successfully, Toast.LENGTH_SHORT).show();
                    tvRetrive.setVisibility(View.GONE);
                    setResult(RESULT_OK);
                    finish();
                }
                finish();
                break;

        }

        return true;
    }


    @Override
    public EditText getEditTextForRunDo() {
//      if (reiseTitleEditText.isFocused()){
//          return reiseTitleEditText;
//      }else {
        return reiseDescEditText;
//      }

    }


}

