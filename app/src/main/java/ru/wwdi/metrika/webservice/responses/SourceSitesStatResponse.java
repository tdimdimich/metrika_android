package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.SourceSitesStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "SourceSitesStatResponse")
public class SourceSitesStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private SourceSitesStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private SourceSitesStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private SourceSitesStat totals;

    @SerializedName("data")
    @Expose
    private List<SourceSitesStat> sourceSitesStats;



    public SourceSitesStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(SourceSitesStat max) {
        this.max = max;
    }

    public void setMin(SourceSitesStat min) {
        this.min = min;
    }

    public void setTotals(SourceSitesStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public SourceSitesStat getMax() {
        return max;
    }

    public SourceSitesStat getMin() {
        return min;
    }

    public SourceSitesStat getTotals() {
        return totals;
    }

    public List<SourceSitesStat> getSourceSiteStats() {
        if(sourceSitesStats!=null) return sourceSitesStats;
        return sourceSitesStats=getMany(SourceSitesStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for(SourceSitesStat stat: sourceSitesStats){
            stat.setResponse(this);
            stat.save();
        }
        save();
    }

    public static void clearResponses() {
        List<SourceSitesStatResponse> list = new Select().from(SourceSitesStatResponse.class).execute();
        for(SourceSitesStatResponse response: list){
            SourceSitesStat max = response.getMax();
            SourceSitesStat min = response.getMin();
            SourceSitesStat totals = response.getTotals();
            List<SourceSitesStat> items = response.getSourceSiteStats();
            for(SourceSitesStat item: items){
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
