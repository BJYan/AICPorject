
package com.aic.aicdetactor.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.micode.soundrecorder.SoundRecorder;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.Event.Event;
import com.aic.aicdetactor.Interface.OnButtonListener;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.adapter.SpinnerAdapter;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.condition.ConditionalJudgement;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.CommonDialogBtnListener;
import com.aic.aicdetactor.fragment.BlueTooth_Fragment;
import com.aic.aicdetactor.fragment.MeasureBaseFragment;
import com.aic.aicdetactor.fragment.MeasureDefaltStateFragment;
import com.aic.aicdetactor.fragment.MeasureDefaltStateFragment.OnMeasureMeasureListener;
import com.aic.aicdetactor.fragment.MeasureEnterReadFragment;
import com.aic.aicdetactor.fragment.MeasureEnterReadFragment.OnEnterReadListener;
import com.aic.aicdetactor.fragment.MeasureObserverFragment;
import com.aic.aicdetactor.fragment.MeasureObserverFragment.OnMediakListener;
import com.aic.aicdetactor.fragment.MeasureTemperatureFragment;
import com.aic.aicdetactor.fragment.MeasureTemperatureFragment.OnTemperatureMeasureListener;
import com.aic.aicdetactor.fragment.MeasureVibrateFragment;
import com.aic.aicdetactor.fragment.MeasureVibrateFragment.OnVibateListener;
import com.aic.aicdetactor.media.SoundRecordActivity;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 主题：根据Item_def 遍历PartItem数组中的PartItemData，一项一项的巡检，并按照格式保存数据及其一些flag。
 * 首先是根据每一个PartItemData的类型，重新布局不同的UI，不同的UI对应的功能按钮数量也不一样。再巡检，保存。
 * @author Administrator
 *
 */
public class PartItemActivity extends CommonActivity implements OnClickListener,
    OnVibateListener,
	OnMediakListener,
	OnTemperatureMeasureListener,
	OnMeasureMeasureListener,
	OnEnterReadListener{

	String TAG = "luotest.PartItemActivity";
	private TextView mItemDefTextView = null;//当只有一项时才显示
	
	private boolean mIsChecking = false;
	private int mStationIndex =0;
	private int mDeviceIndex = 0;
	private int mCheckIndex =0;
	private int mPartItemIndex =0;
	
	//点击listItem后 ListView 视图消失，显示具体测试点界面
	private LinearLayout mUnitcheck_Vibrate = null;
	private Button mButton_Next = null;
	private Button mButton_Pre = null;
	private Button mButton_Measurement = null;
	private LinearLayout LinearLayout_y = null;
	private LinearLayout LinearLayout_z = null;
	private TextView mTextViewX = null;
	private TextView mTextViewY = null;
	private TextView mTextViewZ = null;
	private TextView mTextViewCountDown = null;
	//测试倒计时，用于待信号稳定
	private final int mMaxSecond_StartMeasure = 30;
	private static final int  REMOVELASTFRAGMENT =10086;
	private int mCountDown =mMaxSecond_StartMeasure;
	//设置颜色级别
	private RadioButton mRadioButton = null;
	private Fragment mfragment = null;
	public myApplication app = null;
	//根据Spinner的Index 设置mItemDefIndex
	private int mItemDefIndex =0;
	private OnButtonListener mFragmentCallBack = null;
	private String mFirstStartTime="";
	private String mLastEndTime="";
	private MeasureBaseFragment fragment  =null;
	private ArrayList<PartItemJsonUp> mPartItemList=null;
	private ArrayList<PartItemJsonUp> mOriPartItemList=null;//原始的数据
	private ArrayList<String> mPartItemNameList=null;
	private PartItemListAdapter mAdapterList =null;
	private List<String> mStatusList = new ArrayList<String>();
	
	private List<Fragment> fragmentsList;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	
	LinearLayout mParamsLineLayout;
	Spinner mCaiYangDianSpinner;
	Spinner mCaiYangPinLvSpinner;
	int CaiYangDianIndex=0;
	int CaiYangPinLvIndex=0;
	String[] mCaiyangPinLvDataList=null;
	String[] mCaiyangDianshuDataList=null;
	String StrSelectedItemDefine="";
	boolean isLastDevice=false;
	boolean isRevertCheck=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
//		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.partitemactivity);
		StrSelectedItemDefine=this.getIntent().getStringExtra("ItemDefine");
		isLastDevice=this.getIntent().getBooleanExtra("lastDevice",false);
		isRevertCheck = this.getIntent().getBooleanExtra("IsRevertCheck",false); 
		mPartItemIndex =0;
		fragmentsList = new ArrayList<Fragment>();
		fragmentManager = getFragmentManager();
		initViewAndData();
		mAdapterList.getNewPartItemListDataByStatusArray(app.mDeviceIndex,StrSelectedItemDefine); 
		
		if(isRevertCheck){
			mAdapterList.revertListViewData();
		}
		
		switchFragment(mAdapterList.getCurrentPartItemType(),true);
	}

	void initViewAndData() {
		app = ((myApplication) getApplication());
		mStationIndex = app.mStationIndex;
		mDeviceIndex = app.mDeviceIndex;
		Log.d("atest", " atest mStationIndex =  " + mStationIndex
				+ ",mDeviceIndex=" + mDeviceIndex);
		// 计划巡检还是特别巡检
		TextView planNameTextView = (TextView) findViewById(R.id.routeName);
		planNameTextView.setText(app.getCategoryTitle());
		mParamsLineLayout = (LinearLayout)findViewById(R.id.paramsL);
		mCaiYangDianSpinner =(Spinner)findViewById(R.id.dianshu);
		mCaiYangPinLvSpinner =(Spinner)findViewById(R.id.pinlv);
		Resources res=getResources();
		mCaiyangDianshuDataList=res.getStringArray(R.array.CaiYangDianShu);
		mCaiyangPinLvDataList=res.getStringArray(R.array.CaiYangPinLv_Value);
		ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mCaiyangDianshuDataList);    
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        mCaiYangDianSpinner.setAdapter(adapter);    
	        mCaiYangDianSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
	            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
	                // TODO Auto-generated method stub    
	            	CaiYangDianIndex=Integer.valueOf(mCaiyangDianshuDataList[mCaiYangDianSpinner.getSelectedItemPosition()]);
	            }    
	            public void onNothingSelected(AdapterView<?> arg0) {    
	                // TODO Auto-generated method stub    
	            }    
	        });    
	        
	        mCaiYangDianSpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
	        public void onFocusChange(View v, boolean hasFocus) {    
	            // TODO Auto-generated method stub    
	  
	        }    
	        });    
	        mCaiYangDianSpinner.setSelection(2);
	        
	        String[] pinlv=res.getStringArray(R.array.CaiYangDianShu);
			ArrayAdapter PLadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, pinlv);    
			PLadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		        mCaiYangPinLvSpinner.setAdapter(adapter);    
		        mCaiYangPinLvSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
		            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
		                // TODO Auto-generated method stub    
		            	CaiYangPinLvIndex=Integer.valueOf(mCaiyangPinLvDataList[mCaiYangPinLvSpinner.getSelectedItemPosition()]);
		            }    
		            public void onNothingSelected(AdapterView<?> arg0) {    
		                // TODO Auto-generated method stub    
		            }    
		        });    
		        /*下拉菜单弹出的内容选项焦点改变事件处理*/    
		        mCaiYangPinLvSpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
		        public void onFocusChange(View v, boolean hasFocus) {    
		            // TODO Auto-generated method stub    
		  
		        }    
		        });  
		        mCaiYangPinLvSpinner.setSelection(3);
		// 路线名称
		TextView RouteNameTextView = (TextView) findViewById(R.id.routeName);
		RouteNameTextView.setText(RouteNameTextView.getText().toString()+app.mJugmentListParms.get(app.getCurrentRouteIndex()).T_Line.Name);
		setActionBar(app.getCategoryTitle()+" "+app.mJugmentListParms.get(app.getCurrentRouteIndex()).T_Line.Name, true);
		// 站点名称
		TextView stationTextView = (TextView) findViewById(R.id.stationName);
		stationTextView.setText(stationTextView.getText().toString()+app.mLineJsonData.StationInfo
				.get(mStationIndex).Name);
		// 设备名称
		TextView deviceTextView = (TextView) findViewById(R.id.deviceName);
		deviceTextView
				.setText(deviceTextView.getText().toString()+app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem
						.get(mDeviceIndex).Name);
		
		mAdapterList = new PartItemListAdapter(PartItemActivity.this,mStationIndex,mDeviceIndex,mHandler);
		

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
		mButton_Next = (Button) findViewById(R.id.next);
		mButton_Next.setOnClickListener(this);

		mButton_Pre = (Button) findViewById(R.id.bottombutton_pre);
		mButton_Pre.setOnClickListener(this);

		mButton_Measurement = (Button) findViewById(R.id.bottombutton3);
		mButton_Measurement.setOnClickListener(this);
	}
	
	//根据不同的测量类型，显示不同的UI界面。
	void switchFragment(int type,boolean bFirstInit){
		
		fragmentTransaction = fragmentManager.beginTransaction();
		Bundle bundle = new Bundle(); 
		bundle.putInt("partItemIndex", mPartItemIndex);
		bundle.putInt("type", type);
		mParamsLineLayout.setVisibility(View.GONE);
		  switch(type){
		   case CommonDef.checkUnit_Type.ACCELERATION:
		   case CommonDef.checkUnit_Type.SPEED:	
		   case CommonDef.checkUnit_Type.DISPLACEMENT:
			  // 需要方向
			   mParamsLineLayout.setVisibility(View.VISIBLE);
			   {
					fragment = new MeasureVibrateFragment(mAdapterList);
					
					fragment.setArguments(bundle);  
					if(bFirstInit){
					fragmentTransaction.add(R.id.fragment_content,fragment);
					}else{
						fragmentTransaction.replace(R.id.fragment_content,fragment);
					}
					fragmentTransaction.commit();
					}
			   fragmentsList.add(fragment);
			   break;
		   case CommonDef.checkUnit_Type.TEMPERATURE:
		   case CommonDef.checkUnit_Type.ROTATION_RATE:
		  
			   {
					fragment = new MeasureTemperatureFragment(mAdapterList);
					fragment.setArguments(bundle);  
					if(bFirstInit){
					fragmentTransaction.add(R.id.fragment_content,fragment);
					}else{
						fragmentTransaction.replace(R.id.fragment_content,fragment);
					}					
					fragmentTransaction.commit();
					}
			   fragmentsList.add(fragment);
			   break;
		   case CommonDef.checkUnit_Type.METER_READING:
		   case CommonDef.checkUnit_Type.ENTERING:
		   {
				fragment = new MeasureEnterReadFragment(mAdapterList);
				fragment.setArguments(bundle);  
				if(bFirstInit){
				fragmentTransaction.add(R.id.fragment_content,fragment);
				}else{
					fragmentTransaction.replace(R.id.fragment_content,fragment);
				}					
				fragmentTransaction.commit();
				}
		   fragmentsList.add(fragment);
			   break;
		   case CommonDef.checkUnit_Type.OBSERVATION:
			   fragment = new MeasureObserverFragment(mAdapterList);
				fragment.setArguments(bundle);  
				if(bFirstInit){
				fragmentTransaction.add(R.id.fragment_content,fragment);
				}else{
					fragmentTransaction.replace(R.id.fragment_content,fragment);
				}
				fragmentTransaction.commit();
				fragmentsList.add(fragment);
			   break;
//		   case CommonDef.checkUnit_Type.ENTERING:
//		  
//		   {
//				fragment = new PartItemMeasureObserverFragment();
//				fragment.setArguments(bundle);  
//				if(bFirstInit){
//				fragmentTransaction.add(R.id.fragment_content,fragment);
//				}else{
//					fragmentTransaction.replace(R.id.fragment_content,fragment);
//				}
//				fragmentTransaction.commit();
//				
//				mButton_Measurement.setText(getString(R.string.textrecord));
//				}
//			   break;
		   case CommonDef.checkUnit_Type.STATE_PRESUPPOSITOIN:
		   {
			   fragment = new MeasureDefaltStateFragment(mAdapterList);
				fragment.setArguments(bundle);  
				if(bFirstInit){
				fragmentTransaction.add(R.id.fragment_content,fragment);
				}else{
					fragmentTransaction.replace(R.id.fragment_content,fragment);
				}
				fragmentTransaction.commit();
			   
		   }
		   fragmentsList.add(fragment);
		   break;
		   case REMOVELASTFRAGMENT:
			   fragmentTransaction.remove(fragment);
			   fragmentTransaction.commit();
			break;
			   default:
				   Toast.makeText(getApplicationContext(), "default Fragment type ="+type, Toast.LENGTH_LONG).show();
				   
				   break;
		  }
		  
		  controlButtonDisplayStatus(type);
	}
	
	enum Status{
		
	}
	 private final int MSG_START =0;
	   private final int MSG_NEXT =MSG_START+1;
	   private final int MSG_MEASUERMENT =MSG_START+2;
	   private final int MSG_DIRECTION =MSG_START+3;
	   private final int MSG_PRE =MSG_START+4;
	   private final int MSG_SAVE_PARTITEMDATA =MSG_START+5;
	   private final int MSG_ADD_NEW_PARTITEMDATA =MSG_START+7;
	   private final int MSG_INIT_FRAGMENT =MSG_START+8;
	   private final int MSG_CHANGE_LISTVIEWDATA =MSG_START+9;
	   private final int MSG_Dalay_Finish =MSG_START+13;
	   
	   private final int Status_Start=100;
	   private final int  Save_Current_Device_Data=Status_Start;
	   private final int goto_next_Device=Status_Start+1;
	   private final int Stay_Current_Device=Status_Start+2;
	  
   Handler mHandler = new Handler(){
	   @Override
	    public void handleMessage(Message msg) {
		   switch(msg.what){
		   case MSG_INIT_FRAGMENT:
			   if(msg.arg1==Save_Current_Device_Data){
				  if( !mAdapterList.gotoNextDeviceItem()){
					  mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_Dalay_Finish), 3000);
				  }else{
					 // "gotoNextDevice"
					  Intent intent = new Intent();
					  intent.putExtra(CommonDef.GotoNextDevice, true);
					  PartItemActivity.this.setResult(PartItemContact.PARTITEM_ISNOT_LASTDEVICE_RESULT,intent);
					  finish();
				  }
					 
			   }else{
				   switchFragment(mAdapterList.getCurrentPartItemType(),true);
			   }
			  
			   break;
		   case MSG_Dalay_Finish:
			   finish();
			   break;
		   case MSG_NEXT:
			  // if(!mIsChecking)
			   {
			   nextCheckItem();
			   }
			   break;
		   case MSG_MEASUERMENT:
			   mIsChecking=true;
			   getMeasureValue();
			   break;
		   case MSG_DIRECTION:
			   break;
		   case MSG_PRE:
			   preCheckItem();
			   break;
		   case MSG_SAVE_PARTITEMDATA:
			   saveValue();
			   mHandler.sendMessage(mHandler.obtainMessage(MSG_NEXT));
			   break;
		   case MSG_ADD_NEW_PARTITEMDATA:
			   break;
		   case MSG_CHANGE_LISTVIEWDATA:
			   revertPartItemDataList();
			   break;
		   case Event.LocalData_Init_Success:
				String strContentTips="";
				if(msg.arg1==1){
					//上传成功，需要更新数据表状态
					strContentTips="数据上传成功!";
				}else{
					strContentTips="数据上传异常,"+(String)msg.obj;
				}
				if(dialog!=null){
					dialog.dismiss();
				}
				dialog = new CommonAlterDialog(PartItemActivity.this,"提示",strContentTips,null,null);
				dialog.show();
				break;
				
		   }
	   }
   };
   CommonAlterDialog  dialog=null;
   void revertPartItemDataList(){
	   mAdapterList.revertListViewData(); 
   }
   
   @Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	   if(dialog!=null){
			dialog.dismiss();
		}
	super.onDestroy();
}

final int CAMERA_TYPE =1;
   final int RF_TYPE =0;
   final int AUDIO_TYPE =2;
   final int TEXT_RECORD_TYPE =3;
   final int TEXT_ZHOU_TYPE =4;//轴数据
	
	String mCheckValue = "测量数值";
	
	/**
	 * 向前巡检或查看前一项巡检结果，同样要先获取巡检类型，并对应显示出来UI.
	 */
	void preCheckItem(){	
		//显示数据
		if(mPartItemIndex==0){
			//Toast.makeText(this, "退出", Toast.LENGTH_LONG).show();
			finish();
		}
		mPartItemIndex = mAdapterList.getPrevPartItemIndex();
		switchFragment(mAdapterList.getCurrentPartItemType(), false);	
	}
	
	
	boolean isTimeOut(){
		return false;
	}
	

/**
    * 点击下一项时触发的VIEW变化，显示当前测试项的下一项，
    * 如果是设备最后一个测试项，保存，并切换到下一设备的第一个测试项。
    */
	private void nextCheckItem() {
		if (!isTimeOut()) {
			//判断是否是已经巡检完毕
			mPartItemIndex = mAdapterList.getNextPartItemIndex();			
			if (!mAdapterList.isLastPartItemInCurrentDeviceItem()) {
				//获取下一点的 数据类型并进行fragment 切换显示数据							
					switchFragment(mAdapterList.getCurrentPartItemType(), false);	
				if (mCheckIndex != 0 && !mButton_Pre.isEnabled()) {
					mButton_Pre.setEnabled(true);
				}
			}else {			
				ShowDialog();
			}
		} else {
			Toast.makeText(getApplicationContext(),	getString(R.string.time_out), Toast.LENGTH_LONG).show();
			this.finish();
		}

	}
	void ShowDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setMessage("数据已巡检完毕，是否要存盘吗？") 
		       .setCancelable(true) 
		       .setTitle("提示")
		       .setPositiveButton("确认", new DialogInterface.OnClickListener() { 
		           public void onClick(DialogInterface dialog, int id) { 
		        	   
		        		mPartItemIndex=0;						
		        	    mAdapterList.setFinishDeviceCheckFlagAndSaveDataToSD();
		        	   // mDeviceIndex++;
		        	   //fragment.saveDataToFile();
						Toast.makeText(getApplicationContext(),	"数据保存中", Toast.LENGTH_LONG).show();
						
						Message msg=new Message();
						msg.what = MSG_INIT_FRAGMENT;
						msg.arg1=Save_Current_Device_Data;//next Device
						mHandler.sendMessage(msg);
						
						dialog.dismiss();
		           } 
		       }) 
		       .setNegativeButton("取消", new DialogInterface.OnClickListener() { 
		           public void onClick(DialogInterface dialog, int id) { 
		                dialog.cancel(); 
		           } 
		       }); 
		builder.show();
	}
	
	
	private Uri mediaFilePath = null;
	BluetoothLeControl BLEControl = null;

   String[] direction_item={"X-Y","X-Z","Y-Z"};
    @Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bottombutton_pre://上一测试点
		{
			//首先是显示上一测试点的数据。
			mHandler.sendMessage(mHandler.obtainMessage(MSG_PRE));
		}
			break;

		case R.id.next://下一测试点
		{
			// 保存当前的device下的数据，并不是文件中的device，只是暂存，直到device下的partitem全部巡检完毕才真正的保存数据
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SAVE_PARTITEMDATA));
			
		}
			break;		
		case R.id.bottombutton3://测量
			//if(app.mBLEIsConnected){
				if(!app.isTest){
					if(app.isSpecialLine()){
						 mHandler.sendMessage(mHandler.obtainMessage(MSG_MEASUERMENT));
					}else{
						if(ConditionalJudgement.Is_NoTimeout(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid)){
							 mHandler.sendMessage(mHandler.obtainMessage(MSG_MEASUERMENT));
							}else {
								Toast.makeText(getApplicationContext(), "巡检已超时", Toast.LENGTH_LONG).show();
							}
					}
					
				}else{
					mHandler.sendMessage(mHandler.obtainMessage(MSG_MEASUERMENT));
				}
				
				mButton_Measurement.setEnabled(false);
//			}else{
//				Toast.makeText(this, "蓝牙未连接，请连接", Toast.LENGTH_LONG).show();
//			}
		//	mHandler.sendEmptyMessage(CommonDef.DISABLE_MEASUREMENT_BUTTON);	
			
			break;
		}
	}
	
    void getMeasureValue(){
    	//获取当前系统时间作为开始测量时间
		mFragmentCallBack.OnButtonDown(OnButtonListener.ModifyData, mAdapterList,"",PartItemContact.MEASURE_DATA,CaiYangDianIndex,CaiYangPinLvIndex);
    }
    
    void saveValue(){
		mFragmentCallBack.OnButtonDown(OnButtonListener.ModifyData, mAdapterList,"",PartItemContact.SAVE_DATA,0,0);
    }
    
    @Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		MLog.Logd("test", "onActivityResult() 00" + requestCode + ",resultCode= "
				+ resultCode);
		ParamsPartItemFragment params = new ParamsPartItemFragment();
		params.SaveLab=SystemUtil.createGUID();
		params.RecordLab=SystemUtil.createGUID();
		
		if (requestCode == PartItemContact.PARTITEM_CAMERA_RESULT) {
			mCheckValue = mediaFilePath.toString();
			params.Path=mediaFilePath.toString();
			params.TypeCode=OnButtonListener.PictureDataType;
			mFragmentCallBack.addNewMediaPartItem(params, mAdapterList);
		}

		if (PartItemContact.PARTITEM_SOUNDRECORD_RESULT == requestCode) {
			params.TypeCode=OnButtonListener.AudioDataType;
			params.Path=mediaFilePath.toString();
      		mFragmentCallBack.addNewMediaPartItem(params, mAdapterList);
		}

	}  
   
	@Override
	public void OnClick(String value) {
		// TODO Auto-generated method stub
		MLog.Logd(TAG,"OnClick()IndexButton = "+value);
		mCheckValue = value;
	} 
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		boolean res = fragment instanceof BlueTooth_Fragment;
		if(!res){
		try {
			mFragmentCallBack = (OnButtonListener) fragment;
		} catch (Exception e) {
			throw new ClassCastException(this.toString()+ " must OnButtonListener");
		}
		}
		super.onAttachFragment(fragment);
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
	public void OnClick(int genPartItemDataCounts, int xValue, int yValue,
			int zValue) {
		mHandler.sendEmptyMessage(genPartItemDataCounts);
	}
	
	void controlButtonDisplayStatus(int type){
		int iInvisibleOrGone=0;
		if(app.getScreenWidth()>700){
			iInvisibleOrGone=View.INVISIBLE;
		}else{
			iInvisibleOrGone=View.GONE;
		}
		switch(type){
		
		case CommonDef.checkUnit_Type.TEMPERATURE:
		case CommonDef.checkUnit_Type.ROTATION_RATE:
			mButton_Measurement.setVisibility(View.VISIBLE);
			mButton_Next.setVisibility(View.VISIBLE);
			
			break;
		case CommonDef.checkUnit_Type.ENTERING:			
		case CommonDef.checkUnit_Type.METER_READING:
		case CommonDef.checkUnit_Type.STATE_PRESUPPOSITOIN:
		case  CommonDef.checkUnit_Type.OBSERVATION:
			mButton_Measurement.setVisibility(iInvisibleOrGone);
			mButton_Next.setVisibility(View.VISIBLE);
			break;
		case CommonDef.checkUnit_Type.ACCELERATION:		
		case CommonDef.checkUnit_Type.SPEED:		
		case CommonDef.checkUnit_Type.DISPLACEMENT:
			mButton_Measurement.setVisibility(View.VISIBLE);
			break;
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
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        switch (keyCode) {  
   
        case KeyEvent.KEYCODE_VOLUME_DOWN:  
        	{
        		Log.d(TAG,"+++++++++KEYCODE_VOLUME_DOWN++++++");
        	
        	Intent intent_down = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
        	Setting setting = new Setting();
        	String path = setting.getData_Media_Director(CommonDef.FILE_TYPE_PICTRUE);
        	File outputImage = new File(path,SystemUtil.createGUID());
            try {
                if(outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
            
            mediaFilePath = Uri.fromFile(outputImage);
        	MLog.Logd("test","main_media imageFilePath is "+mediaFilePath);
        	intent_down.putExtra(MediaStore.EXTRA_OUTPUT, mediaFilePath); //这样就将文件的存储方式和uri指定到了Camera应用中  
        	startActivityForResult(intent_down, PartItemContact.PARTITEM_CAMERA_RESULT);  
        	}
            return true;  
   
        case KeyEvent.KEYCODE_VOLUME_UP:  
        {
        	Log.d(TAG,"+++++++++KEYCODE_VOLUME_UP++++++");
        	Intent intent_up = new Intent();
        	intent_up.setClass(PartItemActivity.this, SoundRecorder.class);
        	Setting setting = new Setting();
        	String path = setting.getData_Media_Director(CommonDef.FILE_TYPE_AUDIO);
        	File outputAudio = new File(path,SystemUtil.createGUID()+".audio");
            try {
                if(outputAudio.exists()) {
                	outputAudio.delete();
                }
                outputAudio.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
            mediaFilePath = Uri.fromFile(outputAudio);
            intent_up.putExtra(MediaStore.EXTRA_OUTPUT, mediaFilePath);
        	startActivityForResult(intent_up,PartItemContact.PARTITEM_SOUNDRECORD_RESULT);
        }
            return true;  
        case KeyEvent.KEYCODE_VOLUME_MUTE:  
        	Log.d(TAG,"+++++++++KEYCODE_VOLUME_MUTE++++++");
   
            return true;  
        case KeyEvent.KEYCODE_BACK:
        	Message msg = mHandler.obtainMessage(MSG_PRE);
        	mHandler.sendMessage(msg);
        	//Toast.makeText(this, "Back 退出", Toast.LENGTH_LONG).show();
        	return true;
        }  
        return super.onKeyDown(keyCode, event);  
    }  
    
    void ShueDialog(){
		 CommonDialog acceleChart = new CommonDialog(this);
			acceleChart.setTitle("警告");
			acceleChart.setContent("确认要退出当前设备巡检吗？如退出，当前设备下的已巡检项结果丢弃。");
			acceleChart.setCloseBtnVisibility(View.VISIBLE);
			acceleChart.setButtomBtn(new CommonDialogBtnListener() {

				@Override
				public void onClickBtn2Listener(CommonDialog dialog) {
					// TODO Auto-generated method stub
					mAdapterList=null;
					 dialog.dismiss();
				}

				@Override
				public void onClickBtn1Listener(CommonDialog dialog) {
					// TODO Auto-generated method stub
					//delete temp file
					dialog.dismiss();
				}
			}, "退出", "取消");
			acceleChart.show();
	}
   
}
