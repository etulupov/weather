package ru.tulupov.weather.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.tulupov.weather.R;
import ru.tulupov.weather.model.CurrentTemperature;
import ru.tulupov.weather.widget.WeatherWidget;

public class UpdateService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget_main);

        view.setViewVisibility(R.id.progress, View.VISIBLE);
        view.setTextViewText(R.id.last_update, "updating...");
        // Push update for this widget to the home screen
        final ComponentName thisWidget = new ComponentName(this, WeatherWidget.class);
        final AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, view);


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                "http://weather-backend.appspot.com/api/current_temperature.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        CurrentTemperature currentTemperature = gson.fromJson(s, CurrentTemperature.class);
                        view.setTextViewText(R.id.text, "" + currentTemperature.getTemperature() + " °C");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");


                        view.setTextViewText(R.id.last_update, "last update at "+dateFormat.format(new Date(currentTemperature.getTimestamp() )));
                        view.setViewVisibility(R.id.progress, View.GONE);
                        manager.updateAppWidget(thisWidget, view);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );

        queue.add(myReq);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
