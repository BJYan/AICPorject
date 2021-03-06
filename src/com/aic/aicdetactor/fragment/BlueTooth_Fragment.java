package com.aic.aicdetactor.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.BlueToothBindDevListActivity;
import com.aic.aicdetactor.adapter.BlueToothBindDevListAdapter;
import com.aic.aicdetactor.adapter.BlueToothDevListAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.bluetooth.analysis.ReceivedDataAnalysis;
import com.aic.aicdetactor.comm.Bluetooth;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BlueTooth_Fragment  extends Fragment implements OnClickListener{
	BluetoothAdapter mBluetoothAdapter;
	List<BluetoothDevice> mBondedDevices;
	List<BluetoothDevice> mAllBTDevices;
	BlueToothDevListAdapter blueToothDevListAdapter;
	BlueToothBindDevListAdapter btBindDevListAdapter;
	ProgressBar pbar;
	TextView pbar_text;
	ExpandableListView DevBindedlist;
	final String TAG= "Bluetooth_Fragment";
	myApplication app;
	Button mSendButton;
	
//	BroadcastReceiver mReceiver = new BroadcastReceiver() {
//
//	    public void onReceive(Context context, Intent intent) {
//	        String action = intent.getAction();
//	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//	            mAllBTDevices.add(device);
//	            blueToothDevListAdapter.notifyDataSetChanged();
//	        }
//	        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
//	        	dismissProgressBar();
//	        	Toast.makeText(getActivity(), "搜索完成！", Toast.LENGTH_SHORT).show();
//	        }
//	    }
//	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothAdapter.enable();
		mBondedDevices = new ArrayList<BluetoothDevice>();
		mAllBTDevices = new ArrayList<BluetoothDevice>();
		
		
       
        // 注册系统动态广播消息  
	}
	 private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {  
         
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            Log.e("MainActivity", "接收自定义动态注册广播消息");  
	            if(intent.getAction().equals(Bluetooth.BLEStatus)){  
	            	app.mBLEIsConnected = intent.getBooleanExtra(Bluetooth.IsConneted, false);
	            	mSendButton.setText(""+app.mBLEIsConnected);
	                Notification  baseNF = new Notification();   
	                NotificationManager nm = (NotificationManager) BlueTooth_Fragment.this.getActivity().getSystemService(BlueTooth_Fragment.this.getActivity().NOTIFICATION_SERVICE);   
	            	
	               if(app.mBLEIsConnected) {
	            	  
	                baseNF.icon =R.drawable.connection;  
	                baseNF.tickerText = "BLE蓝牙已连接"; 
	                Log.e("MainActivity", baseNF.tickerText.toString());  
	                
	               }else{
	            	   baseNF.icon =R.drawable.disconnection;  
		               baseNF.tickerText = "BLE蓝牙已断开";  
		               Log.e("MainActivity", baseNF.tickerText.toString());
	               }
	               
	               baseNF.defaults = Notification.DEFAULT_ALL;  
	               baseNF.setLatestEventInfo(BlueTooth_Fragment.this.getActivity(),"AIC", baseNF.tickerText, null);  
	               nm.notify(MeasureBaseFragment.Notification_ID_BASE, baseNF); 
	               Toast.makeText(context, baseNF.tickerText.toString(), Toast.LENGTH_SHORT).show();  
	            }  
	        }  
	    };  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View BlueToothView = inflater.inflate(R.layout.bluetooth_layout, container, false);
		
		DevBindedlist = (ExpandableListView) BlueToothView.findViewById(R.id.bluetooth_device_binded_list);

		DevBindedlist.setGroupIndicator(null);
		
		ListView Devlist = (ListView) BlueToothView.findViewById(R.id.bluetooth_device_list);
		blueToothDevListAdapter = new BlueToothDevListAdapter(getActivity(),mAllBTDevices);
		Devlist.setAdapter(blueToothDevListAdapter);
		
		TextView btSerachBtn = (TextView) BlueToothView.findViewById(R.id.bt_search_btn);
		btSerachBtn.setOnClickListener(this);
		pbar = (ProgressBar) BlueToothView.findViewById(R.id.bluetooth_pbar);
		pbar_text = (TextView) BlueToothView.findViewById(R.id.bluetooth_pbar_text);
		
		mSendButton = (Button)BlueToothView.findViewById(R.id.send);
		mSendButton.setOnClickListener(this);
		
		mAllBTDevices.clear();
		blueToothDevListAdapter.notifyDataSetChanged();
		app = (myApplication)this.getActivity().getApplication();
		
		
		
		return BlueToothView;
	}
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		mAllBTDevices.clear();
		mBondedDevices.clear();
		//scanLeDevice(true);
		
		// Register the BroadcastReceiver
		
		mBluetoothAdapter.startDiscovery();
		mBondedDevices = getBondedDevices();
		btBindDevListAdapter = new BlueToothBindDevListAdapter(getActivity(), mBondedDevices,mHandle);
		DevBindedlist.setAdapter(btBindDevListAdapter);
		
		showProgressBar();
		mSendButton.setText(""+app.mBLEIsConnected);
		registerBoradcastReceiver();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//scanLeDevice(false);
		
		unregisterBroadcastReceiver();
		
	}
	private boolean mScanning=false;
	private static final long SCAN_PERIOD = 100;
	private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
        	mHandle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                   // BlueTooth_Fragment.this.getActivity().invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            showProgressBar();
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            dismissProgressBar();
        }
      //  BlueTooth_Fragment.this.getActivity().invalidateOptionsMenu();
    }
	
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            if(device.getBondState()==BluetoothDevice.BOND_NONE){
	            	 if (!mAllBTDevices.contains(device)) {
	                    	mAllBTDevices.add(device);
	            		}
	            }
	            blueToothDevListAdapter.notifyDataSetChanged();
	        }
	        if(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)){
	        	
	        }
	        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
	        	dismissProgressBar();
	        	Toast.makeText(getActivity(), "搜索完成！", Toast.LENGTH_SHORT).show();
	        }
	    }
	};
	 public List<BluetoothDevice> getBondedDevices(){
		 List<BluetoothDevice> bondedDevs = new ArrayList<BluetoothDevice>();
		 Set<BluetoothDevice> devicesSet = new HashSet<BluetoothDevice>();
		 if(mBluetoothAdapter != null){
			 if(!mBluetoothAdapter.isEnabled()){
				 Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				 startActivity(intent);
			 }
			 devicesSet = mBluetoothAdapter.getBondedDevices();
			
			 if(devicesSet.size()>0) {
				 	for(Iterator<BluetoothDevice> iterator = devicesSet.iterator();iterator.hasNext();) {
				 		BluetoothDevice device = (BluetoothDevice)iterator.next(); 
				 		boolean bExist = mAllBTDevices.contains(device);
				 		Log.i(TAG, "BLE Address is "+device.getAddress() + " "+bExist);
				 		if(!bondedDevs.contains(device)){
				 		bondedDevs.add(device);
				 		}
				 	}     
			 	}
		 	}
		 return bondedDevs;
	 }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bt_search_btn:
		//	Intent intent = new Intent();
		//	intent.setClass(getActivity(), BlueToothBindDevListActivity.class);
		//	getActivity().startActivity(intent);
			scanLeDevice(true);
			break;
		case R.id.send:
			byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) 0xd1, (byte)1, (byte)1,1024,2560);
			btBindDevListAdapter.sendCommmand2BLE(cmd);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		btBindDevListAdapter.disConnectionBLE();
		super.onDestroy();
		
	}


	private void showProgressBar(){
		pbar_text.setVisibility(View.VISIBLE);
		pbar.setVisibility(View.VISIBLE);
	}
	
	private void dismissProgressBar(){
		pbar_text.setVisibility(View.INVISIBLE);
		pbar.setVisibility(View.INVISIBLE);		
	}
	//wave data
	boolean bStartReceiveData=false;

	ReceivedDataAnalysis mAnalysis =new ReceivedDataAnalysis();
	Handler mHandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
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
				break;
			case BluetoothLeControl.Message_Stop_Scanner:
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
				dismissProgressBar();
				break;
			case BluetoothLeControl.Message_End_Upload_Data_From_BLE:
				boolean isvalide = mAnalysis.isValidate();
				float valideValue = mAnalysis.getValidValue();
				float max=mAnalysis.getFabsMaxValue();
				float ff=mAnalysis.getFengFengValue();
				float fabs=mAnalysis.getFabsMaxValue();
				int k=0;
				k++;
				bStartReceiveData = false;
				break;
			case BluetoothLeControl.Message_Connection_Status:
				if(msg.arg2>0){
				app.mCurLinkedBLEAddress=(String) msg.obj;
				}
				app.mBLEIsConnected=msg.arg1>0?true:false;
				try{
				SharedPreferences preference = BlueTooth_Fragment.this.getActivity().getSharedPreferences(BluetoothConstast.BLEXML, Activity.MODE_PRIVATE); 
				Editor editor = preference.edit();
		        editor.putString(BluetoothConstast.BLEAddress, app.mCurLinkedBLEAddress);
		        editor.commit();
				}catch(Exception e){
					e.printStackTrace();
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	 // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        	BlueTooth_Fragment.this.getActivity(). runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mAllBTDevices.contains(device)) {
                    	mAllBTDevices.add(device);
            		}
                    
                    blueToothDevListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    
    
    
    private void registerBoradcastReceiver() {
//        IntentFilter stateChangeFilter = new IntentFilter(
//                BluetoothAdapter.ACTION_STATE_CHANGED);
//        IntentFilter connectedFilter = new IntentFilter(
//                BluetoothDevice.ACTION_ACL_CONNECTED);
//        IntentFilter disConnectedFilter = new IntentFilter(
//                BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        this.getActivity().registerReceiver(stateChangeReceiver, stateChangeFilter);
//        this.getActivity().registerReceiver(stateChangeReceiver, connectedFilter);
//        this.getActivity().registerReceiver(stateChangeReceiver, disConnectedFilter);
        
        
        IntentFilter filter_dynamic = new IntentFilter();  
        filter_dynamic.addAction(Bluetooth.BLEStatus);  
        this.getActivity().registerReceiver(dynamicReceiver, filter_dynamic);  
        
        
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter);
		
    }

    void unregisterBroadcastReceiver(){
    	// this.getActivity().unregisterReceiver(stateChangeReceiver);
    	 getActivity().unregisterReceiver(mReceiver);
    	 
    	 this.getActivity().unregisterReceiver(dynamicReceiver);  
    }
    private BroadcastReceiver stateChangeReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
        	mSendButton.setText(action);
        }
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {
        	mSendButton.setText(action);
        }
        if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
        	mSendButton.setText(action);
        }
    }
};

}