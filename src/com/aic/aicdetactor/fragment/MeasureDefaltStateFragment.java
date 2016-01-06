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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.abnormal.AbnormalConst;
import com.aic.aicdetactor.abnormal.AbnormalInfo;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.adapter.SpinnerAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.dialog.CommonAlterDialog;

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
	final String TAG = "MeasureDefaltStateFragment";
	private PartItemListAdapter AdapterList;
	private int [] mCheckedIndex=null;
	private String[] mOrigObseverList=null;
	final String  StrNormal ="正常";
	
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

	LinearLayout ll;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.measurement, container, false);
		mDeviceNameTextView = (TextView)view.findViewById(R.id.check_name);
		mDeviceNameTextView.setText(getPartItemName());
	//	EditText ValueEditText = (EditText)view.findViewById(R.id.editcontent);
	//	ValueEditText.setText(getPartItemData());
		
		
		 ll = (LinearLayout)view.findViewById(R.id.lined);
//		if(! AdapterList.getCurOriPartItemExtrnalInfo().contains(StrNormal)){
//			AdapterList.getCurOriPartItem().Extra_Information=AdapterList.getCurOriPartItem().Extra_Information+"/"+StrNormal;
//		}
		mOrigObseverList=AdapterList.getCurOriPartItemExtrnalInfo().split("\\/");		
		
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
		}
		AdapterList.getCurrentPartItem().setSartDate();
		return view;
	}
	
//	/**
//	 * 当上一点时 ，需要显示上次选中的数据
//	 */
//	protected void displaySelectedData(){
//mOrigObseverList=AdapterList.getCurOriPartItem().Extra_Information.split("\\/");		
//		
//		mCheckedIndex = new int[mOrigObseverList.length];
//		for (int i = 0; i < mOrigObseverList.length; i++) {
//
//			mCheckedIndex[i] = 0;
//			Log.d("luotest", mOrigObseverList[i].toLowerCase());
//			CheckBox checkbox = new CheckBox(this.getActivity()
//					.getApplicationContext());
//			checkbox.setId(i + 100);
//			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//				@Override
//				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//					// TODO Auto-generated method stub
//					int k = arg0.getId() % 100;
//					if (arg1 == true) {
//						mCheckedIndex[k] = 1;
//					}else{
//						mCheckedIndex[k] = 0;
//					}
//				}
//
//			});
//			checkbox.setBackgroundColor(Color.BLUE);
//			checkbox.setTextColor(Color.BLACK);
//			checkbox.setText(mOrigObseverList[i].toString());
//			ll.addView(checkbox);
//		}
//	}
//	
//	 @Override
//		protected void initDisplayData() {
//			// TODO Auto-generated method stub
//			super.initDisplayData();
//			displaySelectedData();
//		}

    
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
    int AbnormalId=-1;
	String AbnormalCode="";
	@Override
	public boolean canSave() {
		// TODO Auto-generated method stub
//		String value = getSaveData();
//		if(value.length()==0){
//			CommonAlterDialog dialog = new CommonAlterDialog(this.getActivity(),"提示","请选择预设状况",null,null);
//			dialog.show();
//			return false;
//		}
		return true;
	}
	
	String  getSaveData(){
		String Value="";
		for(int m=0;m<mCheckedIndex.length;m++){
			if(mCheckedIndex[m]==1){						
				Value = Value+"/"+mOrigObseverList[m].toString();
				}
			}
		
		if(Value.startsWith("/")){
			Value=Value.substring(1);
		}
		//没选任何选项时,设置信息为normal
		if(Value.equals(StrNormal)){
			Value=this.getActivity().getString(R.string.normal);
			AbnormalId=AbnormalConst.ZhengChang_Id;
			AbnormalCode=AbnormalConst.ZhengChang_Code;
		}else{
			AbnormalCode="-1";
			AbnormalId=-1;
		}
		return Value;
	}
	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value,int measureOrSave,int CaiYangDian,int CaiyangPinLv) {
		// TODO Auto-generated method stub
		
			//开始测量
//			if("".equals(Value)){
//				Value="Measurement";
//			}
			switch(measureOrSave){
			case PartItemContact.SAVE_DATA:
			{
				
				adapter.saveData(getSaveData(),AbnormalCode,AbnormalId,0,0);
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
