package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.DeviceTypeStat;
import ru.wwdi.metrika.models.MobileStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "MobileStatResponse")
public class MobileStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private MobileStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private MobileStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private MobileStat totals;

    @SerializedName("data")
    @Expose
    private List<MobileStat> stats;

    @SerializedName("data_group")
    @Expose
    private List<DeviceTypeStat> groupStats;


    public MobileStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(MobileStat max) {
        this.max = max;
    }

    public void setMin(MobileStat min) {
        this.min = min;
    }

    public void setTotals(MobileStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public MobileStat getMax() {
        return max;
    }

    public MobileStat getMin() {
        return min;
    }

    public MobileStat getTotals() {
        return totals;
    }

    public List<MobileStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(MobileStat.class, "response");
    }

    public List<DeviceTypeStat> getDeviceTypes() {
        if (groupStats != null) return groupStats;
        return groupStats = getMany(DeviceTypeStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for (MobileStat os : stats) {
            os.setResponse(this);
            os.save();
        }
        for (DeviceTypeStat os : groupStats) {
            os.setResponse(this);
            os.save();
        }
        save();
    }

    public static void clearResponses() {
        List<MobileStatResponse> list = new Select().from(MobileStatResponse.class).execute();
        for (MobileStatResponse response : list) {
            MobileStat max = response.getMax();
            MobileStat min = response.getMin();
            MobileStat totals = response.getTotals();
            List<MobileStat> items = response.getStats();
            for (MobileStat item : items) {
                item.delete();
            }
            List<DeviceTypeStat> items2 = response.getDeviceTypes();
            for (DeviceTypeStat item : items2) {
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
