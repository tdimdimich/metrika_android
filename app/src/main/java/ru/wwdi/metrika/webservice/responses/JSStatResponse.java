package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.JSStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "JSStatResponse")
public class JSStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private JSStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private JSStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private JSStat totals;

    @SerializedName("data")
    @Expose
    private List<JSStat> stats;


    public JSStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(JSStat max) {
        this.max = max;
    }

    public void setMin(JSStat min) {
        this.min = min;
    }

    public void setTotals(JSStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public JSStat getMax() {
        return max;
    }

    public JSStat getMin() {
        return min;
    }

    public JSStat getTotals() {
        return totals;
    }

    public List<JSStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(JSStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for (JSStat os : stats) {
            os.setResponse(this);
            os.save();
        }
        save();
    }

    public static void clearResponses() {
        List<JSStatResponse> list = new Select().from(JSStatResponse.class).execute();
        for (JSStatResponse response : list) {
            JSStat max = response.getMax();
            JSStat min = response.getMin();
            JSStat totals = response.getTotals();
            List<JSStat> items = response.getStats();
            for (JSStat item : items) {
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
