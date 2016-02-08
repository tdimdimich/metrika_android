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

import ru.wwdi.metrika.models.AgeGenderStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "AgeGenderStructureStatResponse")
public class AgeGenderStructureStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private AgeGenderStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private AgeGenderStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private AgeGenderStat totals;

    @SerializedName("data")
    @Expose
    private List<AgeGenderStat> stats;


    public AgeGenderStructureStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(AgeGenderStat max) {
        this.max = max;
    }

    public void setMin(AgeGenderStat min) {
        this.min = min;
    }

    public void setTotals(AgeGenderStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public AgeGenderStat getMax() {
        return max;
    }

    public AgeGenderStat getMin() {
        return min;
    }

    public AgeGenderStat getTotals() {
        return totals;
    }

    public List<AgeGenderStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(AgeGenderStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        Collections.sort(stats, new Comparator<AgeGenderStat>() {
            @Override
            public int compare(AgeGenderStat lhs, AgeGenderStat rhs) {
                if (lhs.getVisits() > rhs.getVisits()) return -1;
                if (lhs.getVisits() < rhs.getVisits()) return 1;
                return 0;
            }
        });

        totals.save();
        min.save();
        max.save();
        save();
        for (AgeGenderStat stat : stats) {
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<AgeGenderStructureStatResponse> list = new Select().from(AgeGenderStructureStatResponse.class).execute();
        for (AgeGenderStructureStatResponse response : list) {
            AgeGenderStat max = response.getMax();
            AgeGenderStat min = response.getMin();
            AgeGenderStat totals = response.getTotals();
            List<AgeGenderStat> itemsA = response.getStats();
            for (AgeGenderStat item : itemsA) {
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
