package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.aic.aicdetactor.activity.PartItemListSelectActivity;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.LineType;
import com.aic.aicdetactor.data.PartItemJson;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.CommonDialogBtnListener;
import com.aic.aicdetactor.paramsdata.DeviceListInfoParams;
import com.aic.aicdetactor.paramsdata.LineListInfoParams;
import com.aic.aicdetactor.paramsdata.StationListInfoParams;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;

public class DeviceListAdapter  extends BaseExpandableListAdapter implements CommonDialogBtnListener{
	protected Context context;
	protected LayoutInflater mInflater;
	protected ArrayList<ArrayList<PartItemJsonUp>> mChildrenList;
	protected int mStationIndex=0;
	protected int mDeviceIndex=0;
	protected myApplication app = null;
	protected CommonActivity mActivity = null;
	protected  String TAG="luotest.DeviceListAdapter";
	private LineType mLineType;
	protected List<DeviceListInfoParams> mDeviceList=null;
	public DeviceListAdapter(Context context, CommonActivity av,
			int stationIndex,int deviceIndex,LineType lineType){
			//ArrayList<ArrayList<Map<String, String>>> mChildrenList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		//this.mChildrenList = mChildrenList;
		app =(myApplication) av.getApplication();
		app.mStationIndex=mStationIndex = stationIndex;
		mDeviceIndex = deviceIndex;
		mActivity = av;
		mDeviceList = new ArrayList<DeviceListInfoParams>();
		app = ((myApplication) av.getApplication());
		this.mLineType = lineType;
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
			if(mLineType==LineType.AllRoute){
				if (arg3 == null) {
					arg3 = mInflater.inflate(R.layout.searchpartitem, null);
					holder = new GroupViewHolder();
					holder.NameText = (TextView) arg3
							.findViewById(R.id.pathname);
					holder.DeadLineText = (TextView) arg3
							.findViewById(R.id.checkdate);
					holder.StausText = (TextView) arg3
							.findViewById(R.id.checkvalue);
					holder.ProcessText = (TextView) arg3
							.findViewById(R.id.unit);
					arg3.setTag(holder);
				} else {
					holder = (GroupViewHolder) arg3.getTag();
					
				}
				holder.NameText.setText(mChildrenList.get(mDeviceIndex).get(arg1).Check_Content);
				holder.DeadLineText.setText(mChildrenList.get(mDeviceIndex).get(arg1).End_Check_Datetime);
				if(mChildrenList.get(mDeviceIndex).get(arg1).T_Measure_Type_Code.equals("03")
						||mChildrenList.get(mDeviceIndex).get(arg1).T_Measure_Type_Code.equals("04")
						||mChildrenList.get(mDeviceIndex).get(arg1).T_Measure_Type_Code.equals("05")){
					
					double MAX = mChildrenList.get(mDeviceIndex).get(arg1).Up_Limit;
			    	double MID = mChildrenList.get(mDeviceIndex).get(arg1).Middle_Limit;
			    	double LOW = mChildrenList.get(mDeviceIndex).get(arg1).Down_Limit;
			    	double mCheckValue=0.0f;
			    	if(mChildrenList.get(mDeviceIndex).get(arg1).Extra_Information.length()>0){
			    	 mCheckValue = Double.parseDouble(mChildrenList.get(mDeviceIndex).get(arg1).Extra_Information);
			    	}
			    	try{
			    	if((mCheckValue < MAX) && (mCheckValue>=MID) ){
			    		holder.StausText.setTextColor(Color.YELLOW);
			    		
			    	}else if((mCheckValue >= LOW) && (mCheckValue<MID)){
			    		holder.StausText.setTextColor(Color.BLACK);
			    	}else if(mCheckValue <LOW){
			    		holder.StausText.setTextColor(Color.GRAY);
			    	}else if(mCheckValue>=MAX){
			    		holder.StausText.setTextColor(Color.RED);
			    	}}catch(Exception e){
			    		e.printStackTrace();
			    	}
			    	
				}else if(mChildrenList.get(mDeviceIndex).get(arg1).T_Measure_Type_Code.equals("07")){
					String value=mChildrenList.get(mDeviceIndex).get(arg1).Extra_Information;
					if(value.length()>0){
						mChildrenList.get(mDeviceIndex).get(arg1).Extra_Information = value.replace("/", "\r\n");
					}
				}else if(mChildrenList.get(mDeviceIndex).get(arg1).T_Measure_Type_Code.equals("10")){
					String value=mChildrenList.get(mDeviceIndex).get(arg1).Extra_Information;
					String Result="";
					if(value.length()>0){
						String []list=value.split("\\/");
						for(int i=0;i<list.length;i++){
							list[i]=list[i].substring(0, list[i].length()-2);
							Result = Result+"/"+list[i];
						}
						mChildrenList.get(mDeviceIndex).get(arg1).Extra_Information = Result.replace("/", "\r\n");
					}
					holder.ProcessText.setVisibility(View.GONE);
				}
				holder.StausText.setText(mChildrenList.get(mDeviceIndex).get(arg1).Extra_Information);
				holder.ProcessText.setText(mChildrenList.get(mDeviceIndex).get(arg1).Unit);
			}else{
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
//							View chartView = chartBuilder.getBlackLineChartView("测试数据", data);
//							View chartView1 = chartBuilder.getBlackLineChartView("测试数据", data);
//							View chartView2 = chartBuilder.getBlackLineChartView("测试数据", data);
//
//							View dialogContent = mInflater.inflate(R.layout.dialog_content_thr_charts1_layout, null);
//							LinearLayout chart1Container = (LinearLayout) dialogContent.findViewById(R.id.dialog_thr_chart_first_chart);
//							chart1Container.addView(chartView);
//							LinearLayout chart2Container = (LinearLayout) dialogContent.findViewById(R.id.dialog_thr_chart_sec_chart);
//							chart2Container.addView(chartView1);
//							LinearLayout chart3Container = (LinearLayout) dialogContent.findViewById(R.id.dialog_thr_chart_thr_chart);
//							chart3Container.addView(chartView2);
//							chartDialog.setChartView(dialogContent, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, 680));
//							chartDialog.show();
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
						if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceindex).Is_In_Place!=1){
					//	if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceindex).Is_Device_Checked!=1){
						Intent intent = new Intent();
						app.setCurrentDeviceIndex(deviceindex);
						app.mPartItemIndex = partindex;
						if(mLineType == LineType.SpecialRoute){
							app.setCategoryTitle(mActivity.getString(R.string.line_special_name));							
						}else if(mLineType == LineType.NormalRoute){
							app.setCategoryTitle(mActivity.getString(R.string.line_normal_name));
						}
						intent.setClass(mActivity, PartItemListSelectActivity.class);
						mActivity.startActivity(intent); 
//						}else{
//							Toast.makeText(context, app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceindex).Name + " 已经巡检过了，不能重复巡检", Toast.LENGTH_LONG).show();
//						}
						}else{
							addIs_InPlacePartItem(deviceindex);
							Toast.makeText(context, mActivity.getString(R.string.inpalce_manager), Toast.LENGTH_LONG).show();
						}
					}
					
				});
				holder.NameText.setText(mChildrenList.get(mDeviceIndex).get(arg1).Check_Content);
}
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
		return mDeviceList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mDeviceList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView( int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		
		
		GroupViewHolder holder =null;
		MLog.Logi(TAG, "getGroupView groupPosition = "+arg0+",name is "+mDeviceList.get(arg0).getName());
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
		holder.NameText.setText(mDeviceList.get(arg0).getName());
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
			mDeviceList.clear();
			if(mChildrenList==null){
			mChildrenList = new ArrayList<ArrayList<PartItemJsonUp>>();
			}else{
				mChildrenList.clear();
			}
			for (int i = 0; i < app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.size(); i++) {
				if(mLineType == LineType.SpecialRoute){
					if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Is_Special_Inspection==1){
						DeviceListInfoParams deviceInfo = new DeviceListInfoParams();							
						deviceInfo.setName(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
						MLog.Logd(TAG,"name is"+app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
						deviceInfo.setDeadLine( "2016");
					
						deviceInfo.setProcess(app.mLineJsonData.getItemCounts(2, i, true,true)+ "/" + app.mLineJsonData.getItemCounts(2, i, false,true));
						mDeviceList.add(deviceInfo);
						InitChidrenData(mStationIndex, i);
						}
					}else if(mLineType == LineType.NormalRoute){
						if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Is_Special_Inspection<=0){
							DeviceListInfoParams deviceInfo = new DeviceListInfoParams();							
							deviceInfo.setName(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
							MLog.Logd(TAG,"name is"+app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
							deviceInfo.setDeadLine( "2017");
						
							deviceInfo.setProcess(app.mLineJsonData.getItemCounts(2, i, true,true)+ "/" + app.mLineJsonData.getItemCounts(2, i, false,true));
							mDeviceList.add(deviceInfo);
							InitChidrenData(mStationIndex, i);
						}
				}else if(mLineType == LineType.AllRoute){
					DeviceListInfoParams deviceInfo = new DeviceListInfoParams();							
					deviceInfo.setName(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
					MLog.Logd(TAG,"name is"+app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
					deviceInfo.setDeadLine( "2018");
				
					deviceInfo.setProcess(app.mLineJsonData.getItemCounts(2, i, true,true)+ "/" + app.mLineJsonData.getItemCounts(2, i, false,true));
					mDeviceList.add(deviceInfo);
					InitChidrenData(mStationIndex, i);
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
	private void addIs_InPlacePartItem(int deviceIndex){
		PartItemJsonUp InPlacePartitem = new PartItemJsonUp();		
		InPlacePartitem.Start_Check_Datetime=SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);		
		InPlacePartitem.Check_Content=app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(mDeviceIndex).Name;
		InPlacePartitem.Extra_Information="到位";
		InPlacePartitem.T_Item_Abnormal_Grade_Id=2;
		InPlacePartitem.T_Item_Abnormal_Grade_Code="01";
		InPlacePartitem.Item_Define="";			
		
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_Device_Checked=1;
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_RFID_Checked=1;
		if(app.isTest){
			app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_Omission_Check=89;
		}else{
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).Is_Omission_Check=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Is_Omission_Check;
		
		}
		if(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem !=null
				&&app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem.size()>0){
			app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem.clear();
			app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem.add(InPlacePartitem);
		}else{
			List<PartItemJsonUp>list = new ArrayList<PartItemJsonUp>();
			list.add(InPlacePartitem);
			app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(deviceIndex).PartItem=list;
		}
		
	}

}
