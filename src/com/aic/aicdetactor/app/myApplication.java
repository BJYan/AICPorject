package com.aic.aicdetactor.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;

import com.aic.aicdetactor.bluetooth.BluetoothConstast;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.comm.LineType;
import com.aic.aicdetactor.data.DownloadNormalRootData;
import com.aic.aicdetactor.data.JugmentParms;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.util.SystemUtil;
import com.alibaba.fastjson.JSON;

public class myApplication extends Application
{
    private static final String TAG = "aicdetector";
    
    //当前巡检路线的序号
    private int mRouteIndex = -1;
    //当前巡检路线下站点序号，序号指的是JSON站点数组的序号
    public int mStationIndex =-1;
    //当前巡检路线下 巡检的设备数组的序号
    private int mDeviceIndex = -1;
    //当前巡检的PartItemData
    public int mPartItemIndex = -1;
    
    public boolean isTest=false;
    private boolean isSpecialLine=false;
    private LineType mRouteType;
    //如果为false，数据从原始数据表中获取，否则，从GetUploadJsonFile中获取
    public boolean gIsDataChecked=false;
    //顶层常规巡检还是特定巡检。
    private String gRouteClassName = "";
    //当前路线名称
    private String gRouteName = "";
    //登录的工人用户名
    private String mWorkerName = null;
    //登录用户名对应的密码
    private String mWorkerPwd = null;
    //登录工人的工号
    public String mWorkerNumber = null;
    //生成第六个节点用的信息
    public String mStartDate = null;
    public String mTurnNumber = null;
    public String mTurnStartTime = null;
    public String mTurnEndTime = null;
    private int mIScreenWidth=0;
    private int mIScreenHeight=0;
    private String gCurGsonFilePath="";
    public String mCurLinkedBLEAddress="";
    public boolean mBLEIsConnected=false;
    public  ArrayList<PartItemJsonUp> gCurPartItemList=null;
    //当前日常巡检数据，不包括特巡数据
   // private DownloadNormalRootData mNormalLineJsonData=null;
    //当前特巡数据，不包括日常巡检数据
   // private DownloadNormalRootData mSpecialLineJsonData=null;
    //剔除后的当前巡检数据
    public DownloadNormalRootData mLineJsonData=null;
    public DownloadNormalRootData mLocalSearchLineJsonData=null;
    //
	private List<String> mTMPRouteFileList = null;
	private boolean gBLogIn = false;
	public List<JugmentParms> mJugmentListParms=null;
	//通过NFC获取ID号 及打卡
	private String mNFCCard="";
	
	public void setNFCId(String Id){
		mNFCCard = Id;
	}
	public String getNFCId(){
		return mNFCCard;
	}
	public void setCurrentDeviceIndex(int DeviceIndex){
		mDeviceIndex=DeviceIndex;
	}
	
	public int getCurrentDeviceIndex(){
		return mDeviceIndex;
	}
	
	public void setParItemIndex(int index,String Name){
		mPartItemIndex = index;
	}
	public void setScreenWidth(int width){
		mIScreenWidth = width;
	}
	
	public void setScreenHeight(int height){
		mIScreenHeight = height;
	}
	
	public int getScreenWidth(){
		return mIScreenWidth;
	}
	
	public int getScreenHeight(){
		return mIScreenHeight;
	}
	
	public void setCategoryTitle(String str){
		gRouteClassName = str;
	}
	
	public String getCategoryTitle(){
		return gRouteClassName;
	}
	
	public boolean isSpecialLine(){
		if(mRouteType ==LineType.SpecialRoute ){
			isSpecialLine=true;
		}else{
			isSpecialLine=false;
		}	
		return isSpecialLine;
	}
	
	public void setLineType(LineType type){
			mRouteType =type;
	}
	public void setgRouteName(String RouteName){
		this.gRouteName=RouteName;
	}
	
	public String getgRouteName(){
		return this.gRouteName;
	}
	/**
	 * 生成需要保存的JSON文件名字
	 * @return
	 */
//	public String genXJFileName(){
//		return Environment.getExternalStorageDirectory()+"/"+mStrGuid+mWorkerNumber +".txt";
//	}
    
	public void setCurGsonPath(String path){
		gCurGsonFilePath = path;
	}
	
	public String  getCurGsonPath(){
		return gCurGsonFilePath;
	}
    /**
     * 设置当前的巡检路线的序号，指的是ListView里的序号，从0开始
     * @param routeIndex
     */
    public void setCurrentRouteIndex(int routeIndex){
    	this.mRouteIndex = routeIndex;
    }
        
    /**
     * 获取当前的巡检路线序号 到全局变量
     * @return
     */
    public int getCurrentRouteIndex(){
    	return this.mRouteIndex;
    }
    
    /**
     * 整个应用启动的第一个接口
     */
    @Override
    public void onCreate()
    {
        super.onCreate(); 
        mApplication = this;
        Setting set= new Setting();
       BluetoothLeControl.genDownLoadCommand((byte)0x7f, (byte)0x14,(byte) 0xd2, (byte)0, (byte)0,0,0);   
       SharedPreferences sharedPreferences = getSharedPreferences(BluetoothConstast.BLEXML, this.getApplicationContext().MODE_PRIVATE);
       mCurLinkedBLEAddress = sharedPreferences.getString(BluetoothConstast.BLEAddress, "B0:B4:48:CC:2D:84");
       byte[]bytes={0,0,0x0f,(byte) 0xa1,};
       
       int i = ((((bytes[0] & 0xff) << 24 | (bytes[1] & 0xff)) << 16) | (bytes[2] & 0xff)) << 8 | (bytes[3] & 0xff);
       
       int k =0;
       k++;
       
    }
    
    
    
    private static myApplication mApplication = null;
    
    public static myApplication getApplication() {
        return mApplication;
    }
         
    /**
     * 初始化系统信息
     * @return
     */
    public int InitData(){
    	return 0;
    }
    
    public void setLogInStatus(boolean loginSuccess){
    	this.gBLogIn=loginSuccess;
    }
    
    public boolean isLogin(){
    	return gBLogIn;
    }
    
    public void setLoginWorkerName(String name){
    	mWorkerName=name;
    }
    public String getLoginWorkerName(){
    	return mWorkerName;
    }
    public void setLoginWorkerPwd(String pwd){
    	mWorkerPwd=pwd;
    }
    public String getLoginWorkerPwd(){
    	return mWorkerPwd;
    }
  //把同一个JSON文件数据里的 日常巡检及特殊巡检数据分离,在stationAdapter里把文件中的日常或特殊巡检数据分离出来
    public DownloadNormalRootData getLineDataClassifyFromOneFile(LineType type){
  		 String planjson = SystemUtil.openFile(getCurGsonPath());
  		 if(planjson==null){
  			
  			 return null;
  		 }
  		 if(mLineJsonData!=null){mLineJsonData=null;}
  		//mLineJsonData=JSON.parseObject(planjson,DownloadNormalRootData.class);  
  		
  		if(type==LineType.NormalRoute){ 
  			mLineJsonData=JSON.parseObject(planjson,DownloadNormalRootData.class);  		
	  		//剔除特殊巡检数据
	  		for (int i = 0; i < mLineJsonData.StationInfo.size(); i++) {
	  			try {
	  				for (int deviceIndex = 0; deviceIndex < mLineJsonData.StationInfo.get(i).DeviceItem.size(); deviceIndex++) {
	  					if (Integer.valueOf(mLineJsonData.StationInfo.get(i).DeviceItem.get(deviceIndex).Is_Special_Inspection) > 0) {
	  						mLineJsonData.StationInfo.get(i).DeviceItem.remove(deviceIndex);
	  					}
	  				}
	  				if(mLineJsonData.StationInfo.get(i).DeviceItem.size()==0){
	  					mLineJsonData.StationInfo.remove(i);
	  				}
	  				
	  			} catch (Exception e) {
	  				e.printStackTrace();
	  			}	
	  		}
	  	  		
  		}else if(type==LineType.SpecialRoute){
  			mLineJsonData=JSON.parseObject(planjson,DownloadNormalRootData.class);
	  		//剔除日常巡检数据
	  		for (int i = 0; i < mLineJsonData.StationInfo.size(); i++) {
	  			try {
	  				for (int deviceIndex = 0; deviceIndex < mLineJsonData.StationInfo.get(i).DeviceItem.size(); deviceIndex++) {
	  					if (Integer.valueOf(mLineJsonData.StationInfo.get(i).DeviceItem.get(deviceIndex).Is_Special_Inspection) <= 0) {
	  						mLineJsonData.StationInfo.get(i).DeviceItem.remove(deviceIndex);
	  					}
	  						
	  				}
	  				if(mLineJsonData.StationInfo.get(i).DeviceItem.size()==0){
	  					mLineJsonData.StationInfo.remove(i);
	  				}
	  				
	  			} catch (Exception e) {
	  				e.printStackTrace();
	  			}	
	  		}
	  		
  		}else if(type==LineType.AllRoute){
  			mLineJsonData=JSON.parseObject(planjson,DownloadNormalRootData.class);
  		}
  		return mLineJsonData;
  	}
}