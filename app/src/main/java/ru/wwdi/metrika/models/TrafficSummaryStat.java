package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "TrafficSummaryStat")
public class TrafficSummaryStat extends Model {

    @Column(name="response")
    private TrafficSummaryStatResponse response;

    @Column(name = "visitors")
    @Expose
    @SerializedName("visitors")
    private Integer visitors = 0;

    @Column(name = "new_visitors")
    @Expose
    @SerializedName("new_visitors")
    private Integer newVisitors = 0;

    @Column(name = "depth")
    @Expose
    @SerializedName("depth")
    private Float depth = 0f;

    @Column(name = "visit_time")
    @Expose
    @SerializedName("visit_time")
    private Integer visitTime = 0;

    @Column(name = "date")
    @Expose
    @SerializedName("date")
    private Date date;

    @Column(name = "page_views")
    @Expose
    @SerializedName("page_views")
    private Integer pageViews = 0;

    @Column(name = "new_visitors_perc")
    @Expose
    @SerializedName("new_visitors_perc")
    private Float newVisitorsPerc =0f;

    @Column(name = "visits")
    @Expose
    @SerializedName("visits")
    private Integer visits = 0;

    @Column(name = "denial")
    @Expose
    @SerializedName("denial")
    private Float denial = 0f;

    public TrafficSummaryStatResponse getResponse() {
        return response;
    }

    public void setResponse(TrafficSummaryStatResponse response) {
        this.response = response;
    }

    public Integer getVisitors() {
        return visitors;
    }

    public Integer getNewVisitors() {
        return newVisitors;
    }

    public Float getDepth() {
        return depth;
    }

    public Integer getVisitTime() {
        return visitTime;
    }

    public Date getDate() {
        return date;
//        return DateHelper.getDateFromString(date);
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public Float getNewVisitorsPerc() {
        return newVisitorsPerc;
    }

    public Integer getVisits() {
        return visits;
    }

    public Float getDenial() {
        return denial;
    }
}
