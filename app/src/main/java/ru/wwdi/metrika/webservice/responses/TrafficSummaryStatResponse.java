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

import ru.wwdi.metrika.models.TrafficSummaryStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "TrafficSummaryStatResponse")
public class TrafficSummaryStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private TrafficSummaryStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private TrafficSummaryStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private TrafficSummaryStat totals;

    @SerializedName("data")
    @Expose
    private List<TrafficSummaryStat> trafficSummaries;


    public TrafficSummaryStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(TrafficSummaryStat max) {
        this.max = max;
    }

    public void setMin(TrafficSummaryStat min) {
        this.min = min;
    }

    public void setTotals(TrafficSummaryStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public TrafficSummaryStat getMax() {
        return max != null ? max : new TrafficSummaryStat();
    }

    public TrafficSummaryStat getMin() {
        return min != null ? min : new TrafficSummaryStat();
    }

    public TrafficSummaryStat getTotals() {
        return totals != null ? totals : new TrafficSummaryStat();
    }

    public List<TrafficSummaryStat> getTrafficSummaries() {
        if (trafficSummaries != null) return trafficSummaries;
        trafficSummaries = getMany(TrafficSummaryStat.class, "response");
        Collections.sort(trafficSummaries, new Comparator<TrafficSummaryStat>() {
            @Override
            public int compare(TrafficSummaryStat lhs, TrafficSummaryStat rhs) {
                if (lhs.getDate().getTime() > rhs.getDate().getTime()) return 1;
                if (lhs.getDate().getTime() < rhs.getDate().getTime()) return -1;
                return 0;
            }
        });
        return trafficSummaries;
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;
        if (totals != null)
            totals.save();
        if (min != null)
            min.save();
        if (max != null)
            max.save();
        save();
        Collections.sort(trafficSummaries, new Comparator<TrafficSummaryStat>() {
            @Override
            public int compare(TrafficSummaryStat lhs, TrafficSummaryStat rhs) {
                if (lhs.getDate().getTime() > rhs.getDate().getTime()) return 1;
                if (lhs.getDate().getTime() < rhs.getDate().getTime()) return -1;
                return 0;
            }
        });
        for (TrafficSummaryStat summary : trafficSummaries) {
            summary.setResponse(this);
            summary.save();
        }
    }

    public static void clearResponses() {
        List<TrafficSummaryStatResponse> list = new Select().from(TrafficSummaryStatResponse.class).execute();
        for (TrafficSummaryStatResponse response : list) {
            TrafficSummaryStat max = response.getMax();
            TrafficSummaryStat min = response.getMin();
            TrafficSummaryStat totals = response.getTotals();
            List<TrafficSummaryStat> items = response.getTrafficSummaries();
            for (TrafficSummaryStat item : items) {
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
