package com.aic.aicdetactor.dialog;

import java.util.Vector;

import com.aic.aicdetactor.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class SuperChartDialog extends Dialog{
	Context context;
	LayoutInflater inflater;
	LinearLayout ctrlerContainer;
	Vector<TabHost> ctrlerContainerList;

	public SuperChartDialog(Context context) {
		super(context, R.style.Theme_Light_FullScreenDialogAct);
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
		ctrlerContainerList = new Vector<TabHost>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_super_chart_layout);
		
		setController("时域","轴心","频域");
		setController("x","y","z");
		controllerInit();
	}

	private void controllerInit() {
		
		ctrlerContainer = (LinearLayout) findViewById(R.id.dialog_controller_container);
		ctrlerContainer.removeAllViews();
		for(int i=0;i<ctrlerContainerList.size();i++){
			if(i==0&&ctrlerContainerList.get(0)!=null) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.weight = 1.0f;
				ctrlerContainerList.get(0).setLayoutParams(params);
				ctrlerContainer.addView(ctrlerContainerList.get(0));
			} else if(ctrlerContainerList.get(i)!=null) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.weight = 1.0f;
				params.leftMargin = 10;
				ctrlerContainerList.get(i).setLayoutParams(params);
				ctrlerContainer.addView(ctrlerContainerList.get(i));
			}
		}
	}
	
	protected TabHost getTabView(String tabname1, String tabname2, String tabname3){
		TabHost tab = (TabHost) inflater.inflate(R.layout.dialog_tabhost_layout, null);
		tab.setup();
        
		TabSpec tab1 = tab.newTabSpec("tab1");
		TextView tab1_tv = (TextView) inflater.inflate(R.layout.dialog_tab_layout, null);
		if(tabname1!=null) tab1_tv.setText(tabname1);
		tab1.setIndicator(tab1_tv);
		tab1.setContent(R.id.tab1);
		tab.addTab(tab1);
		TabSpec tab2 = tab.newTabSpec("tab2");
		TextView tab2_tv = (TextView) inflater.inflate(R.layout.dialog_tab_layout, null);
		if(tabname2!=null) tab2_tv.setText(tabname2);
		tab2.setIndicator(tab2_tv);
		tab2.setContent(R.id.tab2);
		tab.addTab(tab2);
		TabSpec tab3 = tab.newTabSpec("tab3");
		TextView tab3_tv = (TextView) inflater.inflate(R.layout.dialog_tab_layout, null);
		if(tabname3!=null) tab3_tv.setText(tabname3);
		tab3.setIndicator(tab3_tv);
		tab3.setContent(R.id.tab3);
		tab.addTab(tab3);
		return tab;
	}
	
	protected void setController(String tabname1, String tabname2, String tabname3){
		TabHost controller = getTabView(tabname1, tabname2, tabname3);
		ctrlerContainerList.add(controller);
	}

	protected void clearController(){
		ctrlerContainerList.clear();
	}
}
