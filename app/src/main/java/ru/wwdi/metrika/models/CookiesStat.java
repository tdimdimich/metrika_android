package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.CookiesStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "CookiesStat")
public class CookiesStat extends Model {

    @Column(name="response")
    private CookiesStatResponse response;

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

    public CookiesStat(CookiesStatResponse response,
                       Float depth,
                       String name,
                       Integer visitTime,
                       Integer pageViews,
                       Integer visits,
                       Float denial
                       ){
        this.depth = depth;
        this.name = name;
        this.visitTime = visitTime;
        this.pageViews = pageViews;
        this.visits = visits;
        this.denial = denial;
    }

    public CookiesStat(){

    }

    public CookiesStatResponse getResponse() {
        return response;
    }

    public void setResponse(CookiesStatResponse response) {
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
