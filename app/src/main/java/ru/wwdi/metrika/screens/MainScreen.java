package ru.wwdi.metrika.screens;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.helpers.AppRater;
import ru.wwdi.metrika.helpers.PxDpHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.helpers.TipsHelper;
import ru.wwdi.metrika.helpers.WaitHelper;
import ru.wwdi.metrika.menu.MenuFragment;
import ru.wwdi.metrika.menu.MenuItem;
import ru.wwdi.metrika.screens.content.EntrancePageStatScreen;
import ru.wwdi.metrika.screens.content.ExitPageStatScreen;
import ru.wwdi.metrika.screens.content.PageTitleStatScreen;
import ru.wwdi.metrika.screens.content.PopularStatScreen;
import ru.wwdi.metrika.screens.content.URLParamsStatScreen;
import ru.wwdi.metrika.screens.settings.Settings1Screen;
import ru.wwdi.metrika.screens.source.SearchEnginesStatScreen;
import ru.wwdi.metrika.screens.source.SearchPhrasesStatScreen;
import ru.wwdi.metrika.screens.source.SourceSitesStatScreen;
import ru.wwdi.metrika.screens.source.SourcesSummaryStatScreen;
import ru.wwdi.metrika.screens.technology.BrowsersStatScreen;
import ru.wwdi.metrika.screens.technology.CookiesStatScreen;
import ru.wwdi.metrika.screens.technology.DeviceTypesStatScreen;
import ru.wwdi.metrika.screens.technology.DisplayGroupsStatScreen;
import ru.wwdi.metrika.screens.technology.DisplaySizesStatScreen;
import ru.wwdi.metrika.screens.technology.FlashStatScreen;
import ru.wwdi.metrika.screens.technology.JSStatScreen;
import ru.wwdi.metrika.screens.technology.JavaStatScreen;
import ru.wwdi.metrika.screens.technology.MobileStatScreen;
import ru.wwdi.metrika.screens.technology.OSsStatScreen;
import ru.wwdi.metrika.screens.technology.SilverlightStatScreen;
import ru.wwdi.metrika.screens.totalSummary.TotalSummaryScreen;
import ru.wwdi.metrika.screens.traffic.TrafficScreen;
import ru.wwdi.metrika.screens.visitors.AgeGenderStructureStatScreen;
import ru.wwdi.metrika.screens.visitors.DeepnessStatScreen;
import ru.wwdi.metrika.screens.visitors.GeoStatScreen;
import ru.wwdi.metrika.screens.visitors.HourlyStatScreen;
import ru.wwdi.metrika.screens.visitors.TimeStatScreen;
import ru.wwdi.metrika.topPanel.TopPanel;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
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
 * Created by dmitrykorotchenkov on 21/04/14.
 */
public class MainScreen extends FragmentActivity implements MenuFragment.MenuListener, Settings1Screen.SettingsListener, TopPanel.TopPanelListener {

    private static final int REQUEST_DATE_PERIOD = 10;

    private DrawerLayout mDrawerLayout;
    private MenuFragment mMenuFragment;
    private FrameLayout mContent;
    private View mNoData;
    private View mErrorView;
    private View mLoadingView;
    private ViewGroup mScreenContainer;
    private TopPanel mTopPanel;

    private CreatorsScreen mCreatorsScreen = new CreatorsScreen();
    private Settings1Screen mSettingsScreen = new Settings1Screen();
    private TotalSummaryScreen mTotalSummaryScreen = new TotalSummaryScreen();
    private TrafficScreen mTrafficScreen = new TrafficScreen();
    private SourceSitesStatScreen mSourceSitesScreen = new SourceSitesStatScreen();
    private SourcesSummaryStatScreen mSourcesSummaryScreen = new SourcesSummaryStatScreen();
    private SearchEnginesStatScreen mSearchEnginesScreen = new SearchEnginesStatScreen();
    private SearchPhrasesStatScreen mSearchPhrasesScreen = new SearchPhrasesStatScreen();
    private GeoStatScreen mGeoScreen = new GeoStatScreen();
    private AgeGenderStructureStatScreen mAgeGenderStructureScreen = new AgeGenderStructureStatScreen();
    private DeepnessStatScreen mDeepnessScreen = new DeepnessStatScreen();
    private TimeStatScreen mTimeScreen = new TimeStatScreen();
    private HourlyStatScreen mHourlyScreen = new HourlyStatScreen();
    private PopularStatScreen mPopularScreen = new PopularStatScreen();
    private EntrancePageStatScreen mEntrancePageScreen = new EntrancePageStatScreen();
    private ExitPageStatScreen mExitPageScreen = new ExitPageStatScreen();
    private PageTitleStatScreen mPageTitleScreen = new PageTitleStatScreen();
    private URLParamsStatScreen mURLParamsScreen = new URLParamsStatScreen();
    private BrowsersStatScreen mBrowsersScreen = new BrowsersStatScreen();
    private OSsStatScreen mOSsScreen = new OSsStatScreen();
    private DisplayGroupsStatScreen mDisplayGroupsScreen = new DisplayGroupsStatScreen();
    private DisplaySizesStatScreen mDisplaySizesScreen = new DisplaySizesStatScreen();
    private MobileStatScreen mMobileScreen = new MobileStatScreen();
    private FlashStatScreen mFlashScreen = new FlashStatScreen();
    private SilverlightStatScreen mSilverlightScreen = new SilverlightStatScreen();
    private JavaStatScreen mJavaScreen = new JavaStatScreen();
    private CookiesStatScreen mCookiesScreen = new CookiesStatScreen();
    private JSStatScreen mJSScreen = new JSStatScreen();
    private DeviceTypesStatScreen mDeviceTypesScreen = new DeviceTypesStatScreen();

    private BaseScreen mCurrentScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WaitHelper.waitAndExecute(5000, new Runnable(){
            @Override
            public void run() {
                TipsHelper.showSwipeTipIfNeeded(MainScreen.this);
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mScreenContainer = (ViewGroup) findViewById(R.id.screenContainer);
        mContent = (FrameLayout) findViewById(R.id.content);
        mErrorView = findViewById(R.id.errorView);
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentScreen!=null) mCurrentScreen.refresh();
            }
        });
        mNoData = findViewById(R.id.noDataView);
        mNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentScreen!=null) mCurrentScreen.refresh();
            }
        });
        mLoadingView = findViewById(R.id.loadingView);
        mMenuFragment = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menuFragment);
        mMenuFragment.setMenuListener(this);
        mTopPanel = (TopPanel) findViewById(R.id.topPanel);
        mTopPanel.setListener(this);

        showScreen(mMenuFragment.getSelectedItem());
        if(SharedPreferencesHelper.isFirstLaunch()){
            onCalendarClick();//show date period selection dialog
        }
        //demonstrateMenuIfFirstLaunch();

        mTopPanel.setEnabled(false);
        Webservice.getCounterList(false, new BaseRequest.Listener<CounterListResponse>() {
            @Override
            public void onResponse(CounterListResponse response) {
                mTopPanel.updateCounters();
                mTopPanel.setEnabled(true);
            }
        });

        AppRater.checkLaunchCount(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String newLanguage = getResources().getConfiguration().locale.getLanguage();
        if(SharedPreferencesHelper.getLanguage()==null){
            SharedPreferencesHelper.setLanguage(newLanguage);
        }else{
            if(!SharedPreferencesHelper.getLanguage().equals(newLanguage)){
                SharedPreferencesHelper.setLanguage(newLanguage);
                resetScreensData();
            }
        }
    }

    private void demonstrateMenuIfFirstLaunch(){
        if(!SharedPreferencesHelper.isFirstLaunch()) return;
        WaitHelper.waitAndExecute(2000, new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.openDrawer(findViewById(R.id.leftDrawer));
                WaitHelper.waitAndExecute(2000, new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawer(findViewById(R.id.leftDrawer));
                    }
                });
            }
        });
    }

    @Override
    public void onMenuItemSelected(MenuItem item) {
        YandexMetrikaApplication.sendEvent(item.getName());
        WaitHelper.waitAndExecute(500, new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(findViewById(R.id.leftDrawer));
            }
        });
        showScreen(item);
    }

    private void showScreen(MenuItem item){
        showScreen(item, false, false);
    }

    private void showScreen(MenuItem item, boolean addToBackStack, boolean animated){
        if(mCurrentScreen==mSettingsScreen){
            //resets counters if its have not been saved
            Webservice.getCounterList(true, new BaseRequest.Listener<CounterListResponse>() {
                @Override
                public void onResponse(CounterListResponse response) {
                    //nothing
                }
            });
        }
        if("settings".equals(item.getName())){
            mSettingsScreen.setListener(this);
            showScreen(mSettingsScreen, addToBackStack, animated);
            setTopPanelVisible(false);
        }else if("exit".equals(item.getName())){
            showLogoutDialog();
        }else if("creators".equals(item.getName())){
            showScreen(mCreatorsScreen, addToBackStack, animated);
            setTopPanelVisible(false);
        }else if("total_summary".equals(item.getName())){
            showScreen(mTotalSummaryScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("attendance".equals(item.getName())){
            showScreen(mTrafficScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("sites".equals(item.getName())){
            showScreen(mSourceSitesScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("summary".equals(item.getName())){
            showScreen(mSourcesSummaryScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("search_systems".equals(item.getName())){
            showScreen(mSearchEnginesScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("search_phrases".equals(item.getName())){
            showScreen(mSearchPhrasesScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("by_country".equals(item.getName())){
            showScreen(mGeoScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("gender_age_structure".equals(item.getName())){
            showScreen(mAgeGenderStructureScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("by_depth_view".equals(item.getName())){
            showScreen(mDeepnessScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("by_time".equals(item.getName())){
            showScreen(mTimeScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("by_part_of_day".equals(item.getName())){
            showScreen(mHourlyScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("popular".equals(item.getName())){
            showScreen(mPopularScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("enter_pages".equals(item.getName())){
            showScreen(mEntrancePageScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("exit_pages".equals(item.getName())){
            showScreen(mExitPageScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("page_titles".equals(item.getName())){
            showScreen(mPageTitleScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("url_parameters".equals(item.getName())){
            showScreen(mURLParamsScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("browsers".equals(item.getName())){
            showScreen(mBrowsersScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("operating_systems".equals(item.getName())){
            showScreen(mOSsScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("display_groups".equals(item.getName())){
            showScreen(mDisplayGroupsScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("display_resolutions".equals(item.getName())){
            showScreen(mDisplaySizesScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("mobile_devices".equals(item.getName())){
            showScreen(mMobileScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("flash_versions".equals(item.getName())){
            showScreen(mFlashScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("silverlight_versions".equals(item.getName())){
            showScreen(mSilverlightScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("java_availability".equals(item.getName())){
            showScreen(mJavaScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("cookies_availability".equals(item.getName())){
            showScreen(mCookiesScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("javascript_availability".equals(item.getName())){
            showScreen(mJSScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("device_types".equals(item.getName())){
            showScreen(mDeviceTypesScreen, addToBackStack, animated);
            setTopPanelVisible(true);
        }else if("share".equals(item.getName())){
            Intent intent=new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + "\n" + getString(R.string.share_url));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_text));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            try{
                startActivity(Intent.createChooser(intent, "Share URL"));
            }catch (ActivityNotFoundException e){
                Toast.makeText(this, R.string.no_application_for_sharing, Toast.LENGTH_LONG);
            }
        }

    }

    private void setTopPanelVisible(boolean visible){
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mScreenContainer.getLayoutParams();
        if(visible){
            mTopPanel.setVisibility(View.VISIBLE);
            lp.setMargins(0, PxDpHelper.dpToPx(50), 0, 0);
        }else{
            mTopPanel.setVisibility(View.INVISIBLE);
            lp.setMargins(0, 0, 0, 0);
        }
        mScreenContainer.setLayoutParams(lp);
    }

    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exit)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        YandexMetrikaApplication.sendEvent("logout");
                        logout();
                    }
                })
                .setNegativeButton(R.string.no, null);
        builder.create().show();
    }

    private void logout(){
        SharedPreferencesHelper.setToken(null);
        clearAllCache();
        android.webkit.CookieManager.getInstance().removeAllCookie();
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearStatisticsCache(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AgeGenderStatResponse.clearResponses();
                BrowsersStatResponse.clearResponses();
                GeoStatResponse.clearResponses();
                OSStatResponse.clearResponses();
                SourcesSummaryStatResponse.clearResponses();
                TrafficSummaryStatResponse.clearResponses();
                SourceSitesStatResponse.clearResponses();
                SearchEnginesStatResponse.clearResponses();
                SearchPhrasesStatResponse.clearResponses();
                AgeGenderStructureStatResponse.clearResponses();
                DeepnessStatResponse.clearResponses();
                HourlyStatResponse.clearResponses();
                PopularStatResponse.clearResponses();
                EntrancePageStatResponse.clearResponses();
                ExitPageStatResponse.clearResponses();
                PageTitleStatResponse.clearResponses();
                URLParamsStatResponse.clearResponses();
                DisplaysStatResponse.clearResponses();
                MobileStatResponse.clearResponses();
                FlashStatResponse.clearResponses();
                SilverlightStatResponse.clearResponses();
                JavaStatResponse.clearResponses();
                CookiesStatResponse.clearResponses();
                JSStatResponse.clearResponses();
            }
        }).start();
    }

    private void clearAllCache(){
        CounterListResponse.clearResponses();
        clearStatisticsCache();
    }

    @Override
    public void onSettingsUpdated() {
        mTopPanel.updateCounters();
        showScreen(mMenuFragment.getSelectedItem(), false, true);
    }

    private void resetScreensData(){
        mTotalSummaryScreen.refresh();
        mTrafficScreen.refresh();
        mSourceSitesScreen.refresh();
        mSourcesSummaryScreen.refresh();
        mSearchEnginesScreen.refresh();
        mSearchPhrasesScreen.refresh();
        mGeoScreen.refresh();
        mAgeGenderStructureScreen.refresh();
        mDeepnessScreen.refresh();
        mTimeScreen.refresh();
        mHourlyScreen.refresh();
        mPopularScreen.refresh();
        mEntrancePageScreen.refresh();
        mExitPageScreen.refresh();
        mPageTitleScreen.refresh();
        mURLParamsScreen.refresh();
        mBrowsersScreen.refresh();
        mOSsScreen.refresh();
        mDisplayGroupsScreen.refresh();
        mDisplaySizesScreen.refresh();
        mMobileScreen.refresh();
        mFlashScreen.refresh();
        mSilverlightScreen.refresh();
        mJavaScreen.refresh();
        mCookiesScreen.refresh();
        mJSScreen.refresh();
        mDeviceTypesScreen.refresh();
    }

    @Override
    public void onCalendarClick() {
        Intent intent = new Intent(this, DateIntervalSelectionScreen.class);
        startActivityForResult(intent, REQUEST_DATE_PERIOD);
    }

    @Override
    public void onSelectedCounterChanged() {
        resetScreensData();
        YandexMetrikaApplication.sendEvent("counter_changed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode==REQUEST_DATE_PERIOD){
            resetScreensData();
            YandexMetrikaApplication.sendEvent("date_changed");
        }
        if(SharedPreferencesHelper.isFirstLaunch()){
            demonstrateMenuIfFirstLaunch();
            SharedPreferencesHelper.setNotFirstLaunch();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /************* METHODS USED FROM SUBSCREENS ************/

    public void showScreen(BaseScreen screen, boolean addToBackStack, boolean animated){
        if(mCurrentScreen==screen) return;
        showContent();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, screen);
        if(addToBackStack) transaction.addToBackStack(null);
        if(animated) transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        mCurrentScreen = screen;
    }

    public void showNoData(){
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mContent.setVisibility(View.GONE);
        mNoData.setVisibility(View.VISIBLE);
    }

    public void showLoading(){
        mLoadingView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mContent.setVisibility(View.GONE);
        mNoData.setVisibility(View.GONE);
    }

    public void showError(){
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.GONE);
        mNoData.setVisibility(View.GONE);
    }

    public void showContent(){
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);
        mNoData.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String selectedItemName = mMenuFragment.getSelectedItem().getName();
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            setMenuEnabled(true);
            if("settings".equals(selectedItemName) || "creators".equals(selectedItemName)){
            }else{
                setTopPanelVisible(true);
            }
        }else{
            setMenuEnabled(false);
            if("total_summary".equals(selectedItemName) || "settings".equals(selectedItemName) || "creators".equals(selectedItemName)){
            }else{
                setTopPanelVisible(false);
            }
        }
    }

    private void setMenuEnabled(boolean enabled) {
        mDrawerLayout.setDrawerLockMode(enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
