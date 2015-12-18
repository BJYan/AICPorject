package com.aic.aicdetactor.fragment;
import android.R.color;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.abnormal.AbnormalConst;
import com.aic.aicdetactor.abnormal.AbnormalInfo;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.data.KEY;

/**
 * 预设项 07 对应的UI
 * @author AIC
 *
 */
public class MeasureDefaltStateFragment  extends MeasureBaseFragment{

	//测量温度结果对应的文字描述
	private TextView mDeviceNameTextView = null;
	//private myApplication app ;
	//之间的通信接口
	private OnMeasureMeasureListener mCallback = null;
	final String TAG = "luotest";
	private PartItemListAdapter AdapterList;
	private int [] mCheckedIndex=null;
	private String[] mOrigObseverList=null;
	
	
	private myApplication app=null;
	public MeasureDefaltStateFragment(PartItemListAdapter AdapterList){
		//app = ((myApplication)av. getApplication());
		this.AdapterList = AdapterList;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app=myApplication.getApplication() ;
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
		TextView ValueTextView = (TextView)view.findViewById(R.id.check_name);
		ValueTextView.setText(getPartItemData());
		
		
		LinearLayout ll = (LinearLayout)view.findViewById(R.id.lined);
		mOrigObseverList=AdapterList.getCurOriPartItem().Extra_Information.split("\\/");		
		String[] CurObseverList=AdapterList.getCurrentPartItem().Extra_Information.split("\\/"); 
		
		mCheckedIndex = new int[mOrigObseverList.length];
		for (int i = 0; i < mOrigObseverList.length; i++) {

			mCheckedIndex[i] = 0;
			Log.d("luotest", mOrigObseverList[i].toLowerCase());
			CheckBox checkbox = new CheckBox(this.getActivity()
					.getApplicationContext());
			checkbox.setId(i + 100);
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					int k = arg0.getId() % 100;
					if (arg1 == true) {
						
						mCheckedIndex[k] = 1;
					}else{
						mCheckedIndex[k] = 0;
					}
				}

			});
			checkbox.setTextColor(Color.BLACK);
			checkbox.setText(mOrigObseverList[i].toString());
			ll.addView(checkbox);
//			// 寻找已经选择的项，并设置为选中
//			for (String SelectStr : CurObseverList) {
//				if (SelectStr.equals(mOrigObseverList[i])) {
//					checkbox.setChecked(true);
//				}
//			}

		}
		AdapterList.getCurrentPartItem().setSartDate();
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
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value,int measureOrSave,int CaiYangDian,int CaiyangPinLv) {
		// TODO Auto-generated method stub
		
			//开始测量
			if("".equals(Value)){
				Value="Measurement";
			}
			switch(measureOrSave){
			case PartItemContact.MEASURE_DATA:
				break;
			case PartItemContact.SAVE_DATA:
			{
				int AbnormalId=-1;
				String AbnormalCode="";
				Value="";
				for(int m=0;m<mCheckedIndex.length;m++){
					if(mCheckedIndex[m]==1){						
						Value = Value+"/"+mOrigObseverList[m].toString();
						}
					}
				
				if(Value.startsWith("/")){
					Value=Value.substring(1);
				}
				//没选任何选项时,设置信息为normal
				if(Value==""){
					Value=this.getActivity().getString(R.string.normal);
					AbnormalId=AbnormalConst.ZhengChang_Id;
					AbnormalCode=AbnormalConst.ZhengChang_Code;
				}else{
					AbnormalCode="-1";
					AbnormalId=-1;
				}
				adapter.saveData(Value,AbnormalCode,AbnormalId,0,0);
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
