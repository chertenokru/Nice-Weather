package ru.chertenok.weather.niceweather.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 13th on 06.12.2017.
 */

public class WeatherDataLoader {
    private static final String KEY = "x-api-key";
    private static final String OPEN_API_MAP = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ru";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;


   public  static void  loadDate(final Context context, final OnLoad onLoad)
   {
       final HashMap<String,String> map = new HashMap<>();

       new Thread() {
           public void run() {
               final JSONObject json = getJSONData(context, "Moscow,ru");
               Log.d("TAG", json.toString());
                   try {
                       if (json!=null && (json.getInt(RESPONSE)==ALL_GOOD))
                       {

                           map.put("temp", json.getJSONObject("main").getString("temp"));
                           map.put("description", json.getJSONArray("weather").getJSONObject(0).getString("description"));

                       }
                   }
                   catch (Exception e)
                   {
                       Log.e("ERROR",e.toString());
                   }

               // if defined send callback
               if (onLoad != null) onLoad.onLoad((json != null) ? true : false, new TodayWeather(map));
           }}
           .start();

   }


   private static JSONObject getJSONData(Context context, String city){
        try{
            URL url = new URL(String.format(OPEN_API_MAP,city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY,Config.getKeyApi());


            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawDate = new StringBuilder(1024);
            String tempVar;

            while ((tempVar = br.readLine()) !=null)
            {
                rawDate.append(tempVar).append(NEW_LINE);
            }
            br.close();
            Log.d("JSON",rawDate.toString());
            JSONObject jsonObject = new JSONObject(rawDate.toString());
            Log.d("JSON",jsonObject.toString());
            if (jsonObject.getInt(RESPONSE) != ALL_GOOD){
                return null;
            }

            return jsonObject;
        }catch (Exception e){
            Log.e("error",e.toString());
            return null;

        }

    }

    public interface OnLoad {
        void onLoad(boolean isOk,TodayWeather todayWeather);
    }

}
