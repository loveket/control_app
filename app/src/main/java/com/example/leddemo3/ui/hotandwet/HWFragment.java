package com.example.leddemo3.ui.hotandwet;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.leddemo3.R;
import com.example.leddemo3.databinding.FragmentHotandwetBinding;
import com.example.leddemo3.global.GlobalHttpUrl;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class HWFragment extends Fragment {

    private FragmentHotandwetBinding binding;
    //String urlport ="http://192.168.0.33:3333/";
    private HWView hwview;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HWModel slideshowViewModel =
                new ViewModelProvider(this).get(HWModel.class);

        binding = FragmentHotandwetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        hwview = view.findViewById(R.id.device_temp_hum);
        hwHttpRequest(hwview);

    }
    public void hwHttpRequest(HWView hwview) {
        if(!GlobalHttpUrl.isUrlNull()){
            Toast.makeText(hwview.getContext(),"请先选择要控制的设备!", Toast.LENGTH_SHORT).show();
            return;
        }
        String urlString ="http://"+ GlobalHttpUrl.url +":3333/gettw";
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1500);
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
                    JSONObject jsonObject = new JSONObject(response.toString());
                    long temp = jsonObject.getLong("temp");
                    long wet = jsonObject.getLong("wet");
                    hwview.setHum((int) wet);
                    hwview.setTemp((int) temp);
                    // 关闭连接和流
                    reader.close();
                    connection.disconnect();

                    // 此处为UI线程，可以更新UI
//                    runOnUiThread(() -> {
//                        // 更新UI，例如显示获取到的数据
//                    });
                } else {
                    // 处理错误
                    Looper.prepare();
                    Toast.makeText(hwview.getContext(),"该设备没有接入温度传感器!", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }catch (SocketTimeoutException e) {
                Looper.prepare();
                Toast.makeText(hwview.getContext(),"该设备没有接入温度传感器!", Toast.LENGTH_SHORT).show();
                Looper.loop();
                // 处理超时异常
                e.printStackTrace();
                // 可以在这里设置超时的处理逻辑

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}