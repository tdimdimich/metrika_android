package ru.wwdi.metrika.models;

import java.util.Date;

/**
 * Created by ryashentsev on 03.05.14.
 */
public class DateInterval {

    private Date mStartDate;
    private Date mEndDate;

    public DateInterval(Date startDate, Date endDate){
        mStartDate = startDate;
        mEndDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if(o==null) return false;
        if(!(o instanceof DateInterval)) return false;
        DateInterval dateInterval = (DateInterval)o;
        if(mStartDate==null || mEndDate==null || dateInterval.getStartDate()==null || dateInterval.getEndDate()==null) return false;
        return mStartDate.equals(dateInterval.getStartDate()) && mEndDate.equals(dateInterval.getEndDate());
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }
}
