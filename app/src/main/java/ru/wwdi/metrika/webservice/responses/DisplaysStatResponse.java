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
import ru.wwdi.metrika.models.DisplayGroupStat;
import ru.wwdi.metrika.models.DisplaySizeStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "DisplaysStatResponse")
public class DisplaysStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private DisplayGroupStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private DisplayGroupStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private DisplayGroupStat totals;

    @SerializedName("data_group")
    @Expose
    private List<DisplayGroupStat> groupStats;

    @SerializedName("data")
    @Expose
    private List<DisplaySizeStat> stats;



    public DisplaysStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(DisplayGroupStat max) {
        this.max = max;
    }

    public void setMin(DisplayGroupStat min) {
        this.min = min;
    }

    public void setTotals(DisplayGroupStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public DisplayGroupStat getMax() {
        return max;
    }

    public DisplayGroupStat getMin() {
        return min;
    }

    public DisplayGroupStat getTotals() {
        return totals;
    }

    public List<DisplayGroupStat> getGroupStats() {
        if(groupStats !=null) return groupStats;
        return groupStats = getMany(DisplayGroupStat.class, "response");
    }

    public List<DisplaySizeStat> getStats() {
        if(stats !=null) return stats;
        return stats = getMany(DisplaySizeStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        totals.save();
        min.save();
        max.save();
        save();
        for(DisplayGroupStat stat: groupStats){
            stat.setResponse(this);
            stat.save();
        }

        int length = stats.size()>10?10:stats.size();
        stats = stats.subList(0, length);

        String format;
        String name = format = YandexMetrikaApplication.getInstance().getString(R.string.others);
        int visits=0;
        int views=0;
        float denial=0;
        float depth=0;
        int visitTime=0;
        for(int i=0;i<stats.size();i++){
            visits+=stats.get(i).getVisits();
            views+=stats.get(i).getPageViews();
            denial+=stats.get(i).getVisits()*stats.get(i).getDenial();
            depth+=stats.get(i).getVisits()*stats.get(i).getDepth();
            visitTime+=stats.get(i).getVisits()*stats.get(i).getVisitTime();
        }

        visits = totals.getVisits() - visits;
        views = totals.getPageViews() - views;
        denial = (totals.getDenial()*totals.getVisits() - denial)/visits;
        depth = (totals.getDepth()*totals.getVisits() - depth)/visits;
        visitTime = (int) ((totals.getVisitTime()*totals.getVisits() - visitTime)/visits);

        DisplaySizeStat other = new DisplaySizeStat(format, 0, 0, depth, name, visitTime, views, visits, denial);
        other.setResponse(this);

        stats.add(other);

        for(DisplaySizeStat stat: stats){
            stat.setResponse(this);
            stat.save();
        }
        save();
    }

    public static void clearResponses() {
        List<DisplaysStatResponse> list = new Select().from(DisplaysStatResponse.class).execute();
        for(DisplaysStatResponse response: list){
            DisplayGroupStat max = response.getMax();
            DisplayGroupStat min = response.getMin();
            DisplayGroupStat totals = response.getTotals();
            List<DisplayGroupStat> items = response.getGroupStats();
            for(DisplayGroupStat item: items){
                item.delete();
            }
            List<DisplaySizeStat> items2 = response.getStats();
            for(DisplaySizeStat item: items2){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
