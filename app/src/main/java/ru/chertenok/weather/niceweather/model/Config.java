package ru.chertenok.weather.niceweather.model;

/**
 * Created by 13th on 06.12.2017.
 */

/**
 * Save and load setting,store default setting
 */
public class Config {
 private static final String DEFAULT_LANG = "ru" ;
 public static final String DEFAULT_OWM_KEY_API = "1f3c67e1c4a6a29b6cb512e4038f498e";
 public static final String DEFAULT_WU_KEY_API = "985708306df9d178";



    public static String getLang() {
        return DEFAULT_LANG;
    }

    //public static String getKeyApi() {
//        return DEFAULT_KEY_API;
//    }



}
