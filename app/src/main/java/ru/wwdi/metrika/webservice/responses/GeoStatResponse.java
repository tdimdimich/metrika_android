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

import ru.wwdi.metrika.models.GeoStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "GeoStatResponse")
public class GeoStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private GeoStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private GeoStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private GeoStat totals;

    @SerializedName("data")
    @Expose
    private List<GeoStat> geoStats;


    public GeoStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(GeoStat max) {
        this.max = max;
    }

    public void setMin(GeoStat min) {
        this.min = min;
    }

    public void setTotals(GeoStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public GeoStat getMax() {
        return max;
    }

    public GeoStat getMin() {
        return min;
    }

    public GeoStat getTotals() {
        return totals;
    }

    public List<GeoStat> getGeoStats() {
        if (geoStats != null) return geoStats;
        return geoStats = getMany(GeoStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        Collections.sort(geoStats, new Comparator<GeoStat>() {
            @Override
            public int compare(GeoStat lhs, GeoStat rhs) {
                if (lhs.getVisits() > rhs.getVisits()) return -1;
                if (lhs.getVisits() < rhs.getVisits()) return 1;
                return 0;
            }
        });

        totals.save();
        min.save();
        max.save();
        save();
        for (GeoStat stat : geoStats) {
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<GeoStatResponse> list = new Select().from(GeoStatResponse.class).execute();
        for (GeoStatResponse response : list) {
            GeoStat max = response.getMax();
            GeoStat min = response.getMin();
            GeoStat totals = response.getTotals();
            List<GeoStat> items = response.getGeoStats();
            for (GeoStat item : items) {
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
