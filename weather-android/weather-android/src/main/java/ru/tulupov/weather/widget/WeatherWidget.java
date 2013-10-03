package ru.tulupov.weather.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

import ru.tulupov.weather.R;
import ru.tulupov.weather.service.UpdateService;


public class WeatherWidget extends AppWidgetProvider {
    private static final String ACTION_WIDGET_RECEIVER = "fff";
    private PendingIntent service = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);

        //Подготавливаем Intent для Broadcast
        Intent active = new Intent(context, WeatherWidget.class);
        active.setAction(ACTION_WIDGET_RECEIVER);
        active.putExtra("msg", "Hello Habrahabr");

        //создаем наше событие
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

        //регистрируем наше событие
        remoteViews.setOnClickPendingIntent(R.id.button, actionPendingIntent);

        remoteViews.setTextViewText(R.id.button, "" + System.currentTimeMillis());

        //обновляем виджет
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);


        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Calendar TIME = Calendar.getInstance();
        TIME.set(Calendar.MINUTE, 0);
        TIME.set(Calendar.SECOND, 0);
        TIME.set(Calendar.MILLISECOND, 0);

        final Intent i = new Intent(context, UpdateService.class);

        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        m.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), 1000 * 60 * 30, service);


    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        //Ловим наш Broadcast, проверяем и выводим сообщение
        final String action = intent.getAction();
        if (ACTION_WIDGET_RECEIVER.equals(action)) {

            Toast.makeText(context, "start req", Toast.LENGTH_SHORT).show();

            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    "http://weather-backend.appspot.com/api/current_temperature.json",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(context, s.substring(0, 30), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(context, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            queue.add(myReq);


//            String msg = "null";
//            try {
//                msg = intent.getStringExtra("msg");
//            } catch (NullPointerException e) {
//                Log.e("Error", "msg = null");
//            }
//            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDisabled(Context context) {
//        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        m.cancel(service);
    }
}
