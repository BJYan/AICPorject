package com.aic.aicdetactor.activity;

import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.DeviceListAdapter;
import com.aic.aicdetactor.adapter.CommonViewPagerAdapter;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.CommonDialogBtnListener;
import com.aic.aicdetactor.dialog.OneCtrlDialog;
import com.aic.aicdetactor.dialog.OneCtrlDialog.OneCtrlDialogBtnListener;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class TempRouteActivity extends CommonActivity implements OnClickListener,OneCtrlDialogBtnListener{
	TextView TempRouteSave,TempRouteChart;
	LayoutInflater mInflater;
	ViewPager dialogContent;
	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_temp_layout);
		
		setActionBar("临时测量",true);
		mInflater = getLayoutInflater();
		
		TempRouteSave = (TextView) findViewById(R.id.route_temp_save);
		TempRouteSave.setOnClickListener(this);
		TempRouteChart = (TextView) findViewById(R.id.route_temp_chart);
		TempRouteChart.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.route_temp_chart:
			OneCtrlDialog chartDialog = new OneCtrlDialog(this);
			//chartDialog.setCloseBtnVisibility(View.VISIBLE);
			//chartDialog.setTitle("测试图谱");
			chartDialog.setButtomBtn(this, "确定", "取消");

			DataAnalysis dataAnalysis = new DataAnalysis();
			float[] data = dataAnalysis.getData();
			float[] MinMaxTemp = new float[]{data[0],data[0]};

			dialogContent = (ViewPager) mInflater.inflate(R.layout.dialog_viewpager, null);
			List<View> views = new ArrayList<View>();
			views.add(mInflater.inflate(R.layout.dialog_content_thr_charts1_layout, null));
			views.add(mInflater.inflate(R.layout.dialog_content_thr_charts2_layout, null));
			views.add(mInflater.inflate(R.layout.dialog_content_one_charts_layout, null));
			views.add(mInflater.inflate(R.layout.motor_load_vibration_layout, null));
			//ViewPager dialogViewPager = (ViewPager) dialogContent.findViewById(R.id.dialog_viewpager);
			CommonViewPagerAdapter DialogPagerAdapter = new CommonViewPagerAdapter(this,views);
			dialogContent.setAdapter(DialogPagerAdapter);
			dialogContent.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					tabHost.setCurrentTab(arg0);
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			String[] tab_first = new String[]{"时域","轴心","频域","有效值"};
			tabHost = chartDialog.TabViewInit(tab_first,new OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					// TODO Auto-generated method stub
			        if(tabId.equals("tab1")) dialogContent.setCurrentItem(0);
			        if(tabId.equals("tab2")) dialogContent.setCurrentItem(1);
			        if(tabId.equals("tab3")) dialogContent.setCurrentItem(2);
			        if(tabId.equals("tab4")) dialogContent.setCurrentItem(3);
				}
			});
			chartDialog.setChartView(dialogContent, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, 680));
			chartDialog.show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClickBtn1Listener(OneCtrlDialog dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickBtn2Listener(OneCtrlDialog dialog) {
		// TODO Auto-generated method stub
		
	}
}
