package com.example.fajar.reisenote.activities;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.example.fajar.reisenote.R;
import com.example.fajar.reisenote.adapter.FilterAdapter;
import com.example.fajar.reisenote.adapter.SwipeableWithButtonExampleAdapter;
import com.example.fajar.reisenote.contentprovider.ReiseContract;
import com.example.fajar.reisenote.data.ReiseData;
import com.example.fajar.reisenote.fragments.SwipeableWithButtonFragment;
import com.example.fajar.reisenote.utils.Constant;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_FAV;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_LAST_UPDATED;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_STARRED;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.CONTENT_URI;
import static com.example.fajar.reisenote.fragments.SwipeableWithButtonFragment.REQUEST_ADD_CODE;

public class ReiseListActivity extends AppCompatActivity implements FilterAdapter.InteractionListener,
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    public static final int REISE_LOADER_ID = 10;

    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;

    @BindView(R.id.filter_apply_button)
    Button applyButton;

    @BindView(R.id.clear_filter_button)
    ImageView clearFilterButton;

    @BindView(R.id.fab_add)
    FloatingActionButton fab;

    HashMap<Integer, Boolean> hashMap = new HashMap<>();
    FilterAdapter filterAdapter;
    private String FRAGMENT_LIST_VIEW = "listview";
    private String TAG = ReiseListActivity.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        ButterKnife.bind(this);


        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sliding_pane_container, new SwipeableWithButtonFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
            //Drawer view

            getSupportLoaderManager().initLoader(REISE_LOADER_ID, null, this);
        }
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(ReiseListActivity.this));

        String[] filterNames = {"Hearted", "Favourites", "Happy Trip", "Sad Trip"};
        filterAdapter = new FilterAdapter(ReiseListActivity.this, filterNames);
        recyclerView.setAdapter(filterAdapter);
        applyButton.setOnClickListener(this);
        clearFilterButton.setOnClickListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReiseListActivity.this, ReiseEditActivity.class);
                startActivityForResult(intent, REQUEST_ADD_CODE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all notes
        getSupportLoaderManager().restartLoader(REISE_LOADER_ID, null, this);
    }


    public void onItemPinned(int position) {
    }


    public void onItemClicked(ReiseData reiseData, int position, TextView textView) {
        Intent intent = new Intent(ReiseListActivity.this, ReiseDetailActivity.class);
        intent.putExtra(Constant.reiseData, reiseData);
        intent.putExtra(Constant.reisePosition, position);

        ActivityOptionsCompat activityOptionsCompat = makeSceneTransitionAnimation(ReiseListActivity.this
                , textView
                , ViewCompat.getTransitionName(textView));
        startActivity(intent, activityOptionsCompat.toBundle());

    }

    public void onItemButtonClicked(ReiseData reiseData) {

    }

    @Override
    public void getDrawerListMap(HashMap<Integer, Boolean> hashMap) {
        this.hashMap = hashMap;

        boolean changeFilterColor = false;

        for (Map.Entry<Integer, Boolean> entry : hashMap.entrySet()) {
            if (entry.getValue())
                changeFilterColor = true;
        }
        if (changeFilterColor) {
            clearFilterButton.setImageDrawable(ContextCompat.getDrawable
                    (ReiseListActivity.this, R.drawable.ic_clear_filter));
        } else {
            clearFilterButton.setImageDrawable(ContextCompat.getDrawable
                    (ReiseListActivity.this, R.drawable.ic_clear));

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mCursor = null;

            @Override
            protected void onStartLoading() {
                if (mCursor != null)
                    deliverResult(mCursor);
                else
                    forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(CONTENT_URI,
                            null,
                            null,
                            null,
                            COLUMN_LAST_UPDATED + " DESC");

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                mCursor = data;
                super.deliverResult(data);
            }

        };


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        if (fragment instanceof SwipeableWithButtonFragment) {
            ((SwipeableWithButtonFragment) fragment).myItemAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        if (fragment instanceof SwipeableWithButtonFragment) {
            ((SwipeableWithButtonFragment) fragment).myItemAdapter.swapCursor(null);
        }
    }

    @Override
    public void onClick(View v) {

        SwipeableWithButtonExampleAdapter swipeableWithButtonExampleAdapter = null;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        if (fragment instanceof SwipeableWithButtonFragment) {
            swipeableWithButtonExampleAdapter = ((SwipeableWithButtonFragment) fragment).myItemAdapter;
        }

        if (swipeableWithButtonExampleAdapter == null)
            return;


        switch (v.getId()) {


            case R.id.filter_apply_button:
                if (hashMap != null) {


                    if (hashMap.get(0) && hashMap.get(1)) {
                        Uri uri = CONTENT_URI.buildUpon().appendPath(ReiseContract.FAV_AND_STAR).build();
                        Cursor cursor = getContentResolver().query(uri, null, COLUMN_FAV + "=? and " + COLUMN_STARRED + "=?", new String[]{"1", "1"}, COLUMN_LAST_UPDATED + " DESC");
                        if (cursor != null) {
                            swipeableWithButtonExampleAdapter.swapCursor(cursor);
                        }
                    } else if (hashMap.get(0)) {
                        Uri uri = CONTENT_URI.buildUpon().appendPath(ReiseContract.FAV).build();
                        Cursor cursor = getContentResolver().query(uri, null, COLUMN_FAV + "=?", new String[]{"1"}, COLUMN_LAST_UPDATED + " DESC");
                        if (cursor != null) {
                            swipeableWithButtonExampleAdapter.swapCursor(cursor);
                        }
                    } else if (hashMap.get(1)) {
                        Uri uri = CONTENT_URI.buildUpon().appendPath(ReiseContract.STAR).build();
                        Cursor cursor = getContentResolver().query(uri, null, COLUMN_STARRED + "=?", new String[]{"1"}, COLUMN_LAST_UPDATED + " DESC");
                        if (cursor != null) {
                            swipeableWithButtonExampleAdapter.swapCursor(cursor);
                        }
                    } else {
                        Uri uri = CONTENT_URI.buildUpon().appendPath(ReiseContract.FAV_AND_STAR).build();
                        Cursor cursor = getContentResolver().query(uri, null, COLUMN_FAV + "=? and " + COLUMN_STARRED + "=?", new String[]{"0", "0"}, COLUMN_LAST_UPDATED + " DESC");
                        if (cursor != null) {
                            swipeableWithButtonExampleAdapter.swapCursor(cursor);
                        }
                    }
                }

                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }

                break;

            case R.id.clear_filter_button:

                Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, COLUMN_LAST_UPDATED + " DESC");
                if (cursor != null) {
                    swipeableWithButtonExampleAdapter.swapCursor(cursor);
                }
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }

                break;
        }

    }


}
