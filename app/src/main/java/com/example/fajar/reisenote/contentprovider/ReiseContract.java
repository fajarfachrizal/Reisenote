package com.example.fajar.reisenote.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ReiseContract {

    public static final String AUTHORITY = "com.example.fajar.reisenote";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_REISE = "reise";
    public static final String FAV = "fav";
    public static final String STAR = "star";
    public static final String FAV_AND_STAR = "fav_and_star";

    public static final class ReiseEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REISE).build();

        public static final String TABLE_NAME = "reise";
        public static final String COLUMN_DESC = "DESC";
        public static final String COLUMN_LAST_UPDATED = "LAST_UPDATED";
        public static final String COLUMN_TITLE = "TITLE";
        public static final String COLUMN_STARRED = "STARRED";
        public static final String COLUMN_FAV = "FAV";
        public static final String COLUMN_POEM = "POEM";
        public static final String COLUMN_IMAGE = "IMAGE";
        public static final String COLUMN_STORY = "STORY";

    }
}
