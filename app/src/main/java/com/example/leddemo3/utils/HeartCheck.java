package com.example.leddemo3.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class HeartCheck{
    //String urlport ="http://192.168.0.33:3333/";
    public void HeartRequest(String url){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //TickHeartRequest(url);
            }
        };
    }

    long delay = 0; // 延迟开始的时间，单位: 毫秒，这里设置为立即开始
    long period = 10000; // 定时任务执行的间隔时间，单位: 毫秒

    // 定时任务从现在开始，每隔10秒执行一次
    //timer.scheduleAtFixedRate(task, delay, period);
    private void TickHeartRequest(String urlip) throws IOException {
        String urlString = urlip; // 替换为你的URL
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // 获取响应码
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 获取响应内容
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder response = new StringBuilder();

                } else {
                    // 处理错误
                }
        }

}


