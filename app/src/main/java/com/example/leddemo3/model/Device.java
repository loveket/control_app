package com.example.leddemo3.model;

public class Device {
    public int id;//序号
    public String devicename;//设备名
    public String deviceip;//设备地址
    public int status;//设备状态

    public Device(){

    }

    public Device(String devicename,String deviceip,int status) {
        this.devicename = devicename;
        this.deviceip = deviceip;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getDeviceip() {
        return deviceip;
    }

    public void setDeviceip(String deviceip) {
        this.deviceip = deviceip;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", devicename='" + devicename + '\'' +
                ", deviceip=" + deviceip +
                ", status=" + status +
                '}';
    }

}
