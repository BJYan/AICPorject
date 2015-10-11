package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.MessageAdapter;
import com.aic.aicdetactor.adapter.MessageListViewAdapter;
import com.aic.aicdetactor.adapter.NetWorkSettingAdapter;
import com.aic.aicdetactor.adapter.NetworkViewPagerAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.TemporaryDataBean;
import com.aic.aicdetactor.database.TemporaryRouteDao;
import com.aic.aicdetactor.fragment.DownLoadFragment.MyOnPageChangeListener;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.Toast;

public class Message_Fragment extends Fragment implements OnClickListener{
	ListView mListView = null;
	private RadioGroup mRadioGroup = null; 
	private int mRadioIndex = 0;
	private RadioButton mRadioButton_left = null;
	private List<Map<String, String>> mItemDatas = null;
	private final String TAG = "Message_Fragment";
	private SimpleAdapter mListViewAdapter = null;
	private myApplication app = null;
	//GridView mGridView = null;
	TabHost tabHost;
	ViewPager viewPager;
	LayoutInflater inflater;
	ArrayList<View> listViews;
	TextView MsgTaskStartTime,MsgTaskEndTime;
	TextView MsgStartTime,MsgEndTime;
	
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
		this.inflater = inflater;
		app =(myApplication) Message_Fragment.this.getActivity().getApplication();
		View view = inflater.inflate(R.layout.message_fragment_layout, container, false);
		
		
		tabHost = (TabHost)view.findViewById(R.id.msg_tabhost);  
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost  
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("任务通知")  
                .setContent(  
                R.id.view1));
  
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("消息通知")  
                .setContent(R.id.view2)); 
        
        viewPager = (ViewPager)view.findViewById(R.id.msg_vPager); 
        listViews = new ArrayList<View>();
        listViews.add(inflater.inflate(R.layout.message_task_notice_layout, null));
        listViews.add(inflater.inflate(R.layout.message_notice_layout, null));
        
        MsgTaskStartTime = (TextView) listViews.get(0).findViewById(R.id.message_task_notice_start_date);
        MsgTaskStartTime.setOnClickListener(this);
        MsgTaskEndTime = (TextView) listViews.get(0).findViewById(R.id.message_task_notice_end_date);
        MsgTaskEndTime.setOnClickListener(this);
        MsgStartTime = (TextView) listViews.get(1).findViewById(R.id.message_notice_start_date);
        MsgStartTime.setOnClickListener(this);
        MsgEndTime = (TextView) listViews.get(1).findViewById(R.id.message_notice_end_date);
        MsgEndTime.setOnClickListener(this);
        
        MessageListViewAdapter msgListViewAdapter = new MessageListViewAdapter(getActivity().getApplicationContext());
        for(int i=0;i<listViews.size();i++){
        	ListView listView = (ListView) listViews.get(i).findViewById(R.id.msg_listView1);
        	listView.setAdapter(msgListViewAdapter);
        }
        MessageAdapter messageAdapter = new MessageAdapter(listViews);
        viewPager.setAdapter(messageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){  
            @Override  
            public void onTabChanged(String tabId){  
                MLog.Logi("DownLoadFragment--tabId--=", tabId);  
                if(tabId.equals("tab1")) viewPager.setCurrentItem(0);
                if(tabId.equals("tab2")) viewPager.setCurrentItem(2);
                if(tabId.equals("tab3")) viewPager.setCurrentItem(1);
            }  
        });

		/* mListView = ( ListView)view.findViewById(R.id.msg_listView1);
		 * mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		mRadioButton_left = (RadioButton)view.findViewById(R.id.msg_radioButton1);	
		mRadioButton_left.setChecked(true);
		mRadioGroup = (RadioGroup)view.findViewById(R.id.meg_group);
		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg0.getCheckedRadioButtonId()){
				case R.id.msg_radioButton1:
					
					initData(TemporaryRouteDao.SEACHER_TYPE_TODAY);
					break;
				case R.id.msg_radioButton2:
					initData(TemporaryRouteDao.SEACHER_TYPE_ALL);
					
					break;
				case R.id.msg_radioButton3:
					initData(TemporaryRouteDao.SEACHER_TYPE_UNREAD);
					
					break;
				}
			}
			
		});	
		mItemDatas = new ArrayList<Map<String, String>>();
		mListViewAdapter = new SimpleAdapter(this.getActivity(), mItemDatas,
				R.layout.message_item, new String[] {
						CommonDef.Temporary_Check_info.type,
						CommonDef.Temporary_Check_info.past_time,
						CommonDef.Temporary_Check_info.receive_date,
						CommonDef.Temporary_Check_info.title,
						}, new int[] {
						R.id.msg_type, R.id.juli, R.id.meg_time,
						R.id.msg_title });
		
		initData(TemporaryRouteDao.SEACHER_TYPE_UNREAD);*/
		return view;
	}
	
	/**
	 * 默认查询未读的消息
	 */
	void initData(int type){
		if(app.mWorkerName == null
				&&app.mWorkerPwd ==null
				){
			Toast.makeText(this.getActivity(), "您还没登录", Toast.LENGTH_SHORT).show();
			return;
			
		}
		TemporaryRouteDao info = new TemporaryRouteDao(this.getActivity().getApplicationContext());
		
		 List<TemporaryDataBean>  list=		info.queryTemporaryInfoList(type,
				app.mWorkerName,
				app.mWorkerPwd);
		 
		 

		mItemDatas.clear();
		for (int index = 0; index < list.size(); index++) {
			try {
				Log.d(TAG, "in initData() for start i=" + index + ","
						+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
				Map<String, String> map = new HashMap<String, String>();
				TemporaryDataBean bean = list.get(index);
				
				map.put(CommonDef.Temporary_Check_info.type,bean.Title);
				map.put(CommonDef.Temporary_Check_info.receive_date,bean.Receive_Time);
				
				map.put(CommonDef.Temporary_Check_info.past_time,SystemUtil.getDiffDate(bean.Receive_Time,this.getActivity().getApplicationContext()));
				
				mItemDatas.add(map);
				Log.d(TAG, "in initData() for end i=" + index + ","
						+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		if(mListViewAdapter !=null){
		mListViewAdapter.notifyDataSetChanged();
		}
	
	}
	void set(){
		// 点击改变选中listItem的背景色
//		if (clickTemp == position) {
//		layout.setBackgroundResource(R.drawable.check_in_gdv_bg_s);
//		} else {
//		layout.setBackgroundColor(Color.TRANSPARENT);
//		}}
	}
	
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.message_task_notice_start_date:
			final PopupWindow taskstartTimePop = new PopupWindow(listViews.get(0),
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			View PopupWindowview_task_start = inflater.inflate(R.layout.calendar_view_layout, null);
			CalendarView calendarView_task_start = (CalendarView) PopupWindowview_task_start.findViewById(R.id.calendar);
			calendarView_task_start.setOnDateChangeListener(new OnDateChangeListener() {  
	        	  
	            @Override  
	            public void onSelectedDayChange(CalendarView arg0, int arg1,  
	                    int arg2, int arg3) {  
	                arg2 = arg2 + 1;  
	                Log.i(TAG, "-------------" + arg1 + "-" + arg2 + "-"  
	                        + arg3); 
	                MsgTaskStartTime.setText(arg1 + "-" + arg2 + "-"+ arg3);
	                taskstartTimePop.dismiss();
	            }  
	        });
			taskstartTimePop.setContentView(PopupWindowview_task_start);
			taskstartTimePop.showAsDropDown(arg0);
			break;
		case R.id.message_task_notice_end_date:
			final PopupWindow taskendTimePop = new PopupWindow(listViews.get(0),
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true); 
			View PopupWindowview_task_end = inflater.inflate(R.layout.calendar_view_layout, null);
			CalendarView calendarView_task_end = (CalendarView) PopupWindowview_task_end.findViewById(R.id.calendar);
			calendarView_task_end.setOnDateChangeListener(new OnDateChangeListener() {  
	        	  
	            @Override  
	            public void onSelectedDayChange(CalendarView arg0, int arg1,  
	                    int arg2, int arg3) {  
	                arg2 = arg2 + 1;  
	                Log.i(TAG, "-------------" + arg1 + "-" + arg2 + "-"  
	                        + arg3); 
	                MsgTaskEndTime.setText(arg1 + "-" + arg2 + "-"+ arg3);
	                taskendTimePop.dismiss();
	            }  
	        });
			taskendTimePop.setContentView(PopupWindowview_task_end);
			taskendTimePop.showAsDropDown(arg0);
			break;
		case R.id.message_notice_start_date:
			final PopupWindow startTimePop = new PopupWindow(listViews.get(0),
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			View PopupWindowview_start = inflater.inflate(R.layout.calendar_view_layout, null);
			CalendarView calendarView_start = (CalendarView) PopupWindowview_start.findViewById(R.id.calendar);
			calendarView_start.setOnDateChangeListener(new OnDateChangeListener() {  
	        	  
	            @Override  
	            public void onSelectedDayChange(CalendarView arg0, int arg1,  
	                    int arg2, int arg3) {  
	                arg2 = arg2 + 1;  
	                Log.i(TAG, "-------------" + arg1 + "-" + arg2 + "-"  
	                        + arg3); 
	                MsgStartTime.setText(arg1 + "-" + arg2 + "-"+ arg3);
	                startTimePop.dismiss();
	            }  
	        });
			startTimePop.setContentView(PopupWindowview_start);
			startTimePop.showAsDropDown(arg0);
			break;
		case R.id.message_notice_end_date:
			final PopupWindow endTimePop = new PopupWindow(listViews.get(0),
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true); 
			View PopupWindowview_end = inflater.inflate(R.layout.calendar_view_layout, null);
			CalendarView calendarView_end = (CalendarView) PopupWindowview_end.findViewById(R.id.calendar);
			calendarView_end.setOnDateChangeListener(new OnDateChangeListener() {  
	        	  
	            @Override  
	            public void onSelectedDayChange(CalendarView arg0, int arg1,  
	                    int arg2, int arg3) {  
	                arg2 = arg2 + 1;  
	                Log.i(TAG, "-------------" + arg1 + "-" + arg2 + "-"  
	                        + arg3); 
	                MsgEndTime.setText(arg1 + "-" + arg2 + "-"+ arg3);
	                endTimePop.dismiss();
	            }  
	        });
			endTimePop.setContentView(PopupWindowview_end);
			endTimePop.showAsDropDown(arg0);
			break;
		default:
			break;
		}
	}
}
