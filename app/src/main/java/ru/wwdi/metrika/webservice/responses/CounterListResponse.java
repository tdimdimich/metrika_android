package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.Counter;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Table(name = "CounterListResponse")
public class CounterListResponse extends BaseResponse {

    @SerializedName("counters")
    @Expose
    private List<Counter>counters;

    public CounterListResponse(){
        super();
    }

    @Override
    public void afterInitFromWeb() {
        super.afterInitFromWeb();
        if(hasErrors()) return;
        CounterListResponse last = new Select().from(CounterListResponse.class).executeSingle();

        //save new response
        save();
        for(Counter c: counters){
            c.setCountersList(this);
            c.save();
        }

        if(last==null) return;

        //merge new response with old
        Counter selectedCounter = SharedPreferencesHelper.getSelectedCounter();
        List<Counter> newCounters = counters;
        List<Counter> oldCounters = last.getCounters();
        Counter oldCounter;
        for(Counter newCounter: newCounters){
            oldCounter = findCounter(oldCounters, newCounter.getCounterId());
            if(oldCounter!=null){
                if(selectedCounter!=null && oldCounter.getCounterId().equals(selectedCounter.getCounterId())){
                    SharedPreferencesHelper.setSelectedCounter(newCounter);
                }
                newCounter.merge(oldCounter);
                oldCounters.remove(oldCounter);
                oldCounter.delete();
            }
        }
        for(Counter c: oldCounters){
            if(selectedCounter!=null && c.getCounterId().equals(selectedCounter.getCounterId())){
                if(newCounters.size()>0) SharedPreferencesHelper.setSelectedCounter(newCounters.get(0));
            }
            c.delete();
        }
        last.delete();
    }

    private Counter findCounter(List<Counter> counters, long counterId){
        for(int i=0;i<counters.size();i++){
            if(counters.get(i).getCounterId()==counterId){
                return counters.get(i);
            }
        }
        return null;
    }

    public static void clearResponses() {
        List<CounterListResponse> list = new Select().from(CounterListResponse.class).execute();
        for (CounterListResponse response : list) {
            for (Counter counter : response.getCounters()) {
                counter.delete();
            }
            response.delete();
        }
    }

    public List<Counter> getCounters() {
        if(counters!=null) return counters;
        return counters=getMany(Counter.class, "response");
    }

}
