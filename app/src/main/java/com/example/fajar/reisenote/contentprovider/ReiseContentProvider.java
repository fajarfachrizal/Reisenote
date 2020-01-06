package com.example.fajar.reisenote.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.CONTENT_URI;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.TABLE_NAME;

public class ReiseContentProvider extends ContentProvider {

    public static final int REISE = 100;
    public static final int REISE_WITH_ID = 101;
    public static final int FAV_REISE = 102;
    public static final int STAR_REISE = 103;
    public static final int FAV_AND_STAR_REISE = 104;
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private ReiseDbHelper reiseDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ReiseContract.AUTHORITY, ReiseContract.PATH_REISE, REISE);
        uriMatcher.addURI(ReiseContract.AUTHORITY, ReiseContract.PATH_REISE + "/#", REISE_WITH_ID);
        uriMatcher.addURI(ReiseContract.AUTHORITY, ReiseContract.PATH_REISE + "/" + ReiseContract.FAV, FAV_REISE);
        uriMatcher.addURI(ReiseContract.AUTHORITY, ReiseContract.PATH_REISE + "/" + ReiseContract.STAR, STAR_REISE);
        uriMatcher.addURI(ReiseContract.AUTHORITY, ReiseContract.PATH_REISE + "/" + ReiseContract.FAV_AND_STAR, FAV_AND_STAR_REISE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        reiseDbHelper = new ReiseDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase sqLiteDatabase = reiseDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case REISE:
                retCursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAV_REISE:
                retCursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case STAR_REISE:
                retCursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAV_AND_STAR_REISE:
                retCursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);


        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = reiseDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri retUri;

        switch (match) {
            case REISE:
                long id = 0;
                try {

                    id = sqLiteDatabase.insert(TABLE_NAME, null, values);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (id > 0) {
                    retUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = reiseDbHelper.getWritableDatabase();

        int reiseDeleted;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case REISE_WITH_ID:

                String id = uri.getPathSegments().get(1);
                reiseDeleted = sqLiteDatabase.delete(TABLE_NAME
                        , "_id=?", new String[]{id});

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        if (reiseDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }

        return reiseDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = reiseDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int reiseUpdated;

        switch (match) {
            case REISE_WITH_ID:

                String id = uri.getPathSegments().get(1);
                reiseUpdated = sqLiteDatabase.update(TABLE_NAME, values, "_id=?", new String[]{id});

                if (reiseUpdated <= 0) {
                    throw new SQLException("Failed to update row " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return reiseUpdated;
    }
}
