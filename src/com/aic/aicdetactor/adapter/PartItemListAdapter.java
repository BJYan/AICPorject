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
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.data.RoutePeroid;
import com.aic.aicdetactor.data.StationInfoJson;
import com.aic.aicdetactor.data.WorkerInfoJson;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.fragment.DownLoadFragment;
import com.aic.aicdetactor.paramsdata.ExtranalBinaryInfo;
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
	
	private String TAG="PartItemListAdapter";
	private myApplication app ;
	private Activity mActivity;
	
	/**
	 * 拍照、录音、振动数据的List,包括设备级测点，设备级测点数据放到第一个里
	 */
	private List<PartItemJsonUp>mAddNewExtranalPartItemList=null;
	/**
	 * 用户保存额外信息文件与json关联的信息列表,包括saveLab及对应的二进制的图片、音频、振动数据
	 */
	private List<HashMap<String,Object>> mExtralList;
	
	/**
	 * 巡检过后 ，新生成的deviceItem结果，包括选择停机、备用筛选的数据，设备级测点，额外的音频、拍照、振动测点数据
	 */
	private DeviceItemJson mNewDeviceItem=null;
	
	private List<String>mOriginalExtranalInfoList=null;
	
	private DeviceItemJson mOriginalDeviceItem=null;
	/**
	 * 保留最后一次选择 运行 、停机 、备用后的partItem数据 ,
	 */
	private ArrayList<PartItemJsonUp> mPartItemAfterSelectedList=null;
	
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
//						Toast.makeText(mActivity.getApplicationContext(), msg.obj.toString(),
//								Toast.LENGTH_SHORT).show();
						
						CommonAlterDialog dialog = new CommonAlterDialog(mActivity,"提示",(String)msg.obj,null,null);
						dialog.show();
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
			return false;
			}
			return true;			
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
	public String getCurOriPartItemExtrnalInfo(){
		return mOriginalExtranalInfoList.get(mPartItemIndex);
	}
	/**
	 * 获取原始数据，用于显示已测量的数据测试情况
	 * 
	 * @return
	 */
	private PartItemJsonUp getCurOriPartItem(){
		mOriginalDeviceItem.PartItem.get(mPartItemIndex).printdata();
		return mOriginalDeviceItem.PartItem.get(mPartItemIndex);
	}
	public void initListViewAndData(boolean bRefreshListView){
		DeviceItemJson originalDevice =app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(mDeviceIndex);
		mOriginalExtranalInfoList= new ArrayList<String>();
		for(int i =0;i<originalDevice.PartItem.size();i++){
			mOriginalExtranalInfoList.add(originalDevice.PartItem.get(i).Extra_Information);
		}
		mOriginalDeviceItem = new DeviceItemJson();
		mOriginalDeviceItem.clone(app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(mDeviceIndex));
		//mOriginalDeviceItem=(DeviceItemJson) app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(mDeviceIndex).clone();
		mAddNewExtranalPartItemList = new ArrayList<PartItemJsonUp>();
		
		mNewDeviceItem = new DeviceItemJson();
		mNewDeviceItem.clone(app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(mDeviceIndex));
		try {
			if( mPartItemAfterSelectedList==null ){
			 mPartItemAfterSelectedList =new ArrayList<PartItemJsonUp>();
			 }else{
				 mPartItemAfterSelectedList.clear();
			 }
			

			PartItemJsonUp item =null;
			for (int i = 0; i < mNewDeviceItem.PartItem.size(); i++) {
				
				item =  mNewDeviceItem.PartItem.get(i);
				mPartItemAfterSelectedList.add(item);
			}
			app.gCurPartItemList = mPartItemAfterSelectedList;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		if(bRefreshListView){this.notifyDataSetChanged();}
		
	}
	
	public String getCurDeviceExitDataGuid(){
		return mNewDeviceItem.Data_Exist_Guid;
	}
	public void setPartItemStartTime()
	{
		mPartItemAfterSelectedList.get(mPartItemIndex).setSartDate();
	}
	
	public void setPartItemEndTimeAndTotalTime(){
		mPartItemAfterSelectedList.get(mPartItemIndex).setEndDate();
		mPartItemAfterSelectedList.get(mPartItemIndex).calcCheckDuration();
	}
	
	//每调用一次，进行一次反向排序显示
	public void revertListViewData(){
		ArrayList<PartItemJsonUp> ItemList = new ArrayList<PartItemJsonUp>();
		for(int i= mPartItemAfterSelectedList.size()-1;i>=0;i--){
			ItemList.add(mPartItemAfterSelectedList.get(i));
		}
		mPartItemAfterSelectedList.clear();
		for(int m= 0;m<ItemList.size();m++){
			mPartItemAfterSelectedList.add(ItemList.get(m));
		}		
		this.notifyDataSetChanged();
		
	}
	public void getNewPartItemListDataByStatusArray(int index,String DeviceItemDef){
		mPartItemAfterSelectedList.clear();
		for(int m= 0;m<mOriginalDeviceItem.PartItem.size();m++){
			if(((mOriginalDeviceItem.PartItem.get(m).Start_Stop_Flag>>index)&0x01)==1)	{
				if(mOriginalDeviceItem.PartItem.get(m).T_Measure_Type_Id!=OnButtonListener.AudioDataId
						&&mOriginalDeviceItem.PartItem.get(m).T_Measure_Type_Id!=OnButtonListener.PictureDataId
						&&mOriginalDeviceItem.PartItem.get(m).T_Measure_Type_Id!=OnButtonListener.WaveDataId){
				mPartItemAfterSelectedList.add(mOriginalDeviceItem.PartItem.get(m));}
				}
			}
		mNewDeviceItem.Item_Define=DeviceItemDef;
		mNewDeviceItem.setStartDate();
		genPartItemDataAfterItemDef();
		mExtralList.clear();
		this.notifyDataSetChanged();
	}
	
	/**
	 * 巡检完该deviceItem时 设置一些参数进来。
	 */
	public void setFinishDeviceCheckFlagAndSaveDataToSD(){
		mNewDeviceItem.setRFChecked();
		mNewDeviceItem.setDeviceChecked();
		mNewDeviceItem.setEndDate();
		mNewDeviceItem.calcCheckDuration();
		
		if(app.isSpecialLine()){
			mNewDeviceItem.setIsOmissionCheck(0);
		}else{			
		mNewDeviceItem.setIsOmissionCheck(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Is_Omission_Check);
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
		if(mPartItemIndex<mPartItemAfterSelectedList.size()){
		type =Integer.valueOf(mPartItemAfterSelectedList.get(mPartItemIndex).T_Measure_Type_Code);
		}
		return (int)type;
	}
	public PartItemJsonUp getCurrentPartItem(){
		return mPartItemAfterSelectedList.get(mPartItemIndex);
	}
	
	/**
	 * 保存当前检测数据及一些标记状态
	 * @param ExValue 测量数值 或选择数据
	 * @param checkIsNormal  是否正常 1 正常，0 异常
	 * @param AbnormaCode  异常情况下对应的异常code
	 * @param AbormalId  异常情况下对应的异常id
	 */
	public void saveData(String ExValue,String AbnormaCode,int AbormalId,int CaiYangShu,int CaiyangPinLv){
		if(mPartItemIndex<mPartItemAfterSelectedList.size()){
			String str1=mOriginalDeviceItem.PartItem.get(mPartItemIndex).Extra_Information;
			mPartItemAfterSelectedList.get(mPartItemIndex).setExtralInfor(ExValue);
			String str2=mOriginalDeviceItem.PartItem.get(mPartItemIndex).Extra_Information;
			mPartItemAfterSelectedList.get(mPartItemIndex).Is_Normal=ConditionalJudgement.GetRusultStatus(AbnormaCode);
			mPartItemAfterSelectedList.get(mPartItemIndex).T_Item_Abnormal_Grade_Code = AbnormaCode;
			mPartItemAfterSelectedList.get(mPartItemIndex).T_Item_Abnormal_Grade_Id=AbormalId;
			mPartItemAfterSelectedList.get(mPartItemIndex).SampleFre =CaiyangPinLv;
			mPartItemAfterSelectedList.get(mPartItemIndex).SamplePoint =CaiYangShu;
			mPartItemAfterSelectedList.get(mPartItemIndex).setVMSDir();
			mPartItemAfterSelectedList.get(mPartItemIndex).setSignalType();	
			mPartItemAfterSelectedList.get(mPartItemIndex).Item_Define=mNewDeviceItem.Item_Define;
			mPartItemAfterSelectedList.get(mPartItemIndex).Check_Mode="";
		
		}
		setPartItemEndTimeAndTotalTime();
	}
	
	/**
	 * 基于当前的测点，增加媒体数据 例如 图片，音频 数据partItemData到 mExtralList中,如果params.object==null，不添加
	 * @param typecode
	 * @param SaveLab  uuid,如果是三轴的话，表示是同一组的数据，三轴振动还需要SaveLab找到其它两轴数据，
	 * @param RecordLab uuid, 数据对应关系中UUID
	 * @param validValue
	 * @param isNormal
	 * @param abNormalCode  如果 typecode ==11 即波形数据时采用，否则填写为null即可
	 */
	public void addNewMediaPartItem(ParamsPartItemFragment params){
		if(params.object==null){
			return;
		}
		//先clone一份当前partitem数据
		PartItemJsonUp PartItemItem = new PartItemJsonUp();
		PartItemItem.Clone(mPartItemAfterSelectedList.get(mPartItemIndex)); 
		PartItemItem.SaveLab= params.SaveLab;
		PartItemItem.RecordLab=params.RecordLab;
		
		//set current recordLab and SaveLab
		mPartItemAfterSelectedList.get(mPartItemIndex).RecordLab=params.RecordLab;
		mPartItemAfterSelectedList.get(mPartItemIndex).SaveLab=params.SaveLab;
		
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
	
		mAddNewExtranalPartItemList.add(PartItemItem);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("RecordLab", params.RecordLab);
		map.put("Object", params.object);
		mExtralList.add(map);
			
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
		PartItemJsonUp mDevicePatItem = new PartItemJsonUp();
		mDevicePatItem.Check_Content=app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(mDeviceIndex).Name;
		mDevicePatItem.Extra_Information="设备级";
		mDevicePatItem.T_Item_Abnormal_Grade_Id=2;
		mDevicePatItem.T_Item_Abnormal_Grade_Code="01";
		mDevicePatItem.Item_Define=mNewDeviceItem.Item_Define;
		mDevicePatItem.End_Check_Datetime=SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
		mAddNewExtranalPartItemList.clear();
		mAddNewExtranalPartItemList.add(mDevicePatItem);
	}
	
	private void saveDeviceItemData(){
		setLineGlobalInfo();
		
		//剔除掉该设备下的所有partitem测点数据
		mNewDeviceItem.PartItem.clear();
		//设置每个测点的iitemdef
		for(PartItemJsonUp part:mPartItemAfterSelectedList){
			part.Item_Define=mNewDeviceItem.Item_Define;
			mNewDeviceItem.PartItem.add(part);
		}
		
		//添加所有的额外测点数据到 测点序列中
		for(PartItemJsonUp part:mAddNewExtranalPartItemList){
			part.Item_Define=mNewDeviceItem.Item_Define;
			mNewDeviceItem.PartItem.add(part);
		}
		
		//删掉cache文件中的原来的设备数据
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.remove(mDeviceIndex);
		//将原来设备位置上的数据进行替换更新
		app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.add(mDeviceIndex, mNewDeviceItem);
		
		
		setOtherDataIfNeeded();
		
		//序列化并本地保存
		String sonStr=JSON.toJSONString(app.mLineJsonData);		
		try {
			String fileGuid=app.getNewFileName();
			//文件要保存到数据库中的
//			if(app.gIsDataChecked){
//				fileGuid=getSaveDataFileName();
//				if("".equals(fileGuid)){
//					fileGuid=SystemUtil.createGUID();
//				}
//			}else{
//				fileGuid=SystemUtil.createGUID();
//			}
			app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.File_Guid=fileGuid;
			SystemUtil.writeFile(Setting.getUpLoadJsonPath()+fileGuid, sonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Is_Special_Inspection=app.isSpecialLine()==true?1:0;
		saveDataToDB();
	}
	
	String getSaveDataFileName(){
				
		return mDao.getDataSaveFileName(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Name,
				app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Number,
				app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_WorkerInfoJson.Class_Group,
				app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Turn_Name,
				String.valueOf(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.Turn_Number),
				app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid.T_Line_Guid);
	}
	
	
	
	
	void setOtherDataIfNeeded(){
		//if(!app.gIsDataChecked){
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
			
		//}
	}
	/**
	 * 修改upload数据表里对应uuid的状态，是否update.
	 * 并插入额外数据到数据表中  
	 */
	private void saveDataToDB(){
		
	//	mDao.insertUploadFile(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid,app.gIsDataChecked,true,true);
		mDao.insertUploadFile(app.mJugmentListParms.get(app.getCurrentRouteIndex()).m_RoutePeroid,false,true,true);
	//	app.gIsDataChecked=true;
		
		
		/**
		 * 插入额外信息到数据表中 
		 */
		for(int m =0;m<mExtralList.size();m++){
			try {
				SystemUtil.writeFileToSD(Setting.getExtralDataPath()+mExtralList.get(m).get("RecordLab"), 
						Setting.getExtralDataPath()+mExtralList.get(m).get("Object"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int k=1;k<mAddNewExtranalPartItemList.size();k++){
			if(mAddNewExtranalPartItemList.get(k).T_Measure_Type_Id==OnButtonListener.PictureDataId
					||mAddNewExtranalPartItemList.get(k).T_Measure_Type_Id==OnButtonListener.AudioDataId
					||mAddNewExtranalPartItemList.get(k).T_Measure_Type_Id==OnButtonListener.WaveDataId){
				String SqlStr = "insert into " +DBHelper.TABLE_Media +"( "
						+DBHelper.Media_Table.Line_Guid +","
						+DBHelper.Media_Table.Name + ","
						+DBHelper.Media_Table.Date + ","
						+DBHelper.Media_Table.Mime_Type + ","
						+DBHelper.Media_Table.Is_Uploaded + ","
						+DBHelper.Media_Table.FilePath
					+") values ('"
					+app.mLineJsonData.T_Line.T_Line_Guid+"','"
					+mAddNewExtranalPartItemList.get(k).RecordLab +"','"
					+SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDD)+"','"
					+mAddNewExtranalPartItemList.get(k).T_Measure_Type_Id +"',"
					+"'0'" +",'"
					+Setting.getExtralDataPath()+mAddNewExtranalPartItemList.get(k).RecordLab
					+"')";
				mDao.execSQLUpdate(SqlStr);
				
			}
		
			
		} 
	}	
}
