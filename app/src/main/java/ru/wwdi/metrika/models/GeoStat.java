package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.GeoStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "GeoStat")
public class GeoStat extends Model {

    @Column(name="response")
    private GeoStatResponse response;

    @Column(name = "denial")
    @Expose
    @SerializedName("denial")
    private Float denial;

    @Column(name = "name")
    @Expose
    @SerializedName("name")
    private String name;

    @Column(name = "region_type")
    @Expose
    @SerializedName("region_type")
    private String regionType;

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


    public GeoStatResponse getResponse() {
        return response;
    }

    public void setResponse(GeoStatResponse response) {
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public String getRegionType() {
        return regionType;
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
