package com.aic.aicdetactor;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.database.RouteDao;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomDialog extends Dialog {
	int layoutRes;//布局文件
    Context context;
    String workerName=null;
    String oldPwdStr = null;
	String newPwdStr = null;
	String surePwdStr = null;
	String errortip = null;
	EditText old_pwd = null;
	 EditText new_pwd = null;
	 EditText sure_pwd =null;
	 Activity activity =null;
	 Button mModifyBT=null;
	 private ClickListenerInterface clickListenerInterface;
    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }
    
    public interface ClickListenerInterface {    	 
    	         public void doConfirm(String oldPwd,String newPwd,String surePwd);    	 
    	     }
    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
    	         this.clickListenerInterface = clickListenerInterface;
    	     }
    /**
     * 自定义布局的构造方法
     * @param context
     * @param resLayout
     */
    public CustomDialog(Context context,int resLayout){
        super(context);
        this.context = context;
        this.layoutRes=resLayout;
    }
    /**
     * 自定义主题及布局的构造方法
     * @param context
     * @param theme
     * @param resLayout
     */
    public CustomDialog(Context context, int theme,int resLayout,String workerName){
        super(context, theme);
        this.workerName =workerName;
        this.context = context;
        this.layoutRes=resLayout;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutRes);
        old_pwd = (EditText) findViewById(R.id.editText1);
		new_pwd = (EditText) findViewById(R.id.editText2);
		sure_pwd = (EditText) findViewById(R.id.editText3);
		mModifyBT= (Button) findViewById(R.id.button1);
		mModifyBT.setOnClickListener( new  View.OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_CHECK));
			}
			
		});
    }
    
    void modify(){
		
		if( workerName ==null){
			mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_NOT_LOGIN));
			return;
		}
		oldPwdStr = old_pwd.getText().toString();
		newPwdStr = new_pwd.getText().toString();
		surePwdStr = sure_pwd.getText().toString();
		if("".equals(newPwdStr) || "".equals(surePwdStr) || "".equals(oldPwdStr)){
			mHandler.sendMessage(mHandler.obtainMessage(R.string.pwd_err_tip));
			return;
		}
		
		if(!surePwdStr.equals(newPwdStr)){
			mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_NOT_EQUAL));
			return;
		}
		RouteDao dao = RouteDao.getInstance(context);
		ContentValues va = new ContentValues();
		if(!dao.ModifyWorkerPwd( workerName, oldPwdStr, newPwdStr, va)){
			Message msg =mHandler.obtainMessage(MESSAGE_LOGIN_ERROR);
			msg.obj = va.get("error");
			mHandler.sendMessage(msg);
			
		}else{
			mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_MODIFY_SUCCESS));			
		}
		
	}
	private final int MESSAGE_CHECK =0;
	private final int MESSAGE_NOT_EQUAL =1;
	private final int MESSAGE_NOT_LOGIN =2;
	private final int MESSAGE_LOGIN_ERROR =3;
	private final int MESSAGE_MODIFY_SUCCESS =4;
	Handler mHandler = new Handler(){
		 @Override
		    public void handleMessage(Message msg) {
		       // mImageView.setImageBitmap(mBitmap);
			 switch(msg.what){
			 case R.string.pwd_err_tip:
				 break;
			 case MESSAGE_CHECK:
				 modify();
				// Toast.makeText(context, context.getString(R.string.pwd_err_tip), Toast.LENGTH_LONG).show();
				 break;
			 case MESSAGE_NOT_EQUAL:
				 Toast.makeText(context, context.getString(R.string.pwd_err_tip2), Toast.LENGTH_LONG).show();
				 break;
			 case MESSAGE_NOT_LOGIN:
				 Toast.makeText(context, context.getString(R.string.pwd_not_login), Toast.LENGTH_LONG).show();
				 break;
			 case MESSAGE_LOGIN_ERROR:
				 Toast.makeText(context, (msg.obj).toString(), Toast.LENGTH_LONG).show();
				 break;
			 case MESSAGE_MODIFY_SUCCESS:
				 Toast.makeText(context, context.getString(R.string.pwd_modify_sucess), Toast.LENGTH_LONG).show();
				 dismiss();
				 break;
			 }
			
		    }
};
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

}
