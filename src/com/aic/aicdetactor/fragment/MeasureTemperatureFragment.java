package com.aic.aicdetactor.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.abnormal.AbnormalConst;
import com.aic.aicdetactor.abnormal.AbnormalInfo;
import com.aic.aicdetactor.activity.PartItemActivity;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.comm.CommonDef.checkUnit_Type;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.FlippingLoadingDialog;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 温度00、录入01、抄表02 、转速06 用的UI 
 * @author AIC
 *
 */
public class MeasureTemperatureFragment  extends MeasureBaseFragment{

	protected static final int TEMP_COUNTDOWN = 1000;
	//UI
	//示意图片显示
	private ImageView mImageView = null;
	//温度测试结果
	private TextView mTemeratureResultStr = null;
	//温度测试结果对应的颜色，是否正常
	private RadioButton mRadioButton = null;
	//测量温度结果对应的文字描述
	private TextView mColorTextView = null;
	private TextView mDeviceNameTextView = null;
	private TextView mTextViewUnit;
	private TextView mTextViewName;
	private EditText mEditTextValue;
	private TextView mTimeTV;
	private Spinner mSpinner;
	private Timer mTimer=null;
	private TimerTask mTimerTask=null;
	
	//DATA
	//之间的通信接口
	private OnTemperatureMeasureListener mCallback = null;
	private List<Map<String, Object>> mMapList = null;
	final String TAG = "luotest";
	private PartItemListAdapter AdapterList;
	String mAbnormalStr="";
	private int SpinnerSelectedIndex=0;
	byte mDLCMD=0;
	
	private Timer mCountdownTimer=null;
	private TimerTask mCountdownTimerTask = null;
	FlippingLoadingDialog mCountdownDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	public  MeasureTemperatureFragment(PartItemListAdapter AdapterList){
		this.AdapterList = AdapterList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.temperature, container, false);
		mImageView = (ImageView)view.findViewById(R.id.imageView1);
		mDeviceNameTextView = (TextView)view.findViewById(R.id.check_name);
		mRadioButton = (RadioButton)view.findViewById(R.id.radioButton2);
		mColorTextView = (TextView)view.findViewById(R.id.resultTips);
		mTemeratureResultStr =(TextView)view.findViewById(R.id.temperature);
		mSpinner = (Spinner)view.findViewById(R.id.temperspinner);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mAbnormalStr= mSpinner.getSelectedItem().toString();
				SpinnerSelectedIndex= arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		mCountdownDialog = new FlippingLoadingDialog(this.getActivity(),"");
		mCountdownDialog.setCancelable(false);
		mTextViewName = (TextView)view.findViewById(R.id.textViewtmp);
		mTimeTV = (TextView)view.findViewById(R.id.hint);
		
		mEditTextValue = (EditText)view.findViewById(R.id.check_temp);
		mTextViewUnit = (TextView)view.findViewById(R.id.unit);
		mTextViewUnit.setText(getPartItemUnit());
		initDisplayData();
		MLog.Logd(TAG," Temperature_fragment:onCreateView() ");
		Log.d(TAG, "getName is "+mTextViewName.getText());
		AdapterList.getCurrentPartItem().setSartDate();
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startCountdownTimer();
	}
	
	void startCountdownTimer(){	
		if(mType==0 || mType==6){
		if(mCountdownTimerTask==null){
			mCountdownTimerTask = new TimerTask(){
				
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				msg.obj=1;
				msg.what = TEMP_COUNTDOWN;
				handler.sendMessage(msg);
			}
			};
		}
		if(mCountdownTimer==null){
			mCountdownTimer = new Timer();
			mCountdownTimer.schedule(mCountdownTimerTask, 0,1000);
			}
		}
	}
	
	void closeCountdownTimer(){
		if(mType==0 || mType==6){
		if(mCountdownTimer!=null){
			mCountdownTimer.cancel();
			mCountdownTimer=null;
		}
		
		if(mCountdownTimerTask!=null){
			mCountdownTimerTask.cancel();
			mCountdownTimerTask=null;
		}
		}
	}
    
	 @Override
	protected void initDisplayData(){
		mDeviceNameTextView.setText(getPartItemName());		
		mEditTextValue.setText(getPartItemData());
		switch(mType){
		case 0://温度
			mTextViewName.setText("温度:");
			mDLCMD=(byte) 0XD6;
			break;
		case 1://录入
			mTextViewName.setText("录入:");
			mTextViewUnit.setVisibility(View.INVISIBLE);
			mSpinner.setVisibility(View.VISIBLE);
			break;
		case 2://抄表
			mTextViewName.setText("抄表:");
			mTextViewUnit.setVisibility(View.INVISIBLE);
			mSpinner.setVisibility(View.VISIBLE);
			break;
		case 6://转速
			mTextViewName.setText("转速:");
			mDLCMD=(byte) 0XD3;
			break;
		}
	}
	 private float mCheckedValue=0.0f;
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onPause()");
		super.onPause();
		closeCountdownTimer();
	}
	
	void getDataFromBLE(){
		BLEControl.setParamates(handler);
		
		byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) mDLCMD, (byte)0, (byte)0,0,0);
		super.BLEControl.Communication2Bluetooth(BLEControl.getSupportedGattServices(),cmd);
	}
	/**
	 * 测量并显示测量后的数据
	 */
    void  measureAndDisplayData(){    
    	
    	double MAX = super.mPartItemData.Up_Limit;;
    	double MID = super.mPartItemData.Middle_Limit;
    	double LOW = super.mPartItemData.Down_Limit;
		
    //	mCheckedValue = (float)(Math.random()*max_mTemperatureeration);
    	
    	if((mCheckedValue < MAX) && (mCheckedValue>=MID) ){
    		mRadioButton.setBackgroundColor(Color.YELLOW);
    		mColorTextView.setText(getString(R.string.warning));
    	}else if((mCheckedValue >= LOW) && (mCheckedValue<MID)){
    		mRadioButton.setBackgroundColor(Color.BLACK);
    		mColorTextView.setText(getString(R.string.normal));
    		mEditTextValue.setTextColor(Color.BLACK);
    	}else if(mCheckedValue <LOW){
    		mRadioButton.setBackgroundColor(Color.GRAY);
    		mColorTextView.setText(getString(R.string.invalid));
    		mEditTextValue.setTextColor(Color.GRAY);
    	}else if(mCheckedValue>=MAX){
    		mRadioButton.setBackgroundColor(Color.RED);
    		mColorTextView.setText(getString(R.string.dangerous));
    		mEditTextValue.setTextColor(Color.RED);
    	}
    	mEditTextValue.setText(String.valueOf(mCheckedValue));
    }

    
    int iFailedTime =0;
    int receiveCount =0;
    Handler handler = new Handler(){
    	int countdownSec = 11;
		@Override
		public void dispatchMessage(Message msg) {
			int i = 0;
			// TODO Auto-generated method stub
			switch(msg.what){
		case BluetoothLeControl.MessageReadDataFromBT:
			byte[]strbyte=(byte[]) (msg.obj);
			
			if(!bStartReceiveData){
				mAnalysis.reset();					
			}
			mAnalysis.getDataFromBLE(strbyte,bStartReceiveData);
			if(!bStartReceiveData){
				bStartReceiveData=!bStartReceiveData;
			}
			if(mAnalysis.isReceivedAllData() ){
				handler.sendMessage(handler.obtainMessage(BluetoothLeControl.Message_End_Upload_Data_From_BLE));
			}
			
			Log.d(TAG, "receive count="+receiveCount +", received data is  "+SystemUtil.bytesToHexString(strbyte));
			
			
			
			
//			String str= SystemUtil.bytesToHexString(strbyte);
//			if(mReceiveDataLenth==0){
//			mReceiveDataLenth =DataAnalysis.getReceiveDataLenth(str, mDLCMD);
//			}
//			if(mReceiveDataLenth == mStrReceiveData.append(str.toString()).length()){
//				handler.sendMessage(handler.obtainMessage(BluetoothLeControl.Message_End_Upload_Data_From_BLE));
//			}else{
//				
//				int count=msg.getData().getInt("count");
//				Log.d(TAG, "HandleMessage() count ="+count +" mStrReceiveData is " +mStrReceiveData.length()+","+mStrReceiveData.toString());
//			}
			break;
		case BluetoothLeControl.Message_Stop_Scanner:
			mTimeTV.setVisibility(View.VISIBLE);
			mTimeTV.setText("正在测量中");
			break;
		case BluetoothLeControl.Message_End_Upload_Data_From_BLE:
			mTimeTV.setText("测量完毕");
			bStartReceiveData = false;
			if(mAnalysis.isReceivedAllData()){
				if(mAnalysis.isValidate()){
					 mCheckedValue=mAnalysis.getValidValue();
					 measureAndDisplayData();
						
				}else{
					iFailedTime++;	
					if(iFailedTime<=MAX_FAILED_TIMES){
						startCountdownTimer();
						mColorTextView.setText("数据丢失,请重测"+" " +iFailedTime);
					}else{
						iFailedTime=0;
					}
				}
			}else{
				iFailedTime++;	
				if(iFailedTime<=MAX_FAILED_TIMES){
					startCountdownTimer();
					mColorTextView.setText("数据丢失,请重测"+" " +iFailedTime);
				}else{
					iFailedTime=0;
				}
			}
			
//			mStrLastReceiveData = mStrReceiveData.toString();
//			mStrReceiveData.delete(0, mStrReceiveData.length());
//			DataAnalysis proxy = new DataAnalysis();
//			
//			int k = proxy.isValidate(mStrLastReceiveData,(byte)mDLCMD);
//			if(k==0){
//				iFailedTime=0;
//			proxy.getResult();
//			if(mDLCMD == BluetoothConstast.CMD_Type_GetTemper){
//			mCheckedValue=proxy.getTemperValue();
//			}else if(mDLCMD == BluetoothConstast.CMD_Type_CaiJiZhuanSu){
//				mCheckedValue=proxy.getTemperValue();
//			}
//			mCanSendCMD=true;
//			measureAndDisplayData();
//			}else{
//				String strErr = mTimeTV.getText().toString();
//				if(!strErr.contains("数据丢失")){
//					strErr=strErr+"数据丢失,请重测";
//					;
//				}else{
//					iFailedTime++;
//				}
//				mColorTextView.setText("数据丢失,请重测"+" " +iFailedTime);
//			}
			break;
		case BluetoothLeControl.Message_Connection_Status:
			switch(msg.arg1){
			case 1://BLE has connected
				break;
			case 0://BLE has disconnected
				break;
			}
			break;
			
		case TEMP_COUNTDOWN:
			int count = (Integer) msg.obj;
			if(countdownSec==5) getDataFromBLE();
			if(countdownSec==0) {
				mCountdownDialog.show();
				closeCountdownTimer();
				if(mCountdownDialog.isShowing()) mCountdownDialog.dismiss();
			} else {
				countdownSec=countdownSec-count;
				if(!mCountdownDialog.isShowing()) mCountdownDialog.show();
				mCountdownDialog.setText("等待蓝牙连接 "+countdownSec+"秒");
			}
			
			break;
			}
			super.dispatchMessage(msg);
		}
			
    	
    };
//	Runnable runnable = new Runnable() {
//		@Override
//		public void run() {
//			getDataFromBLE();
//		}
//	}; 
    
    public interface OnTemperatureMeasureListener{
    	
    //	void OnClick(String IndexButton);
    	void OnClick(int genPartItemDataCounts,int xValue,int yValue,int zValue);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MLog.Logd(TAG," Temperature_fragment:onAttach()");
        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnTemperatureMeasureListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTemperatureMeasureListener");
        }
    }

    void savePartItemData(PartItemListAdapter adapter){
    	  int abnormalId=0;
          String abnormalCode="";
          
    	if(adapter.getCurrentPartItemType()==checkUnit_Type.ENTERING
    			||adapter.getCurrentPartItemType()==checkUnit_Type.METER_READING){
    		String[]code =this.getResources().getStringArray(R.array.enter_abnormal_code);
    		abnormalCode= code[SpinnerSelectedIndex];
    		abnormalId= Integer.valueOf(abnormalCode)+1;
    		adapter.saveData(mEditTextValue.getText().toString(),abnormalCode,abnormalId,0,0);
    	}else if(adapter.getCurrentPartItemType()==checkUnit_Type.TEMPERATURE
    			|| adapter.getCurrentPartItemType()==checkUnit_Type.ROTATION_RATE){
    	final double  MAX = super.mPartItemData.Up_Limit;;
    	final double  MID = super.mPartItemData.Middle_Limit;
    	final double  LOW = super.mPartItemData.Down_Limit;
      
    	
    	if((mCheckedValue < MAX) && (mCheckedValue>=MID) ){
    		abnormalId=AbnormalConst.JingGao_Id;
    		abnormalCode=AbnormalConst.JingGao_Code;
    	}else if((mCheckedValue >= LOW) && (mCheckedValue<MID)){
    		abnormalId=AbnormalConst.ZhengChang_Id;
    		abnormalCode=AbnormalConst.ZhengChang_Code;
    	}else if(mCheckedValue <LOW){
    		abnormalId=AbnormalConst.WuXiao_Id;
    		abnormalCode=AbnormalConst.WuXiao_Code;
    	}else if(mCheckedValue>=MAX){
    		abnormalId=AbnormalConst.WeiXian_Id;
    		abnormalCode=AbnormalConst.WeiXian_Code;
    	}
    	
    	adapter.saveData(String.valueOf(mCheckedValue),abnormalCode,abnormalId,0,0);
    	}
    }
	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value,int measureOrSave,int CaiYangDian,int CaiyangPinLv) {
		// TODO Auto-generated method stub
		switch(measureOrSave){
		case PartItemContact.SAVE_DATA:
			if("请选择状态".equals(mAbnormalStr)
					&& (adapter.getCurrentPartItemType()==checkUnit_Type.ENTERING
	    			||adapter.getCurrentPartItemType()==checkUnit_Type.METER_READING)){
			//	CommonAlterDialog dialog = new CommonAlterDialog(this.getActivity().getApplicationContext(),"提示","请选择状态",null,null);
			//	dialog.show();
			}else{
			savePartItemData(adapter);
			}
			break;
		case PartItemContact.MEASURE_DATA:
				getDataFromBLE();
				mCallback.OnClick(CommonDef.DISABLE_MEASUREMENT_BUTTON,0,0,0);
			
			break;
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
		//mCallback.OnClick(CommonDef.DISABLE_MEASUREMENT_BUTTON,0,0,0);
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
	}
//	@Override
//	public void saveCheckValue() {
//		// TODO Auto-generated method stub
//		Log.d("atest", "温度2222   saveCheckValue()");
//		super.setPartItemData("温度2222");
//	}
	@Override
	public void addNewMediaPartItem(ParamsPartItemFragment params,PartItemListAdapter object) {
		// TODO Auto-generated method stub
		object.addNewMediaPartItem(params);
	}
}
