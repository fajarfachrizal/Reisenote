package com.example.fajar.reisenote.activities;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.fajar.reisenote.R;
import com.example.fajar.reisenote.ReiseWidgetProvider;
import com.example.fajar.reisenote.data.ReiseData;
import com.example.fajar.reisenote.service.WidgetUpdateService;
import com.example.fajar.reisenote.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReiseDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FOR_EDIT_SCREEN = 105;

    @BindView(R.id.reise_detail_title)
    TextView reiseTitle;

    @BindView(R.id.reise_detail_desc)
    TextView reiseDesc;

    @BindView(R.id.reise_detail_last_updated)
    TextView reiseLastUpdated;

    @BindView(R.id.add_widget)
    TextView reiseAddWidget;

    @BindView(R.id.edit_textview)
    TextView reiseEditTextView;

    @BindView(R.id.text_retrive_detail)
    TextView retriveTextView;

    @BindView(R.id.back_button)
    ImageView backButton;


    ReiseData reiseData;
    private int reisePosition;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_reise_detail);

        ButterKnife.bind(this);

        initAction();
    }

    private void initAction() {
        if (getIntent().getExtras() == null)
            return;
        reiseData = (ReiseData) getIntent().getExtras().get(Constant.reiseData);
        reisePosition = getIntent().getExtras().getInt(Constant.reisePosition);

        if (reiseData == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reiseTitle.setTransitionName(reisePosition + "");
        }
        reiseTitle.setText(reiseData.getReiseTitle());
        reiseDesc.setText(reiseData.getReiseDesc());
        retriveTextView.setText(reiseData.getPathImg());

        reiseLastUpdated.setText(String.format("Last updated: %s", reiseData.getReiseLastUpdate()));

        reiseEditTextView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReiseDetailActivity.this, ReiseEditActivity.class);
                intent.putExtra(Constant.reiseData, reiseData);
                intent.putExtra(Constant.isEditReise, true);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ReiseDetailActivity.this,
                        reiseTitle,
                        "simple_title_animation"
                );
                startActivityForResult(intent, REQUEST_CODE_FOR_EDIT_SCREEN, activityOptionsCompat.toBundle());
            }
        });

        reiseAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ReiseDetailActivity.this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
                        ReiseDetailActivity.this, ReiseWidgetProvider.class
                ));

                if (appWidgetIds.length == 0) {
                    Toast.makeText(ReiseDetailActivity.this, R.string.please_make_widget_first, Toast.LENGTH_SHORT).show();
                } else {

                    WidgetUpdateService.startActionReiseWidget(ReiseDetailActivity.this,
                            reiseData, reisePosition);

                    Toast.makeText(ReiseDetailActivity.this, R.string.diary_added, Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_EDIT_SCREEN && resultCode == RESULT_OK) {
            reiseData = (ReiseData) data.getExtras().get(Constant.reiseData);
            retriveTextView.setText(reiseData.getPathImg());
            reiseTitle.setText(reiseData.getReiseTitle());
            reiseDesc.setText(reiseData.getReiseDesc());
            reiseLastUpdated.setText(String.format("Last updated: %s", reiseData.getReiseLastUpdate()));

        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
}
