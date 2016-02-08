package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.BrowsersStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "BrowserStat")
public class BrowserStat extends Model {

    /*
    "denial" : 0.7182,
         "visits" : 11685,
         "page_views" : 17243,
         "version" : null,
         "name" : "Firefox",
         "visit_time" : 67,
         "depth" : 1.4757,
     */

    @Column(name="response")
    private BrowsersStatResponse response;

    @Column(name = "depth")
    @Expose
    @SerializedName("depth")
    private Float depth;

    @Column(name = "name")
    @Expose
    @SerializedName("name")
    private String name;

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

    public BrowsersStatResponse getResponse() {
        return response;
    }

    public void setResponse(BrowsersStatResponse response) {
        this.response = response;
    }

    public String getName() {
        return name;
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
