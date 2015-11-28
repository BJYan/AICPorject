package com.aic.aicdetactor.bluetooth.analysis;

import java.math.BigDecimal;

import com.aic.aicdetactor.bluetooth.BluetoothConstast;

import android.util.Log;

public class DataAnalysis {
	private static final String TAG = "DataAnalysis";
    private boolean bPrintTestValue =true;
	/*String str = "7d7d7d7d0101040009e5800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "08000800080008000800080008000800080008000800080008000800080008000800080008000800080008"
			+ "00080008000800080008000800080008000800080008000800080008000800080008000800080008000800"
			+ "0800080008000800080008000800080008000800080008000800080008000800080008000800000008000f"
			+ "fff000000000000000000000000000034a60000070623f632c2";*/

	DataHead dataHead;
	float[] data;
	//int[] stringdata;
	int[] waveformTypeA;
	int[] waveformTypeB;
	int temperature;
	int electric;
	int checkCode;
	 byte cmdByte=0;
	String StrValue="";
	final static int MinLenth =10;
	public DataAnalysis() {
		// TODO Auto-generated constructor stub
	}
	
	//电压换算为加速度的算法常亮
	int mstandardMv=30;
	int macceleratedSpeed=10;
	int mVmv=2500;
	String StrZearo="0000";
	String Str7f="7fff";
	String Str80="8000";
	String Str4f="ffff";
	
	float FabsMaxValue;
	float FengFengValue;
	
	//获取得实际加速度数值
	float getVoValue(String StrHex){
		float value =0f;
		short plb=(short)(int)Integer.valueOf(StrHex,16);
		value=plb;
		value/=65536/5000;
		value = value*macceleratedSpeed/mstandardMv;

		if(bPrintTestValue)Log.d(TAG, "getVoValue()value = "+value);
		return value;
	}
	
	public void getResult() {
		// TODO Auto-generated constructor stub
		String  str =StrValue;
		byte cmdtype = cmdByte;
		Log.i(TAG, "str.length() = "+str.length());
		Log.i(TAG, "analysis str = "+str.toString());
		dataHead = new DataHead(str.substring(0, 20),cmdtype);
		
		waveformTypeA = new int[3];
		waveformTypeB = new int[3];
		data = new float[dataHead.dataNum];
		if(cmdByte != BluetoothConstast.CMD_Type_CaiJi){return;}
		int j=0;
		for(int i=0;i<dataHead.dataNum*4;i+=4){
		
			//data[j] = ((float)Integer.valueOf(str.substring(i+20, i+24),16)/65536)*5000;
			data[j] = getVoValue(str.substring(i+20, i+24));
			j=j+1;
		}
		
		waveformTypeA[0] = Integer.valueOf(str.substring(str.length()-60, str.length()-58),16);
		Log.i(TAG, "waveformTypeA[0] = "+waveformTypeA[0] + ", 0X ="+str.substring(str.length()-60, str.length()-58));
		waveformTypeA[1] = Integer.valueOf(str.substring(str.length()-58, str.length()-50),16);
		Log.i(TAG, "waveformTypeA[1] = "+waveformTypeA[1] + ", 0X ="+str.substring(str.length()-58, str.length()-50));
		//waveformTypeA[2] = Integer.valueOf(str.substring(str.length()-50, str.length()-42),16);
		Log.i(TAG, "waveformTypeA[2] = "+waveformTypeA[2] + ", 0X ="+str.substring(str.length()-50, str.length()-42));
		
		waveformTypeB[0] = Integer.valueOf(str.substring(str.length()-42, str.length()-40),16);
		Log.i(TAG, "waveformTypeB[0] = "+waveformTypeB[0] + ", 0X ="+str.substring(str.length()-42, str.length()-40));
		waveformTypeB[1] = Integer.valueOf(str.substring(str.length()-40, str.length()-32),16);
		Log.i(TAG, "waveformTypeB[1] = "+waveformTypeB[1] + ", 0X ="+str.substring(str.length()-40, str.length()-32));
		waveformTypeB[2] = Integer.valueOf(str.substring(str.length()-32, str.length()-24),16);
		Log.i(TAG, "waveformTypeB[2] = "+waveformTypeB[2] + ", 0X ="+str.substring(str.length()-32, str.length()-24));
		
		temperature = Integer.valueOf(str.substring(str.length()-24, str.length()-16),16);
		Log.i(TAG, "temperature = "+temperature + ", 0X ="+str.substring(str.length()-24, str.length()-16));
		electric = Integer.valueOf(str.substring(str.length()-16, str.length()-8),16);
		Log.i(TAG, "electric = "+electric + ", 0X ="+str.substring(str.length()-16, str.length()-8));
		//checkCode  = Integer.valueOf(str.substring(str.length()-8, str.length()),16);
		Log.i(TAG, "checkCode = "+checkCode + ", 0X ="+str.substring(str.length()-8, str.length()));
		
		FabsMaxValue = computeFabsMaxValue();
		FengFengValue = computeFengFengValue();
	}
	
	public float[] getData() {
		if(bPrintTestValue)Log.d(TAG, "getData()data = "+data);
		return data;
	}	
	public int getDataNum(){
		if(bPrintTestValue)Log.d(TAG, "getDataNum()dataHead.dataNum = "+dataHead.dataNum);
		return dataHead.dataNum;
	}
	
	/**
	 * 有效数值
	 * @return
	 */
	public float getValidValue(){
		double dValue=0;
		for(int i=0;i<data.length;i++){
			dValue+=Math.pow(data[i], 2);
		}
		dValue=Math.sqrt(dValue/data.length);
		BigDecimal   b  =   new BigDecimal(dValue);  
		dValue  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		 
		if(bPrintTestValue)Log.d(TAG, "getValidValue()dValue = "+dValue);
		return (float) dValue;
	}
	/**
	 * 峰值
	 * @return
	 */
	public float computeFabsMaxValue(){
		float dValue=0;
		for(int i=0;i<data.length;i++){
			if(dValue<Math.abs(data[i])){
				dValue=Math.abs(data[i]);
			}
		}
		BigDecimal   b  =   new BigDecimal(dValue);  
		dValue  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		 
		if(bPrintTestValue)Log.d(TAG, "getFabsMaxValue()dValue = "+dValue);
		return dValue;
	}
	public float getFabsMaxValue(){
		return FabsMaxValue;
	}
	/**
	 * 获取峰峰数值
	 * @return
	 */
	public float computeFengFengValue(){
		float dValue=0;
		float nValue=0;
		for(int i=0;i<data.length;i++){
			if(dValue<data[i]){
				dValue=data[i];
			}
			
			if(nValue>=data[i]){
				nValue=data[i];
			}
		}
		BigDecimal   b  =   new BigDecimal(dValue-nValue);  
		dValue  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		 
		if(bPrintTestValue)Log.d(TAG, "getFengFengValue()dValue = "+dValue);
		return dValue;
	}
	
	public float getFengFengValue(){
		return FabsMaxValue;	
	}
	
	public int  isValidate(String str,byte cmdtype){
		int returnValue =-1;
		StrValue = str;
		cmdByte=cmdtype;
		if(StrValue!=null && StrValue.length()>MinLenth){
			switch(cmdByte){
			case BluetoothConstast.CMD_Type_CaiJi:
			{
				if(StrValue.substring(0, 8).toLowerCase().equals("7d7d7d7d")){
					//CRC32校验
//					byte[]data2= new byte[mStrValue.length-4];
//					System.arraycopy(mStrValue,mStrValue.length-4,data2,0,mStrValue.length-4);
//					byte[]bytecrc32=SystemUtil.getCRC32(data2).getBytes();
//					if(bytecrc32.length>=4){
//						for(int i=0;i<bytecrc32.length;i++){
//							if(bytecrc32[i]!=mStrValue[mStrValue.length-4-i]){
//								return -2;
//							}
//						}
//					
//				}else{
//					return 1;
//				}
					if(StrValue.length() == (20+	getDataPointCount()*4 +(9+9+4*3)*2))
					{
						returnValue=0;
					}else{
						returnValue= StrValue.length();
					}
			}
			break;
			}
			case BluetoothConstast.CMD_Type_ReadSensorParams:
			{
				if(StrValue.substring(0, 6).toLowerCase().equals("7d14d2")){
					returnValue= 0;
				}else{
					returnValue= 1;
				}
			}
				break;
			case BluetoothConstast.CMD_Type_GetTemper:
			{
				if(StrValue.substring(0, 6).toLowerCase().equals("7d14d6")){
					returnValue= 0;
				}else{
					returnValue= 1;
				}
			}
			break;
			case BluetoothConstast.CMD_Type_GetCharge:
			{
				if(StrValue.substring(0, 6).toLowerCase().equals("7d14d7")){
					returnValue= 0;
				}else{
					returnValue= 1;
				}
			}
			break;
			case BluetoothConstast.CMD_Type_CaiJiZhuanSu:
			{
				if(StrValue.substring(0, 6).toLowerCase().equals("7d14d3")){
					returnValue= 0;
				}else{
					returnValue= 1;
				}
			}
			break;
			}
			
		}
		
		if(bPrintTestValue)Log.d(TAG, "isValidate()returnValue = "+returnValue);
		return returnValue ;
	}
	/**
	 * 获取电量数值，根据下发的D7命令
	 * @return
	 */
	public float getChargeValue(){
		float chargeValue =0.0f;
		if(BluetoothConstast.CMD_Type_GetCharge==cmdByte){
			chargeValue=Integer.valueOf(StrValue.substring(7, 15),16);
		}
		BigDecimal   b  =   new BigDecimal(chargeValue);  
		chargeValue  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		
		if(bPrintTestValue)Log.d(TAG, "getChargeValue()chargeValue = "+chargeValue);
		return chargeValue;
	}
	
	/**
	 * 获取温度数值，根据下发的D6命令
	 * @return
	 */
	public float getTemperValue(){
		float tempValue =0.0f;
		if(BluetoothConstast.CMD_Type_GetTemper==cmdByte){
			tempValue=Integer.valueOf(StrValue.substring(7, 15),16);
		}
		BigDecimal   b  =   new BigDecimal(tempValue);  
		tempValue  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		if(bPrintTestValue)Log.d(TAG, "getTemperValue()chargeValue = "+tempValue);
		return tempValue;
	}
	
	/**
	 * 获取转速数值，根据下发的D3命令
	 * @return
	 */
	public float getZhuanSuValue(){
		float ZhuansuValue =0.0f;
		if(BluetoothConstast.CMD_Type_CaiJiZhuanSu==cmdByte){
			ZhuansuValue=Integer.valueOf(StrValue.substring(7, 15),16);
		}
		if(bPrintTestValue)Log.d(TAG, "getZhuanSuValue()ZhuansuValue = "+ZhuansuValue);
		return ZhuansuValue;
	}
	
	/**
	 * 获取轴数
	 * @return
	 */
	public int getAXCount(){
		int b=0;
		if(StrValue.length()<MinLenth){return 0;}
		if(BluetoothConstast.CMD_Type_CaiJi==cmdByte){	
			b= Integer.valueOf(StrValue.substring(8, 10));
			}else if(BluetoothConstast.CMD_Type_ReadSensorParams==cmdByte){
				String a = StrValue.substring(6, 8);
				b= Integer.valueOf(a,16);
			}
		if(bPrintTestValue)Log.d(TAG, "getAXCount()b = "+b);
		return b;
	}
	/**
	 * 获取采样点数
	 * @return
	 */
	public int getDataPointCount(){
		int b=0;
		if(StrValue.length()<MinLenth){return 0;}
		if(BluetoothConstast.CMD_Type_CaiJi==cmdByte){	
			String a= StrValue.substring(12, 16);
			
			b= Integer.valueOf(a,16);
			}
		if(bPrintTestValue)Log.d(TAG, "getDataPointCount()b = "+b);
		return b;
	}
	
	/**
	 * 获取采样频率
	 * @return
	 */
	public int getCaiYangFrequency(){
		int frequency=0;
		if(StrValue.length()<MinLenth){return 0;}
		if(BluetoothConstast.CMD_Type_CaiJi==cmdByte){
			frequency= Integer.valueOf(StrValue.substring(16, 20),16);
			}
		
		if(bPrintTestValue)Log.d(TAG, "getCaiYangFrequency()frequency = "+frequency);
		return frequency;
	}
	
	/**
	 * 获取波形数据
	 * @return
	 */
	public String getWaveData(){
		if(StrValue.length()<MinLenth){return null;}
		if(BluetoothConstast.CMD_Type_CaiJi==cmdByte){	
		int count = getDataPointCount();
		String waveDataB = StrValue.substring(20,count*4+20);
		if(bPrintTestValue)Log.d(TAG, "getWaveData()waveDataB = "+waveDataB);
		return waveDataB;
		}
		return null;
	}
	
	/**
     * 获取数据的校验码，以String 类型的数据返回方便比较
     * @return
     */
    public String getCRC32(){
    	if(StrValue.length()<MinLenth){return null;}
		String StrCRC32=StrValue.substring(StrValue.length()-4*2,StrValue.length());
		if(bPrintTestValue)Log.d(TAG, "getCRC32()StrCRC32 = "+StrCRC32);
		return StrCRC32;
    }
    
    public static long getReceiveDataLenth(String strFirstReceive,byte dlCmd){
    	long lent=0;
    	switch(dlCmd){
    	case BluetoothConstast.CMD_Type_CaiJi:
    		lent = Integer.valueOf(strFirstReceive.substring(12, 16),16)*4+20+(9+9+3*4)*2;
    		break;
    	case BluetoothConstast.CMD_Type_ReadSensorParams:
    	case BluetoothConstast.CMD_Type_CaiJiZhuanSu:
    	case BluetoothConstast.CMD_Type_SetSensorParams:
    	case BluetoothConstast.CMD_Type_GetTemper:
    	case BluetoothConstast.CMD_Type_GetCharge:
    		lent=40;
    		break;
    	}
    	
    	return lent;
    }
}
