package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.check.StationActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.SwitchView;

public class RouteFragment extends Fragment {
	//
	private ListView mListView;
	String TAG = "luotest";
	
	private SwitchView mSwitch = null;
	
	private String name = null;
	private String pwd= null;
	private List<Map<String, String>> mItemDatas = null;
	private SimpleAdapter mListViewAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.route_activity, container, false);
	//	mBTName = (TextView)view.findViewById(R.id.sensor_name2);
		mListView = (ListView) view.findViewById(R.id.listView);
		mSwitch = (SwitchView) view.findViewById(R.id.link_switch);

		mItemDatas = new ArrayList<Map<String, String>>();
		initListData();
		mListViewAdapter = new SimpleAdapter(this.getActivity(), mItemDatas,
				R.layout.checkitem, new String[] {
						CommonDef.route_info.INDEX,
						CommonDef.route_info.NAME,
						CommonDef.route_info.DEADLINE,
						CommonDef.route_info.STATUS,
						CommonDef.route_info.PROGRESS }, new int[] {
						R.id.index, R.id.pathname, R.id.deadtime,
						R.id.status, R.id.progress });
		Log.d(TAG,
				"in init() setAdapter"
						+ SystemUtil
								.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
		mListView.setAdapter(mListViewAdapter);
		Log.d(TAG,
				"in init() after setAdapter"
						+ SystemUtil
								.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, String> mapItem = (HashMap<String, String>) (mListView
						.getItemAtPosition(arg2));
				Log.d(TAG,
						"MAPITEM is "
								+ mapItem.toString()
								+ " pathname is "
								+ (String) mapItem
										.get(CommonDef.route_info.NAME));
				((myApplication) RouteFragment.this.getActivity().getApplication()).gRouteName = mapItem.get(CommonDef.route_info.NAME);
				 ((myApplication) RouteFragment.this.getActivity().getApplication()).mRouteIndex = arg2;
				Intent intent = new Intent();
				intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX,
						arg2);
				((myApplication) RouteFragment.this.getActivity().getApplication())
						.setCurrentRouteIndex(arg2);
				intent.putExtra(CommonDef.ROUTE_CLASS_NAME, "计划巡检");
				intent.putExtra(CommonDef.route_info.NAME,
						(String) mapItem.get(CommonDef.route_info.NAME));
				intent.putExtra(CommonDef.route_info.INDEX,
						(String) mapItem.get(CommonDef.route_info.INDEX));
				intent.setClass(RouteFragment.this.getActivity().getApplicationContext(),
						StationActivity.class);
				startActivity(intent);
			}
		});
	
		return view;
		}
	
	void initListData() {	
	
		Log.d(TAG, "in init() 1 start " + SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
		
		int iRouteCount = ((myApplication) RouteFragment.this.getActivity().getApplication()).InitData();
		Log.d(TAG, "in init() 2 start " + SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
		CheckStatus status = null;
		mItemDatas.clear();
		for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
			try {
				Log.d(TAG, "in init() for start i=" + routeIndex + ","
						+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
				Map<String, String> map = new HashMap<String, String>();
				status = ((myApplication) RouteFragment.this.getActivity().getApplication()).getNodeCount(null,
						0, routeIndex);
				status.setContext(RouteFragment.this.getActivity().getApplicationContext());
				map.put(CommonDef.route_info.NAME,
						((myApplication) RouteFragment.this.getActivity().getApplication())
								.getRoutName(routeIndex));
				map.put(CommonDef.route_info.DEADLINE, status.mLastTime);
				map.put(CommonDef.route_info.STATUS, status.getStatus());

				map.put(CommonDef.route_info.PROGRESS, status.mCheckedCount
						+ "/" + status.mSum);
				// map.put("progress", "2/");
				String index = "" + (routeIndex + 1);
				map.put(CommonDef.route_info.INDEX, index);

				mItemDatas.add(map);
				Log.d(TAG, "in init() for end i=" + routeIndex + ","
						+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(mListViewAdapter !=null){
		mListViewAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		initListData();
		super.onResume();
	}
	
	
}
