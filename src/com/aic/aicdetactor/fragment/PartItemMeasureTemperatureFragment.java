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
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;


public class PartItemMeasureTemperatureFragment  extends PartItemMeasureBaseFragment  implements OnButtonListener{

	//示意图片显示
	private ImageView mImageView = null;
	//温度测试结果
	private TextView mTemeratureResultStr = null;
	//温度测试结果对应的颜色，是否正常
	private RadioButton mRadioButton = null;
	//测量温度结果对应的文字描述
	private TextView mColorTextView = null;
	private TextView mDeviceNameTextView = null;
	//之间的通信接口
	private OnTemperatureMeasureListener mCallback = null;
	private List<Map<String, Object>> mMapList = null;
//	String parStr = null;
	final String TAG = "luotest";
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
		int nindex = super.mPartItemIndex;
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onDestroyOptionsMenu() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onDestroyOptionsMenu()");
		super.onDestroyOptionsMenu();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onDestroyView()");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onDetach()");
		super.onDetach();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onResume()");
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		MLog.Logd(TAG," Temperature_fragment:onSaveInstanceState()");
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onStop()");
		super.onStop();
	}
private TextView mTextViewUnit;
private TextView mTextViewName;
private EditText mEditTextValue;
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
		mDeviceNameTextView.setText(getPartItemName());
		
		mTextViewName = (TextView)view.findViewById(R.id.textViewtmp);
		
		mEditTextValue = (EditText)view.findViewById(R.id.check_temp);
		mTextViewUnit = (TextView)view.findViewById(R.id.unit);
		mTextViewUnit.setText(getPartItemUnit());
		switch(mType){
		case 0://温度
			mTextViewName.setText("温度:");
			mTextViewUnit.setText("C");
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
			//mTextViewUnit.setText("转/秒");
			break;
		}
		
		//parseExternalInfo();
		MLog.Logd(TAG," Temperature_fragment:onCreateView() ");
		Log.d(TAG, "getName is "+mTextViewName.getText());
		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		MLog.Logd(TAG," Temperature_fragment:onPause()");
		super.onPause();
	}
	  //临时生成随机的温度,转速数据。
    void genRandomValue(){    
    	double max_temperation=100;
    	double MAX = 200;
    	double MID = 100;
    	double LOW = 0;
		
    	MAX = super.mPartItemData.Up_Limit;
    	MID = super.mPartItemData.Middle_Limit;
    	LOW = super.mPartItemData.Down_Limit;

    	float temp = (int) (Math.random()*max_temperation);
    	if((temp < MAX) && (temp>=MID) ){
    		mRadioButton.setBackgroundColor(Color.YELLOW);
    		mColorTextView.setText(getString(R.string.warning));
    		mPartItemData.Is_Normal=0;
    		mPartItemData.T_Item_Abnormal_Grade_Id=3;
    		mPartItemData.T_Item_Abnormal_Grade_Code="02";
    	}else if((temp >= LOW) && (temp<MID)){
    		mRadioButton.setBackgroundColor(Color.BLACK);
    		mColorTextView.setText(getString(R.string.normal));
    		mEditTextValue.setTextColor(Color.BLACK);
    		mPartItemData.Is_Normal=1;
    		mPartItemData.T_Item_Abnormal_Grade_Id=2;
    		mPartItemData.T_Item_Abnormal_Grade_Code="01";
    	}else if(temp <LOW){
    		mRadioButton.setBackgroundColor(Color.GRAY);
    		mColorTextView.setText(getString(R.string.invalid));
    		mEditTextValue.setTextColor(Color.GRAY);
    		mPartItemData.Is_Normal=0;
    		mPartItemData.T_Item_Abnormal_Grade_Id=1;
    		mPartItemData.T_Item_Abnormal_Grade_Code="00";
    	}else if(temp>=MAX){
    		mRadioButton.setBackgroundColor(Color.RED);
    		mColorTextView.setText(getString(R.string.dangerous));
    		mEditTextValue.setTextColor(Color.RED);
    		mPartItemData.Is_Normal=0;
    		mPartItemData.T_Item_Abnormal_Grade_Id=4;
    		mPartItemData.T_Item_Abnormal_Grade_Code="03";
    	}
    	mEditTextValue.setText(String.valueOf(temp));
    	mCallback.OnClick(String.valueOf(temp ));
    }

    @Override
	public void onStart() {
		// TODO Auto-generated method stub
    	MLog.Logd(TAG," Temperature_fragment:onStart()");
		super.onStart();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		super.onViewCreated(view, savedInstanceState);
		MLog.Logd(TAG," Temperature_fragment:onViewCreated()");
		
		//handler.postDelayed(runnable, 500);
	
	}
    Handler handler = new Handler(); 
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			genRandomValue();
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

	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value) {
		// TODO Auto-generated method stub
		genRandomValue();
		if("".equals(Value)){
			Value="temperator";
		}
		adapter.saveData(Value);
	}

	@Override
	public void saveCheckValue() {
		// TODO Auto-generated method stub
		Log.d("atest", "温度2222   saveCheckValue()");
		super.setPartItemData("温度2222");
	}
}
