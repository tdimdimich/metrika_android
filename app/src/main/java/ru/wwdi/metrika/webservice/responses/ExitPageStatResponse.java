package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.ExitPageStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "ExitPageStatResponse")
public class ExitPageStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private ExitPageStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private ExitPageStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private ExitPageStat totals;

    @SerializedName("data")
    @Expose
    private List<ExitPageStat> stats;


    public ExitPageStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(ExitPageStat max) {
        this.max = max;
    }

    public void setMin(ExitPageStat min) {
        this.min = min;
    }

    public void setTotals(ExitPageStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public ExitPageStat getMax() {
        return max;
    }

    public ExitPageStat getMin() {
        return min;
    }

    public ExitPageStat getTotals() {
        return totals;
    }

    public List<ExitPageStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(ExitPageStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        totals.save();
        min.save();
        max.save();
        save();
        for (ExitPageStat stat : stats) {
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<ExitPageStatResponse> list = new Select().from(ExitPageStatResponse.class).execute();
        for (ExitPageStatResponse response : list) {
            ExitPageStat max = response.getMax();
            ExitPageStat min = response.getMin();
            ExitPageStat totals = response.getTotals();
            List<ExitPageStat> items = response.getStats();
            for (ExitPageStat item : items) {
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
