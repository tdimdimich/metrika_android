package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.JavaStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "JavaStatResponse")
public class JavaStatResponse extends BaseResponse {

    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private JavaStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private JavaStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private JavaStat totals;

    @SerializedName("data")
    @Expose
    private List<JavaStat> stats;



    public JavaStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(JavaStat max) {
        this.max = max;
    }

    public void setMin(JavaStat min) {
        this.min = min;
    }

    public void setTotals(JavaStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public JavaStat getMax() {
        return max;
    }

    public JavaStat getMin() {
        return min;
    }

    public JavaStat getTotals() {
        return totals;
    }

    public List<JavaStat> getStats() {
        if(stats !=null) return stats;
        return stats =getMany(JavaStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for(JavaStat os: stats){
            os.setResponse(this);
            os.save();
        }
        save();
    }

    public static void clearResponses() {
        List<JavaStatResponse> list = new Select().from(JavaStatResponse.class).execute();
        for(JavaStatResponse response: list){
            JavaStat max = response.getMax();
            JavaStat min = response.getMin();
            JavaStat totals = response.getTotals();
            List<JavaStat> items = response.getStats();
            for(JavaStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
