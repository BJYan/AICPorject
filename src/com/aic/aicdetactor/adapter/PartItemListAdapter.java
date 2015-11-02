package com.aic.aicdetactor.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.Event.Event;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.DeviceItemJson;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.data.RoutePeroid;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 巡检当前RF对应的station 下的所有device项，完成后，进行存盘。
 * 缓存指的是某个device 还没巡检完毕，缓存当前的DeviceItem测试结果.
 * 所以在巡检过程中要有一个临时的device数据，来缓存当前的巡检过程结果，当此device下的所有partItem都巡检完毕了，把此device数据添加到即将要保存的数据中。
 * @author AIC
 *
 */
public class PartItemListAdapter extends BaseAdapter {
	private List<String> statusList = new ArrayList<String>();
	
	private String TAG="PartItemListAdapter";
	private myApplication app ;
	private Activity mActivity;
	
	//选择运行/停机/备用等后需生成的一个PartItem数据结构
	private PartItemJsonUp mPartItemAfterItemDef =null;
	private DeviceItemJson mDeviceItemCahce=null;
	private ArrayList<PartItemJsonUp> mPartItemList=null;
	private ArrayList<PartItemJsonUp> mOriPartItemList=null;//原始的数据
	
	private int mStationIndex=0;
	private int mDeviceIndex=0;
	private int mPartItemIndex = 0;
	
	private Handler mHandler=null;
	public PartItemListAdapter(Activity av,int mStationIndex,int mDeviceIndex){
		this.mDeviceIndex=mDeviceIndex;
		this.mStationIndex=mStationIndex;
		this.mActivity = av;
		app = ((myApplication)av. getApplication());
		
		mPartItemAfterItemDef = new PartItemJsonUp();
		
		
		mHandler =new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					Log.d("luotestA", msg.obj.toString());
					Toast.makeText(mActivity.getApplicationContext(), msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}};
		initListViewAndData(false);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPartItemList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mPartItemList.get(arg0).Check_Content;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		TextView tmView;
		if(arg1 == null){
		TextView tm = new TextView(mActivity.getApplicationContext());
		tmView = tm;
		}else {
		tmView = (TextView)arg1;
		}
		tmView.setText(mPartItemList.get(arg0).Check_Content);
		return tmView;
	}
	
	
	public int  setCurPartItemIndex(int partItemIndex){
		if(mDeviceItemCahce.PartItem.size()>=partItemIndex){
		mPartItemIndex=partItemIndex;
		}
		return mPartItemIndex;
	}
	
	public int  resetMaxPartItemIndex(){
		if(mDeviceItemCahce.PartItem.size()<=mPartItemIndex){
			if(mDeviceItemCahce.PartItem.size()==mPartItemIndex){
			mPartItemIndex--;
			}else{
				mPartItemIndex = mDeviceItemCahce.PartItem.size()-1;	
			}
		}
		return mPartItemIndex;
	}
	/**
	 * 判断当前的deviceItem是否已经巡检完成了，true 表示已巡检完毕，否则没巡检完。
	 * @return
	 */
	public boolean isCurrentDeviceItemFinish(){
		if(mPartItemIndex>=mPartItemList.size()-1){
			return true;
		}		
		return false;
	}
	
	/**
	 * 调用之前需要确认 isCurrentDeviceItemFinish()，如果返回是true 才可以调用，否则不需要调用。
	 * @return
	 */
	public boolean gotoNextDeviceItem(){
		if(mDeviceIndex<=app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.size()){
			if(mDeviceIndex==app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.size()-1){
			//mease is this is last deviceItem and all deviceItems that under current station have checked.
			return false;
			}else{
				mDeviceIndex++;
				resetInitListData();
				return true;
			}
		}
		
		return false;
}
	
	public void initListViewAndData(boolean bRefreshListView){
		
	//	mDeviceItemCahce = DeviceItemJson.clone(app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(mDeviceIndex));
		mDeviceItemCahce = app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(mDeviceIndex);
		
		try {
			if(mPartItemList == null ||mOriPartItemList==null ){
			 mPartItemList = new ArrayList<PartItemJsonUp>();
			 mOriPartItemList= new ArrayList<PartItemJsonUp>();
			 }else{
				 mOriPartItemList.clear();
				 mPartItemList.clear();
			 }
			

			PartItemJsonUp item =null;
			for (int i = 0; i < mDeviceItemCahce.PartItem.size(); i++) {
				
				item =  mDeviceItemCahce.PartItem.get(i);
				mPartItemList.add(item);
				mOriPartItemList.add(item);
			}
			app.gCurPartItemList = mPartItemList;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		if(bRefreshListView){this.notifyDataSetChanged();}
		
	}
	
	public void setPartItemStartTime()
	{
		mPartItemList.get(mPartItemIndex).setSartDate();
	}
	
	public void setPartItemEndTimeAndTotalTime(){
		mPartItemList.get(mPartItemIndex).setEndDate();
		mPartItemList.get(mPartItemIndex).calcCheckDuration();
	}
	public void resetInitListData(){
		initListViewAndData(true);
	}
	public void revertListViewData(){
		
		ArrayList<PartItemJsonUp> ItemList = new ArrayList<PartItemJsonUp>();
		for(int i= mPartItemList.size()-1;i>=0;i--){
			ItemList.add(mPartItemList.get(i));
		}
		mPartItemList.clear();
		mDeviceItemCahce.PartItem.clear();
		for(int m= 0;m<ItemList.size();m++){
			mPartItemList.add(ItemList.get(m));
			mDeviceItemCahce.PartItem.add(ItemList.get(m));
		}		
		this.notifyDataSetChanged();
		
	}
	
	public void getNewPartItemListDataByStatusArray(int index,String DeviceItemDef){
		mPartItemList.clear();
		for(int m= 0;m<mOriPartItemList.size();m++){
			if(((mOriPartItemList.get(m).Start_Stop_Flag>>index)&0x01)==1)	{
				mPartItemList.add(mOriPartItemList.get(m));
				}
			}
		mDeviceItemCahce.Item_Define=DeviceItemDef;
		mDeviceItemCahce.setStartDate();
		
		genPartItemDataAfterItemDef();
		
		this.notifyDataSetChanged();
	}
	
	/**
	 * 巡检完该deviceItem时 设置一些参数进来。
	 */
	public void setFinishDeviceCheckFlagAndSaveDataToSD(){
		mDeviceItemCahce.setRFChecked();
		mDeviceItemCahce.setDeviceChecked();
		mDeviceItemCahce.setEndDate();
		if(app.isTest){
			mDeviceItemCahce.setIsOmissionCheck(17687);
		}else{
			mDeviceItemCahce.setIsOmissionCheck(app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.Is_Omission_Check);
		}
		
		saveDeviceItemData();

	}
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	
	public int getCurrentPartItemType(){
		long type =Integer.valueOf(mPartItemList.get(mPartItemIndex).T_Measure_Type_Code);
		return (int)type;
		}
		
	public int getPrevPartItemType(){
		long type =-1;
		
		if(mPartItemIndex>=0 && mPartItemIndex<mPartItemList.size()){
		type=Integer.valueOf(mPartItemList.get(mPartItemIndex).T_Measure_Type_Code);
		}else{
			MLog.Loge(TAG, "getPrevPartItemType() error:out of arrayList");
		}
		return (int)type;
	}
	
	
	public int getNextPartItemType(){
		long type =-1;
		if(mPartItemIndex<mPartItemList.size()){
		type=Integer.valueOf(mPartItemList.get(mPartItemIndex).T_Measure_Type_Code);
		}else{
			MLog.Loge(TAG, "getNextPartItemType() error:out of arrayList");
		}
		return (int)type;
	}
	
	/**
	 * 保存当前检测数据及一些标记状态
	 * @param Value
	 */
	public void saveData(String Value){
		
		switch(Integer.valueOf(mPartItemList.get(mPartItemIndex).T_Measure_Type_Code)){
//			case CommonDef.checkUnit_Type.ACCELERATION:
//				break;
//			case CommonDef.checkUnit_Type.ACCELERATION:
//				break;
//			case CommonDef.checkUnit_Type.ACCELERATION:
//				break;
//			case CommonDef.checkUnit_Type.ACCELERATION:
//				break;
//			case CommonDef.checkUnit_Type.ACCELERATION:
//				break;
			case CommonDef.checkUnit_Type.OBSERVATION:
				
				break;
		}
		
		mPartItemList.get(mPartItemIndex).Extra_Information = Value;
	}
	
	/**
	 * 保存当前巡检线路时，需要填充数据全局信息
	 */
	private void setLineGlobalInfo(){
		
		//填充线路的 global information
		app.mLineJsonData.GlobalInfo.Check_Date=SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDD);		
		app.mLineJsonData.GlobalInfo.Guid=SystemUtil.createGUID();		
		app.mLineJsonData.GlobalInfo.Task_Mode=app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.Task_Mode;		
		
		if(app.mLineJsonData.GlobalInfo.Task_Mode==0){
			app.mLineJsonData.GlobalInfo.Start_Time=app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.Start_Time;
			app.mLineJsonData.GlobalInfo.End_Time=app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.End_Time;
			if(app.isTest){
				app.mLineJsonData.GlobalInfo.T_Period_Name="PeriodName_Test";
				app.mLineJsonData.GlobalInfo.Turn_Number=1002;
			}else{
			app.mLineJsonData.GlobalInfo.T_Period_Name=app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.T_Period_Name;
			app.mLineJsonData.GlobalInfo.Turn_Number=app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.Turn_Number;
			}
			
		}else if(app.mLineJsonData.GlobalInfo.Task_Mode==1){
			app.mLineJsonData.GlobalInfo.Start_Time="";
			app.mLineJsonData.GlobalInfo.End_Time="";
			app.mLineJsonData.GlobalInfo.T_Period_Name="";
			app.mLineJsonData.GlobalInfo.Turn_Number=1001;
			
		}
		
	}
	
	
	/**
	 * 选择运行/停机/备用后生成的PartItem对象
	 */
	private void genPartItemDataAfterItemDef(){
		mPartItemAfterItemDef.Check_Content=app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(mDeviceIndex).Name;
		mPartItemAfterItemDef.Extra_Information="设备";
		mPartItemAfterItemDef.T_Item_Abnormal_Grade_Id=2;
		mPartItemAfterItemDef.T_Item_Abnormal_Grade_Code="01";
		
	}
	
	private void saveDeviceItemData(){
		//先保存临时device数据，并把mPartItemAfterItemDef添加进去。
		//再将临时device数据添加到即将保存的json数据中
		setLineGlobalInfo();
		mDeviceItemCahce.PartItem.clear();
		for(PartItemJsonUp part:mPartItemList){
			mDeviceItemCahce.PartItem.add(part);
		}
		//genPartItemDataAfterItemDef();
		mDeviceItemCahce.PartItem.add(mPartItemAfterItemDef);
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.remove(mDeviceIndex);
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.add(mDeviceIndex, mDeviceItemCahce);
		
		String sonStr=JSON.toJSONString(app.mLineJsonData);
		Event ex = new Event();
		ex.UploadNormalPlanResultInfo_Event(null,null,null,mHandler,sonStr);
		
		
		
		
		try {
			//文件要保存到数据库中的
			SystemUtil.writeFile("/sdcard/AIC/data/"+app.mLineJsonData.GlobalInfo.Guid+".txt", sonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveDataToDB();
	}
	
	private void saveDataToDB(){
		RouteDao dao = RouteDao.getInstance(mActivity.getApplicationContext());
		if(app.isTest){
			RoutePeroid RoutePeroid = new RoutePeroid();
			RoutePeroid.Base_Point="1";
			RoutePeroid.Class_Group="T01";
			RoutePeroid.End_Time=SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
			
			dao.insertUploadFile(RoutePeroid);	
		}else{
		dao.insertUploadFile(app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid);
		}
	}
}
