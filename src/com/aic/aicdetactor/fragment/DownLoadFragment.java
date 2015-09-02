package com.aic.aicdetactor.fragment;

import java.util.List;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.database.TemporaryRouteDao;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class DownLoadFragment extends Fragment implements OnClickListener {
	private LinearLayout mSetting_linearLayout = null; 
	private LinearLayout mUp_linearLayout = null;
	private LinearLayout mDown_linearLayout = null;
	private RadioGroup mRadioGroup = null;
  
	private SharedPreferences mSharedPreferences = null;
    
	private final String AUTO_UP = "download_up_auto_up";
	private final String UP_DELETE = "download_up_up_delete";
	private final String UP_TYPE = "download_up_up_type";
	private final String AUTO_DOWN = "download_down_auto_down";
	private final String DOWN_TYPE = "download_down_down_type";
	private final String ONLY_WIFI = "download_setting_onlywifi";
	//上传配置
	private CheckBox mUp_CheckBox_autoUp = null;
	private CheckBox mUp_CheckBox_Up_Delete = null;
	private RadioGroup mUp_RadioGroup = null;
	private String mStr_Up_IP="";
	private String mStr_Up_Pda_code="";
	//本机在WIFI状态下路由分配给的IP地址  
	private String mStr_Up_Pda_ip="";
	private String mPda_mac="";
	private int mUp_RadioGroup_Index =-1;
	private Button mUp_Button = null;
	private boolean mbUp_auto_up = false;
	private boolean mbUp_up_delete = false;
	//下载配置
	private CheckBox mDown_CheckBox_autoDown = null;
	
	private RadioGroup mDown_RadioGroup = null;
	private String mStr_Down_IP="";
	private String mStr_Down_Pda_code="";
	private String mStr_Down_Pda_ip="";
	private String mStr_Down_Pda_mac="";
	private Button mDown_Button = null;
	private int mDown_RadioGroup_Index =-1;
	private boolean mbDown_auto_down = false;
	//设置配置
	private String mStr_Setting_IP="";
	private String mStr_Setting_Pda_code="";
	private CheckBox mSetting_CheckBox_OnlyWifi = null;
	private boolean mbSetting_Only_Wifi = false;
	private TextView mUpMacTextView = null;
	private TextView mDownMacTextView = null;
	private TextView mUpIPTextView = null;
	private TextView mDownIPTextView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.downup_fragment, container, false);
		
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		
		mSetting_linearLayout = (LinearLayout)view.findViewById(R.id.setting_linear);
		mUp_linearLayout = (LinearLayout)view.findViewById(R.id.up_linear);
		mDown_linearLayout = (LinearLayout)view.findViewById(R.id.down_linear);
		mUpMacTextView = (TextView)view.findViewById(R.id.textView_up_pda_mac);
		mDownMacTextView = (TextView)view.findViewById(R.id.textView_down_pda_mac);
		mUpIPTextView= (TextView)view.findViewById(R.id.textView_up_pda_ip);
		mDownIPTextView= (TextView)view.findViewById(R.id.textView_down_pda_ip);
		displayMacAndIPAddress();
		//选项 上传  下载 设置
		mRadioGroup = (RadioGroup)view.findViewById(R.id.downup_group);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg0.getCheckedRadioButtonId()){
				case R.id.downup_radioButton1:
					
					mUp_linearLayout.setVisibility(View.VISIBLE);
					mSetting_linearLayout.setVisibility(View.GONE);
					mDown_linearLayout.setVisibility(View.GONE);
					break;
				case R.id.downup_radioButton2:
					mUp_linearLayout.setVisibility(View.GONE);
					mSetting_linearLayout.setVisibility(View.GONE);
					mDown_linearLayout.setVisibility(View.VISIBLE);
					
					break;
				case R.id.downup_radioButton3:
					
					mSetting_linearLayout.setVisibility(View.VISIBLE);
					mUp_linearLayout.setVisibility(View.GONE);
					mDown_linearLayout.setVisibility(View.GONE);
					break;
				}
			}
			
		});	
				
			//上传设置	
		mUp_CheckBox_autoUp =(CheckBox)view.findViewById(R.id.checkbox_up_auto_up);
		mUp_CheckBox_autoUp.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mbUp_auto_up = arg1;
			}
			
		});
		mUp_CheckBox_Up_Delete =(CheckBox)view.findViewById(R.id.uped_delete_checkbox1);
		mUp_CheckBox_Up_Delete.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mbUp_up_delete = arg1;
			}
			
		});
		mUp_RadioGroup =(RadioGroup)view.findViewById(R.id.up_group);
		mUp_RadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg0.getCheckedRadioButtonId()){
				case R.id.up_radioButton1:					
					mUp_RadioGroup_Index =0;
					break;
				case R.id.up_radioButton2:
					mUp_RadioGroup_Index =1;					
					break;
				case R.id.up_radioButton3:					
					mUp_RadioGroup_Index =2;
					break;
				case R.id.up_radioButton4:					
					mUp_RadioGroup_Index =3;
					break;
				}
			}
			
		});	
		
		mUp_Button = (Button)view.findViewById(R.id.up_up);
		mUp_Button.setOnClickListener(this);
		
	  //下载设置
		mDown_CheckBox_autoDown =(CheckBox)view.findViewById(R.id.down_checkbox1);
		mDown_CheckBox_autoDown.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mbDown_auto_down = arg1;
			}
			
		});
		mDown_RadioGroup =(RadioGroup)view.findViewById(R.id.down_group);
		mDown_RadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg0.getCheckedRadioButtonId()){
				case R.id.down_radioButton1:					
					mDown_RadioGroup_Index =0;
					break;
				case R.id.down_radioButton2:
					mDown_RadioGroup_Index =1;					
					break;
				}
			}
			
		});	
		
		mDown_Button = (Button)view.findViewById(R.id.down_down);
		mDown_Button.setOnClickListener(this);
		
		//设置
		mSetting_CheckBox_OnlyWifi =  (CheckBox)view.findViewById(R.id.setting_checkbox1);
		mSetting_CheckBox_OnlyWifi.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mbSetting_Only_Wifi = arg1;
			}
			
		});
		
		//初始化信息
		getData();
		initViewValue();
	    return view;
	}


	/**
	 * 需要保存的是界面上的复选框 及单选框的内容，下次读出来和保存时 一致即可
	 */
	void saveData(){
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putBoolean(AUTO_UP, mbUp_auto_up);
		editor.putBoolean(UP_DELETE, mbUp_up_delete);		
		editor.putLong(UP_TYPE, mUp_RadioGroup_Index);		
		editor.putBoolean(AUTO_DOWN, mbDown_auto_down);		
		editor.putLong(DOWN_TYPE, mDown_RadioGroup_Index);	
		editor.putBoolean(ONLY_WIFI, mbSetting_Only_Wifi);	
		
		editor.commit();
	}
	
	
	void getData(){		
		
		mbUp_auto_up =  mSharedPreferences.getBoolean(AUTO_UP, false);
		mbUp_up_delete =  mSharedPreferences.getBoolean(UP_DELETE, false);
		mbDown_auto_down =  mSharedPreferences.getBoolean(AUTO_DOWN, false);
		mUp_RadioGroup_Index = (int) mSharedPreferences.getLong(UP_TYPE, 0);
		mDown_RadioGroup_Index = (int) mSharedPreferences.getLong(DOWN_TYPE, 0);
		mbSetting_Only_Wifi =  mSharedPreferences.getBoolean(ONLY_WIFI, false);
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		saveData();
		super.onDestroy();
	}


	void initViewValue(){
		mUp_CheckBox_autoUp.setChecked(mbUp_auto_up);
		mUp_CheckBox_Up_Delete.setChecked(mbUp_up_delete);
		mDown_CheckBox_autoDown.setChecked(mbDown_auto_down);
		mSetting_CheckBox_OnlyWifi.setChecked(mbSetting_Only_Wifi);
		switch(mUp_RadioGroup_Index){
		case 0:
			mUp_RadioGroup.check(R.id.up_radioButton1);
			break;
		case 1:
			mUp_RadioGroup.check(R.id.up_radioButton2);
			break;
		case 2:
			mUp_RadioGroup.check(R.id.up_radioButton3);
			break;
		case 3:
			mUp_RadioGroup.check(R.id.up_radioButton4);
			break;
		}
		
		switch(mDown_RadioGroup_Index){
		case 0:
			mDown_RadioGroup.check(R.id.down_radioButton1);
			break;
		case 1:
			mDown_RadioGroup.check(R.id.down_radioButton2);
			break;
		}
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.up_up:
			break;
		case R.id.down_down:
			break;
		}
	}
	
	void displayMacAndIPAddress(){
		WifiManager wifiMan = (WifiManager) this.getActivity().getSystemService(Context.WIFI_SERVICE);  
		WifiInfo info = wifiMan.getConnectionInfo();  
		mPda_mac = info.getMacAddress();// 获得本机的MAC地址  
		String ssid = info.getSSID();// 获得本机所链接的WIFI名称  
		  
		int ipAddress = info.getIpAddress();  
		  
		// 获得IP地址的方法一：  
		if (ipAddress != 0) {  
			mStr_Up_Pda_ip = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."   
		        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));  
		}  
		
		mUpMacTextView.setText(mPda_mac);
		mDownMacTextView.setText(mPda_mac);
		
		mUpIPTextView.setText(mStr_Up_Pda_ip);
		mDownIPTextView.setText(mStr_Up_Pda_ip);
	}
}
