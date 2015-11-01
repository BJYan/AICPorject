package com.aic.aicdetactor.fragment;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.data.KEY;


public class PartItemMeasureMeasurementFragment  extends PartItemMeasureBaseFragment  implements OnButtonListener{

	//测量温度结果对应的文字描述
	private TextView mDeviceNameTextView = null;
	//之间的通信接口
	private OnMeasureMeasureListener mCallback = null;
	final String TAG = "luotest";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//parStr =getArguments().getString(KEY.KEY_PARTITEMDATA);
			
		
		super.onCreate(savedInstanceState);
		int i = super.mPartItemIndex;
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
		View view = inflater.inflate(R.layout.measurement, container, false);
		mDeviceNameTextView = (TextView)view.findViewById(R.id.check_name);
		mDeviceNameTextView.setText(getPartItemName());
		return view;
	}

    
    public interface OnMeasureMeasureListener{
    	
    	void OnClick(String IndexButton);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnMeasureMeasureListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMeasureMeasureListener");
        }
    }

	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value) {
		// TODO Auto-generated method stub
		//开始测量
	//	super.setPartItemData("Measurement");
		if("".equals(Value)){
			Value="Measurement";
		}
		adapter.saveData(Value);
	}



	@Override
	public void saveCheckValue() {
		Log.d("atest", "Measurement   saveCheckValue()");
		// TODO Auto-generated method stub
		super.setPartItemData("Measurement");
	}

}
