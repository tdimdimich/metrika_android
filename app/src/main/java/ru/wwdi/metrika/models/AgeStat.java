package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.AgeGenderStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "AgeStat")
public class AgeStat extends Model {

    /*
    {
         "denial" : 0.2704,
         "visits_percent" : 0.015,
         "name" : "младше 18 лет",
         "id" : "17",
         "visit_time" : 94,
         "depth" : 1.7143
      }
     */

    @Column(name="response")
    private AgeGenderStatResponse response;

    @Column(name = "denial")
    @Expose
    @SerializedName("denial")
    private Float denial;

    @Column(name = "visits_percent")
    @Expose
    @SerializedName("visits_percent")
    private Float visitsPercent;

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

    public AgeGenderStatResponse getResponse() {
        return response;
    }

    public void setResponse(AgeGenderStatResponse response) {
        this.response = response;
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
