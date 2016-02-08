package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.SearchPhrasesStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "SearchPhraseStat")
public class SearchPhraseStat extends Model {

    @Column(name="response")
    private SearchPhrasesStatResponse response;

    @Column(name = "phrase")
    @Expose
    @SerializedName("phrase")
    private String phrase;

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

    public SearchPhrasesStatResponse getResponse() {
        return response;
    }

    public void setResponse(SearchPhrasesStatResponse response) {
        this.response = response;
    }

    public String getPhrase() {
        return phrase;
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
