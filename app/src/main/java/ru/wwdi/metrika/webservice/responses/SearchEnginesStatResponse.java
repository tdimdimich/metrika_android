package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.SearchEngineStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "SearchEnginesStatResponse")
public class SearchEnginesStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private SearchEngineStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private SearchEngineStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private SearchEngineStat totals;

    @SerializedName("data")
    @Expose
    private List<SearchEngineStat> searchEngineStats;



    public SearchEnginesStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(SearchEngineStat max) {
        this.max = max;
    }

    public void setMin(SearchEngineStat min) {
        this.min = min;
    }

    public void setTotals(SearchEngineStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public SearchEngineStat getMax() {
        return max;
    }

    public SearchEngineStat getMin() {
        return min;
    }

    public SearchEngineStat getTotals() {
        return totals;
    }

    public List<SearchEngineStat> getSearchEngineStats() {
        if(searchEngineStats !=null) return searchEngineStats;
        return searchEngineStats = getMany(SearchEngineStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for(SearchEngineStat stat: searchEngineStats){
            stat.setResponse(this);
            stat.save();
        }
        save();
    }

    public static void clearResponses() {
        List<SearchEnginesStatResponse> list = new Select().from(SearchEnginesStatResponse.class).execute();
        for(SearchEnginesStatResponse response: list){
            SearchEngineStat max = response.getMax();
            SearchEngineStat min = response.getMin();
            SearchEngineStat totals = response.getTotals();
            List<SearchEngineStat> items = response.getSearchEngineStats();
            for(SearchEngineStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
