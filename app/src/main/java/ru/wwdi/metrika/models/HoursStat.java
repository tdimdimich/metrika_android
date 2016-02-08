package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.HourlyStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "HoursStat")
public class HoursStat extends Model {

    /*
    {
         "hours" : "00:00",
         "avg_visits" : 134.8571

     */

    @Column(name="response")
    private HourlyStatResponse response;

    @Column(name = "avg_visits")
    @Expose
    @SerializedName("avg_visits")
    private Float avgVisits;

    @Column(name = "hours")
    @Expose
    @SerializedName("hours")
    private String hours;

    @Column(name = "denial")
    @Expose
    @SerializedName("denial")
    private Float denial=Float.valueOf(0);

    @Column(name = "depth")
    @Expose
    @SerializedName("depth")
    private Float depth=Float.valueOf(0);

    @Column(name = "visit_time")
    @Expose
    @SerializedName("visit_time")
    private Integer visitTime;

    public HourlyStatResponse getResponse() {
        return response;
    }

    public void setResponse(HourlyStatResponse response) {
        this.response = response;
    }

    public Float getAvgVisits() {
        return avgVisits;
    }

    public String getHours() {
        return hours;
    }

    public Float getDepth() {
        return depth;
    }

    public Integer getVisitTime() {
        return visitTime;
    }

    public Float getDenial() {
        return denial;
    }
}
