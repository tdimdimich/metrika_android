package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.FlashStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "FlashStatResponse")
public class FlashStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private FlashStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private FlashStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private FlashStat totals;

    @SerializedName("data")
    @Expose
    private List<FlashStat> stats;


    public FlashStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(FlashStat max) {
        this.max = max;
    }

    public void setMin(FlashStat min) {
        this.min = min;
    }

    public void setTotals(FlashStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public FlashStat getMax() {
        return max;
    }

    public FlashStat getMin() {
        return min;
    }

    public FlashStat getTotals() {
        return totals;
    }

    public List<FlashStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(FlashStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for (FlashStat os : stats) {
            os.setResponse(this);
            os.save();
        }
        save();
    }

    public static void clearResponses() {
        List<FlashStatResponse> list = new Select().from(FlashStatResponse.class).execute();
        for (FlashStatResponse response : list) {
            FlashStat max = response.getMax();
            FlashStat min = response.getMin();
            FlashStat totals = response.getTotals();
            List<FlashStat> items = response.getStats();
            for (FlashStat item : items) {
                item.delete();
            }
            response.delete();
            if (max != null) {
                max.delete();
            }
            if (min != null) {
                min.delete();
            }
            if (totals != null) {
                totals.delete();
            }
        }
    }

}
