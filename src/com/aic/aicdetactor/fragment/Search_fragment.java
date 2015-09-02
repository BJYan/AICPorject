package com.aic.aicdetactor.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.comm.CommonDef;


public class Search_fragment extends Fragment  implements OnButtonListener,OnClickListener,OnCheckedChangeListener{


	private Button mButtonFliter = null;
	private Button mButtonFliterByTime = null;
	private int mCheck_Type_Index = -1;
	private int mDevice_Type_Index = -1;
	private int mInfo_Type_Index = -1;
	private int mException_Type_Index = -1;
	private int mTime_Type_Index = -1;
	private final String TAG = "luotest";
	private  PopupWindow pw_Left  = null;
	private  PopupWindow pw_Right  = null;
	private RadioGroup checkType = null;
	private RadioGroup deviceType = null;
	private RadioGroup infoType  = null;
	private RadioGroup exceptionType = null;	
	private RadioGroup timeType = null;
	private ListView mListView = null;
	//之间的通信接口
	private OnMeasureListener mCallback = null;
	private List<Map<String, Object>> mMapList = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
			
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		mButtonFliter = (Button)view.findViewById(R.id.search_fliter);
		mButtonFliter.setOnClickListener(this);
		mButtonFliterByTime = (Button)view.findViewById(R.id.search_fliterbytime);
		mButtonFliterByTime.setOnClickListener(this);
		mListView = (ListView)view.findViewById(R.id.search_list);
		return view;
	}

	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	

    @Override
	public void onStart() {
		// TODO Auto-generated method stub
    	
		super.onStart();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		super.onViewCreated(view, savedInstanceState);
		
		
		//handler.postDelayed(runnable, 500);
	
	}
  
    
    public interface OnMeasureListener{
    	
    	void OnClick(String IndexButton);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
//        try {
//            mCallback = (OnMeasureListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnMeasureListener");
//        }
    }

	@Override
	public void OnButtonDown(int buttonId, Bundle bundle) {
		// TODO Auto-generated method stub
		
	}

	void initPopupWindowFliter(View parent) {
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View rootview = inflater.inflate(
				R.layout.search_popupwidon_fliter, null, false);

		pw_Left = new PopupWindow(rootview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		checkType = (RadioGroup) rootview.findViewById(R.id.radioGroup1);
		checkType.setOnCheckedChangeListener(this);

		deviceType = (RadioGroup) rootview.findViewById(R.id.radioGroup2);
		deviceType.setOnCheckedChangeListener(this);

		infoType = (RadioGroup) rootview.findViewById(R.id.radioGroup3);
		infoType.setOnCheckedChangeListener(this);

		exceptionType = (RadioGroup) rootview.findViewById(R.id.radioGroup4);
		exceptionType.setOnCheckedChangeListener(this);

		// // 显示popupWindow对话框
		pw_Left.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
					pw_Left.dismiss();
					return true;
				}
				return false;
			}

		});
		ColorDrawable dw = new ColorDrawable(Color.GRAY);
		pw_Left.setBackgroundDrawable(dw);
		pw_Left.setOutsideTouchable(true);
		pw_Left.showAsDropDown(parent, Gravity.CENTER, 0);
		pw_Left.update();

	}
	
	void initPopupWindowByTime(View parent) {
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View rootview = inflater.inflate(
				R.layout.search_fragment_fliter_by_time, null, false);

		pw_Right = new PopupWindow(rootview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		timeType = (RadioGroup) rootview.findViewById(R.id.radioGroup1_bytime);
		timeType.setOnCheckedChangeListener(this);


		// // 显示popupWindow对话框
		pw_Right.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
					pw_Right.dismiss();
					return true;
				}
				return false;
			}

		});
		ColorDrawable dw = new ColorDrawable(Color.GRAY);
		pw_Right.setBackgroundDrawable(dw);
		pw_Right.setOutsideTouchable(true);
		pw_Right.showAsDropDown(parent, Gravity.CENTER, 0);
		pw_Right.update();

	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.search_fliter:
			initPopupWindowFliter(arg0);
			break;
		case R.id.search_fliterbytime:
			initPopupWindowByTime(arg0);
			break;
//		case R.id.spinner1_flit:
//			break;
//		case R.id.spinner1_flit:
//			break;
//		case R.id.spinner1_flit:
//			break;
			
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.radioGroup1:
			mCheck_Type_Index = arg1;
			break;
		case R.id.radioGroup2:
			mDevice_Type_Index = arg1;
			break;
		case R.id.radioGroup3:
			mInfo_Type_Index = arg1;
			break;
		case R.id.radioGroup4:
			mException_Type_Index = arg1;
			break;	
		case R.id.radioGroup1_bytime:
			mTime_Type_Index = arg1;
			break;
		}
		Log.d(TAG,"onCheckedChanged() arg1 ="+arg1);
	}
}
