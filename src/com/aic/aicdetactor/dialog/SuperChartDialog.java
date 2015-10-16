package com.aic.aicdetactor.dialog;

import java.util.Vector;

import com.aic.aicdetactor.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
		
		String[] tab_first = new String[]{"时域","轴心","频域"};
		String[] tab_sec = new String[]{"x","y","z"};
		controllerInit(tab_first,tab_sec);
	}
	
	private void controllerInit(String[] tabname1,String[] tabname2){
		if(tabname1!=null) {
			TabHost tab1 = (TabHost)findViewById(R.id.dialog_tabhost1);
			tab1.setVisibility(View.VISIBLE);
			TabViewInit(tab1, tabname1);
		}
		if(tabname2!=null) {
			TabHost tab2 = (TabHost)findViewById(R.id.dialog_tabhost2);
			tab2.setVisibility(View.VISIBLE);
			TabViewInit(tab2, tabname2);
		}
		if(tabname1!=null&&tabname2!=null){
			View line = findViewById(R.id.dialog_tabhost_line);
			line.setVisibility(View.VISIBLE);
		}
	}
	
	protected TabHost TabViewInit(TabHost tab, String[] tabname){
		tab.setup();
        
		TabSpec tab1 = tab.newTabSpec("tab1");
		TextView tab1_tv = (TextView) inflater.inflate(R.layout.dialog_tab_layout, null);
		if(tabname[0]!=null) tab1_tv.setText(tabname[0]);
		tab1.setIndicator(tab1_tv);
		tab1.setContent(R.id.tab1);
		tab.addTab(tab1);
		TabSpec tab2 = tab.newTabSpec("tab2");
		TextView tab2_tv = (TextView) inflater.inflate(R.layout.dialog_tab_layout, null);
		if(tabname[1]!=null) tab2_tv.setText(tabname[1]);
		tab2.setIndicator(tab2_tv);
		tab2.setContent(R.id.tab2);
		tab.addTab(tab2);
		TabSpec tab3 = tab.newTabSpec("tab3");
		TextView tab3_tv = (TextView) inflater.inflate(R.layout.dialog_tab_layout, null);
		if(tabname[2]!=null) tab3_tv.setText(tabname[2]);
		tab3.setIndicator(tab3_tv);
		tab3.setContent(R.id.tab3);
		tab.addTab(tab3);
		return tab;
	}

}
