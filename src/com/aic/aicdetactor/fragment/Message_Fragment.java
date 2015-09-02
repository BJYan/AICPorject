package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.TemporaryDataBean;
import com.aic.aicdetactor.database.TemporaryRouteDao;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;

public class Message_Fragment extends Fragment {
	ListView mListView = null;
	private RadioGroup mRadioGroup = null; 
	private int mRadioIndex = 0;
	private RadioButton mRadioButton_left = null;
	private List<Map<String, String>> mItemDatas = null;
	private final String TAG = "luotest";
	private SimpleAdapter mListViewAdapter = null;
	private myApplication app = null;
	//GridView mGridView = null;
	//TabHost mTabHost = null;
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
		app =(myApplication) Message_Fragment.this.getActivity().getApplication();
		View view = inflater.inflate(R.layout.message, container, false);
		mListView = ( ListView)view.findViewById(R.id.msg_listView1);
		
		mListView.setOnItemClickListener(new OnItemClickListener(){

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
		
		initData(TemporaryRouteDao.SEACHER_TYPE_UNREAD);
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
}
