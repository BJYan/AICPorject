package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.Config.Config;
import com.aic.aicdetactor.Event.Event;
import com.aic.aicdetactor.adapter.NetWorkSettingAdapter;
import com.aic.aicdetactor.adapter.NetworkViewPagerAdapter;
import com.aic.aicdetactor.adapter.SpinnerAdapter;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.CommonDialogBtnListener;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

public class DownLoadFragment extends Fragment implements OnClickListener {
  
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
	//本机在WIFI状态下路由分配给的IP地址  
	private String mStr_Up_Pda_ip="";
	private String mPda_mac="";
	private int mUp_RadioGroup_Index =-1;
	private boolean mbUp_auto_up = false;
	private boolean mbUp_up_delete = false;
	//下载配置
	private CheckBox mDown_CheckBox_autoDown = null;
	
	private RadioGroup mDown_RadioGroup = null;
	private int mDown_RadioGroup_Index =-1;
	private boolean mbDown_auto_down = false;
	//设置配置
	private CheckBox mSetting_CheckBox_OnlyWifi = null;
	private boolean mbSetting_Only_Wifi = false;
	private TextView mUpMacTextView = null;
	private TextView mDownMacTextView = null;
	private TextView mUpIPTextView = null;
	private TextView mDownIPTextView = null;
	
	private ListView mDownLoadListView;
	List<View> listViews;
	TabHost tabHost;
	ViewPager viewPager;
	NetWorkSettingAdapter netWorkSettingAdapter;
	private Handler mHandler=null;
	View networkView;
	List<View> settingViewList;
	LayoutInflater mInflater;
	TextView devName;
	SpinnerAdapter mDLLineAdapter=null;
	LineDao mDao;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mHandler =new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case Event.LocalData_Init_Failed:
					{
						CommonAlterDialog dialog = new CommonAlterDialog(DownLoadFragment.this.getActivity(),"提示","没本地巡检数据，请确认有/sdcard/AICLine.txt文件",null,null);
					
					dialog.show();
					}
					break;
				case Event.NetWork_Connecte_Timeout:
				case Event.NetWork_MSG_Tips:
				case Event.Server_No_Data:
					{
						CommonAlterDialog dialog = new CommonAlterDialog(DownLoadFragment.this.getActivity(),"提示",(String)msg.obj,null,null);
						dialog.show();
					}
					break;
				case  Event.LocalData_Init_Success:
					
					Toast.makeText(getActivity().getApplicationContext(),(String)(msg.obj),
							Toast.LENGTH_SHORT).show();
					initAllDownLoadRoutList();
					break;
				case  Event.GetNewRouteLine_Message:
					showOKCancel((String)msg.obj);
					
					break;
				case Event.ReplaceRouteLineData_Message:
					mDao.ReplaceOriginalFileAndUpdateDB((String)msg.obj);
						break;
				default:
					break;
				}
				super.handleMessage(msg);
			}};
	}
	 

	void initAllDownLoadRoutList(){
		mDLLineAdapter = new SpinnerAdapter(DownLoadFragment.this.getActivity().getApplicationContext(),mDao.getAllDownLoadRouteInfo());
		mDownLoadListView.setAdapter(mDLLineAdapter);
		mDownLoadListView.setClickable(false);
		
		mDLLineAdapter.setListData(mDao.getAllDownLoadRouteInfo());
		mDLLineAdapter.notifyDataSetChanged();
	}
	void showOKCancel(final String str){
		 CommonDialog acceleChart = new CommonDialog(this.getActivity());
			acceleChart.setTitle("警告");
			acceleChart.setContent("确认要覆盖现有的巡检路线?\n如点击确认即将退出应用");
			acceleChart.setCloseBtnVisibility(View.VISIBLE);
			acceleChart.setButtomBtn(new CommonDialogBtnListener() {

				@Override
				public void onClickBtn2Listener(CommonDialog dialog) {
					// TODO Auto-generated method stub
					
					 Message msg = mHandler.obtainMessage(Event.ReplaceRouteLineData_Message);
					 msg.obj=str;
					 mHandler.sendMessage(msg);					
					 dialog.dismiss();
				}

				@Override
				public void onClickBtn1Listener(CommonDialog dialog) {
					// TODO Auto-generated method stub
					//delete temp file
					String []list = str.split("\\*");
					if(list!=null && list.length>0){
					 SystemUtil.deleteFile(list[0]);
					}
					
					dialog.dismiss();
				}
			}, "确认覆盖", "取消");
			acceleChart.show();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mInflater = getActivity().getLayoutInflater();
		networkView = inflater.inflate(R.layout.network_fragment, container, false);
        networkTabviewInit(inflater);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        mDao = LineDao.getInstance(getActivity());
		uploadInit();
		downloadInit();   
		settingViewinit();
	
	    return networkView;
	}


	private void settingViewinit() {
		settingViewList = new ArrayList<View>();
		settingViewList.add(mInflater.inflate(R.layout.network_setting_item_machineinfo, null,false)); 
		settingViewList.add(mInflater.inflate(R.layout.network_setting_item_retpassword, null,false)); 
		settingViewList.add(mInflater.inflate(R.layout.network_setting_item_pushsetting, null,false)); 
		settingViewList.add(mInflater.inflate(R.layout.network_setting_item_cardreader, null,false));
		settingViewList.add(mInflater.inflate(R.layout.network_setting_item_storage, null,false)); 
		settingViewList.add(mInflater.inflate(R.layout.network_setting_item_timesync, null,false));
		
		netWorkSettingAdapter = new NetWorkSettingAdapter(getActivity(),settingViewList);
        ExpandableListView netExListView = (ExpandableListView)listViews.get(2).findViewById(R.id.network_setting_list);
        netExListView.setAdapter(netWorkSettingAdapter);
        netExListView.setGroupIndicator(null);

        devName = (TextView) settingViewList.get(0).findViewById(R.id.network_setting_device_name);
		devName.setOnClickListener(this);
		TextView devIP = (TextView) settingViewList.get(0).findViewById(R.id.network_setting_device_ip);
		TextView devMac = (TextView) settingViewList.get(0).findViewById(R.id.network_setting_device_mac);
		
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
		devMac.setText(mPda_mac);		
		devIP.setText(mStr_Up_Pda_ip);
		
		EditText countid = (EditText) settingViewList.get(1).findViewById(R.id.network_setting_countid);
		EditText oldPassword = (EditText) settingViewList.get(1).findViewById(R.id.network_setting_old_password);
		EditText newPassword = (EditText) settingViewList.get(1).findViewById(R.id.network_setting_new_password);
		EditText confirmPassword = (EditText) settingViewList.get(1).findViewById(R.id.network_setting_confirm_password);
		Button savebtn = (Button) settingViewList.get(1).findViewById(R.id.network_setting_save);
 
		Switch newplan = (Switch) settingViewList.get(2).findViewById(R.id.network_setting_newplan);
		Switch newmsg = (Switch) settingViewList.get(2).findViewById(R.id.network_setting_newmsg);
		Switch newtask = (Switch) settingViewList.get(2).findViewById(R.id.network_setting_newtask);
		Switch lowmemory = (Switch) settingViewList.get(2).findViewById(R.id.network_setting_lowmemory);
 
		EditText[] serverip = new EditText[4];
		serverip[0] = (EditText) settingViewList.get(3).findViewById(R.id.network_setting_server_ip1);
		serverip[1] = (EditText) settingViewList.get(3).findViewById(R.id.network_setting_server_ip2);
		serverip[2] = (EditText) settingViewList.get(3).findViewById(R.id.network_setting_server_ip3);
		serverip[3] = (EditText) settingViewList.get(3).findViewById(R.id.network_setting_server_ip4);
		EditText cardNum = (EditText) settingViewList.get(3).findViewById(R.id.network_setting_card_number);
		Button serverSave = (Button) settingViewList.get(3).findViewById(R.id.network_setting_server_save);

		TextView totalStorage = (TextView) settingViewList.get(4).findViewById(R.id.network_setting_total_storage);
		TextView availableStorage = (TextView) settingViewList.get(4).findViewById(R.id.network_setting_available_storage);
		Spinner checkResultNum = (Spinner) settingViewList.get(4).findViewById(R.id.network_setting_check_result_num);
		Spinner checkResultPic = (Spinner) settingViewList.get(4).findViewById(R.id.network_setting_check_result_pic);
		Spinner checkResultRecord = (Spinner) settingViewList.get(4).findViewById(R.id.network_setting_check_result_record);
		Spinner tempCheckData = (Spinner) settingViewList.get(4).findViewById(R.id.network_setting_temp_check_data);
		Spinner taskData = (Spinner) settingViewList.get(4).findViewById(R.id.network_setting_task_data);
		 
		Button updateBtn = (Button) settingViewList.get(5).findViewById(R.id.network_setting_update);
	}


	private void networkTabviewInit(LayoutInflater inflater) {
		tabHost = (TabHost)networkView.findViewById(R.id.tabhost);  
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost  
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("上传")  
                .setContent(  
                R.id.view1));  
  
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("下载")  
                .setContent(R.id.view3));  
  
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("设置")  
                .setContent(R.id.view2)); 
        
        viewPager = (ViewPager)networkView.findViewById(R.id.vPager); 
        listViews = new ArrayList<View>();
        listViews.add(inflater.inflate(R.layout.network_upload_layout, null));
        listViews.add(inflater.inflate(R.layout.network_download_layout, null));
        listViews.add(inflater.inflate(R.layout.network_setting_layout, null));
        NetworkViewPagerAdapter networkViewPagerAdapter = new NetworkViewPagerAdapter(listViews);
        viewPager.setAdapter(networkViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){  
            @Override  
            public void onTabChanged(String tabId){  
                MLog.Logi("DownLoadFragment--tabId--=", tabId);  
                if(tabId.equals("tab1")) viewPager.setCurrentItem(0);
                if(tabId.equals("tab2")) viewPager.setCurrentItem(2);
                if(tabId.equals("tab3")) viewPager.setCurrentItem(1);
            }  
        });
	}


	private void uploadInit() {
		// TODO Auto-generated method stub
		final EditText[] ipEditText = new EditText[4];
		ipEditText[0] = (EditText) listViews.get(0).findViewById(R.id.network_upload_ip_1);
		ipEditText[1] = (EditText) listViews.get(0).findViewById(R.id.network_upload_ip_2);
		ipEditText[2] = (EditText) listViews.get(0).findViewById(R.id.network_upload_ip_3);
		ipEditText[3] = (EditText) listViews.get(0).findViewById(R.id.network_upload_ip_4);
		ipEditText[0].setInputType(EditorInfo.TYPE_CLASS_PHONE);  
		ipEditText[1].setInputType(EditorInfo.TYPE_CLASS_PHONE);  
		ipEditText[2].setInputType(EditorInfo.TYPE_CLASS_PHONE);  
		ipEditText[3].setInputType(EditorInfo.TYPE_CLASS_PHONE);  
		
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(DownLoadFragment.this.getActivity());
        String StrIP=settings.getString(CommonDef.APP_Settings.ServiceIP, Config.getServiceIP());
        String []StrList=StrIP.split("\\.");
        for(int i=0;i<StrList.length;i++){
        	ipEditText[i].setText(StrList[i]);	
        }
		Button SaveBT = (Button) listViews.get(0).findViewById(R.id.button1);
		SaveBT.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(IpIsValide(ipEditText[0].getText().toString())&&
						IpIsValide(ipEditText[1].getText().toString())&&
						IpIsValide(ipEditText[2].getText().toString())&&
						IpIsValide(ipEditText[3].getText().toString())){
					
					SharedPreferences mSharedPreferences= PreferenceManager.getDefaultSharedPreferences(DownLoadFragment.this.getActivity());		
					
					SharedPreferences.Editor editor = mSharedPreferences.edit();
					
					editor.putString(CommonDef.APP_Settings.ServiceIP,
							ipEditText[0].getText().toString()+"."+
									ipEditText[1].getText().toString()+"."+
									ipEditText[2].getText().toString()+"."+
									ipEditText[3].getText().toString());
					editor.commit();
					
					Toast.makeText(DownLoadFragment.this.getActivity(), "IP地址保存成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(DownLoadFragment.this.getActivity(), "请输入正确的IP地址", Toast.LENGTH_LONG).show();
				}
						
				
			}});
		
		CheckBox normalData = (CheckBox) listViews.get(0).findViewById(R.id.network_upload_data_type_cb1);
		CheckBox specialData = (CheckBox) listViews.get(0).findViewById(R.id.network_upload_data_type_cb2);
		CheckBox tempData = (CheckBox) listViews.get(0).findViewById(R.id.network_upload_data_type_cb3);
		CheckBox taskData = (CheckBox) listViews.get(0).findViewById(R.id.network_upload_data_type_cb4);
		CheckBox deviceInfo = (CheckBox) listViews.get(0).findViewById(R.id.network_upload_data_type_cb5);
		
		Switch uploadTypeSwitch = (Switch) listViews.get(0).findViewById(R.id.network_upload_type_switch);
		final RadioButton uploadType_wifi = (RadioButton) listViews.get(0).findViewById(R.id.network_upload_type_rb1);
		final RadioButton uploadType_usb = (RadioButton) listViews.get(0).findViewById(R.id.network_upload_type_rb2);
		final RadioButton uploadType_both = (RadioButton) listViews.get(0).findViewById(R.id.network_upload_type_rb3);
		final Button uploadBtn = (Button) listViews.get(0).findViewById(R.id.network_upload_button);
		uploadTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) {
					uploadType_wifi.setVisibility(View.GONE);
					uploadType_usb.setVisibility(View.GONE);
					uploadType_both.setVisibility(View.GONE);
					uploadBtn.setVisibility(View.GONE);
				} else {
					uploadType_wifi.setVisibility(View.VISIBLE);
					uploadType_usb.setVisibility(View.VISIBLE);
					uploadType_both.setVisibility(View.VISIBLE);
					uploadBtn.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	private void downloadInit() {
		Button searchBtn = (Button) listViews.get(1).findViewById(R.id.network_download_searchbtn);
		final TextView mDown_Button = (TextView)listViews.get(1).findViewById(R.id.network_download_button);
        mDown_Button.setOnClickListener(this);
		Switch downloadTypeSwitch = (Switch) listViews.get(1).findViewById(R.id.network_download_typeswitch);
		downloadTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) mDown_Button.setVisibility(View.GONE);
				else mDown_Button.setVisibility(View.VISIBLE);
			}
		});
		mDownLoadListView=(ListView) listViews.get(1).findViewById(R.id.hasdllistview);
		initAllDownLoadRoutList();
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
		case R.id.network_download_button:
			Event.QueryCommand_Event(arg0,getActivity(),mHandler);
			break;
		case R.id.network_setting_device_name:
			showReNameDialog();
			break;

		default:
			break;
		}
	}
	
	enum WifiMacType{
		MAcAddr,WifiAddr
	}

	private String getAddress(WifiMacType type){
		WifiManager wifiMan = (WifiManager) this.getActivity().getSystemService(Context.WIFI_SERVICE);  
		WifiInfo info = wifiMan.getConnectionInfo(); 
		if(type == WifiMacType.MAcAddr){
			//return info.getMacAddress();
			return "F9-2C-15-00-12-FC";
		}
		
		if(type == WifiMacType.WifiAddr){
			int ipAddress = info.getIpAddress();  
			if (ipAddress != 0) {  
				return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."   
			        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));  
			}  
		}
		
		return null;
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
	
	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			tabHost.setCurrentTab(arg0);
		}
		
	}
	
	private void showReNameDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("本机名称");
		View renameDialog = mInflater.inflate(R.layout.dialog_rename_layout, null);
		final EditText DevRename = (EditText) renameDialog.findViewById(R.id.bluetooth_rename);
		DevRename.setText(devName.getText().toString());
		builder.setView(renameDialog);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				devName.setText(DevRename.getEditableText().toString());
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.show();
		//window.setContentView(R.layout.dialog_rename_layout);
	}
	
	
	
	boolean IpIsValide(String str){
		if(str.length()==0)return false;
		if(Integer.valueOf(str)>=0 &&Integer.valueOf(str)<256 ){
			return true;
		}
		return false;
	}
}
