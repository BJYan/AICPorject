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
import com.aic.aicdetactor.adapter.CommonViewPagerAdapter;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.check.ElectricParameteActivity;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.data.AbnomalGradeIdConstant;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.dialog.FlippingLoadingDialog;
import com.aic.aicdetactor.fragment.SearchFragment.MyOnPageChangeListener;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 速度04、 位移05、 加速度03 公用的UI
 * @author AIC
 *
 */
public class MeasureVibrateFragment extends MeasureBaseFragment  implements OnButtonListener{

	protected static final int COUNTDOWN = 0;
	private ListView mListView = null;
	private ImageView mImageView = null;
	//private GridView mGridView = null;
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
	private String TAG = "luotest";
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
	DataAnalysis dataAnalysis;
	StringBuffer mStrReceiveData=new StringBuffer();
	String mStrLastReceiveData="";
	int iFailedTimes =0;
	final int MAX_FAILED_TIMES=3;
	private Timer mTimer=null;
	private Timer mCountdownTimer=null;
	private TimerTask mTimerTask=null;
	private TimerTask mCountdownTimerTask = null;
	FlippingLoadingDialog mCountdownDialog;
	Handler handler;
	Button mButton_Measurement;
	
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
				int i = 11;
				public void handleMessage(Message msg) {
					
					switch (msg.what) {
					case COUNTDOWN:
						int count = (Integer) msg.obj;
						if(i==5) getDataFromBLE();
						if(i==0) {
							closeCountdownTimer();
							if(mCountdownDialog.isShowing()) mCountdownDialog.dismiss();
							mButton_Measurement.setEnabled(true);
						} else {
							i=i-count;
							if(!mCountdownDialog.isShowing()) mCountdownDialog.show();
							mCountdownDialog.setText("等待蓝牙连接 "+i+"秒");
						}

						break;

					default:
						break;
					}
				};
			};
	}
 
	public  MeasureVibrateFragment(PartItemListAdapter AdapterList){
		this.AdapterList = AdapterList;
	}
	
	void startCountdownTimer(){		
		if(mCountdownTimerTask==null){
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
	}
	
	void closeCountdownTimer(){
		if(mCountdownTimer!=null){
			mCountdownTimer.cancel();
			mCountdownTimer=null;
		}
		
		if(mCountdownTimerTask!=null){
			mCountdownTimerTask.cancel();
			mCountdownTimerTask=null;
		}
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
		mCallback.OnClick(CommonDef.DISABLE_MEASUREMENT_BUTTON,0,0,0);
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
		if(mStrReceiveData!=null){
		mStrReceiveData.delete(0, mStrReceiveData.length());
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"Vibrate_fragment:onCreateView()");
		
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = (View) inflater.inflate(R.layout.brivate_fragment_layout, container, false);
		final ViewPager viewPager = (ViewPager)view.findViewById(R.id.vibrate_vPager);
		tabHost = (TabHost)view.findViewById(R.id.vibrate_tabhost);  
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
  
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("轴心")  
                .setContent(R.id.view2));
        
		views.add(mInflater.inflate(R.layout.brivate, null));
		views.add(mInflater.inflate(R.layout.chart_thr_charts1_layout, null));
		views.add(mInflater.inflate(R.layout.chart_thr_charts2_layout, null));
		views.add(mInflater.inflate(R.layout.chart_one_charts_layout, null));
		CommonViewPagerAdapter DialogPagerAdapter = new CommonViewPagerAdapter(getActivity(),views);
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
                Log.i("DownLoadFragment--tabId--=", tabId);  
                if(tabId.equals("tab1")) viewPager.setCurrentItem(0);
                if(tabId.equals("tab2")) viewPager.setCurrentItem(1);
                if(tabId.equals("tab3")) viewPager.setCurrentItem(2);
                if(tabId.equals("tab4")) viewPager.setCurrentItem(3);
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
		InitChart();
		mCountdownDialog = new FlippingLoadingDialog(getActivity(), "1");
		mCountdownDialog.setCancelable(false);
		mButton_Measurement = (Button)getActivity().findViewById(R.id.bottombutton3);
		mButton_Measurement.setEnabled(false);
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		startCountdownTimer();
	}
	
	private void InitChart() {
		// TODO Auto-generated method stub
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
		
		
		LinearLayout frequencyChartView = (LinearLayout) views.get(3).findViewById(R.id.onechart);
		
		if(dataAnalysis!=null){
			y_FengValue.setText("峰值:"+dataAnalysis.getFabsMaxValue());
			y_FengFengValue.setText("峰峰值:"+dataAnalysis.getFengFengValue());
			y_ValidValue.setText("有效值:"+dataAnalysis.getValidValue());
			
			XetMax.setText(""+dataAnalysis.getFabsMaxValue());
			XetFengFeng.setText(""+dataAnalysis.getFengFengValue());		
			XetValid.setText(""+dataAnalysis.getValidValue());
			
			float[] data = dataAnalysis.getData();
			if(dataAnalysis.getAXCount()==1){
				timeChartViewY.addView(chartBuilder.getBlackLineChartView("test", data,dataAnalysis.getFabsMaxValue(),dataAnalysis.getFengFengValue()));
				char1XL.setVisibility(View.GONE);
				char1ZL.setVisibility(View.GONE);
				axesChartViewY.addView(chartBuilder.getBlackLineChartView("test", data,dataAnalysis.getFabsMaxValue(),dataAnalysis.getFengFengValue()));
				axesXLL.setVisibility(View.GONE);
				axesZLL.setVisibility(View.GONE);
				frequencyChartView.setVisibility(View.GONE);		
			}else if(dataAnalysis.getAXCount()==3){			
			timeChartViewX.addView(chartBuilder.getBlackLineChartView("test", data, dataAnalysis.getFabsMaxValue(),dataAnalysis.getFengFengValue()));
			timeChartViewZ.addView(chartBuilder.getBlackLineChartView("test", data, dataAnalysis.getFabsMaxValue(),dataAnalysis.getFengFengValue()));		
			axesChartViewX.addView(chartBuilder.getBlackLineChartView("test", data, dataAnalysis.getFabsMaxValue(),dataAnalysis.getFengFengValue()));
			axesChartViewZ.addView(chartBuilder.getBlackLineChartView("test", data, dataAnalysis.getFabsMaxValue(),dataAnalysis.getFengFengValue()));
			frequencyChartView.addView(chartBuilder.getBlackLineChartView("test", data,dataAnalysis.getFabsMaxValue(),dataAnalysis.getFengFengValue()));
			}
		}else{
			float[] data = new float[1024];
			axesChartViewY.addView(chartBuilder.getBlackLineChartView("test", data, 5,10));
			timeChartViewY.addView(chartBuilder.getBlackLineChartView("test", data, 5,10));
			timeChartViewX.addView(chartBuilder.getBlackLineChartView("test", data, 5,10));
			timeChartViewZ.addView(chartBuilder.getBlackLineChartView("test", data, 5,10));		
			axesChartViewX.addView(chartBuilder.getBlackLineChartView("test", data, 5,10));
			axesChartViewZ.addView(chartBuilder.getBlackLineChartView("test", data, 5,10));
		}
		
	}

	  int recLen =0;
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			measureAndDisplayData();
			if(recLen++<=700){
				mHandle.postDelayed(this, 1100); 
				getDataFromBLE();
				mCanSendCMD=false;
				mReceiveDataLenth=0;
				mStrReceiveData.delete(0, mStrReceiveData.length());
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
    	
    	
//		mCheckValue = (int) (Math.random()*max_temperation);
    	
    	switch(mPartItemData.Axle_Number){
    	case 1:
    		break;
    	case 2:
    		break;
    	}
    	mYTextView.setText(String.valueOf(mCheckValue)+ " "+mPartItemData.Unit);
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
    	int isNormal=0;
    	int AbnormalId=0;
    	String Abnormalcode="-1";
    	
    	if((mCheckValue < MAX) && (mCheckValue>=MID) ){
    		isNormal=AbnormalConst.Abnormal;
    		AbnormalId=AbnormalConst.JingGao_Id;
    		Abnormalcode=AbnormalConst.JingGao_Code;
    		
    	}else if((mCheckValue >= LOW) && (mCheckValue<MID)){
    		isNormal=AbnormalConst.Normal;
    		AbnormalId=AbnormalConst.ZhengChang_Id;
    		Abnormalcode=AbnormalConst.ZhengChang_Code;
    	}else if(mCheckValue <LOW){
    		isNormal=AbnormalConst.Abnormal;
    		AbnormalId=AbnormalConst.WuXiao_Id;
    		Abnormalcode=AbnormalConst.WuXiao_Code;
    	}else if(mCheckValue>=MAX){
    		isNormal=AbnormalConst.Abnormal;
    		AbnormalId=AbnormalConst.WeiXian_Id;
    		Abnormalcode=AbnormalConst.WeiXian_Code;
    	}    	
		
    	adapter.saveData(String.valueOf(mCheckValue),isNormal,Abnormalcode,AbnormalId);
    }

	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value,int measureOrSave) {
		// TODO Auto-generated method stub
		switch(measureOrSave){
		case PartItemContact.SAVE_DATA:
			saveData(adapter);
			break;
		case PartItemContact.MEASURE_DATA:
	//		if(mCanSendCMD){
			closeTimer();
			startTimer();
			if(false){
			mHandle.postDelayed(runnable, 1000);
			}
//			else{
//			
//				getDataFromBLE();
//				
//			}
			
			break;
		}
		
	}

	void getDataFromBLE(){
		BLEControl.setParamates(mHandle);
		mDLCMD=(byte) 0xd1;
		byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) mDLCMD, (byte)1, (byte)1);
		super.BLEControl.Communication2Bluetooth(BLEControl.getSupportedGattServices(),cmd);
	}
	
	byte []mDataBufferByte=null;
	public Handler mHandle = new Handler(){

		int receiveCount =0;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case BluetoothLeControl.MessageReadDataFromBT:
				byte[]strbyte=(byte[]) (msg.obj);
				String str= SystemUtil.bytesToHexString(strbyte);
				int count=msg.getData().getInt("count");
				receiveCount++;
				if(mReceiveDataLenth==0){
					//String str= SystemUtil.bytesToHexString(strbyte);
					mReceiveDataLenth =DataAnalysis.getReceiveDataLenth(str, mDLCMD);
					mDataBufferByte = new byte[(int)mReceiveDataLenth];
					//System.arraycopy(strbyte,0,mDataBufferByte,0,strbyte.length);
					}else{
						//System.arraycopy(strbyte,0,mDataBufferByte,0,strbyte.length);
					}
				
				if(mReceiveDataLenth == mStrReceiveData.append(str.toString()).length()){
					mHandle.sendMessage(mHandle.obtainMessage(BluetoothLeControl.Message_End_Upload_Data_From_BLE));
				}else{						
					
					Log.d(TAG, "HandleMessage() count ="+count +" mStrReceiveData is " +mStrReceiveData.length()+","+mStrReceiveData.toString());
				}
				Log.d(TAG, "receive count="+count +", mReceiveDataLenth = "+mReceiveDataLenth +",mStrReceiveData lenth= " +mStrReceiveData.length()+","+mStrReceiveData.toString());
				break;
			case BluetoothLeControl.Message_Stop_Scanner:
				mTimeTV.setVisibility(View.VISIBLE);
				mTimeTV.setText("正在测量中");
				break;
			case BluetoothLeControl.Message_End_Upload_Data_From_BLE:
				mTimeTV.setText("测量完毕");
				if(mStrReceiveData.length()>0){
					mStrLastReceiveData = mStrReceiveData.toString();
					InsertMediaData(mStrLastReceiveData,true);					
					if(dataAnalysis==null){
						dataAnalysis = new DataAnalysis();
					}
					int isValide = dataAnalysis.isValidate(mStrLastReceiveData,mDLCMD);
					mStrReceiveData.delete(0, mStrReceiveData.length());
					Log.d(TAG, "receive data lenth = "+mStrLastReceiveData.length() +"and ValidValue ="+isValide);
					if(isValide!=0){
						iFailedTimes++;	
						if(iFailedTimes<=MAX_FAILED_TIMES){
							closeTimer();
							startTimer();
							mColorTextView.setText("数据丢失,请重测"+" " +iFailedTimes);
							Toast.makeText(getActivity(), mColorTextView.getText().toString(), Toast.LENGTH_LONG).show();
						}else{
							iFailedTimes=0;
						}
						
						return ;
					}else{
						iFailedTimes=0;
						dataAnalysis.getResult();
					dataAnalysis.getAXCount();					
					dataAnalysis.getCaiYangFrequency();
					dataAnalysis.getCRC32();
					dataAnalysis.getDataNum();
					dataAnalysis.getDataPointCount();
					dataAnalysis.getFabsMaxValue();
					dataAnalysis.getFengFengValue();
					
					mCheckValue=dataAnalysis.getValidValue();
					dataAnalysis.getWaveData();
					measureAndDisplayData();
					String Wavedata=dataAnalysis.getWaveData().toString();
					InsertMediaData(Wavedata,false);
					
					ifNeedAnalysisData(mDLCMD);
					closeTimer();
					}
				}else{
					closeTimer();
					Log.d(TAG,"mStrReceiveData is null");
				}
				
				break;
			case BluetoothLeControl.Message_Connection_Status:
				switch(msg.arg1){
				case 1://BLE has connected
					break;
				case 0://BLE has disconnected
					break;
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 只针对采集D1类型的数据进行图形分析
	 * @param type
	 */
void ifNeedAnalysisData(byte type){
	if(type != BluetoothConstast.CMD_Type_CaiJi){return ;}
	float[] data = dataAnalysis.getData();
	float[] MinMaxTemp = new float[]{data[0],data[0]};
	for(int i=0;i<data.length;i++){
		if(data[i]<=MinMaxTemp[0]) MinMaxTemp[0] = data[i];
		if(data[i]>MinMaxTemp[1]) MinMaxTemp[1] = data[i];
	}
	
	InitChart();
	
	UpLoadWaveData(data);
}
	void InsertMediaData(String data,boolean test){
		String guid = SystemUtil.createGUID();
		if(test){
			guid="2.txt";
		}
		try {
			SystemUtil.writeFile("/sdcard/aic/data/"+guid, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RouteDao dao = RouteDao.getInstance(getActivity());
		String StrSql="insert into "+DBHelper.TABLE_Media +" ( "
				+DBHelper.Media_Table.Data_Exist_Guid +","
				+DBHelper.Media_Table.Date +","
				+DBHelper.Media_Table.Is_Updated +","
				+DBHelper.Media_Table.Line_Guid +","
				+DBHelper.Media_Table.Mime_Type +","
				+DBHelper.Media_Table.Name +","
				+DBHelper.Media_Table.Path +","
				+DBHelper.Media_Table.UpdatedDate +") values ('"
				+AdapterList.getCurDeviceExitDataGuid()+"','"
				+SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM)+"','"
				+"0"+"','"
				+super.app.mLineJsonData.T_Line.T_Line_Guid+"','"
				+"image"+"','"
				+guid+"','"				
				+"/sdcard/aic/data/"+guid+"','"
				+"0"+"');";
				
		dao.execSQL(StrSql);
	}
	
	void UpLoadWaveData(float[] testData){
		 byte[] bytedata=new byte[testData.length*4];;
		 for(int i=0;i<testData.length;i++){
			 byte[] data=SystemUtil.float2byte(testData[i]);
			 for(int k=0;k<data.length;k++){
				 bytedata[i*4+k]=data[k];
			 }
		 }
		 
		 
		Event.UploadWaveDataRequestInfo_Event(null,null,null,null,bytedata);
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
	
}
