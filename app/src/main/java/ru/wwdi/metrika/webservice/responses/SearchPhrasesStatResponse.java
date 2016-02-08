package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.SearchPhraseStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "SearchPhrasesStatResponse")
public class SearchPhrasesStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private SearchPhraseStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private SearchPhraseStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private SearchPhraseStat totals;

    @SerializedName("data")
    @Expose
    private List<SearchPhraseStat> searchPhraseStats;



    public SearchPhrasesStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(SearchPhraseStat max) {
        this.max = max;
    }

    public void setMin(SearchPhraseStat min) {
        this.min = min;
    }

    public void setTotals(SearchPhraseStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public SearchPhraseStat getMax() {
        return max;
    }

    public SearchPhraseStat getMin() {
        return min;
    }

    public SearchPhraseStat getTotals() {
        return totals;
    }

    public List<SearchPhraseStat> getSearchPhraseStats() {
        if(searchPhraseStats !=null) return searchPhraseStats;
        return searchPhraseStats = getMany(SearchPhraseStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for(SearchPhraseStat stat: searchPhraseStats){
            stat.setResponse(this);
            stat.save();
        }
        save();
    }

    public static void clearResponses() {
        List<SearchPhrasesStatResponse> list = new Select().from(SearchPhrasesStatResponse.class).execute();
        for(SearchPhrasesStatResponse response: list){
            SearchPhraseStat max = response.getMax();
            SearchPhraseStat min = response.getMin();
            SearchPhraseStat totals = response.getTotals();
            List<SearchPhraseStat> items = response.getSearchPhraseStats();
            for(SearchPhraseStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
