package com.example.android.blutoothfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
/*Blutooth finder
*
* It is an android app to find blutooth devices*/
public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView status;
    Button search;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String> listOfDevices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ArrayAdapter adapter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action",action);
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Log.i("Discovery:","Started");
            }else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = "";
                String  address = "";
                String Rssi = "";
                StringBuilder item = new StringBuilder();
                address = device.getAddress();
                if (!addresses.contains(address)){
                    if(device.getName() !=null){
                        name = device.getName();
                        item.append(name+", ");
                    }

                    item.append(address+", ");
                    Rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));
                    item.append(Rssi);
                    addresses.add(address);
                    listOfDevices.add(item.toString());
                    adapter.notifyDataSetChanged();
                    Log.i("item",item.toString());
                }


            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                status.setText("Finished");
                search.setEnabled(true);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.state);
        search = findViewById(R.id.button);
        listView = findViewById(R.id.listOfDevices);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listOfDevices);
        listView.setAdapter(adapter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver,intentFilter);


    }
    public void search(View view){
        status.setText("Searching...");
        search.setEnabled(false);
        listOfDevices.clear();
        bluetoothAdapter.startDiscovery();
    }
}