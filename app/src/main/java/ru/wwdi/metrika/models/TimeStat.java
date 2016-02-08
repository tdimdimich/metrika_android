package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.DeepnessStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "TimeStat")
public class TimeStat extends Model {

    @Column(name="response")
    private DeepnessStatResponse response;

    @Column(name = "denial")
    @Expose
    @SerializedName("denial")
    private Float denial;

    @Column(name = "visits_percent")
    @Expose
    @SerializedName("visits_percent")
    private Float visitsPercent;

    @Column(name = "visits")
    @Expose
    @SerializedName("visits")
    private Integer visits;

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

    public DeepnessStatResponse getResponse() {
        return response;
    }

    public void setResponse(DeepnessStatResponse response) {
        this.response = response;
    }

    public Integer getVisits() {
        return visits;
    }

    public Float getVisitsPercent() {
        return visitsPercent;
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

    public Float getDenial() {
        return denial;
    }
}
