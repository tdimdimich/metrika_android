package ru.wwdi.metrika.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ryashentsev on 09.05.14.
 */
public class YandexError {

    @Expose
    @SerializedName("text")
    private String mText;

    @Expose
    @SerializedName("code")
    private String mCode;


    public String getText() {
        return mText;
    }

    public String getCode() {
        return mCode;
    }
}
