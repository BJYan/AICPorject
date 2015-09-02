package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.check.StationActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.util.SystemUtil;


public class RouteFragment extends Fragment {
	//
	private ListView mListView;
	private final String TAG = "luotest";
	private RadioGroup mRadioGroup = null; 



	private myApplication app = null;
	private final int ROUTE_XJ =0;
	private final int ROUTE_Temp= 1;
	private final int ROUTE_Spec= 2;
	private int mSelectedRadioIndex =0;
	//private String name = null;
	//private String pwd= null;
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
		Log.d(TAG,"onCreateView()>>");
		// TODO Auto-generated method stub
		app =(myApplication) RouteFragment.this.getActivity().getApplication();
		View view = inflater.inflate(R.layout.route_activity, container, false);
	
		mListView = (ListView) view.findViewById(R.id.listView);	

		mRadioGroup = (RadioGroup)view.findViewById(R.id.route_group);
		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg0.getCheckedRadioButtonId()){
				case R.id.route_radioButton1:					
					initListData(ROUTE_XJ);
					mSelectedRadioIndex =0;
					break;
				case R.id.route_radioButton2:
					initListData(ROUTE_Spec);
					mSelectedRadioIndex =1;
					break;
				case R.id.route_radioButton3:
					initListData(ROUTE_Temp);
					mSelectedRadioIndex =1;
					break;
				}
			}
			
		});	
		mItemDatas = new ArrayList<Map<String, String>>();
		//initListData(ROUTE_XJ);
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
				app.gRouteName = mapItem.get(CommonDef.route_info.NAME);
				 app.mRouteIndex = arg2;
				Intent intent = new Intent();
				intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX,
						arg2);
				app.setCurrentRouteIndex(arg2);
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
		Log.d(TAG,"onCreateView()<<");
		return view;
		}
	
	/**
	 * 0:计划巡检
	 * 1:临时任务
	 * @param type
	 */
	void initListData(int types) {
		final int type =types;
		Log.d(TAG,"initListData()>>");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (type == ROUTE_XJ) {
					Log.d(TAG,
							"in init() 1 start "
									+ SystemUtil
											.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

					int iRouteCount = app.InitData();
					Log.d(TAG,
							"in init() 2 start "
									+ SystemUtil
											.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					CheckStatus status = null;
					mItemDatas.clear();
					for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
						try {
							Log.d(TAG,
									"in init() for start i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
							Map<String, String> map = new HashMap<String, String>();
							status = app.getNodeCount(null, 0, routeIndex);
							status.setContext(RouteFragment.this.getActivity()
									.getApplicationContext());
							map.put(CommonDef.route_info.NAME,
									app.getRoutName(routeIndex));
							map.put(CommonDef.route_info.DEADLINE,
									status.mLastTime);
							map.put(CommonDef.route_info.STATUS,
									status.getStatus());

							map.put(CommonDef.route_info.PROGRESS,
									status.mCheckedCount + "/" + status.mSum);
							// map.put("progress", "2/");
							String index = "" + (routeIndex + 1);
							map.put(CommonDef.route_info.INDEX, index);

							mItemDatas.add(map);
							Log.d(TAG,
									"in init() for end i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (type == ROUTE_Temp) {
					mItemDatas.clear();
				}
				mHander.sendMessage(mHander.obtainMessage(MSG_UPDATE_LISTVIEW));
				Log.d(TAG,"initListData()<<");
			}
		}).start();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		initListData(mSelectedRadioIndex);
		super.onResume();
	}
	
	private final int MSG_UPDATE_LISTVIEW =0;
	Handler mHander= new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case MSG_UPDATE_LISTVIEW:
			
			if (mListViewAdapter != null) {
				mListViewAdapter.notifyDataSetChanged();
			}
			break;
			}
			super.handleMessage(msg);
		}
		
	};
}
