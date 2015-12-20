package com.aic.aicdetactor.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aic.aicdetactor.Event.Event;
import com.aic.aicdetactor.Interface.OnButtonListener;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.condition.ConditionalJudgement;
import com.aic.aicdetactor.data.DeviceItemJson;
import com.aic.aicdetactor.data.ExtranalBinaryInfo;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.data.RoutePeroid;
import com.aic.aicdetactor.data.StationInfoJson;
import com.aic.aicdetactor.data.WorkerInfoJson;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.fragment.DownLoadFragment;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
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
	private List<HashMap<String,Object>> mExtralList;
	
	//选择运行/停机/备用等后需生成的一个PartItem数据结构
	private PartItemJsonUp mPartItemAfterItemDef =null;
	private DeviceItemJson mDeviceItemCahce=null;
	/**
	 * 选择运行、停机、备用筛选后的partItem数据
	 */
	private ArrayList<PartItemJsonUp> mPartItemList=null;
	/**
	 * 保留最后一次选择 运行 、停机 、备用后的partItem数据 ,用于显示只用
	 */
	private ArrayList<PartItemJsonUp> mPartItemAfterSelectedList=null;
	/**
	 * 额外的partItem,即 新增的 图片 、音频 、波形数据的list
	 */
	private ArrayList<PartItemJsonUp> mOriPartItemList=null;//原始的数据
	
	private int mStationIndex=0;
	private int mDeviceIndex=0;
	private int mPartItemIndex = 0;
	private Handler mHandler=null;
	private Handler mActivityHandler=null;
	boolean mStopSetDBStatus=false;
	LineDao mDao=null;
	boolean bUploadStatus=false;
	public PartItemListAdapter(Activity av,int mStationIndex,int mDeviceIndex,Handler ActivityHandler){
		this.mDeviceIndex=mDeviceIndex;
		this.mStationIndex=mStationIndex;
		this.mActivity = av;
		app = ((myApplication)av. getApplication());
		mDao= LineDao.getInstance(app.getApplicationContext());
		mActivityHandler = ActivityHandler;
		mExtralList = new ArrayList<HashMap<String,Object>>();
		mHandler =new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case Event.LocalData_Init_Success:
					Log.d("luotestA", msg.obj.toString());
					Message msg2= mActivityHandler.obtainMessage(Event.LocalData_Init_Success);
					msg2.arg1=msg.arg1;
					mActivityHandler.sendMessage(msg2);
					if(msg.arg1==1){
						//上传成功，需要更新数据表状态
						bUploadStatus=true;
					}else{
						bUploadStatus=false;
					}
//					Toast.makeText(mActivity.getApplicationContext(), msg.obj.toString(),
//							Toast.LENGTH_SHORT).show();
					break;
				case Event.NetWork_Connecte_Timeout:
				case Event.NetWork_MSG_Tips:
				case Event.Server_No_Data:
					{
						Toast.makeText(mActivity.getApplicationContext(), msg.obj.toString(),
								Toast.LENGTH_SHORT).show();
						
//						CommonAlterDialog dialog = new CommonAlterDialog(mActivity,"提示",(String)msg.obj,null,null);
//						dialog.show();
					}
					mStopSetDBStatus=true;
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
		return mPartItemAfterSelectedList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mPartItemAfterSelectedList.get(arg0).Check_Content;
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
		tmView.setText(mPartItemAfterSelectedList.get(arg0).Check_Content);
		tmView.setTextColor(Color.BLACK);
		return tmView;
	}
	
	
	public int  getNextPartItemIndex(){
		if(mPartItemIndex<mPartItemAfterSelectedList.size()){
		mPartItemIndex++;
		}
		return mPartItemIndex;
	}
	
	public int  getPrevPartItemIndex(){
		if(mPartItemIndex>0){
		mPartItemIndex--;
		mPartItemList.remove(mPartItemList.size()-1);
		}
		return mPartItemIndex;
	}
	
	/**
	 * 判断当前的deviceItem是否已经巡检完成了，true 表示已巡检完毕，否则没巡检完。
	 * @return
	 */
	public boolean isLastPartItemInCurrentDeviceItem(){
		Log.d(TAG, "isLastPartItemInCurrentDeviceItem()mPartItemIndex="+mPartItemIndex +",and mPartItemAfterSelectedList.size()="+mPartItemAfterSelectedList.size());
		if(mPartItemIndex>=mPartItemAfterSelectedList.size()){
			mPartItemIndex=mPartItemAfterSelectedList.size()-1;
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
				//app.mDeviceIndex=++mDeviceIndex;
			//	resetInitListData();
				return true;
			}
		}
		
		return false;
}
	public boolean isLastDevice(int deviceIndex){
		boolean value=false;
		
		if(deviceIndex==app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.size()-1){
			value=true;
		}
		return value;
		
	}
	/**
	 * 获取原始数据，用于显示已测量的数据测试情况
	 * 
	 * @return
	 */
	public PartItemJsonUp getCurOriPartItem(){
		return mOriPartItemList.get(mPartItemIndex);
	}
	public void initListViewAndData(boolean bRefreshListView){
		mDeviceItemCahce = new DeviceItemJson();
		mDeviceItemCahce.clone(app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(mDeviceIndex));
	//	mDeviceItemCahce = app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(mDeviceIndex);
		//班组
//		mDeviceItemCahce.T_Worker_Class_Group=app.mJugmentListParms.get(app.mRouteIndex).m_WorkerInfoJson.Class_Group;
//		//班次
//		mDeviceItemCahce.T_Worker_Class_Shift=String.valueOf(app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.Span);
//		mDeviceItemCahce.T_Worker_Guid=app.mJugmentListParms.get(app.mRouteIndex).m_WorkerInfoJson.Guid;
//		mDeviceItemCahce.T_Worker_Name=app.mJugmentListParms.get(app.mRouteIndex).m_WorkerInfoJson.Name;
//		mDeviceItemCahce.T_Worker_Number=app.mJugmentListParms.get(app.mRouteIndex).m_WorkerInfoJson.Number;
		try {
			if(mPartItemList == null ||mOriPartItemList==null || mPartItemAfterSelectedList==null ){
			 mPartItemList = new ArrayList<PartItemJsonUp>();
			 mOriPartItemList= new ArrayList<PartItemJsonUp>();
			 mPartItemAfterSelectedList =new ArrayList<PartItemJsonUp>();
			 }else{
				 mOriPartItemList.clear();
				 mPartItemList.clear();
				 mPartItemAfterSelectedList.clear();
			 }
			

			PartItemJsonUp item =null;
			for (int i = 0; i < mDeviceItemCahce.PartItem.size(); i++) {
				
				item =  mDeviceItemCahce.PartItem.get(i);
				mPartItemList.add(item);
				mOriPartItemList.add(item);
				mPartItemAfterSelectedList.add(item);
			}
			app.gCurPartItemList = mPartItemAfterSelectedList;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		if(bRefreshListView){this.notifyDataSetChanged();}
		
	}
	
	public String getCurDeviceExitDataGuid(){
		return mDeviceItemCahce.Data_Exist_Guid;
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
		for(int i= mPartItemAfterSelectedList.size()-1;i>=0;i--){
			ItemList.add(mPartItemAfterSelectedList.get(i));
		}
		mPartItemList.clear();
		mPartItemAfterSelectedList.clear();
		mDeviceItemCahce.PartItem.clear();
		for(int m= 0;m<ItemList.size();m++){
			mPartItemList.add(ItemList.get(m));
			mPartItemAfterSelectedList.add(ItemList.get(m));
			mDeviceItemCahce.PartItem.add(ItemList.get(m));
		}		
		this.notifyDataSetChanged();
		
	}
	public void getNewPartItemListDataByStatusArray(int index,String DeviceItemDef){
		mPartItemList.clear();
		mPartItemAfterSelectedList.clear();
		for(int m= 0;m<mOriPartItemList.size();m++){
			if(((mOriPartItemList.get(m).Start_Stop_Flag>>index)&0x01)==1)	{
				if(mOriPartItemList.get(m).T_Measure_Type_Id!=OnButtonListener.AudioDataId
						&&mOriPartItemList.get(m).T_Measure_Type_Id!=OnButtonListener.PictureDataId
						&&mOriPartItemList.get(m).T_Measure_Type_Id!=OnButtonListener.WaveDataId){
				mPartItemList.add(mOriPartItemList.get(m));
				mPartItemAfterSelectedList.add(mOriPartItemList.get(m));}
				}
			}
		mDeviceItemCahce.Item_Define=DeviceItemDef;
		mDeviceItemCahce.setStartDate();
		genPartItemDataAfterItemDef();
		mExtralList.clear();
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
			if(app.isSpecialLine()){
				mDeviceItemCahce.setIsOmissionCheck(0);
			}else{
				mDeviceItemCahce.setIsOmissionCheck(1887);
			}
		}else{
			if(app.isSpecialLine()){
				mDeviceItemCahce.setIsOmissionCheck(0);
			}else{			
			mDeviceItemCahce.setIsOmissionCheck(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Is_Omission_Check);
			}
		}
		mPartItemIndex=0;
		saveDeviceItemData();

	}
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	
	public int getCurrentPartItemType(){
		long type=0;
		if(mPartItemIndex<mPartItemList.size()){
		type =Integer.valueOf(mPartItemList.get(mPartItemIndex).T_Measure_Type_Code);
		}
		return (int)type;
	}
	public PartItemJsonUp getCurrentPartItem(){
		return mPartItemList.get(mPartItemIndex);
	}
	
	/**
	 * 保存当前检测数据及一些标记状态
	 * @param ExValue 测量数值 或选择数据
	 * @param checkIsNormal  是否正常 1 正常，0 异常
	 * @param AbnormaCode  异常情况下对应的异常code
	 * @param AbormalId  异常情况下对应的异常id
	 */
	public void saveData(String ExValue,String AbnormaCode,int AbormalId,int CaiYangShu,int CaiyangPinLv){
		if(mPartItemIndex<mPartItemList.size()){
		mPartItemList.get(mPartItemIndex).setExtralInfor(ExValue);
		mPartItemList.get(mPartItemIndex).Is_Normal=ConditionalJudgement.GetRusultStatus(AbnormaCode);
		mPartItemList.get(mPartItemIndex).T_Item_Abnormal_Grade_Code = AbnormaCode;
		mPartItemList.get(mPartItemIndex).T_Item_Abnormal_Grade_Id=AbormalId;
		mPartItemList.get(mPartItemIndex).SampleFre =CaiyangPinLv;
		mPartItemList.get(mPartItemIndex).SamplePoint =CaiYangShu;
		mPartItemList.get(mPartItemIndex).setVMSDir();
		mPartItemList.get(mPartItemIndex).setSignalType();	
		mPartItemList.get(mPartItemIndex).Item_Define=mDeviceItemCahce.Item_Define;
		mPartItemList.get(mPartItemIndex).Check_Mode="";
		
		}
		setPartItemEndTimeAndTotalTime();
	}
	
	/**
	 * 新增加媒体数据 例如 图片，音频 数据partItemData
	 * @param typecode
	 * @param SaveLab  uuid,如果是三轴的话，表示是同一组的数据，三轴振动还需要SaveLab找到其它两轴数据，
	 * @param RecordLab uuid, 数据对应关系中UUID
	 * @param validValue
	 * @param isNormal
	 * @param abNormalCode  如果 typecode ==11 即波形数据时采用，否则填写为null即可
	 */
	public void addNewMediaPartItem(ParamsPartItemFragment params){
		//先clone一份当前partitem数据
		PartItemJsonUp PartItemItem = new PartItemJsonUp();
		PartItemItem.Clone(mPartItemList.get(mPartItemIndex)); 
		PartItemItem.SaveLab= params.SaveLab;
		PartItemItem.RecordLab=params.RecordLab;
		
		//set current recordLab and SaveLab
		mPartItemList.get(mPartItemIndex).RecordLab=params.RecordLab;
		mPartItemList.get(mPartItemIndex).SaveLab=params.SaveLab;
		
		if(params.TypeCode==OnButtonListener.AudioDataType||params.TypeCode==OnButtonListener.PictureDataType){
			PartItemItem.T_Measure_Type_Code=""+params.TypeCode;
			PartItemItem.T_Measure_Type_Id=params.TypeCode+1;
			PartItemItem.Extra_Information="";
			PartItemItem.Unit="";
			if(params.IsNormal){
				PartItemItem.T_Item_Abnormal_Grade_Code="01";
				PartItemItem.T_Item_Abnormal_Grade_Id=2;
			}else{
				PartItemItem.T_Item_Abnormal_Grade_Code="05";
				PartItemItem.T_Item_Abnormal_Grade_Id=6;
			}
			PartItemItem.Is_Normal=params.IsNormal==true?1:0;
		}else if(params.TypeCode==OnButtonListener.WaveDataType){
			PartItemItem.Extra_Information=""+params.ValidValue;	
			PartItemItem.T_Item_Abnormal_Grade_Code=params.AbnormalCode;
			PartItemItem.T_Item_Abnormal_Grade_Id=Integer.valueOf(params.AbnormalCode)+1;
			PartItemItem.T_Measure_Type_Code=""+params.TypeCode;
			PartItemItem.T_Measure_Type_Id=params.TypeCode+1;
			PartItemItem.Is_Normal=ConditionalJudgement.GetRusultStatus(params.AbnormalCode);
		}
		
		PartItemItem.End_Check_Datetime =SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
		PartItemItem.Total_Check_Time= Integer.valueOf(SystemUtil.getDiffDate(PartItemItem.Start_Check_Datetime, PartItemItem.End_Check_Datetime));
	
		//不能显示在巡检UI上
		mPartItemList.add(PartItemItem);
		if(params.object!=null){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("RecordLab", params.RecordLab);
			map.put("Object", params.object);
			mExtralList.add(map);
		}
			
	}
	/**
	 * 保存当前巡检线路时，需要填充数据全局信息
	 */
	private void setLineGlobalInfo(){
		
		//填充线路的 global information
		if(app.mLineJsonData.GlobalInfo.Check_Date!=null){
			return;
		}
		app.mLineJsonData.GlobalInfo.Check_Date=SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDD);		
		app.mLineJsonData.GlobalInfo.Guid=SystemUtil.createGUID();		
		app.mLineJsonData.GlobalInfo.Task_Mode=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Task_Mode;		
		//app.mLineJsonData.GlobalInfo.T_Turn_Guid = app.mJugmentListParms.get(app.mRouteIndex).m_RoutePeroid.T_Turn_Guid;
		app.mLineJsonData.GlobalInfo.T_Turn_Guid = SystemUtil.createGUID();
		
		app.mLineJsonData.GlobalInfo.T_Worker_Guid = app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Guid;
		app.mLineJsonData.GlobalInfo.Check_Datetime = SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
		app.mLineJsonData.GlobalInfo.Turn_Finish_Mode = app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Turn_Finish_Mode;
		
		
		if(app.mLineJsonData.GlobalInfo.Task_Mode==0){
			app.mLineJsonData.GlobalInfo.Start_Time=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Start_Time;
			app.mLineJsonData.GlobalInfo.End_Time=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.End_Time;
			if(app.isTest){
				app.mLineJsonData.GlobalInfo.T_Period_Name="PeriodName_Test";
				app.mLineJsonData.GlobalInfo.Turn_Number=1002;
			}else{
			app.mLineJsonData.GlobalInfo.T_Period_Name=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.T_Period_Name;
			app.mLineJsonData.GlobalInfo.Turn_Number=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Turn_Number;
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
		if(mPartItemAfterItemDef!=null){
			mPartItemAfterItemDef=null;
		}
		mPartItemAfterItemDef = new PartItemJsonUp();
		mPartItemAfterItemDef.Check_Content=app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(mDeviceIndex).Name;
		mPartItemAfterItemDef.Extra_Information="设备级";
		mPartItemAfterItemDef.T_Item_Abnormal_Grade_Id=2;
		mPartItemAfterItemDef.T_Item_Abnormal_Grade_Code="01";
		mPartItemAfterItemDef.Item_Define=mDeviceItemCahce.Item_Define;
	}
	
	private void saveDeviceItemData(){
		//先保存临时device数据，并把mPartItemAfterItemDef添加进去。
		//再将临时device数据添加到即将保存的json数据中
		setLineGlobalInfo();
		mDeviceItemCahce.PartItem.clear();
		for(PartItemJsonUp part:mPartItemList){
			part.Item_Define=mDeviceItemCahce.Item_Define;
			mDeviceItemCahce.PartItem.add(part);
		}
		mDeviceItemCahce.PartItem.add(mPartItemAfterItemDef);
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.remove(mDeviceIndex);
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.add(mDeviceIndex, mDeviceItemCahce);
		
		
		setOtherDataIfNeeded();
		
		//序列化并本地保存
		String sonStr=JSON.toJSONString(app.mLineJsonData);		
		try {
			String fileGuid="";
			//文件要保存到数据库中的
			if(app.gIsDataChecked){
				fileGuid=getSaveDataFileName();
				if("".equals(fileGuid)){
					fileGuid=SystemUtil.createGUID();
				}
			}else{
				fileGuid=SystemUtil.createGUID();
			}
			app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.File_Guid=fileGuid;
			SystemUtil.writeFile(Setting.getUpLoadJsonPath()+fileGuid, sonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Is_Special_Inspection=0;
		saveDataToDB();
		
		//准备上传数据,应该从数据表中读取未上传的数据
		Event ex = new Event();
		if(sonStr!=null){
		ex.UploadNormalPlanResultInfo_Event(null,mHandler,sonStr);
		}
		UploadAllUploadJsonFile();
	}
	
	String getSaveDataFileName(){
				
		return mDao.getDataSaveFileName(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Name,
				app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Number,
				app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Class_Group,
				app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Turn_Name,
				String.valueOf(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Turn_Number),
				app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.T_Line_Guid);
	}
	
	/**
	 * 上传所有已经巡检过的未上传的日常巡检数据
	 */
	void UploadAllUploadJsonFile(){
		Event ex = new Event();
		List<String> fileList =mDao.getUnUploadAllUploadJsonFile();
		for(int k=0;k<fileList.size();k++){
			String JsonData=SystemUtil.openFile(fileList.get(k));
			if(JsonData!=null){
			ex.UploadNormalPlanResultInfo_Event(null,mHandler,JsonData);
			}
		}
		
		List<ExtranalBinaryInfo> list = mDao.getAllUnUploadExtralData();
		for(int m=0;m<list.size();m++){			
			String data=SystemUtil.openFile(list.get(m).filePath);
			byte []bytedata=null;
			if(data!=null){
				bytedata = data.getBytes();
			Event.UploadWaveDataRequestInfo_Event(null,mHandler,bytedata,list.get(m).RecordLab);
			}
		}
		
		
		
	}
	
	
	
	void setOtherDataIfNeeded(){
		if(!app.gIsDataChecked){
			//create device exit_data_guid and t_worker informations
			for(StationInfoJson station:app.mLineJsonData.StationInfo){
				for(DeviceItemJson device:station.DeviceItem){
					device.Data_Exist_Guid = SystemUtil.createGUID();
					device.T_Worker_Class_Group=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Class_Group;
					device.T_Worker_Class_Shift=String.valueOf(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Span);
					device.T_Worker_Guid=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Guid;
					device.T_Worker_Name=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Name;
					device.T_Worker_Number=app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Number;
				}
			}
			
		}
	}
	/**
	 * 修改upload数据表里对应uuid的状态，是否update.
	 * 并插入额外数据到数据表中  
	 */
	private void saveDataToDB(){
		
		if(app.isTest){
			RoutePeroid RoutePeroid = new RoutePeroid();
			RoutePeroid.Base_Point="1";
			RoutePeroid.Class_Group="T01";
			RoutePeroid.End_Time=SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
			
			mDao.insertUploadFile(RoutePeroid,app.gIsDataChecked,true,true);	
		}else{
			mDao.insertUploadFile(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid,app.gIsDataChecked,true,true);
		}
		app.gIsDataChecked=true;
		
		
		/**
		 * 插入额外信息到数据表中 
		 */
		for(int m =0;m<mExtralList.size();m++){
			//mExtralList.get(m)
			try {
				SystemUtil.writeFileToSD(Setting.getExtralDataPath()+mExtralList.get(m).get("RecordLab"), 
						Setting.getExtralDataPath()+mExtralList.get(m).get("Object"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int k=this.mPartItemAfterSelectedList.size();k<this.mPartItemList.size();k++){
			if(mPartItemList.get(k).T_Measure_Type_Id==OnButtonListener.PictureDataId
					||mPartItemList.get(k).T_Measure_Type_Id==OnButtonListener.AudioDataId
					||mPartItemList.get(k).T_Measure_Type_Id==OnButtonListener.WaveDataId){
				String SqlStr = "insert into " +DBHelper.TABLE_Media +"( "
						+DBHelper.Media_Table.Line_Guid +","
						+DBHelper.Media_Table.Name + ","
						+DBHelper.Media_Table.Date + ","
						+DBHelper.Media_Table.Mime_Type + ","
						+DBHelper.Media_Table.Is_Uploaded + ","
						+DBHelper.Media_Table.Path
					+") values ('"
					+app.mLineJsonData.T_Line.T_Line_Guid+"','"
					+mPartItemList.get(k).RecordLab +"','"
					+SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDD)+"','"
					+mPartItemList.get(k).T_Measure_Type_Id +"',"
					+"'0'" +",'"
					+Setting.getExtralDataPath()+mPartItemList.get(k).RecordLab
					+"')";
				mDao.execSQLUpdate(SqlStr);
				
			}
		
			
		} 
	}	
}
