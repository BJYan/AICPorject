
package com.aic.aicdetactor.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.Interface.OnButtonListener;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.adapter.SpinnerAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 主题：根据Item_def 遍历PartItem数组中的PartItemData，一项一项的巡检，并按照格式保存数据及其一些flag。
 * 首先是根据每一个PartItemData的类型，重新布局不同的UI，不同的UI对应的功能按钮数量也不一样。再巡检，保存。
 * @author Administrator
 *
 */
public class PartItemListSelectActivity extends CommonActivity implements OnClickListener{

	private ListView mListView = null;
	String TAG = "luotest.PartItemActivity";
	private Spinner mSpinner = null;
	
	private ArrayAdapter<String> spinnerAdapter;
	private CheckBox mCheckbox = null;
	private int mStationIndex =0;
	private int mDeviceIndex = 0;
	TextView deviceTextView;
	//设置颜色级别
	public myApplication app = null;
	//根据Spinner的Index 设置mItemDefIndex
	private Button mConfigButton;
	private PartItemListAdapter mAdapterList =null;
	private List<String> mStatusList = new ArrayList<String>();
	
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	
	LinearLayout mParamsLineLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
//		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.partitemselect);
		fragmentManager = getFragmentManager();
		initViewAndData();
		
	}

	void initViewAndData() {
		app = ((myApplication) getApplication());
		mStationIndex = app.mStationIndex;
		mDeviceIndex = app.getCurrentDeviceIndex();
		Log.d("atest", " atest mStationIndex =  " + mStationIndex
				+ ",mDeviceIndex=" + mDeviceIndex);
		// 计划巡检还是特别巡检
		TextView planNameTextView = (TextView) findViewById(R.id.routeName);
		planNameTextView.setText(app.getCategoryTitle());
		mParamsLineLayout = (LinearLayout)findViewById(R.id.paramsL);
		// 路线名称
		TextView RouteNameTextView = (TextView) findViewById(R.id.routeName);
		RouteNameTextView.setText(RouteNameTextView.getText().toString()+app.mJugmentListParms.get(app.getCurrentRouteIndex()).T_Line.Name);
		setActionBar(app.getCategoryTitle()+" "+app.mJugmentListParms.get(app.getCurrentRouteIndex()).T_Line.Name, true);
		// 站点名称
		TextView stationTextView = (TextView) findViewById(R.id.stationName);
		stationTextView.setText(stationTextView.getText().toString()+app.mLineJsonData.StationInfo
				.get(mStationIndex).Name);
		// 设备名称
		deviceTextView = (TextView) findViewById(R.id.deviceName);
		
		mCheckbox = (CheckBox) findViewById(R.id.checkorder);
		mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				isREvertCheck = arg1;
//				Message msg =mHandler.obtainMessage(MSG_CHANGE_LISTVIEWDATA);
//				msg.arg1=isReverseDetection==true?1:0;
				mHandler.sendEmptyMessage(MSG_CHANGE_LISTVIEWDATA);
				
			}
			
		});
		mSpinner = (Spinner) findViewById(R.id.checkspinner);
		mListView = (ListView) findViewById(R.id.partitemlist);
	
		
		initSpinnerAdapterData();

		// 返回图标
		ImageView imageView = (ImageView) findViewById(R.id.backImage);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MLog.Logd(TAG, "imageView.setOnClickListener");
				// TODO Auto-generated method stub
				finish();
			}

		});

		mConfigButton = (Button) findViewById(R.id.config);
		mConfigButton.setOnClickListener(this);
	}
	
	void initSpinnerAdapterData(){
		mAdapterList = new PartItemListAdapter(PartItemListSelectActivity.this,mStationIndex,mDeviceIndex,mHandler);
		mListView.setAdapter(mAdapterList);
		getDeviceStatusArray(mStationIndex, mDeviceIndex);
		spinnerAdapter = new SpinnerAdapter(this, mStatusList);
		mSpinner.setAdapter(spinnerAdapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			
				String str=mSpinner.getSelectedItem().toString();
			
				mCheckbox.setChecked(false);
				if(mSpinner.getSelectedItemPosition()>0){
					Message msg =mHandler.obtainMessage(MSG_CHANGE_LISTVIEWDATAEX);
					msg.arg1=mSpinner.getSelectedItemPosition();
					msg.obj=mSpinner.getSelectedItem().toString();
					mHandler.sendMessage(msg);
				}else{
					mAdapterList.initListViewAndData(true);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}
	
	 private final int MSG_START =0;
	   private final int MSG_INIT_FRAGMENT =MSG_START+8;
	   private final int MSG_CHANGE_LISTVIEWDATA =MSG_START+9;
	   private final int MSG_CHANGE_LISTVIEWDATAEX =MSG_START+10;
	   private final int MSG_Dalay_Finish =MSG_START+13;

   Handler mHandler = new Handler(){
	   @Override
	    public void handleMessage(Message msg) {
		   switch(msg.what){
		   case MSG_INIT_FRAGMENT:
			   if(msg.arg1==1){
				   mSpinner.setSelection(0);
				   initSpinnerAdapterData();
				  if( !mAdapterList.gotoNextDeviceItem()){
					  mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_Dalay_Finish), 3000);
					  return;
				  }else{
				  }
			   }
			  
			   break;
		   case MSG_Dalay_Finish:
			   finish();
			   break;
		  
		   case MSG_CHANGE_LISTVIEWDATA:
			   revertPartItemDataList();
			   break;
		   case MSG_CHANGE_LISTVIEWDATAEX:
			   regetDataByStatusArrayIndex(mSpinner.getSelectedItemPosition()-1,msg.obj);
			   break;
		   }
	   }
   };
   
   void revertPartItemDataList(){
	   mAdapterList.revertListViewData(); 
   }
   
   void regetDataByStatusArrayIndex(int index,Object itemdef){
	   mAdapterList.getNewPartItemListDataByStatusArray(index,(String)itemdef); 
	   strItemDefine= (String)itemdef;
   }
   String strItemDefine="";
   final int CAMERA_TYPE =1;
   final int RF_TYPE =0;
   final int AUDIO_TYPE =2;
   final int TEXT_RECORD_TYPE =3;
   final int TEXT_ZHOU_TYPE =4;//轴数据
	
	String mCheckValue = "测量数值";
	
	
	
	
	BluetoothLeControl BLEControl = null;
boolean isREvertCheck=false;
    @Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.config:
			 if( mAdapterList.getCount()>0){
				 if(mSpinner.getSelectedItemPosition()>0){
					 BLEControl = BluetoothLeControl.getInstance(this);
					 if(app.mCurLinkedBLEAddress!=null &&app.mCurLinkedBLEAddress.length()<2 &&!app.mBLEIsConnected ){
						 Toast.makeText(this, "请在应用蓝牙界面手动连接", Toast.LENGTH_LONG).show();
						 }else{
							 BLEControl.Connection(app.mCurLinkedBLEAddress);
							 }
					    Intent intent = new Intent();
						intent.setClass(this, PartItemActivity.class);
						intent.putExtra("ItemDefine", strItemDefine);
						intent.putExtra("IsRevertCheck", isREvertCheck);
						if(mAdapterList.isLastDevice(mDeviceIndex)){
							intent.putExtra("lastDevice", true);
							startActivity(intent); 
							finish();
						}else{
							intent.putExtra("lastDevice", false);
							startActivityForResult(intent,PartItemContact.PARTITEM_ISNOT_LASTDEVICE_RESULT); 
						}
						
				 }else{
					Toast.makeText(getApplicationContext(), "请选择状态", Toast.LENGTH_LONG).show();
				}
			  }else {
					  Toast.makeText(getApplicationContext(), "设备下没有巡检项", Toast.LENGTH_LONG).show();
			   }
			break;		
		}
	}
	
	
	

	PopupWindow pw_Left = null;
	
	@SuppressLint("NewApi")
	void initPopupWindowFliter(View parent) {
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View rootview = inflater.inflate(
				R.layout.digit, null, false);

		 pw_Left = new PopupWindow(rootview, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		// // 显示popupWindow对话框
		pw_Left.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
					pw_Left.dismiss();
					return true;
				}
				return false;
			}

		});
		ColorDrawable dw = new ColorDrawable(Color.GRAY);
		pw_Left.setBackgroundDrawable(dw);
		pw_Left.setOutsideTouchable(true);
		int []location= new int[2];
		parent.getLocationOnScreen(location);
		pw_Left.showAsDropDown(parent, Gravity.NO_GRAVITY, location[0],location[1]-pw_Left.getHeight());
		pw_Left.update();

	}
		
	
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCheckbox.setChecked(false);
		deviceTextView
		.setText(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem
				.get(mDeviceIndex).Name);
	}

	@Override  
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == PartItemContact.PARTITEM_ISNOT_LASTDEVICE_RESULT) {
				if(data!=null &&data.getBooleanExtra(CommonDef.GotoNextDevice, false)){
				app.setCurrentDeviceIndex(++mDeviceIndex);
				initSpinnerAdapterData();  
				}
			}
		}  
	 
	public List<String>getDeviceStatusArray(int station,int device){
		mStatusList.clear();
		String str="";
		boolean bFind = false;
		for (int i = 0; i < app.mLineJsonData.StationInfo.size(); i++) {
			if(bFind) break;
			try {
				for (int deviceIndex = 0; deviceIndex < app.mLineJsonData.StationInfo.get(i).DeviceItem.size(); deviceIndex++) {
					if(i == station && deviceIndex == device){
						str =app.mLineJsonData.StationInfo.get(i).DeviceItem.get(deviceIndex).Status_Array;
						bFind=true;
						break;
						}
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		mStatusList.add("请选择状态");
		String[]array = str.split("\\/");
		for(int k=0;k<array.length;k++){
			mStatusList.add(array[k]);
		}
		return mStatusList;
		
	}
   
}
