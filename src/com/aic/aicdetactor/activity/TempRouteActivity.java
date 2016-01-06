package com.aic.aicdetactor.activity;

import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.acharEngine.ChartBuilder;
import com.aic.aicdetactor.adapter.DeviceListAdapter;
import com.aic.aicdetactor.adapter.CommonViewPagerAdapter;
import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.comm.Bluetooth;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.CommonDialogBtnListener;
import com.aic.aicdetactor.dialog.OneCtrlDialog;
import com.aic.aicdetactor.dialog.OneCtrlDialog.OneCtrlDialogBtnListener;
import com.aic.aicdetactor.fragment.BlueTooth_Fragment;
import com.aic.aicdetactor.fragment.MeasureBaseFragment;
import com.aic.aicdetactor.service.BLEService;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class TempRouteActivity extends CommonActivity implements OnClickListener,OneCtrlDialogBtnListener{
	TextView TempRouteSave,TempRouteChart;
	LayoutInflater mInflater;
	ViewPager dialogContent;
	TabHost tabHost;
	ImageView accelerationChart,tempChart,displacemenChart,temperatureChart;
	ChartBuilder chartBuilder;
	private LinearLayout mTempLinearLayout;
	private LinearLayout mZhuanSuLinearLayout;
	private LinearLayout mVibrateLinearLayout;
	private TextView mTextViewTempValue;
	private TextView mTextViewZhuanSuValue;
	private TempType mType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_temp_layout);
		Intent intent = this.getIntent();
		
		String StrFactory = intent.getStringExtra(CommonDef.TMPLineFactoryName);
		String StrDepartment = intent.getStringExtra(CommonDef.TMPLineDepartmentName);
		String StrWorkshop = intent.getStringExtra(CommonDef.TMPLineWorkshopName);
		String mDeviceNameEditText = intent.getStringExtra(CommonDef.TMPLineDeviceName);
		String mDeviceSNEditText = intent.getStringExtra(CommonDef.TMPLineDeviceSN);
		String mMeasureNameEditText = intent.getStringExtra(CommonDef.TMPLineMeasureName);
		String StrDataType = intent.getStringExtra(CommonDef.TMPLineMeasureDataType);
		
		setActionBar("临时测量",true);
		mInflater = getLayoutInflater();
		
		mTextViewTempValue = (TextView) findViewById(R.id.tempvalue);
		mTextViewZhuanSuValue= (TextView) findViewById(R.id.tmp_zhuansuValue); 
		TempRouteSave = (TextView) findViewById(R.id.route_temp_measure);
		TempRouteSave.setOnClickListener(this);
		TempRouteChart = (TextView) findViewById(R.id.route_temp_chart);
		TempRouteChart.setOnClickListener(this);
		accelerationChart = (ImageView) findViewById(R.id.acceleration_chart);
		accelerationChart.setOnClickListener(this);
		tempChart = (ImageView) findViewById(R.id.temp_chart);
		tempChart.setOnClickListener(this);
		displacemenChart = (ImageView) findViewById(R.id.displacemen_chart);
		displacemenChart.setOnClickListener(this);
		temperatureChart = (ImageView) findViewById(R.id.temperature_chart);
		temperatureChart.setOnClickListener(this);
		
		chartBuilder = new ChartBuilder(this);
		
		mTempLinearLayout = (LinearLayout)findViewById(R.id.temp);
		mZhuanSuLinearLayout = (LinearLayout)findViewById(R.id.zhuansu);
		mVibrateLinearLayout = (LinearLayout)findViewById(R.id.zhendong);
		
		if(StrDataType.equals(this.getString(R.string.tmp_measure_temp_type_name))){
			mType=TempType.Temperatures;
			mTempLinearLayout.setVisibility(View.VISIBLE);
			TempRouteChart.setVisibility(View.GONE);
			//TempRouteSave.setWidth();
		}else if(StrDataType.equals(this.getString(R.string.tmp_measure_zhuansu_type_name))){
			mType=TempType.Speed;
			mZhuanSuLinearLayout.setVisibility(View.VISIBLE);
			TempRouteChart.setVisibility(View.GONE);
		}else if(StrDataType.equals(this.getString(R.string.tmp_measure_vibrate_type_name))){
			mVibrateLinearLayout.setVisibility(View.VISIBLE);
			mType=TempType.Vibrate;
		}
		final ViewPager viewPager = (ViewPager)findViewById(R.id.vibrate_vPager);
		tabHost = (TabHost)findViewById(R.id.vibrate_tabhost);  
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost  
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("有效值")  
                .setContent(  
                R.id.view1));
      tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("时域")  
      .setContent(R.id.view2));
      tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("频域")  
      .setContent(  
      R.id.view1));
      IntentFilter filter_dynamic = new IntentFilter();  
      filter_dynamic.addAction(BLEService.GET_BLE_MEASUREMENT_RESULT_ACTION);  
      registerReceiver(dynamicReceiver, filter_dynamic); 

      Intent serviceIntent = new Intent(TempRouteActivity.this, BLEService.class);
      serviceIntent.setAction(BLEService.CMDSTART);
      startService(serviceIntent);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.route_temp_chart:
			tempChartShow();
			break;
		case R.id.acceleration_chart:
			acceleChartShow();
			break;
		case R.id.temp_chart:
			acceleChartShow();
			break;
		case R.id.displacemen_chart:
			acceleChartShow();
			break;
		case R.id.temperature_chart:
			acceleChartShow();
			break;
		case R.id.route_temp_measure:
			Intent serviceIntent = new Intent(TempRouteActivity.this, BLEService.class);
		      serviceIntent.setAction(BLEService.CMDSENDCOMMAND);
		      serviceIntent.putExtra("frameHead", 0x7d);
		      /**
		       * byte frameHead = intent.getByteExtra("frameHead", (byte) 0);
	            	byte commandLenth = intent.getByteExtra("commandLenth", (byte) 0);
	            	mDLCMD = intent.getByteExtra("readSensorParams", (byte) 0);
	            	byte AxCount = intent.getByteExtra("AxCount", (byte) 0);
	            	byte SensorType = intent.getByteExtra("SensorType", (byte) 0);	            	
	            	int caiyangdian = intent.getIntExtra("caiyangdian", (byte) 0);
	            	int caiyangpinlv = intent.getByteExtra("caiyangpinlv", (byte) 0);
		       */
		      
			switch(mType){
			case Temperatures:
				serviceIntent.putExtra("commandLenth", (byte)0x14);
				serviceIntent.putExtra("readSensorParams", (byte)0XD6);
				serviceIntent.putExtra("SensorType", (byte)BluetoothConstast.CMD_Type_GetTemper);
				
					//Toast.makeText(this, "请在应用蓝牙页面中连接蓝牙", Toast.LENGTH_LONG).show();
				break;
			case Speed:
				serviceIntent.putExtra("commandLenth", (byte)0x14);
				serviceIntent.putExtra("readSensorParams", (byte)0XD3);
				serviceIntent.putExtra("SensorType", (byte)BluetoothConstast.CMD_Type_CaiJiZhuanSu);
				
//				if(mBLEService.isConnected()){
//					mBLEService.sendCommand((byte)0x7f, (byte)0x14,(byte) 0XD3, (byte)0, BluetoothConstast.CMD_Type_CaiJiZhuanSu,0,0);
//				}else{
//					Toast.makeText(this, "请在应用蓝牙页面中连接蓝牙", Toast.LENGTH_LONG).show();
//				}
				break;
			case Vibrate:
				
				serviceIntent.putExtra("commandLenth", (byte)0x14);
				serviceIntent.putExtra("readSensorParams", (byte)0XD1);
				serviceIntent.putExtra("SensorType", (byte)BluetoothConstast.CMD_Type_CaiJi);
				serviceIntent.putExtra("caiyangdian", 1024);
				serviceIntent.putExtra("caiyangpinlv", 1024);
				
//				if(mBLEService.isConnected()){
//					mBLEService.sendCommand((byte)0x7f, (byte)0x14,(byte) 0XD1, (byte)0, BluetoothConstast.CMD_Type_CaiJi,0,0);
//				}else{
//					Toast.makeText(this, "请在应用蓝牙页面中连接蓝牙", Toast.LENGTH_LONG).show();
//				}
				break;
			}
			
			startService(serviceIntent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(dynamicReceiver); 
	}

	private void acceleChartShow() {
		CommonDialog acceleChart = new CommonDialog(this);
		acceleChart.setTitle("Y-加速度趋势图");
		acceleChart.setCloseBtnVisibility(View.VISIBLE);
		acceleChart.setButtomBtn(new CommonDialogBtnListener() {
			
			@Override
			public void onClickBtn2Listener(CommonDialog dialog) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClickBtn1Listener(CommonDialog dialog) {
				// TODO Auto-generated method stub
				
			}
		}, "搜索", "");
		View acceleChartView = getLayoutInflater().inflate(R.layout.dialog_acceleration_trend, null);
		LinearLayout chartContainer = (LinearLayout) acceleChartView.findViewById(R.id.dialog_accele_trend_chart);
		//DataAnalysis dataAnalysis = new DataAnalysis();
		//float[] data = dataAnalysis.getData();
		//chartContainer.addView(chartBuilder.getBlackLineChartView("test", data, dataAnalysis.getFabsMaxValue(),dataAnalysis.getFengFengValue()));
		acceleChart.show();
	}

	private void tempChartShow() {
		OneCtrlDialog chartDialog = new OneCtrlDialog(this);
		chartDialog.setCloseBtnVisibility(View.VISIBLE);
		//chartDialog.setTitle("测试图谱");
		chartDialog.setButtomBtn(this, "确定", "取消");

		DataAnalysis dataAnalysis = new DataAnalysis();
		float[] data = dataAnalysis.getData();
		//float[] MinMaxTemp = new float[]{data[0],data[0]};

		dialogContent = (ViewPager) mInflater.inflate(R.layout.dialog_viewpager, null);
		List<View> views = new ArrayList<View>();
		views.add(mInflater.inflate(R.layout.dialog_content_thr_charts1_layout, null));
		views.add(mInflater.inflate(R.layout.dialog_content_thr_charts2_layout, null));
		views.add(mInflater.inflate(R.layout.dialog_content_one_charts_layout, null));
		views.add(mInflater.inflate(R.layout.motor_load_vibration_layout, null));
		//ViewPager dialogViewPager = (ViewPager) dialogContent.findViewById(R.id.dialog_viewpager);
		CommonViewPagerAdapter DialogPagerAdapter = new CommonViewPagerAdapter(this,views);
		dialogContent.setAdapter(DialogPagerAdapter);
		dialogContent.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				tabHost.setCurrentTab(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		String[] tab_first = new String[]{"时域","轴心","频域","有效值"};
		tabHost = chartDialog.TabViewInit(tab_first,new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
		        if(tabId.equals("tab1")) dialogContent.setCurrentItem(0);
		        if(tabId.equals("tab2")) dialogContent.setCurrentItem(1);
		        if(tabId.equals("tab3")) dialogContent.setCurrentItem(2);
		        if(tabId.equals("tab4")) dialogContent.setCurrentItem(3);
			}
		});
		chartDialog.setChartView(dialogContent, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, 680));
		chartDialog.show();
	}

	@Override
	public void onClickBtn1Listener(OneCtrlDialog dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickBtn2Listener(OneCtrlDialog dialog) {
		// TODO Auto-generated method stub
		
	}
	
	enum TempType{
		Temperatures,
		Speed,
		Vibrate
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
	
	private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {  
        
        @Override  
        public void onReceive(Context context, Intent intent) {  
            Log.e(TAG, "接收自定义动态注册广播消息");  
            if(intent.getAction().equals(BLEService.GET_BLE_MEASUREMENT_RESULT_ACTION)){  
            	boolean status = intent.getBooleanExtra(BLEService.MEASURESTATUS, false);
            	if(status){
            		byte type=intent.getByteExtra(BLEService.MEASURETYPE,(byte)0xd3 );	
            		float value =  intent.getFloatExtra(BLEService.MEASUREFENGFENG,0 );
            		switch(type){
            		case (byte)0xd1:
            			break;
            		case (byte)0xd3:
            			mTextViewZhuanSuValue.setText(String.valueOf(value));
            			break;
            		case (byte)0xd6:
            			mTextViewTempValue.setText(String.valueOf(value));
            			break;
            			
            		}
            		/**
            		 * intent.putExtra(MEASUREVALUE, mValiedValue);	
					 intent.putExtra(MEASUREMAXVALUE, mFabMaxValue);	
					
					 
            		 */
            		
            		
            	}
            }  
        }  
    };  
}
