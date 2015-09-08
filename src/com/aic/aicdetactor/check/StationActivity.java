package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.StationListAdapter;
import com.aic.aicdetactor.adapter.SuperTreeViewAdapter;
import com.aic.aicdetactor.adapter.TreeViewAdapter;
import com.aic.aicdetactor.adapter.TreeViewTest;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.data.PartItemItem;
import com.aic.aicdetactor.util.SystemUtil;
import com.google.gson.Gson;

public class StationActivity extends Activity {

	private RadioGroup mRadioGroup = null;
	private ViewPager mViewPager = null;
	private  List<View> mList_Views = null;
	private  int mStationIndex =0;
	private String mStationNameStr = null;
	private boolean isStationClicked = false;
	private boolean isTestInterface = true;
	//
	private int mRouteIndex =0;
	private ExpandableListView mListView;
	private boolean isUseWivewPager =false;
	String TAG = "luotest";
	private String  routeName = null;
	private StationListAdapter mListViewAdapter = null;
	private List<Map<String, String>> mListDatas = null;
	private myApplication    app = null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
//		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
			setContentView(R.layout.station_activity);
			
			
			app = (myApplication) getApplication();
			Intent intent =getIntent();
			mRouteIndex = intent.getExtras().getInt(CommonDef.route_info.LISTVIEW_ITEM_INDEX);
			
			String  oneCatalog = intent.getExtras().getString(CommonDef.ROUTE_CLASS_NAME);		
			routeName = intent.getExtras().getString(CommonDef.route_info.NAME);
			TextView planNameTextView  =(TextView)findViewById(R.id.planname);
			planNameTextView.setText(oneCatalog);			
			TextView RouteNameTextView  =(TextView)findViewById(R.id.station_text_name);
			RouteNameTextView.setText(""+(mRouteIndex +1)+ "		"+routeName);
			ImageView imageView = (ImageView)findViewById(R.id.imageView1);
			imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Log.d(TAG,"imageView.setOnClickListener");
					// TODO Auto-generated method stub
					finish();
				}
				
			});

		
			mListView = (ExpandableListView) findViewById(R.id.listView);
			mListDatas = new ArrayList<Map<String, String>>();
			
//			 adapter=new TreeViewAdapter(this,TreeViewAdapter.PaddingLeft>>1);  
//		        superAdapter=new SuperTreeViewAdapter(this,stvClickEvent);  
//		        expandableList=(ExpandableListView) StationActivity.this.findViewById(R.id.listView);  
//		        getStationList(mRouteIndex);
//		        InitListViewData();
			
			initListViewData();
//			mListViewAdapter = new SimpleAdapter(this, mListDatas,
//					R.layout.checkitem, new String[] { CommonDef.station_info.INDEX, CommonDef.station_info.NAME,
//					CommonDef.station_info.DEADLINE, CommonDef.station_info.STATUS, CommonDef.station_info.PROGRESS }, new int[] {
//							R.id.index, R.id.pathname, R.id.deadtime,
//							R.id.status, R.id.progress });
			mListViewAdapter = new StationListAdapter(StationActivity.this,this.getApplicationContext(),mRouteIndex);
			mListView.setAdapter(mListViewAdapter);
//			mListView.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					HashMap<String, String> mapItem = (HashMap<String, String>) (mListView
//							.getItemAtPosition(arg2));
//					Log.d(TAG,
//							"stationActivit StationName is "
//									+ (String) mapItem.get(CommonDef.station_info.NAME));
//					
//					
//					 
//					 app.gStationName = routeName ;
//					 app.mStationIndex = arg2;
//					 
//					 
//					 
//					Intent intent = new Intent();
//					intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX , mRouteIndex);
//					intent.putExtra(CommonDef.station_info.LISTVIEW_ITEM_INDEX, arg2);
//					intent.putExtra(CommonDef.ROUTE_CLASS_NAME, "计划巡检");
//					intent.putExtra(CommonDef.station_info.NAME,
//							(String) mapItem.get(CommonDef.station_info.NAME));
//					intent.putExtra(CommonDef.route_info.NAME, routeName);					
//					intent.setClass(getApplicationContext(),
//							DeviceActivity.class);
//					startActivity(intent);
//				}
//			});

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
				Log.d(TAG, " idtest myid is " + myid);
				
				// test getpartitemsub,TEST pass
				teststr = "0102030405*B302皮带机电机*电机震动*00000005*mm/s*1*40E33333*40900000*3D23D70A*";
				//"0102*F#测点*转速*00000007*r/min*129*461C4000*460CA000*459C4000*"

				for (int n = 0; n < 10; n++) {

					String str = app
							.getPartItemSubStr(teststr, n);
					Log.d(TAG,"teststr is "	+ str);
				}
				float f1 = SystemUtil.getTemperature("42700000");
				Log.d(TAG,"temperature is "	+ SystemUtil.getTemperature("42700000"));
				Log.d(TAG,"temperature is "	+ SystemUtil.getTemperature("42b40000"));
				//Log.d(TAG,"temperature is "	+ SystemUtil.getTemperature("42700000"));
				///
				
			}
		}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//initListViewData();
		super.onResume();
	}



	private void initListViewData(){
		Log.d(TAG,"initListViewData()");
		try {

			List<Object> stationItemList = app
					.getStationList(mRouteIndex);

			CheckStatus status = null;
			mListDatas.clear();
			for (int i = 0; i < stationItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				status = app
						.getNodeCount(stationItemList.get(i),1,0);
				status.setContext(getApplicationContext());
				map.put(CommonDef.station_info.NAME, app
						.getStationItemName(stationItemList.get(i)));
				map.put(CommonDef.station_info.DEADLINE, status.mLastTime);
				map.put(CommonDef.station_info.STATUS, status.getStatus());
				
				map.put(CommonDef.station_info.PROGRESS, status.mCheckedCount+"/"+status.mSum);
				//String index = "" + (i + 1);
				//map.put("index", index);

				mListDatas.add(map);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(mListViewAdapter != null){
			mListViewAdapter.notifyDataSetChanged();
		}
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
				Log.d(TAG,"release ms="+String.valueOf(System.currentTimeMillis()-a)+","+item.getCheckContent());
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
}
