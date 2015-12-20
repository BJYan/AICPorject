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
	/**
	 * 数据包数
	 */
	int mReceivedPackageCounts=0;
	int caiyangdian=0;
	int mAxCounts=0;
	byte mDLCmd=0;
	final String TAG="ReceivedDataAnalysis";
	/**
	 * 已经接收到的数据长度
	 */
	int mReceivedDataByteSizes=0;
	/**
	 * 应该获取的数据长度
	 */
	int mShoudReceivedByteSizes=0;
	/**
	  * 获取wave数据
	  */
	public float[] getWaveFloatData(){
		 int JumpByteN=10;
		 short plb;
		 switch(mDLCmd){
		 case BluetoothConstast.CMD_Type_ReadSensorParams:
			 break;
		 case BluetoothConstast.CMD_Type_CaiJi:
			 for(int i=0;i<mReceiveWaveFloatData.length;i++){
				  plb=mReceiveByteDataCRC[i*2+JumpByteN];
				  plb=(short)(int)((plb<<8)&0xff00);
				 
				 plb+=mReceiveByteDataCRC[i*2+JumpByteN+1]&0xff;
				 float a=plb;
				 if(true)Log.d(TAG, "getWaveFloatData() 1 a = "+a +","+mReceiveByteDataCRC[i*2+JumpByteN]+","+mReceiveByteDataCRC[i*2+JumpByteN+1]);
				 mReceiveWaveFloatData[i]=getVoValue(a);
			 }
			 break;
		 }
		 	return mReceiveWaveFloatData; 
	 }
	
	float getVoValue(float v){
		float value =0f;
		int mstandardMv=30;
		int macceleratedSpeed=10;
		value=v;
		if(true)Log.d(TAG, "getVoValue() 1 value = "+value);
		value/=65536/5000;
		value = value*macceleratedSpeed/mstandardMv;

		if(true)Log.d(TAG, "getVoValue()value = "+value);
		return value;
	}
	 /**
	  * 
	  */
	public  String getWaveByteData(){
		 switch(mDLCmd){
		 case BluetoothConstast.CMD_Type_ReadSensorParams:
			 break;
		 case BluetoothConstast.CMD_Type_CaiJi:
			 System.arraycopy(mReceiveByteDataCRC, 10, mReceiveWaveByteData, 0, getDataPointCount()*2);
			 break;
		 }
		 String str = SystemUtil.bytesToHexString(mReceiveByteDataCRC);
		 	return str; 
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
		 * 有效数值或温度
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
			 case BluetoothConstast.CMD_Type_GetTemper:
				 dValue=getTemperature();
				 break;
			 case BluetoothConstast.CMD_Type_CaiJiZhuanSu:
				 dValue=getZhuanSu();
				 break;
			 }
			 
			
			 
			if(true)Log.d(TAG, "getValidValue()dValue = "+dValue);
			return (float) dValue;
		}
		
		
		float getZhuanSu(){
			float zhuansu=0;
			if(mReceiveByteDataCRC[2]==(byte)0xd3){
				byte[]tempByte=new byte[4];
				System.arraycopy(mReceiveByteDataCRC, 3, tempByte, 0, tempByte.length);
				zhuansu=SystemUtil.ByteArrayToFloat(tempByte);
			}
			return zhuansu;
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
						String strGetCRC32 =Long.toHexString(crc.getValue());
						
						byte[]crcValue = new byte[4];
						for(int i=0;i<4;i++){
							crcValue[i]=mReceiveByteDataCRC[mReceiveByteDataCRC.length-4+i];
						}
						String sa=SystemUtil.bytesToHexString(crcValue);
						if(sa.equals(strGetCRC32)){
							returnValue=true;
						}
						Log.d(TAG," isValidate() strGetCRC32 is "+strGetCRC32 +"and calc CRC32 is "+sa);
					}else{
						Log.e(TAG," isValidate() head data error");
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
	
	 public int getPackagesCounts(){
		 int counts=0;
		 switch(mDLCmd){
		 case BluetoothConstast.CMD_Type_CaiJi:
			{
				
				counts=(int) ((caiyangdian*2+10+30)/2 +0.5);
			}
			break;
			case BluetoothConstast.CMD_Type_ReadSensorParams:
			
			case BluetoothConstast.CMD_Type_GetTemper:
			
			case BluetoothConstast.CMD_Type_GetCharge:
				counts=1;
			break;
			}
		 return counts;
	 }
	public boolean isReceivedAllData(){
		Log.d(TAG,"isReceivedAllData() = "+ (mReceivedDataByteSizes==mShoudReceivedByteSizes?1:0)+",,"+mReceivedDataByteSizes +",mShoudReceivedByteSizes="+mShoudReceivedByteSizes);
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
			Log.d(TAG, "first package data is "+SystemUtil.bytesToHexString(strbyte));
			return cmd;
		}
		/**
		 * 接受获取到的BLE数据
		 * @param strbyte
		 * @param bStartReceiveData
		 */
	public	void getDataFromBLE(byte []strbyte,boolean bStartReceiveData){
	    	if(!bStartReceiveData){
	    		//获取参数及数据采样点 采样频率 及其数据长度信息
	    		mDLCmd=getCMDTypeFromFirstReceivedPacageData(strbyte);
	    		if(mDLCmd==0){mDLCmd=(byte) 0xd1;}
				switch(mDLCmd)
				{
				case (byte)0xd2:
				case (byte)0xd3:
				{
					CRC32 crc = new CRC32();
					crc.update(strbyte, 0, 16);
					String s =Long.toHexString(crc.getValue());
					byte[]crcValue = new byte[4];
					for(int i=0;i<4;i++){
						crcValue[i]=strbyte[strbyte.length-4+i];
					}
					String sa=SystemUtil.bytesToHexString(crcValue);
					if(sa.equals(s)){
					}
					if(mReceiveByteDataCRC!=null){
						mReceiveByteDataCRC=null;
					}
					mShoudReceivedByteSizes=strbyte.length;
					mReceiveByteDataCRC = new byte[mShoudReceivedByteSizes];
				}
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
				case (byte)0xd6:
				{
					CRC32 crc = new CRC32();
					crc.update(strbyte, 0, 16);
					String s =Long.toHexString(crc.getValue());
					byte[]crcValue = new byte[4];
					for(int i=0;i<4;i++){
						crcValue[i]=strbyte[strbyte.length-4+i];
					}
					String sa=SystemUtil.bytesToHexString(crcValue);
					if(sa.equals(s)){
					}
					if(mReceiveByteDataCRC!=null){
						mReceiveByteDataCRC=null;
					}
					mShoudReceivedByteSizes=strbyte.length;
					mReceiveByteDataCRC = new byte[mShoudReceivedByteSizes];
				}
					break;
				}
				
			}
	    	
	    	int len=strbyte.length;
	    	//统计已经接受到的数据长度
	    	
	    	
			//收集BLE发送过来的所有原始数据
	    	Log.d(TAG, "mDLCmd = "+mDLCmd + "strbyte.length = "+strbyte.length);
	    	Log.d(TAG, "mReceivedDataByteSizes = "+mReceivedDataByteSizes + ",mShoudReceivedByteSizes = "+mShoudReceivedByteSizes);
	    	try{
	    	if( mReceivedDataByteSizes>=mShoudReceivedByteSizes){
	    		len=mShoudReceivedByteSizes-(mReceivedPackageCounts-1)*20;
	    		if(len<=0){len =0;}
	    		System.arraycopy(strbyte,0,mReceiveByteDataCRC,(mReceivedPackageCounts-1)*20,len);	
	    	}else{
	    		System.arraycopy(strbyte,0,mReceiveByteDataCRC,mReceivedPackageCounts*20,len);	
	    	}
	    	mReceivedDataByteSizes=mReceivedDataByteSizes+len;
			mReceivedPackageCounts++;
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	
	/**
	 * 重置数据
	 */
	public void reset(){
		if(mReceiveWaveFloatData!=null){
			mReceiveWaveFloatData=null;
		}
		mReceivedDataByteSizes=0;
		mReceivedPackageCounts=0;
		caiyangdian=0;
		mAxCounts=0;
		if(mReceiveByteDataCRC!=null){
			mReceiveByteDataCRC=null;
		}
	}
	
	/**
	 * 获取温度数值
	 */
    float getTemperature(){
		float tem=0;
		if(mReceiveByteDataCRC[2]==(byte)0xd6){
			byte[]tempByte=new byte[8];
			System.arraycopy(mReceiveByteDataCRC, 3, tempByte, 0, tempByte.length);
			tem=SystemUtil.ByteArrayToFloat(tempByte);
		}
		return tem;
	}

}




