package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.PopularStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "PopularStatResponse")
public class PopularStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private PopularStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private PopularStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private PopularStat totals;

    @SerializedName("data")
    @Expose
    private List<PopularStat> stats;



    public PopularStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(PopularStat max) {
        this.max = max;
    }

    public void setMin(PopularStat min) {
        this.min = min;
    }

    public void setTotals(PopularStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PopularStat getMax() {
        return max;
    }

    public PopularStat getMin() {
        return min;
    }

    public PopularStat getTotals() {
        return totals;
    }

    public List<PopularStat> getStats() {
        if(stats!=null) return stats;
        return stats=getMany(PopularStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        totals.save();
        min.save();
        max.save();
        save();
        for(PopularStat stat: stats){
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<PopularStatResponse> list = new Select().from(PopularStatResponse.class).execute();
        for(PopularStatResponse response: list){
            PopularStat max = response.getMax();
            PopularStat min = response.getMin();
            PopularStat totals = response.getTotals();
            List<PopularStat> items = response.getStats();
            for(PopularStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
