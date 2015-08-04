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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.AuxiliaryInfoNode;
import com.aic.aicdetactor.data.Temperature;
import com.aic.aicdetactor.fragment.Observer_fragment;
import com.aic.aicdetactor.fragment.Observer_fragment.OnMediakListener;
import com.aic.aicdetactor.fragment.Temperature_fragment;
import com.aic.aicdetactor.fragment.Temperature_fragment.OnMeasureListener;
import com.aic.aicdetactor.fragment.Vibrate_fragment;
import com.aic.aicdetactor.fragment.Vibrate_fragment.OnVibateListener;
import com.aic.aicdetactor.media.MediaMainActivity;
import com.aic.aicdetactor.media.SoundRecordActivity;
import com.aic.aicdetactor.util.SystemUtil;


public class PartItemActivity extends FragmentActivity implements OnClickListener,
    OnVibateListener,
	OnMediakListener,
	OnMeasureListener {

	//private ListView mListView = null;
	String TAG = "luotest";
//	private Spinner mSpinner = null;
	private TextView mItemDefTextView = null;//当只有一项时才显示
	private String mCheckItemNameStr = null;//检查项名
	private String mCheckUnitNameStr = null;//检查部件名称
	
	private ArrayAdapter<String> spinnerAdapter;
	private int mLastSpinnerIndex = 0;
	private Object partItemObject = null;
	private Object mCurrentDeviceObject = null;
//	private List<Map<String, Object>> mMapList;
	public final int SPINNER_SELECTCHANGED =0;
	private SimpleAdapter mListViewAdapter = null;
	
	private List<Object> mPartItemSelectedList=null;
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
	private JSONArray mJSONArray = null;
	//private String mDeviceNameStr= null;
	//private String mDeviceQueryNameStr = null;
	private String mDeviceItemDefStr = null;
	private boolean []mBValue = null;	//巡检的结果
	

	private OnButtonListener mFragmentCallBack = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.unitcheck);

		Intent intent = getIntent();
		mRouteIndex = ((myApplication) getApplication()).mRouteIndex;
		mStationIndex = ((myApplication) getApplication()).mStationIndex;
		mDeviceIndex = ((myApplication) getApplication()).mDeviceIndex;
	
		
		//mDeviceNameStr = ((myApplication) getApplication()).gDeviceName;
		
		mCheckUnit_DataType = intent.getExtras().getInt(CommonDef.check_item_info.DATA_TYPE );
		mPartItemCounts = intent.getExtras().getInt(CommonDef.check_item_info.ITEM_COUNTS );

		TextView planNameTextView = (TextView) findViewById(R.id.routeName);
		planNameTextView.setText(((myApplication) getApplication()).gRouteClassName);

		TextView RouteNameTextView = (TextView) findViewById(R.id.routeName);
		RouteNameTextView.setText(((myApplication) getApplication()).gRouteName);

		TextView stationTextView = (TextView) findViewById(R.id.stationName);
		stationTextView.setText(((myApplication) getApplication()).gStationName);
		
		TextView deviceTextView = (TextView) findViewById(R.id.deviceName);
		deviceTextView.setText(((myApplication) getApplication()).gDeviceName);

		ImageView imageView = (ImageView)findViewById(R.id.backImage);
		imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"imageView.setOnClickListener");
				// TODO Auto-generated method stub
				finish();
			}
			
		});

		mButtion_Position = (Button)findViewById(R.id.bottombutton1);
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
		
		mButton_Direction = (Button)findViewById(R.id.bottombutton2);
		mButton_Direction.setOnClickListener(this);
		
		mButton_Next = (Button)findViewById(R.id.next);
		mButton_Next.setOnClickListener(this);
		
		mButton_Measurement = (Button)findViewById(R.id.bottombutton3);
		mButton_Measurement.setOnClickListener(this);
        mJSONArray = new JSONArray();
        mBValue = new boolean[mPartItemCounts];
        initFragment(mCheckUnit_DataType,true);
	}

	

	void initFragment(int type,boolean bFirstInit){
		Log.d(TAG, "initFragment() type is " +type );
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		Toast.makeText(this.getApplicationContext(), " type ="+type, Toast.LENGTH_LONG).show();
		
		  switch(type){
		   case CommonDef.checkUnit_Type.ACCELERATION:
		   case CommonDef.checkUnit_Type.SPEED:	
		   case CommonDef.checkUnit_Type.DISPLACEMENT:
			  // 需要方向
	
			   mButton_Direction.setEnabled(true);
			   mButton_Direction.setText(getString(R.string.direction));
			   {
					Fragment fragment = new Vibrate_fragment(1);
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
		getPartItemData(mPartItemIndex);
		super.onResume();
	}

 
	int getPatItemType(int partItemIndex){
		JSONObject object = null;
		if(mPartItemSelectedList != null){
			object = (JSONObject) mPartItemSelectedList.get(partItemIndex);
		}
		return mCheckUnit_DataType=Integer.parseInt(((myApplication) getApplication())
		.getPartItemCheckUnitName(object,CommonDef.partItemData_Index.PARTITEM_DATA_TYPE_NAME));
		
	}
   void getPartItemData(int itemIndex){
	   try {
		   partItemObject = ((myApplication) getApplication()).getPartItemObject(mStationIndex,mDeviceIndex);
		   Log.d(TAG, "partItemDataList IS " + partItemObject.toString());
		   List<Object> deviceItemList = ((myApplication) getApplication()).getDeviceItemList(mStationIndex);
		   
		   mCurrentDeviceObject = deviceItemList.get(mDeviceIndex);
		 //  mDeviceQueryNameStr =   ((myApplication) getApplication()).getDeviceQueryNumber(mCurrentDeviceObject);
		   Log.d(TAG, "mCurrentDeviceObject IS " + mCurrentDeviceObject.toString());
		   mPartItemSelectedList = ((myApplication) getApplication()).getPartItemListByItemDef(partItemObject,itemIndex);
			
		   
		  
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
				//mMapList.add(map);
			}			
		} catch (Exception e) {
			e.printStackTrace();
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
Log.d(TAG,"SaveData() turnNumber is " 
		+ ((myApplication) getApplication()).mTurnNumber + ",startTime is "
		+ ((myApplication) getApplication()).mTurnStartTime
		+",endTime is "+ ((myApplication) getApplication()).mTurnEndTime);

		((myApplication) getApplication()).setAuxiliaryNode(mRouteIndex,
				node.getObject());
		
		JSONObject deviceObject = (JSONObject) mCurrentDeviceObject;
		deviceObject.put("IsChecked", 1);
		deviceObject.put("GroupName", 1);
		deviceObject.put("WorkerName", 1);
		deviceObject.put("WorkerNumber", 1);
		
		if(mDeviceItemDefStr == null){						
			
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
		if((mCheckIndex < mPartItemCounts)){
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
    * 点击下一项时触发的VIEW变化，显示当前测试项的下一项，
    * 如果是设备最后一个测试项，保存，并切换到下一设备的第一个测试项。
    */
	private void nextCheckItem() {
		if(mCheckIndex < (mPartItemCounts)){
			if(mCheckIndex != (mPartItemCounts-1)){
		getPatItemType(mCheckIndex +1);
		}
		// 先获取数据类型
		initFragment(mCheckUnit_DataType, false);
		
		// startCountDown();
		saveCheckedItemNode();

		Log.d(TAG, "nextCheckItem(),iCheckedCount =" + iCheckedCount
				+ ",mCurrentCheckIndex =" + mCheckIndex);
		mCheckIndex++;
		}else{
			
		}
		int count = 0;
		for (int i = 0; i < mBValue.length; i++) {
			if (mBValue[i]) {
				Log.d(TAG, "nextCheckItem() i = " + i);
				count++;
			}
		}
		if (count == mPartItemCounts) {
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
	private Uri imageFilePath = null;
	private static final int RESULT_CODE = 1; 
     @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			Toast.makeText(getApplicationContext(), "点了Enter", 0).show();
		} 
		return super.dispatchKeyEvent(event);
	}

   String[] direction_item={"X-Y","X-Z","Y-Z"};
    @Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bottombutton1:
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
				Log.d("test", "main_media imageFilePath is " + imageFilePath);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath); // 这样就将文件的存储方式和uri指定到了Camera应用中

				// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, RESULT_CODE);
			} else {
				// 需要新建立一个数字按键的VIEW
				InputMethodManager imm = (InputMethodManager) this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				imm.showSoftInput(mButtion_Position,
						InputMethodManager.SHOW_IMPLICIT);
			}
		}
			break;
		case R.id.next:
			nextCheckItem();
			break;		
		case R.id.bottombutton3:
			if(mButton_Measurement.getText().equals(getString(R.string.textrecord))){
				
			}
			break;
		case R.id.bottombutton2:
			if(mButton_Direction.getText().equals(getString(R.string.soundrecord))){
				Intent intent = new Intent();
				intent.setClass(PartItemActivity.this, SoundRecordActivity.class);
				startActivity(intent);
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
	
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	 Log.d("test", "onActivityResult() 00" + requestCode + ",resultCode= "+resultCode); 
	  if(requestCode == RESULT_CODE){  
		  Log.d("test", "onActivityResult() "); 
            //说明是由Camera返回的数据  
            //由Camera应用返回的图片数据是一个Camera对象，存储在一个名为data的extra域  
            //然后将获取到的图片存储显示在ImageView中  
        try {  
           // Bundle extra = data.getExtras(); 
          //  Bitmap bmp = (Bitmap)data.getExtras().get("data");
            Log.d("test", "onActivityResult()  1"); 
            /** 
             * 然而为了节约内存的消耗，这里返回的图片是一个121*162的缩略图。 
             * 那么如何返回我们需要的大图呢？看上面 
             * 然而存储了图片。有了图片的存储位置，能不能直接将图片显示出来呢》 
             * 这个问题就设计到对于图片的处理和显示，是非常消耗内存的，对于PC来说可能不算什么，但是对于手机来说 
             * 很可能使你的应用因为内存耗尽而死亡。不过还好，Android为我们考虑到了这一点 
             * Android中可以使用BitmapFactory类和他的一个内部类BitmapFactory.Options来实现图片的处理和显示 
             * BitmapFactory是一个工具类，里面包含了很多种获取Bitmap的方法。BitmapFactory.Options类中有一个inSampleSize，比如设定他的值为8，则加载到内存中的图片的大小将 
             * 是原图片的1/8大小。这样就远远降低了内存的消耗。 
             * BitmapFactory.Options op = new BitmapFactory.Options(); 
             * op.inSampleSize = 8; 
             * Bitmap pic = BitmapFactory.decodeFile(imageFilePath, op); 
             * 这是一种快捷的方式来加载一张大图，因为他不用考虑整个显示屏幕的大小和图片的原始大小 
             * 然而有时候，我需要根据我们的屏幕来做相应的缩放，如何操作呢？ 
             *  
             */  
            //首先取得屏幕对象  
            Display display = this.getWindowManager().getDefaultDisplay();  
            //获取屏幕的宽和高  
            int dw = display.getWidth();  
            int dh = display.getHeight();  
            /** 
             * 为了计算缩放的比例，我们需要获取整个图片的尺寸，而不是图片 
             * BitmapFactory.Options类中有一个布尔型变量inJustDecodeBounds，将其设置为true 
             * 这样，我们获取到的就是图片的尺寸，而不用加载图片了。 
             * 当我们设置这个值的时候，我们接着就可以从BitmapFactory.Options的outWidth和outHeight中获取到值 
             */  
            BitmapFactory.Options op = new BitmapFactory.Options(); 
            Log.d("test", "onActivityResult()  2"); 
            //op.inSampleSize = 8;  
            op.inJustDecodeBounds = true;  
            //Bitmap pic = BitmapFactory.decodeFile(imageFilePath, op);//调用这个方法以后，op中的outWidth和outHeight就有值了  
            //由于使用了MediaStore存储，这里根据URI获取输入流的形式  
            Bitmap pic = BitmapFactory.decodeStream(this  
                    .getContentResolver().openInputStream(imageFilePath),  
                    null, op);  
            int wRatio = (int) Math.ceil(op.outWidth / (float) dw); //计算宽度比例  
            int hRatio = (int) Math.ceil(op.outHeight / (float) dh); //计算高度比例  
            Log.d("test", wRatio + "wRatio");  
            Log.d("test", hRatio + "hRatio");  
            /** 
             * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。 
             * 如果高和宽不是全都超出了屏幕，那么无需缩放。 
             * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》 
             * 这需要判断wRatio和hRatio的大小 
             * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。 
             * 缩放使用的还是inSampleSize变量 
             */  
            Log.d("test", "onActivityResult()  3"); 
            if (wRatio > 1 && hRatio > 1) {  
                if (wRatio > hRatio) {  
                    op.inSampleSize = wRatio;  
                } else {  
                    op.inSampleSize = hRatio/2;  
                }  
            }  
            op.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了  
            pic = BitmapFactory.decodeStream(this.getContentResolver()  
                    .openInputStream(imageFilePath), null, op);  
            Bundle bundle =new Bundle();
            bundle.putString("pictureUri", imageFilePath.toString());
            mFragmentCallBack.OnButtonDown(0, bundle);
            //imageView.setImageBitmap(pic);  
        } catch (Exception e) {  
        	Log.d("test","main_media Exception "+e.toString());
            e.printStackTrace();  
        }   
    }  
    }  
   
	@Override
	public void OnClick(String value) {
		// TODO Auto-generated method stub
		Log.d(TAG,"OnClick()IndexButton = "+value);
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
		void OnButtonDown(int buttonId,Bundle bundle);
	};

}
