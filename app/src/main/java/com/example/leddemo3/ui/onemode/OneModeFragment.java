package com.example.leddemo3.ui.onemode;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.leddemo3.R;
import com.example.leddemo3.databinding.FragmentOnemodeBinding;
import com.example.leddemo3.global.GlobalHttpUrl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OneModeFragment extends Fragment {

    private FragmentOnemodeBinding binding;
    private LinearLayout ll;
    private TextView tv;
    //String urlport ="http://192.168.0.33:3333/";
    private int red=0;
    private int green=0;
    private int blue=0;
    private ColorPickerView colorPickerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OneModeViewModel galleryViewModel =
                new ViewModelProvider(this).get(OneModeViewModel.class);

        binding = FragmentOnemodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        ll = (LinearLayout) view.findViewById(R.id.ll_color);
        tv = (TextView) view.findViewById(R.id.tv_info);
        Button button=(Button)view.findViewById(R.id.submitColor);
        colorPickerView = new ColorPickerView(view.getContext());
        ll.addView(colorPickerView);
        colorPickerView.setOnColorBackListener(new ColorPickerView.OnColorBackListener() {
            @Override
            public void onColorBack(int a, int r, int g, int b) {
                red=r;
                green=g;
                blue=b;
                tv.setText("R：" + r + "\tG：" + g + "\tB：" + b + "\t" + colorPickerView.getStrColor());
                tv.setTextColor(Color.argb(a, r, g, b));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorHttpRequest(v);
                // 在这里处理按钮点击事件
                // Toast.makeText(MainActivity, "开灯了!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void colorHttpRequest(View view) {
        if(!GlobalHttpUrl.isUrlNull()){
            Toast.makeText(view.getContext(),"请先选择要控制的设备!", Toast.LENGTH_SHORT).show();
            return;
        }
        String urlString = "http://"+ GlobalHttpUrl.url +":3333/ledtype?typenum=0&red="+String.valueOf(red)+"&green="+String.valueOf(green)+"&blue="+String.valueOf(blue); // 替换为你的URL
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