package com.aic.aicdetactor.bluetooth.analysis;

import java.math.BigDecimal;
import java.util.zip.CRC32;

import android.util.Log;
import android.widget.Toast;

import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.fragment.BlueTooth_Fragment;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 解析接受到的BLE数据
 * 用法如下
 * ReceivedDataAnalysis analysis = new ReceivedDataAnalysis();
 * analysis.getDataFromBLE(byte[]data,boolean bStartReceiveData);
 * 如果接受完毕 接着分析数据
 * boolean isValid = analysis.isValidate();
 * 
 * @author AIC
 *
 */
public class ReceivedDataAnalysis {
	float []mReceiveWaveFloatData=null;
	byte []mReceiveWaveByteData=null;
	
	byte []mReceiveByteDataCRC=null;
	int mReceivedDataCounts=0;
	int caiyangdian=0;
	int mAxCounts=0;
	byte mDLCmd=0;
	final String TAG="ReceivedDataAnalysis";
	int mReceivedDataByteSizes=0;
	int mShoudReceivedByteSizes=0;
	/**
	  * 获取wave数据
	  */
	public float[] getWaveFloatData(){
		 switch(mDLCmd){
		 case BluetoothConstast.CMD_Type_ReadSensorParams:
			 break;
		 case BluetoothConstast.CMD_Type_CaiJi:
			 for(int i=0;i<mReceiveWaveFloatData.length;i=i+2){
				 mReceiveWaveFloatData[i]=mReceiveByteDataCRC[i+11]<<8|mReceiveByteDataCRC[i+12];
			 }
			 break;
		 }
		 	return mReceiveWaveFloatData; 
	 }
	 
	 /**
	  * 
	  */
	public  byte[] getWaveByteData(){
		 switch(mDLCmd){
		 case BluetoothConstast.CMD_Type_ReadSensorParams:
			 break;
		 case BluetoothConstast.CMD_Type_CaiJi:
			 System.arraycopy(mReceiveByteDataCRC, 10, mReceiveWaveByteData, 0, getDataPointCount()*2);
			 break;
		 }
		 	return mReceiveByteDataCRC; 
	 }
	 //获取轴数
	 public int getAxCount(){
		 mAxCounts= mReceiveByteDataCRC[4];
		 return mAxCounts;
	 }
	 int getDataPointCount(){
		return  mReceiveByteDataCRC[6]<<8|mReceiveByteDataCRC[7];
	 }
	    
	 
	 /**
		 * 有效数值
		 * @return
		 */
		public float getValidValue(){
			double dValue=0;
			 switch(mDLCmd){
			 case BluetoothConstast.CMD_Type_ReadSensorParams:
				 break;
			 case BluetoothConstast.CMD_Type_CaiJi:
				 for(int i=0;i<mReceiveWaveFloatData.length;i++){
						dValue+=Math.pow(mReceiveWaveFloatData[i], 2);
					}
					dValue=Math.sqrt(dValue/mReceiveWaveFloatData.length);
					BigDecimal   b  =   new BigDecimal(dValue);  
					dValue  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
				 break;
			 }
			 
			
			 
			if(true)Log.d(TAG, "getValidValue()dValue = "+dValue);
			return (float) dValue;
		}
		/**
		 * 峰值
		 * @return
		 */
		public float getFabsMaxValue(){
			float dValue=0;
			 switch(mDLCmd){
			 case BluetoothConstast.CMD_Type_ReadSensorParams:
				 break;
			 case BluetoothConstast.CMD_Type_CaiJi:
				 for(int i=0;i<mReceiveWaveFloatData.length;i++){
						if(dValue<Math.abs(mReceiveWaveFloatData[i])){
							dValue=Math.abs(mReceiveWaveFloatData[i]);
						}
					}
					BigDecimal   b  =   new BigDecimal(dValue);  
					dValue  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
				 break;
			 }
			 
			
			 
			if(true)Log.d(TAG, "getFabsMaxValue()dValue = "+dValue);
			return dValue;
		}
		/**
		 * 获取峰峰数值
		 * @return
		 */
		public float getFengFengValue(){
			float dValue=0;
			float nValue=0;
			switch(mDLCmd){
			 case BluetoothConstast.CMD_Type_ReadSensorParams:
				 break;
			 case BluetoothConstast.CMD_Type_CaiJi:
				 for(int i=0;i<mReceiveWaveFloatData.length;i++){
						if(dValue<mReceiveWaveFloatData[i]){
							dValue=mReceiveWaveFloatData[i];
						}
						
						if(nValue>=mReceiveWaveFloatData[i]){
							nValue=mReceiveWaveFloatData[i];
						}
					}
					BigDecimal   b  =   new BigDecimal(dValue-nValue);  
					dValue  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
				 break;
			 }
			
			
			 
			if(true)Log.d(TAG, "getFengFengValue()dValue = "+dValue);
			return dValue;
		}
		
		/**
		 * 判断是否有效数值，如果无效的话，直接抛弃，否则进行进一步分析
		 * @param receivedAllBytes
		 * @param cmdtype
		 * @param shuldLenth
		 * @return 1 有效，其他无效
		 */
		public static  boolean  isValidate(byte[] receivedAllBytes,byte cmdtype){
			boolean  returnValue =false;
			if(receivedAllBytes!=null && receivedAllBytes.length>=20){
				switch(cmdtype){
				case BluetoothConstast.CMD_Type_CaiJi:
				{
					if(SystemUtil.bytesToHexString(receivedAllBytes).substring(0, 8).toLowerCase().equals("7d7d7d7d")){
						//CRC32校验
						CRC32 crc = new CRC32();
						crc.update(receivedAllBytes, 0, receivedAllBytes.length-4);
						String s =Long.toHexString(crc.getValue());
						
						byte[]crcValue = new byte[4];
						for(int i=0;i<4;i++){
							crcValue[i]=receivedAllBytes[receivedAllBytes.length-4+i];
						}
						String sa=SystemUtil.bytesToHexString(crcValue);
						if(sa.equals(s)){
							returnValue=true;
						}
					}
					
				}
				break;
				case BluetoothConstast.CMD_Type_ReadSensorParams:
				{
					if(SystemUtil.bytesToHexString(receivedAllBytes).substring(0, 6).toLowerCase().equals("7d14d2")){
						returnValue=true;
					}
				}
					break;
				case BluetoothConstast.CMD_Type_GetTemper:
				{
					if(SystemUtil.bytesToHexString(receivedAllBytes).substring(0, 6).toLowerCase().equals("7d14d6")){
						returnValue=true;
					}
				}
				break;
				case BluetoothConstast.CMD_Type_GetCharge:
				{
					if(SystemUtil.bytesToHexString(receivedAllBytes).substring(0,6).toLowerCase().equals("7d14d7")){
						returnValue=true;
					}
				}
				break;
				case BluetoothConstast.CMD_Type_CaiJiZhuanSu:
				{
					if(SystemUtil.bytesToHexString(receivedAllBytes).substring(0, 6).toLowerCase().equals("7d14d3")){
						returnValue=true;
					}
				}
				break;
				}
				
			}
			
			if(true)Log.d("ReceivedDataAnalysis", "isValidate()returnValue = "+returnValue);
			return returnValue ;
		}
	
		
		public boolean  isValidate(){
			boolean  returnValue =false;
			if(mReceiveByteDataCRC!=null && mReceiveByteDataCRC.length>=20){
				switch(mDLCmd){
				case BluetoothConstast.CMD_Type_CaiJi:
				{
					if(SystemUtil.bytesToHexString(mReceiveByteDataCRC).substring(0, 8).toLowerCase().equals("7d7d7d7d")){
						//CRC32校验
						CRC32 crc = new CRC32();
						crc.update(mReceiveByteDataCRC, 0, mReceiveByteDataCRC.length-4);
						String s =Long.toHexString(crc.getValue());
						
						byte[]crcValue = new byte[4];
						for(int i=0;i<4;i++){
							crcValue[i]=mReceiveByteDataCRC[mReceiveByteDataCRC.length-4+i];
						}
						String sa=SystemUtil.bytesToHexString(crcValue);
						if(sa.equals(s)){
							returnValue=true;
						}
					}
					
				}
				break;
				case BluetoothConstast.CMD_Type_ReadSensorParams:
				{
					if(SystemUtil.bytesToHexString(mReceiveByteDataCRC).substring(0, 6).toLowerCase().equals("7d14d2")){
						returnValue=true;
					}
				}
					break;
				case BluetoothConstast.CMD_Type_GetTemper:
				{
					if(SystemUtil.bytesToHexString(mReceiveByteDataCRC).substring(0, 6).toLowerCase().equals("7d14d6")){
						returnValue=true;
					}
				}
				break;
				case BluetoothConstast.CMD_Type_GetCharge:
				{
					if(SystemUtil.bytesToHexString(mReceiveByteDataCRC).substring(0,6).toLowerCase().equals("7d14d7")){
						returnValue=true;
					}
				}
				break;
				case BluetoothConstast.CMD_Type_CaiJiZhuanSu:
				{
					if(SystemUtil.bytesToHexString(mReceiveByteDataCRC).substring(0, 6).toLowerCase().equals("7d14d3")){
						returnValue=true;
					}
				}
				break;
				}
				
			}
			
			if(true)Log.d(TAG, "isValidate()returnValue = "+returnValue);
			return returnValue ;
		}
		
	 int getReceivedByteSizes(){
		return mReceivedDataByteSizes;
	}
	
	 int getShoudReceivedByteSizes(){
		return mShoudReceivedByteSizes;
	}
	
	public boolean isReceivedAllData(){
		if(mReceivedDataByteSizes==mShoudReceivedByteSizes){
			return true;
		}
		return false;
	}
	
		
		/**
		 * 
		 * @param strbyte
		 * @return
		 */
	private byte getCMDTypeFromFirstReceivedPacageData(byte []strbyte){
			byte cmd=0;
			if(SystemUtil.bytesToHexString(strbyte).substring(0, 8).toLowerCase().equals("7d7d7d7d")){
				cmd=(byte) 0xd1;
    		}
			if(SystemUtil.bytesToHexString(strbyte).substring(0, 6).toLowerCase().equals("7d14d2")){
				cmd=(byte) 0xd2;
			}
			
			if(SystemUtil.bytesToHexString(strbyte).substring(0, 6).toLowerCase().equals("7d14d6")){
				cmd=(byte) 0xd6;
			}
			if(SystemUtil.bytesToHexString(strbyte).substring(0,6).toLowerCase().equals("7d14d7")){
				cmd=(byte) 0xd7;
			}
			if(SystemUtil.bytesToHexString(strbyte).substring(0, 6).toLowerCase().equals("7d14d3")){
				cmd=(byte) 0xd3;
			}
			return cmd;
		}
		/**
		 * 接受获取到的BLE数据
		 * @param strbyte
		 * @param bStartReceiveData
		 */
	public	void getDataFromBLE(byte []strbyte,boolean bStartReceiveData){
	    	if(!bStartReceiveData){
	    		mDLCmd=getCMDTypeFromFirstReceivedPacageData(strbyte);
				switch(mDLCmd)
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
					//	Toast.makeText(BlueTooth_Fragment.this.getActivity(), "收到信息 正确", Toast.LENGTH_SHORT).show();	
					}
					if(mReceiveByteDataCRC!=null){
						mReceiveByteDataCRC=null;
					}
					mShoudReceivedByteSizes=20;
					mReceiveByteDataCRC = new byte[mShoudReceivedByteSizes];
					
					break;
				case (byte)0xd1:
					caiyangdian=strbyte[6]<<8|strbyte[7];
					mAxCounts= strbyte[4];
					if(mReceiveWaveFloatData!=null){
						mReceiveWaveFloatData=null;						
					}
					mReceiveWaveFloatData = new float[caiyangdian];
					mReceiveWaveByteData = new byte[caiyangdian*2];
							
					mShoudReceivedByteSizes = caiyangdian*2 +10+9+9+3*4;
					if(mReceiveByteDataCRC!=null){
						mReceiveByteDataCRC=null;
					}
				
					 mReceiveByteDataCRC = new byte[mShoudReceivedByteSizes];
					break;
				}
				
			}
	    	mReceivedDataByteSizes=mReceivedDataByteSizes+strbyte.length;
			//收集BLE发送过来的所有原始数据
			System.arraycopy(strbyte,0,mReceiveByteDataCRC,mReceivedDataCounts*20,strbyte.length);	
			//Toast.makeText(BlueTooth_Fragment.this.getActivity(), "收到信息", Toast.LENGTH_SHORT).show();
			mReceivedDataCounts++;
	    }
	
	/**
	 * 重置数据
	 */
	public void reset(){
		if(mReceiveWaveFloatData!=null){
			mReceiveWaveFloatData=null;
		}
		mReceivedDataCounts=0;
		caiyangdian=0;
		mAxCounts=0;
		if(mReceiveByteDataCRC!=null){
			mReceiveByteDataCRC=null;
		}
	}
}

class ReceivedDataHead {
	public int[] head;
	/**
	 * 轴数
	 * 单轴 1，双轴2,三轴3
	 */
	public int sensorType;
	/**
	 * 数据类型
	 *  加速度1,速度2,位移3
	 */
	public int dataType;
	/**
	 * 采样点个数
	 */
	public int dataNum;
	/**
	 * 采样点频率
	 */
	public int frequency;


	public ReceivedDataHead(String str,byte DLCMD) {
		// TODO Auto-generated constructor stub
		switch(DLCMD){
		case BluetoothConstast.CMD_Type_CaiJi:
		{
			head = new int[4];
			for(int i=0;i<8;i+=2){
				head[i/2] = Integer.valueOf(str.substring(i, i+2),16);
			}
			sensorType = Integer.valueOf(str.substring(8, 10),16);
			dataType =  Integer.valueOf(str.substring(10, 12),16);
			dataNum = Integer.valueOf(str.substring(12, 16),16);
			frequency = Integer.valueOf(str.substring(16, 20),16);
		}
		break;
		case BluetoothConstast.CMD_Type_ReadSensorParams:
		{
			head = new int[2];
			for(int i=0;i<4;i+=2){
				head[i/2] = Integer.valueOf(str.substring(i, i+2),16);
			}
			sensorType = Integer.valueOf(str.substring(6,8),16);
			dataType =  0;
			dataNum = 0;
			frequency = 0;
		}
		break;
		
		case BluetoothConstast.CMD_Type_GetTemper:
		case BluetoothConstast.CMD_Type_GetCharge:
		{
			head = new int[3];
			for(int i=0;i<6;i+=2){
				head[i/2] = Integer.valueOf(str.substring(i, i+2),16);
			}
			sensorType = 0;
			dataType =  0;
			dataNum = 0;
			frequency = 0;
		}
		break;
		}
	}
}
