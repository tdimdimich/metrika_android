package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.URLParamsStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "URLParamsStat")
public class URLParamsStat extends Model {

    @Column(name="response")
    private URLParamsStatResponse response;

    @Column(name = "name")
    @Expose
    @SerializedName("name")
    private String name;

    @Column(name = "page_views")
    @Expose
    @SerializedName("page_views")
    private Integer pageViews;

    public URLParamsStat(){

    }

    public URLParamsStat(String name, Integer pageViews){
        this.name = name;
        this.pageViews = pageViews;
    }

    public URLParamsStatResponse getResponse() {
        return response;
    }

    public void setResponse(URLParamsStatResponse response) {
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public Integer getPageViews() {
        return pageViews;
    }
}
