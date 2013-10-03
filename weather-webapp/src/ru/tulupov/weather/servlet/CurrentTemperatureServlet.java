package ru.tulupov.weather.servlet;

import com.google.gson.Gson;
import ru.tulupov.weather.model.json.CurrentTemperature;
import ru.tulupov.weather.provider.NsuWeatherProvider;
import ru.tulupov.weather.util.GsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class CurrentTemperatureServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        NsuWeatherProvider provider = new NsuWeatherProvider();
        CurrentTemperature currentTemperature = provider.getCurrentTemperature();


        String data = GsonUtils.toJson(currentTemperature);
        OutputStream os = response.getOutputStream();
        os.write(data.getBytes());
        os.close();


    }
}
