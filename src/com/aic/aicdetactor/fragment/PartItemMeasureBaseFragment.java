package com.aic.aicdetactor.fragment;

import java.io.IOException;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.data.DeviceItemJson;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.data.PartItemJson;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.util.SystemUtil;
import com.google.gson.Gson;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class PartItemMeasureBaseFragment extends Fragment {

	protected int mPartItemIndex=0;
	protected PartItemJsonUp mPartItemData=null;
	private myApplication app = null;
	private String TAG="AIC.MeasureBaseFragment";
	//protected DeviceItemJson mDeviceItemData=null;
	/**
	 * 数据的类型
	 */
	protected int mType=-1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"onCreate()");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPartItemIndex =getArguments().getInt("partItemIndex");
		mType = getArguments().getInt("type");
		app =(myApplication) PartItemMeasureBaseFragment.this.getActivity().getApplication();
		//mDeviceItemData =app.gCurDeviceItemCahce;
		mPartItemData = app.gCurPartItemList.get(mPartItemIndex);//mDeviceItemData.PartItem.get(mPartItemIndex);
		int m=0;
		m++;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	//测量项名称
	protected String getPartItemName(){
		return mPartItemData.Check_Content + "类型："+mPartItemData.T_Measure_Type_Code;
	}
	
	//数据有效范围，上限
	protected float getPartItemUp_Limit(){
		return mPartItemData.Up_Limit;
	}
	
	//数据有效范围，中限
	protected float getPartItemMiddle_Limit(){
		return mPartItemData.Middle_Limit;
	}
	//数据的单位
	protected String getPartItemUnit(){
		return mPartItemData.Unit;
	}
	//数据有效范围，下限
	protected float getPartItemDown_Limit(){
		return mPartItemData.Down_Limit;
	}
	
	//设置测量的数据结果
	protected void setPartItemData(String value){
		Log.d("atest"," PartItemMeasureBaseFragment: value="+value);
		mPartItemData.Extra_Information = value;
	}
	
	//判断是否从上到下顺序测量完，
	public  boolean isCheckedFinish(){
		return(mPartItemIndex+1)== app.gCurPartItemList.size()?true:false;
	}
	
	
	public abstract   void saveCheckValue();

}
