
package com.aic.aicdetactor.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.PartItem_Contact;
import com.aic.aicdetactor.data.AuxiliaryInfoNode;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.data.PartItemItem;
import com.aic.aicdetactor.data.T_Device_Item;
import com.aic.aicdetactor.fragment.Observer_fragment;
import com.aic.aicdetactor.fragment.Observer_fragment.OnMediakListener;
import com.aic.aicdetactor.fragment.Temperature_fragment;
import com.aic.aicdetactor.fragment.Temperature_fragment.OnTemperatureMeasureListener;
import com.aic.aicdetactor.fragment.Vibrate_fragment;
import com.aic.aicdetactor.fragment.Vibrate_fragment.OnVibateListener;
import com.aic.aicdetactor.fragment.measurement_fragment;
import com.aic.aicdetactor.fragment.measurement_fragment.OnMeasureMeasureListener;
import com.aic.aicdetactor.media.NotepadActivity;
import com.aic.aicdetactor.media.SoundRecordActivity;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.google.gson.Gson;

/**
 * 主题：根据Item_def 遍历PartItem数组中的PartItemData，一项一项的巡检，并按照格式保存数据及其一些flag。
 * 首先是根据每一个PartItemData的类型，重新布局不同的UI，不同的UI对应的功能按钮数量也不一样。再巡检，保存。
 * @author Administrator
 *
 */
public class PartItemActivity extends FragmentActivity implements OnClickListener,
    OnVibateListener,
	OnMediakListener,
	OnTemperatureMeasureListener,
	OnMeasureMeasureListener{

	//private ListView mListView = null;
	String TAG = "luotest";
//	private Spinner mSpinner = null;
	private TextView mItemDefTextView = null;//当只有一项时才显示
	private String mCheckItemNameStr = null;//检查项名
	private String mCheckUnitNameStr = null;//检查部件名称
	
	private ArrayAdapter<String> spinnerAdapter;
	private int mLastSpinnerIndex = 0;
	private Object mPartItemObject = null;
	private Object mCurrentDeviceItem = null;
//	private List<Map<String, Object>> mMapList;
	public final int SPINNER_SELECTCHANGED =0;
	private SimpleAdapter mListViewAdapter = null;
	
	//partItem 数组，包含Fast_Record_Item_Name 及 PartItemData : 
	private List<Object> mPartItemList=null;
	private CheckBox mCheckbox = null;
	private int mRouteIndex =0;
	private int mStationIndex =0;
	private int mDeviceIndex = 0;
	private int mCheckIndex =0;
	private int mPartItemIndex =0;
	//是否需要反向排序来巡检
	private boolean isReverseDetection = false;
	//点击listItem后 ListView 视图消失，显示具体测试点界面
	private boolean bListViewVisible = true;
	private boolean bSpinnerVisible = true;
	private LinearLayout mUnitcheck_Vibrate = null;
	private int mCheckUnit_DataType = 0;
	private Button mButton_Direction = null;
	private Button mButton_Next = null;
	private Button mButton_Pre = null;
	private Button mButton_Measurement = null;
	private Button mButtion_Position = null;
	//private Button mButtion_Media = null;
	private LinearLayout LinearLayout_y = null;
	private LinearLayout LinearLayout_z = null;
	private TextView mTextViewX = null;
	private TextView mTextViewY = null;
	private TextView mTextViewZ = null;
	private TextView mTextViewCountDown = null;
	private int iCheckedCount =0;
	private boolean bHasFinishChecked = false;
	private int mPartItemCounts = 0;
	//测试倒计时，用于待信号稳定
	private final int mMaxSecond_StartMeasure = 30;
	private int mCountDown =mMaxSecond_StartMeasure;
	//设置颜色级别
	private RadioButton mRadioButton = null;
	private Fragment mfragment = null;
	//一个DeviceItem Object
	private JSONObject mDeviceItemObject =null;
	//一个ParteItem数组对应的JSON数组
	private JSONArray mJSONArray = null;
	private JSONArray mNewArrayJSON = null;
	private String mDeviceItemDefStr = null;
	//private boolean []mBValue = null;	//巡检的结果
	private myApplication app = null;
	JSONObject mCurPartItemobject = null;
	//根据Spinner的Index 设置mItemDefIndex
	private int mItemDefIndex =0;
	private OnButtonListener mFragmentCallBack = null;
	private String mFirstStartTime="";
	private String mLastEndTime="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
//		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		MLog.Logd(TAG,"PartItemActivity:onCreate() ");
		setContentView(R.layout.unitcheck);
		app =((myApplication) getApplication());
		//Intent intent = getIntent();
		mRouteIndex = app.mRouteIndex;
		mStationIndex = app.mStationIndex;
		mDeviceIndex = app.mDeviceIndex;
	
		//计划巡检还是特别巡检
		TextView planNameTextView = (TextView) findViewById(R.id.routeName);
		planNameTextView.setText(app.gRouteClassName);

		//路线名称
		TextView RouteNameTextView = (TextView) findViewById(R.id.routeName);
		RouteNameTextView.setText(app.gRouteName);
         //站点名称
		TextView stationTextView = (TextView) findViewById(R.id.stationName);
		//stationTextView.setText(app.gStationName);
		//设备名称
		TextView deviceTextView = (TextView) findViewById(R.id.deviceName);		
		//deviceTextView.setText(app.gDeviceName);

		//返回图标
		ImageView imageView = (ImageView)findViewById(R.id.backImage);
		imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				MLog.Logd(TAG,"imageView.setOnClickListener");
				// TODO Auto-generated method stub
				finish();
			}
			
		});

		mButtion_Position = (Button)findViewById(R.id.bottombutton1);
		mButtion_Position.setOnClickListener(this);

		
		mButton_Direction = (Button)findViewById(R.id.bottombutton2);
		mButton_Direction.setOnClickListener(this);
		
		mButton_Next = (Button)findViewById(R.id.next);
		mButton_Next.setOnClickListener(this);
		 
		mButton_Pre = (Button)findViewById(R.id.bottombutton_pre);
		mButton_Pre.setOnClickListener(this);
		
		mButton_Measurement = (Button)findViewById(R.id.bottombutton3);
		mButton_Measurement.setOnClickListener(this);
		//对应
        mJSONArray = new JSONArray();
        mNewArrayJSON= new JSONArray();
        //mBValue = new boolean[mPartItemCounts];
        mHandler.sendMessage(mHandler.obtainMessage(MSG_INIT_DATA));
        
        
	}

	

	//根据不同的测量类型，显示不同的UI界面。
	void switchFragment(int type,boolean bFirstInit){
		MLog.Logd(TAG, "switchFragment() type is " +type+",mCurPartItemobject is "+mCurPartItemobject.toString() );
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		//Toast.makeText(this.getApplicationContext(), " type ="+type, Toast.LENGTH_LONG).show();
		
		  switch(type){
		   case CommonDef.checkUnit_Type.ACCELERATION:
		   case CommonDef.checkUnit_Type.SPEED:	
		   case CommonDef.checkUnit_Type.DISPLACEMENT:
			  // 需要方向
	
			   mButton_Direction.setEnabled(true);
			   mButton_Direction.setText(getString(R.string.direction));
			   {
					Fragment fragment = new Vibrate_fragment();
					
					Gson g= new Gson();
					PartItemItem a=g.fromJson( mPartItemList.get(mCheckIndex).toString(), PartItemItem.class);
					Bundle bundle = new Bundle();  	
					bundle.putParcelable("value", a);
					bundle.putString(KEY.KEY_PARTITEMDATA, mCurPartItemobject.optString(KEY.KEY_PARTITEMDATA)); 
					//bundle.putInt(KEY.KEY_ZHOU_COUNTS, Integer.parseInt(app.getPartItemCheckUnitName(mCurPartItemobject,CommonDef.partItemData_Index.PARTITEM_RELAX_COUNT)));  
					fragment.setArguments(bundle);  
					if(bFirstInit){
					fragmentTransaction.add(R.id.fragment_content,fragment);
					}else{
						fragmentTransaction.replace(R.id.fragment_content,fragment);
					}
					fragmentTransaction.commit();
					}
			   mButtion_Position.setText(getString(R.string.position));
				mButton_Measurement.setText(getString(R.string.measurement));
				mButton_Direction.setText(getString(R.string.direction));	
			   break;
		   case CommonDef.checkUnit_Type.TEMPERATURE:
		   case CommonDef.checkUnit_Type.ROTATIONAL_SPEED:
			  //方向按鈕隱藏
			   mButton_Direction.setEnabled(false);
			  
			   {
					Fragment fragment = new Temperature_fragment();
					Bundle bundle = new Bundle();  
					bundle.putString(KEY.KEY_PARTITEMDATA, mCurPartItemobject.optString(KEY.KEY_PARTITEMDATA));  
					fragment.setArguments(bundle);  
					if(bFirstInit){
					fragmentTransaction.add(R.id.fragment_content,fragment);
					}else{
						fragmentTransaction.replace(R.id.fragment_content,fragment);
					}					
					fragmentTransaction.commit();
					}
			   mButtion_Position.setText(getString(R.string.position));
				mButton_Measurement.setText(getString(R.string.measurement));
				mButton_Direction.setText(getString(R.string.direction));	
			   break;
		   case CommonDef.checkUnit_Type.RECORD:
		   case CommonDef.checkUnit_Type.DEFAULT_CONDITION:
		   {
				Fragment fragment = new Observer_fragment();
				Bundle bundle = new Bundle();  					
				bundle.putString(KEY.KEY_PARTITEMDATA, mCurPartItemobject.optString(KEY.KEY_PARTITEMDATA));  
				fragment.setArguments(bundle);  
				if(bFirstInit){
				fragmentTransaction.add(R.id.fragment_content,fragment);
				}else{
					fragmentTransaction.replace(R.id.fragment_content,fragment);
				}
				fragmentTransaction.commit();
				
				mButton_Direction.setEnabled(true);
				mButtion_Position.setText(getString(R.string.camera));
				mButton_Measurement.setText(getString(R.string.textrecord));
				mButton_Direction.setText(getString(R.string.soundrecord));				
				}
			   break;
		   case CommonDef.checkUnit_Type.MEASUREMENT:
		   {
			   Fragment fragment = new measurement_fragment();
				Bundle bundle = new Bundle();  					
				bundle.putString(KEY.KEY_PARTITEMDATA, mCurPartItemobject.optString(KEY.KEY_PARTITEMDATA));  
				fragment.setArguments(bundle);  
				if(bFirstInit){
				fragmentTransaction.add(R.id.fragment_content,fragment);
				}else{
					fragmentTransaction.replace(R.id.fragment_content,fragment);
				}
				fragmentTransaction.commit();
			   
		   }
			   break;
			   
			   default:
				   mButtion_Position.setText(getString(R.string.position));
					mButton_Measurement.setText(getString(R.string.measurement));
					mButton_Direction.setText(getString(R.string.direction));	
				   Toast.makeText(getApplicationContext(), "default Fragment type ="+type, Toast.LENGTH_LONG).show();
				   break;
		  }
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		 //获取第一个PartItemData的测量类型
       
		super.onResume();
	}

 /**
  * 获取该项的巡检类型 及是否要巡检的flag，
  * 0,需要巡检
  * 1，提示 是否要巡检
  * 2,不巡检
  * @return
  */
	int getPatItemType(){
		
		if(mPartItemList != null){
			mCurPartItemobject = (JSONObject) mPartItemList.get(mCheckIndex);
			MLog.Logd(TAG,"getPatItemType() mPartItemList size ="+mPartItemList.size() +" object is " +mCurPartItemobject.toString());
		}else{
			MLog.Logd(TAG,"getPatItemType() mPartItemList IS NULL");
		}
//		 mCheckUnit_DataType=Integer.parseInt(app
//		.getPartItemCheckUnitName(mCurPartItemobject,CommonDef.partItemData_Index.PARTITEM_DATA_TYPE));
//		 
//		return  Integer.parseInt(app.getPartItemCheckUnitName(mCurPartItemobject,CommonDef.partItemData_Index.PARTITEM_TIPS_FLAG));
		return 0;
	}
	
	//初始化数据PartItemData,并获取第一个PartItemData的测量类型
   void InitPartItemData(){
//	   try {
//		   MLog.Logd(TAG, "InitPartItemData() start mStationIndex =" +mStationIndex +",mDeviceIndex ="+mDeviceIndex );
//		   mPartItemObject = app.getPartItemObject(mStationIndex,mDeviceIndex);
//		  
//		   List<Object> deviceItemList = app.getDeviceItemList(mStationIndex);
//		   
//		   mCurrentDeviceItem = deviceItemList.get(mDeviceIndex);
//		   MLog.Logd(TAG, "mCurrentDeviceItem IS " + mCurrentDeviceItem.toString());
//		   
//		   mPartItemList = app.getPartItem(mPartItemObject,-1);
//		   MLog.Logd(TAG, "InitPartItemData() mPartItemList size ="+mPartItemList.size());
//		   mPartItemCounts = mPartItemList.size();
//		   getPatItemType();// Integer.valueOf(app.getPartItemCheckUnitName(mPartItemList.get(0),CommonDef.partItemData_Index.PARTITEM_ADDITIONAL_INFO));
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
   }   
   
   private final int MSG_INIT_DATA =0;
   private final int MSG_NEXT =1;
   private final int MSG_MEASUERMENT =2;
   private final int MSG_DIRECTION =3;
   private final int MSG_PRE =4;
   private final int MSG_SAVE_PARTITEMDATA =5;
   private final int MSG_SAVE_DEVICEITEM =6;
   private final int MSG_ADD_A_PARTITEMDATA =7;
   Handler mHandler = new Handler(){
	   @Override
	    public void handleMessage(Message msg) {
		   switch(msg.what){
		   case MSG_INIT_DATA:
			   InitPartItemData();
			   switchFragment(mCheckUnit_DataType,true);
			   break;
		   case MSG_NEXT:
			   nextCheckItem();
			   break;
		   case MSG_MEASUERMENT:
			   getMeasureValue();
			   break;
		   case MSG_DIRECTION:
			   break;
		   case MSG_PRE:
			   preCheckItem();
			   break;
		   case MSG_SAVE_PARTITEMDATA:
			   saveCheckedItemNode();			  
			   break;
		   case MSG_SAVE_DEVICEITEM:
			   saveDeviceItem();
			   break;
		   case MSG_ADD_A_PARTITEMDATA:
			   addAPartItemData(msg.arg1,msg.arg2,msg.obj);
			   
			   break;
		   }
	   }
   };
   
   final int CAMERA_TYPE =1;
   final int RF_TYPE =0;
   final int AUDIO_TYPE =2;
   final int TEXT_RECORD_TYPE =3;
   final int TEXT_ZHOU_TYPE =4;//轴数据
	/**
	 * 用户选择完启停机后（如"运行/停机/备用/其它"中的一项），要生成一个新的PartItemData：
	 */
   void addAPartItemData(int type,int zhouCounts,Object msgobject){
	   switch(type){
	   case RF_TYPE:
//	   {
//		   JSONObject object = new JSONObject();
//		   String value = app.gDeviceName+ff
//				   +((JSONObject)mCurrentDeviceItem).optString(T_Device_Item.Device_Array_Item_Const.Key_Code)+ff
//				   +ff+ff+ff+ff+ff+ff+ff+ff+ff+ff+ff
//				   +"设备"+ff
//				   +ff+ff+ff+ff+ff+ff+ff+ff+ff+ff
//				   +ff+ff+ff+ff+ff+ff+ff+ff+ff+ff
//				   +"2"+ff+"01"+ff+ff+ff;
//		   try {
//			object.put(KEY.KEY_PARTITEMDATA, value);
//			object.put(KEY.KEY_Fast_Record_Item_Name, "111111111");
//			mNewArrayJSON.put(object);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 //  mHandler.sendMessage(mHandler.obtainMessage(MSG_SAVE_DEVICEITEM));
//	   }
		   break;
	   case CAMERA_TYPE:
	   {
		   JSONObject object = new JSONObject();
		   try {
			String data=  mCurPartItemobject.optString(KEY.KEY_PARTITEMDATA);
			object.put(KEY.KEY_Fast_Record_Item_Name, mCurPartItemobject.optString(KEY.KEY_Fast_Record_Item_Name));
			object.put(KEY.KEY_PARTITEMDATA,addUpdata(data,mCheckValue,null));
			mNewArrayJSON.put(object);
			MLog.Logd(TAG, "addAPartItemData() mJSONArray.size ="+mJSONArray.length());
			//mHandler.sendMessage(mHandler.obtainMessage(MSG_SAVE_PARTITEMDATA));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		   
		
	   }
		   break;
	   case AUDIO_TYPE:
	   {
		   JSONObject object = new JSONObject();
		   try {
			String data=  mCurPartItemobject.optString(KEY.KEY_PARTITEMDATA);
			object.put(KEY.KEY_Fast_Record_Item_Name, mCurPartItemobject.optString(KEY.KEY_Fast_Record_Item_Name));
			object.put(KEY.KEY_PARTITEMDATA,addUpdata(data,mCheckValue,null));
			mNewArrayJSON.put(object);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		   
		
	   }
		   break;
	   case TEXT_RECORD_TYPE:
	   {
		   JSONObject object = new JSONObject();
		   try {
			String data=  mCurPartItemobject.optString(KEY.KEY_PARTITEMDATA);
			object.put(KEY.KEY_Fast_Record_Item_Name, mCurPartItemobject.optString(KEY.KEY_Fast_Record_Item_Name));
			object.put(KEY.KEY_PARTITEMDATA,addUpdata(data,mCheckValue,null));
			mNewArrayJSON.put(object);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		   
		
	   }
		   break;
	   case TEXT_ZHOU_TYPE:
			if (zhouCounts > 1) {
				String guid = SystemUtil.createGUID();
				String values = msgobject.toString();
				String[] array = values.split(",");
				for (int i = 0; i < array.length; i++) {
					// 需要保存X，Y的数据，相同的GUID，不同的checkValue
					JSONObject object = new JSONObject();
					try {
						String data = mCurPartItemobject.optString(KEY.KEY_PARTITEMDATA);
						object.put(
								KEY.KEY_Fast_Record_Item_Name,
								mCurPartItemobject.optString(KEY.KEY_Fast_Record_Item_Name));
						object.put(KEY.KEY_PARTITEMDATA,addUpdata(data, array[i], guid));
						mNewArrayJSON.put(object);
						MLog.Logd(TAG, "addAPartItemData() mJSONArray.size ="+ mJSONArray.length());
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		   break;
	   }
	  
   }
	String mCheckValue = "测量数值";
	
	/**
	 * 向前巡检或查看前一项巡检结果，同样要先获取巡检类型，并对应显示出来UI.
	 */
	void preCheckItem(){
		if(mCheckIndex < (mPartItemCounts) && mCheckIndex>0){	
			mCheckIndex--;
		getPatItemType();
		
		//显示数据
		switchFragment(mCheckUnit_DataType, false);
		

		if(mCheckIndex==0){
			mButton_Pre.setEnabled(false);
		}
		}
	}
	/**
	 * 保存当前的DeviceItem项的数据，添加一些flag and time information
	 */
	void saveDeviceItem(){
		JSONObject object =(JSONObject) mCurrentDeviceItem;
		try {
			object.put(T_Device_Item.Device_Array_Item_Const.Key_Start_Check_Datetime, mStartTime);
			object.put(T_Device_Item.Device_Array_Item_Const.Key_End_Check_Datetime, mEndTime);
			object.put(T_Device_Item.Device_Array_Item_Const.Key_Is_Device_Checked, 1);
			object.put(T_Device_Item.Device_Array_Item_Const.Key_Is_RFID_Checked, 1);
			object.put(T_Device_Item.Device_Array_Item_Const.Key_Total_Check_Time, SystemUtil.getDiffDate(mStartTime, mEndTime));
			object.put(T_Device_Item.Device_Array_Item_Const.Key_Is_Omission_Check, 1);
			for(int i=0;i< mNewArrayJSON.length();i++){
				mJSONArray.put(mNewArrayJSON.get(i));
			}
			object.put(T_Device_Item.Device_Array_Item_Const.Key_PartItem, mJSONArray);
			MLog.Logd(TAG, "saveDeviceItem() object is "+object.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveUpLoadData();
	//	app.insertUpLoadInfo(this.getApplicationContext());
		finish();
	}
	/**
	 * 保存当前的 巡检项巡检结果，重新生成一个PartItem
	 */
	void saveCheckedItemNode() {
		mEndTime = SystemUtil.getSystemTime(0);
		// 先保存当前测试项的数据
		MLog.Logd(TAG, "mPartItemList size is ," + mPartItemList.size());
		JSONObject json = (JSONObject) mPartItemList.get(mCheckIndex);
		MLog.Logd(TAG, "saveCheckedItemNode()," + json);
		String partItemData = "";
		try {
			partItemData = json.getString(KEY.KEY_PARTITEMDATA);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 添加巡检结果到结果中，便于形成最后的结果。
		if (mZhouCounts == 0) {
			try {
				json.put(
						KEY.KEY_PARTITEMDATA,
						addUpdata(partItemData, mCheckValue,
								SystemUtil.createGUID()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// json =
			// app.setPartItem_ItemDef(json,0,mCheckValue+SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM)+"*3");
			MLog.Logd(TAG, "saveCheckedItemNode() result is," + json);
			mJSONArray.put(json);
		} else {
			MLog.Logd(TAG, "saveCheckedItemNode() mCheckValue," + mCheckValue+",mZhouCounts="+mZhouCounts);
			String[] value = mCheckValue.split(",");
			String guid = SystemUtil.createGUID();
			for (int i = 0; i < mZhouCounts&&i <value.length; i++) {
				try {
					json.put(KEY.KEY_PARTITEMDATA,
							addUpdata(partItemData, value[i], guid));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MLog.Logd(TAG, "saveCheckedItemNode() result is," + json);
				mJSONArray.put(json);
			}
		}
	}
	
	String Item_def = "运行";
	String mStartTime = "";
	String mEndTime="";
	
	final String  ff="*";
	
	/**
	 * 重新生成一个PartItemData对象，并填充新的数值
	 * @param oldvalue
	 * @param checkValue
	 * @param Item23_Guid ,默认为NULL，由该函数来生成GUID，否则由外面传入GUID
	 * @return
	 */
	private String addUpdata(String oldvalue,String checkValue,String Item23_Guid){
		String value= "";
		ContentValues vc = new ContentValues();
		vc.put("19", Item_def);
		vc.put("20", mStartTime);
		vc.put("21", mEndTime);
		vc.put("22", SystemUtil.getDiffDate(mStartTime,mEndTime));
		switch(mCheckUnit_DataType){
		 case CommonDef.checkUnit_Type.ACCELERATION:
			 vc.put("23", Item23_Guid);	 
			 vc.put("24", SystemUtil.createGUID());
			 vc.put("26", 1);//当测量类型为加速度、速度、位移时，=0,=1,=2分别代表，X,Y,Z轴，其它类型为空。
			 vc.put("27", 0);//当测量类型为加速度、速度、位移时，对应值分别为0、1、2，其它数据类型为空
			 vc.put("28", "");//当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
			 vc.put("29", "上限值");//当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
				vc.put("30", "不为空");//当测量类型为加速度、速度、位移时，不为空； 				其它数据类型为空

				vc.put("31", "不为空");//当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
			 break;
		 case CommonDef.checkUnit_Type.SPEED:	
			 vc.put("23", Item23_Guid);	 
			 vc.put("24", SystemUtil.createGUID());
			 vc.put("26", 1);//当测量类型为加速度、速度、位移时，=0,=1,=2分别代表，X,Y,Z轴，其它类型为空。
			 vc.put("27", 1);//当测量类型为加速度、速度、位移时，对应值分别为0、1、2，其它数据类型为空
			 vc.put("28", "");//当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
			 vc.put("29", "");
			vc.put("30", "");
				vc.put("31", "");
			 break;
		 case CommonDef.checkUnit_Type.DISPLACEMENT:
			 vc.put("23", Item23_Guid);	 
			 vc.put("24", SystemUtil.createGUID());
			 vc.put("26", 1);//当测量类型为加速度、速度、位移时，=0,=1,=2分别代表，X,Y,Z轴，其它类型为空。
			 vc.put("27", 2);//当测量类型为加速度、速度、位移时，对应值分别为0、1、2，其它数据类型为空
			 vc.put("28", "");//当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
			 vc.put("29", "");
				vc.put("30", "");
				vc.put("31", "");
			   break;
		  
		default:
			vc.put("23", "");
			vc.put("24", "");
			vc.put("26", "");
			vc.put("27", "");
			vc.put("28", "");
			vc.put("29", "");
			vc.put("30", "");
			vc.put("31", "");
			   break;
		  
		}
		vc.put("25", 0);
		
		vc.put("32", "");
		vc.put("33", "");
		vc.put("34", "");
		vc.put("35", "");
		
		value = vc.get("19")+ff
				+vc.get("20")+ff
				+vc.get("21")+ff
				+vc.get("22")+ff
				+vc.get("23")+ff
				+vc.get("24")+ff
				+vc.get("25")+ff
				+vc.get("26")+ff
				+vc.get("27")+ff
				+vc.get("28")+ff
				+vc.get("29")+ff
				+vc.get("30")+ff
				+vc.get("31")+ff
				+vc.get("32")+ff
				+vc.get("33")+ff
				+vc.get("34")+ff
				+vc.get("35")+ff
				+ff+ff+ff+ff;
		
		
		String[] array = oldvalue.split(KEY.PARTITEMDATA_SPLIT_KEYWORD);
		String newValue = "";
		for(int i =0;i<array.length;i++){
			if(i!=13){
			newValue= newValue+array[i]+ff;
			}else{
				newValue= newValue+checkValue+ff;
			}
		}
		return newValue+value;
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
			if (mCheckIndex < mPartItemCounts) {
				if (mCheckIndex != (mPartItemCounts - 1)) {
					mCheckIndex++;
					mCheckValue="";
					int flag =getPatItemType();
					if(flag ==0){
					// 显示数据
					switchFragment(mCheckUnit_DataType, false);
					}else if(flag==1){
						//提示是否需要巡检，如果选择巡检，按正常巡检，否则不巡检
						AlertDialog.Builder builder = new Builder(this.getApplicationContext());
						builder.setMessage("确认退出吗？");
						builder.setTitle("提示");
						
						builder.setPositiveButton("确认", new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switchFragment(mCheckUnit_DataType, false);
						}
						
						});
						
						builder.setNegativeButton("取消", new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						}
						});
						builder.create().show();
					}
					MLog.Logd(TAG, "nextCheckItem(),mCheckUnit_DataType =" + mCheckUnit_DataType
							+ ",mCheckIndex =" + mCheckIndex +",mCurPartItemobject is "+mCurPartItemobject.toString());
				} else {
					// 保存当前DeviceItem数据

					// 设置Button的文字描述，为退出做准备
					mButton_Next.setText(getString(R.string.save_and_finish));

				}
				if (mCheckIndex != 0 && !mButton_Pre.isEnabled()) {
					mButton_Pre.setEnabled(true);
				}

			}
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.time_out), Toast.LENGTH_LONG).show();
			this.finish();
		}

	}
	

	private Uri imageFilePath = null;
    

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
		case R.id.bottombutton1://位置信息
		{

			if (mButtion_Position.getText().equals(getString(R.string.camera))) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				ContentValues values = new ContentValues(3);
				values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");
				values.put(MediaStore.Images.Media.DESCRIPTION,
						"this is description");
				values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
				imageFilePath = getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				MLog.Logd("test", "main_media imageFilePath is " + imageFilePath);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath); // 这样就将文件的存储方式和uri指定到了Camera应用中

				startActivityForResult(intent, PartItem_Contact.PARTITEM_CAMERA_RESULT);
			} else {
				initPopupWindowFliter(arg0);
			}
		}
			break;
		case R.id.next://下一测试点
		{
			if(mButton_Next.getText().equals(getString(R.string.save_and_finish))){
				//
				Message msg = mHandler.obtainMessage(MSG_ADD_A_PARTITEMDATA);
				msg.arg1 = RF_TYPE;
				mHandler.sendMessage(msg);
				mHandler.sendMessage(mHandler.obtainMessage(MSG_SAVE_DEVICEITEM));
			}else{	
				if(mCheckUnit_DataType ==CommonDef.checkUnit_Type.RECORD
						||mCheckUnit_DataType ==CommonDef.checkUnit_Type.DEFAULT_CONDITION){
					mFragmentCallBack.OnButtonDown(1, null);
				}
	        mHandler.sendMessage(mHandler.obtainMessage(MSG_SAVE_PARTITEMDATA));
	        mHandler.sendMessage(mHandler.obtainMessage(MSG_NEXT));
			}
			
		}
			break;		
		case R.id.bottombutton3://测量
			if(mButton_Measurement.getText().equals(getString(R.string.textrecord))){
				Intent intent = new Intent();
				JSONObject json = (JSONObject) mPartItemList.get(mCheckIndex);
//				String value = app.getPartItemCheckUnitName(json, CommonDef.partItemData_Index.PARTITEM_CHECKPOINT_NAME);
//				intent.putExtra(CommonDef.check_unit_info.NAME, value);
				intent.setClass(PartItemActivity.this, NotepadActivity.class);
				startActivityForResult(intent,PartItem_Contact.PARTITEM_NOTEPAD_RESULT);
			}else{
				 mHandler.sendMessage(mHandler.obtainMessage(MSG_MEASUERMENT));
				
			}
			break;
		case R.id.bottombutton2://方向
			if(mButton_Direction.getText().equals(getString(R.string.soundrecord))){
				Intent intent = new Intent();
				intent.setClass(PartItemActivity.this, SoundRecordActivity.class);
				startActivityForResult(intent,PartItem_Contact.PARTITEM_SOUNDRECORD_RESULT);
			}else{
			 new AlertDialog.Builder(PartItemActivity.this)
	         .setTitle(getString(R.string.direction_select))
	         .setItems(direction_item, new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) {
	            	 mButton_Direction.setText(direction_item[which]);
	            	 //获取选择的项,X-Y,X-Z,Y-Z
	             Toast info =Toast.makeText(PartItemActivity.this, direction_item[which],Toast.LENGTH_LONG);
	                 info.setMargin(0.0f, 0.3f);
	                 info.show();
	             }
	         }).create().show();
			 }
			break;	
		}
	}
	
    private int valueIndex =0;
    void getMeasureValue(){
    	//获取当前系统时间作为开始测量时间
		mStartTime = SystemUtil.getSystemTime(0);
    	//mCheckValue = "getMeasureValue "+valueIndex++;
		mFragmentCallBack.OnButtonDown(0, null);
    }
    @Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		MLog.Logd("test", "onActivityResult() 00" + requestCode + ",resultCode= "
				+ resultCode);
		if (requestCode == PartItem_Contact.PARTITEM_CAMERA_RESULT) {
			mCheckValue = imageFilePath.toString();
			
				Bundle bundle = new Bundle();
				bundle.putString("pictureUri", imageFilePath.toString());
				mFragmentCallBack.OnButtonDown(0, bundle);
				// imageView.setImageBitmap(pic);
				Message msg = mHandler.obtainMessage(MSG_ADD_A_PARTITEMDATA);
				msg.arg1 = CAMERA_TYPE;
				mHandler.sendMessage(msg);
			
			
		}

		if (PartItem_Contact.PARTITEM_NOTEPAD_RESULT == requestCode) {

			SharedPreferences mSharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			// 实例化SharedPreferences.Editor对象（第二步）
			String timeStr = mSharedPreferences.getString(
					CommonDef.PartItemData_Shered_info.Time,
					SystemUtil.getSystemTime(0));

			mCheckValue = mSharedPreferences.getString(
					CommonDef.PartItemData_Shered_info.Content, "");

			// 重新生成一个parItemData数据项目
			Message msg = mHandler.obtainMessage(MSG_ADD_A_PARTITEMDATA);
			msg.arg1 = TEXT_RECORD_TYPE;
			mHandler.sendMessage(msg);
		}

		if (PartItem_Contact.PARTITEM_SOUNDRECORD_RESULT == requestCode) {
			Intent intent = data;
			mCheckValue = intent.getExtras().getString(CommonDef.AUDIO_PATH);
			Message msg = mHandler.obtainMessage(MSG_ADD_A_PARTITEMDATA);
			msg.arg1 = AUDIO_TYPE;
			mHandler.sendMessage(msg);
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
		try {
			mFragmentCallBack = (OnButtonListener) fragment;
		} catch (Exception e) {
			throw new ClassCastException(this.toString()+ " must OnButtonListener");
		}
		super.onAttachFragment(fragment);
	}
	
	public interface OnButtonListener{
		void OnButtonDown(int buttonId,Bundle object);
	};

	/**
	 * 保存巡检数据成文件，并保存到数据表中。
	 */
	void saveUpLoadData(){
		String fileName = "/sdcard/0001.txt";
	//	app.SaveData(mRouteIndex, fileName);
	}
	PopupWindow pw_Left = null;
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
	
private int mZhouCounts=0;

	@Override
	public void OnClick(int genPartItemDataCounts, int xValue, int yValue,
			int zValue) {
		// TODO Auto-generated method stub
//		if(genPartItemDataCounts==1){
//			mCheckValue = String.valueOf(xValue);
//		}
		MLog.Logd(TAG, "OnClick() "+genPartItemDataCounts+","+xValue+","+yValue+","+zValue);
		mCheckValue=String.valueOf(xValue)+","+String.valueOf(yValue)+","+String.valueOf(zValue);
		mZhouCounts = genPartItemDataCounts;
		MLog.Logd(TAG, "OnClick() mCheckValue"+mCheckValue);
		//if(genPartItemDataCounts>1){
			//Message msg = mHandler.obtainMessage(MSG_ADD_A_PARTITEMDATA);
			//msg.arg1 = TEXT_ZHOU_TYPE;
			//msg.arg2=genPartItemDataCounts;
			//msg.obj=String.valueOf(xValue)+","+String.valueOf(yValue)+","+String.valueOf(zValue);
			//mHandler.sendMessage(msg);
			
			 
		//}
		
		
	}
}
