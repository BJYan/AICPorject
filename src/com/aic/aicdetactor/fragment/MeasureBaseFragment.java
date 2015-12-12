package com.aic.aicdetactor.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aic.aicdetactor.Interface.OnButtonListener;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.ReceivedDataAnalysis;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.PartItemJsonUp;

public abstract class MeasureBaseFragment extends Fragment implements OnButtonListener  {

	protected PartItemJsonUp mPartItemData=null;
	protected myApplication app = null;
	private String TAG="AIC.MeasureBaseFragment";
	/**
	 * 仅为显示只用
	 */
	protected int PartItemIndex=0;
	BluetoothLeControl BLEControl = null;
	final int MAX_FAILED_TIMES=3;
   boolean mCanSendCMD=false;
   boolean isRecultNormal=false;
   boolean bStartReceiveData=false;
   String Abnormalcode="-1";
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
		mType = getArguments().getInt("type");
		app =(myApplication) MeasureBaseFragment.this.getActivity().getApplication();
		mPartItemData = app.gCurPartItemList.get(getArguments().getInt("partItemIndex"));
		BLEControl = BluetoothLeControl.getInstance(MeasureBaseFragment.this.getActivity());
		if(shouldConnectBLE()){
			if(app.mCurLinkedBLEAddress!=null &&app.mCurLinkedBLEAddress.length()<2 &&!app.mBLEIsConnected ){
				Toast.makeText(this.getActivity(), "请重新连接BLE", Toast.LENGTH_LONG).show();
				}else{
					
					BLEControl.Connection(app.mCurLinkedBLEAddress);
				}
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
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
		return mPartItemData.Check_Content + "\t\t\n类型："+mPartItemData.T_Measure_Type_Code +"\n\t"+PartItemIndex+"/"+app.gCurPartItemList.size();
	}
	
	
	//测量项名称
	protected String getPartItemData(){
		if(mPartItemData.End_Check_Datetime!=null &&mPartItemData.End_Check_Datetime.length()>0){
		return mPartItemData.Extra_Information ;
		}else
		{
		return "";
		}
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
	
	/**
	 * 初始化显示的数据及其颜色，在onResume函数中会调用，之类不需要显示调用
	 */
	protected void initDisplayData(){
		
	}
	
	

	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter object,
			String Value, int measureOrSave, int CaiYangDian, int CaiyangPinLv) {
		// TODO Auto-generated method stub
		if(buttonId>0){
			
		}
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
	
	boolean shouldConnectBLE(){
		boolean connect=false;
		
		int type=Integer.valueOf(mPartItemData.T_Measure_Type_Code);
		switch(type){
		 case CommonDef.checkUnit_Type.ACCELERATION:
		   case CommonDef.checkUnit_Type.SPEED:	
		   case CommonDef.checkUnit_Type.DISPLACEMENT:
		   case CommonDef.checkUnit_Type.TEMPERATURE:
			   connect=true;
		}
		return connect;
	}
}
