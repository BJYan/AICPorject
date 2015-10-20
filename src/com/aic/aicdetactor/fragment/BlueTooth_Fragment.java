package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.BlueToothBindDevListActivity;
import com.aic.aicdetactor.adapter.BlueToothBindDevListAdapter;
import com.aic.aicdetactor.adapter.BlueToothDevListAdapter;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlueTooth_Fragment  extends Fragment implements OnClickListener{
	BluetoothAdapter adapter;
	List<BluetoothDevice> BondedDevices;
	List<BluetoothDevice> BtDevices;
	BlueToothDevListAdapter blueToothDevListAdapter;
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            BtDevices.add(device);
	            blueToothDevListAdapter.notifyDataSetChanged();
	        }
	        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
	        	Toast.makeText(getActivity(), "������ɣ�", Toast.LENGTH_SHORT).show();
	        }
	    }
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		adapter = BluetoothAdapter.getDefaultAdapter();
		BondedDevices = new ArrayList<BluetoothDevice>();
		BtDevices = new ArrayList<BluetoothDevice>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View BlueToothView = inflater.inflate(R.layout.bluetooth_layout, container, false);
		
		ExpandableListView DevBindedlist = (ExpandableListView) BlueToothView.findViewById(R.id.bluetooth_device_binded_list);
		BondedDevices = getBondedDevices();
		BlueToothBindDevListAdapter btBindDevListAdapter = new BlueToothBindDevListAdapter(getActivity(), BondedDevices);
		DevBindedlist.setAdapter(btBindDevListAdapter);
		DevBindedlist.setGroupIndicator(null);
		
		ListView Devlist = (ListView) BlueToothView.findViewById(R.id.bluetooth_device_list);
		blueToothDevListAdapter = new BlueToothDevListAdapter(getActivity(),BtDevices);
		Devlist.setAdapter(blueToothDevListAdapter);
		
		TextView btSerachBtn = (TextView) BlueToothView.findViewById(R.id.bt_search_btn);
		btSerachBtn.setOnClickListener(this);
		return BlueToothView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter);
		adapter.startDiscovery();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
	}

	 public List<BluetoothDevice> getBondedDevices(){
		 List<BluetoothDevice> bondedDevs = new ArrayList<BluetoothDevice>();
		 Set<BluetoothDevice> devices = new HashSet<BluetoothDevice>();
		 if(adapter != null){
			 if(!adapter.isEnabled()){
				 Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				 startActivity(intent);
			 }
			 devices = adapter.getBondedDevices();
			 
			 if(devices.size()>0) {
				 	for(Iterator<BluetoothDevice> iterator = devices.iterator();iterator.hasNext();) {
				 		BluetoothDevice device = (BluetoothDevice)iterator.next(); 
				 		bondedDevs.add(device);
				 	}     
			 	}
		 	}
		 return bondedDevs;
	 }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bt_search_btn:
			Intent intent = new Intent();
			intent.setClass(getActivity(), BlueToothBindDevListActivity.class);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}
}
