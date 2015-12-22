package com.aic.aicdetactor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.achartengine.ChartFactory;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.XYChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.dialog.FlippingLoadingDialog;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogCancelListener;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogOKListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;

public class CommonActivity extends FragmentActivity{
	protected static final String TAG = "CommonActivity";
	protected FlippingLoadingDialog mLoadingDialog;
	public myApplication app = null;
	CommonAlterDialog commonAlterDialog;
	CommonDialog chartDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mLoadingDialog = new FlippingLoadingDialog(this, "请求提交中");
	}
	
	protected void setActionBar(String titleName, boolean hasBackButton){
		ActionBar actionBar = getActionBar();		
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME  
		        | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		if(titleName!=null) actionBar.setTitle(titleName);
		actionBar.setDisplayHomeAsUpEnabled(hasBackButton);
		showOverflowMenu();
	}
	
	//force to show overflow menu in actionbar
	protected void showOverflowMenu() {
         try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void showLoadingDialog(String text) {
		if (text != null) {
			mLoadingDialog.setText(text);
		}
		if(!mLoadingDialog.isShowing()) mLoadingDialog.show();
	}

	public void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}
	
	public void showChartDialog(Context context){
		chartDialog = new CommonDialog(context);
		chartDialog.setCloseBtnVisibility(View.VISIBLE);
		chartDialog.show();
	}
	
	public void dismissChartDialog(){
		if (chartDialog!=null&&chartDialog.isShowing()) {
			chartDialog.dismiss();
		}
	}
	
	public void showAlterDialog(Context context, String title, String content,
			AltDialogOKListener okListener,AltDialogCancelListener cancelListener) {
		commonAlterDialog = new CommonAlterDialog(context, title, content,
			okListener, cancelListener);
		commonAlterDialog.show();
	}
	
	public void dismissAlterDialog(){
		if (commonAlterDialog!=null&&commonAlterDialog.isShowing()) {
			commonAlterDialog.dismiss();
		}
	}
	 @Override  
	  public boolean onCreateOptionsMenu(Menu menu) {  
	    getMenuInflater().inflate(R.menu.main, menu);  
	    return true;  
	  }  
	 @Override  
	  public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) { 
	    case android.R.id.home:
	    	finish();
	    	break;
//	    case R.id.action_modify_pwd: {
//	    	CustomDialog dialog=new CustomDialog(this, R.style.customDialog, R.layout.modify_password,app.getLoginWorkerName());
//	        dialog.show();
//	        }
//	      break;  
//	    case R.id.action_changeWorker:
//	    //	initFragment();
//	    	break;
//	    case R.id.action_more:{
//			}
//	    	break;
	    case R.id.action_about:{
	    	final Dialog dialog = new AboutDialog(this);
			dialog.show();
			}
	    	break;
	    	
	    default:  
	      break;  
	    }  
	    return true;  
	  }

}
