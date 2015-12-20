package com.aic.aicdetactor.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.abnormal.AbnormalConst;
import com.aic.aicdetactor.abnormal.AbnormalInfo;
import com.aic.aicdetactor.activity.PartItemActivity;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.comm.CommonDef.checkUnit_Type;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.FlippingLoadingDialog;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 录入01、抄表02 用的UI 
 * @author AIC
 *
 */
public class MeasureEnterReadFragment  extends MeasureBaseFragment{

	//UI
	private TextView mDeviceNameTextView = null;
	private TextView mTextViewUnit;
	private TextView mTextViewName;
	private EditText mEditTextValue;
	private Spinner mSpinner;
	//DATA
	private OnEnterReadListener mCallback = null;
	final String TAG = "MeasureEnterReadFragment";
	private PartItemListAdapter AdapterList;
	String mAbnormalStr="";
	private int SpinnerSelectedIndex=0;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	public  MeasureEnterReadFragment(PartItemListAdapter AdapterList){
		this.AdapterList = AdapterList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		int layoutId=R.layout.enter;
		if(mType==2){
			layoutId=R.layout.meterreading;
		}
		View view = inflater.inflate(layoutId, container, false);
		mDeviceNameTextView = (TextView)view.findViewById(R.id.check_name);
		mSpinner = (Spinner)view.findViewById(R.id.temperspinner);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mAbnormalStr= mSpinner.getSelectedItem().toString();
				SpinnerSelectedIndex= arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
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
	
		case 1://录入
			mTextViewName.setText("录入:");
			mTextViewUnit.setVisibility(View.INVISIBLE);
			mSpinner.setVisibility(View.VISIBLE);
			break;
		case 2://抄表
			mTextViewName.setText("抄表:");
			mTextViewUnit.setVisibility(View.INVISIBLE);
			mSpinner.setVisibility(View.VISIBLE);
			break;	
		}
	}
	 private float mCheckedValue=0.0f;
	

    public interface OnEnterReadListener{
    	
    //	void OnClick(String IndexButton);
    	void OnClick(int genPartItemDataCounts,int xValue,int yValue,int zValue);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MLog.Logd(TAG," Temperature_fragment:onAttach()");
        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnEnterReadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEnterReadListener");
        }
    }

    void savePartItemData(PartItemListAdapter adapter){
    	  int abnormalId=0;
          String abnormalCode="";
          
    	if(adapter.getCurrentPartItemType()==checkUnit_Type.ENTERING
    			||adapter.getCurrentPartItemType()==checkUnit_Type.METER_READING){
    		String[]code =this.getResources().getStringArray(R.array.enter_abnormal_code);
    		abnormalCode= code[SpinnerSelectedIndex];
    		abnormalId= Integer.valueOf(abnormalCode)+1;
    		adapter.saveData(mEditTextValue.getText().toString(),abnormalCode,abnormalId,0,0);
    	}
    }
	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value,int measureOrSave,int CaiYangDian,int CaiyangPinLv) {
		// TODO Auto-generated method stub
		switch(measureOrSave){
		case PartItemContact.SAVE_DATA:
			if("请选择状态".equals(mAbnormalStr)
					&& (adapter.getCurrentPartItemType()==checkUnit_Type.ENTERING
	    			||adapter.getCurrentPartItemType()==checkUnit_Type.METER_READING)){
			//	CommonAlterDialog dialog = new CommonAlterDialog(this.getActivity().getApplicationContext(),"提示","请选择状态",null,null);
			//	dialog.show();
			}else{
			savePartItemData(adapter);
			}
			break;
			}
		
	}

	
	@Override
	public void addNewMediaPartItem(ParamsPartItemFragment params,PartItemListAdapter object) {
		// TODO Auto-generated method stub
		object.addNewMediaPartItem(params);
	}
}
