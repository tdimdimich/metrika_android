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
import ru.wwdi.metrika.models.URLParamsStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "URLParamsStatResponse")
public class URLParamsStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private URLParamsStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private URLParamsStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private URLParamsStat totals;

    @SerializedName("data")
    @Expose
    private List<URLParamsStat> stats;



    public URLParamsStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(URLParamsStat max) {
        this.max = max;
    }

    public void setMin(URLParamsStat min) {
        this.min = min;
    }

    public void setTotals(URLParamsStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public URLParamsStat getMax() {
        return max;
    }

    public URLParamsStat getMin() {
        return min;
    }

    public URLParamsStat getTotals() {
        return totals;
    }

    public List<URLParamsStat> getStats() {
        if(stats!=null) return stats;
        return stats=getMany(URLParamsStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        totals.save();
        min.save();
        max.save();
        save();


        int length = stats.size()>10?10:stats.size();
        stats = stats.subList(0, length);

        String format;
        String name = format = YandexMetrikaApplication.getInstance().getString(R.string.others);
        int views=0;
        for(int i=0;i<stats.size();i++){
            views+=stats.get(i).getPageViews();
        }

        views = totals.getPageViews() - views;
        URLParamsStat other = new URLParamsStat(format, views);
        other.setResponse(this);
        stats.add(other);


        for(URLParamsStat stat: stats){
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<URLParamsStatResponse> list = new Select().from(URLParamsStatResponse.class).execute();
        for(URLParamsStatResponse response: list){
            URLParamsStat max = response.getMax();
            URLParamsStat min = response.getMin();
            URLParamsStat totals = response.getTotals();
            List<URLParamsStat> items = response.getStats();
            for(URLParamsStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
