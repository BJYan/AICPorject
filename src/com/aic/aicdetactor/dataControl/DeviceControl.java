package com.aic.aicdetactor.dataControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.aic.aicdetactor.Interface.OnButtonListener;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;
import com.aic.aicdetactor.condition.ConditionalJudgement;
import com.aic.aicdetactor.data.DeviceItemJson;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.util.SystemUtil;
/**
 * 对当前设备下的数据进行筛选、保存、正反序等操作.
 * 
 * 通过传入一个设备数据参数
 * @author AIC
 *
 */
public class DeviceControl extends DeviceItemJson {
	String TAG = "DeviceControl";
	private int mPartItemIndex = 0;
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
	//选择运行/停机/备用等后需生成的一个PartItem数据结构
	private PartItemJsonUp mPartItemAfterItemDef =null;
	
	private List<HashMap<String,Object>> mExtralList;
	
	public DeviceControl(DeviceItemJson device){
		clone(device);
		mExtralList = new ArrayList<HashMap<String,Object>>();
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
	 * 获取原始数据，用于显示已测量的数据测试情况
	 * 
	 * @return
	 */
	public PartItemJsonUp getCurOriPartItem(){
		return mOriPartItemList.get(mPartItemIndex);
	}
	
	public ArrayList<PartItemJsonUp> resetData(){
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
			for (int i = 0; i < PartItem.size(); i++) {
				
				item =  PartItem.get(i);
				mPartItemList.add(item);
				mOriPartItemList.add(item);
				mPartItemAfterSelectedList.add(item);
			}
			//app.gCurPartItemList = mPartItemAfterSelectedList;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return mPartItemAfterSelectedList;
	}
	
	public String getCurDeviceExitDataGuid(){
		return Data_Exist_Guid;
	}
	public void setPartItemStartTime()
	{
		mPartItemList.get(mPartItemIndex).setSartDate();
	}
	
	public void setPartItemEndTimeAndTotalTime(){
		mPartItemList.get(mPartItemIndex).setEndDate();
		mPartItemList.get(mPartItemIndex).calcCheckDuration();
	}
	
	public void revertListViewData(){
		
		ArrayList<PartItemJsonUp> ItemList = new ArrayList<PartItemJsonUp>();
		for(int i= mPartItemAfterSelectedList.size()-1;i>=0;i--){
			ItemList.add(mPartItemAfterSelectedList.get(i));
		}
		mPartItemList.clear();
		mPartItemAfterSelectedList.clear();
		PartItem.clear();
		for(int m= 0;m<ItemList.size();m++){
			mPartItemList.add(ItemList.get(m));
			mPartItemAfterSelectedList.add(ItemList.get(m));
			PartItem.add(ItemList.get(m));
		}		
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
		Item_Define=DeviceItemDef;
		setStartDate();
		genPartItemDataAfterItemDef();
		mExtralList.clear();
	}
	//mDeviceItemCahce
	/**
	 * 巡检完该deviceItem时 设置一些参数进来。
	 */
	public void setFinishDeviceCheckFlagAndSaveDataToSD(int OmissionCheck){
		setRFChecked();
		setDeviceChecked();
		setEndDate();
		
		if(Is_Special_Inspection==1){
			setIsOmissionCheck(0);
		}else{			
		setIsOmissionCheck(OmissionCheck);
		}
		
		mPartItemIndex=0;
		saveDeviceItemData();

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
		mPartItemList.get(mPartItemIndex).Item_Define=Item_Define;
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
	 * 选择运行/停机/备用后生成的PartItem对象
	 */
	public void genPartItemDataAfterItemDef(){
		if(mPartItemAfterItemDef!=null){
			mPartItemAfterItemDef=null;
		}
		mPartItemAfterItemDef = new PartItemJsonUp();
		mPartItemAfterItemDef.Check_Content=Name;
		mPartItemAfterItemDef.Extra_Information="设备级";
		mPartItemAfterItemDef.T_Item_Abnormal_Grade_Id=2;
		mPartItemAfterItemDef.T_Item_Abnormal_Grade_Code="01";
		mPartItemAfterItemDef.Item_Define=Item_Define;
	}
	
	public void saveDeviceItemData(){
		//先保存临时device数据，并把mPartItemAfterItemDef添加进去。
		//再将临时device数据添加到即将保存的json数据中
		PartItem.clear();
		for(PartItemJsonUp part:mPartItemList){
			part.Item_Define=Item_Define;
			PartItem.add(part);
		}
		PartItem.add(mPartItemAfterItemDef);	
	}
	
}
