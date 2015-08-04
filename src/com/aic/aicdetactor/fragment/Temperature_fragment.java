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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.comm.CommonDef;


public class Temperature_fragment extends Fragment  implements OnButtonListener{

	//listview
	private ListView mListview = null;
	//示意图片显示
	private ImageView mImageView = null;
	//温度测试结果
	private TextView mTemeratureResultStr = null;
	//温度测试结果对应的颜色，是否正常
	private RadioButton mRadioButton = null;
	//测量温度结果对应的文字描述
	private TextView mColorTextView = null;
	
	//之间的通信接口
	private OnMeasureListener mCallback = null;
	private List<Map<String, Object>> mMapList = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		mMapList = new ArrayList<Map<String, Object>>();
		//初始化ListVew 数据项
		String [] arraryStr = new String[]{this.getString(R.string.electric_device_parameters),
				this.getString(R.string.electric_device_spectrum)};
			for (int i = 0; i < arraryStr.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();				
				map.put(CommonDef.check_item_info.UNIT_NAME,arraryStr[i] );								
				map.put(CommonDef.check_item_info.DEADLINE, "2015-06-20 10:00");

				//已检查项的检查数值怎么保存？并显示出来
				//已巡检的项的个数统计，暂时由是否有巡检时间来算，如果有的话，即已巡检过了，否则为未巡检。
				mMapList.add(map);
			}
			
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.temperature, container, false);
		mListview = (ListView)view.findViewById(R.id.listView1);
		mImageView = (ImageView)view.findViewById(R.id.imageView1);
		
		mRadioButton = (RadioButton)view.findViewById(R.id.radioButton2);
		mColorTextView = (TextView)view.findViewById(R.id.resultTips);
		mTemeratureResultStr =(TextView)view.findViewById(R.id.temperature);
		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	  //临时生成随机的三维坐标数据及温度数据。
    void genRandom_temperation(){    
    	float max_temperation=300;
    	float MAX = 200;
    	float MID = 100;
    	float LOW = 0;
    
    	float temp = (int) (Math.random()*max_temperation);
    	if((temp < MAX) && (temp>=MID) ){
    		mRadioButton.setBackgroundColor(Color.YELLOW);
    		mColorTextView.setText(getString(R.string.warning));
    		
    	}else if((temp >= LOW) && (temp<MID)){
    		mRadioButton.setBackgroundColor(Color.BLACK);
    		mColorTextView.setText(getString(R.string.normal));
    	}else if(temp <LOW){
    		mRadioButton.setBackgroundColor(Color.GRAY);
    		mColorTextView.setText(getString(R.string.invalid));
    	}else if(temp>=MAX){
    		mRadioButton.setBackgroundColor(Color.RED);
    		mColorTextView.setText(getString(R.string.dangerous));
    	}
    	mTemeratureResultStr.setText(temp + " C");
    	mCallback.OnClick(temp + " C"+"*");
    }

    @Override
	public void onStart() {
		// TODO Auto-generated method stub
    	genRandom_temperation();
		super.onStart();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		super.onViewCreated(view, savedInstanceState);
		
		
		//handler.postDelayed(runnable, 500);
	
	}
    Handler handler = new Handler(); 
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			genRandom_temperation();
		}
	}; 
    
    public interface OnMeasureListener{
    	
    	void OnClick(String IndexButton);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnMeasureListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMeasureListener");
        }
    }

	@Override
	public void OnButtonDown(int buttonId, Bundle bundle) {
		// TODO Auto-generated method stub
		
	}
}
