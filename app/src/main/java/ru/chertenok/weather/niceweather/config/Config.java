package ru.chertenok.weather.niceweather.config;

/**
 * Created by 13th on 06.12.2017.
 */

import android.content.Context;
import android.content.SharedPreferences;



/**
 * Save and load setting,store default setting
 */
public class Config {

    //             CONST

    // ------- weather const-------------------
    private static final String DEFAULT_LANG = "ru";
    private static final String DEFAULT_OWM_KEY_API = "1f3c67e1c4a6a29b6cb512e4038f498e";
    private static final String DEFAULT_WU_KEY_API = "985708306df9d178";
    private static final WeatherSource defaultWeatherSource = WeatherSource.openMap;
    // ---------  SharedPref ------------------------
    private static final String CONFIG_NAME = "config";
    private static final String CONFIG_PREF_KEY_WEATHER_SOURCE = "source";
    // ----------- program ---------------------
    private static final String timeFormat = " HH:mm:ss";
    private static final boolean useBDtoWeather = true;
    private static final boolean useBDtoSetting = false;

    //            VAR

    private static WeatherSource currentWeatherSource = defaultWeatherSource;





    public static void load(Context applicationContext) {

        SharedPreferences preferences = applicationContext.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(CONFIG_PREF_KEY_WEATHER_SOURCE)) {
            currentWeatherSource = WeatherSource.valueOf(preferences.getString(CONFIG_PREF_KEY_WEATHER_SOURCE,defaultWeatherSource.name()));
        }
    }

    public static void save(Context applicationContext) {
        SharedPreferences.Editor edit = applicationContext.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE).edit();
        edit.putString(CONFIG_PREF_KEY_WEATHER_SOURCE, currentWeatherSource.name());
        edit.commit();
    }




    public static boolean isUseBDtoWeather() {
        return useBDtoWeather;
    }

    public static String getTimeFormat() {
        return timeFormat;
    }



    public enum WeatherSource {openMap, Underground};

    public static String getLang() {
        return DEFAULT_LANG;
    }

    public static String getKeyApi() {
        return (currentWeatherSource == WeatherSource.openMap) ? DEFAULT_OWM_KEY_API : DEFAULT_WU_KEY_API;
    }

    public static WeatherSource getWeatherSource() {
        return currentWeatherSource;
    }

    public static void setWeatherSource(WeatherSource ws) {
        currentWeatherSource = ws;
    }


}
