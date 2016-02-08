package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.PopularStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "PopularStat")
public class PopularStat extends Model {

    /*
{
         "page_views" : 4809,
         "exit" : 2650,
         "url" : "http://help.wwdi.ru/metrika/general/goals.xml",
         "id" : "10478431011615719079",
         "entrance" : 3294
      }
     */
    @Column(name="response")
    private PopularStatResponse response;

    @Column(name = "url")
    @Expose
    @SerializedName("url")
    private String url;

    @Column(name = "exit")
    @Expose
    @SerializedName("exit")
    private Integer exit;

    @Column(name = "entrance")
    @Expose
    @SerializedName("entrance")
    private Integer entrance;

    @Column(name = "page_views")
    @Expose
    @SerializedName("page_views")
    private Integer pageViews;


    public PopularStatResponse getResponse() {
        return response;
    }

    public void setResponse(PopularStatResponse response) {
        this.response = response;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public String getUrl() {
        return url;
    }

    public Integer getExit() {
        return exit;
    }

    public Integer getEntrance() {
        return entrance;
    }
}
