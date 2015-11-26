package com.aic.aicdetactor.fragment;

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
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	}
	
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
		
		Button mSendButton = (Button)BlueToothView.findViewById(R.id.send);
		mSendButton.setOnClickListener(this);
//		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//		getActivity().registerReceiver(mReceiver, filter);
//		mBluetoothAdapter.startDiscovery();
		
		mBondedDevices = getBondedDevices();
		btBindDevListAdapter = new BlueToothBindDevListAdapter(getActivity(), mBondedDevices,mHandle);
		DevBindedlist.setAdapter(btBindDevListAdapter);
		
		mAllBTDevices.clear();
		blueToothDevListAdapter.notifyDataSetChanged();
		app = (myApplication)this.getActivity().getApplication();
		
		
		
		return BlueToothView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//getActivity().unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		mAllBTDevices.clear();
		
		scanLeDevice(true);
		
		mBondedDevices = getBondedDevices();
		btBindDevListAdapter.notifyDataSetChanged();
		//btBindDevListAdapter.notifyDataSetInvalidated();
		
		// Register the BroadcastReceiver
		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		scanLeDevice(false);
		
	}
	private boolean mScanning=false;
	private static final long SCAN_PERIOD = 10000;
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
			byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) 0xd1, (byte)1, (byte)1);
			btBindDevListAdapter.sendCommmand2BLE(cmd);
			break;
		default:
			break;
		}
	}
	
	private void showProgressBar(){
		pbar_text.setVisibility(View.VISIBLE);
		pbar.setVisibility(View.VISIBLE);
	}
	
	private void dismissProgressBar(){
		pbar_text.setVisibility(View.INVISIBLE);
		pbar.setVisibility(View.INVISIBLE);		
	}
	StringBuffer mStrReceiveData=new StringBuffer();;
	String mStrLastReceiveData="";
	//wave data
	float []mReceiveFloatData=null;
	byte []mReceiveByteDataCRC=null;
	boolean bStartReceiveData=false;
	byte mCurReceivedCMD=0;
	int mReceivedDataCounts=0;
	int caiyangdian=0;
	Handler mHandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case BluetoothLeControl.MessageReadDataFromBT:
				byte[]strbyte=(byte[]) (msg.obj);
				getDataFromBLE(strbyte,bStartReceiveData);
				if(!bStartReceiveData){
					bStartReceiveData=!bStartReceiveData;
				}

				break;
			case BluetoothLeControl.Message_Stop_Scanner:
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
				dismissProgressBar();
				break;
			case BluetoothLeControl.Message_End_Upload_Data_From_BLE:
				mStrLastReceiveData = mStrReceiveData.toString();
				mStrReceiveData.delete(0, mStrReceiveData.length());
				DataAnalysis proxy = new DataAnalysis();
				
				int k = proxy.isValidate(mStrLastReceiveData,(byte)msg.arg1);
				if(k==0){
					k=proxy.getAXCount();
					proxy.getResult();
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
				
			case BluetoothLeControl.Message_Connected_BLE_Address:
				app.mCurLinkedBLEAddress=(String) msg.obj;
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
    
 void    getDataFromBLE(byte []strbyte,boolean bStartReceiveData){
    	if(!bStartReceiveData){
			bStartReceiveData = true;
			mCurReceivedCMD=strbyte[2];
			switch(mCurReceivedCMD)
			{
			case (byte)0xd2:
				CRC32 crc = new CRC32();
				crc.update(strbyte, 0, 16);
				String s =Long.toHexString(crc.getValue());
				byte[]crcValue = new byte[4];
				for(int i=0;i<4;i++){
					crcValue[i]=strbyte[strbyte.length-4+i];
				}
				String sa=SystemUtil.bytesToHexString(crcValue);
				if(sa.equals(s)){
					Toast.makeText(BlueTooth_Fragment.this.getActivity(), "收到信息 正确", Toast.LENGTH_SHORT).show();	
				}
				mReceiveByteDataCRC = new byte[20];
				break;
			case (byte)0x7d:
				 caiyangdian=strbyte[7]<<8|strbyte[8];
			mReceiveFloatData = new float[caiyangdian];
			for(int k=0;k<5;k=k+2){
				mReceiveFloatData[k]=strbyte[k+11]<<8|strbyte[k+12];
			}
			mReceiveByteDataCRC = new byte[caiyangdian +10+9+9+3*4];
			
				break;
			}
			
		}else{
			switch(mCurReceivedCMD)
			{
			case (byte)0xd2:
				
				break;
			case (byte)0x7d:
				int max=20;
				if(20*mReceivedDataCounts>10+caiyangdian){
					max = 0;
				}
				//收集波形数据
				for(int k=0;k<max;k=k+2){
					mReceiveFloatData[mReceivedDataCounts*20+k-15]=strbyte[k]<<8|strbyte[k+1];
				}
				break;
			}
		}
		
		//收集BLE发送过来的所有原始数据
		System.arraycopy(strbyte,0,mReceiveByteDataCRC,mReceivedDataCounts*20,strbyte.length);	
		Toast.makeText(BlueTooth_Fragment.this.getActivity(), "收到信息", Toast.LENGTH_SHORT).show();
		mReceivedDataCounts++;
    }
    
}
