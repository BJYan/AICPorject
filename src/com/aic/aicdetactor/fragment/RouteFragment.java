package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.TempRouteActivity;
import com.aic.aicdetactor.adapter.LineListAdapter;
import com.aic.aicdetactor.adapter.RoutePageAdapter;
import com.aic.aicdetactor.adapter.SpinnerAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.LineType;
import com.aic.aicdetactor.comm.OrganizationType;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.util.MLog;


public class RouteFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	//
	private final String TAG = "RouteFragment";
	//private RadioGroup mRadioGroup = null; 
	private myApplication app = null;
	//private List<Map<String, String>> mItemDatas = null;
	private SimpleAdapter mListViewAdapter = null;
	private Spinner mFactorySpinner;
	private Spinner mSSpinner;
	private Spinner mRoomSpinner;
	private Spinner mTypeSpinner;
	
	private List<String>mFactoryList=null;
	private List<String>mSList=null;
	private List<String>mRoomList=null;
	private List<String>mMeasureTypeList=null;
	private ArrayList<View> listViews=null;
	private ArrayAdapter<String> mFAdapter=null;
	private ArrayAdapter<String> mSAdapter=null;
	private ArrayAdapter<String> mRAdapter=null;
	private TabHost tabHost;
	private ViewPager viewPager;
	private ListView mNormalList ;
	private ListView mSpecList;
	final int  mNormalListIndex =0;
	final int  mSpecialListIndex =1;
	final int  mTempListIndex =2;
	private EditText mDeviceNameEditText;
	private EditText mDeviceSNEditText;
	private EditText mMeasureNameEditText;
	private String StrFactory="";
	private String StrDepartment="";
	private String StrWorkshop="";
	private String StrDataType="";
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
            //tabWidget.getChildAt(i).getLayoutParams().height = 60;  
            //tabWidget.getChildAt(i).getLayoutParams().width = 65;
            //修改显示字体大小
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
           }
        
        listViews = new ArrayList<View>();
        listViews.add(inflater.inflate(R.layout.route_normal_layout, null));
        listViews.add(inflater.inflate(R.layout.route_spec_layout, null));
        listViews.add(inflater.inflate(R.layout.route_temp_fragment_layout, null));
        
        Button routeTempMeasure = (Button) listViews.get(mTempListIndex).findViewById(R.id.route_temp_measure);
        routeTempMeasure.setOnClickListener(this);
        
        initSpinnerViewAndData();
        
        mNormalList = (ListView) listViews.get(mNormalListIndex).findViewById(R.id.route_normal_list);
       // this.registerForContextMenu(mNormalList);
        LineListAdapter mNormalListAdapter = new LineListAdapter(getActivity().getApplicationContext(),RouteFragment.this.getActivity(),LineType.NormalRoute);
        mNormalList.setAdapter(mNormalListAdapter);
        mNormalListAdapter.notifyDataSetChanged();
       

        mSpecList = (ListView) listViews.get(mSpecialListIndex).findViewById(R.id.route_spec_list);
        LineListAdapter mSpectListAdapter = new LineListAdapter(getActivity().getApplicationContext(),RouteFragment.this.getActivity(),LineType.SpecialRoute);
        mSpecList.setAdapter(mSpectListAdapter);
        mSpectListAdapter.notifyDataSetChanged();
        
    	mDeviceNameEditText = (EditText)listViews.get(mTempListIndex).findViewById(R.id.editText1);;
    	mDeviceSNEditText=(EditText)listViews.get(mTempListIndex).findViewById(R.id.editText2);;
    	mMeasureNameEditText=(EditText)listViews.get(mTempListIndex).findViewById(R.id.editText3);;
        
        RoutePageAdapter mPageAdapter = new RoutePageAdapter(listViews);
        viewPager.setAdapter(mPageAdapter);
        viewPager.setCurrentItem(mNormalListIndex);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){  
            @Override  
            public void onTabChanged(String tabId){  
                Log.i(TAG+"--tabId--=", tabId);  
                if(tabId.equals("tab1")){ 
                	viewPager.setCurrentItem(mNormalListIndex);app.setLineType(LineType.NormalRoute);
                	}
                if(tabId.equals("tab2")) {
                	viewPager.setCurrentItem(mSpecialListIndex);app.setLineType(LineType.SpecialRoute);                	
                	}
                if(tabId.equals("tab3")) {
                	viewPager.setCurrentItem(mTempListIndex);app.setLineType(LineType.TemporaryRoute);
                	}
            }  
        });
		return searchView;
		}
	

	void initSpinnerViewAndData() {
		LineDao dao = LineDao.getInstance(this.getActivity().getApplicationContext());
		mFactoryList = dao.getOrganizationList(OrganizationType.OrganizationCorporation);
		mFactorySpinner = (Spinner) listViews.get(mTempListIndex).findViewById(R.id.spinner1);
		mFAdapter = new SpinnerAdapter(this.getActivity(), mFactoryList);
		mFAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mFactorySpinner.setAdapter(mFAdapter);
		mFactorySpinner.setOnItemSelectedListener(this);
	//	mFactorySpinner.setOnTouchListener(this);
		if(mFactoryList.size()>0){	StrFactory = mFactoryList.get(0);}
		
		mSList = dao.getOrganizationList(OrganizationType.OrganizationGroup);
		mSSpinner = (Spinner) listViews.get(mTempListIndex).findViewById(R.id.spinner2);
		mSAdapter = new SpinnerAdapter(this.getActivity(), mSList);
		mSAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSSpinner.setAdapter(mSAdapter);
		mSSpinner.setOnItemSelectedListener(this);
		//mSSpinner.setOnTouchListener(this);
		if(mSList.size()>0){StrDepartment = mSList.get(0);}
		
		mRoomList = dao.getOrganizationList(OrganizationType.OrganizationWorkShop);
		mRoomSpinner = (Spinner) listViews.get(mTempListIndex).findViewById(R.id.spinner3);
		mRAdapter = new SpinnerAdapter(this.getActivity(), mRoomList);
		mRAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mRoomSpinner.setAdapter(mRAdapter);
		mRoomSpinner.setOnItemSelectedListener(this);
		//mRoomSpinner.setOnTouchListener(this);
		if(mRoomList.size()>0){		StrWorkshop = mRoomList.get(0);}
		
		mTypeSpinner = (Spinner) listViews.get(mTempListIndex).findViewById(R.id.spinner4);
		mTypeSpinner.setOnItemSelectedListener(this);
	//	mTypeSpinner.setOnTouchListener(this);
		String [] aList =this.getActivity().getResources().getStringArray(R.array.TempMeasureType);
		StrDataType = aList[0];
		
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
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.route_temp_measure:
			
//			if(mDeviceNameEditText.getText().length()==0
//					||mDeviceSNEditText.getText().length()==0
//							||mMeasureNameEditText.getText().length()==0){
//				CommonAlterDialog	mdialog = new CommonAlterDialog(RouteFragment.this.getActivity(),"提示","请输入完整的信息!",null,null);
//				mdialog.show();
//				return;
//				
//			}
			
			Intent intent = new Intent();
			intent.setClass(getActivity(), TempRouteActivity.class);
			intent.putExtra(CommonDef.TMPLineFactoryName, StrFactory);
			intent.putExtra(CommonDef.TMPLineDepartmentName, StrDepartment);
			intent.putExtra(CommonDef.TMPLineWorkshopName, StrWorkshop);
			intent.putExtra(CommonDef.TMPLineDeviceName, mDeviceNameEditText.getText().toString());
			intent.putExtra(CommonDef.TMPLineDeviceSN, mDeviceSNEditText.getText().toString());
			intent.putExtra(CommonDef.TMPLineMeasureName, mMeasureNameEditText.getText().toString());
			intent.putExtra(CommonDef.TMPLineMeasureDataType, StrDataType);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}

	
	
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.spinner1:
			StrFactory = mFactorySpinner.getSelectedItem().toString();
		//	Toast.makeText(RouteFragment.this.getActivity().getApplicationContext(), "Selected"+arg2, Toast.LENGTH_SHORT).show();
			break;
		case R.id.spinner2:
			StrDepartment=mSSpinner.getSelectedItem().toString();
		//	Toast.makeText(RouteFragment.this.getActivity().getApplicationContext(), "Selectedspinner2", Toast.LENGTH_SHORT).show();
			break;
		case R.id.spinner3:
			StrWorkshop=mRoomSpinner.getSelectedItem().toString();
		//	Toast.makeText(RouteFragment.this.getActivity().getApplicationContext(), "Selectedspinner3", Toast.LENGTH_SHORT).show();
			break;
		case R.id.spinner4:
			StrDataType=mTypeSpinner.getSelectedItem().toString();
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public boolean onTouch(View arg0, MotionEvent arg1) {
//		// TODO Auto-generated method stub
//		return false;
//	}

//	@Override
//	public boolean onTouch(View arg0, MotionEvent arg1) {
//		// TODO Auto-generated method stub
//		switch (arg0.getId()) {
//		case R.id.spinner1:
//			Toast.makeText(RouteFragment.this.getActivity().getApplicationContext(), "test2spinner1", Toast.LENGTH_SHORT).show();
//			break;
//		case R.id.spinner2:
//			Toast.makeText(RouteFragment.this.getActivity().getApplicationContext(), "test2spinner2", Toast.LENGTH_SHORT).show();
//			break;
//		case R.id.spinner3:
//			Toast.makeText(RouteFragment.this.getActivity().getApplicationContext(), "test2spinner3", Toast.LENGTH_SHORT).show();
//			break;
//		default:
//			break;
//		}
//		return false;
//	}
	
}
