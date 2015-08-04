package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.AuxiliaryInfoNode;
import com.aic.aicdetactor.data.Temperature;
import com.aic.aicdetactor.media.MediaMainActivity;
import com.aic.aicdetactor.util.SystemUtil;

public class DeviceItemActivity extends Activity implements OnClickListener {

	private ListView mListView = null;
	String TAG = "luotest";
	private Spinner mSpinner = null;
	private TextView mItemDefTextView = null;//当只有一项时才显示
	private String mCheckItemNameStr = null;//检查项名
	private String mCheckUnitNameStr = null;//检查部件名称
	private List<String> spinnerList = new ArrayList<String>();
	private ArrayAdapter<String> spinnerAdapter;
	private int mLastSpinnerIndex = 0;
	private Object partItemObject = null;
	private Object mCurrentDeviceObject = null;
	private List<Map<String, Object>> mMapList;
	public final int SPINNER_SELECTCHANGED =0;
	private SimpleAdapter mListViewAdapter = null;
	
	private List<Object> mPartItemSelectedList=null;
	private CheckBox mCheckbox = null;
	private int mRouteIndex =0;
	private int mStationIndex =0;
	private int mDeviceIndex = 0;
	private int mCheckIndex =0;
	//是否需要反向排序来巡检
	private boolean isReverseDetection = false;
	//点击listItem后 ListView 视图消失，显示具体测试点界面
	private boolean bListViewVisible = true;
	private boolean bSpinnerVisible = true;
	private LinearLayout mUnitcheck_Vibrate = null;
	private int mCheckUnit_DataType = 0;
	private Button mButton_Direction = null;
	private Button mButton_Next = null;
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
	//测试倒计时，用于待信号稳定
	private final int mMaxSecond_StartMeasure = 30;
	private int mCountDown =mMaxSecond_StartMeasure;
	//设置颜色级别
	private RadioButton mRadioButton = null;
	
	private JSONArray mJSONArray = null;
	private String mDeviceNameStr= null;
	private String mDeviceQueryNameStr = null;
	private String mDeviceItemDefStr = null;
	private boolean []mBValue = null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.deviceitem_activity_ex);

		Intent intent = getIntent();
		mRouteIndex = intent.getExtras().getInt(CommonDef.route_info.LISTVIEW_ITEM_INDEX);
		mStationIndex = intent.getExtras().getInt(CommonDef.station_info.LISTVIEW_ITEM_INDEX);
		mDeviceIndex = intent.getExtras().getInt(CommonDef.device_info.LISTVIEW_ITEM_INDEX);

		String oneCatalog = intent.getExtras().getString(CommonDef.ROUTE_CLASS_NAME);
		mDeviceNameStr = intent.getExtras().getString(CommonDef.device_info.NAME);
		String routeNameStr = intent.getExtras().getString(CommonDef.route_info.NAME);
		String  stationName = intent.getExtras().getString(CommonDef.station_info.NAME);
Log.d(TAG,"routeName is "+ routeNameStr);
		TextView planNameTextView = (TextView) findViewById(R.id.planname);
		planNameTextView.setText(oneCatalog);
		((myApplication) getApplication()).gRouteName =  oneCatalog;
		((myApplication) getApplication()).gStationName =  stationName;
		((myApplication) getApplication()).gDeviceName =  mDeviceNameStr;

		TextView RouteNameTextView = (TextView) findViewById(R.id.station_text_name);
		RouteNameTextView.setText(""+(mDeviceIndex+1) +" >>"+routeNameStr+">>"+stationName+">>"+mDeviceNameStr);

		TextView secondcatalognameTextView = (TextView) findViewById(R.id.secondcatalogname);
		secondcatalognameTextView.setText(routeNameStr);

		ImageView imageView = (ImageView)findViewById(R.id.imageView1);
		imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"imageView.setOnClickListener");
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		mUnitcheck_Vibrate = (LinearLayout)findViewById(R.id.unitcheck);
		mUnitcheck_Vibrate.setVisibility(View.GONE);
		Log.d(TAG, "ONcREATE stationIndex is " + mStationIndex + "deviceIndex"+ mDeviceIndex);
		mListView = (ListView) findViewById(R.id.listView);
		mMapList = new ArrayList<Map<String, Object>>();
		
		InitDataNeeded(0,false);
		mListViewAdapter = new SimpleAdapter(this, mMapList,
				R.layout.checkunit, new String[] { 
				CommonDef.check_item_info.INDEX, //索引
				CommonDef.check_item_info.UNIT_NAME,//巡检项名称
				CommonDef.check_item_info.VALUE,//巡检结果
				CommonDef.check_item_info.DEADLINE, //巡检最近时间
				CommonDef.check_item_info.NAME 
				}, new int[] {
						R.id.index,
						R.id.pathname,
						R.id.checkvalue, 
						R.id.deadtime});
		mListView.setAdapter(mListViewAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				bListViewVisible = false;
				
				//获取partItemData中的巡检数据种类
				/**
				 * 4、5相同
				 * 2、7
				 * 8个字节，不能为空。共9种数据类型，分别为：
=”00000002” 表示测量温度
=“00000003” 表示记录项，用户即可从上位机事先编好的多个选项里选择一项，也可编辑一些新的信息，项与项之间用“/”隔开。另外每项字符串末尾有额外“0”或“1”单字节控制信息，“0”代表正常，“1”代表“异常”，如：“正常0/微亏1/严亏1”，巡检仪界面上只会显示“正常/微亏/严亏”，如用户选择了“正常”，上传的巡检项末尾会添加“0”，表示设备正常，如选择了“微亏”或“严亏”，上传的巡检项末尾会添加“1”，表示设备异常。
=“00000004” 表示测量加速度
=“00000005” 表示测量速度
=“00000006” 表示测量位移
=“00000007” 表示测量转速
=“00000008” 表示预设状况项，用户即从上位机事先编好的多个选项里选择多项，也可编辑。如从编辑好的项中选择或编辑选择项，上传的巡检项末尾会添加“1”，表示异常；如用户输入 “正常”字符串，上传的巡检项末尾会添加“0”，表示正常。
=“00000009” 表示图片
=“00000010” 表示振动矢量波形

				 */
				mCheckIndex = arg2;
				// TODO Auto-generated method stub
				 HashMap<String,String>
				 map = (HashMap<String,String>)mListView.getItemAtPosition(mCheckIndex);
				 mCheckItemNameStr = map.get(CommonDef.check_item_info.NAME);
				 mCheckUnitNameStr = map.get(CommonDef.check_item_info.UNIT_NAME);
				 mCheckUnit_DataType = Integer.parseInt(map.get(CommonDef.check_item_info.DATA_TYPE));
				 Log.d(TAG,"partitemdata data type =" +mCheckUnit_DataType);
				// needVisible();
				 
				 ((myApplication) getApplication()).mPartItemName = mCheckItemNameStr;
				 ((myApplication) getApplication()).mPartItemIndex = arg2;
				 Intent intent = new Intent();
				// intent.putExtra(CommonDef.check_item_info.INDEX, arg2);
				 intent.putExtra(CommonDef.check_item_info.DATA_TYPE, mCheckUnit_DataType);
				 intent.putExtra(CommonDef.check_item_info.ITEM_COUNTS, mListView.getCount());
				 
				 intent.setClass(DeviceItemActivity.this,PartItemActivity.class);
				 startActivity(intent);
			}
		});
		mButtion_Position = (Button)findViewById(R.id.position);
		mButtion_Position.setOnClickListener(this);
		mButtion_Position.setOnKeyListener(new Button.OnKeyListener(){

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), " " +arg1+","+ arg2.getKeyCode(),
						0).show();
				return false;
			}
			
		});
		
		mButton_Direction = (Button)findViewById(R.id.direction);
		mButton_Direction.setOnClickListener(this);
		
		mButton_Next = (Button)findViewById(R.id.next);
		mButton_Next.setOnClickListener(this);
		
		mButton_Measurement = (Button)findViewById(R.id.measurement);
		mButton_Measurement.setOnClickListener(this);
	//	mButtion_Media =(Button)findViewById(R.id.media);
	//	mButtion_Media.setOnClickListener(this);
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		mItemDefTextView = (TextView)findViewById(R.id.status);
		initSpinnerData();
		if(bSpinnerVisible){		
			spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerList);    
	        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
			spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        //第四步：将适配器添加到下拉列表上    
	        mSpinner.setAdapter(spinnerAdapter);    
	        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
	        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
	            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
	                // TODO Auto-generated method stub   
	            	if(mLastSpinnerIndex == arg2) return ;
	            	
	            	Message msg = new Message();
	            	msg.arg1 = arg2;
	            	msg.what = SPINNER_SELECTCHANGED;
	            	myHandler.sendMessage(msg);
	            	mLastSpinnerIndex = arg2;
	            	mDeviceItemDefStr = spinnerList.get(arg2);
	                
	            }    
	            public void onNothingSelected(AdapterView<?> arg0) {    
	                // TODO Auto-generated method stub    
	             
	            }    
	        });   
		}
        mCheckbox = (CheckBox)findViewById(R.id.checkBox1);
        mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub				
				isReverseDetection = arg1;
				
				//如果为true ，需要对listView里的Item数据进行反向排列并显示
				List<Map<String, Object>> OutList = new ArrayList<Map<String, Object>>();
				for(int n =0;n< mMapList.size();n++){
					OutList.add(mMapList.get(n));
				}
				//mMapList.clear();
				SystemUtil.reverseListData(mMapList);
				
				for(int i =0;i< mMapList.size();i++){
					Log.d(TAG, "ListReverse " + i +","+mMapList.get(i).toString());
				}
				mListViewAdapter.notifyDataSetChanged();
			}});
        
        LinearLayout_y = (LinearLayout)findViewById(R.id.y);
        LinearLayout_z = (LinearLayout)findViewById(R.id.z);

        
        mTextViewX = (TextView)findViewById(R.id.x_value);
        mTextViewY = (TextView)findViewById(R.id.y_value);
        mTextViewZ = (TextView)findViewById(R.id.z_value);
        mTextViewCountDown = (TextView)findViewById(R.id.countdown);
        mRadioButton = (RadioButton)findViewById(R.id.radioButton2);
        mJSONArray = new JSONArray();
        
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		InitDataNeeded(0,true);
		initSpinnerData();
		super.onResume();
	}

	void initSpinnerData(){
		try {
			spinnerList = ((myApplication) getApplication()).getDeviceItemDefList(partItemObject);
			if(spinnerList.size()<=1){
				bSpinnerVisible= false;
				mSpinner.setVisibility(Spinner.GONE);
				mItemDefTextView.setVisibility(TextView.VISIBLE);
				mItemDefTextView.setText(spinnerList.get(0));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(spinnerAdapter != null){
		spinnerAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (!bListViewVisible) {
				bListViewVisible = !bListViewVisible;
				needVisible();
				return true;
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_UP: {
			if(!bListViewVisible){
			Intent intent = new Intent();
			intent.setClass(DeviceItemActivity.this, MediaMainActivity.class);
			startActivity(intent);
			return true;
			}
		}

		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	
	Handler myHandler = new Handler() {  
		
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
                  case SPINNER_SELECTCHANGED:
					InitDataNeeded(msg.arg1,true);			
                       break;   
             }   
             super.handleMessage(msg);   
        }   
   };  
   
   /**
    * 
    * @param itemIndex :spinner widget index
    * @param updateAdapter:是否要重新裝載adapter data
    */
   void InitDataNeeded(int itemIndex,boolean updateAdapter){
	   try {
		   partItemObject = ((myApplication) getApplication()).getPartItemObject(mStationIndex,mDeviceIndex);
		   Log.d(TAG, "partItemDataList IS " + partItemObject.toString());
		   List<Object> deviceItemList = ((myApplication) getApplication()).getDeviceItemList(mStationIndex);
		   
		   mCurrentDeviceObject = deviceItemList.get(mDeviceIndex);
		   mDeviceQueryNameStr =   ((myApplication) getApplication()).getDeviceQueryNumber(mCurrentDeviceObject);
		   Log.d(TAG, "mCurrentDeviceObject IS " + mCurrentDeviceObject.toString());
		   mPartItemSelectedList = ((myApplication) getApplication()).getPartItemListByItemDef(partItemObject,itemIndex);
			
		   if(updateAdapter){
			   mMapList.clear();
			   }
		   if(mBValue == null){
			   mBValue = new boolean[mPartItemSelectedList.size()];
			   for(int i = 0; i < mPartItemSelectedList.size(); i++){
				   mBValue[i]=false;
				   }
		   }
			for (int i = 0; i < mPartItemSelectedList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();				
				map.put(CommonDef.check_item_info.UNIT_NAME, ((myApplication) getApplication())
						.getPartItemCheckUnitName(mPartItemSelectedList.get(i),CommonDef.partItemData_Index.PARTITEM_UNIT_NAME));
				//checkname
				map.put(CommonDef.check_item_info.NAME, ((myApplication) getApplication())
						.getPartItemCheckUnitName(mPartItemSelectedList.get(i),CommonDef.partItemData_Index.PARTITEM_CHECKPOINT_NAME));
				map.put(CommonDef.check_item_info.DATA_TYPE, ((myApplication) getApplication())
						.getPartItemCheckUnitName(mPartItemSelectedList.get(i),CommonDef.partItemData_Index.PARTITEM_DATA_TYPE_NAME));
				
				map.put(CommonDef.check_item_info.VALUE, ((myApplication) getApplication())
						.getPartItemCheckUnitName(mPartItemSelectedList.get(i),CommonDef.partItemData_Index.PARTITEM_ADDITIONAL_INFO_NAME));
				
				map.put(CommonDef.check_item_info.DEADLINE, ((myApplication) getApplication())
						.getPartItemCheckUnitName(mPartItemSelectedList.get(i),CommonDef.partItemData_Index.PARTITEM_CHECKED_TIME));

				//已检查项的检查数值怎么保存？并显示出来,
				//已巡检的项的个数统计，暂时由是否有巡检时间来算，如果有的话，即已巡检过了，否则为未巡检。
				//map.put(CommonDef.check_item_info.DEADLINE, "2015-06-20 10:00");				
				mMapList.add(map);
			}
			if(updateAdapter){
				 if(mPartItemSelectedList.size()>0){
					   mListViewAdapter.notifyDataSetChanged();
				   }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
   }   
   /**
    * 根据巡检项的数据种类判断需要显示的哪个布局
    */
   private void checkUnit_ViewControl(){
	   switch(mCheckUnit_DataType){
	   case CommonDef.checkUnit_Type.ACCELERATION:
	   case  CommonDef.checkUnit_Type.SPEED:
		  // 需要方向
		   LinearLayout_y.setVisibility(View.VISIBLE);
		   LinearLayout_z.setVisibility(View.VISIBLE);
		   mButton_Direction.setEnabled(true);
		   break;
	   case CommonDef.checkUnit_Type.TEMPERATURE:
	   case CommonDef.checkUnit_Type.ROTATIONAL_SPEED:
		  //方向按鈕隱藏
		   mButton_Direction.setEnabled(false);
		   LinearLayout_y.setVisibility(View.GONE);
		   LinearLayout_z.setVisibility(View.GONE);
		   break;
	   case CommonDef.checkUnit_Type.RECORD:
		   break;
		   default:
			   break;
	   }
	   genRandomXYZ_temperation();
   }
   private void needVisible(){
	   if(bListViewVisible){
		   mListView.setVisibility(ListView.VISIBLE);
		   if(bSpinnerVisible){
		   mSpinner.setVisibility(Spinner.VISIBLE);
		   mItemDefTextView.setVisibility(View.GONE);
		   }else{			
			   mItemDefTextView.setText(spinnerList.get(0));
		   }
		   mCheckbox.setVisibility(CheckBox.VISIBLE);
		   mUnitcheck_Vibrate.setVisibility(View.GONE);
		   InitDataNeeded(0,true);
		   initSpinnerData();
	   }else{
		   mUnitcheck_Vibrate.setVisibility(View.VISIBLE);
		   mListView.setVisibility(ListView.GONE);
		   mSpinner.setVisibility(Spinner.GONE);
		   mCheckbox.setVisibility(CheckBox.GONE);
		   mItemDefTextView.setVisibility(View.VISIBLE);
		   checkUnit_ViewControl();
		   if(mCheckUnitNameStr == null ){
			   if(mCheckItemNameStr!=null){
				   mItemDefTextView.setText(getString(R.string.checkitem_name) + mCheckItemNameStr ); 
			   }else{
				   mItemDefTextView.setText(getString(R.string.checkitem_name) );
			   }
		   }
		   if(mCheckUnitNameStr != null && mCheckItemNameStr!=null){
		   mItemDefTextView.setText(getString(R.string.checkitem_name) + mCheckUnitNameStr +":"+mCheckItemNameStr );
		   }
		   Log.d(TAG,"deviceItemActivity needVisible() mCheckUnitNameStr is " +mCheckUnitNameStr +",mCheckItemNameStr is "+mCheckItemNameStr);
		   
	   }
   }
   
	private void SaveData() throws JSONException {
		bHasFinishChecked = true;
		
		AuxiliaryInfoNode node = new AuxiliaryInfoNode();
		node.set(AuxiliaryInfoNode.KEY_Index, 8);

		node.set(AuxiliaryInfoNode.KEY_Date, SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

		node.set(AuxiliaryInfoNode.KEY_GUID, SystemUtil.createGUID());	

		node.set(AuxiliaryInfoNode.KEY_TurnNumber, ((myApplication) getApplication()).mTurnNumber);

		node.set(AuxiliaryInfoNode.KEY_WorkerNumber, ((myApplication) getApplication()).mWorkerNumber);

		node.set(AuxiliaryInfoNode.KEY_StartTime, ((myApplication) getApplication()).mTurnStartTime);

		node.set(AuxiliaryInfoNode.KEY_EndTime, ((myApplication) getApplication()).mTurnEndTime);
Log.d(TAG,"SaveData() turnNumber is " + ((myApplication) getApplication()).mTurnNumber + ",startTime is "+ ((myApplication) getApplication()).mTurnStartTime
		+",endTime is "+ ((myApplication) getApplication()).mTurnEndTime);
		((myApplication) getApplication()).setAuxiliaryNode(mRouteIndex,
				node.getObject());
		
		JSONObject deviceObject = (JSONObject) mCurrentDeviceObject;
		deviceObject.put("IsChecked", 1);
		deviceObject.put("GroupName", 1);
		deviceObject.put("WorkerName", 1);
		deviceObject.put("WorkerNumber", 1);
		
		if(mDeviceItemDefStr == null){						
			deviceObject.put("ItemDef", spinnerList.get(0));
		}else{
			deviceObject.put("ItemDef", mDeviceItemDefStr);
		}		
		((myApplication) getApplication()).SaveData(mRouteIndex,((myApplication) getApplication()).genXJFileName());
	}
	String mCheckValue = null;
	/**
	 * 保存当前的 巡检项巡检结果
	 */
	void saveCheckedItemNode(){
		if((mCheckIndex < (mListView.getCount()))){
			//先保存当前测试项的数据
			JSONObject json = (JSONObject) mPartItemSelectedList.get(mCheckIndex);
			Log.d(TAG, "saveCheckedItemNode(),"+json);
			
			//添加巡检结果到结果中，便于形成最后的结果。
			json = ((myApplication) getApplication()).setPartItem_ItemDef(json,0,mCheckValue+SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM)+"*3");
			Log.d(TAG, "saveCheckedItemNode() result is,"+json);			
			mJSONArray.put(json);
			mBValue[mCheckIndex]= true;
		}
	}
	/**
	 * 显示指定某一项的巡检项
	 * @param index
	 */
	void displayItemNode(int index ){
		if((mCheckIndex < (mListView.getCount()))){
		JSONObject json = null;
		HashMap<String, String> map = (HashMap<String, String>) mListView.getItemAtPosition(index);
		mCheckUnit_DataType = Integer.parseInt(map.get(CommonDef.check_item_info.DATA_TYPE));
		
		//如果是测温的话，需要解析上中下限的数据，来显示温度颜色
		if(mCheckUnit_DataType  == CommonDef.checkUnit_Type.TEMPERATURE){
			json = (JSONObject) mPartItemSelectedList.get(index);

			Temperature tmp = ((myApplication) getApplication()).getPartItemTemperatrue(json);
			mMax_temperature =		tmp.max;
			mMid_temperature =tmp.mid;
			mMin_temperature =tmp.min;
	
		}
		mCheckItemNameStr = map.get(CommonDef.check_item_info.NAME);
		mCheckUnitNameStr = map.get(CommonDef.check_item_info.UNIT_NAME);
		Log.d(TAG, "partitemdata data type =" + mCheckUnit_DataType);
		
		needVisible();
		}
	}
   /**
    * 点击下一项时触发的VIEW变化，显示当前测试项的下一项，
    * 如果是设备最后一个测试项，保存，并切换到下一设备的第一个测试项。
    */
	private void nextCheckItem() {
		//startCountDown();		
		saveCheckedItemNode();
		//iCheckedCount++;
		Log.d(TAG, "nextCheckItem(),iCheckedCount ="+iCheckedCount +",mCurrentCheckIndex ="+mCheckIndex);
		mCheckIndex++;
		displayItemNode(mCheckIndex);
		int count = 0 ;
		for(int i = 0; i <mBValue.length;i++){
			if(mBValue[i]){
				Log.d(TAG, "nextCheckItem() i = "+i );
				count++;	
			}
		}
//		if(count ==(mListView.getCount()-1)){
//			
//		}else{
//			mButton_Next.setText(getString(R.string.next_item));
//		}
		if(count ==(mListView.getCount())){
			mButton_Next.setText(getString(R.string.save_and_finish));
			try {
					SaveData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(getApplicationContext(),
						getString(R.string.save_device_checkdata),
						Toast.LENGTH_SHORT).show();
				finish();
			}
	}
	

	private float mMax_temperature =0;
	private float mMid_temperature =0;
	private float mMin_temperature =0;
	
     @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			Toast.makeText(getApplicationContext(), "点了Enter", 0).show();
		} else {
//			Toast.makeText(getApplicationContext(), "点了" + event.getKeyCode(),
//					0).show();
			// A-Z,0-9,Enter,Delete有效，输入中文的时候，点击键盘，无效
		}
		return super.dispatchKeyEvent(event);
	}

   String[] direction_item={"X-Y","X-Z","Y-Z"};
    @Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.position:
		{
			//需要新建立一个数字按键的VIEW
			InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			imm.showSoftInput(mButtion_Position, InputMethodManager.SHOW_IMPLICIT); 
		}
			break;
		case R.id.next:
			nextCheckItem();
			break;		
		case R.id.measurement:
			break;
		case R.id.direction:
			 new AlertDialog.Builder(DeviceItemActivity.this)
	         .setTitle(getString(R.string.direction_select))
	         .setItems(direction_item, new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) {
	            	 mButton_Direction.setText(direction_item[which]);
	            	 //获取选择的项,X-Y,X-Z,Y-Z
	             Toast info =Toast.makeText(DeviceItemActivity.this, direction_item[which],Toast.LENGTH_LONG);
	                 info.setMargin(0.0f, 0.3f);
	                 info.show();
	             }
	         }).create().show();
			break;	
		}
	}
	
    //临时生成随机的三维坐标数据及温度数据。
    void genRandomXYZ_temperation(){
    	int max_xyz=360;
    	float max_temperation=300;
    	float MAX_TEMP = mMax_temperature;
    	float MID_TEMP = mMid_temperature;
    	float LOW_TEMP = mMin_temperature;
   
    	int x = (int) (Math.random()*max_xyz);
    	int y = (int) (Math.random()*max_xyz);
    	int z = (int) (Math.random()*max_xyz);
    	float temp = (int) (Math.random()*max_temperation);
    	
    	mTextViewY.setText(String.valueOf(y));
    	mTextViewZ.setText(String.valueOf(z));
    	if(LinearLayout_y.getVisibility() == View.VISIBLE){
    	mTextViewX.setText(getString(R.string.x)+String.valueOf(x));
    	}else{
    	mTextViewX.setText(String.valueOf(temp)+"°C");
    	}
    	
    	
    	if((temp < MAX_TEMP) && (temp>=MID_TEMP) ){
    		mRadioButton.setBackgroundColor(Color.YELLOW);
    		
    	}else if((temp >= LOW_TEMP) && (temp<MID_TEMP)){
    		mRadioButton.setBackgroundColor(Color.BLACK);
    	}else if(temp <LOW_TEMP){
    		mRadioButton.setBackgroundColor(Color.GRAY);
    	}else if(temp>=MAX_TEMP){
    		mRadioButton.setBackgroundColor(Color.RED);
    	}
    	mCheckValue = x+","+y+","+z + ","+temp+"*";
    }
    
    /**
     * 倒计时显示，每个测试项需要先稳定30秒，之后才可以巡检，确保数据的稳定性
     */
    private void startCountDown(){
    	mCountDown =mMaxSecond_StartMeasure;
		handler.postDelayed(runnable, 1000);
    }
    Handler handler = new Handler(); 
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			mCountDown--;
			if (mCountDown >= 0) {
				mTextViewCountDown.setText(String.format(
						getString(R.string.measurement_countdown), mCountDown));
				handler.postDelayed(this, 1000);
				if (mCountDown == 0) {
					mButton_Measurement.setEnabled(true);
				}
			}

		}
	}; 

}
