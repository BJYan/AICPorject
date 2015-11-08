package com.aic.aicdetactor.activity;

import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.DeviceListAdapter;
import com.aic.aicdetactor.adapter.DialogPagerAdapter;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.ChartDialogBtnListener;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TempRouteActivity extends CommonActivity implements OnClickListener,ChartDialogBtnListener{
	TextView TempRouteSave,TempRouteChart;
	LayoutInflater mInflater;

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
			CommonDialog chartDialog = new CommonDialog(this);
			chartDialog.setCloseBtnVisibility(View.VISIBLE);
			chartDialog.setTitle("测试图谱");
			chartDialog.setButtomBtn(this, "确定", "取消");
			DataAnalysis dataAnalysis = new DataAnalysis();
			float[] data = dataAnalysis.getData();
			float[] MinMaxTemp = new float[]{data[0],data[0]};


			ViewPager dialogContent = (ViewPager) mInflater.inflate(R.layout.dialog_viewpager, null);
			List<View> views = new ArrayList<View>();
			views.add(mInflater.inflate(R.layout.dialog_content_thr_charts1_layout, null));
			views.add(mInflater.inflate(R.layout.dialog_content_thr_charts2_layout, null));
			views.add(mInflater.inflate(R.layout.dialog_content_one_charts_layout, null));
			//ViewPager dialogViewPager = (ViewPager) dialogContent.findViewById(R.id.dialog_viewpager);
			DialogPagerAdapter DialogPagerAdapter = new DialogPagerAdapter(this,views);
			dialogContent.setAdapter(DialogPagerAdapter);

			chartDialog.setChartView(dialogContent, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, 680));
			chartDialog.show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClickBtn1Listener(CommonDialog dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickBtn2Listener(CommonDialog dialog) {
		// TODO Auto-generated method stub
		
	}

}
