package com.example.leddemo3.ui.lightness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.leddemo3.MainActivity;
import com.example.leddemo3.R;
import com.example.leddemo3.databinding.FragmentLightnessBinding;
import com.example.leddemo3.databinding.FragmentOnemodeBinding;
import com.example.leddemo3.global.GlobalHttpUrl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LightnessFragment extends Fragment {

    private FragmentLightnessBinding binding;
    //String urlport ="http://192.168.0.33:3333/";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LightnessViewModel galleryViewModel =
                new ViewModelProvider(this).get(LightnessViewModel.class);

        binding = FragmentLightnessBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //亮度条
        SeekBar seekBar = view.findViewById(R.id.seekBarId);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 进度变化时的回调
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始拖动时的回调
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止拖动时的回调
                handlerLightness(seekBar,view);
                // 在这里处理按钮点击事件
            }
        });
    }
        public void handlerLightness(SeekBar seekBar,View v){
            if(!GlobalHttpUrl.isUrlNull()){
                Toast.makeText(v.getContext(),"请先选择要控制的设备!", Toast.LENGTH_SHORT).show();
                return;
            }
        String urlString = "http://"+ GlobalHttpUrl.url +":3333/lightness?lightnum="+String.valueOf(seekBar.getProgress()); // 替换为你的URL
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
                } else {
                    // 处理错误
                }
            } catch (Exception e) {
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