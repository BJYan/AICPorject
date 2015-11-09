package com.aic.aicdetactor.dialog;

import java.util.Vector;

import com.aic.aicdetactor.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class OneCtrlDialog extends Dialog implements android.view.View.OnClickListener{
	Context context;
	LayoutInflater inflater;
	LinearLayout ctrlerContainer;
	Vector<TabHost> ctrlerContainerList;
	OneCtrlDialogBtnListener listener; 
	View contentView;

	public interface OneCtrlDialogBtnListener{
		public void onClickBtn1Listener(OneCtrlDialog dialog);
		public void onClickBtn2Listener(OneCtrlDialog dialog);
	}
	public OneCtrlDialog(Context context) {
		super(context, R.style.Theme_Light_FullScreenDialogAct);
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
		ctrlerContainerList = new Vector<TabHost>();
		contentView = inflater.inflate(R.layout.dialog_onectrl_layout, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(contentView);

		//String[] tab_first = new String[]{"时域","轴心","频域","有效值"};
		//String[] tab_sec = new String[]{"x","y","z"};
		//controllerInit(tab_first,null);
		//TabViewInit(tab_first);
	}
	
	public void setCloseBtnVisibility(int visibility){
		ImageView dialogClose = (ImageView) contentView.findViewById(R.id.dialog_close); 
		dialogClose.setVisibility(visibility);
		dialogClose.setOnClickListener(this);
	}
	
	public void setButtomBtn(OneCtrlDialogBtnListener listener, String btn1name, String btn2name){
		this.listener = listener;
		Button Button1 = null,Button2 = null;
		if(!btn1name.equals("")) {
			Button1 =  (Button) contentView.findViewById(R.id.dialog_btn1);
			Button1.setVisibility(View.VISIBLE);
			Button1.setOnClickListener(this); 
		}
		if(!btn2name.equals("")) {
			Button2 =  (Button) contentView.findViewById(R.id.dialog_btn2);
			Button2.setVisibility(View.VISIBLE);
			Button2.setOnClickListener(this); 
		}
		if(!btn1name.equals("")&&!btn2name.equals("")){ 
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( Button2.getLayoutParams());
			lp.leftMargin = 70;
			if(Button2!=null) Button2.setLayoutParams(lp);
		}
	}
	
	public void setChartView(View chartView, ViewGroup.LayoutParams layoutParams){
		LinearLayout ChartContainer = (LinearLayout) contentView.findViewById(R.id.dialog_content_container);
		ChartContainer.removeAllViews();
		ChartContainer.addView(chartView,layoutParams);
	}
	
	public TabHost TabViewInit(String[] tabname, OnTabChangeListener tabChangeListener){
		TabHost tab = (TabHost)contentView.findViewById(R.id.dialog_tabhost1);
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
		TabSpec tab4 = tab.newTabSpec("tab4");
		TextView tab4_tv = (TextView) inflater.inflate(R.layout.dialog_tab_layout, null);
		if(tabname[3]!=null) tab4_tv.setText(tabname[3]);
		tab4.setIndicator(tab4_tv);
		tab4.setContent(R.id.tab4);
		tab.addTab(tab4);
		
		tab.setOnTabChangedListener(tabChangeListener);
		return tab;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.dialog_close:
			this.dismiss();
			break;
		case R.id.dialog_btn1:
			if(listener!=null) listener.onClickBtn1Listener(this);
			break;
		case R.id.dialog_btn2:
			if(listener!=null) listener.onClickBtn2Listener(this);
			break;
			
		default:
			break;
		}
	}

}
