package com.hisivasankar.flicksbuddy.pojos;

/**
 * Created by Sivasankar on 12/24/2015.
 */
public class Flick {
    private String mName;
    private String mImageUrl;
    private String mDescription;
    private String mRating;
    private String mHref;
    private int mImageID;

    public Flick(String name) {
        mName = name;
    }

    public Flick(int imageID) {
        mImageID = imageID;
    }

    public Flick(String name, String imgUrl, String description, String rating) {
        mName = name;
        mImageUrl = imgUrl;
        mDescription = description;
        mRating = rating;
    }

    @Override
    public String toString() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getName() {
        return mName;
    }

    public String getRating() {
        return mRating;
    }

    public String getHref() {
        return mHref;
    }


    public int getImageID() {
        return mImageID;
    }
}
