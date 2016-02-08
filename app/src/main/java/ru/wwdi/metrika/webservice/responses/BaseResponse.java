package ru.wwdi.metrika.webservice.responses;

import com.activeandroid.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.wwdi.metrika.models.DataError;
import ru.wwdi.metrika.models.YandexError;

/**
 * Created with IntelliJ IDEA.
 * User: ryashentsev
 * Date: 17.05.13
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseResponse extends Model {

    private DataError mError;

    @Expose
    @SerializedName("errors")
    private List<YandexError> mYandexErrors;

    public BaseResponse(){
        super();
    }

    public void setError(DataError error){
        mError = error;
    }

    public boolean hasNoDataError(){
        return mYandexErrors!=null && mYandexErrors.size()>0 && mYandexErrors.get(0).getCode().equals("ERR_NO_DATA");
    }

    public boolean hasErrors(){
        return mError!=null || (mYandexErrors!=null && mYandexErrors.size()>0);
    }

    public DataError getError(){
        return mError;
    }

    public List<YandexError> getYandexErrors() {
        return mYandexErrors;
    }

    public void afterInitFromWeb(){
        if(mYandexErrors!=null && mYandexErrors.size()>0){
            mError = DataError.ERROR_YANDEX;
        }
    }
}
