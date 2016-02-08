package ru.wwdi.metrika;

import com.crashlytics.android.Crashlytics;
import com.google.analytics.tracking.android.ExceptionReporter;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import io.fabric.sdk.android.Fabric;

public class YandexMetrikaApplication extends com.activeandroid.app.Application{
	public static final String APP_ID = BuildConfig.METRICA_KEY;
	public static final String URL = "http://api-metrika.yandex.ru";

	
	private static YandexMetrikaApplication sInstance;
    private static Tracker sTracker;

	@Override
	public void onCreate() {
        if (APP_ID == null) {
            throw new RuntimeException("You should to set Yandex Metrica API key to local.properties METRICA_KEY");
        }
        super.onCreate();
		Fabric.with(this, new Crashlytics());
	    sInstance = this;
    }

	public static YandexMetrikaApplication getInstance() {
		return sInstance;
	}

    private static Tracker getTracker(){
        if(sTracker==null){
            sTracker = GoogleAnalytics.getInstance(sInstance).getTracker(BuildConfig.GoogleAnalytics_KEY);

            ExceptionReporter handler = new ExceptionReporter(
                    sTracker,                                        // Currently used Tracker.
                    GAServiceManager.getInstance(),                   // GAServiceManager singleton.
                    Thread.getDefaultUncaughtExceptionHandler(),
                    sInstance);     // Current default uncaught exception handler.

            // Make handler the new default uncaught exception handler for main(GUI) thread.
            Thread.setDefaultUncaughtExceptionHandler(handler);

        }
        return sTracker;
    }

    public static void sendEvent(String eventName){
        android.util.Log.d(YandexMetrikaApplication.class.getSimpleName(), "sendEvent "+eventName);
        Tracker tracker = getTracker();
        if(tracker!=null) {
            tracker.send(MapBuilder.createEvent("mm_android", eventName, null, null).build());
        }
    }
	
//	public String getErrorMessage(String errorCode) {
//		return "";
//	}
//
//	public static final int ERR_NO_SUCH_METHOD					= 1001;
//	public static final int ERR_PARAM_REQUIRED					= 1002;
//	public static final int ERR_READ_ONLY						= 1003;
//	public static final int ERR_EMAIL							= 1004;
//	public static final int ERR_EMAIL_EMPTY						= 1005;
//	public static final int ERR_MIRROR							= 1006;
//	public static final int ERR_MIRROR_EXISTS					= 1007;
//	public static final int ERR_SITE							= 1008;
//	public static final int ERR_SITE_EXISTS						= 1009;
//	public static final int ERR_SITE_NAME						= 1010;
//	public static final int ERR_SITE_NAME_EXISTS				= 1011;
//	public static final int ERR_CODE_INFORMER_COLOR_ARROW		= 1012;
//	public static final int ERR_CODE_INFORMER_COLOR_END			= 1013;
//	public static final int ERR_CODE_INFORMER_COLOR_START		= 1014;
//	public static final int ERR_CODE_INFORMER_COLOR_TEXT		= 1015;
//	public static final int RR_CODE_INFORMER_INDICATOR			= 1016;
//	public static final int ERR_CODE_INFORMER_SIZE				= 1017;
//	public static final int ERR_CODE_INFORMER_TYPE				= 1018;
//	public static final int ERR_GOAL_CONDITIONS_EMPTY			= 1019;
//	public static final int ERR_GOAL_CONDITIONS_LIMIT			= 1020;
//	public static final int ERR_GOAL_CONDITION_TYPE				= 1021;
//	public static final int ERR_GOAL_CONDITION_TYPE_EMPTY		= 1022;
//	public static final int ERR_GOAL_CONDITION_URL				= 1023;
//	public static final int ERR_GOAL_CONDITION_URL_EMPTY		= 1024;
//	public static final int ERR_GOAL_DEPTH						= 1025;
//	public static final int ERR_GOAL_DEPTH_EMPTY				= 1026;
//	public static final int ERR_GOAL_DUPLICATED					= 1027;
//	public static final int ERR_GOAL_FLAG						= 1028;
//	public static final int ERR_GOAL_FLAG_LIMIT					= 1029;
//	public static final int ERR_GOAL_NAME_EMPTY					= 1030;
//	public static final int ERR_GOAL_TYPE						= 1031;
//	public static final int ERR_GOAL_TYPE_EMPTY					= 1032;
//	public static final int ERR_GOALS_LIMIT						= 1033;
//	public static final int ERR_FILTER_ACTION					= 1034;
//	public static final int ERR_FILTER_ACTION_EMPTY				= 1035;
//	public static final int ERR_FILTER_ATTR						= 1036;
//	public static final int ERR_FILTER_ATTR_EMPTY				= 1037;
//	public static final int ERR_FILTER_IP						= 1038;
//	public static final int ERR_FILTER_IP_TYPE					= 1039;
//	public static final int ERR_FILTER_IP_EQUAL					= 1040;
//	public static final int ERR_FILTER_MIRRORS_ACTION			= 1041;
//	public static final int ERR_FILTER_REF_TYPE					= 1042;
//	public static final int ERR_FILTER_STATUS					= 1043;
//	public static final int ERR_FILTER_TITLE_TYPE				= 1044;
//	public static final int ERR_FILTER_TYPE						= 1045;
//	public static final int ERR_FILTER_TYPE_EMPTY				= 1046;
//	public static final int ERR_FILTER_UNIQ_ID_ACTION			= 1047;
//	public static final int ERR_FILTER_URL_TYPE					= 1048;
//	public static final int ERR_FILTER_VALUE_EMPTY				= 1049;
//	public static final int ERR_FILTERS_LIMIT					= 1050;
//	public static final int ERR_OPERATION_ACTION				= 1051;
//	public static final int ERR_OPERATION_ACTION_EMPTY			= 1052;
//	public static final int ERR_OPERATION_ATTR					= 1053;
//	public static final int ERR_OPERATION_ATTR_EMPTY			= 1054;
//	public static final int ERR_OPERATION_STATUS				= 1055;
//	public static final int ERR_OPERATION_VALUE_EMPTY			= 1056;
//	public static final int ERR_OPERATIONS_LIMIT				= 1057;
//	public static final int ERR_DELEGATE_LOGIN_MYSELF			= 1058;
//	public static final int ERR_DELEGATE_LOGIN_NOT_EXISTS		= 1059;
//	public static final int ERR_DELEGATES_LIMIT					= 1060;
//	public static final int ERR_GRANT_LOGIN_MYSELF				= 1061;
//	public static final int ERR_GRANT_LOGIN_NOT_EXISTS			= 1062;
//	public static final int ERR_GRANT_PERM						= 1063;
//	public static final int ERR_GRANTS_LIMIT					= 1064;
//	public static final int ERR_DATE_BEGIN						= 1065;
//	public static final int ERR_DATE_DELTA						= 1066;
//	public static final int ERR_DATE_END						= 1067;
//	public static final int ERR_NO_DATA							= 1068;
//
	
}


