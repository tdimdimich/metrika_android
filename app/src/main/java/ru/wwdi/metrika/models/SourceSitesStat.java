package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.SourceSitesStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "SourceSitesStat")
public class SourceSitesStat extends Model {

/*
{
         "url" : "http://feedback2.wwdi.ru/metrika/",
 }

 */

    @Column(name="response")
    private SourceSitesStatResponse response;

    @Column(name = "url")
    @Expose
    @SerializedName("url")
    private String url;

    @Column(name = "depth")
    @Expose
    @SerializedName("depth")
    private Float depth;

    @Column(name = "visit_time")
    @Expose
    @SerializedName("visit_time")
    private Integer visitTime;

    @Column(name = "page_views")
    @Expose
    @SerializedName("page_views")
    private Integer pageViews;

    @Column(name = "visits")
    @Expose
    @SerializedName("visits")
    private Integer visits;

    @Column(name = "denial")
    @Expose
    @SerializedName("denial")
    private Float denial;

    public SourceSitesStatResponse getResponse() {
        return response;
    }

    public void setResponse(SourceSitesStatResponse response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public Float getDepth() {
        return depth;
    }

    public Integer getVisitTime() {
        return visitTime;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public Integer getVisits() {
        return visits;
    }

    public Float getDenial() {
        return denial;
    }
}
