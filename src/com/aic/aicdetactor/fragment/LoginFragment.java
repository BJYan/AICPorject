package com.aic.aicdetactor.fragment;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.TestSetting;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.SystemUtil;

public class LoginFragment extends Fragment implements OnClickListener{
	private Button mButtonLog = null;
	private EditText mEditTextUserName = null;
	private EditText mEditTextUserPwd = null;
	String TAG = "luotest";
	private LoginListener mCallBack = null;
	private String mLogName=null;
	private String mLogPwd = null;
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
		View view = inflater.inflate(R.layout.activity_main, container, false);
		mButtonLog = ( Button)view.findViewById(R.id.login);
		mButtonLog.setOnClickListener(this);
		mEditTextUserName = (EditText)view.findViewById(R.id.name);
		mEditTextUserPwd = (EditText)view.findViewById(R.id.password);
	
		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.login:
			mCallBack.Click(Login(),mLogName,mLogPwd);
			break;
		}
	}
	
	public interface LoginListener{
		void Click(boolean logIn,String Name,String pwd);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		
		if(mCallBack == null){
			
		}
	  try {
		  mCallBack = (LoginListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginListener");
        }
		  
		super.onAttach(activity);
	}
	
	boolean Login(){
		mLogName = mEditTextUserName.getText().toString();
		mLogPwd = mEditTextUserPwd.getText().toString();
		//search table 
		TestSetting test = new TestSetting(this.getActivity().getApplicationContext());
		List<String> testlist = test.getTestFile();

		if (testlist != null) {
			for (int testi = 0; testi < testlist.size(); testi++) {
				Log.d(TAG,"read file from test=" + testi + ","+ testlist.get(testi));
				((myApplication) this.getActivity().getApplication()).insertNewRouteInfo(SystemUtil.createGUID(), testlist.get(testi), this.getActivity());
			}
		} 
		RouteDao dao = new RouteDao(this.getActivity().getApplicationContext());
	List<String>fileList = 	dao.queryLogIn(mLogName, mLogPwd);
	
	if(fileList.size()>0){	
		return true;
	}
	return false;
	}
}
