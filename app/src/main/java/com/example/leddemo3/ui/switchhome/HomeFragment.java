package com.example.leddemo3.ui.switchhome;

import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.leddemo3.MainActivity;
import com.example.leddemo3.R;
import com.example.leddemo3.databinding.FragmentSwitchBinding;
import com.example.leddemo3.global.GlobalHttpUrl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeFragment extends Fragment {

    private FragmentSwitchBinding binding;
    //String urlport ="http://192.168.0.33:3333/";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentSwitchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        //开灯逻辑
        Button onButton = (Button) view.findViewById(R.id.onButton);
        Button offButton = (Button) view.findViewById(R.id.offButton);

        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLedHttpRequest(v);
                // 在这里处理按钮点击事件
               // Toast.makeText(MainActivity, "开灯了!", Toast.LENGTH_SHORT).show();
            }
        });
        //关灯逻辑

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理按钮点击事件
                offLedHttpRequest(v);
                //Toast.makeText(MainActivity.this, "关灯了!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void onLedHttpRequest(View view) {
        if(!GlobalHttpUrl.isUrlNull()){
            Toast.makeText(view.getContext(),"请先选择要控制的设备!", Toast.LENGTH_SHORT).show();
            return;
        }
        String urlString ="http://"+ GlobalHttpUrl.url +":3333/on"; // 替换为你的URL
        new Thread(() -> {
            try {
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

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // 关闭连接和流
                    reader.close();
                    connection.disconnect();

                    // 此处为UI线程，可以更新UI
//                    runOnUiThread(() -> {
//                        // 更新UI，例如显示获取到的数据
//                    });
                } else {
                    // 处理错误
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    public void offLedHttpRequest(View view) {
        if(!GlobalHttpUrl.isUrlNull()){
            Toast.makeText(view.getContext(),"请先选择要控制的设备!", Toast.LENGTH_SHORT).show();
            return;
        }
        String urlString ="http://"+ GlobalHttpUrl.url+":3333/off"; // 替换为你的URL
        new Thread(() -> {
            try {
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

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // 关闭连接和流
                    reader.close();
                    connection.disconnect();

                    // 此处为UI线程，可以更新UI
//                    runOnUiThread(() -> {
//                        // 更新UI，例如显示获取到的数据
//                    });
                } else {
                    // 处理错误
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}