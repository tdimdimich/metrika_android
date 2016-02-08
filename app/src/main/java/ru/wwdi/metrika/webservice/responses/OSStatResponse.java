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

import ru.wwdi.metrika.models.OSStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "OSStatResponse")
public class OSStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private OSStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private OSStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private OSStat totals;

    @SerializedName("data")
    @Expose
    private List<OSStat> stats;


    public OSStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(OSStat max) {
        this.max = max;
    }

    public void setMin(OSStat min) {
        this.min = min;
    }

    public void setTotals(OSStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public OSStat getMax() {
        return max;
    }

    public OSStat getMin() {
        return min;
    }

    public OSStat getTotals() {
        return totals;
    }

    public List<OSStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(OSStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        Collections.sort(stats, new Comparator<OSStat>() {
            @Override
            public int compare(OSStat lhs, OSStat rhs) {
                if (lhs.getVisits() > rhs.getVisits()) return -1;
                if (lhs.getVisits() < rhs.getVisits()) return 1;
                return 0;
            }
        });

        save();
        totals.save();
        min.save();
        max.save();
        for (OSStat os : stats) {
            os.setResponse(this);
            os.save();
        }
        save();
    }

    public static void clearResponses() {
        List<OSStatResponse> list = new Select().from(OSStatResponse.class).execute();
        for (OSStatResponse response : list) {
            OSStat max = response.getMax();
            OSStat min = response.getMin();
            OSStat totals = response.getTotals();
            List<OSStat> items = response.getStats();
            for (OSStat item : items) {
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
