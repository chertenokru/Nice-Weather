package ru.chertenok.weather.niceweather.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.chertenok.weather.niceweather.config.Config;

/**
 * Created by 13th on 15.12.2017.
 */

public class WeatherDateBase extends SQLiteOpenHelper {

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.rawQuery(Config.BD_CREATE_TABLE_VER_1,null);

    }

    public WeatherDateBase(Context context) {
        super(context, Config.getBdName(), null, Config.getBdVersion());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
