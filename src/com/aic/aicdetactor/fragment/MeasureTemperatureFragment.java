package com.aic.aicdetactor.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.abnormal.AbnormalInfo;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.CommonDef.checkUnit_Type;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 温度00、录入01、抄表02 、转速06 用的UI 
 * @author AIC
 *
 */
public class MeasureTemperatureFragment  extends MeasureBaseFragment  implements OnButtonListener{

	//UI
	//示意图片显示
	private ImageView mImageView = null;
	//温度测试结果
	private TextView mTemeratureResultStr = null;
	//温度测试结果对应的颜色，是否正常
	private RadioButton mRadioButton = null;
	//测量温度结果对应的文字描述
	private TextView mColorTextView = null;
	private TextView mDeviceNameTextView = null;
	private TextView mTextViewUnit;
	private TextView mTextViewName;
	private EditText mEditTextValue;
	
	//DATA
	//之间的通信接口
	private OnTemperatureMeasureListener mCallback = null;
	private List<Map<String, Object>> mMapList = null;
	final String TAG = "luotest";
	private PartItemListAdapter AdapterList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onCreate()");
//		mMapList = new ArrayList<Map<String, Object>>();
//		//初始化ListVew 数据项
//		String [] arraryStr = new String[]{this.getString(R.string.electric_device_parameters),
//				this.getString(R.string.electric_device_spectrum)};
//			for (int i = 0; i < arraryStr.length; i++) {
//				Map<String, Object> map = new HashMap<String, Object>();				
//				map.put(CommonDef.check_item_info.NAME,arraryStr[i] );								
//				map.put(CommonDef.check_item_info.DEADLINE, "2015-06-20 10:00");
//
//				//已检查项的检查数值怎么保存？并显示出来
//				//已巡检的项的个数统计，暂时由是否有巡检时间来算，如果有的话，即已巡检过了，否则为未巡检。
//				mMapList.add(map);
//			}
//			
		
		super.onCreate(savedInstanceState);
		
	}
	public  MeasureTemperatureFragment(PartItemListAdapter AdapterList){
		this.AdapterList = AdapterList;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.temperature, container, false);
		mImageView = (ImageView)view.findViewById(R.id.imageView1);
		mDeviceNameTextView = (TextView)view.findViewById(R.id.check_name);
		mRadioButton = (RadioButton)view.findViewById(R.id.radioButton2);
		mColorTextView = (TextView)view.findViewById(R.id.resultTips);
		mTemeratureResultStr =(TextView)view.findViewById(R.id.temperature);
		
		
		
		
		mTextViewName = (TextView)view.findViewById(R.id.textViewtmp);
		
		mEditTextValue = (EditText)view.findViewById(R.id.check_temp);
		mTextViewUnit = (TextView)view.findViewById(R.id.unit);
		mTextViewUnit.setText(getPartItemUnit());
		initDisplayData();
		MLog.Logd(TAG," Temperature_fragment:onCreateView() ");
		Log.d(TAG, "getName is "+mTextViewName.getText());
		AdapterList.getCurrentPartItem().setSartDate();
		return view;
	}
    
	 @Override
	protected void initDisplayData(){
		mDeviceNameTextView.setText(getPartItemName());		
		mEditTextValue.setText(getPartItemData());
		switch(mType){
		case 0://温度
			mTextViewName.setText("温度:");
			break;
		case 1://录入
			mTextViewName.setText("录入:");
			mTextViewUnit.setVisibility(View.GONE);
			break;
		case 2://抄表
			mTextViewName.setText("抄表:");
			mTextViewUnit.setVisibility(View.GONE);
			break;
		case 6://转速
			mTextViewName.setText("转速:");
			break;
		}
	}
	 private float mCheckedValue=0.0f;
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onPause()");
		super.onPause();
	}
	
	/**
	 * 测量并显示测量后的数据
	 */
    void  measureAndDisplayData(){    
    	
    	double MAX = super.mPartItemData.Up_Limit;;
    	double MID = super.mPartItemData.Middle_Limit;
    	double LOW = super.mPartItemData.Down_Limit;
    	double max_mTemperatureeration=100;
		
    	mCheckedValue = (float)(Math.random()*max_mTemperatureeration);
    	
    	if((mCheckedValue < MAX) && (mCheckedValue>=MID) ){
    		mRadioButton.setBackgroundColor(Color.YELLOW);
    		mColorTextView.setText(getString(R.string.warning));
    	}else if((mCheckedValue >= LOW) && (mCheckedValue<MID)){
    		mRadioButton.setBackgroundColor(Color.BLACK);
    		mColorTextView.setText(getString(R.string.normal));
    		mEditTextValue.setTextColor(Color.BLACK);
    	}else if(mCheckedValue <LOW){
    		mRadioButton.setBackgroundColor(Color.GRAY);
    		mColorTextView.setText(getString(R.string.invalid));
    		mEditTextValue.setTextColor(Color.GRAY);
    	}else if(mCheckedValue>=MAX){
    		mRadioButton.setBackgroundColor(Color.RED);
    		mColorTextView.setText(getString(R.string.dangerous));
    		mEditTextValue.setTextColor(Color.RED);
    	}
    	mEditTextValue.setText(String.valueOf(mCheckedValue));
    }

    
   
    Handler handler = new Handler(); 
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			measureAndDisplayData();
		}
	}; 
    
    public interface OnTemperatureMeasureListener{
    	
    	void OnClick(String IndexButton);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MLog.Logd(TAG," Temperature_fragment:onAttach()");
        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnTemperatureMeasureListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTemperatureMeasureListener");
        }
    }

    void savePartItemData(PartItemListAdapter adapter){
    	  int abnormalId=0;
          String abnormalCode="";
          int isNormal=-1;
          
    	if(adapter.getCurrentPartItemType()==checkUnit_Type.ENTERING
    			||adapter.getCurrentPartItemType()==checkUnit_Type.METER_READING){
    		
    		adapter.saveData(mEditTextValue.getText().toString(),isNormal,abnormalCode,abnormalId);
    	}else if(adapter.getCurrentPartItemType()==checkUnit_Type.TEMPERATURE
    			|| adapter.getCurrentPartItemType()==checkUnit_Type.ROTATION_RATE){
    	final double  MAX = super.mPartItemData.Up_Limit;;
    	final double  MID = super.mPartItemData.Middle_Limit;
    	final double  LOW = super.mPartItemData.Down_Limit;
      
    	
    	if((mCheckedValue < MAX) && (mCheckedValue>=MID) ){
    		isNormal=0;
    		abnormalId=3;
    		abnormalCode="02";
    	}else if((mCheckedValue >= LOW) && (mCheckedValue<MID)){
    		isNormal=1;
    		abnormalId=2;
    		abnormalCode="01";
    	}else if(mCheckedValue <LOW){
    		isNormal=0;
    		abnormalId=1;
    		abnormalCode="00";
    	}else if(mCheckedValue>=MAX){
    		isNormal=0;
    		abnormalId=4;
    		abnormalCode="03";
    	}
    	
    	adapter.saveData(String.valueOf(mCheckedValue),isNormal,abnormalCode,abnormalId);
    	}
    }
	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value,int measureOrSave) {
		// TODO Auto-generated method stub
		switch(measureOrSave){
		case PartItemContact.SAVE_DATA:
			savePartItemData(adapter);
			break;
		case PartItemContact.MEASURE_DATA:
			measureAndDisplayData();
			break;
		}
		
	}

//	@Override
//	public void saveCheckValue() {
//		// TODO Auto-generated method stub
//		Log.d("atest", "温度2222   saveCheckValue()");
//		super.setPartItemData("温度2222");
//	}
}