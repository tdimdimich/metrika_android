package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.DeepnessStat;
import ru.wwdi.metrika.models.TimeStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "DeepnessStatResponse")
public class DeepnessStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private DeepnessStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private DeepnessStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private DeepnessStat totals;

    @SerializedName("data_depth")
    @Expose
    private List<DeepnessStat> deepnessStats;

    @SerializedName("data_time")
    @Expose
    private List<TimeStat> timeStats;



    public DeepnessStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(DeepnessStat max) {
        this.max = max;
    }

    public void setMin(DeepnessStat min) {
        this.min = min;
    }

    public void setTotals(DeepnessStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public DeepnessStat getMax() {
        return max;
    }

    public DeepnessStat getMin() {
        return min;
    }

    public DeepnessStat getTotals() {
        return totals;
    }

    public List<DeepnessStat> getDeepnessStats() {
        if(deepnessStats!=null) return deepnessStats;
        return deepnessStats=getMany(DeepnessStat.class, "response");
    }

    public List<TimeStat> getTimeStats() {
        if(timeStats!=null) return timeStats;
        return timeStats=getMany(TimeStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;
        Collections.sort(timeStats, new Comparator<TimeStat>() {
            @Override
            public int compare(TimeStat lhs, TimeStat rhs) {
                if (lhs.getVisits() > rhs.getVisits()) return -1;
                if (lhs.getVisits() < rhs.getVisits()) return 1;
                return 0;
            }
        });
        Collections.sort(deepnessStats, new Comparator<DeepnessStat>() {
            @Override
            public int compare(DeepnessStat lhs, DeepnessStat rhs) {
                if (lhs.getVisits() > rhs.getVisits()) return -1;
                if (lhs.getVisits() < rhs.getVisits()) return 1;
                return 0;
            }
        });

        totals.save();
        min.save();
        max.save();
        save();
        for(DeepnessStat stat: deepnessStats){
            stat.setResponse(this);
            stat.save();
        }
        for(TimeStat stat: timeStats){
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<DeepnessStatResponse> list = new Select().from(DeepnessStatResponse.class).execute();
        for(DeepnessStatResponse response: list){
            DeepnessStat max = response.getMax();
            DeepnessStat min = response.getMin();
            DeepnessStat totals = response.getTotals();
            List<DeepnessStat> items = response.getDeepnessStats();
            for(DeepnessStat item: items){
                item.delete();
            }
            List<TimeStat> itemsT = response.getTimeStats();
            for(TimeStat item: itemsT){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
