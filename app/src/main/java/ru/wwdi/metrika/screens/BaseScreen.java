package ru.wwdi.metrika.screens;

import android.support.v4.app.Fragment;

/**
 * Created by ryashentsev on 05.05.14.
 */
public abstract class BaseScreen extends Fragment {

    protected void showNoData(){
        MainScreen ms = getMainScreen();
        if(ms==null || ms.isFinishing()) return;
        ms.showNoData();
    }

    protected void showScreen(BaseScreen screen){
        if(getMainScreen()==null) return;
        getMainScreen().showScreen(screen, true, true);
    }

    private MainScreen getMainScreen(){
        return (MainScreen)getActivity();
    }

    protected void showLoading(){
        MainScreen ms = getMainScreen();
        if(ms==null || ms.isFinishing()) return;
        ms.showLoading();
    }

    protected void showError(){
        MainScreen ms = getMainScreen();
        if(ms==null || ms.isFinishing()) return;
        ms.showError();
    }

    protected void showContent(){
        MainScreen ms = getMainScreen();
        if(ms==null || ms.isFinishing()) return;
        ms.showContent();
    }

    public abstract void refresh();

}
