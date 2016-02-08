package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.models.SourcesSummaryStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "SourcesSummaryStatResponse")
public class SourcesSummaryStatResponse extends BaseResponse {

    /*
   "min" : {
      "denial" : 0,
      "visits" : 5,
      "page_views" : 14,
      "visits_delayed" : 1,
      "visit_time" : 84,
      "depth" : 1.724
   },
   "max" : {
      "denial" : 0.7246,
      "visits" : 19014,
      "page_views" : 32781,
      "visits_delayed" : 4521,
      "visit_time" : 4060,
      "depth" : 26.0145
   },
   "data" : [
      {
         "denial" : 0.5506,
         "visits" : 19014,
         "page_views" : 32781,
         "name" : "Прямые заходы",
         "visit_time" : 84,
         "depth" : 1.724,
         "visits_delayed" : 4521,
         "id" : "0"
      }
  ],
   "rows" : 7,
   "date1" : "20140430",
   "id" : "2138128",
   "goals" : [],
   "totals" : {
      "denial" : 0.4533,
      "visits" : 24702,
      "page_views" : 48114,
      "visits_delayed" : 7499,
      "visit_time" : 126,
      "depth" : 1.9478
   }
     */


    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private SourcesSummaryStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private SourcesSummaryStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private SourcesSummaryStat totals;

    @SerializedName("data")
    @Expose
    private List<SourcesSummaryStat> sourcesSummaries;



    public SourcesSummaryStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(SourcesSummaryStat max) {
        this.max = max;
    }

    public void setMin(SourcesSummaryStat min) {
        this.min = min;
    }

    public void setTotals(SourcesSummaryStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public SourcesSummaryStat getMax() {
        return max;
    }

    public SourcesSummaryStat getMin() {
        return min;
    }

    public SourcesSummaryStat getTotals() {
        return totals;
    }

    public List<SourcesSummaryStat> getSourceSummaries() {
        if(sourcesSummaries!=null) return sourcesSummaries;
        return sourcesSummaries=getMany(SourcesSummaryStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        save();
        totals.save();
        min.save();
        max.save();
        for(SourcesSummaryStat summary: sourcesSummaries){
            summary.setResponse(this);
            summary.save();
        }
        save();
    }


//    public static SourcesSummaryStatResponse getResponse() {
//        DateInterval dateInterval = SharedPreferencesHelper.getCurrentDateInterval();
//        Date startDate = dateInterval.getStartDate();
//        Date endDate = dateInterval.getEndDate();
//        return new Select().from(SourcesSummaryStatResponse.class).where("startDate='" + startDate.getTime() + "'").and("endDate='" + endDate.getTime() + "'").executeSingle();
//    }
//
//    public static void clearResponse(Date startDate, Date endDate) {
//        SourcesSummaryStatResponse response = new Select().from(SourcesSummaryStatResponse.class).where("startDate='" + startDate.getTime() + "'").and("endDate='" + endDate.getTime() + "'").executeSingle();
//        if(response==null) return;
//        SourcesSummaryStat max = response.getMax();
//        SourcesSummaryStat min = response.getMin();
//        SourcesSummaryStat totals = response.getTotals();
//        List<SourcesSummaryStat> items = response.getSourceSummaries();
//        for(SourcesSummaryStat item: items){
//            item.delete();
//        }
//        response.delete();
//        max.delete();
//        min.delete();
//        totals.delete();
//    }

    public static void clearResponses() {
        List<SourcesSummaryStatResponse> list = new Select().from(SourcesSummaryStatResponse.class).execute();
        for(SourcesSummaryStatResponse response: list){
            SourcesSummaryStat max = response.getMax();
            SourcesSummaryStat min = response.getMin();
            SourcesSummaryStat totals = response.getTotals();
            List<SourcesSummaryStat> items = response.getSourceSummaries();
            for(SourcesSummaryStat item: items){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
