package com.aic.aicdetactor.fragment;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.BlueToothBindDevListAdapter;
import com.aic.aicdetactor.adapter.BlueToothDevListAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class BlueTooth_Fragment  extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.bluetooth_layout, container, false);
		
		ExpandableListView DevBindedlist = (ExpandableListView) view.findViewById(R.id.bluetooth_device_binded_list);
		BlueToothBindDevListAdapter btBindDevListAdapter = new BlueToothBindDevListAdapter(getActivity());
		DevBindedlist.setAdapter(btBindDevListAdapter);
		DevBindedlist.setGroupIndicator(null);
		
		ListView Devlist = (ListView) view.findViewById(R.id.bluetooth_device_list);
		BlueToothDevListAdapter blueToothDevListAdapter = new BlueToothDevListAdapter(getActivity());
		Devlist.setAdapter(blueToothDevListAdapter);
		return view;
	}
}
