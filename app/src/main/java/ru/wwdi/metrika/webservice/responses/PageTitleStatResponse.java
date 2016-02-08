package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.PageTitleStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "PageTitleStatResponse")
public class PageTitleStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private PageTitleStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private PageTitleStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private PageTitleStat totals;

    @SerializedName("data")
    @Expose
    private List<PageTitleStat> stats;


    public PageTitleStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(PageTitleStat max) {
        this.max = max;
    }

    public void setMin(PageTitleStat min) {
        this.min = min;
    }

    public void setTotals(PageTitleStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PageTitleStat getMax() {
        return max;
    }

    public PageTitleStat getMin() {
        return min;
    }

    public PageTitleStat getTotals() {
        return totals;
    }

    public List<PageTitleStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(PageTitleStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        totals.save();
        min.save();
        max.save();
        save();
        for (PageTitleStat stat : stats) {
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<PageTitleStatResponse> list = new Select().from(PageTitleStatResponse.class).execute();
        for (PageTitleStatResponse response : list) {
            PageTitleStat max = response.getMax();
            PageTitleStat min = response.getMin();
            PageTitleStat totals = response.getTotals();
            List<PageTitleStat> items = response.getStats();
            for (PageTitleStat item : items) {
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
