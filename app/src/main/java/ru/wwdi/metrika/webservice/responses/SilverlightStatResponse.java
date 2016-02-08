package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.SilverlightStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "SilverlightStatResponse")
public class SilverlightStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private SilverlightStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private SilverlightStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private SilverlightStat totals;

    @SerializedName("data")
    @Expose
    private List<SilverlightStat> stats;



    public SilverlightStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(SilverlightStat max) {
        this.max = max;
    }

    public void setMin(SilverlightStat min) {
        this.min = min;
    }

    public void setTotals(SilverlightStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public SilverlightStat getMax() {
        return max;
    }

    public SilverlightStat getMin() {
        return min;
    }

    public SilverlightStat getTotals() {
        return totals;
    }

    public List<SilverlightStat> getStats() {
        if(stats !=null) return stats;
        return stats =getMany(SilverlightStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for(SilverlightStat os: stats){
            os.setResponse(this);
            os.save();
        }
        save();
    }

    public static void clearResponses() {
        List<SilverlightStatResponse> list = new Select().from(SilverlightStatResponse.class).execute();
        for(SilverlightStatResponse response: list){
            SilverlightStat max = response.getMax();
            SilverlightStat min = response.getMin();
            SilverlightStat totals = response.getTotals();
            List<SilverlightStat> items = response.getStats();
            for(SilverlightStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
