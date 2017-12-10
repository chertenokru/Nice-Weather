package ru.chertenok.weather.niceweather.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Time;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by 13th on 05.12.2017.
 */

public class TodayWeather  implements Serializable{
    private Map<String,String> map  = new HashMap<>();
    private long lastDateUpdate;
    private String city;
    private Bitmap icon = null;

    public TodayWeather(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void updateWeather(HashMap<String, String> map, Bitmap icon)
    {
        this.map.clear();
        this.map.putAll(map);
      //  if (icon != null && !icon.isRecycled()) icon.recycle();

        this.icon = icon.copy(Bitmap.Config.ARGB_8888,false);
        lastDateUpdate= new Date().getTime();

    }

    public long getLastDateUpdate() {
        return lastDateUpdate;
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
