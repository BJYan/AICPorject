package com.aic.aicdetactor.fragment;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Top_Navigation_Fragment extends Fragment {

	public String TAG = "luotest";
	TextView route = null;
	TextView station  = null;
	TextView device = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.top_navitation, container, false);
		route = (TextView) view.findViewById(R.id.station_text_name);
		station = (TextView) view.findViewById(R.id.stationName);
		device = (TextView) view.findViewById(R.id.deviceName);
		//route.setText("巡检路线");
		//station.setText("站点名称");
		//device.setText("设备名称");
		ImageView imageView = (ImageView) view.findViewById(R.id.backImage);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d(TAG, "imageView.setOnClickListener");
				// TODO Auto-generated method stub
				Top_Navigation_Fragment.this.getActivity().finish();
			}

		});
		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		route.setText(((myApplication) Top_Navigation_Fragment.this.getActivity().getApplication()).gRouteName);
//		station.setText(((myApplication) Top_Navigation_Fragment.this.getActivity().getApplication()).gStationName);
//		device.setText(((myApplication) Top_Navigation_Fragment.this.getActivity().getApplication()).gDeviceName);

		
		super.onActivityCreated(savedInstanceState);
	}

}
