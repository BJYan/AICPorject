package com.aic.aicdetactor.bluetooth.analysis;

import com.aic.aicdetactor.util.SystemUtil;

public class CaiYangValues {

	int mValueType=0;
	float ValidValue=0;
	float FengFengValue=0;
	String mStrIng="";
	public CaiYangValues(String StrIn){
		mStrIng=StrIn;
	}
	public int getValueType(){
		if(isValid()){
			mValueType=Integer.valueOf(mStrIng.substring(0, 2),16);
		}
		return mValueType;
	}
	public float getValidValue(){
		if(isValid()){
			ValidValue=SystemUtil.ByteArrayToFloat(mStrIng.substring(2, 10).getBytes());
		}
		return ValidValue;
	}
	
	public float getFengFengValue(){
		if(isValid()){
			FengFengValue=SystemUtil.ByteArrayToFloat(mStrIng.substring(10, 18).getBytes());
		}
		return FengFengValue;
	}
	
	boolean isValid(){
		if(mStrIng.trim().length()!=9*2){
			return true;
		}
		return false;
	}
}
