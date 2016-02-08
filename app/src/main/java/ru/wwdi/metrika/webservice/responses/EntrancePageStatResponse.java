package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.EntrancePageStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "EntrancePageStatResponse")
public class EntrancePageStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private EntrancePageStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private EntrancePageStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private EntrancePageStat totals;

    @SerializedName("data")
    @Expose
    private List<EntrancePageStat> stats;



    public EntrancePageStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(EntrancePageStat max) {
        this.max = max;
    }

    public void setMin(EntrancePageStat min) {
        this.min = min;
    }

    public void setTotals(EntrancePageStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public EntrancePageStat getMax() {
        return max;
    }

    public EntrancePageStat getMin() {
        return min;
    }

    public EntrancePageStat getTotals() {
        return totals;
    }

    public List<EntrancePageStat> getStats() {
        if(stats!=null) return stats;
        return stats=getMany(EntrancePageStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        totals.save();
        min.save();
        max.save();
        save();
        for(EntrancePageStat stat: stats){
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<EntrancePageStatResponse> list = new Select().from(EntrancePageStatResponse.class).execute();
        for(EntrancePageStatResponse response: list){
            EntrancePageStat max = response.getMax();
            EntrancePageStat min = response.getMin();
            EntrancePageStat totals = response.getTotals();
            List<EntrancePageStat> items = response.getStats();
            for(EntrancePageStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
