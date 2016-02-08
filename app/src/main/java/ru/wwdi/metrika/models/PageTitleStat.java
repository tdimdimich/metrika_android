package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.wwdi.metrika.webservice.responses.PageTitleStatResponse;

/**
 * Created by ryashentsev on 05.05.14.
 */
@Table(name = "PageTitleStat")
public class PageTitleStat extends Model {

    /*
    {
         "page_views" : 4975,
         "name" : "Цели — Яндекс.Помощь. Метрика",
         "id" : "14729704480144203099"
      }
     */
    @Column(name="response")
    private PageTitleStatResponse response;

    @Column(name = "name")
    @Expose
    @SerializedName("name")
    private String name;

    @Column(name = "page_views")
    @Expose
    @SerializedName("page_views")
    private Integer pageViews;


    public PageTitleStatResponse getResponse() {
        return response;
    }

    public void setResponse(PageTitleStatResponse response) {
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public Integer getPageViews() {
        return pageViews;
    }
}
