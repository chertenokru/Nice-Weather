package ru.chertenok.weather.niceweather.model;

import android.content.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by 13th on 05.12.2017.
 */

public class TodayWeather  implements Serializable{
    private Map<String,String> map ;

    public TodayWeather(HashMap<String, String> map) {
        this.map = map;
    }

    public String getValueByName(String name)
    {
        return map.get(name);
    }

    /*** return translated string from resource by resource-name
      * @param context
     * @param name
     * @return
     */

    public static String getTranslatedName(Context context, String name)
    {
        return context.getString(context.getResources().getIdentifier(name, "string", context.getPackageName()));
    }

}
