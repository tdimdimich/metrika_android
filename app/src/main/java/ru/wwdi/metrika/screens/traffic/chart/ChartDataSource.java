package ru.wwdi.metrika.screens.traffic.chart;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.models.TrafficSummaryStat;
import ru.wwdi.metrika.screens.traffic.TrafficScreen;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 27.05.14.
 */
public abstract class ChartDataSource {

    public static interface ChartDataSourceListener{
        void onDataChanged();
    }

    protected TrafficSummaryStatResponse mData;
    protected TrafficScreen.DataType mDataType;
    protected List<List<TrafficSummaryStat>> mPartsData;
    protected List<DateInterval> mDateIntervals;
    private Set<ChartDataSourceListener> mListeners = new HashSet<ChartDataSourceListener>();
    private boolean mIsInited = false;

    public ChartDataSource(){
    }

    public boolean isInited(){
        return  mIsInited;
    }

    public void addListener(ChartDataSourceListener listener){
        mListeners.add(listener);
    }

    public void removeListener(ChartDataSourceListener listener){
        mListeners.add(listener);
    }

    public void setData(TrafficSummaryStatResponse data){
        mData = data;

        //формируем все необходимые данные: интервалы даты и значения для каждой точки. Точек может быть от 0 до 6
        mDateIntervals = new ArrayList<DateInterval>();
        mPartsData = new ArrayList<List<TrafficSummaryStat>>();
        List<TrafficSummaryStat> partData;
        for(int i=0;i<6;i++){
            partData = getDataForPart(data, i);
            if(partData==null) break;
            mPartsData.add(getDataForPart(data, i));
            mDateIntervals.add(getDateIntervalOfSublineData(mPartsData.get(i)));
        }
        if(mDataType!=null){
            recomputePoints();
        }
        mIsInited = true;
    }

    public void setDataType(TrafficScreen.DataType type){
        mDataType = type;
        if(mData!=null){
            recomputePoints();
        }
    }

    protected float getValueFromSingleStatObject(TrafficSummaryStat data, TrafficScreen.DataType dataType){
        if(dataType== TrafficScreen.DataType.VISITS){
            return data.getVisits();
        }else if(dataType== TrafficScreen.DataType.VISITORS){
            return data.getVisitors();
        }else if(dataType== TrafficScreen.DataType.NEW_VISITORS){
            return data.getNewVisitors();
        }else if(dataType== TrafficScreen.DataType.VIEWS){
            return data.getPageViews();
        }else if(dataType== TrafficScreen.DataType.DENIAL){
            return data.getDenial();
        }else if(dataType== TrafficScreen.DataType.VIEW_DEPTH){
            return data.getDepth();
        }else if(dataType== TrafficScreen.DataType.VISIT_TIME){
            return data.getVisitTime();
        }
        return 0;
    }

    protected abstract void recomputePoints();

    public List<DateInterval> getDateIntervals(){
        return mDateIntervals;
    }

    public abstract float getMinValue();

    public abstract float getMaxValue();

    private DateInterval getDateIntervalOfSublineData(List<TrafficSummaryStat> sublineData){
        if(sublineData.size()==0) return null;
        Date startDate=null;
        Date endDate=null;
        for(TrafficSummaryStat stat: sublineData){
            if(endDate==null || endDate.getTime()<stat.getDate().getTime()){
                endDate = stat.getDate();
            }
            if(startDate==null || startDate.getTime()>stat.getDate().getTime()){
                startDate = stat.getDate();
            }
        }
        return new DateInterval(startDate, endDate);
    }

    private int getPartSize(int totalSize, int position){
        if(position>=totalSize) return 0;
        if(totalSize<=6) return 1;
        int partSize = totalSize/6;
        int delta = totalSize - partSize*6;
        if(delta>position) partSize++;
        return partSize;
    }

    private List<TrafficSummaryStat> getDataForPart(TrafficSummaryStatResponse totalData, int position){
        int size = getPartSize(totalData.getTrafficSummaries().size(), position);
        if(size==0) return null;
        int startPosition=0;
        for(int i=0;i<position;i++){
            startPosition += getPartSize(totalData.getTrafficSummaries().size(), i);
        }
        ArrayList<TrafficSummaryStat> result = new ArrayList<TrafficSummaryStat>();
        for(int i=startPosition;i<startPosition+size;i++){
            result.add(totalData.getTrafficSummaries().get(i));
        }
        return result;
    }

    protected void notifyDataSetChanged(){
        for(ChartDataSourceListener listener: mListeners){
            listener.onDataChanged();
        }
    }


}
