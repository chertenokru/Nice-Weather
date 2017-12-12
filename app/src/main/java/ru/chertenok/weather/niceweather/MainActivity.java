package ru.chertenok.weather.niceweather;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.chertenok.weather.niceweather.model.Config;
import ru.chertenok.weather.niceweather.model.OnLoad;
import ru.chertenok.weather.niceweather.model.OpenWeatherMapDataLoader;
import ru.chertenok.weather.niceweather.model.TodayWeather;
import ru.chertenok.weather.niceweather.model.WeatherUndergroundDataLoader;

public class MainActivity extends AppCompatActivity
        implements OnLoad,NavigationView.OnNavigationItemSelectedListener
{

    private TextView tv_temp;
    private TextView tv_desc;
    private TextView tv_city;
    private TextView tv_date;
    private ImageView iv_icon;
    private MenuItem mi_wopenmap;
    private MenuItem mi_wundegraund;
    private NavigationView navigationView;
    private FloatingActionButton fab2;
    private DrawerLayout drawer;



    private Handler handler = new Handler();
    private TodayWeather todayWeather;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Wheather updating....", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                updateData();
            }
        });
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.openDrawer(Gravity.LEFT);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tv_temp = findViewById(R.id.tv_temp);
        tv_desc = findViewById(R.id.tv_WeatherDescription);
        tv_city = findViewById(R.id.tv_city);
        tv_date = findViewById(R.id.tv_date);
        iv_icon = findViewById(R.id.iv_Weather);
        mi_wopenmap = findViewById(R.id.nav_openMap);
        mi_wundegraund = findViewById(R.id.nav_underground);

        todayWeather = new TodayWeather("Moscow","ru");
        Config.load(getApplicationContext());
        updateData();
    }

  private void updateData(){
     if (Config.getWeatherSource() == Config.WeatherSource.openMap)
           OpenWeatherMapDataLoader.loadDate(getApplicationContext(),todayWeather, this);
     else
            WeatherUndergroundDataLoader.loadDate(getApplicationContext(),todayWeather,this);
  }

  private void updateUI(TodayWeather todayWeather)
    {
        tv_temp.setText(todayWeather.getValueByName("temp"));
        tv_desc.setText(todayWeather.getValueByName("description"));
        tv_city.setText(todayWeather.getCity());

        GregorianCalendar calendar = new GregorianCalendar( );
        calendar.setTimeInMillis(todayWeather.getLastDateUpdate());
        tv_date.setText("last update - "+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));
        iv_icon.setImageBitmap(todayWeather.getIcon());

        if (Config.getWeatherSource() == Config.WeatherSource.openMap) navigationView.setCheckedItem(R.id.nav_openMap);
        if (Config.getWeatherSource() == Config.WeatherSource.Underground) navigationView.setCheckedItem(R.id.nav_underground);
    }

    @Override
    public void onPrepareSupportNavigateUpTaskStack(@NonNull TaskStackBuilder builder) {
        super.onPrepareSupportNavigateUpTaskStack(builder);

        mi_wundegraund.setChecked(Config.getWeatherSource() == Config.WeatherSource.Underground);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_openMap) {
            Config.setWeatherSource(Config.WeatherSource.openMap);
            updateData();

        } else if (id == R.id.nav_underground) {
            Config.setWeatherSource(Config.WeatherSource.Underground);
            updateData();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Config.save(getApplicationContext());
    }


    @Override
    public void onLoad(boolean isOk, final  TodayWeather todayWeather) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateUI(todayWeather);
            }

        });
    }
}
