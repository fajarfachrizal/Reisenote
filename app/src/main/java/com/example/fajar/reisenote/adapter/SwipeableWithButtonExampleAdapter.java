package com.example.fajar.reisenote.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.example.fajar.reisenote.Glide.GlideImageLoader;
import com.example.fajar.reisenote.R;
import com.example.fajar.reisenote.activities.ReiseListActivity;
import com.example.fajar.reisenote.contentprovider.ReiseContract;
import com.example.fajar.reisenote.data.ReiseData;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.provider.BaseColumns._ID;
import static com.example.fajar.reisenote.activities.ReiseListActivity.REISE_LOADER_ID;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_DESC;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_FAV;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_IMAGE;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_LAST_UPDATED;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_STARRED;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.COLUMN_TITLE;
import static com.example.fajar.reisenote.contentprovider.ReiseContract.ReiseEntry.CONTENT_URI;

public class SwipeableWithButtonExampleAdapter
        extends RecyclerView.Adapter<SwipeableWithButtonExampleAdapter.MyViewHolder>
        implements SwipeableItemAdapter<SwipeableWithButtonExampleAdapter.MyViewHolder> {
    private static final String TAG = "MySwipeableItemAdapter";
    public Cursor mCursor;
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
    private boolean[] pinnedArray;
    private EventListener mEventListener;
    private View.OnClickListener mSwipeableViewContainerOnClickListener;
    private View.OnClickListener mUnderSwipeableViewButtonOnClickListener;

    public SwipeableWithButtonExampleAdapter(Context context, boolean[] pinnedArray) {
        this.pinnedArray = pinnedArray;
        this.context = context;


        mSwipeableViewContainerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeableViewContainerClick(v);
            }
        };
        mUnderSwipeableViewButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUnderSwipeableViewButtonClick(v);
            }
        };

        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    public static String getDateStringfromMilliseconds(long aLong) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(aLong);

        long reiseYear = calendar.get(Calendar.YEAR);
        long reiseWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        long reiseMonth = calendar.get(Calendar.MONTH);
        long reiseDate = calendar.get(Calendar.DATE);

        Calendar calendar1 = Calendar.getInstance();
        long currentYear = calendar1.get(Calendar.YEAR);
        long currentWeek = calendar1.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        long currentDate = calendar1.get(Calendar.DATE);


        if (reiseDate == currentDate) {
            SimpleDateFormat simpleDateFormat3 =
                    new SimpleDateFormat("'Today at 'K:mm a", Locale.ENGLISH);
            return simpleDateFormat3.format(calendar.getTime());
        }
        if (reiseWeek == currentWeek) {
            SimpleDateFormat simpleDateFormat2 =
                    new SimpleDateFormat("E' at 'K:mm a", Locale.ENGLISH);
            return simpleDateFormat2.format(calendar.getTime());

        }

        if (reiseYear == currentYear) {
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("MMM dd' at 'K:mm a", Locale.ENGLISH);
            return simpleDateFormat.format(calendar.getTime());

        } else {
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("MMM dd yyyy' at 'K:mm a", Locale.ENGLISH);
            return simpleDateFormat.format(calendar.getTime());
        }

    }

    private void onSwipeableViewContainerClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(
                    RecyclerViewAdapterUtils.getParentViewHolderItemView(v));
        }
    }

    public ReiseData getReiseData(int position) {
        mCursor.moveToPosition(position);
        int idIndex = mCursor.getColumnIndex(ReiseContract.ReiseEntry._ID);
        int descriptionIndex = mCursor.getColumnIndex(COLUMN_DESC);
        int titleIndex = mCursor.getColumnIndex(COLUMN_TITLE);
        int favIndex = mCursor.getColumnIndex(ReiseContract.ReiseEntry.COLUMN_FAV);
        int starIndex = mCursor.getColumnIndex(COLUMN_STARRED);
        int dateTime = mCursor.getColumnIndex(COLUMN_LAST_UPDATED);
        int imgPathIndex = mCursor.getColumnIndex(COLUMN_IMAGE);

        ReiseData reiseData = new ReiseData();

        reiseData.setId(mCursor.getInt(idIndex));
        reiseData.setReiseDesc(mCursor.getString(descriptionIndex));
        reiseData.setReiseTitle(mCursor.getString(titleIndex));
        reiseData.setPathImg(mCursor.getString(imgPathIndex));
        reiseData.setReiseLastUpdate(
                getDateStringfromMilliseconds(mCursor.getLong(dateTime))
        );
        reiseData.setIsFav(mCursor.getInt(favIndex) != 0);
        reiseData.setIsStarred(mCursor.getInt(starIndex) != 0);

        return reiseData;

    }

    private void onUnderSwipeableViewButtonClick(View v) {
        if (mEventListener != null) {
            mEventListener.onUnderSwipeableViewButtonClicked(
                    RecyclerViewAdapterUtils.getParentViewHolderItemView(v));
        }
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getColumnIndex(ReiseContract.ReiseEntry._ID);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.reise_list_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mCursor == null)
            return;
        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(ReiseContract.ReiseEntry._ID);
        int descriptionIndex = mCursor.getColumnIndex(COLUMN_DESC);
        int titleIndex = mCursor.getColumnIndex(COLUMN_TITLE);
        int favIndex = mCursor.getColumnIndex(ReiseContract.ReiseEntry.COLUMN_FAV);
        int starIndex = mCursor.getColumnIndex(COLUMN_STARRED);
        int dateTimeIndex = mCursor.getColumnIndex(COLUMN_LAST_UPDATED);
        int imgPathIndex = mCursor.getColumnIndex(COLUMN_IMAGE);
//        int storyIndex = mCursor.getColumnIndex(ReiseContract.ReiseEntry.COLUMN_TITLE);
//        int poemIndex = mCursor.getColumnIndex(ReiseContract.ReiseEntry.COLUMN_TITLE);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String description = mCursor.getString(descriptionIndex);
        String title = mCursor.getString(titleIndex);
        String imgPath = mCursor.getString(imgPathIndex);
        int fav = mCursor.getInt(favIndex);
        int star = mCursor.getInt(starIndex);
        String dateString = getDateStringfromMilliseconds(mCursor.getLong(dateTimeIndex));

        ViewCompat.setTransitionName(holder.reiseTitle, position + "");

        holder.reiseLastUpdated.setText(dateString);
        //Set values
        holder.itemView.setTag(id);

        // set listeners
        // (if the item is *pinned*, click event comes to the mContainer)
        holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);
        holder.mBehindViews.setOnClickListener(mUnderSwipeableViewButtonOnClickListener);

        // set text
        holder.reiseTitle.setText(title);
        holder.reiseDesc.setText(description);
        holder.imgPathUrl.setText(imgPath);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_pic_error)
                .priority(Priority.HIGH);

        new GlideImageLoader(holder.imgRetrive2,
                holder.progressBarCyclic2).load(imgPath, options);


        if (star == 0)
            holder.starImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_grey));
        else
            holder.starImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_gold));

        if (fav == 0)
            holder.favImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav_grey));
        else
            holder.favImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav_gold));

        // set swiping properties
        holder.setMaxLeftSwipeAmount(-0.30f);
        holder.setMaxRightSwipeAmount(0);
        if (pinnedArray.length != mCursor.getCount()) {
            pinnedArray = new boolean[mCursor.getCount()];
        }
        holder.setSwipeItemHorizontalSlideAmount(pinnedArray[position] ? -0.30f : 0);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }


    @Override
    public int onGetSwipeReactionType(MyViewHolder holder, int position, int x, int y) {
        if (com.example.fajar.reisenote.utils.ViewUtils.hitTest(holder.getSwipeableContainerView(), x, y)) {
            return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
        } else {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        }
    }

    @Override
    public void onSwipeItemStarted(MyViewHolder holder, int position) {

    }

    @Override
    public void onSetSwipeBackground(MyViewHolder holder, int position, int type) {
        if (type == Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND) {
            holder.mBehindViews.setVisibility(View.GONE);
        } else {
            holder.mBehindViews.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public SwipeResultAction onSwipeItem(MyViewHolder holder, int position, int result) {
        Log.d(TAG, "onSwipeItem(position = " + position + ", result = " + result + ")");

        switch (result) {
            // swipe left --- pin
            case Swipeable.RESULT_SWIPED_LEFT:
                return new SwipeLeftResultAction(this, position);
            // other --- do nothing
            case Swipeable.RESULT_SWIPED_RIGHT:
            case Swipeable.RESULT_CANCELED:
            default:
                if (position != RecyclerView.NO_POSITION) {
                    return new UnpinResultAction(this, position);
                } else {
                    return null;
                }
        }
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        if (c != null)
            notifyDataSetChanged();
        return temp;
    }

    // NOTE: Make accessible with short name
    private interface Swipeable extends SwipeableItemConstants {
    }

    public interface EventListener {
        void onItemPinned(int position);

        void onItemViewClicked(View v);

        void onUnderSwipeableViewButtonClicked(View v);
    }

    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private final int mPosition;
        private SwipeableWithButtonExampleAdapter mAdapter;
        private boolean mSetPinned;

        SwipeLeftResultAction(SwipeableWithButtonExampleAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            boolean isPinned = mAdapter.pinnedArray[mPosition];

            if (!isPinned) {
                mAdapter.pinnedArray[mPosition] = true;
                mAdapter.notifyItemChanged(mPosition);
                mSetPinned = true;
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mSetPinned && mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onItemPinned(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class UnpinResultAction extends SwipeResultActionDefault {
        private final int mPosition;
        private SwipeableWithButtonExampleAdapter mAdapter;

        UnpinResultAction(SwipeableWithButtonExampleAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            boolean isPinned = mAdapter.pinnedArray[mPosition];
            if (isPinned) {
                mAdapter.pinnedArray[mPosition] = false;
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    public class MyViewHolder extends AbstractSwipeableItemViewHolder {
        public RelativeLayout mContainer;
        public RelativeLayout mBehindViews;
        public TextView reiseTitle;
        TextView reiseDesc, reiseLastUpdated, imgPathUrl;
        ImageView starImageView, favImageView, imgRetrive2;
        ProgressBar progressBarCyclic2;

        public MyViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.reise_container);
            mBehindViews = v.findViewById(R.id.behind_views);
            reiseTitle = v.findViewById(R.id.reise_title);
            reiseDesc = v.findViewById(R.id.reise_desc);
            reiseLastUpdated = v.findViewById(R.id.last_updated);
            starImageView = v.findViewById(R.id.star_icon_id);
            favImageView = v.findViewById(R.id.fav_icon_id);
            imgPathUrl = v.findViewById(R.id.img_path);
            imgRetrive2 = v.findViewById(R.id.image_load3);
            progressBarCyclic2 = v.findViewById(R.id.progressBar_cyclic2);


            starImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCursor.moveToPosition(getAdapterPosition()); // get to the right location in the cursor

                    // Determine the values of the wanted data
                    final int id = mCursor.getInt(mCursor.getColumnIndex(ReiseContract.ReiseEntry._ID));
                    String description = mCursor.getString(mCursor.getColumnIndex(COLUMN_DESC));
                    String title = mCursor.getString(mCursor.getColumnIndex(COLUMN_TITLE));
                    String imgPath = mCursor.getString(mCursor.getColumnIndex(COLUMN_IMAGE));
                    int fav = mCursor.getInt(mCursor.getColumnIndex(ReiseContract.ReiseEntry.COLUMN_FAV));
                    int star = mCursor.getInt(mCursor.getColumnIndex(COLUMN_STARRED));
                    long lastUpdated = mCursor.getLong(mCursor.getColumnIndex(COLUMN_LAST_UPDATED));

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(_ID, id);
                    contentValues.put(COLUMN_DESC, description);
                    contentValues.put(COLUMN_FAV, fav);
                    contentValues.put(COLUMN_TITLE, title);
                    contentValues.put(COLUMN_LAST_UPDATED, lastUpdated);
                    contentValues.put(COLUMN_IMAGE, imgPath);


                    if (star == 0)
                        starImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_gold));
                    else
                        starImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_grey));

                    contentValues.put(COLUMN_STARRED, star == 0 ? 1 : 0);

                    Uri uri = CONTENT_URI.buildUpon().appendPath(id + "").build();
                    context.getContentResolver().update(uri, contentValues, null, null);
                    ((ReiseListActivity) context).getSupportLoaderManager().restartLoader(REISE_LOADER_ID, null, (ReiseListActivity) context);

                }
            });

            favImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCursor.moveToPosition(getAdapterPosition()); // get to the right location in the cursor

                    // Determine the values of the wanted data
                    final int id = mCursor.getInt(mCursor.getColumnIndex(ReiseContract.ReiseEntry._ID));
                    String description = mCursor.getString(mCursor.getColumnIndex(COLUMN_DESC));
                    String title = mCursor.getString(mCursor.getColumnIndex(COLUMN_TITLE));
                    String imgPath = mCursor.getString(mCursor.getColumnIndex(COLUMN_IMAGE));
                    int fav = mCursor.getInt(mCursor.getColumnIndex(ReiseContract.ReiseEntry.COLUMN_FAV));
                    int star = mCursor.getInt(mCursor.getColumnIndex(COLUMN_STARRED));
                    long lastUpdated = mCursor.getLong(mCursor.getColumnIndex(COLUMN_LAST_UPDATED));

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(_ID, id);
                    contentValues.put(COLUMN_DESC, description);
                    contentValues.put(COLUMN_STARRED, star);
                    contentValues.put(COLUMN_TITLE, title);
                    contentValues.put(COLUMN_LAST_UPDATED, lastUpdated);
                    contentValues.put(COLUMN_IMAGE, imgPath);


                    if (fav == 0)
                        favImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav_gold));
                    else
                        favImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav_grey));

                    contentValues.put(COLUMN_FAV, fav == 0 ? 1 : 0);

                    Uri uri = CONTENT_URI.buildUpon().appendPath(id + "").build();
                    context.getContentResolver().update(uri, contentValues, null, null);
                    ((ReiseListActivity) context).getSupportLoaderManager().restartLoader(REISE_LOADER_ID, null, (ReiseListActivity) context);

                }
            });
        }


        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }

    }

}
