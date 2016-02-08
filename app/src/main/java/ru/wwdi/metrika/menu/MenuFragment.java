package ru.wwdi.metrika.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;

/**
 * Created by ryashentsev on 28.04.14.
 */
public class MenuFragment extends Fragment implements MenuItemView.MenuItemClickListener {

    private MenuListener mListener;
    private Set<MenuItemContainer> mOpenedItemContainers = new HashSet<MenuItemContainer>();
    private MenuItemView mSelectedItemView;

    public MenuFragment(){
        super();
    }

    public MenuItem getSelectedItem(){
        if(mSelectedItemView==null) return null;
        return mSelectedItemView.getMenuItem();
    }

    private MenuItemView addItem(LayoutInflater inflater, LinearLayout container, MenuItem menuItem){
        if(menuItem instanceof MenuItemContainer){
            MenuItemContainerView view = (MenuItemContainerView) inflater.inflate(R.layout.menu_item_container, null);
            view.setMenuItem((MenuItemContainer)menuItem);
            view.setMenuItemClickListener(this);
            container.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return view;
        }else{
            MenuItemView view = (MenuItemView) inflater.inflate(R.layout.menu_item, null);
            view.setMenuItem(menuItem);
            view.setMenuItemClickListener(this);
            container.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(mSelectedItemView==null){
                onClick(view);
            }
            return view;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_menu, null);
        LinearLayout menuContainer = (LinearLayout) view.findViewById(R.id.container);
        try {
            List<MenuItem> menuItems = MenuLoader.getMenu();
            for(MenuItem item: menuItems){
                addItem(inflater, menuContainer, item);
            }
        } catch (Exception e) {
            Log.e(YandexMetrikaApplication.class.getSimpleName(), "Can't load menu!");
        }
        return view;
    }

    public void setMenuListener(MenuListener listener){
        mListener = listener;
    }

    @Override
    public void onClick(MenuItemView view) {
        if(view instanceof MenuItemContainerView){
            MenuItemContainerView containerView = (MenuItemContainerView)view;
            MenuItemContainer item = containerView.getMenuItem();
            if(mOpenedItemContainers.contains(item)){
                mOpenedItemContainers.remove(containerView.getMenuItem());
                containerView.close(true);
            }else{
                mOpenedItemContainers.add(containerView.getMenuItem());
                containerView.open(true);
            }
        }else{
            MenuItem item = view.getMenuItem();
            if(item.isSelectable()){
                if(mSelectedItemView!=null) mSelectedItemView.setSelected(false);
                mSelectedItemView = view;
                mSelectedItemView.setSelected(true);
            }
            if(mListener!=null)mListener.onMenuItemSelected(view.getMenuItem());
        }
    }

    public static interface MenuListener{
        void onMenuItemSelected(MenuItem item);
    }

}
