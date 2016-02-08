package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.DisplaysStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "DisplaySizeStat")
public class DisplaySizeStat extends Model {

    /*
    {
         "width" : 1024,
         "height" : 1600,
         "format" : "10:16",
         "id" : "1024_1600"
      }
     */

    @Column(name="response")
    private DisplaysStatResponse response;

    @Column(name = "format")
    @Expose
    @SerializedName("format")
    private String format;

    @Column(name = "height")
    @Expose
    @SerializedName("height")
    private Integer height;

    @Column(name = "width")
    @Expose
    @SerializedName("width")
    private Integer width;

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

    public DisplaySizeStat(){

    }

    public DisplaySizeStat(String format, Integer height, Integer width, Float depth, String name, Integer visitTime, Integer pageViews, Integer visits, Float denial){
        this.format = format;
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.name = name;
        this.visitTime = visitTime;
        this.pageViews = pageViews;
        this.visits = visits;
        this.denial = denial;
    }

    public DisplaysStatResponse getResponse() {
        return response;
    }

    public void setResponse(DisplaysStatResponse response) {
        this.response = response;
    }

    public String getFormat() {
        return format;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
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
