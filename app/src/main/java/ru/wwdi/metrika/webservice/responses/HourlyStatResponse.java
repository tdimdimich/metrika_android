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

import ru.wwdi.metrika.models.HoursStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "HourlyStatResponse")
public class HourlyStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private HoursStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private HoursStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private HoursStat totals;

    @SerializedName("data")
    @Expose
    private List<HoursStat> stats;

    public HourlyStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(HoursStat max) {
        this.max = max;
    }

    public void setMin(HoursStat min) {
        this.min = min;
    }

    public void setTotals(HoursStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public HoursStat getMax() {
        return max;
    }

    public HoursStat getMin() {
        return min;
    }

    public HoursStat getTotals() {
        return totals;
    }

    public List<HoursStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(HoursStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        Collections.sort(stats, new Comparator<HoursStat>() {
            @Override
            public int compare(HoursStat lhs, HoursStat rhs) {
                if (lhs.getAvgVisits() > rhs.getAvgVisits()) return -1;
                if (lhs.getAvgVisits() < rhs.getAvgVisits()) return 1;
                return 0;
            }
        });

        totals.save();
        min.save();
        max.save();
        save();
        for (HoursStat stat : stats) {
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<HourlyStatResponse> list = new Select().from(HourlyStatResponse.class).execute();
        for (HourlyStatResponse response : list) {
            HoursStat max = response.getMax();
            HoursStat min = response.getMin();
            HoursStat totals = response.getTotals();
            List<HoursStat> items = response.getStats();
            for (HoursStat item : items) {
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
