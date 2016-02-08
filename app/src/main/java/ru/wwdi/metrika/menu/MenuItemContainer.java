package ru.wwdi.metrika.menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryashentsev on 28.04.14.
 */
public class MenuItemContainer extends MenuItem {

    private List<MenuItem> mChildren;

    public MenuItemContainer(JSONObject obj) throws JSONException {
        super(obj);
        mChildren = new ArrayList<MenuItem>();
        JSONArray jsonChildren = obj.getJSONArray("children");
        MenuItem item;
        for(int i=0;i<jsonChildren.length();i++){
            item = new MenuItem(jsonChildren.getJSONObject(i));
            mChildren.add(item);
        }
    }

    public List<MenuItem> getChildren(){
        return mChildren;
    }
}
