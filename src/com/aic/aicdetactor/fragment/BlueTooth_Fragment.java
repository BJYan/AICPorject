package com.aic.aicdetactor.fragment;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.BlueToothBindDevListActivity;
import com.aic.aicdetactor.adapter.BlueToothBindDevListAdapter;
import com.aic.aicdetactor.adapter.BlueToothDevListAdapter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class BlueTooth_Fragment  extends Fragment implements OnClickListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View BlueToothView = inflater.inflate(R.layout.bluetooth_layout, container, false);
		
		ExpandableListView DevBindedlist = (ExpandableListView) BlueToothView.findViewById(R.id.bluetooth_device_binded_list);
		BlueToothBindDevListAdapter btBindDevListAdapter = new BlueToothBindDevListAdapter(getActivity());
		DevBindedlist.setAdapter(btBindDevListAdapter);
		DevBindedlist.setGroupIndicator(null);
		
		ListView Devlist = (ListView) BlueToothView.findViewById(R.id.bluetooth_device_list);
		BlueToothDevListAdapter blueToothDevListAdapter = new BlueToothDevListAdapter(getActivity());
		Devlist.setAdapter(blueToothDevListAdapter);
		
		TextView btSerachBtn = (TextView) BlueToothView.findViewById(R.id.bt_search_btn);
		btSerachBtn.setOnClickListener(this);
		return BlueToothView;
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
