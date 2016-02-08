package ru.wwdi.metrika.screens.traffic.chart;

import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.models.TrafficSummaryStat;
import ru.wwdi.metrika.screens.traffic.TrafficScreen;

/**
 * Created by ryashentsev on 27.05.14.
 */
public class PointsChartDataSource extends ChartDataSource {

    private float mMin = 0;
    private float mMax = 0;
    private List<Float> mPoints;

    public PointsChartDataSource() {
    }

    @Override
    protected void recomputePoints() {
        List<TrafficSummaryStat> partData;
        TrafficSummaryStat stat;
        float point;
        Float val;
        int visitsForPoint;
        mPoints = new ArrayList<Float>();
        mMin = Float.MAX_VALUE;
        mMax = 0;
        for(int i=0;i<mPartsData.size();i++){
            point = 0;
            visitsForPoint = 0;
            partData = mPartsData.get(i);
            for(int j=0;j<partData.size();j++){
                stat = partData.get(j);
                visitsForPoint += stat.getVisits();
                val = getValueFromSingleStatObject(stat, mDataType);
                if(mDataType==TrafficScreen.DataType.DENIAL ||
                        mDataType==TrafficScreen.DataType.VIEW_DEPTH ||
                        mDataType==TrafficScreen.DataType.VISIT_TIME) val*=stat.getVisits();
                point+=val;
            }
            if(mDataType==TrafficScreen.DataType.DENIAL ||
                    mDataType==TrafficScreen.DataType.VIEW_DEPTH ||
                    mDataType==TrafficScreen.DataType.VISIT_TIME){
                if(visitsForPoint>0) point/=visitsForPoint;
            }
            mMax = Math.max(mMax, point);
            mMin = Math.min(mMin, point);
            mPoints.add(point);
        }
        notifyDataSetChanged();
    }

    public List<Float> getPoints(){
        return mPoints;
    }

    @Override
    public float getMinValue() {
        return mMin;
    }

    @Override
    public float getMaxValue() {
        return mMax;
    }

}
