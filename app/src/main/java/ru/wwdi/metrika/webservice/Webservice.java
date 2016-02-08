package ru.wwdi.metrika.webservice;

import ru.wwdi.metrika.webservice.requests.AgeGenderStatRequest;
import ru.wwdi.metrika.webservice.requests.AgeGenderStructureStatRequest;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.requests.BrowsersStatRequest;
import ru.wwdi.metrika.webservice.requests.CookiesStatRequest;
import ru.wwdi.metrika.webservice.requests.CounterListRequest;
import ru.wwdi.metrika.webservice.requests.DeepnessStatRequest;
import ru.wwdi.metrika.webservice.requests.DisplaysStatRequest;
import ru.wwdi.metrika.webservice.requests.EntrancePageStatRequest;
import ru.wwdi.metrika.webservice.requests.ExitPageStatRequest;
import ru.wwdi.metrika.webservice.requests.FlashStatRequest;
import ru.wwdi.metrika.webservice.requests.GeoStatRequest;
import ru.wwdi.metrika.webservice.requests.HourlyStatRequest;
import ru.wwdi.metrika.webservice.requests.JSStatRequest;
import ru.wwdi.metrika.webservice.requests.JavaStatRequest;
import ru.wwdi.metrika.webservice.requests.MobileStatRequest;
import ru.wwdi.metrika.webservice.requests.OSStatRequest;
import ru.wwdi.metrika.webservice.requests.PageTitleStatRequest;
import ru.wwdi.metrika.webservice.requests.PopularStatRequest;
import ru.wwdi.metrika.webservice.requests.SearchEnginesStatRequest;
import ru.wwdi.metrika.webservice.requests.SearchPhrasesStatRequest;
import ru.wwdi.metrika.webservice.requests.SilverlightStatRequest;
import ru.wwdi.metrika.webservice.requests.SourceSitesStatRequest;
import ru.wwdi.metrika.webservice.requests.SourcesSummaryStatRequest;
import ru.wwdi.metrika.webservice.requests.TrafficSummaryStatRequest;
import ru.wwdi.metrika.webservice.requests.URLParamsStatRequest;
import ru.wwdi.metrika.webservice.responses.AgeGenderStatResponse;
import ru.wwdi.metrika.webservice.responses.AgeGenderStructureStatResponse;
import ru.wwdi.metrika.webservice.responses.BrowsersStatResponse;
import ru.wwdi.metrika.webservice.responses.CookiesStatResponse;
import ru.wwdi.metrika.webservice.responses.CounterListResponse;
import ru.wwdi.metrika.webservice.responses.DeepnessStatResponse;
import ru.wwdi.metrika.webservice.responses.DisplaysStatResponse;
import ru.wwdi.metrika.webservice.responses.EntrancePageStatResponse;
import ru.wwdi.metrika.webservice.responses.ExitPageStatResponse;
import ru.wwdi.metrika.webservice.responses.FlashStatResponse;
import ru.wwdi.metrika.webservice.responses.GeoStatResponse;
import ru.wwdi.metrika.webservice.responses.HourlyStatResponse;
import ru.wwdi.metrika.webservice.responses.JSStatResponse;
import ru.wwdi.metrika.webservice.responses.JavaStatResponse;
import ru.wwdi.metrika.webservice.responses.MobileStatResponse;
import ru.wwdi.metrika.webservice.responses.OSStatResponse;
import ru.wwdi.metrika.webservice.responses.PageTitleStatResponse;
import ru.wwdi.metrika.webservice.responses.PopularStatResponse;
import ru.wwdi.metrika.webservice.responses.SearchEnginesStatResponse;
import ru.wwdi.metrika.webservice.responses.SearchPhrasesStatResponse;
import ru.wwdi.metrika.webservice.responses.SilverlightStatResponse;
import ru.wwdi.metrika.webservice.responses.SourceSitesStatResponse;
import ru.wwdi.metrika.webservice.responses.SourcesSummaryStatResponse;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;
import ru.wwdi.metrika.webservice.responses.URLParamsStatResponse;

/**
* Created with IntelliJ IDEA.
* User: ryashentsev
* Date: 17.05.13
* Time: 14:14
* To change this template use File | Settings | File Templates.
*/
public class Webservice {

    public static final String API_BASE_URL = "http://api-metrika.yandex.ru";

    public static CounterListRequest getCounterList(boolean useCache, BaseRequest.Listener<CounterListResponse> listener){
        CounterListRequest request = new CounterListRequest(useCache, listener);
        request.start();
        return request;
    }

    public static JSStatRequest getJSStat(boolean useCache, BaseRequest.Listener<JSStatResponse> listener){
        JSStatRequest request = new JSStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static CookiesStatRequest getCookiesStat(boolean useCache, BaseRequest.Listener<CookiesStatResponse> listener){
        CookiesStatRequest request = new CookiesStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static JavaStatRequest getJavaStat(boolean useCache, BaseRequest.Listener<JavaStatResponse> listener){
        JavaStatRequest request = new JavaStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static SilverlightStatRequest getSilverlightStat(boolean useCache, BaseRequest.Listener<SilverlightStatResponse> listener){
        SilverlightStatRequest request = new SilverlightStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static FlashStatRequest getFlashStat(boolean useCache, BaseRequest.Listener<FlashStatResponse> listener){
        FlashStatRequest request = new FlashStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static MobileStatRequest getMobileStat(boolean useCache, BaseRequest.Listener<MobileStatResponse> listener){
        MobileStatRequest request = new MobileStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static DisplaysStatRequest getDisplaysStat(boolean useCache, BaseRequest.Listener<DisplaysStatResponse> listener){
        DisplaysStatRequest request = new DisplaysStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static URLParamsStatRequest getURLParamsStat(boolean useCache, BaseRequest.Listener<URLParamsStatResponse> listener){
        URLParamsStatRequest request = new URLParamsStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static PageTitleStatRequest getPageTitleStat(boolean useCache, BaseRequest.Listener<PageTitleStatResponse> listener){
        PageTitleStatRequest request = new PageTitleStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static PopularStatRequest getPopularStat(boolean useCache, BaseRequest.Listener<PopularStatResponse> listener){
        PopularStatRequest request = new PopularStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static ExitPageStatRequest getExitPageStat(boolean useCache, BaseRequest.Listener<ExitPageStatResponse> listener){
        ExitPageStatRequest request = new ExitPageStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static EntrancePageStatRequest getEntrancePageStat(boolean useCache, BaseRequest.Listener<EntrancePageStatResponse> listener){
        EntrancePageStatRequest request = new EntrancePageStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static HourlyStatRequest getHourlyStat(boolean useCache, BaseRequest.Listener<HourlyStatResponse> listener){
        HourlyStatRequest request = new HourlyStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static DeepnessStatRequest getDeepnessStat(boolean useCache, BaseRequest.Listener<DeepnessStatResponse> listener){
        DeepnessStatRequest request = new DeepnessStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static AgeGenderStructureStatRequest getAgeGenderStructureStat(boolean useCache, BaseRequest.Listener<AgeGenderStructureStatResponse> listener){
        AgeGenderStructureStatRequest request = new AgeGenderStructureStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static SearchPhrasesStatRequest getSearchPhrasesStat(boolean useCache, BaseRequest.Listener<SearchPhrasesStatResponse> listener){
        SearchPhrasesStatRequest request = new SearchPhrasesStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static SearchEnginesStatRequest getSearchEnginesStat(boolean useCache, BaseRequest.Listener<SearchEnginesStatResponse> listener){
        SearchEnginesStatRequest request = new SearchEnginesStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static SourceSitesStatRequest getSourceSitesStat(boolean useCache, BaseRequest.Listener<SourceSitesStatResponse> listener){
        SourceSitesStatRequest request = new SourceSitesStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static TrafficSummaryStatRequest getTrafficSummaryStat(boolean useCache, BaseRequest.Listener<TrafficSummaryStatResponse> listener){
        TrafficSummaryStatRequest request = new TrafficSummaryStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static SourcesSummaryStatRequest getSourcesSummaryStat(boolean useCache, BaseRequest.Listener<SourcesSummaryStatResponse> listener){
        SourcesSummaryStatRequest request = new SourcesSummaryStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static OSStatRequest getOSStat(boolean useCache, BaseRequest.Listener<OSStatResponse> listener){
        OSStatRequest request = new OSStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static GeoStatRequest getGeoStat(boolean useCache, BaseRequest.Listener<GeoStatResponse> listener){
        GeoStatRequest request = new GeoStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static BrowsersStatRequest getBrowsersStat(boolean useCache, BaseRequest.Listener<BrowsersStatResponse> listener){
        BrowsersStatRequest request = new BrowsersStatRequest(useCache, listener);
        request.start();
        return request;
    }

    public static AgeGenderStatRequest getAgeGenderStat(boolean useCache, BaseRequest.Listener<AgeGenderStatResponse> listener){
        AgeGenderStatRequest request = new AgeGenderStatRequest(useCache, listener);
        request.start();
        return request;
    }

}
