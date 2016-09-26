package com.example.john.fragmentdemo.model.CriminallIntent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ZheWei on 2016/9/24.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mBoolean;//是否已经解决
    private String mSuspect; //嫌疑犯

    //设置图片的保存位置
    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean getBoolean() {
        return mBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        mBoolean = aBoolean;
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
