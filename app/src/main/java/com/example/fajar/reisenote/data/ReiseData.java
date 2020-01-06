package com.example.fajar.reisenote.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ReiseData implements Parcelable {
    public static final Creator<ReiseData> CREATOR = new Creator<ReiseData>() {
        @Override
        public ReiseData createFromParcel(Parcel in) {
            return new ReiseData(in);
        }

        @Override
        public ReiseData[] newArray(int size) {
            return new ReiseData[size];
        }
    };
    private String reiseTitle;
    private String reiseDesc;
    private String reiseLastUpdate;
    private String pathImg;
    private long id;
    private boolean isStarred;
    private boolean isFav;
    private boolean isPoem;
    private boolean isPinned;
    private boolean isStory;

    protected ReiseData(Parcel in) {
        reiseTitle = in.readString();
        reiseDesc = in.readString();
        reiseLastUpdate = in.readString();

        id = in.readLong();
        isStarred = in.readByte() != 0;
        isFav = in.readByte() != 0;
        isPoem = in.readByte() != 0;
        isPinned = in.readByte() != 0;
        isStory = in.readByte() != 0;
        pathImg = in.readString();
    }

    public ReiseData() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public String getReiseTitle() {
        return reiseTitle;
    }

    public void setReiseTitle(String reiseTitle) {
        this.reiseTitle = reiseTitle;
    }

    public String getReiseDesc() {
        return reiseDesc;
    }

    public void setReiseDesc(String reiseDesc) {
        this.reiseDesc = reiseDesc;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public String getReiseLastUpdate() {
        return reiseLastUpdate;
    }

    public void setReiseLastUpdate(String reiseLastUpdate) {
        this.reiseLastUpdate = reiseLastUpdate;
    }

    public boolean getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public boolean getIsFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    public boolean getIsPoem() {
        return isPoem;
    }

    public void setIsPoem(boolean isPoem) {
        this.isPoem = isPoem;
    }

    public boolean getIsStory() {
        return isStory;
    }

    public void setIsStory(boolean isStory) {
        this.isStory = isStory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reiseTitle);
        dest.writeString(reiseDesc);
        dest.writeString(reiseLastUpdate);
        dest.writeLong(id);
        dest.writeString(pathImg);
        dest.writeByte((byte) (isStarred ? 1 : 0));
        dest.writeByte((byte) (isFav ? 1 : 0));
        dest.writeByte((byte) (isPoem ? 1 : 0));
        dest.writeByte((byte) (isPinned ? 1 : 0));
        dest.writeByte((byte) (isStory ? 1 : 0));
    }
}
