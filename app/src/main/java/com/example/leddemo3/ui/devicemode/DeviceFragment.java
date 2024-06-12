package com.example.leddemo3.ui.devicemode;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.leddemo3.R;
import com.example.leddemo3.databinding.FragmentDeviceBinding;
import com.example.leddemo3.db.DeviceSqlite;
import com.example.leddemo3.global.GlobalHttpUrl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

public class DeviceFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private FragmentDeviceBinding binding;
    //String urlport = "http://192.168.0.33:3333/";
    //测试数据
    private ArrayList<String> deviceArr;
    private ArrayList<Integer> deviceStatusArr;
    private ArrayList<String> deviceIPArr;
    public DeviceSqlite mHelper;
    public DeviceAdapter adapter;
    public ListView lv;
    Handler handler = new Handler();
    Runnable runnable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DeviceViewModel homeViewModel =
                new ViewModelProvider(this).get(DeviceViewModel.class);

        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deviceArr = new ArrayList<>();
        deviceStatusArr = new ArrayList<>();
        deviceIPArr = new ArrayList<>();
        adapter = new DeviceAdapter(deviceArr, deviceIPArr, deviceStatusArr, view.getContext());
        lv = view.findViewById(R.id.listDevice);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(this);
        mHelper = new DeviceSqlite(view.getContext());
        EditText devicename = view.findViewById(R.id.edevice_name);
        EditText deviceip = view.findViewById(R.id.edevice_ip);
        Button submitdevice = view.findViewById(R.id.submitDevice);

        //添加设备
        submitdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess=AddDevice(devicename.getText().toString(), deviceip.getText().toString(), v);
                if (isSuccess){
                    devicename.setText("");
                    deviceip.setText("");
                }
            }
        });

        selectAllData();
        TickCkeckOnlineStatus();


    }
    private void selectAllData() {
        Cursor cursor = mHelper.getData();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("DEVICENAME"));
                @SuppressLint("Range") String ip = cursor.getString(cursor.getColumnIndex("DEVICEIP"));
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex("STATUS"));
                deviceArr.add(name);
                deviceIPArr.add(ip);
                deviceStatusArr.add(status);
                // 使用数据
            } while (cursor.moveToNext());
        }
        // 关闭Cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        binding = null;
    }

    private boolean AddDevice(String dname, String dip, View v) {
        if (dname.length() == 0 || dip.length() == 0) {
            Toast.makeText(getContext(), "设备名或设备IP不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (deviceArr.contains(dname)||deviceIPArr.contains(dip)){
                Toast.makeText(getContext(), "设备名或设备IP不能重复!", Toast.LENGTH_SHORT).show();
                return false;
        }
        boolean result = mHelper.insertData(dname, dip, 0);
        if (result) {
            deviceArr.add(dname);
            deviceIPArr.add(dip);
            deviceStatusArr.add(0);
            adapter.notifyDataSetChanged();
            Toast.makeText(v.getContext(), "添加设备成功!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(v.getContext(), "添加设备失败!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        AlertDialog dialog = builder.create();
        View dialogView = View.inflate(view.getContext(), R.layout.dialog, null);
        TextView dName = dialogView.findViewById(R.id.dialogName);
        TextView dControl = dialogView.findViewById(R.id.dialogControl);
        TextView dDelete = dialogView.findViewById(R.id.dialogDelete);
        String devicename=deviceArr.get(position);
        dName.setText(devicename);
        //控制
        dControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControlDevice(position, v);
                dialog.dismiss();
            }
        });
        //删除
        dDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDevice(position, v);
                dialog.dismiss();
            }
        });
        //********
        dialog.setView(dialogView);
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager wm = getActivity().getWindowManager();
        Point p = new Point();
        wm.getDefaultDisplay().getSize(p);
        params.width = p.x / 2;
        dialog.getWindow().setAttributes(params);
        return false;
    }

    public void ControlDevice(int p, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("控制");
        builder.setMessage("确定控制该设备吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ip = deviceIPArr.get(p);
                String name = deviceArr.get(p);
                GlobalHttpUrl.url = ip;
                Toast.makeText(v.getContext(), "正在控制设备:" + name, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void DeleteDevice(int p, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("删除");
        builder.setMessage("确定要删除吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = deviceArr.get(p);
                deviceArr.remove(p);
                mHelper.deleteData(name);
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    @SuppressLint("StaticFieldLeak")
    public void ScanIP(String scanip, int i) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                // 这里进行网络请求操作
                try {
                    InetAddress serverAddress = InetAddress.getByName(scanip);
                    boolean reachable = serverAddress.isReachable(2000); // 超时时间设置为5秒
                    if (reachable) {
                        deviceStatusArr.set(i,1);
                        mHelper.updateData(scanip,1);
                    } else {
                        deviceStatusArr.set(i,0);
                        mHelper.updateData(scanip,0);
                    }
                    //adapter.notifyDataSetChanged();
                } catch (RuntimeException e) {
                    Log.e("Ping", "UnknownHostException", e);
                } catch (IOException e) {
                    Log.e("Ping", "IOException", e);
                }
                return "网络请求结果";
            }

//            @Override
//            protected void onPostExecute(String result) {
//                // 在这里更新UI
//            }
        }.execute();


    }
    private void TickCkeckOnlineStatus(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // 你的任务代码
                for (int i = 0; i < deviceIPArr.size(); i++) {
                    ScanIP(deviceIPArr.get(i),i);
                }
                adapter.notifyDataSetChanged();
                // 每隔1秒重复执行任务
                new Handler().postDelayed(this, 1000);
            }
        });
    }
}