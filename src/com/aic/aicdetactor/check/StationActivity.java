package com.aic.aicdetactor.check;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.StationListAdapter;
import com.aic.aicdetactor.adapter.SuperTreeViewAdapter;
import com.aic.aicdetactor.adapter.TreeViewAdapter;
import com.aic.aicdetactor.adapter.TreeViewTest;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.data.PartItemItem;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.google.gson.Gson;

public class StationActivity extends CommonActivity implements OnClickListener{

	private static final int TAKE_PIC = 1;
	private static final int RECORD = 2;
	private RadioGroup mRadioGroup = null;
	private ViewPager mViewPager = null;
	private  List<View> mList_Views = null;
	private  int mStationIndex =0;
	private String mStationNameStr = null;
	private boolean isStationClicked = false;
	private boolean isTestInterface = false;
	//
	private int mRouteIndex =0;
	private ExpandableListView mListView;
	private boolean isUseWivewPager =false;
	String TAG = "luotest";
	private String  routeName = null;
	private StationListAdapter mListViewAdapter = null;
	private List<Map<String, String>> mListDatas = null;
	private myApplication    app = null;
private boolean mSpecial = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
//		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
			setContentView(R.layout.station_activity);
			
			setActionBar("日常巡检",true);
			app = (myApplication) getApplication();
			Intent intent =getIntent();
			mRouteIndex = intent.getExtras().getInt(CommonDef.route_info.LISTVIEW_ITEM_INDEX);
			mSpecial = intent.getExtras().getBoolean(CommonDef.route_info.IsSpecilaLine);
			String  oneCatalog = intent.getExtras().getString(CommonDef.ROUTE_CLASS_NAME);		
			routeName = intent.getExtras().getString(CommonDef.route_info.NAME);
			
			TextView planNameTextView  =(TextView)findViewById(R.id.route_type_name);
			planNameTextView.setText(routeName);	
			/*TextView title_bar_noback_name = (TextView) findViewById(R.id.title_bar_noback_name);
			title_bar_noback_name.setText(oneCatalog);*/
			/*ActionBar bar=getActionBar();
			bar.setLogo(null);;
			bar.setDisplayUseLogoEnabled(false);
			bar.setTitle(oneCatalog);*/
			
			ImageView recordBtn = (ImageView)findViewById(R.id.station_record);
			recordBtn.setOnClickListener(this);
			ImageView takepicBtn = (ImageView)findViewById(R.id.station_take_pic);
			takepicBtn.setOnClickListener(this);
			
			//打卡
			ImageView imageViewka = (ImageView)findViewById(R.id.ka);
			imageViewka.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					MLog.Logd(TAG,"imageView.setOnClickListener");
					// TODO Auto-generated method stub
					//全屏幕显示一个图片 及 去掉字样的button
					//finish();
				}
				
			});
			
			ImageView imageViewMenu = (ImageView)findViewById(R.id.menuimage);
			imageViewMenu.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					MLog.Logd(TAG,"imageView.setOnClickListener");
					// TODO Auto-generated method stub
					//弹出菜单
					//finish();
					initPopupWindowFliter(arg0);
				}
				
			});
		
			mListView = (ExpandableListView) findViewById(R.id.listView);
			mListDatas = new ArrayList<Map<String, String>>();
			mListView.setGroupIndicator(null);
			mListViewAdapter = new StationListAdapter(StationActivity.this,this.getApplicationContext(),mRouteIndex,mSpecial);
			mListView.setAdapter(mListViewAdapter);

			if (isTestInterface) {
				// //test idinfo ,test pass
				int myid = 100;
				String teststr = "AIC8E791D89B";
				teststr = "AIC8C78BD09B";
				try {
					myid = app
							.getStationItemIndexByID(0,teststr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MLog.Logd(TAG, " idtest myid is " + myid);
				
				// test getpartitemsub,TEST pass
				teststr = "0102030405*B302皮带机电机*电机震动*00000005*mm/s*1*40E33333*40900000*3D23D70A*";
				//"0102*F#测点*转速*00000007*r/min*129*461C4000*460CA000*459C4000*"

				for (int n = 0; n < 10; n++) {

					String str = app
							.getPartItemSubStr(teststr, n);
					MLog.Logd(TAG,"teststr is "	+ str);
				}
				float f1 = SystemUtil.getTemperature("42700000");
				MLog.Logd(TAG,"temperature is "	+ SystemUtil.getTemperature("42700000"));
				MLog.Logd(TAG,"temperature is "	+ SystemUtil.getTemperature("42b40000"));
				//MLog.Logd(TAG,"temperature is "	+ SystemUtil.getTemperature("42700000"));
				///
				
			}
		}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//initListViewData();
		super.onResume();
	}



	public List<Object>mStationList =null;
	public List<String>mStationNameList=null;
	public List<Object>mDeviceList =null;
	public List<String>mDeviceNameList=null;
	public List<Object>mPartItemList =null;
	public List<String>mPartItemNameList=null;
	
	public List<Object>getStationList(int routeIndex){
		try {
			mStationList=	app.getStationList(routeIndex);
			if(mStationNameList==null){
				mStationNameList = new ArrayList<String>();
			}else{
				mStationNameList.clear();
			}
			for(int i=0;i<mStationList.size();i++){
				mStationNameList.add(app.getStationItemName(mStationList.get(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mStationList;
	}
	
	public List<Object>getDeviceList(int stationIndex){
		try {
			//mStationList=	app.getStationList(routeIndex);
			if(mDeviceNameList==null){
				mDeviceNameList = new ArrayList<String>();
			}else{
				mDeviceNameList.clear();
			}
			
			mDeviceList =app.getDeviceList(mStationList.get(stationIndex));
			for(int i=0;i<mDeviceList.size();i++){
				mDeviceNameList.add(app.getDeviceItemName(mDeviceList.get(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mDeviceList;
	}
	
	public List<String>getPartItemList(int stationIndex,int deviceIndex){
		try {
			if(mPartItemNameList==null){
				mPartItemNameList = new ArrayList<String>();
			}else{
				mPartItemNameList.clear();
			}
			//getDeviceList(stationIndex);
			mDeviceList =app.getDeviceList(mStationList.get(stationIndex));
			Gson g= new Gson();
			//for(int i=0;i<mDeviceList.size();i++){
				mPartItemList = app.getPartItemDataList(stationIndex, deviceIndex);
				for(int k=0;k<mPartItemList.size();k++){
				
				long a = System.currentTimeMillis();
				PartItemItem item =g.fromJson(mPartItemList.get(k).toString(), PartItemItem.class);
				MLog.Logd(TAG,"release ms="+String.valueOf(System.currentTimeMillis()-a)+","+item.getCheckContent());
				mPartItemNameList.add(item.getCheckContent());
				}
			//}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mPartItemNameList;
	}
	ExpandableListView expandableList;  
    TreeViewAdapter adapter;  
    SuperTreeViewAdapter superAdapter;  
	void InitListViewData(){
		  
        List<SuperTreeViewAdapter.SuperTreeNode> superTreeNode = superAdapter.GetTreeNode();  
        for(int i=0;i<mStationNameList.size();i++)//第一层  
        {  
            SuperTreeViewAdapter.SuperTreeNode superNode=new SuperTreeViewAdapter.SuperTreeNode();  
            superNode.parent=mStationNameList.get(i);  
            getDeviceList(i);
            //第二层  
            for(int ii=0;ii<mDeviceNameList.size();ii++)  
            {  
                TreeViewAdapter.TreeNode node=new TreeViewAdapter.TreeNode();  
                node.parent=mDeviceNameList.get(ii);//第二级菜单的标题  
                getPartItemList(i,ii); 
                for(int iii=0;iii<mPartItemNameList.size();iii++)//第三级菜单  
                {  
                    node.childs.add(mPartItemNameList.get(iii));  
                }  
                superNode.childs.add(node);  
            }  
            superTreeNode.add(superNode);  
              
        }  
        superAdapter.UpdateTreeNode(superTreeNode);  
        expandableList.setAdapter(superAdapter);  
    
	}
	
	 OnChildClickListener stvClickEvent=new OnChildClickListener(){  
		  
	        @Override  
	        public boolean onChildClick(ExpandableListView parent,  
	                View v, int groupPosition, int childPosition,  
	                long id) {  
	            String str="parent id:"+String.valueOf(groupPosition)+",children id:"+String.valueOf(childPosition);  
	            Toast.makeText(StationActivity.this, str, 300).show(); 
	            return false;  
	        }  
	          
	    };  
	    void initPopupWindowFliter(View parent) {
			LayoutInflater inflater = (LayoutInflater) this
					.getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View rootview = inflater.inflate(
					R.layout.station_menu, null, false);

			final PopupWindow pw_Left = new PopupWindow(rootview, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, true);
			pw_Left.setBackgroundDrawable(null);
			
			Spinner mSpinner = (Spinner) rootview.findViewById(R.id.spinner1);
			String[] mItems = getResources().getStringArray(R.array.spinnername);
			ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		//	_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
			mSpinner.setAdapter(_Adapter);
			
			
			Spinner mSpinnerPoint = (Spinner) rootview.findViewById(R.id.spinner2);
			ArrayAdapter<String> _AdapterPoint=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
			mSpinnerPoint.setAdapter(_AdapterPoint);

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
			pw_Left.showAsDropDown(parent, Gravity.CENTER, 0);
			pw_Left.update();

		}

	    @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        // TODO Auto-generated method stub  
	        super.onActivityResult(requestCode, resultCode, data);  
	        if (resultCode == Activity.RESULT_OK && resultCode==TAKE_PIC) {  
	            String sdStatus = Environment.getExternalStorageState();  
	            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
	                Log.i("TestFile",  
	                        "SD card is not avaiable/writeable right now.");  
	                return;  
	            }  
	            String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
	            Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
	            Bundle bundle = data.getExtras();  
	            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
	          
	            FileOutputStream b = null;  
	           //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？  
	            File file = new File("/sdcard/myImage/");  
	            file.mkdirs();// 创建文件夹  
	            String fileName = "/sdcard/myImage/"+name;  
	  
	            try {  
	                b = new FileOutputStream(fileName);  
	                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
	            } catch (FileNotFoundException e) {  
	                e.printStackTrace();  
	            } finally {  
	                try {  
	                    b.flush();  
	                    b.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	            
	        }  
	        if(resultCode == Activity.RESULT_OK && resultCode==RECORD){
	        	Toast.makeText(this, "RECORD", Toast.LENGTH_LONG).show();
	        }
	    }  

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.station_record:
				Intent intent_record = new Intent(Media.RECORD_SOUND_ACTION);
				startActivity(intent_record);
				break;
			case R.id.station_take_pic:
                Intent intent_takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
                startActivityForResult(intent_takepic, TAKE_PIC);
				break;
			default:
				break;
			}
		}
}
