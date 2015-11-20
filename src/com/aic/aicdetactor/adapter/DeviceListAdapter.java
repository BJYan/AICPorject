package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.acharEngine.AverageTemperatureChart;
import com.aic.aicdetactor.acharEngine.ChartBuilder;
import com.aic.aicdetactor.acharEngine.IDemoChart;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.check.PartItemActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.PartItemJson;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.CommonDialogBtnListener;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;

public class DeviceListAdapter  extends BaseExpandableListAdapter implements CommonDialogBtnListener{
	private Context context;
	private LayoutInflater mInflater;
	ArrayList<ArrayList<PartItemJsonUp>> mChildrenList;
	private List<Map<String, String>> mDataList = null;
	private int mStationIndex=0;
	private int mDeviceIndex=0;
	private myApplication app = null;
	private CommonActivity mActivity = null;
	private final String TAG="luotest.DeviceListAdapter";
	private boolean mIsSpecial= false;
	public DeviceListAdapter(Context context, CommonActivity av,
			int stationIndex,int deviceIndex,boolean mIsSpecial){
			//ArrayList<ArrayList<Map<String, String>>> mChildrenList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		//this.mChildrenList = mChildrenList;
		app =(myApplication) av.getApplication();
		app.mStationIndex=mStationIndex = stationIndex;
		mDeviceIndex = deviceIndex;
		mActivity = av;
		mDataList = new ArrayList<Map<String, String>>();
		app = ((myApplication) av.getApplication());
		this.mIsSpecial = mIsSpecial;
		InitData();
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mChildrenList.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}
	
	@Override
	public View getChildView(final int arg0, final int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
				GroupViewHolder holder = null;

				if (arg3 == null) {
					arg3 = mInflater.inflate(R.layout.checkitem_thr_item, null);
					holder = new GroupViewHolder();
					holder.NameText = (TextView) arg3
							.findViewById(R.id.pathname);
					holder. image = (ImageView) arg3.findViewById(R.id.history);
					holder.image.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							//添加拉起趋势图的操作
							/*Intent intent = null;
							View v= (View) arg0.getParent();
							TextView tv= (TextView) v.findViewById(R.id.pathname);
							IDemoChart[] mCharts = new IDemoChart[] {
									 new AverageTemperatureChart()};
						      intent = mCharts[0].execute(context,tv.getText().toString());
						      mActivity.startActivity(intent);*/
							CommonDialog chartDialog = new CommonDialog(mActivity);
							chartDialog.setCloseBtnVisibility(View.VISIBLE);
							chartDialog.setTitle("测试图谱");
							chartDialog.setButtomBtn(DeviceListAdapter.this, "确定", "取消");
							DataAnalysis dataAnalysis = new DataAnalysis();
							float[] data = dataAnalysis.getData();

							ChartBuilder chartBuilder = new ChartBuilder(context);
							View chartView = chartBuilder.getBlackLineChartView("测试数据", data);
							View chartView1 = chartBuilder.getBlackLineChartView("测试数据", data);
							View chartView2 = chartBuilder.getBlackLineChartView("测试数据", data);

							View dialogContent = mInflater.inflate(R.layout.dialog_content_thr_charts1_layout, null);
							LinearLayout chart1Container = (LinearLayout) dialogContent.findViewById(R.id.dialog_thr_chart_first_chart);
							chart1Container.addView(chartView);
							LinearLayout chart2Container = (LinearLayout) dialogContent.findViewById(R.id.dialog_thr_chart_sec_chart);
							chart2Container.addView(chartView1);
							LinearLayout chart3Container = (LinearLayout) dialogContent.findViewById(R.id.dialog_thr_chart_thr_chart);
							chart3Container.addView(chartView2);
							chartDialog.setChartView(dialogContent, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, 680));
							chartDialog.show();
						}
						
					});
					arg3.setTag(holder);
				} else {
					holder = (GroupViewHolder) arg3.getTag();
					
				}
				arg3.setOnClickListener(new OnClickListener(){
					int deviceindex = mDeviceIndex;
					int partindex = arg1;
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
					//	mChildrenList.get(arg0).get(arg1)
						//拉起测量等数据的检测界面
						//TextView typeT= (TextView) arg0.findViewById(R.id.type);
						//获取测试项的类型
						//int itype = Integer.valueOf(typeT.getText().toString());
						//根据类型选择显示不同的UI，调用界面类似于PartItemActivity:switchFragment函数
						if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceindex).Is_In_Place!=1){
						
						Intent intent = new Intent();
						app.mDeviceIndex=deviceindex;
						app.mPartItemIndex = partindex;
						intent.setClass(mActivity, PartItemActivity.class);
						mActivity.startActivity(intent); 
						}else{
							addIs_InPlacePartItem(deviceindex);
							Toast.makeText(context, "到位管理完成", Toast.LENGTH_LONG).show();
						}
					}
					
				});
				holder.NameText.setText(mChildrenList.get(mDeviceIndex).get(arg1).Check_Content);
			return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		//MLog.Logd(TAG,"getChildrenCount "+mChildrenList.get(arg0).size());
		return mChildrenList.get(mDeviceIndex).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mDataList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		//return mDataList.size();
		return 1;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(final int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		
		
		GroupViewHolder holder =null;
		HashMap<String, String> map = (HashMap<String, String>) mDataList.get(mDeviceIndex);
		MLog.Logi(TAG, "getGroupView groupPosition = "+arg0);
		if (arg2 == null) {			
			arg2 = mInflater.inflate(R.layout.device_list_item, null);
			holder = new GroupViewHolder();
			holder.image = (ImageView) arg2.findViewById(R.id.arrow);
			holder.NameText = (TextView) arg2.findViewById(R.id.pathname);
			holder.DeadLineText = (TextView) arg2.findViewById(R.id.deadtime);
			holder.ProcessText = (TextView) arg2.findViewById(R.id.progress);
			arg2.setTag(holder);
		}else{
			holder=(GroupViewHolder) arg2.getTag();
		}
		if(arg1){
			holder.image.setBackgroundResource(R.drawable.arrow_ex);
		}else{
			holder.image.setBackgroundResource(R.drawable.arrow);
		}
		holder.NameText.setText(map.get(CommonDef.device_info.NAME).toString());
//		arg2.setOnClickListener(new OnClickListener(){
//			int deviceIndex = arg0;
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_In_Place==1){
//			if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem ==null
//					|| app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem.size()==0){
//				addIs_InPlacePartItem(deviceIndex);
//			}
//				}else{
//				//	super.
//				}
//			}
//			
//		});
		return arg2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
		return true;
	}
	
	void InitData() {
		long g=System.currentTimeMillis();
		MLog.Logd(TAG, "InitData>> ");
		try {
			mDataList.clear();
			if(mChildrenList==null){
			mChildrenList = new ArrayList<ArrayList<PartItemJsonUp>>();
			}else{
				mChildrenList.clear();
			}
			for (int i = 0; i < app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.size(); i++) {
				if(mIsSpecial){
						if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Is_Special_Inspection>0){
					Map<String, String> map = new HashMap<String, String>();
					map.put(CommonDef.device_info.NAME,app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
					MLog.Logd(TAG,"name is"+app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
					map.put(CommonDef.device_info.DEADLINE, "2016");
				
					map.put(CommonDef.device_info.PROGRESS, app.mLineJsonData.getItemCounts(2, i, true,true)+ "/" + app.mLineJsonData.getItemCounts(2, i, false,true));
					mDataList.add(map);
					InitChidrenData(mStationIndex, i);
						}
					}else{
						if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Is_Special_Inspection<=0){
					Map<String, String> map = new HashMap<String, String>();
					map.put(CommonDef.device_info.NAME,app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
					MLog.Logd(TAG,"name is"+app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
					map.put(CommonDef.device_info.DEADLINE, "2016");
				
					map.put(CommonDef.device_info.PROGRESS, app.mLineJsonData.getItemCounts(2, i, true,true)+ "/" + app.mLineJsonData.getItemCounts(2, i, false,true));
					mDataList.add(map);
					InitChidrenData(mStationIndex, i);
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		MLog.Logd(TAG, "InitData<< "+String.valueOf(System.currentTimeMillis()-g));
	}


	

	void InitChidrenData(int stationIndex, int itemIndexs) {
		long gg=System.currentTimeMillis();
		MLog.Logd(TAG, "InitChidrenData>> stationIndex="+stationIndex);
		try {
			ArrayList<PartItemJsonUp> childList = new ArrayList<PartItemJsonUp>();
			PartItemJsonUp item =null;
			for (int i = 0; i < app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(itemIndexs).PartItem.size(); i++) {
				
				item =  app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(itemIndexs).PartItem.get(i);//mGSon.fromJson(mPartItemList.get(i).toString(), PartItemItem.class);
				childList.add(item);
			}
			mChildrenList.add(childList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		MLog.Logd(TAG, "InitChidrenData<< stationIndex ="+stationIndex+","+String.valueOf(System.currentTimeMillis()-gg));
	}

	@Override
	public void onClickBtn1Listener(CommonDialog dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickBtn2Listener(CommonDialog dialog) {
		// TODO Auto-generated method stub
		
	}
	
	//如果是 在位管理的话 ，需要在partitem数组中新增添一项partitem
	void addIs_InPlacePartItem(int deviceIndex){
		PartItemJsonUp partitem = new PartItemJsonUp();
		
		partitem.Start_Check_Datetime=SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_Device_Checked=1;
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_RFID_Checked=1;
		if(app.isTest){
			app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_Omission_Check=89;
		}else{
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_Omission_Check=app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.Is_Omission_Check;
		
		}
		if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem !=null
				&&app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem.size()>0){
			List<PartItemJsonUp> PartItem = app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem;
			app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem.removeAll(PartItem);
			app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem.add(partitem);
		}else{
			List<PartItemJsonUp>list = new ArrayList<PartItemJsonUp>();
			list.add(partitem);
			app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem=list;
		}
		
	}

}
