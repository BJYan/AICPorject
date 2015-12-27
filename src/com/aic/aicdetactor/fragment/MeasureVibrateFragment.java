package com.aic.aicdetactor.fragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.Event.Event;
import com.aic.aicdetactor.abnormal.AbnormalConst;
import com.aic.aicdetactor.abnormal.AbnormalInfo;
import com.aic.aicdetactor.acharEngine.AverageTemperatureChart;
import com.aic.aicdetactor.acharEngine.ChartBuilder;
import com.aic.aicdetactor.acharEngine.IDemoChart;
import com.aic.aicdetactor.activity.ElectricParameteActivity;
import com.aic.aicdetactor.adapter.CommonViewPagerAdapter;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.data.AbnomalGradeIdConstant;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.dialog.FlippingLoadingDialog;
import com.aic.aicdetactor.fragment.SearchFragment.MyOnPageChangeListener;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 速度04、 位移05、 加速度03 公用的UI
 * @author AIC
 *
 */
public class MeasureVibrateFragment extends MeasureBaseFragment{
	protected static final int VIBRATE_COUNTDOWN = 0;
	private ListView mListView = null;
	private ImageView mImageView = null;
	private RadioButton mRadioButton = null;
	private TextView mResultTipStr = null;
	private OnVibateListener mCallback = null;
	private SimpleAdapter mListViewAdapter = null;
	private int mIndex = -1;
	private List<Map<String, Object>> mMapList;
	private TextView mXTextView  = null;
	private TextView mYTextView  = null;
	private TextView mZTextView  = null;
	private TextView mTimeTextView  = null;
	private TextView mColorTextView  = null;
	private TextView mDeviceNameTextView = null;
	private String TAG = "MeasureVibrateFragment";
	private ImageView mHistoryImageView = null;
	private RelativeLayout MXLinear = null;
	private RelativeLayout MZLinear = null;
	PartItemListAdapter AdapterList;
	TextView mTimeTV=null;
	TabHost tabHost;
	private float mCheckValue =0.0f;
	List<View> views;
	LayoutInflater mInflater;
	long mReceiveDataLenth=0;
	byte mDLCMD=0;
	//DataAnalysis dataAnalysis;
	//StringBuffer mStrReceiveData=new StringBuffer();
	//String mStrLastReceiveData="";
	
	////////////////////压力测试用/////////////////////////
	boolean bPressTest=false;
	int iTestSuccessTimes=0;
	int iTestTimes=500;
	////////////////////////////////////////
	int iFailedTimes =0;	
	private Timer mTimer=null;
	private Timer mCountdownTimer=null;
	private TimerTask mTimerTask=null;
	private TimerTask mCountdownTimerTask = null;
	FlippingLoadingDialog mCountdownDialog;
	Handler handler;
	Button mMeasurementButton;
	float FengValue =0;
	float FengFengValue = 0;
	float ValidValue = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"Vibrate_fragment :onCreate()");
		// TODO Auto-generated method stub
		mMapList = new ArrayList<Map<String, Object>>();
		views = new ArrayList<View>();
		mInflater = getActivity().getLayoutInflater();
		//初始化ListVew 数据项
		String [] arraryStr = new String[]{this.getString(R.string.electric_device_parameters),
				this.getString(R.string.electric_device_spectrum)};
			for (int i = 0; i < arraryStr.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();				
				map.put(CommonDef.check_item_info.NAME,arraryStr[i] );								
				map.put(CommonDef.check_item_info.DEADLINE, "2015-06-20 10:00");

				//已检查项的检查数值怎么保存？并显示出来
				//已巡检的项的个数统计，暂时由是否有巡检时间来算，如果有的话，即已巡检过了，否则为未巡检。
				mMapList.add(map);
			}
			
			super.onCreate(savedInstanceState);
			
			handler = new Handler(){
				int countdownSec = 11;
				public void handleMessage(Message msg) {
					
					switch (msg.what) {
					case VIBRATE_COUNTDOWN:
						int count = (Integer) msg.obj;
						if(countdownSec==5) getDataFromBLE();
						if(countdownSec==0) {
							closeCountdownTimer();
							if(mCountdownDialog.isShowing()) mCountdownDialog.dismiss();
							mMeasurementButton.setEnabled(true);
						} else {
							countdownSec=countdownSec-count;
							if(!mCountdownDialog.isShowing()) mCountdownDialog.show();
							mCountdownDialog.setText("等待蓝牙连接 "+countdownSec+"秒");
						}

						break;

					default:
						break;
					}
				};
			};
			
			//mCallback.OnClick(CommonDef.ENABLE_MEASUREMENT_BUTTON,0,0,0);
	}
 
	public  MeasureVibrateFragment(PartItemListAdapter AdapterList){
		this.AdapterList = AdapterList;
	}
	
	void startCountdownTimer(){		
	/*	if(mCountdownTimerTask==null){
			mCountdownTimerTask = new TimerTask(){
				
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				msg.obj=1;
				msg.what = COUNTDOWN;
				handler.sendMessage(msg);
			}
			};
		}
		if(mCountdownTimer==null){
			mCountdownTimer = new Timer();
			mCountdownTimer.schedule(mCountdownTimerTask, 0,1000);
			}
		*/	
			
	}
	
	void closeCountdownTimer(){
		/*
		if(mCountdownTimer!=null){
			mCountdownTimer.cancel();
			mCountdownTimer=null;
		}
		
		if(mCountdownTimerTask!=null){
			mCountdownTimerTask.cancel();
			mCountdownTimerTask=null;
		}
		mMeasurementButton.setEnabled(true);
		*/
	}
	
	void startTimer(){		
		if(mTimerTask==null){
		mTimerTask = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getDataFromBLE();
			}
			};
		}
		if(mTimer==null){
			mTimer = new Timer();
			mTimer.schedule(mTimerTask, 1000);
			}
		mMeasurementButton.setEnabled(false);
	}
	
	void closeTimer(){
		if(mTimer!=null){
			mTimer.cancel();
			mTimer=null;
		}
		
		if(mTimerTask!=null){
			mTimerTask.cancel();
			mTimerTask=null;
		}
		
		mCanSendCMD=false;
		mReceiveDataLenth=0;
		mMeasurementButton.setEnabled(true);
	}
	View view;
	CommonViewPagerAdapter DialogPagerAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"Vibrate_fragment:onCreateView()");
		
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		view = (View) inflater.inflate(R.layout.brivate_fragment_layout, container, false);
		final ViewPager viewPager = (ViewPager)view.findViewById(R.id.vibrate_vPager);
		tabHost = (TabHost)view.findViewById(R.id.vibrate_tabhost);  
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost  
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("有效值")  
                .setContent(  
                R.id.view1));
  
//        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("时域")  
//                .setContent(R.id.view2));
//        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("频域")  
//                .setContent(  
//                R.id.view1));
//        if(mPartItemData.Axle_Number>1){
//	        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("轴心")  
//	                .setContent(R.id.view2));
//        }
		views.add(mInflater.inflate(R.layout.brivate2, null));
//		views.add(mInflater.inflate(R.layout.chart_thr_charts1_layout, null));
//		views.add(mInflater.inflate(R.layout.chart_thr_charts2_layout, null));
//		if(mPartItemData.Axle_Number>1){
//		views.add(mInflater.inflate(R.layout.chart_one_charts_layout, null));
//		}
		DialogPagerAdapter = new CommonViewPagerAdapter(getActivity(),views);
		viewPager.setAdapter(DialogPagerAdapter);
		 viewPager.setCurrentItem(0);
	        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mListView = (ListView)views.get(0).findViewById(R.id.listView1);
		mListViewAdapter = new SimpleAdapter(this.getActivity().getApplicationContext(), mMapList,
				R.layout.checkunit, new String[] { 			
				CommonDef.check_item_info.NAME,//巡检项名称		
				CommonDef.check_item_info.DEADLINE, //巡检最近时间			
				}, new int[] {				
						R.id.pathname,				
						R.id.deadtime});
		mListView.setAdapter(mListViewAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if(arg2 == 0){
					Intent intent = new Intent();					
					 intent.setClass(MeasureVibrateFragment.this.getActivity(),ElectricParameteActivity.class);
					 startActivity(intent);
				}else{
					Intent intent = null;
					IDemoChart[] mCharts = new IDemoChart[] {
							 new AverageTemperatureChart()};
				     // intent = new Intent(this, TemperatureChart.class);
				      intent = mCharts[0].execute(MeasureVibrateFragment.this.getActivity(),"test");
				    startActivity(intent);
				}
				 
				
			}
		});
		tabHost.setOnTabChangedListener(new OnTabChangeListener(){  
            @Override  
            public void onTabChanged(String tabId){  
                Log.i(TAG+"--tabId--=", tabId);  
                if(tabId.equals("tab1")) viewPager.setCurrentItem(0);
                if(tabId.equals("tab2")) viewPager.setCurrentItem(1);
                if(tabId.equals("tab3")) viewPager.setCurrentItem(2);
                if(mPartItemData.Axle_Number>1){
                if(tabId.equals("tab4")) viewPager.setCurrentItem(3);
                }
            }  
        });
		mImageView = (ImageView)views.get(0).findViewById(R.id.imageView1);
		
		mRadioButton = (RadioButton)views.get(0).findViewById(R.id.colorRadio);
		mResultTipStr = (TextView)views.get(0).findViewById(R.id.textiew1);
		
		mXTextView = (TextView)views.get(0).findViewById(R.id.x_value);
		
		mDeviceNameTextView = (TextView)views.get(0).findViewById(R.id.check_name);
		mDeviceNameTextView.setText(getPartItemName());
		mYTextView = (TextView)views.get(0).findViewById(R.id.y_value);
		mZTextView = (TextView)views.get(0).findViewById(R.id.z_value);
		mTimeTextView = (TextView)views.get(0).findViewById(R.id.time_value);
		MXLinear = (RelativeLayout)views.get(0).findViewById(R.id.Xline);
		MZLinear = (RelativeLayout)views.get(0).findViewById(R.id.Zline);
		mTimeTV = (TextView)views.get(0).findViewById(R.id.time);
		if(mPartItemData.Axle_Number==0){
			MXLinear.setVisibility(View.GONE);			
		}
		if(mPartItemData.Axle_Number==1){
			MXLinear.setVisibility(View.GONE);
			MZLinear.setVisibility(View.GONE);
		}
		if(mPartItemData.Axle_Number==2){
			MZLinear.setVisibility(View.GONE);	
		}
		mColorTextView = (TextView)views.get(0).findViewById(R.id.colordiscrip);
		initDisplayData();
		AdapterList.getCurrentPartItem().setSartDate();
		mCountdownDialog = new FlippingLoadingDialog(getActivity(), "1");
		mCountdownDialog.setCancelable(false);
		mMeasurementButton = (Button)getActivity().findViewById(R.id.bottombutton3);
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(bPressTest){
		//	mCallback.OnClick(CommonDef.ENABLE_MEASUREMENT_BUTTON,0,0,0);		
		}else{
			startCountdownTimer();
			
		}
	}
	
	
	boolean isFirstEnterChart=true;
	private void InitChart() {
		// TODO Auto-generated method stub
		try{
		if(isFirstEnterChart){
		 tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("时域")  
	                .setContent(R.id.view2));
	        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("频域")  
	                .setContent(  
	                R.id.view1));
	        if(mPartItemData.Axle_Number>1){
		        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("轴心")  
		                .setContent(R.id.view2));
	        }
	        views.add(mInflater.inflate(R.layout.chart_thr_charts1_layout, null));
			views.add(mInflater.inflate(R.layout.chart_thr_charts2_layout, null));
			if(mPartItemData.Axle_Number>1){
			views.add(mInflater.inflate(R.layout.chart_one_charts_layout, null));
			}
			DialogPagerAdapter.notifyDataSetChanged();
			isFirstEnterChart = false;
		}
		ChartBuilder chartBuilder = new ChartBuilder(getActivity());
		LinearLayout timeChartViewY = (LinearLayout) views.get(1).findViewById(R.id.dialog_thr_chart_first_chart);
		LinearLayout timeChartViewX = (LinearLayout) views.get(1).findViewById(R.id.dialog_thr_chart_sec_chart);
		LinearLayout timeChartViewZ = (LinearLayout) views.get(1).findViewById(R.id.dialog_thr_chart_thr_chart);
		
		LinearLayout char1ZL = (LinearLayout) views.get(1).findViewById(R.id.zL);
		LinearLayout char1YL = (LinearLayout) views.get(1).findViewById(R.id.yL);
		LinearLayout char1XL = (LinearLayout) views.get(1).findViewById(R.id.xL);
		
		EditText XetMax = (EditText) views.get(1).findViewById(R.id.dialog_thr_chart_first_option_content1);
		EditText XetFengFeng = (EditText) views.get(1).findViewById(R.id.dialog_thr_chart_first_option_content2);
		EditText XetValid = (EditText) views.get(1).findViewById(R.id.dialog_thr_chart_first_option_content3);
		
		
		
		TextView y_FengValue = (TextView) views.get(0).findViewById(R.id.y_FengValue);
		TextView y_FengFengValue = (TextView) views.get(0).findViewById(R.id.y_FengFengValue);
		TextView y_ValidValue = (TextView) views.get(0).findViewById(R.id.y_ValidValue);
		
		
		LinearLayout axesChartViewX = (LinearLayout) views.get(2).findViewById(R.id.dialog_thr_chart2_first_chart);
		LinearLayout axesChartViewY = (LinearLayout) views.get(2).findViewById(R.id.dialog_thr_chart2_sec_chart);
		LinearLayout axesChartViewZ = (LinearLayout) views.get(2).findViewById(R.id.dialog_thr_chart2_thr_chart);
		
		LinearLayout axesXLL = (LinearLayout) views.get(2).findViewById(R.id.xLL);
		LinearLayout axesYLL = (LinearLayout) views.get(2).findViewById(R.id.yLL);
		LinearLayout axesZLL = (LinearLayout) views.get(2).findViewById(R.id.zLL);
		
		LinearLayout frequencyChartView =null;
		if(mPartItemData.Axle_Number>1){
		frequencyChartView = (LinearLayout) views.get(3).findViewById(R.id.onechart);
		}
		String TitileTipsStr="AIC 巡检仪";
		if(mAnalysis.isValidate()){
			
			y_FengValue.setText("峰值:"+FengValue);
			y_FengFengValue.setText("峰峰值:"+FengFengValue);
			y_ValidValue.setText("有效值:"+ValidValue);
			
			XetMax.setText(""+FengValue);
			XetFengFeng.setText(""+FengFengValue);		
			XetValid.setText(""+ValidValue);
			
		
			if(mAnalysis.getAxCount()==1){
				timeChartViewY.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, mWaveFloatData,FengValue,FengFengValue));
				char1XL.setVisibility(View.GONE);
				char1ZL.setVisibility(View.GONE);
				axesChartViewY.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, mWaveFloatData,FengValue,FengFengValue));
				axesXLL.setVisibility(View.GONE);
				axesZLL.setVisibility(View.GONE);
			}else if(mAnalysis.getAxCount()==3){			
			timeChartViewX.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, mWaveFloatData, FengValue,FengFengValue));
			timeChartViewZ.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, mWaveFloatData, FengValue,FengFengValue));		
			axesChartViewX.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, mWaveFloatData, FengValue,FengFengValue));
			axesChartViewZ.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, mWaveFloatData, FengValue,FengFengValue));
			frequencyChartView.addView(chartBuilder.getBlackLineChartView("test", mWaveFloatData,FengValue,FengFengValue));
			}
		}else{
			float[] data ={-1,12,3,-4,10,4,-9,6};
			float a =20;
			float fa =40;
			double c= 4.67;
			
			axesChartViewY.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, data, a,fa));
			timeChartViewY.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, data, a,fa));
			timeChartViewX.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, data, a,fa));
			timeChartViewZ.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, data, a,fa));		
			axesChartViewX.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, data, a,fa));
			axesChartViewZ.addView(chartBuilder.getBlackLineChartView(TitileTipsStr, data, a,fa));
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	  int recLen =0;
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			measureAndDisplayData();
			if(recLen++<=iTestTimes){
				mHandle.postDelayed(this, 2000); 
				getDataFromBLE();
				mCanSendCMD=false;
				mReceiveDataLenth=0;
				Log.d(TAG, "run() " +recLen);
				
		}else{
			mColorTextView.setText("测试完毕");
		}
		}
	}; 
	
	 @Override
	protected void initDisplayData(){
		 mYTextView.setText(getPartItemData());
		if(mPartItemData.T_Item_Abnormal_Grade_Id!=AbnomalGradeIdConstant.NORMAL){
			mYTextView.setTextColor(Color.RED);
		}else{
			mYTextView.setTextColor(Color.BLACK);
		}
	}
	
    void measureAndDisplayData(){    
    	double MAX = super.mPartItemData.Up_Limit;
    	double MID = super.mPartItemData.Middle_Limit;
    	double LOW = super.mPartItemData.Down_Limit;
    	switch(mPartItemData.Axle_Number){
    	case 1:
    		break;
    	case 2:
    		break;
    	}
    	mYTextView.setText(String.valueOf(mCheckValue)+ " "+mPartItemData.Unit  +","+receiveCount);
    	try{
    	if((mCheckValue < MAX) && (mCheckValue>=MID) ){
    		mRadioButton.setBackgroundColor(Color.YELLOW);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.warning));
    		
    	}else if((mCheckValue >= LOW) && (mCheckValue<MID)){
    		mRadioButton.setBackgroundColor(Color.BLACK);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.normal));
    	}else if(mCheckValue <LOW){
    		mRadioButton.setBackgroundColor(Color.GRAY);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.invalid));
    	}else if(mCheckValue>=MAX){
    		mRadioButton.setBackgroundColor(Color.RED);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.dangerous));
    	}}catch(Exception e){
    		e.printStackTrace();
    	}
    }

	
    
    
    public interface OnVibateListener{
    	
    	void OnClick(int genPartItemDataCounts,int xValue,int yValue,int zValue);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnVibateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVibateListener");
        }
    }
    
    void saveData(PartItemListAdapter adapter){
    	double MAX = super.mPartItemData.Up_Limit;
    	double MID = super.mPartItemData.Middle_Limit;
    	double LOW = super.mPartItemData.Down_Limit;
    	int AbnormalId=0;
    	
    	isRecultNormal = false;
    	if((mCheckValue < MAX) && (mCheckValue>=MID) ){
    		AbnormalId=AbnormalConst.JingGao_Id;
    		Abnormalcode=AbnormalConst.JingGao_Code;
    		
    	}else if((mCheckValue >= LOW) && (mCheckValue<MID)){
    		AbnormalId=AbnormalConst.ZhengChang_Id;
    		Abnormalcode=AbnormalConst.ZhengChang_Code;
    		isRecultNormal = true;
    	}else if(mCheckValue <LOW){
    		AbnormalId=AbnormalConst.WuXiao_Id;
    		Abnormalcode=AbnormalConst.WuXiao_Code;
    	}else if(mCheckValue>=MAX){
    		AbnormalId=AbnormalConst.WeiXian_Id;
    		Abnormalcode=AbnormalConst.WeiXian_Code;
    	}    	
		if(!isRecultNormal){
			ParamsPartItemFragment params = new ParamsPartItemFragment();
			params.TypeCode=11;
			params.IsNormal=false;
			params.RecordLab=SystemUtil.createGUID();
			params.SaveLab=SystemUtil.createGUID();
			params.ValidValue=mCheckValue;
			params.AbnormalCode=Abnormalcode;
			params.object=mAnalysis.getWaveByteData();
			adapter.addNewMediaPartItem(params);
		}
    	adapter.saveData(String.valueOf(mCheckValue),Abnormalcode,AbnormalId,CaiYangShu,CAiYangPinLv);
    }

    int CaiYangShu=0;
    int CAiYangPinLv=0;
	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value,int measureOrSave,int CaiYangDian,int CaiyangPinLv) {
		// TODO Auto-generated method stub
		switch(measureOrSave){
		case PartItemContact.SAVE_DATA:
			saveData(adapter);
			break;
		case PartItemContact.MEASURE_DATA:
			CaiYangShu=CaiYangDian;
			CAiYangPinLv= CaiyangPinLv;
			if(bPressTest){
			mHandle.postDelayed(runnable, 1000);
			}else{
				closeTimer();
				startTimer();
			}
			break;
		}		
	}

	void getDataFromBLE(){
		BLEControl.setParamates(mHandle);
		mDLCMD=(byte) 0xd1;
		byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) mDLCMD, (byte)1, (byte)1,CaiYangShu,CAiYangPinLv);
		super.BLEControl.Communication2Bluetooth(BLEControl.getSupportedGattServices(),cmd);
	}
	
	byte []mDataBufferByte=null;
	int receiveCount =0;
	float[] mWaveFloatData;
	public Handler mHandle = new Handler(){

		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case BluetoothLeControl.MessageReadDataFromBT:
				byte[]strbyte=(byte[]) (msg.obj);
				if(!bStartReceiveData){
					receiveCount=0;
					iTestSuccessTimes=0;
					mAnalysis.reset();					
				}
				receiveCount++;
				mAnalysis.getDataFromBLE(strbyte,bStartReceiveData);
				if(!bStartReceiveData){
					bStartReceiveData=true;
				}
				if(mAnalysis.getPackagesCounts() ==receiveCount){
					mHandle.sendMessage(mHandle.obtainMessage(BluetoothLeControl.Message_End_Upload_Data_From_BLE));
				}
				
				Log.d(TAG, "receive count="+receiveCount +", received data is  "+SystemUtil.bytesToHexString(strbyte));
				mYTextView.setText(""+receiveCount);
				break;
			case BluetoothLeControl.Message_Stop_Scanner:
				mTimeTV.setVisibility(View.VISIBLE);
				mTimeTV.setText("正在测量中");
				break;
			case BluetoothLeControl.Message_End_Upload_Data_From_BLE:
				mTimeTV.setText("测量完毕");
				mMeasurementButton.setEnabled(true);
				if(!bStartReceiveData){
					return;
				}
				bStartReceiveData = false;
				
				if(mAnalysis.getPackagesCounts() !=receiveCount){
					mColorTextView.setText("数据包丢失"+" " +iFailedTimes);
				}
				
				if(mAnalysis.isValidate()){
					iTestSuccessTimes++;
					mWaveFloatData =mAnalysis.getWaveFloatData();
					
					 FengValue =mAnalysis.getFabsMaxValue();
					 FengFengValue = mAnalysis.getFengFengValue();
					mCheckValue= ValidValue = mAnalysis.getValidValue();
					
					measureAndDisplayData();
					String Wavedata=mAnalysis.getWaveByteData().toString();
					LineDao dao = LineDao.getInstance(getActivity());
					dao.InsertMediaData(Wavedata,true,AdapterList.getCurDeviceExitDataGuid(),app.mLineJsonData.T_Line.T_Line_Guid);
					InitChart();
					
					closeTimer();
						
				}else{
					if(!bPressTest){
						iFailedTimes++;	
						if(iFailedTimes<=MAX_FAILED_TIMES){
							closeTimer();
							startTimer();
							mColorTextView.setText("校验失败"+" " +iFailedTimes);
						}else{
							iFailedTimes=0;
						}
					}
				}
				
				if(bPressTest){
					mColorTextView.setText("压力测试成功结果:"+" " +iTestSuccessTimes/iTestTimes);
				}
				break;	
			}
			super.handleMessage(msg);
		}
	};
	
	
//	void UpLoadWaveData(String RecordLab){
//	float[]waveData=	mAnalysis.getWaveFloatData();
//	if(waveData==null){
//		return;
//	}
//		 byte[] bytedata=new byte[waveData.length*4];
//		 for(int i=0;i<waveData.length;i++){
//			 byte[] data=SystemUtil.float2byte(waveData[i]);
//			 for(int k=0;k<data.length;k++){
//				 bytedata[i*4+k]=data[k];
//			 }
//		 }
//		 
//		 
//		Event.UploadWaveDataRequestInfo_Event(null,null,bytedata,RecordLab);
//	}
//	
	
	
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
	public void addNewMediaPartItem(ParamsPartItemFragment params,PartItemListAdapter object) {
		// TODO Auto-generated method stub
		//保存音频 ，图片信息,波形数据在saveData中保存
		object.addNewMediaPartItem(params);
	}
	
}


	