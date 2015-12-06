package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.List;

import network.com.citizensoft.networkdemo.MainActivity;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.Config.Config;
import com.aic.aicdetactor.Event.Event;
import com.aic.aicdetactor.adapter.NetWorkSettingAdapter;
import com.aic.aicdetactor.adapter.NetworkViewPagerAdapter;
import com.aic.aicdetactor.adapter.SpinnerAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.database.TemporaryRouteDao;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.CommonDialogBtnListener;
import com.aic.aicdetactor.media.NotepadActivity;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
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
					CommonAlterDialog dialog = new CommonAlterDialog(DownLoadFragment.this.getActivity(),"提示","没本地巡检数据，请确认有/sdcard/AICLine.txt文件",null,null);
					dialog.show();
					break;
				case  Event.LocalData_Init_Success:
					
					Toast.makeText(getActivity().getApplicationContext(),(String)(msg.obj),
							Toast.LENGTH_SHORT).show();
					mDLLineAdapter.setListData(getDownLoadRouteInfo());
					mDLLineAdapter.notifyDataSetChanged();
					break;
				case  Event.UpdateRouteLine_Message:
					showOKCancel((String)msg.obj);
					
					break;
				case Event.UpdateRouteLineData_Message:
					String str = (String)msg.obj;
					String []ParamsStr=str.split("\\*");
					SystemUtil.renameFile(ParamsStr[0],ParamsStr[0].substring(0, ParamsStr[0].length()-4));
					myApplication app = myApplication.getApplication();
					RouteDao dao = RouteDao.getInstance(app.getApplicationContext());
					 String StrSql = "update "+DBHelper.TABLE_SOURCE_FILE
								+" set "+DBHelper.SourceTable.DownLoadDate+"="+SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDD2) 
								+" ,  "+DBHelper.SourceTable.Checked_Count+"='0'"
								+",  "+DBHelper.SourceTable.NormalItemCounts+"='"+ParamsStr[1]
								+"',  "+DBHelper.SourceTable.SPecialItemCounts+"='"+ParamsStr[2]									
								+"' where "+DBHelper.SourceTable.PLANGUID +" is '"+ParamsStr[3]+"'";
						
						dao.execSQLUpdate(StrSql);
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}};
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
					
					 Message msg = mHandler.obtainMessage(Event.UpdateRouteLineData_Message);
					 msg.obj=str;
					 mHandler.sendMessage(msg);
					 dialog.dismiss();
				}

				@Override
				public void onClickBtn1Listener(CommonDialog dialog) {
					// TODO Auto-generated method stub
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
		//View view = inflater.inflate(R.layout.downup_fragment, container, false);
		networkView = inflater.inflate(R.layout.network_fragment, container, false);
        networkTabviewInit(inflater);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		
		uploadInit();
		downloadInit();   
		settingViewinit();
		/*
		mSetting_linearLayout = (LinearLayout)listViews.get(1).findViewById(R.id.setting_linear);
		mUp_linearLayout = (LinearLayout)listViews.get(0).findViewById(R.id.up_linear);
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
		initViewValue();*/
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
									ipEditText[3].getText().toString()+".");
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
		ListView listView=(ListView) listViews.get(1).findViewById(R.id.hasdllistview);
		mDLLineAdapter = new SpinnerAdapter(this.getActivity().getApplicationContext(),getDownLoadRouteInfo());
//		listView.setAdapter(new ArrayAdapter<String>(this.getActivity(),
//                android.R.layout.simple_list_item_1, getDownLoadRouteInfo()));
		listView.setAdapter(mDLLineAdapter);
		listView.setClickable(false);
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
	
	List<String> getDownLoadRouteInfo(){
		RouteDao dao = RouteDao.getInstance(getActivity());
		String SqlStr="select * from "+DBHelper.TABLE_SOURCE_FILE;
		Cursor cursor = dao.execSQL(SqlStr);
		List<String> list= new ArrayList<String>();
		try{
			while(cursor!=null &&cursor.moveToNext()){
				String str =cursor.getString(cursor.getColumnIndex(DBHelper.SourceTable.PLANNAME))+"  "+cursor.getString(cursor.getColumnIndex(DBHelper.SourceTable.DownLoadDate));
				list.add(str);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		SqlStr = "select * from "+DBHelper.TABLE_TEMPORARY;
		
		try{
			cursor = dao.execSQL(SqlStr);
			while(cursor!=null &&cursor.moveToNext()){
				String str =cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Title));
				list.add(str);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		return list;
	}
	
	boolean IpIsValide(String str){
		if(str.length()==0)return false;
		if(Integer.valueOf(str)>=0 &&Integer.valueOf(str)<256 ){
			return true;
		}
		return false;
	}
}
