package com.osam.bodyprotector;

public class DeviceData {


    public String deviceName, deviceHardwareAddress;

    public DeviceData(){}

    public DeviceData(String deviceName, String deviceHardwareAddress){
        this.deviceName = deviceName;
        this.deviceHardwareAddress = deviceHardwareAddress;
    }

    public String getDeviceName(){return deviceName;}

    public String getDeviceHardwareAddress(){return deviceHardwareAddress;}

}
