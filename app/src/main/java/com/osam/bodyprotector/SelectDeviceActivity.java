package com.osam.bodyprotector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity {


    public DeviceListAdapter deviceListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        // Bluetooth Setup
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get List of Paired Bluetooth Device
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        List<Object> deviceList = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                DeviceData deviceInfoModel = new DeviceData(deviceName,deviceHardwareAddress);
                deviceList.add(deviceInfoModel);
            }
            // Display paired device using recyclerView
            ListView recyclerView = findViewById(R.id.DeviceList);
            deviceListAdapter = new DeviceListAdapter(this,deviceList);
            recyclerView.setAdapter(deviceListAdapter);
            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Context context = getBaseContext();
                    Intent intent = new Intent(context,BluetoothActivity.class);
                    DeviceData data = (DeviceData) deviceListAdapter.getItem(position);
                    // Send device details to the MainActivity
                    intent.putExtra("deviceName", data.getDeviceName());
                    intent.putExtra("deviceAddress", data.getDeviceHardwareAddress());
                    // Call MainActivity
                    context.startActivity(intent);
                }
            });
        } else {
            View view = findViewById(R.id.DeviceList);
            Snackbar snackbar = Snackbar.make(view, "블루투스 기능과 페어링할 기기를 켜주십시오.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View view) { }
            });
            snackbar.show();
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { //각각의 디바이스로부터 정보를 받으려면 만들어야함
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            DeviceData deviseClass = new DeviceData();
            deviseClass.deviceName = device.getName();
            deviseClass.deviceHardwareAddress = device.getAddress();
            deviceListAdapter.deviceList.add(deviseClass);
            deviceListAdapter.notifyDataSetChanged();
        }
    };

    public static class DeviceListAdapter extends BaseAdapter {
        private Context context;
        private List<Object> deviceList;

        public DeviceListAdapter(Context context, List<Object> deviceList) {
            this.context = context;
            this.deviceList = deviceList;
        }

        private class ViewHolder {
            public TextView textName;
            public TextView textAddress;
        }

        @Override
        public int getCount() {
            return deviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_device_info, null);

                holder.textName = convertView.findViewById(R.id.textViewDeviceName);
                holder.textAddress = convertView.findViewById(R.id.textViewDeviceAddress);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            final DeviceData deviceInfoModel = (DeviceData) deviceList.get(position);
            holder.textName.setText(deviceInfoModel.getDeviceName());
            holder.textAddress.setText(deviceInfoModel.getDeviceHardwareAddress());


            return convertView;
        }
    }
}