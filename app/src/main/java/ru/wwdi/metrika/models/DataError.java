package ru.wwdi.metrika.models;

/**
 * Created with IntelliJ IDEA.
 * User: ryashentsev
 * Date: 06.03.13
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public class DataError {

    public static final DataError ERROR_CANT_PARSE_JSON = new DataError(-1, "Can't parse json");
    public static final DataError ERROR_NO_CONNECTION = new DataError(-2, "No connection");
    public static final DataError ERROR_UNKNOWN = new DataError(-3, "Unknown error");
    public static final DataError ERROR_YANDEX = new DataError(-4, "Yandex error");

    private int mCode;
    private String mMessage;

    public DataError(int code, String message){
        mCode = code;
        mMessage = message;
    }

    public int getCode(){
        return mCode;
    }

    public String getMessage(){
        return mMessage;
    }

}
