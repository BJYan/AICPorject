package com.aic.aicdetactor.fragment;
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

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.acharEngine.AverageTemperatureChart;
import com.aic.aicdetactor.acharEngine.IDemoChart;
import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.check.ElectricParameteActivity;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.util.SystemUtil;


public class PartItemMeasureVibrateFragment extends PartItemMeasureBaseFragment  implements OnButtonListener{

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
	private int mZhouCounts = 0;
	private LinearLayout MYLinear = null;
	private LinearLayout MZLinear = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"Vibrate_fragment :onCreate()");
		// TODO Auto-generated method stub
		mZhouCounts =getArguments().getInt(KEY.KEY_ZHOU_COUNTS);
		mMapList = new ArrayList<Map<String, Object>>();
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
 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"Vibrate_fragment:onCreateView()");
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.brivate, container, false);
		mListView = (ListView)view.findViewById(R.id.listView1);
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
					 intent.setClass(PartItemMeasureVibrateFragment.this.getActivity(),ElectricParameteActivity.class);
					 startActivity(intent);
				}else{
					Intent intent = null;
					IDemoChart[] mCharts = new IDemoChart[] {
							 new AverageTemperatureChart()};
				     // intent = new Intent(this, TemperatureChart.class);
				      intent = mCharts[0].execute(PartItemMeasureVibrateFragment.this.getActivity(),"test");
				    startActivity(intent);
				}
				 
				
			}
		});
		
		mImageView = (ImageView)view.findViewById(R.id.imageView1);
		
		mRadioButton = (RadioButton)view.findViewById(R.id.colorRadio);
		mResultTipStr = (TextView)view.findViewById(R.id.textiew1);
		
		mXTextView = (TextView)view.findViewById(R.id.x_value);
		mDeviceNameTextView = (TextView)view.findViewById(R.id.check_name);
		mDeviceNameTextView.setText(getPartItemName());
		mYTextView = (TextView)view.findViewById(R.id.y_value);
		mZTextView = (TextView)view.findViewById(R.id.z_value);
		mTimeTextView = (TextView)view.findViewById(R.id.time_value);
		MYLinear = (LinearLayout)view.findViewById(R.id.y_linear);
		MZLinear = (LinearLayout)view.findViewById(R.id.z_linear);
		if(mZhouCounts==0){
			MYLinear.setVisibility(View.GONE);
			
		}
		if(mZhouCounts==1){
			MYLinear.setVisibility(View.GONE);
			MZLinear.setVisibility(View.GONE);
		}
		if(mZhouCounts==2){
			MZLinear.setVisibility(View.GONE);	
		}
		mColorTextView = (TextView)view.findViewById(R.id.colordiscrip);
		
		return view;
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
		
		//genRandomXYZ();
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
			genRandomXYZ();
		}
	}; 
	
	void parseExternalInfo(){
//    	String[] array = parStr.split(KEY.PARTITEMDATA_SPLIT_KEYWORD);
//		String newValue = array[CommonDef.partItemData_Index.PARTITEM_ADDITIONAL_INFO];
//		mXTextView.setText(newValue);
//		mYTextView.setText(newValue);
//		mZTextView.setText(newValue);
		
    }
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
	  //临时生成随机的三维坐标数据,需要线程来循环
    void genRandomXYZ(){    
    	int max_xyz=360;
    	float max_temperation=300;
    	float MAX = 200;
    	float MID = 100;
    	float LOW = 0;
    	
    	MAX = super.mPartItemData.Up_Limit;
    	MID = super.mPartItemData.Middle_Limit;
    	LOW = super.mPartItemData.Down_Limit;
		
    	int x = (int) (Math.random()*max_xyz);
    	int y = (int) (Math.random()*max_xyz);
    	int z = (int) (Math.random()*max_xyz);
    	float temp = (int) (Math.random()*max_temperation);
    	
    	mXTextView.setText(String.valueOf(x));
    	mYTextView.setText(String.valueOf(y));
    	mZTextView.setText(String.valueOf(z));
    	switch(mZhouCounts){
    	case 1:
    		y=z=0;
    		break;
    	case 2:
    		z=0;
    		break;
    	}
   
    	if((temp < MAX) && (temp>=MID) ){
    		mRadioButton.setBackgroundColor(Color.YELLOW);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.warning));
    		
    	}else if((temp >= LOW) && (temp<MID)){
    		mRadioButton.setBackgroundColor(Color.BLACK);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.normal));
    	}else if(temp <LOW){
    		mRadioButton.setBackgroundColor(Color.GRAY);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.invalid));
    	}else if(temp>=MAX){
    		mRadioButton.setBackgroundColor(Color.RED);
    		if(mColorTextView !=null)
    		mColorTextView.setText(getString(R.string.dangerous));
    	}
    	
    	Log.d(TAG,"in genRandomXYZ() x ="+ x+",y ="+y+",z="+z + ",temp = "+temp);
    	mCallback.OnClick((mZhouCounts>0)?mZhouCounts:3,x,y,z);
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

	@Override
	public void OnButtonDown(int buttonId, PartItemListAdapter bundle,String Value) {
		// TODO Auto-generated method stub
		genRandomXYZ();
	}


	@Override
	public void saveCheckValue() {
		// TODO Auto-generated method stub
		Log.d("atest", "震动   saveCheckValue()");
		super.setPartItemData("震动");
	}
}
