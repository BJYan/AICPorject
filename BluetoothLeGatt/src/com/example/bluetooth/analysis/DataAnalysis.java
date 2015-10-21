package com.example.bluetooth.analysis;

import android.util.Log;

public class DataAnalysis {
	private static final String TAG = "DataAnalysis";

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
	int[] stringdata;
	int[] waveformTypeA;
	int[] waveformTypeB;
	int temperature;
	int electric;
	int checkCode;
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
	
	//获取得实际加速度数值
	float getVoValue(String StrHex){
		float value =0f;
		/*if(StrHex.compareTo(StrZearo)>=0 && StrHex.compareTo(Str7f)<=0){
			value=Integer.valueOf(StrHex,16)+mVmv;
		}
		
		
		if(StrHex.compareTo(Str80)>=0 && StrHex.compareTo(Str4f)<=0){
			value=Integer.valueOf(StrHex,16)-mVmv;
		}*/
		value = (float)Integer.valueOf(StrHex,16);
		if(value>50000) value = value-65536;
		value= (value/65536)*5000;
		
		value = value*macceleratedSpeed/mstandardMv;
		
		return value;
	}
	
	public void getResult(String str) {
		// TODO Auto-generated constructor stub
		//this.str = str;
		//String str = str1 +str2;
		Log.i(TAG, "str.length() = "+str.length());
		Log.i(TAG, "analysis str = "+str.toString());
		dataHead = new DataHead(str.substring(0, 20));
		waveformTypeA = new int[3];
		waveformTypeB = new int[3];
		
		data = new float[dataHead.dataNum];
		stringdata = new int[dataHead.dataNum];
		int j=0;
		for(int i=0;i<dataHead.dataNum*4;i+=4){
		
			//data[j] = ((float)Integer.valueOf(str.substring(i+20, i+24),16)/65536)*5000;
			data[j] = getVoValue(str.substring(i+20, i+24));
			//stringdata[j] = Integer.valueOf(str.substring(i+20, i+24),16);
			j=j+1;
			//Log.i(TAG, "data["+j+"] = "+data[j] + ", 0X ="+str.substring(i+20, i+24));
			
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
	}
	
	public float[] getData() {
		return data;
	}	
	public int getDataNum(){
		return dataHead.dataNum;
	}
}
