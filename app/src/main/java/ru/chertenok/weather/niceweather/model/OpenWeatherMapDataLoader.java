package ru.chertenok.weather.niceweather.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import static android.content.ContentValues.TAG;

/**
 * Created by 13th on 06.12.2017.
 */

public class OpenWeatherMapDataLoader {
    private static final String KEY = "x-api-key";
    private static final String OPEN_API_MAP = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ru";
    private static final String OPEN_API_IMAGE ="http://openweathermap.org/img/w/";
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
                       if (json!=null && (json.getInt(RESPONSE)==ALL_GOOD))
                       {

                           float  temp = Float.parseFloat(json.getJSONObject("main").getString("temp"));

                           map.put("temp", ""+Math.round(temp)+" ");
                           map.put("description", json.getJSONArray("weather").getJSONObject(0).getString("description"));

                           String ico = OPEN_API_IMAGE+json.getJSONArray("weather").getJSONObject(0).getString("icon")+OPEN_API_IMAGE_EXT;
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
            URL url = new URL(String.format(OPEN_API_MAP,city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, Config.getKeyApi());


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
            Log.e(TAG, "Url parsing was failed: " + iUrl);
        } catch (IOException ex) {
            Log.d(TAG, iUrl + " does not exists");
        } catch (OutOfMemoryError e) {
            Log.w(TAG, "Out of memory!!!");
            return null;
        } finally {
            if ( buf_stream != null )
                try { buf_stream.close(); } catch (IOException ex) {}
            if ( conn != null )
                conn.disconnect();
        }
        return bitmap;
    }


    public interface OnLoad {
        void onLoad(boolean isOk,TodayWeather todayWeather);
    }



}
