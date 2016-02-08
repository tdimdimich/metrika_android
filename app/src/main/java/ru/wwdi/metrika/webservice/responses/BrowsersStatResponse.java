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

import ru.wwdi.metrika.models.BrowserStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "BrowsersStatResponse")
public class BrowsersStatResponse extends BaseResponse {

    /*
   "min" : {
      "denial" : 0,
      "visits" : 1,
      "page_views" : 1,
      "visit_time" : 0,
      "depth" : 1
   },
   "max" : {
      "denial" : 1,
      "visits" : 19146,
      "page_views" : 35543,
      "visit_time" : 1393,
      "depth" : 7
   },
   "data" : [
      {
         "denial" : 0.5013,
         "visits" : 19146,
         "region_type" : "country",
         "page_views" : 35543,
         "name" : "Россия",
         "visit_time" : 114,
         "depth" : 1.8564,
         "id" : "225"
      }
   ],
   "rows" : 75,
   "date1" : "20140501",
   "id" : "2138128",
   "goals" : [],
   "totals" : {
      "denial" : 0.465,
      "visits" : 22448,
      "page_views" : 42601,
      "visit_time" : 120,
      "depth" : 1.8978
   }
     */


    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private BrowserStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private BrowserStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private BrowserStat totals;

    @SerializedName("data")
    @Expose
    private List<BrowserStat> browserStats;



    public BrowsersStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(BrowserStat max) {
        this.max = max;
    }

    public void setMin(BrowserStat min) {
        this.min = min;
    }

    public void setTotals(BrowserStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public BrowserStat getMax() {
        return max;
    }

    public BrowserStat getMin() {
        return min;
    }

    public BrowserStat getTotals() {
        return totals;
    }

    public List<BrowserStat> getStats() {
        if(browserStats!=null) return browserStats;
        return browserStats=getMany(BrowserStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;
        Collections.sort(browserStats, new Comparator<BrowserStat>() {
            @Override
            public int compare(BrowserStat lhs, BrowserStat rhs) {
                if (lhs.getVisits() > rhs.getVisits()) return -1;
                if (lhs.getVisits() < rhs.getVisits()) return 1;
                return 0;
            }
        });

        totals.save();
        min.save();
        max.save();
        save();
        for(BrowserStat stat: browserStats){
            stat.setResponse(this);
            stat.save();
        }
    }

    public static void clearResponses() {
        List<BrowsersStatResponse> list = new Select().from(BrowsersStatResponse.class).execute();
        for(BrowsersStatResponse response: list){
            BrowserStat max = response.getMax();
            BrowserStat min = response.getMin();
            BrowserStat totals = response.getTotals();
            List<BrowserStat> items = response.getStats();
            for(BrowserStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
