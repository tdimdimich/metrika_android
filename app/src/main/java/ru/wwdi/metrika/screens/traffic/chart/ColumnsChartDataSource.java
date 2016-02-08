package ru.wwdi.metrika.screens.traffic.chart;

import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.models.TrafficSummaryStat;

/**
 * Created by ryashentsev on 27.05.14.
 */
public class ColumnsChartDataSource extends ChartDataSource {

    private float mMin = 0;
    private float mMax = 0;
    private List<List<Float>> mPoints;

    public ColumnsChartDataSource() {
    }

    @Override
    protected void recomputePoints() {
        mMax = 0;
        List<TrafficSummaryStat> partData;
        TrafficSummaryStat stat;
        List<Float> point;
        Float val;
        mPoints = new ArrayList<List<Float>>();
        for(int i=0;i<mPartsData.size();i++){
            point = new ArrayList<Float>();
            partData = mPartsData.get(i);
            for(int j=0;j<partData.size();j++){
                stat = partData.get(j);
                val = getValueFromSingleStatObject(stat, mDataType);
                mMax = Math.max(mMax, val);
                point.add(val);
            }
            mPoints.add(point);
        }
        notifyDataSetChanged();
    }

    public List<List<Float>> getColumns(){
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
