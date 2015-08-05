package com.aic.aicdetactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;










import com.aic.aicdetactor.comm.Bluetooth;
import com.aic.aicdetactor.service.BluetoothService;

import android.app.Activity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class btwifi_setting_activity  extends Activity  implements OnClickListener{
	private ListView mListView_bt =null;
	private String TAG = "luotest";
	private Switch mSwitch_bt = null;
	private Switch mSwitch_wifi = null;
	private BluetoothAdapter mBTAdapter = null;
	private TextView mBTStatusTextView = null;
	private BluetoothDevice mBlueDevice = null;;
	// Member object for the chat services  
    private BluetoothService mBtService = null; 
	// String buffer for outgoing messages  
    private StringBuffer mOutStringBuffer; 
	
    private Button mBt_button = null;
    private Button mWifi_button = null;
    private EditText mBt_content = null;
    private EditText mWifi_content = null;
	private String mBtName = "Name";
	private String mBtAddress = "Adress";
	private List<Map<String, Object>> mList_bt = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> mList_wifi = new ArrayList<Map<String, Object>>();
	private SimpleAdapter mAdapter_bt = null;
	private SimpleAdapter mAdapter_wifi = null;
	private Intent mBackIntent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.btwifi_setting_activity);

 		
		IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, mFilter);
		
		mListView_bt = (ListView)findViewById(R.id.bluetooth_list);
		
		mAdapter_bt = new SimpleAdapter(this, mList_bt,
				R.layout.bt_adapter_item, new String[] {
				mBtName,mBtAddress}, new int[] {R.id.bt_name,R.id.bt_adress});

		mListView_bt.setAdapter(mAdapter_bt);
		mListView_bt.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.d(TAG,"list size ="+mList_bt.size()  +",arg2 ="+arg2 +",arg3 = "+arg3);
				Map<String, Object> temp = mList_bt.get(arg2);
				String address = (String) temp.get(mBtAddress);
				 // Get the BLuetoothDevice object  
                BluetoothDevice device = mBTAdapter.getRemoteDevice(address);  
                // Attempt to connect to the device  
                if(mBackIntent == null){
                	mBackIntent = new Intent();
                }
                Log.d(TAG,"temp.toString() ="+temp.toString()); ;
                mBackIntent.putExtra("Name", (String)temp.get(mBtName));
                mBackIntent.putExtra("Status", (String)temp.get(mBtAddress));
               
                Message msg = new Message();
                msg.what = Bluetooth.MESSAGE_START_CONNECT;
                msg.obj = device;
                mHandler.sendMessageDelayed(msg, 1000);
				return true;
			}			
		});

		mBt_button = (Button)findViewById(R.id.bt_send);
		mBt_button.setOnClickListener(this);
		mWifi_button = (Button)findViewById(R.id.wifi_send);
		mWifi_button.setOnClickListener(this);
		mBt_content = (EditText)findViewById(R.id.bt_content);
		mWifi_content = (EditText)findViewById(R.id.wifi_content);
		
		mSwitch_bt = (Switch)findViewById(R.id.bt_switch);
		mSwitch_bt.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					//直接打开蓝牙
					if(!mBTAdapter.enable()){
					mBTAdapter.enable();
					}
					mHandler.sendEmptyMessageDelayed(Bluetooth.MESSAGE_START_DISCOVERY, 1000);
					
				}else{
					//关闭蓝牙					
					mBTAdapter.cancelDiscovery();
					mBTAdapter.disable();
					mList_bt.clear();
					mBTStatusTextView.setText(getString(R.string.unlink));
					mListView_bt.setVisibility(View.GONE);
				}
				
			}});
		mBTStatusTextView = (TextView)findViewById(R.id.link_statusView1);
		mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBTAdapter==null){
			Toast.makeText(getApplicationContext(), getString(R.string.no_bt_device), Toast.LENGTH_LONG).show();
			finish();
		}
		if(mBTAdapter.enable()){
			Log.d(TAG,"begin discovery BT");
			mBTAdapter.startDiscovery();
			mSwitch_bt.setChecked(true);
			mBTStatusTextView.setText(getString(R.string.link));
		}else{
			mSwitch_bt.setChecked(false);
			mList_bt.clear();
			mBTAdapter.cancelDiscovery();
			mBTStatusTextView.setText(getString(R.string.unlink));
		}
		
		  // Initialize the BluetoothService to perform bluetooth connections  
        mBtService = new BluetoothService(this, mHandler);  
  
        // Initialize the buffer for outgoing messages  
        mOutStringBuffer = new StringBuffer("");  
	}
	
	// The Handler that gets information back from the BluetoothService  
    private final Handler mHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case Bluetooth.MESSAGE_STATE_CHANGE:  
                //if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);  
                switch (msg.arg1) {  
                case BluetoothService.STATE_CONNECTED:  
                	 Toast.makeText(getApplicationContext(), "has connected: " , Toast.LENGTH_SHORT).show();                   
                    break;  
                case BluetoothService.STATE_CONNECTING:  
                	Toast.makeText(getApplicationContext(), "connecting: " , Toast.LENGTH_SHORT).show();   
                   // mTitle.setText(R.string.title_connecting);  
                    break;  
                case BluetoothService.STATE_LISTEN:  
                case BluetoothService.STATE_NONE:  
                	Toast.makeText(getApplicationContext(), "not_connected: " , Toast.LENGTH_SHORT).show();   
                 //   mTitle.setText(R.string.title_not_connected);  
                    break;  
                }  
                break;  
            case Bluetooth.MESSAGE_WRITE:  
                byte[] writeBuf = (byte[]) msg.obj;  
                // construct a string from the buffer  
                String writeMessage = new String(writeBuf);  
                Toast.makeText(getApplicationContext(), "writeMessage: "+writeMessage , Toast.LENGTH_SHORT).show(); 
               // mConversationArrayAdapter.add("Me:  " + writeMessage);  
                break;  
            case Bluetooth.MESSAGE_READ:  
                byte[] readBuf = (byte[]) msg.obj;  
                // construct a string from the valid bytes in the buffer  
                String readMessage = new String(readBuf, 0, msg.arg1);  
                mBt_content.setText(readMessage);  
               
                break;  
            case Bluetooth.MESSAGE_DEVICE_NAME:  
                // save the connected device's name  
              
                Toast.makeText(getApplicationContext(), "Connected to " , Toast.LENGTH_SHORT).show();  
                break;  
            case Bluetooth.MESSAGE_TOAST:  
                Toast.makeText(getApplicationContext(), msg.getData().getString(Bluetooth.TOAST), Toast.LENGTH_SHORT).show();  
                break;  
            case Bluetooth.MESSAGE_START_DISCOVERY:
            	mBTAdapter.startDiscovery();
				Log.d(TAG,"begin discovery BT");
				mBTStatusTextView.setText(getString(R.string.link));
				mListView_bt.setVisibility(View.VISIBLE);
            	break;
            case Bluetooth.MESSAGE_START_CONNECT:
            	mBtService.connect((BluetoothDevice)msg.obj);
            	break;
            }  
        }  
    };  
    
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		 setResult(RESULT_OK, mBackIntent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
         
		super.onBackPressed();
	}
	private void sendMessage(String message) {  
        // Check that we're actually connected before trying anything  
        if (mBtService.getState() != BluetoothService.STATE_CONNECTED) {  
            Toast.makeText(this, "not connected", Toast.LENGTH_SHORT).show();  
            return;  
        }  
  
        // Check that there's actually something to send  
        if (message.length() > 0) {  
            // Get the message bytes and tell the BluetoothService to write  
            byte[] send = message.getBytes();  
            mBtService.write(send);  
  
            // Reset out string buffer to zero and clear the edit text field  
            mOutStringBuffer.setLength(0);  
//            mBt_content.setText(mOutStringBuffer);  
        }  
    }  
 
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// 查找到设备action
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Toast.makeText(getApplicationContext(), "find a new BlueTooth Device", Toast.LENGTH_LONG).show();
				// 得到蓝牙设备
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 如果是已配对的则略过，已得到显示，其余的在添加到列表中进行显示
				Map<String, Object> map = new HashMap<String, Object>();
 				map.put(mBtName, device.getName());
				map.put(mBtAddress, device.getAddress());
				Log.d(TAG,"UUID is "+	device.getUuids());
//				if(mList_bt.size()>0){
//				for(int i =0;i<mList_bt.size();i++){
//					Map<String, Object> temp = mList_bt.get(i);
//					if(temp.get(mBtName).equals(device.getName())
//							&& temp.get(mBtAddress).equals(device.getAddress())){
//					}else{
//						mList_bt.add(map); 				
//						mAdapter_bt.notifyDataSetChanged();
//					}
//				}	
//				}else{
//					mList_bt.add(map); 				
//					mAdapter_bt.notifyDataSetChanged();
//				}	
				mList_bt.add(map); 				
				mAdapter_bt.notifyDataSetChanged();
				mListView_bt.setVisibility(View.VISIBLE);
			
			}

		}
		
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub		
		if (mBtService != null) {
			if (mBtService.getState() == BluetoothService.STATE_NONE) {  
			mBtService.start();
			}
		}
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		unregisterReceiver(mReceiver);
		if (mBtService != null) mBtService.stop();  
		super.onDestroy();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bt_send:
		{
			sendMessage(mBt_content.getText().toString());
			
		}
			break;
		case R.id.wifi_send:
			break;
		}
	}

}
