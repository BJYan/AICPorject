/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bluetooth.le;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.bluetooth.analysis.DataAnalysis;
import com.example.bluetooth.le.BluetoothLeClass.OnDataAvailableListener;
import com.example.bluetooth.le.BluetoothLeClass.OnServiceDiscoverListener;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity implements OnClickListener {
	private final static String TAG = "luotest.DeviceScanActivity";//DeviceScanActivity.class.getSimpleName();
	private final static String UUID_KEY_WRITE_DATA = "00002a52-0000-1000-8000-00805f9b34fb";//0000ffe1-0000-1000-8000-00805f9b34fb";
	private final static String UUID_KEY_READ_DATA = "00002a18-0000-1000-8000-00805f9b34fb";
    private LeDeviceListAdapter mLeDeviceListAdapter;
    /**搜索BLE终端*/
    private BluetoothAdapter mBluetoothAdapter;
    /**读写BLE终端*/
    private BluetoothLeClass mBLE;
    private boolean mScanning;
   // private Handler mHandler;
   
    private static final String fta="7F 14 0A 00 04 00 00 00 00 00 00 00 00 00 00 00 6f 14 f5 ca";
    private static final String ftb="7f 14 D1 01 01 00 00 00 00 00 00 00 00 00 00 00 0c 6d 35 b9";
    private static final String ftc="7F 14 D2 00 00 00 00 00 00 00 00 00 00 00 00 00 fd 02 d4 5e";
    private static final String ftd="7F 14 D3 00 00 00 00 00 00 00 00 00 00 00 00 00 fd 25 85 69";
    private static final String fte="7F 14 D4 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 15";
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private String strValue = "";
    private String strValue1 =""; 
    
    private ListView mListView;
    private Button mSendButton;
    private EditText mCmdText;
    private TextView mReceiveText;
    private TextView mReceiveTotalText;
   // private CheckBox mCheckBox;
    private RadioGroup mRadioGroup;
    private int readCount =0;
    private long mtime=0;
    private int mTestType=R.id.radio2;
    DataAnalysis dataAnalysis;
    
    
	String str ="7d7d7d7d0101040009e5ebf3f02cf4c4f9e6ff4b049f09c00e5312a61615187c19fb1a9f1a20189d15d212850e44097f046bf"
			+ "f08f9d2f49af006ebc4e88ee619e512e42de54ae6d6e9c7ed7bf17ff66efbc000f206560b5e0fd513c816e719101a531a62198e"
			+ "179814b111270c9507ad0284fd1bf7eef2d6eeabeab4e77ee56be4a4e496e58de7bdeae2eec1f318f836fd5702d808110cf5115"
			+ "5150517c419eb1a991a7d194416fd13e40fa00b52060800d9fb59f660f15ded15e994e6dfe51de475e4b2e60fe898ebd3effef4"
			+ "b1f9d5fef5047509670e3d127a160b188d19f11aa21a1c1860160a12670e4f09a8045eff22f9ddf4c2f001ebece8b0e645e4fae"
			+ "471e54ee6f5e9b9ed20f1a3f652fb7700b906310b5f0fd2138216dc19151a4e1ab0199917b714c511520cbf07e102b3fd28f803"
			+ "f306ee91eaade7a5e597e46de491e5eae79bea7eee5cf306f80efd4802b707d80cc8114314db17b119b21a9b1a721911172413b"
			+ "f0fce0b7806450117fb9af68df174ed07e9b6e707e55ce48de4cce639e88bebb7efeaf482f98cfec4044b09610e2d124f15c318"
			+ "3b19df1a821a2a185a15ee12aa0e6309d20481ff3cf9dcf4dff029ec1ee8bde64ce4e2e492e520e6f9e99eed1af18cf63efba10"
			+ "0c4063f0b0e0fc9139516e619091a291a7519bb17cb14fb11760ce2081e02b6fd61f812f333ee9feab9e7ace5d9e463e491e599"
			+ "e78deaa3ee60f2fdf7b8fd200294077e0c6410d914d117ba19b31a741a52193916f413ea10130b6206990118fbdcf691f1dbed5"
			+ "ae9f3e6f3e530e45ee4bde650e87eebc6efa9f481f97ffef40411094d0e0a124615b1184e19f11a7319f8186315fd128f0e7209"
			+ "a3048dff44f9e3f50cf023ec26e8c2e647e4dce462e549e6c5e968ed10f151f63bfb5d00aa06140b0a0fae13a616c7190c1a7a1"
			+ "aad19bc17ea154011a80d47084202dbfdc7f875f32aeebfead7e7dbe586e49ae48ce56ee77eea69ee29f2b7f7a7fcf1022a077d"
			+ "0c4610e214aa17b119761a801a71198c1732140510060b9706b40150fbf9f6b1f1f4ed89e9f2e705e558e479e4eae61fe893ebc"
			+ "0efb6f451f94afec0042109210e121232159d18541a021a951a0318b215f112bc0ea909d104c9ff5cfa53f4eaf027ec46e89ce6"
			+ "3ee4e7e457e530e6cce912ed03f0f5f5e6fb34007105b90af60f8a138116a319131a691aab19ee1813154711730d68085c0344f"
			+ "dc8f896f36aeea9eb6ae825e5bee4c2e487e58ce782ea66ee24f288f788fcb7022d07670c4a10b51482177519441a5f1a531951"
			+ "1713140210350b8b06920160fbe1f6e1f1fded97ea03e72ee565e494e4d9e603e871eb98efb6f454f96afeb903ed09310de7123"
			+ "b159a183119d81a711a2b18c3160913090eb30a1f0512ff9bfa46f511f068ec34e8e9e687e4f6e475e519e665e959ecb5f10ff5"
			+ "b6face005205a70aa50f7713661697190a1a501acd19d0181c155011bb0d7108710376fdd9f8c3f37eef2beb39e81ee5d1e4d1e"
			+ "457e593e781ea6cee29f27ff763fc8a0233072c0c3b109714871753196c1a6b1a5a192d171f141f102f0bce06c70193fc24f70b"
			+ "f217ed9ee9ffe714e545e4b6e4bde5e2e823eb9bef9ef41ef92dfe7703f309190dd4122315ad186b1a061ac51a3118e3166d133"
			+ "50ede09eb051effebfa84f555f076ec5fe8fce688e4d5e45de4ffe66de937ecabf0bff576faf7fff605550a970f261335167818"
			+ "da1a371a5a19fe17f0154511f40d7408ab0350fe18f8cff3a7ef34eb32e82ee5f7e4cee497e58de779ea74ee0af297f74afc650"
			+ "21607640c4810b3143b174b195f1a7c1a7319531754142b107f0bca06c601a2fc2ef6daf1e0eda3e9efe715e532e46ee474e615"
			+ "e82aeb6eef3cf3f1f8fbfe52039508e60db311e11599182f1a011aaf1a3818d9168b13520f120a5e052affe2fa9af53af09aec8"
			+ "ae91fe68ae4e3e466e501e68ce922ec88f0a9f564faa0ffef05610a460ef91317166118b51a1e1a9819d71816155e11fd0dab08"
			+ "b403a9fe6af8f6f3edef60eb67e82fe604e4b4e48ee572e74fea4dedfcf27ef737fc9301b807290c1510a01470175819391a7d1"
			+ "a9e19771761144910750bfe070a01a7fc73f71af245ede9ea0de74ee572e45de49fe5f9e7f5eb30ef06f3bdf8cbfdfd03810897";
	
	String str1="0d8d11b91563182719db1aa61a3618d0168913320f530a850596002ffacdf5b9f0ececaee90fe67ee51ee488e4f5e6cee932ec9"
			+ "3f078f561fa6effcf052e0a5f0edd130c164918ae1a201a6e19cc180a157511ea0d9b08c4039efe69f8e6f3dfef49eb3fe81ce5"
			+ "eae4bee491e560e774ea0aedd2f219f71afc6b01a1070d0bee10981450175b197a1a8b1a8d197b175714a910ca0c20075501dcf"
			+ "cbbf732f259edecea37e746e555e476e4a2e5b8e7ffeb23ef18f3a8f88dfdf00357088a0d6011a91546182a19c61a9b1a2518f1"
			+ "169f133b0f590a9305a00028faf5f5aef104eccfe951e6c2e51ee488e504e6a3e918ec79f07bf54cfa55ffb205240a2e0ee912e"
			+ "b164718b91a191ac419bf1848159911fa0dcc08df03e7fe4af8f9f402ef70eb4fe843e5e7e4c1e480e532e732ea08ed6df1d3f6"
			+ "f3fc0f01ac06c00bdb105b1431175219511aab1a8419b2179c14a210bd0c60076d0236fc9ef762f270ee5dea60e78de572e465e"
			+ "4a9e5bce80ceafbef23f352f875fdd4033708600d0711b9152817ed19b31aa21a4e18df166813700f480a94058b0077fafdf5bc"
			+ "f118eccae967e6b4e520e472e505e65de907ec58f065f539fa52ffd604fb0a280ecf12e4162818ee1a411a9519f51854159c122"
			+ "f0dea090d03e9fe89f936f3f8efa0eb5ce86de5f5e4a3e450e53fe707e9d1ed82f1edf6bbfc0b015406cb0bc1103c140e16f900"
			+ "001ab0e42d00000000000000000000000000002632000007028a512a64";
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(R.string.title_devices);
       
        setContentView(R.layout.main);
        
        dataAnalysis = new DataAnalysis();
        mListView = (ListView)findViewById(R.id.listView1);
        mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 final BluetoothDevice device = mLeDeviceListAdapter.getDevice(arg2);
			        if (device == null) return;
			        if (mScanning) {
			            mBluetoothAdapter.stopLeScan(mLeScanCallback);
			            mScanning = false;
			        }
			        
			        mBLE.connect(device.getAddress());
				
			}});
        mSendButton = (Button)findViewById(R.id.button1);
        mSendButton.setOnClickListener(this);
        mCmdText = (EditText)findViewById(R.id.editText1);
        mReceiveText = (TextView)findViewById(R.id.textView2);
        mReceiveTotalText = (TextView)findViewById(R.id.textView4);
        mRadioGroup = (RadioGroup)findViewById(R.id.radiogroup1);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				mTestType=	arg0.getCheckedRadioButtonId();
//				switch(arg0.getCheckedRadioButtonId()){
//				case R.id.radio1:
//					mTestType=	;
//					break;
//				case R.id.radio2:
//					break;
//				case R.id.radio3:
//					break;
//				case R.id.radio4:
//					break;
//				case R.id.radio5:
//					break;
//					
//				}
				
			}
        	
        });
       // mCheckBox = (CheckBox)findViewById(R.id.checkbox1);
       
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        //开启蓝牙
        mBluetoothAdapter.enable();
        
        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
        //发现BLE终端的Service时回调
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        //收到BLE终端数据交互的事件
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
       // setListAdapter(mLeDeviceListAdapter);
        mListView.setAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
        mBLE.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBLE.close();
    }
    

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    /**
     * 搜索到BLE终端服务的事件
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener(){

		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			displayGattServices(mBLE.getSupportedGattServices(),null,mTestType);
		}
    	
    };
    
    /**
     * 收到BLE终端数据交互的事件
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new OnDataAvailableListener(){

    	/**
    	 * BLE终端数据被读的事件
    	 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			Log.d(TAG,"onCharacteristicRead() "+status +","+characteristic.getValue());
			if (status == BluetoothGatt.GATT_SUCCESS) 
				Log.e(TAG,"onCharRead "+gatt.getDevice().getName()
						+" read "
						+characteristic.getUuid().toString()
						+" -> "
						+Utils.bytesToHexString(characteristic.getValue()));
			 Log.d(TAG,"onCharacteristicRead() "+","+Utils.bytesToHexString(characteristic.getValue()));
			
		}
		
	    /**
	     * 收到BLE终端写入数据回调
	     */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			String teStr= Utils.bytesToHexString(characteristic.getValue());
			Log.e(TAG,"onCharWrite "+gatt.getDevice().getName()
					+" write "
					+characteristic.getUuid().toString()
					+" -> "
					+teStr);
			
			if(strValue.length()<3800) strValue= strValue+teStr;
			else strValue1= strValue1+teStr;
			
			Log.d(TAG," read data "+(++readCount) +","+Utils.bytesToHexString(characteristic.getValue()));
			//Log.d(TAG," eread data  strValue is "+teStr);
			Log.d(TAG,"Atotal_receive_data_strValue : "+strValue.toString());
			Log.d(TAG,"Atotal_receive_data_strValue1 : "+strValue1.toString());
			//Log.d(TAG," total receive data  charValue : "+new String(testValue));
			Message msg = mHandler.obtainMessage(1);
			msg.arg1=readCount;
			Bundle b = new Bundle();
			b.putString("data", teStr);
			b.putString("data1", strValue);
			b.putString("data2", strValue1);
			b.putLong("time", System.currentTimeMillis());
			msg.setData(b);
			//if(teStr.length()<40) mHandler.sendMessage(msg);
			mHandler.sendMessage(msg);
			
		}
    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
	
    private void displayGattServices(List<BluetoothGattService> gattServices,String cmdStr,int idtype) {
        if (gattServices == null) return;
        strValue="";
        strValue1="";
       // strValue.delete(0, strValue.length()-1);
               for (BluetoothGattService gattService : gattServices) {
        	//-----Service的字段信息-----//
        	int type = gattService.getType();
            Log.e(TAG,"-->service type:"+Utils.getServiceType(type));
            Log.e(TAG,"-->includedServices size:"+gattService.getIncludedServices().size());
            Log.e(TAG,"-->service uuid:"+gattService.getUuid());
            
            //-----Characteristics的字段信息-----//
            List<BluetoothGattCharacteristic> gattCharacteristics =gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic  gattCharacteristic: gattCharacteristics) {
                Log.e(TAG,"---->char uuid:"+gattCharacteristic.getUuid());
                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG,"---->char permission:"+Utils.getCharPermission(permission));
                
                int property = gattCharacteristic.getProperties();
                Log.e(TAG,"---->char property:"+Utils.getCharPropertie(property));

                byte[] data = gattCharacteristic.getValue();
        		if (data != null && data.length > 0) {
        			Log.e(TAG,"---->char value:"+new String(data));
        		}
        		Log.e("luotest","---->UUID:"+new String(gattCharacteristic.getUuid().toString()));
        		//UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
        		if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_WRITE_DATA)){        			
        			//测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
        			mHandler.sendMessage(mHandler.obtainMessage(MSG_RE_SEND_DATA));
        			//接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
        			mBLE.setCharacteristicNotification(gattCharacteristic, true);
        			//设置数据内容 7f 14
        			String ft="";
    				switch(mTestType){
    				case R.id.radio1:
    					ft=fta;
    					break;
    				case R.id.radio2:
    					ft=ftb;
    					break;
    				case R.id.radio3:
    					ft=ftc;
    					break;
    				case R.id.radio4:
    					ft=ftd;
    					break;
    				case R.id.radio5:
    					ft=fte;
    					break;
    					
    				}
        			byte[]b =Utils.hexStringToByteArray(ft.replace(" ", ""));
        			Log.d("luotest","luo a displayGattServices "+b.length);
        			
        			
        			if(b!=null){
        			gattCharacteristic.setValue(b);
        			}else{
        				mHandler.sendEmptyMessage(MSG_ERROR);
        				return;        				
        			}
        			//往蓝牙模块写入数据
        			mBLE.writeCharacteristic(gattCharacteristic);
        		}
        		
        		//read BLE data
        		if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_READ_DATA)){     
	        		mBLE.setCharacteristicNotification(gattCharacteristic, true);
	        		mBLE.readCharacteristic(gattCharacteristic);
        		}
        		//-----Descriptors的字段信息-----//
				List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic.getDescriptors();
				for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
					Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
					int descPermission = gattDescriptor.getPermissions();
					Log.e(TAG,"-------->desc permission:"+ Utils.getDescPermission(descPermission));
					
					byte[] desData = gattDescriptor.getValue();
					if (desData != null && desData.length > 0) {
						Log.e(TAG, "-------->desc value:"+ new String(desData));
					}
        		 }
            }
        }//

    }


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG,"luo onClick()");
		switch(arg0.getId()){
		case R.id.button1:
			if("".equals(mCmdText.getText().toString())){
				Toast.makeText(getApplicationContext(), "命令不能为空", Toast.LENGTH_SHORT).show();
			}else{
				 ;
				displayGattServices(mBLE.getSupportedGattServices(),mCmdText.getText().toString(),mTestType);
			}
			
			/*Message msg = mHandler.obtainMessage(1);
			msg.arg1=readCount;
			Bundle b = new Bundle(); 
			b.putString("data1", str);
			b.putString("data2", str1);
			b.putLong("time", System.currentTimeMillis());
			msg.setData(b); 
			mHandler.sendMessage(msg);*/
			
		break;
		
		}
	}
 private final static int MSG_DISPLAY_DATA =1;
 private final static int MSG_RE_SEND_DATA =2;
 private final static int MSG_ERROR =3;
 //private final static int MSG_DISPLAY_DATA =1;
 
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case MSG_DISPLAY_DATA:
				String str =(String) msg.getData().get("data");
				//strValue1 = strValue1+str;
				//Log.i(TAG, ">>>>>>>>>>>>>"+strValue1.length());
				long time= (Long) msg.getData().get("time");
				mReceiveText.setText(String.valueOf(msg.arg1)+"条, 耗时:" +String.valueOf(time-mtime) +"ms,\n本包:"+str);
				mReceiveTotalText.setText((String) msg.getData().get("data2"));
				String Value1 = (String) msg.getData().get("data1");
				String Value2 = (String) msg.getData().get("data2");
				//Log.d(TAG,"test_data_strValue : "+Value1.toString());
				//Log.d(TAG,"test_data_strValue1 : "+Value2.toString());
				String result = Value1 + Value2;
				if(result.length()==4176) {
					Log.e(TAG, "qwe=" +result);
					dataAnalysis.getResult(result);
					float[] data = dataAnalysis.getData();
					float[] MinMaxTemp = new float[]{data[0],data[0]};
					for(int i=0;i<data.length;i++){
						if(data[i]<=MinMaxTemp[0]) MinMaxTemp[0] = data[i];
						if(data[i]>MinMaxTemp[1]) MinMaxTemp[1] = data[i];
					}
					String XTitle = "测点序号\nMin:"+MinMaxTemp[0]+"\nMax:"+MinMaxTemp[1]+"\n测试点数："+dataAnalysis.getDataNum();
					String YTitle = "m/s^2";
					 startActivity(getBlackLineChartView("Test", data, new String[]{XTitle,YTitle}));
				   }
				break;
			case MSG_RE_SEND_DATA:
				readCount=0;
				mtime= System.currentTimeMillis();
				mReceiveTotalText.setText("");
    			mReceiveText.setText("");
				break;
			case MSG_ERROR:
				Toast.makeText(getApplicationContext(), "下发命令长度应不大于20个字节", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
		 
	 };
	 
	 public Intent getBlackLineChartView(String title, float[] y, String[] xyTitle){
		    
		 String[] titles = new String[]{title};
		 List<float[]> yValues = new ArrayList<float[]>();
		 yValues.add(y);
		 List<float[]> xValues = new ArrayList<float[]>(); 
		 float[] x= new float[y.length];
		 for(int i=0;i<y.length;i++){
			 x[i]=i;
		 }
		 xValues.add(x);
		 XYMultipleSeriesDataset dataset = buildDataset(titles, xValues, yValues);

		 XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    renderer.setXLabels(12);
		    renderer.setYLabels(10);
		    renderer.setShowGrid(true);
		    renderer.setXLabelsAlign(Align.RIGHT);
		    renderer.setYLabelsAlign(Align.RIGHT);
		    renderer.setZoomButtonsVisible(true);
		    //renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		    //renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		    renderer.setBackgroundColor(Color.WHITE);
		    renderer.setMarginsColor(Color.WHITE);
		    renderer.setApplyBackgroundColor(true);
		    renderer.setYAxisMin(-300);
		    renderer.setYAxisMax(300);
		    renderer.setXAxisMin(0);
		    renderer.setXAxisMax(200);
		    renderer.setLegendHeight(150);
		    //renderer.setXLabels(200);
		    renderer.setPanEnabled(true, true);
		    renderer.setPanLimits(new double[]{0, 4500, 0, 1700});
		    renderer.setMargins(new int[] {20, 35, 10, 10});
		    renderer.setPanEnabled(true,true);
		    if(xyTitle!=null){
			    renderer.setAxisTitleTextSize(20);
			    renderer.setXTitle(xyTitle[0]);
			    renderer.setYTitle(xyTitle[1]);
		    }

		 XYSeriesRenderer XYrenderer = new XYSeriesRenderer();
		    XYrenderer.setColor(Color.RED);
		    XYrenderer.setPointStyle(PointStyle.CIRCLE);
		    renderer.addSeriesRenderer(XYrenderer);
		 return ChartFactory.getLineChartIntent(getApplicationContext(), dataset, renderer);
	 }
	 
	 /**
	  * 曲线图(数据集) : 创建曲线图图表数据集
	  * 
	  * @param 赋予的标题
	  * @param xValues x轴的数据
	  * @param yValues y轴的数据
	  * @return XY轴数据集
	  */
	 protected XYMultipleSeriesDataset buildDataset(String[] titles, List<float[]> xValues,
	     List<float[]> yValues) {
	   XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();/* 创建图表数据集 */
	   addXYSeries(dataset, titles, xValues, yValues, 0);              /* 添加单条曲线数据到图表数据集中 */
	   return dataset;
	 }
	  
	 /**
	  * 曲线图(被调用方法) : 添加 XY 轴坐标数据 到 XYMultipleSeriesDataset 数据集中
	  * 
	  * @param dataset 最后的 XY 数据集结果, 相当与返回值在参数中
	  * @param titles  要赋予的标题
	  * @param xValues x轴数据集合
	  * @param yValues y轴数据集合
	  * @param scale   缩放
	  * 
	  * titles 数组个数 与 xValues, yValues 个数相同
	  * tittle 与 一个图标可能有多条曲线, 每个曲线都有一个标题
	  * XYSeries 是曲线图中的 一条曲线, 其中封装了 曲线名称, X轴和Y轴数据
	  */
	 public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<float[]> xValues,
	     List<float[]> yValues, int scale) {
	   int length = titles.length;                         /* 获取标题个数 */
	   for (int i = 0; i < length; i++) {
	     XYSeries series = new XYSeries(titles[i], scale); /* 单条曲线数据 */
	     float[] xV = xValues.get(i);                     /* 获取该条曲线的x轴坐标数组 */
	     float[] yV = yValues.get(i);                     /* 获取该条曲线的y轴坐标数组 */
	     int seriesLength = xV.length;
	     for (int k = 0; k < seriesLength; k++) {
	       series.add(xV[k], yV[k]);                       /* 将该条曲线的 x,y 轴数组存放到 单条曲线数据中 */
	     }
	     dataset.addSeries(series);                        /* 将单条曲线数据存放到 图表数据集中 */
	   }
	 }
}