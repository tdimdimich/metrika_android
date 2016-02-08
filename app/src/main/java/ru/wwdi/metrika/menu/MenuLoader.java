package ru.wwdi.metrika.menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.YandexMetrikaApplication;

/**
 * Created by ryashentsev on 28.04.14.
 */
public class MenuLoader {

    private static final String MENU_FILE = "left_menu.json";

    public static List<MenuItem> getMenu() throws IOException, JSONException {
        String menuFileContent = getMenuFileContent();
        JSONArray jsonMenuFileContent = new JSONArray(menuFileContent);
        List<MenuItem> result = new ArrayList<MenuItem>();
        JSONObject jsonItem;
        for(int i=0;i<jsonMenuFileContent.length();i++){
            jsonItem = jsonMenuFileContent.getJSONObject(i);
            if(jsonItem.has("children")){
                result.add(new MenuItemContainer(jsonItem));
            }else{
                result.add(new MenuItem(jsonItem));
            }
        }
        return result;
    }

    public static String getMenuFileContent() throws IOException {
        BufferedReader br = null;
        try{
            InputStream is = YandexMetrikaApplication.getInstance().getResources().getAssets().open(MENU_FILE);
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line;
            StringBuffer result = new StringBuffer();
            while((line = br.readLine())!=null ){
                result.append(line);
            }
            return result.toString();
        }finally {
            try {
                br.close();
            } catch (IOException e) {}
        }
    }
}
