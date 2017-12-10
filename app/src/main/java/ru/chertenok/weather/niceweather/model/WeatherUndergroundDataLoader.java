package ru.chertenok.weather.niceweather.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by 13th on 06.12.2017.
 */

public class WeatherUndergroundDataLoader {
    private static final String OPEN_API_MAP = "http://api.wunderground.com/api/%s/conditions/lang:RU/q/Russia/Moscow.json";
    //private static final String OPEN_API_IMAGE ="http://openweathermap.org/img/w/";
    private static final String OPEN_API_IMAGE_EXT =".png";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;
    public static Bitmap icon = null;


   public  static void  loadDate(final Context context, final TodayWeather todayWeather, final OnLoad onLoad)
   {
       final HashMap<String,String> map = new HashMap<>();

       new Thread() {
           public void run() {
               final JSONObject json = getJSONData(context, todayWeather.getCity());
               Log.d("TAG", json.toString());
                   try {
                     //  if (json!=null && (json.getInt(RESPONSE)==ALL_GOOD))
                       {

                           float  temp = Float.parseFloat(json.getJSONObject("current_observation").getString("temp_c"));

                           map.put("temp", ""+Math.round(temp)+" ");
                           map.put("description", json.getJSONObject("current_observation").getString("weather"));

                           String ico = json.getJSONObject("current_observation").getString("icon_url");
                           if (icon != null) {
                              // icon.recycle();
                               }
                           icon = downloadImage(ico);

                       }
                   }
                   catch (Exception e)
                   {
                       Log.e("ERROR",e.toString());
                   }

               todayWeather.updateWeather(map,icon);
               // if defined send callback
               if (onLoad != null) onLoad.onLoad((json != null) ? true : false, todayWeather);
           }}
           .start();

   }


   private static JSONObject getJSONData(Context context, String city){
        try{
            URL url = new URL(String.format(OPEN_API_MAP,Config.DEFAULT_WU_KEY_API));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         //   connection.addRequestProperty(KEY, Config.getKeyApi());


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
       //     if (jsonObject.getInt(RESPONSE) != ALL_GOOD){
       //         return null;
        //    }

            return jsonObject;
        }catch (Exception e){
            Log.e("error",e.toString());
            return null;

        }

    }


    public static Bitmap downloadImage(String iUrl) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream buf_stream = null;
        try {
            Log.v("DEBUG", "Starting loading image by URL: " + iUrl);
            conn = (HttpURLConnection) new URL(iUrl).openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);
            bitmap = BitmapFactory.decodeStream(buf_stream);
            buf_stream.close();
            conn.disconnect();
            buf_stream = null;
            conn = null;
        } catch (MalformedURLException ex) {
            Log.e("error", "Url parsing was failed: " + iUrl);
        } catch (IOException ex) {
            Log.d("error", iUrl + " does not exists");
        } catch (OutOfMemoryError e) {
            Log.w("error", "Out of memory!!!");
            return null;
        } finally {
            if ( buf_stream != null )
                try { buf_stream.close(); } catch (IOException ex) {}
            if ( conn != null )
                conn.disconnect();
        }
        return bitmap;
    }


}
