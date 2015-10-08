package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TabWidget;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.RouteNormalListAdapter;
import com.aic.aicdetactor.adapter.RoutePageAdapter;
import com.aic.aicdetactor.adapter.RouteSpecListAdapter;
import com.aic.aicdetactor.adapter.SearchAdapter;
import com.aic.aicdetactor.adapter.SearchDatabaseExListAdapter;
import com.aic.aicdetactor.adapter.SearchLocalListAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.check.StationActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.fragment.SearchFragment.MyOnPageChangeListener;
import com.aic.aicdetactor.util.MLog;
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
	private String mRouteTypeName =null;
	
	TabHost tabHost;
	ViewPager viewPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MLog.Logd(TAG,"onCreateView()>>");
		// TODO Auto-generated method stub
		app =(myApplication) RouteFragment.this.getActivity().getApplication();
		
		
		View searchView = inflater.inflate(R.layout.route_fragment_layout, container, false);
		tabHost = (TabHost)searchView.findViewById(R.id.route_tabhost);  
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost  
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(this.getActivity().getString(R.string.plancheck_name))  
                .setContent(  
                R.id.view1));
  
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(this.getActivity().getString(R.string.tem_spec))  
                .setContent(R.id.view2)); 
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(this.getActivity().getString(R.string.tem_check))  
                .setContent(R.id.view3)); 
        viewPager = (ViewPager)searchView.findViewById(R.id.route_vPager);
        TabWidget tabWidget =tabHost.getTabWidget();
        for (int i =0; i < tabWidget.getChildCount(); i++) {  
            //修改Tabhost高度和宽度
            tabWidget.getChildAt(i).getLayoutParams().height = 60;  
            tabWidget.getChildAt(i).getLayoutParams().width = 65;
            //修改显示字体大小
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
           }
        
        ArrayList<View> listViews = new ArrayList<View>();
        listViews.add(inflater.inflate(R.layout.route_normal_layout, null));
        listViews.add(inflater.inflate(R.layout.route_spec_layout, null));
        listViews.add(inflater.inflate(R.layout.route_temp_layout, null));
        
        ListView mNormalList = (ListView) listViews.get(0).findViewById(R.id.route_normal_list);
        RouteNormalListAdapter mNormalListAdapter = new RouteNormalListAdapter(getActivity().getApplicationContext(),RouteFragment.this.getActivity());
        mNormalList.setAdapter(mNormalListAdapter);
//        mNormalList.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//        	
//        });
        ListView mSpecList = (ListView) listViews.get(1).findViewById(R.id.route_spec_list);
        RouteSpecListAdapter mSpectListAdapter = new RouteSpecListAdapter(getActivity().getApplicationContext());
        mSpecList.setAdapter(mSpectListAdapter);
        
        RoutePageAdapter mPageAdapter = new RoutePageAdapter(listViews);
        viewPager.setAdapter(mPageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){  
            @Override  
            public void onTabChanged(String tabId){  
                Log.i("DownLoadFragment--tabId--=", tabId);  
                if(tabId.equals("tab1")) viewPager.setCurrentItem(0);
                if(tabId.equals("tab2")) viewPager.setCurrentItem(1);
                if(tabId.equals("tab3")) viewPager.setCurrentItem(2);
            }  
        });
		return searchView;
		
		
		
		
//		
//		View view = inflater.inflate(R.layout.route_activity, container, false);
//	
//		mListView = (ListView) view.findViewById(R.id.listView);	
//
//		mRadioGroup = (RadioGroup)view.findViewById(R.id.route_group);
//		
//		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
//
//			@Override
//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
//				// TODO Auto-generated method stub
//				switch(arg0.getCheckedRadioButtonId()){
//				case R.id.route_radioButton1:					
//					initListData(CommonDef.RouteType.Route_Normal);
//					mSelectedRadioIndex =0;
//					break;
//				case R.id.route_radioButton2:
//					initListData(CommonDef.RouteType.Route_Spec);
//					mSelectedRadioIndex =1;
//					break;
//				case R.id.route_radioButton3:
//					initListData(CommonDef.RouteType.Route_Tmp);
//					mSelectedRadioIndex =2;
//					break;
//				}
//			}
//			
//		});	
//		mItemDatas = new ArrayList<Map<String, String>>();
//		//initListData(ROUTE_XJ);
//		mListViewAdapter = new SimpleAdapter(this.getActivity(), mItemDatas,
//				R.layout.route_list_item, new String[] {
//						CommonDef.route_info.INDEX,
//						CommonDef.route_info.NAME,
//						CommonDef.route_info.DEADLINE,
//						//CommonDef.route_info.STATUS,
//						CommonDef.route_info.PROGRESS }, new int[] {
//						R.id.index, R.id.pathname, R.id.deadtime,
////						R.id.status, 
//						R.id.progress });
//		
//		MLog.Logd(TAG,
//				"in init() setAdapter"
//						+ SystemUtil
//								.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
//		mListView.setAdapter(mListViewAdapter);
//		mListView.setDividerHeight(10);
//		mListView.setDivider(new ColorDrawable(0xffffff));
//		MLog.Logd(TAG,
//				"in init() after setAdapter"
//						+ SystemUtil
//								.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				HashMap<String, String> mapItem = (HashMap<String, String>) (mListView
//						.getItemAtPosition(arg2));
//				MLog.Logd(TAG,
//						"MAPITEM is "
//								+ mapItem.toString()
//								+ " pathname is "
//								+ (String) mapItem
//										.get(CommonDef.route_info.NAME));
//				app.gRouteName = mapItem.get(CommonDef.route_info.NAME);
//				 app.mRouteIndex = arg2;
//				Intent intent = new Intent();
//				intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX,
//						arg2);
//				app.setCurrentRouteIndex(arg2);
//				switch(mSelectedRadioIndex){
//				case 0:
//					mRouteTypeName = getString(R.string.plancheck_name);
//					break;
//				case 2:
//					mRouteTypeName = getString(R.string.tem_check);
//					break;
//				case 1:
//					mRouteTypeName = getString(R.string.tem_spec);
//					break;
//				}
//				intent.putExtra(CommonDef.ROUTE_CLASS_NAME, mRouteTypeName);
//				intent.putExtra(CommonDef.route_info.NAME,
//						(String) mapItem.get(CommonDef.route_info.NAME));
//				intent.putExtra(CommonDef.route_info.INDEX,
//						(String) mapItem.get(CommonDef.route_info.INDEX));
//				intent.setClass(RouteFragment.this.getActivity().getApplicationContext(),
//						StationActivity.class);
//				startActivity(intent);
//			}
//		});
//		MLog.Logd(TAG,"onCreateView()<<");
//		return view;
		}
	
	/**
	 * 0:计划巡检
	 * 1:临时任务
	 * @param type
	 */
	void initListData(final CommonDef.RouteType type) {
		//final int type =types;
		MLog.Logd(TAG,"initListData()>>");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (type == CommonDef.RouteType.Route_Normal) {
					MLog.Logd(TAG,
							"in init() 1 start "
									+ SystemUtil
											.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

					int iRouteCount = app.InitData();
					MLog.Logd(TAG,
							"in init() 2 start "
									+ SystemUtil
											.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					CheckStatus status = null;
					mItemDatas.clear();
					for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
						try {
							MLog.Logd(TAG,
									"in init() for start i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
							Map<String, String> map = new HashMap<String, String>();
							status = app.getNodeCount(null, 0, routeIndex);
							status.setContext(RouteFragment.this.getActivity().getApplicationContext());
							map.put(CommonDef.route_info.NAME,
									app.getRoutName(routeIndex));
							map.put(CommonDef.route_info.DEADLINE,
									status.mLastTime);

							map.put(CommonDef.route_info.PROGRESS,
									status.mCheckedCount + "/" + status.mSum);
						
							String index = "" + (routeIndex + 1);
							map.put(CommonDef.route_info.INDEX, index);

							mItemDatas.add(map);
							MLog.Logd(TAG,
									"in init() for end i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (type == CommonDef.RouteType.Route_Spec) {
					mItemDatas.clear();
				}else if(type == CommonDef.RouteType.Route_Tmp){
					mItemDatas.clear();
				}
				mHander.sendMessage(mHander.obtainMessage(MSG_UPDATE_LISTVIEW));
				MLog.Logd(TAG,"initListData()<<");
			}
		}).start();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
//		if (mSelectedRadioIndex == 0) {
//			initListData(CommonDef.RouteType.Route_Normal);
//		} else if (mSelectedRadioIndex == 1) {
//			initListData(CommonDef.RouteType.Route_Spec);
//		} else if (mSelectedRadioIndex == 2) {
//			initListData(CommonDef.RouteType.Route_Tmp);
//		}
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
	
	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			tabHost.setCurrentTab(arg0);
		}
		
	}
}
