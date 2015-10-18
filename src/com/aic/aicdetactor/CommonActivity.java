package com.aic.aicdetactor;

import java.lang.reflect.Field;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.dialog.FlippingLoadingDialog;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogCancelListener;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogOKListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

public class CommonActivity extends Activity{
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
		mLoadingDialog.show();
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
	    case R.id.action_modify_pwd: {
	    	CustomDialog dialog=new CustomDialog(this, R.style.customDialog, R.layout.modify_password,app.mWorkerName);
	        dialog.show();
	        }
	      break;  
	    case R.id.action_changeWorker:
	    //	initFragment();
	    	break;
	    case R.id.action_more:{
			}
	    	break;
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
