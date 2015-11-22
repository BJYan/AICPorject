package com.aic.aicdetactor.fragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.Event.Event;
import com.aic.aicdetactor.abnormal.AbnormalConst;
import com.aic.aicdetactor.abnormal.AbnormalInfo;
import com.aic.aicdetactor.acharEngine.AverageTemperatureChart;
import com.aic.aicdetactor.acharEngine.ChartBuilder;
import com.aic.aicdetactor.acharEngine.IDemoChart;
import com.aic.aicdetactor.adapter.CommonViewPagerAdapter;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.check.ElectricParameteActivity;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.PartItemContact;
import com.aic.aicdetactor.data.AbnomalGradeIdConstant;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.SystemUtil;

/**
 * 速度04、 位移05、 加速度03 公用的UI
 * @author AIC
 *
 */
public class MeasureVibrateFragment extends MeasureBaseFragment  implements OnButtonListener{

	private ListView mListView = null;
	private ImageView mImageView = null;
	//private GridView mGridView = null;
	private RadioButton mRadioButton = null;
	private TextView mResultTipStr = null;
	private OnVibateListener mCallback = null;
	private SimpleAdapter mListViewAdapter = null;
	private int mIndex = -1;
	private List<Map<String, Object>> mMapList;
	private TextView mXTextView  = null;
	private TextView mYTextView  = null;
	private TextView mZTextView  = null;
	private TextView mTimeTextView  = null;
	private TextView mColorTextView  = null;
	private TextView mDeviceNameTextView = null;
	private String TAG = "luotest";
	private ImageView mHistoryImageView = null;
	private LinearLayout MYLinear = null;
	private LinearLayout MZLinear = null;
	PartItemListAdapter AdapterList;
	TextView mTimeTV=null;
	private float mCheckValue =0.0f;
	
	List<View> views;
	LayoutInflater mInflater;
	long mReceiveDataLenth=0;
	byte mDLCMD=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"Vibrate_fragment :onCreate()");
		// TODO Auto-generated method stub
		mMapList = new ArrayList<Map<String, Object>>();
		views = new ArrayList<View>();
		mInflater = getActivity().getLayoutInflater();
		//初始化ListVew 数据项
		String [] arraryStr = new String[]{this.getString(R.string.electric_device_parameters),
				this.getString(R.string.electric_device_spectrum)};
			for (int i = 0; i < arraryStr.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();				
				map.put(CommonDef.check_item_info.NAME,arraryStr[i] );								
				map.put(CommonDef.check_item_info.DEADLINE, "2015-06-20 10:00");

				//已检查项的检查数值怎么保存？并显示出来
				//已巡检的项的个数统计，暂时由是否有巡检时间来算，如果有的话，即已巡检过了，否则为未巡检。
				mMapList.add(map);
			}
			
			super.onCreate(savedInstanceState);
	}
 
	public  MeasureVibrateFragment(PartItemListAdapter AdapterList){
		this.AdapterList = AdapterList;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"Vibrate_fragment:onCreateView()");
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		ViewPager view = (ViewPager) inflater.inflate(R.layout.brivate_fragment_layout, container, false);
		views.add(mInflater.inflate(R.layout.brivate, null));
		views.add(mInflater.inflate(R.layout.chart_thr_charts1_layout, null));
		views.add(mInflater.inflate(R.layout.chart_thr_charts2_layout, null));
		views.add(mInflater.inflate(R.layout.chart_one_charts_layout, null));
		CommonViewPagerAdapter DialogPagerAdapter = new CommonViewPagerAdapter(getActivity(),views);
		view.setAdapter(DialogPagerAdapter);
		
		mListView = (ListView)views.get(0).findViewById(R.id.listView1);
		mListViewAdapter = new SimpleAdapter(this.getActivity().getApplicationContext(), mMapList,
				R.layout.checkunit, new String[] { 			
				CommonDef.check_item_info.NAME,//巡检项名称		
				CommonDef.check_item_info.DEADLINE, //巡检最近时间			
				}, new int[] {				
						R.id.pathname,				
						R.id.deadtime});
		mListView.setAdapter(mListViewAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if(arg2 == 0){
					Intent intent = new Intent();					
					 intent.setClass(MeasureVibrateFragment.this.getActivity(),ElectricParameteActivity.class);
					 startActivity(intent);
				}else{
					Intent intent = null;
					IDemoChart[] mCharts = new IDemoChart[] {
							 new AverageTemperatureChart()};
				     // intent = new Intent(this, TemperatureChart.class);
				      intent = mCharts[0].execute(MeasureVibrateFragment.this.getActivity(),"test");
				    startActivity(intent);
				}
				 
				
			}
		});
		
		mImageView = (ImageView)views.get(0).findViewById(R.id.imageView1);
		
		mRadioButton = (RadioButton)views.get(0).findViewById(R.id.colorRadio);
		mResultTipStr = (TextView)views.get(0).findViewById(R.id.textiew1);
		
		mXTextView = (TextView)views.get(0).findViewById(R.id.x_value);
		
		mDeviceNameTextView = (TextView)views.get(0).findViewById(R.id.check_name);
		mDeviceNameTextView.setText(getPartItemName());
		mYTextView = (TextView)views.get(0).findViewById(R.id.y_value);
		mZTextView = (TextView)views.get(0).findViewById(R.id.z_value);
		mTimeTextView = (TextView)views.get(0).findViewById(R.id.time_value);
		MYLinear = (LinearLayout)views.get(0).findViewById(R.id.y_linear);
		MZLinear = (LinearLayout)views.get(0).findViewById(R.id.z_linear);
		mTimeTV = (TextView)views.get(0).findViewById(R.id.time);
		if(mPartItemData.Axle_Number==0){
			MYLinear.setVisibility(View.GONE);			
		}
		if(mPartItemData.Axle_Number==1){
			MYLinear.setVisibility(View.GONE);
			MZLinear.setVisibility(View.GONE);
		}
		if(mPartItemData.Axle_Number==2){
			MZLinear.setVisibility(View.GONE);	
		}
		mColorTextView = (TextView)views.get(0).findViewById(R.id.colordiscrip);
		initDisplayData();
		AdapterList.getCurrentPartItem().setSartDate();
		return view;
	}
	
	private void InitChart() {
		// TODO Auto-generated method stub
		ChartBuilder chartBuilder = new ChartBuilder(getActivity());
		LinearLayout timeChartViewY = (LinearLayout) views.get(1).findViewById(R.id.dialog_thr_chart_first_chart);
		LinearLayout timeChartViewX = (LinearLayout) views.get(1).findViewById(R.id.dialog_thr_chart_sec_chart);
		LinearLayout timeChartViewZ = (LinearLayout) views.get(1).findViewById(R.id.dialog_thr_chart_thr_chart);
		
		LinearLayout axesChartViewX = (LinearLayout) views.get(2).findViewById(R.id.dialog_thr_chart2_first_chart);
		LinearLayout axesChartViewY = (LinearLayout) views.get(2).findViewById(R.id.dialog_thr_chart2_sec_chart);
		LinearLayout axesChartViewZ = (LinearLayout) views.get(2).findViewById(R.id.dialog_thr_chart2_thr_chart);
		
		LinearLayout frequencyChartView = (LinearLayout) views.get(3).findViewById(R.id.onechart);
		
		//DataAnalysis dataAnalysis = new DataAnalysis();
		float[] data = dataAnalysis.getData();
		
		timeChartViewY.addView(chartBuilder.getBlackLineChartView("test", data));
		timeChartViewX.addView(chartBuilder.getBlackLineChartView("test", data));
		timeChartViewZ.addView(chartBuilder.getBlackLineChartView("test", data));
		
		axesChartViewX.addView(chartBuilder.getBlackLineChartView("test", data));
		axesChartViewY.addView(chartBuilder.getBlackLineChartView("test", data));
		axesChartViewZ.addView(chartBuilder.getBlackLineChartView("test", data));
		
		frequencyChartView.addView(chartBuilder.getBlackLineChartView("test", data));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG,"Vibrate_fragment:onViewCreated()");
		super.onViewCreated(view, savedInstanceState);
		
		//根据传入的PartItemIndex 索引 获取显示的数据项
		Bundle args = this.getArguments();
//		 partItemObject = ((myApplication) getApplication()).getPartItemObject(mStationIndex,mDeviceIndex);
//		   Log.d(TAG, "partItemDataList IS " + partItemObject.toString());
//		   List<Object> deviceItemList = ((myApplication) getApplication()).getDeviceItemList(mStationIndex);
//		   
//		   mCurrentDeviceObject = deviceItemList.get(mDeviceIndex);
//		   mDeviceQueryNameStr =   ((myApplication) getApplication()).getDeviceQueryNumber(mCurrentDeviceObject);
//		   Log.d(TAG, "mCurrentDeviceObject IS " + mCurrentDeviceObject.toString());
//		   mPartItemSelectedList = ((myApplication) getApplication()).getPartItemListByItemDef(partItemObject,itemIndex);
//			
//		   if(updateAdapter){
//			   mMapList.clear();
//			   }
//		   if(mBValue == null){
//			   mBValue = new boolean[mPartItemSelectedList.size()];
//			   for(int i = 0; i < mPartItemSelectedList.size(); i++){
//				   mBValue[i]=false;
//				   }
//		   }
		
		//measureAndDisplayData();
		//handler.postDelayed(runnable, 500);
		displayPic(null);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	  @Override
		public void onStart() {
			// TODO Auto-generated method stub
		  
		  displayPic(null);
			super.onStart();
		}
	  
	Handler handler = new Handler(); 
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			measureAndDisplayData();
		}
	}; 
	
	void displayPic(Uri path){
		if(path == null ){
			return ;
		}
		   try {  
               // Bundle extra = data.getExtras(); 
              //  Bitmap bmp = (Bitmap)data.getExtras().get("data");
                Log.d(TAG, "displayPic()  1"); 
              
                //首先取得屏幕对象  
                Display display = this.getActivity().getWindowManager().getDefaultDisplay();  
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
                Log.d("test", "displayPic()  2"); 
                //op.inSampleSize = 8;  
                op.inJustDecodeBounds = true;  
                //Bitmap pic = BitmapFactory.decodeFile(imageFilePath, op);//调用这个方法以后，op中的outWidth和outHeight就有值了  
                //由于使用了MediaStore存储，这里根据URI获取输入流的形式  
                Bitmap pic = BitmapFactory.decodeStream(this.getActivity()
                        .getContentResolver().openInputStream(path),  
                        null, op);  
                int wRatio = (int) Math.ceil(op.outWidth / (float) dw); //计算宽度比例  
                int hRatio = (int) Math.ceil(op.outHeight / (float) dh); //计算高度比例  
                Log.d(TAG, wRatio + "wRatio");  
                Log.d(TAG, hRatio + "hRatio");  
                /** 
                 * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。 
                 * 如果高和宽不是全都超出了屏幕，那么无需缩放。 
                 * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》 
                 * 这需要判断wRatio和hRatio的大小 
                 * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。 
                 * 缩放使用的还是inSampleSize变量 
                 */  
                Log.d(TAG, "displayPic()  3"); 
                if (wRatio > 1 && hRatio > 1) {  
                    if (wRatio > hRatio) {  
                        op.inSampleSize = wRatio;  
                    } else {  
                        op.inSampleSize = hRatio/2;  
                    }  
                }  
                op.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了  
                pic = BitmapFactory.decodeStream(this.getActivity().getContentResolver().openInputStream(path), null, op);  
                mImageView.setImageBitmap(pic);  
            } catch (Exception e) {  
            	Log.d(TAG,"displayPic() Exception "+e.toString());
                e.printStackTrace();  
            }   
	}
	 @Override
	protected void initDisplayData(){
		mXTextView.setText(getPartItemData());
		if(mPartItemData.T_Item_Abnormal_Grade_Id!=AbnomalGradeIdConstant.NORMAL){
			mXTextView.setTextColor(Color.RED);
		}else{
			mXTextView.setTextColor(Color.BLACK);
		}
	}
	
    void measureAndDisplayData(){    
    	double MAX = super.mPartItemData.Up_Limit;
    	double MID = super.mPartItemData.Middle_Limit;
    	double LOW = super.mPartItemData.Down_Limit;
    	
    	
//		mCheckValue = (int) (Math.random()*max_temperation);
    	
    	switch(mPartItemData.Axle_Number){
    	case 1:
    		break;
    	case 2:
    		break;
    	}
    	mXTextView.setText(String.valueOf(mCheckValue));
    	if((mCheckValue < MAX) && (mCheckValue>=MID) ){
    		mRadioButton.setBackgroundColor(Color.YELLOW);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.warning));
    		
    	}else if((mCheckValue >= LOW) && (mCheckValue<MID)){
    		mRadioButton.setBackgroundColor(Color.BLACK);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.normal));
    	}else if(mCheckValue <LOW){
    		mRadioButton.setBackgroundColor(Color.GRAY);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.invalid));
    	}else if(mCheckValue>=MAX){
    		mRadioButton.setBackgroundColor(Color.RED);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.dangerous));
    	}
    }

	
    
    
    public interface OnVibateListener{
    	
    	void OnClick(int genPartItemDataCounts,int xValue,int yValue,int zValue);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnVibateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVibateListener");
        }
    }
    
    void saveData(PartItemListAdapter adapter){
    	double MAX = super.mPartItemData.Up_Limit;
    	double MID = super.mPartItemData.Middle_Limit;
    	double LOW = super.mPartItemData.Down_Limit;
    	int isNormal=0;
    	int AbnormalId=0;
    	String Abnormalcode="-1";
    	
    	if((mCheckValue < MAX) && (mCheckValue>=MID) ){
    		isNormal=AbnormalConst.Abnormal;
    		AbnormalId=AbnormalConst.JingGao_Id;
    		Abnormalcode=AbnormalConst.JingGao_Code;
    		
    	}else if((mCheckValue >= LOW) && (mCheckValue<MID)){
    		isNormal=AbnormalConst.Normal;
    		AbnormalId=AbnormalConst.ZhengChang_Id;
    		Abnormalcode=AbnormalConst.ZhengChang_Code;
    	}else if(mCheckValue <LOW){
    		isNormal=AbnormalConst.Abnormal;
    		AbnormalId=AbnormalConst.WuXiao_Id;
    		Abnormalcode=AbnormalConst.WuXiao_Code;
    	}else if(mCheckValue>=MAX){
    		isNormal=AbnormalConst.Abnormal;
    		AbnormalId=AbnormalConst.WeiXian_Id;
    		Abnormalcode=AbnormalConst.WeiXian_Code;
    	}    	
		
    	adapter.saveData(String.valueOf(mCheckValue),isNormal,Abnormalcode,AbnormalId);
    }

	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter adapter,String Value,int measureOrSave) {
		// TODO Auto-generated method stub
		switch(measureOrSave){
		case PartItemContact.SAVE_DATA:
			saveData(adapter);
			break;
		case PartItemContact.MEASURE_DATA:
	//		if(mCanSendCMD){
				getDataFromBLE();
				mCanSendCMD=false;
				mReceiveDataLenth=0;
				mStrReceiveData.delete(0, mStrReceiveData.length());
		//	}
			
			break;
		}
		
	}

	@Override
	protected Handler getHandler() {
		// TODO Auto-generated method stub
		//return super.getHandler();
		return mHandle;
	}
	void getDataFromBLE(){
		BLEControl.setParamates(mHandle);
		mDLCMD=(byte) 0xd1;
		byte[]cmd=BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) mDLCMD, (byte)1, (byte)1);
		super.BLEControl.Communication2Bluetooth(BLEControl.getSupportedGattServices(),cmd);
	}
	DataAnalysis dataAnalysis;
	StringBuffer mStrReceiveData=new StringBuffer();;
	String mStrLastReceiveData="";
	int iFailedTime =0;
	public Handler mHandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case BluetoothLeControl.MessageReadDataFromBT:
				byte[]strbyte=(byte[]) (msg.obj);
				String str= SystemUtil.bytesToHexString(strbyte);
				int count=msg.getData().getInt("count");
				if(mReceiveDataLenth==0){
					mReceiveDataLenth =DataAnalysis.getReceiveDataLenth(str, mDLCMD);
					}
				
				if(mReceiveDataLenth == mStrReceiveData.append(str.toString()).length()){
					handler.sendMessage(handler.obtainMessage(BluetoothLeControl.Message_End_Upload_Data_From_BLE));
				}else{						
					
					Log.d(TAG, "HandleMessage() count ="+count +" mStrReceiveData is " +mStrReceiveData.length()+","+mStrReceiveData.toString());
				}
				Log.d(TAG, "receive count="+count +", mReceiveDataLenth = "+mReceiveDataLenth +",mStrReceiveData lenth= " +mStrReceiveData.length()+","+mStrReceiveData.toString());
				break;
			case BluetoothLeControl.Message_Stop_Scanner:
				mTimeTV.setVisibility(View.VISIBLE);
				mTimeTV.setText("正在测量中");
				break;
			case BluetoothLeControl.Message_End_Upload_Data_From_BLE:
				mTimeTV.setText("测量完毕");
				if(mStrReceiveData.length()>0){
					mStrLastReceiveData = mStrReceiveData.toString();
					InsertMediaData(mStrLastReceiveData,true);					
					if(dataAnalysis==null){
						dataAnalysis = new DataAnalysis();
					}
					int k = dataAnalysis.isValidate(mStrLastReceiveData,mDLCMD);
					mStrReceiveData.delete(0, mStrReceiveData.length());
					Log.d(TAG, "receive data lenth = "+mStrLastReceiveData.length() +"and ValidValue ="+k);
					if(k!=0){
						String strErr = mColorTextView.getText().toString();
						if(!strErr.contains("数据丢失")){
							strErr=strErr+"数据丢失,请重测";
							;
						}else{
							iFailedTime++;
						}
						mColorTextView.setText("数据丢失,请重测"+" " +iFailedTime);
						
						return ;
					}else{
						iFailedTime=0;
						dataAnalysis.getResult();
					dataAnalysis.getAXCount();					
					dataAnalysis.getCaiYangFrequency();
					dataAnalysis.getCRC32();
					dataAnalysis.getDataNum();
					dataAnalysis.getDataPointCount();
					dataAnalysis.getFabsMaxValue();
					dataAnalysis.getFengFengValue();
					
					mCheckValue=dataAnalysis.getValidValue();
					dataAnalysis.getWaveData();
					measureAndDisplayData();
					String Wavedata=dataAnalysis.getWaveData().toString();
					InsertMediaData(Wavedata,false);
					
					ifNeedAnalysisData((byte)msg.arg1);
					}
				}else{
					Log.d(TAG,"mStrReceiveData is null");
				}
				
				break;
			case BluetoothLeControl.Message_Connection_Status:
				switch(msg.arg1){
				case 1://BLE has connected
					break;
				case 0://BLE has disconnected
					break;
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 只针对采集D1类型的数据进行图形分析
	 * @param type
	 */
void ifNeedAnalysisData(byte type){
	if(type != BluetoothConstast.CMD_Type_CaiJi){return ;}
	
	
	float[] data = dataAnalysis.getData();
	float[] MinMaxTemp = new float[]{data[0],data[0]};
	for(int i=0;i<data.length;i++){
		if(data[i]<=MinMaxTemp[0]) MinMaxTemp[0] = data[i];
		if(data[i]>MinMaxTemp[1]) MinMaxTemp[1] = data[i];
	}
	
	InitChart();
	
	UpLoadWaveData(data);
}
	void InsertMediaData(String data,boolean test){
		String guid = SystemUtil.createGUID();
		if(test){
			guid="2.txt";
		}
		try {
			SystemUtil.writeFile("/sdcard/aic/data/"+guid, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RouteDao dao = RouteDao.getInstance(getActivity());
		String StrSql="insert into "+DBHelper.TABLE_Media +" ( "
				+DBHelper.Media_Table.Data_Exist_Guid +","
				+DBHelper.Media_Table.Date +","
				+DBHelper.Media_Table.Is_Updated +","
				+DBHelper.Media_Table.Line_Guid +","
				+DBHelper.Media_Table.Mime_Type +","
				+DBHelper.Media_Table.Name +","
				+DBHelper.Media_Table.Path +","
				+DBHelper.Media_Table.UpdatedDate +") values ('"
				+AdapterList.getCurDeviceExitDataGuid()+"','"
				+SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM)+"','"
				+"0"+"','"
				+super.app.mLineJsonData.T_Line.T_Line_Guid+"','"
				+"image"+"','"
				+guid+"','"				
				+"/sdcard/aic/data/"+guid+"','"
				+"0"+"');";
				
		dao.execSQL(StrSql);
	}
	
	void UpLoadWaveData(float[] testData){
		 byte[] bytedata=new byte[testData.length*4];;
		 for(int i=0;i<testData.length;i++){
			 byte[] data=SystemUtil.float2byte(testData[i]);
			 for(int k=0;k<data.length;k++){
				 bytedata[i*4+k]=data[k];
			 }
		 }
		 
		 
		Event.UploadWaveDataRequestInfo_Event(null,null,null,null,bytedata);
	}
}
