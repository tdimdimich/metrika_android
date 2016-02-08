package ru.wwdi.metrika.menu;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryashentsev on 28.04.14.
 */
public class MenuItem {

    private String mName;
    private boolean mSelectable = true;

    public MenuItem(JSONObject obj) throws JSONException {
        mName = obj.getString("name");
        if(obj.has("selectable")){
            mSelectable = obj.getBoolean("selectable");
        }
    }

    public boolean isSelectable() {
        return mSelectable;
    }

    public String getName(){
        return mName;
    }

}
