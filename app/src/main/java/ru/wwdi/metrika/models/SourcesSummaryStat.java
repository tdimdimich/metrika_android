package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.SourcesSummaryStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "SourcesSummaryStat")
public class SourcesSummaryStat extends Model {

/*
      {
         "name" : "Прямые заходы",
         "visits_delayed" : 4521,
         "id" : "0"
      }
 */

    @Column(name="response")
    private SourcesSummaryStatResponse response;

    @Column(name = "visits_delayed")
    @Expose
    @SerializedName("visits_delayed")
    private Integer visitsDelayed;

    @Column(name = "name")
    @Expose
    @SerializedName("name")
    private String name;

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

    public SourcesSummaryStatResponse getResponse() {
        return response;
    }

    public void setResponse(SourcesSummaryStatResponse response) {
        this.response = response;
    }

    public Integer getVisitsDelayed() {
        return visitsDelayed;
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
