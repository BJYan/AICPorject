package com.aic.aicdetactor.fragment;

import java.io.IOException;

import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.ReceivedDataAnalysis;
import com.aic.aicdetactor.data.DeviceItemJson;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.data.PartItemJson;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.util.SystemUtil;
import com.google.gson.Gson;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class MeasureBaseFragment extends Fragment {

	//int mPartItemIndex=0;
	protected PartItemJsonUp mPartItemData=null;
	protected myApplication app = null;
	private String TAG="AIC.MeasureBaseFragment";
	//protected DeviceItemJson mDeviceItemData=null;
	BluetoothLeControl BLEControl = null;
   //StringBuffer mStrReceiveData = new StringBuffer();
   //String mStrLastReceiveData="";
	final int MAX_FAILED_TIMES=3;
   boolean mCanSendCMD=false;
   
   boolean bStartReceiveData=false;
   ReceivedDataAnalysis mAnalysis =new ReceivedDataAnalysis();
	/**
	 * 数据的类型
	 */
	protected int mType=-1;
	protected PartItemListAdapter AdapterList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"onCreate()");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//mPartItemIndex =getArguments().getInt("partItemIndex");
		mType = getArguments().getInt("type");
		app =(myApplication) MeasureBaseFragment.this.getActivity().getApplication();
		mPartItemData = app.gCurPartItemList.get(getArguments().getInt("partItemIndex"));//mDeviceItemData.PartItem.get(mPartItemIndex);
		int m=0;
		m++;
		BLEControl = BluetoothLeControl.getInstance(MeasureBaseFragment.this.getActivity());
		//BLEControl.setParamates(mhandler);
		if(app.mCurLinkedBLEAddress.length()<2){
			Toast.makeText(this.getActivity(), "请重新连接BLE", Toast.LENGTH_LONG).show();
			}else{
				BLEControl.Connection(app.mCurLinkedBLEAddress);
			}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
	//	initDisplayData();
		super.onResume();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	//测量项名称
	protected String getPartItemName(){
		return mPartItemData.Check_Content + "类型："+mPartItemData.T_Measure_Type_Code ;
		//return mPartItemData.Check_Content + "类型："+mPartItemData.T_Measure_Type_Code +"\t\t"+mPartItemIndex +"/"+app.gCurPartItemList.size();
	}
	
	
	//测量项名称
	protected String getPartItemData(){
		if(mPartItemData.End_Check_Datetime!=null &&mPartItemData.End_Check_Datetime.length()>0){
		return mPartItemData.Extra_Information ;
		}else
		{
		return "";
		}
		//return mPartItemData.Check_Content + "类型："+mPartItemData.T_Measure_Type_Code +"\t\t"+mPartItemIndex +"/"+app.gCurPartItemList.size();
	}
	
	//数据有效范围，上限
	protected double getPartItemUp_Limit(){
		return mPartItemData.Up_Limit;
	}
	
	//数据有效范围，中限
	protected double getPartItemMiddle_Limit(){
		return mPartItemData.Middle_Limit;
	}
	//数据的单位
	protected String getPartItemUnit(){
		return mPartItemData.Unit;
	}
	//数据有效范围，下限
	protected double getPartItemDown_Limit(){
		return mPartItemData.Down_Limit;
	}
	
//	//设置测量的数据结果
//	protected void setPartItemData(String value){
//		Log.d("atest"," PartItemMeasureBaseFragment: value="+value);
//		mPartItemData.Extra_Information = value;
//	}
	
//	//判断是否从上到下顺序测量完，
//	public  boolean isCheckedFinish(){
//		return(mPartItemIndex+1)== app.gCurPartItemList.size()?true:false;
//	}
	
	/**
	 * 初始化显示的数据及其颜色，在onResume函数中会调用，之类不需要显示调用
	 */
	protected void initDisplayData(){
		
	}
	
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	/**
	 * 显示上次已选中的选项并显示
	 */
	protected void displaySelectedData(){
		
	}
	//public abstract   void saveCheckValue();

	protected Handler getHandler(){
		return mhandler;
	}
	protected Handler mhandler=null;
}
