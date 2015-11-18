package com.aic.aicdetactor.bluetooth;

import java.util.List;

import com.aic.aicdetactor.util.MyCRC32;
import com.aic.aicdetactor.util.SystemUtil;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothLeControl {
	BluetoothLeClass mBLE;
	public static final int Message_Beigin =200;
	public static final int Message_Connection_Status =Message_Beigin+1;
	public static final int MessageReadDataFromBT=Message_Beigin+2;
	public static final int MessageSetDataToBT=Message_Beigin+3;
	public static final int MSG_ERROR=Message_Beigin+4;
	public static final int Message_Start_Scanner=Message_Beigin+5;
	public static final int Message_Stop_Scanner=Message_Beigin+6;
	public static final int Message_End_Upload_Data_From_BLE=Message_Beigin+7;
	private boolean bStartCommunicationWithBT=false;
	private boolean isConnected=false;
	//5秒钟没获取数据就认为收据发送完毕
	private boolean bGetDataContinue=false;
	private final int MaxSecond=5;
	private final static String UUID_KEY_WRITE_DATA = "00002a52-0000-1000-8000-00805f9b34fb";
	private final static String UUID_KEY_READ_DATA = "00002a18-0000-1000-8000-00805f9b34fb";
	private final String TAG="BluetoothLeControl";
	//下载参数命令包 5    6f 14 f5 ca
	public static final float[] fta={0x7F,0x14,0xD5,0x0A,0x00 ,0x04 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00};
	//读实时数据    a2 63 66 77
	public static final float[] ftb={0x7f,0x14,0xD1,0x01,0x01 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00};
	//读传感器本身参数   fd 02 d4 5e
	public static final float[] ftc={0x7F,0x14,0xD2,0x00,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00};
	//采集转速    fd 25 85 69
	public static final float[] ftd={0x7F,0x14,0xD3,0x00,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00};
	//下载设备名称 17 18 19 15
	public static final float[] fte={0x7F,0x14,0xD4,0x01,0x02 ,0x03 ,0x04 ,0x05 ,0x06 ,0x07 ,0x08 ,0x09 ,0x10 ,0x11 ,0x12 ,0x13 ,0x14 ,0x15 ,0x16};

	    
	    
	private Handler mHandler=null;
	/**
	 * 单例
	 * @param context
	 * @param handler
	 */
	private BluetoothLeControl(Context context){
		mBLE = BluetoothLeClass.getInstance(context);
		mBLE.initialize();	
		mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
		mBLE.setOnDataAvailableListener(mOnDataAvailable);
		mBLE.setOnConnectListener(mOnConnectListener);
		mBLE.setOnDisconnectListener(mOnDisconnectListener);
	}
	
	private static BluetoothLeControl singleton = null;
	public static BluetoothLeControl getInstance(Context cxt) {
		
			if (singleton == null) {
				singleton = new BluetoothLeControl(cxt);
			}
			return singleton;
	}
	/**
	 * 每次传输数据时候都需要先调用此函数
	 * @param handler
	 */
	public void setParamates(Handler handler){
		mHandler=handler;
		mHandler.sendEmptyMessage(BluetoothLeControl.Message_Stop_Scanner);
		
	}
	
	public void disconnection(){
		mBLE.disconnect();
		mBLE.close();
		isConnected = false;
	}
	/**
	 * 设置连接蓝牙的地址
	 * @param strAdress
	 * @return
	 */
	public boolean Connection(String strAdress){
		//if(isConnected) return isConnected;
		return isConnected=mBLE.connect(strAdress);
	}
	
	public boolean isConnected(){
		return isConnected;
	}
	
	private BluetoothLeClass.OnConnectListener mOnConnectListener = new BluetoothLeClass.OnConnectListener() {
		
		@Override
		public void onConnect(BluetoothGatt gatt) {
			// TODO Auto-generated method stub
			Message msg = mHandler.obtainMessage(Message_Connection_Status);
			msg.arg1=1;
			mHandler.sendMessage(msg);
		}
	};
	
	private BluetoothLeClass.OnDisconnectListener mOnDisconnectListener = new BluetoothLeClass.OnDisconnectListener() {
		
		@Override
		public void onDisconnect(BluetoothGatt gatt) {
			// TODO Auto-generated method stub
			Message msg = mHandler.obtainMessage(Message_Connection_Status);
			msg.arg1=0;
			mHandler.sendMessage(msg);
		}
	};
	/**
     * 搜索到BLE终端服务的事件
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener(){

		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) 0xd2, (byte)0, (byte)0);	            	 
        	Communication2Bluetooth(cmd);
		}
    	
    };
    
    /**
     * 收到BLE终端数据交互的事件
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener(){

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
						+SystemUtil.bytesToHexString(characteristic.getValue()));
			 Log.d(TAG,"onCharacteristicRead() "+","+SystemUtil.bytesToHexString(characteristic.getValue()));
			 count=0;
		}
		
		int count =0;
	    /**
	     * 收到BLE终端写入数据回调
	     */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			byte[] byteData=characteristic.getValue();
			String teStr= SystemUtil.bytesToHexString(characteristic.getValue());
			Log.e(TAG,"onCharWrite "+gatt.getDevice().getName()
					+" write "
					+characteristic.getUuid().toString()
					+" -> "
					+teStr);
			bGetDataContinue=true;
			count++;
			Message msg = mHandler.obtainMessage(MessageReadDataFromBT);
			Bundle b = new Bundle();
			//b.putString("data", teStr);	
			b.putByteArray("key_byte", byteData);
			b.putInt("count", count);
			msg.setData(b);
			mHandler.sendMessage(msg);
			bGetDataContinue=false;
		}
    };
    public List<BluetoothGattService> getSupportedGattServices(){
    	return mBLE.getSupportedGattServices();
    }
    /**
	 * 先发命令 再获取数据
	 * @param cmdStr
	 */
	 public void Communication2Bluetooth(byte[] cmdByte) {
		 List<BluetoothGattService> gattServices= mBLE.getSupportedGattServices();
	        if (gattServices == null || cmdByte==null) {return;}
	        
	        for (BluetoothGattService gattService : gattServices) {
	        	//-----Service的字段信息-----//
	        //	int type = gattService.getType();
	            	   Log.e(TAG,"in Communication2Bluetooth()");
	            //-----Characteristics的字段信息-----//
	            List<BluetoothGattCharacteristic> gattCharacteristics =gattService.getCharacteristics();
	            for (final BluetoothGattCharacteristic  gattCharacteristic: gattCharacteristics) {
	                Log.e(TAG,"---->char uuid:"+gattCharacteristic.getUuid());
	            //    int permission = gattCharacteristic.getPermissions();
	                
	            //    int property = gattCharacteristic.getProperties();

	                byte[] data = gattCharacteristic.getValue();
	        		if (data != null && data.length > 0) {
	        			Log.e(TAG,"---->char value:"+new String(data));
	        		}
	        		Log.e("luotest","---->UUID:"+new String(gattCharacteristic.getUuid().toString()));
	        		//UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
	        		if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_WRITE_DATA)){        			
	        			//测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
	        			//mHandler.sendMessage(mHandler.obtainMessage(MSG_RE_SEND_DATA));
	        			//接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
	        			mBLE.setCharacteristicNotification(gattCharacteristic, true);
	        			//设置数据内容 7f 14
//	        			String ftb="7f 14 D1 01 01 00 00 00 00 00 00 00 00 00 00 00";
//	        			byte[]b =SystemUtil.hexStringToByteArray(ftb.replace(" ", ""));
//	        			if(b!=null){
//		        			gattCharacteristic.setValue(b);
//		        			}
	        			
	        			if(cmdByte!=null){
	        			gattCharacteristic.setValue(cmdByte);
	        			}
	        			else{
	        				mHandler.sendEmptyMessage(MSG_ERROR);
	        				return;        				
	        			}
	        			//往蓝牙模块写入数据
	        			mBLE.writeCharacteristic(gattCharacteristic);
	        			recLen=0;
	        			handler.postDelayed(runnable, 1000);
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
					//	int descPermission = gattDescriptor.getPermissions();
						
						byte[] desData = gattDescriptor.getValue();
						if (desData != null && desData.length > 0) {
							Log.e(TAG, "-------->desc value:"+ new String(desData));
						}
	        		 }
	            }
	        }//

	    }
	 public String getSensorTypeFromResponseData(String responseData,String cmdType){
		 if(responseData!=null && responseData.length()>0){
			 if(cmdType!=null){
				 cmdType.subSequence(4, 5);
			 }
			 return null;
	 }else
	 {
		 return "-1";
	 }
	 }
	 
	 public static  byte[] genDownLoadCommand(byte frameHead,byte commandLenth,byte readSensorParams,byte AxCount,byte SensorType){
		 byte[] download=new byte[16];
//		 if(frameHead==null||frameHead.length()==0
//				 ||commandLenth==null||commandLenth.length()==0
//				 ||readSensorParams==null||readSensorParams.length()==0){
//			 return null;
//		 }
		 if(readSensorParams==(BluetoothConstast.CMD_Type_ReadSensorParams)){
			 download[0]=0x7f;
			 download[1]=0x14;
			 download[2]=(byte) 0xd2;
		 }else if(readSensorParams==(BluetoothConstast.CMD_Type_CaiJi)){
			 download[0]=0x7f;
			 download[1]=0x14;
			 download[2]=(byte) 0xd1;			
			 download[3]=AxCount;
			 download[4]= SensorType;
		 }
		 
		 byte[] temp=new byte[16];
		 for(int i=0;i<temp.length;i++){
			 temp[i]=download[i];
		 }
String last=MyCRC32.getCRC32(temp)	;
byte[]b=last.getBytes();
last=SystemUtil.getCRC32(download);
byte[]d=last.getBytes();
int k =0;
k++;
		 return download;
	 }
	 
	 //设置倒计时来控制或判断BLE发送数据完毕
	 int recLen =0;
	 Handler handler = new Handler(); 
	    Runnable runnable = new Runnable() { 
	        @Override 
	        public void run() { 
	            recLen++; 
	            Log.d(TAG, "Runnable() recLen="+recLen);
	            if(recLen<21){
	            handler.postDelayed(this, 1000); 
	            }else{
	            	mHandler.sendEmptyMessage(Message_End_Upload_Data_From_BLE);
	            }
	        } 
	    }; 
	    
}
