package ru.chertenok.weather.niceweather.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import ru.chertenok.weather.niceweather.config.Config;

/**
 * Created by 13th on 15.12.2017.
 */

public class WeatherDateBase extends SQLiteOpenHelper {
    private static  SQLiteDatabase bd;
    // ---------- BD ----------------------------
    private static final int BD_VERSION = 1;
    private static final String BD_NAME = "weather.db";
    public static final String BD_CREATE_TABLE_VER_1 = "create table Setting(name text, value text);"+
            "create table WeatherToday(_id int, data int, city text, provider text);" +
            "create table WeatherTodayDetail (id_key int, param text, value text); ";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BD_CREATE_TABLE_VER_1);
        bd = getWritableDatabase();
    }

    public WeatherDateBase(Context context) {
        super(context, BD_NAME, null, BD_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        super.close();
        this.bd = null;
    }

    public void saveInBDSettings(List list)
    {

    }
}
