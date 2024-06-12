package com.example.leddemo3.ui.devicemode;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.leddemo3.R;

import java.util.ArrayList;

public class DeviceAdapter extends BaseAdapter {
    ArrayList<String> deviceArr;
    ArrayList<String> deviceIPArr;
    ArrayList<Integer> deviceStatusArr;
    Context context;

    public DeviceAdapter(ArrayList<String> deviceArr, ArrayList<String> deviceIPArr, ArrayList<Integer> deviceStatusArr, Context context) {
        this.deviceArr = deviceArr;
        this.deviceIPArr = deviceIPArr;
        this.deviceStatusArr = deviceStatusArr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return deviceArr!=null?deviceArr.size():0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_devicelayout,null);
            viewHolder=new ViewHolder();
            viewHolder.tName=convertView.findViewById(R.id.deviceitem_name);
            viewHolder.tIP=convertView.findViewById(R.id.device_ip);
            viewHolder.status=convertView.findViewById(R.id.device_status);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tName.setText(deviceArr.get(position));
        viewHolder.tIP.setText(deviceIPArr.get(position));
        if (deviceStatusArr.get(position)==0){
            viewHolder.status.setTextColor(Color.parseColor("#ff0000"));
            viewHolder.status.setText("离线");
        }else if (deviceStatusArr.get(position)==1){
            viewHolder.status.setTextColor(Color.parseColor("#00ff33"));
            viewHolder.status.setText("在线");
        }
        return convertView;
    }
    class ViewHolder{
        TextView tName;
        TextView tIP;
        TextView status;
    }
}
