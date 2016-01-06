package com.aic.aicdetactor.service;

import java.util.Timer;
import java.util.TimerTask;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.ReceivedDataAnalysis;
import com.aic.aicdetactor.comm.Bluetooth;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.fragment.BlueTooth_Fragment;
import com.aic.aicdetactor.fragment.MeasureBaseFragment;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BLEService extends Service {
	private  BluetoothLeControl mBLEControl = null;
	private byte []mDataBufferByte=null;
	private int receiveCount =0;
	private float[] mWaveFloatData;
	private boolean mCanSendCMD=false;
	private boolean isRecultNormal=false;
	private boolean bStartReceiveData=false;
	private int iTestSuccessTimes=0;
	private int iTestTimes=500;
	private int iFailedTimes =0;	
	private Timer mTimer=null;
	private Timer mCountdownTimer=null;
	private TimerTask mTimerTask=null;
	private TimerTask mCountdownTimerTask = null;
	private ReceivedDataAnalysis mAnalysis =new ReceivedDataAnalysis();
	private final String TAG="BLEService";
	private long mReceiveDataLenth=0;
	private byte mDLCMD=0;
	private final int MAX_FAILED_TIMES=3;
	
	private boolean isConnected=false;
	private boolean isMeasurement=false;
	
	private myApplication app;
	
	//发现蓝牙设备 action
	public final static String FIND_BLUETOOTH_OBJECT_ACTION="com.aic.action.findBlueTooth";
	//连接蓝牙设备 action
	public final static String CONNECT_BLUETOOTH_OBJECT_ACTION="com.aic.action.connectBlueTooth";
	//已经连接到蓝牙设备 action
	public final static String CONNECTED_BLUETOOTH_OBJECT_ACTION="com.aic.action.connectedBlueTooth";
	//蓝牙设备已经断开 action
	public final static String DISCONNECTED_BLUETOOTH_OBJECT_ACTION="com.aic.action.disconnectedBlueTooth";
	
	//通过intent发送计算出来的数据Action
	public final static String GET_BLE_MEASUREMENT_RESULT_ACTION="com.aic.action.getBLEMeasurementResult";
	
	
	public static final String CMDSTART = "start";
	public static final String CMDDISGAVER = "disgaver";
	public static final String CMDCONNECT = "connect";
	public static final String CMDSENDCOMMAND = "sendCommand";
	public static final String CMDDISCONNECT = "disconnect";
	
	public static final String MEASURESTATUS="status";//true or false
	public static final String MEASUREVALUE="value";//有效数值，温度、转速等测量计算出来的数值
	public static final String MEASUREMAXVALUE="maxvalue";//波形峰值
	public static final String MEASUREFENGFENG="fengfengvalue";//波形峰峰值
	public static final String MEASURETYPE="TYPE";//测量类型，即下发的命令类型
	public static final String MEASUREFAILEINFO="failinfo";//失败原因
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		IntentFilter commandFilter = new IntentFilter();
        commandFilter.addAction(FIND_BLUETOOTH_OBJECT_ACTION);
        commandFilter.addAction(CONNECT_BLUETOOTH_OBJECT_ACTION);
        commandFilter.addAction(CONNECTED_BLUETOOTH_OBJECT_ACTION);
        commandFilter.addAction(DISCONNECTED_BLUETOOTH_OBJECT_ACTION);
        registerReceiver(mIntentReceiver, commandFilter);
        app = (myApplication) getApplication();
        mBLEControl = BluetoothLeControl.getInstance(app.getApplicationContext());
        mBLEControl.setParamates(mHandle);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mIntentReceiver);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//BLEControl.setParamates(handler);
		 if (intent != null) {
	            String action = intent.getAction();
	           // String cmd = intent.getStringExtra("command");
	           Log.d(TAG,"onStartCommand " + action + " / " + action);

	            if (CMDSTART.equals(action)) {
	            } else if (CMDDISGAVER.equals(action)) {
	            	
	            } else if (CMDCONNECT.equals(action)) {
	            	//String  BLEAddress = intent.getStringExtra("bleAddress");
	            	//BLEControl.Connection(BLEAddress);
	            	mBLEControl.Connection(app.mCurLinkedBLEAddress);
	            } else if (CMDSENDCOMMAND.equals(action)) {
	            	byte frameHead = intent.getByteExtra("frameHead", (byte) 0);
	            	byte commandLenth = intent.getByteExtra("commandLenth", (byte) 0);
	            	mDLCMD = intent.getByteExtra("readSensorParams", (byte) 0);
	            	byte AxCount = intent.getByteExtra("AxCount", (byte) 0);
	            	byte SensorType = intent.getByteExtra("SensorType", (byte) 0);	            	
	            	int caiyangdian = intent.getIntExtra("caiyangdian", (byte) 0);
	            	int caiyangpinlv = intent.getByteExtra("caiyangpinlv", (byte) 0);
	            	sendCommand(frameHead,commandLenth,mDLCMD,AxCount,SensorType,caiyangdian,caiyangpinlv);
	            } else if (CMDDISCONNECT.equals(action)) {
	            	mBLEControl.disconnection();
	            }
	        }
	        
		 return START_STICKY;
		//return super.onStartCommand(intent, flags, startId);
	}
	
	
	//向下位机发送命令
	private void sendCommand(byte frameHead,byte commandLenth,byte readSensorParams,byte AxCount,byte SensorType
			 ,int caiyangdian,int caiyangpinlv){
		byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) mDLCMD, (byte)0, (byte)0,0,0);
		mBLEControl.Communication2Bluetooth(mBLEControl.getSupportedGattServices(),cmd);
		
	}

	private float mValiedValue=0;
	private  float mFabMaxValue=0;
	private  float mFFValue=0;
	public float getValiedValue(){
		return mValiedValue;
	}
	public float getFFValue(){
		return mFFValue;
	}
	public float getFabMaxValue(){
		return mFabMaxValue;
	}
	
	public boolean isMeasurment()
	{
		return isMeasurement;
	}
	public boolean isConnected()
	{
		return true;
	//	return isConnected;
	}
	
	public float[] getWaveDataArray(){
		if(mAnalysis!=null){
		mWaveFloatData=mAnalysis.getWaveFloatData();
		}
		return mWaveFloatData;
	}
	public Handler mHandle = new Handler(){

		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case BluetoothLeControl.MessageReadDataFromBT:
				isMeasurement=true;
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
				break;
			case BluetoothLeControl.Message_Stop_Scanner:
				break;
			case BluetoothLeControl.Message_End_Upload_Data_From_BLE:
				if(!bStartReceiveData){
					return;
				}
				bStartReceiveData = false;
				Intent intent = new Intent();
				intent.setAction(GET_BLE_MEASUREMENT_RESULT_ACTION);
				String extrInfo="";
				if(mAnalysis.getPackagesCounts() !=receiveCount){
				intent.putExtra(MEASURESTATUS, false);	
				extrInfo="丢包";
				}
				
				if(mAnalysis.isValidate()){
					iTestSuccessTimes++;
					mWaveFloatData =mAnalysis.getWaveFloatData();
					
					  mValiedValue=mAnalysis.getValidValue();
					   mFabMaxValue=mAnalysis.getFabsMaxValue();
					   mFFValue=mAnalysis.getFengFengValue();
					 
					 intent.putExtra(MEASURESTATUS, true);	
					 intent.putExtra(MEASUREVALUE, mValiedValue);	
					 intent.putExtra(MEASUREMAXVALUE, mFabMaxValue);	
					 intent.putExtra(MEASUREFENGFENG,mFFValue );
					 intent.putExtra(MEASURETYPE,mDLCMD );	
					 
					 
					String Wavedata=mAnalysis.getWaveByteData().toString();
				}else{
						iFailedTimes++;	
						if(iFailedTimes<=MAX_FAILED_TIMES){
							closeTimer();
							startTimer();
						}else{
							iFailedTimes=0;
						}
					intent.putExtra(MEASUREFAILEINFO, "失败第"+iFailedTimes+"次,"+extrInfo);	
				}
				
				sendBroadcast(intent);
				isMeasurement=false;
				break;	
			}
			super.handleMessage(msg);
		}
	};
		
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
			//mMeasurementButton.setEnabled(false);
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
			//mMeasurementButton.setEnabled(true);
		}
		void getDataFromBLE(){
			//BLEControl.setParamates(mHandle);
			//mDLCMD=(byte) 0xd1;
			//byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) mDLCMD, (byte)1, (byte)1,CaiYangShu,CAiYangPinLv);
		//	BLEControl.Communication2Bluetooth(BLEControl.getSupportedGattServices(),cmd);
		}
		
		private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {  
	         
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            Log.e(TAG, "接收自定义动态注册广播消息");  
	            if(intent.getAction().equals(Bluetooth.BLEStatus)){  
	            	isConnected= intent.getBooleanExtra(Bluetooth.IsConneted, false);
	            }  
	        }  
	    };  
	    
		 private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		        @Override
		        public void onReceive(Context context, Intent intent) {
		            String action = intent.getAction();
		            String cmd = intent.getStringExtra("command");
//		            MusicUtils.debugLog("mIntentReceiver.onReceive " + action + " / " + cmd);
//		            if (CMDNEXT.equals(cmd) || NEXT_ACTION.equals(action)) {
//		                gotoNext(true);
//		            } else if (CMDPREVIOUS.equals(cmd) || PREVIOUS_ACTION.equals(action)) {
//		                prev();
//		            } else if (CMDTOGGLEPAUSE.equals(cmd) || TOGGLEPAUSE_ACTION.equals(action)) {
//		                if (isPlaying()) {
//		                    pause();
//		                    mPausedByTransientLossOfFocus = false;
//		                } else {
//		                    play();
//		                }
//		            } else if (CMDPAUSE.equals(cmd) || PAUSE_ACTION.equals(action)) {
//		                pause();
//		                mPausedByTransientLossOfFocus = false;
//		            } else if (CMDPLAY.equals(cmd)) {
//		                play();
//		            } else if (CMDSTOP.equals(cmd)) {
//		                pause();
//		                mPausedByTransientLossOfFocus = false;
//		                seek(0);
//		            } else if (MediaAppWidgetProvider.CMDAPPWIDGETUPDATE.equals(cmd)) {
//		                // Someone asked us to refresh a set of specific widgets, probably
//		                // because they were just added.
//		                int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//		                mAppWidgetProvider.performUpdate(MediaPlaybackService.this, appWidgetIds);
//		            }
		        }
		    };
		    
		    
//		    Thread thread = new Thread();
//		    HandlerThread thread2 = new HandlerThread("string");
//		    thread2.start();
}
