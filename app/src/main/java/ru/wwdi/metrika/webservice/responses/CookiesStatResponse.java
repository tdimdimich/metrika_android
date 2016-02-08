package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.models.CookiesStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "CookiesStatResponse")
public class CookiesStatResponse extends BaseResponse {

    @Column(name = "startDate", index = true)
    private Date startDate;

    @Column(name = "endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private CookiesStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private CookiesStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private CookiesStat totals;

    @SerializedName("data")
    @Expose
    private List<CookiesStat> stats;


    public CookiesStatResponse() {
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(CookiesStat max) {
        this.max = max;
    }

    public void setMin(CookiesStat min) {
        this.min = min;
    }

    public void setTotals(CookiesStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public CookiesStat getMax() {
        return max;
    }

    public CookiesStat getMin() {
        return min;
    }

    public CookiesStat getTotals() {
        return totals;
    }

    public List<CookiesStat> getStats() {
        if (stats != null) return stats;
        return stats = getMany(CookiesStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if (hasErrors()) return;

        totals.save();
        min.save();
        max.save();
        save();

        if (stats.size() == 1) {
            CookiesStat stat = new CookiesStat(this, 0f, YandexMetrikaApplication.getInstance().getString(R.string.no_cookies), 0, 0, 0, 0f);
            stats.add(stat);
        }

        for (CookiesStat stat : stats) {
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<CookiesStatResponse> list = new Select().from(CookiesStatResponse.class).execute();
        for (CookiesStatResponse response : list) {
            CookiesStat max = response.getMax();
            CookiesStat min = response.getMin();
            CookiesStat totals = response.getTotals();
            List<CookiesStat> items = response.getStats();
            for (CookiesStat item : items) {
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
