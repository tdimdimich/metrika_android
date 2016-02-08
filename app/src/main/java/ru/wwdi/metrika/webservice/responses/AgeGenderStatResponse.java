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

import ru.wwdi.metrika.models.AgeStat;
import ru.wwdi.metrika.models.GenderStat;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "AgeGenderStatResponse")
public class AgeGenderStatResponse extends BaseResponse {

    /*
   "data_gender" : [
      {
         "denial" : 0.1814,
         "visits_percent" : 0.825,
         "name" : "мужской",
         "id" : "1",
         "visit_time" : 196,
         "depth" : 2.3836
      },
      {
         "denial" : 0.2405,
         "visits_percent" : 0.175,
         "name" : "женский",
         "id" : "2",
         "visit_time" : 144,
         "depth" : 2.1356
      }
   ],
   "min" : {
      "denial" : 0.177,
      "visits_percent" : 0.015,
      "visit_time" : 94,
      "depth" : 1.7143
   },
   "max" : {
      "denial" : 0.2704,
      "visits_percent" : 0.63,
      "visit_time" : 200,
      "depth" : 2.4055
   },
   "data" : [
      {
         "denial" : 0.2704,
         "visits_percent" : 0.015,
         "name" : "младше 18 лет",
         "id" : "17",
         "visit_time" : 94,
         "depth" : 1.7143
      }
   ],
   "rows" : 5,
   "date1" : "20140501",
   "id" : "2138128",
   "subset_share" : 0.578,
   "goals" : [],
   "totals" : {
      "denial" : 0.1928,
      "visit_time" : 186,
      "depth" : 2.334
   }
     */


    @Column(name="startDate", index = true)
    private Date startDate;

    @Column(name="endDate", index = true)
    private Date endDate;

    @SerializedName("max")
    @Expose
    @Column(name = "max")
    private GenderStat max;

    @SerializedName("min")
    @Expose
    @Column(name = "min")
    private GenderStat min;

    @SerializedName("totals")
    @Expose
    @Column(name = "totals")
    private GenderStat totals;

    @SerializedName("data")
    @Expose
    private List<AgeStat> ageStats;

    @SerializedName("data_gender")
    @Expose
    private List<GenderStat> genderStats;



    public AgeGenderStatResponse(){
        super();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMax(GenderStat max) {
        this.max = max;
    }

    public void setMin(GenderStat min) {
        this.min = min;
    }

    public void setTotals(GenderStat totals) {
        this.totals = totals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public GenderStat getMax() {
        return max;
    }

    public GenderStat getMin() {
        return min;
    }

    public GenderStat getTotals() {
        return totals;
    }

    public List<AgeStat> getAgeStats() {
        if(ageStats!=null) return ageStats;
        return ageStats=getMany(AgeStat.class, "response");
    }

    public List<GenderStat> getGenderStats() {
        if(genderStats!=null) return genderStats;
        return genderStats=getMany(GenderStat.class, "response");
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;

        Collections.sort(ageStats, new Comparator<AgeStat>() {
            @Override
            public int compare(AgeStat lhs, AgeStat rhs) {
                if(lhs.getVisitsPercent()>rhs.getVisitsPercent()) return -1;
                if(lhs.getVisitsPercent()<rhs.getVisitsPercent()) return 1;
                return 0;
            }
        });
        Collections.sort(genderStats, new Comparator<GenderStat>() {
            @Override
            public int compare(GenderStat lhs, GenderStat rhs) {
                if(lhs.getVisitsPercent()>rhs.getVisitsPercent()) return -1;
                if(lhs.getVisitsPercent()<rhs.getVisitsPercent()) return 1;
                return 0;
            }
        });

        totals.save();
        min.save();
        max.save();
        save();
        for(AgeStat ageStat : ageStats){
            ageStat.setResponse(this);
            ageStat.save();
        }
        for(GenderStat genderStat : genderStats){
            genderStat.setResponse(this);
            genderStat.save();
        }
    }

//    public static AgeGenderStatResponse getResponse() {
//        DateInterval dateInterval = SharedPreferencesHelper.getCurrentDateInterval();
//        Date startDate = dateInterval.getStartDate();
//        Date endDate = dateInterval.getEndDate();
//        return new Select().from(AgeGenderStatResponse.class).where("startDate='" + startDate.getTime() + "'").and("endDate='" + endDate.getTime() + "'").executeSingle();
//    }
//
//    public static void clearResponse(Date startDate, Date endDate) {
//        AgeGenderStatResponse response = new Select().from(AgeGenderStatResponse.class).where("startDate='" + startDate.getTime() + "'").and("endDate='" + endDate.getTime() + "'").executeSingle();
//        if(response==null) return;
//        GenderStat max = response.getMax();
//        GenderStat min = response.getMin();
//        GenderStat totals = response.getTotals();
//        List<AgeStat> itemsA = response.getAgeStats();
//        for(AgeStat item: itemsA){
//            item.delete();
//        }
//        List<GenderStat> itemsG = response.getGenderStats();
//        for(GenderStat item: itemsG){
//            item.delete();
//        }
//        response.delete();
//        max.delete();
//        min.delete();
//        totals.delete();
//    }

    public static void clearResponses(){
        List<AgeGenderStatResponse> list = new Select().from(AgeGenderStatResponse.class).execute();
        for(AgeGenderStatResponse response: list){
            GenderStat max = response.getMax();
            GenderStat min = response.getMin();
            GenderStat totals = response.getTotals();
            List<AgeStat> itemsA = response.getAgeStats();
            for(AgeStat item: itemsA){
                item.delete();
            }
            List<GenderStat> itemsG = response.getGenderStats();
            for(GenderStat item: itemsG){
                item.delete();
            }
            response.delete();
            max.delete();
            min.delete();
            totals.delete();
        }
    }

}
