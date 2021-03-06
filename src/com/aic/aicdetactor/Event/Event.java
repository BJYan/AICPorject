package com.aic.aicdetactor.Event;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.http.conn.ConnectTimeoutException;

import network.aic.xj.client.ServiceProvider;
import network.aic.xj.common.ResponseCode;
import network.aic.xj.common.context.request.AckServerCommandRequestArgs;
import network.aic.xj.common.context.request.DeliverNormalPlanRequestArgs;
import network.aic.xj.common.context.request.DeliverTempPlanRequestArgs;
import network.aic.xj.common.context.request.QueryServerCommandRequestArgs;
import network.aic.xj.common.context.request.UploadFirstOpenTempPlanRequestArgs;
import network.aic.xj.common.context.request.UploadMobilePhoneInfoRequestArgs;
import network.aic.xj.common.context.request.UploadNormalPlanResultRequestArgs;
import network.aic.xj.common.context.request.UploadRFIDRequestArgs;
import network.aic.xj.common.context.request.UploadTempPlanResultRequestArgs;
import network.aic.xj.common.context.request.UploadWaveDataRequestArgs;
import network.aic.xj.common.context.response.CommandInfo;
import network.aic.xj.common.json.temp.result.TempPlanUploadResult;
import network.aic.xj.common.request.AckServerCommandRequest;
import network.aic.xj.common.request.QueryOrganizationRequest;
import network.aic.xj.common.request.QueryServerCommandRequest;
import network.aic.xj.common.request.QueryServerInfoRequest;
import network.aic.xj.common.request.UploadFirstOpenTempPlanRequest;
import network.aic.xj.common.request.UploadMobilePhoneInfoRequest;
import network.aic.xj.common.request.UploadNormalPlanResultRequest;
import network.aic.xj.common.request.UploadRFIDRequest;
import network.aic.xj.common.request.UploadTempPlanResultRequest;
import network.aic.xj.common.request.UploadWaveDataRequest;
import network.aic.xj.common.response.AckServerCommandResponse;
import network.aic.xj.common.response.QueryOrganizationResponse;
import network.aic.xj.common.response.QueryServerCommandResponse;
import network.aic.xj.common.response.QueryServerInfoResponse;
import network.aic.xj.common.response.UploadFirstOpenTempPlanResponse;
import network.aic.xj.common.response.UploadMobilePhoneInfoResponse;
import network.aic.xj.common.response.UploadNormalPlanResultResponse;
import network.aic.xj.common.response.UploadRFIDResponse;
import network.aic.xj.common.response.UploadTempPlanResultResponse;
import network.aic.xj.common.response.UploadWaveDataResponse;
import network.com.citizensoft.common.util.DateUtil;
import network.com.citizensoft.network.SocketCallTimeout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.Config.Config;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.DownloadNormalRootData;
import com.aic.aicdetactor.data.T_Temporary_Line;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.CommonDialogBtnListener;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.util.SystemUtil;
import com.alibaba.fastjson.JSON;

public class Event {
	public static final int Envent_Init= 300;
	public static final int LocalData_Init_Failed= Envent_Init+0;
	public static final int LocalData_Init_Success= Envent_Init+1;
	public final static int GetNewRouteLine_Message=Envent_Init+2;
	public final static int ReplaceRouteLineData_Message=Envent_Init+3;
	public final static int NetWork_Connecte_Timeout=Envent_Init+4;
	public final static int NetWork_MSG_Tips=Envent_Init+5;
	public final static int TEMP_ROUTELINE_DOWNLOAD_MSG=Envent_Init+6;
	public final static int Server_No_Data=Envent_Init+7;
	
	
	public final static int Upload_Data_Success=Envent_Init+8;
	public final static int Upload_Data_Failed=Envent_Init+9;
	
	
	private int[] _lockCommandIds = null;
	private static String TAG = "AICEvent";
	public static void UploadRFID_Event(View view,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.getServiceIP();//;
				sp.Port = Config.getServicePort();//;

				UploadRFIDRequest request = new UploadRFIDRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = Config.getMACAddress();//"F9-2C-15-00-12-FC";// 本机MAC
				// request.User = "";当前使用用户，保留
				request.Args = new UploadRFIDRequestArgs();
				request.Args.Read_Time = DateUtil.getCurrentDate();
				request.Args.RFID = String.valueOf(System.currentTimeMillis());

				SocketCallTimeout timeout = new SocketCallTimeout();
				timeout.ConnectTimeoutSeconds = 30;
				timeout.ReceiveTimeoutSeconds = 60;
				timeout.SendTimeoutSeconds = 60;

				try {
					UploadRFIDResponse response = sp.Execute(request, timeout);
					Message msg = new Message();
					msg.what = 0;
					msg.obj = response.Info.Code;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * 更新服务器巡检路线信息
	 * @param view
	 * @param activity
	 * @param handler
	 */
	public static void QueryCommand_Event(View view,final Activity activity,final Handler handler/*,final boolean bSearch,List<String>candlownLoadLineInfoList*/) {
		final boolean isLocalDebug =false;
		new Thread(new Runnable() {

			@Override
			public void run() {
				Setting setting = new Setting();
				String StrMessage="";
      if(!isLocalDebug){
				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.getServiceIP();//;
				sp.Port = Config.getServicePort();//;
				String planjson="";
				QueryServerCommandRequest request = new QueryServerCommandRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = Config.getMACAddress();//"F9-2C-15-00-12-FC";// 本机MAC
				// request.User = "";当前使用用户，保留
				request.Args = new QueryServerCommandRequestArgs();
				request.Args.LockSeconds = 0;

				SocketCallTimeout timeout = new SocketCallTimeout();
				timeout.ConnectTimeoutSeconds = 30;
				timeout.ReceiveTimeoutSeconds = 60;
				timeout.SendTimeoutSeconds = 60;

				try {
					QueryServerCommandResponse response = sp.Execute(request,
							timeout);
					if (response.Info.Code.equals(ResponseCode.OK)) {						
						NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
						if(response.Args.Commands.length<=0){
							//服务器上没数据
							 Message msg = handler.obtainMessage(Server_No_Data);
							 msg.obj="服务器上没巡检数据!";
							 handler.sendMessage(msg);
							 return;
						}
						for (CommandInfo ci : response.Args.Commands) {
							int icon = android.R.drawable.sym_action_email;
							Log.d(TAG, "ci="+ci +",ci.Data is "+ci.Data +"ci.Data.Lenth = "+ci.Data.length());
							Notification notification = new Notification.Builder(
									activity.getApplicationContext()).setSmallIcon(icon)
									.setAutoCancel(true)
									.setContentText(ci.Data.length() <= 10?ci.Data : ci.Data.substring(0, 10))
									.setContentTitle(ci.Name).build();
							mNotificationManager.notify(ci.Name.hashCode(), notification);
							
							//zhengyangyong 2015-10-06
							if(ci.Name.equals("DeliverNormalPlan")){ 
//								if(bSearch){
//									String name ="";
//									if(candlownLoadLineInfoList!=null){
//									candlownLoadLineInfoList.add(name);
//									}
//									}else{
								boolean isSpecialLine = false;
								DeliverNormalPlanRequestArgs args = JSON.parseObject(ci.Data, DeliverNormalPlanRequestArgs.class);
								//处理Base64之后的巡检计划
								 planjson =  new String(Base64.decode(args.PlanData, Base64.DEFAULT),"utf-8");
								 DownloadNormalRootData Normaldata=JSON.parseObject(planjson,DownloadNormalRootData.class);
								 for(int i=0;i< Normaldata.StationInfo.size();i++){
									 if(isSpecialLine){break;}
									 for(int j=0;j<Normaldata.StationInfo.get(i).DeviceItem.size();j++){
										 if(Normaldata.StationInfo.get(i).DeviceItem.get(j).Is_Special_Inspection>0){
											 isSpecialLine=true;
											 break;
										 }
									 }
								 }
								 Log.d(TAG,"name:"+ Normaldata.T_Line.Name+",guid:"+Normaldata.T_Line.T_Line_Guid+",T_Line_Content_Guid:"+Normaldata.T_Line.T_Line_Content_Guid);
								 LineDao dao = LineDao.getInstance(activity.getApplicationContext());
								 String filePath =setting.getData_Media_Director(CommonDef.FILE_TYPE_OriginaJson) +Normaldata.T_Line.T_Line_Guid;
								 boolean isFileExist= SystemUtil.isFileExist(filePath);
							     int isExit =dao.isOriginalLineExist(Normaldata.T_Line.T_Line_Guid,Normaldata.T_Line.T_Line_Content_Guid,filePath);
							     if(isExit==0){
							    	 if(StrMessage.length()>0){
							    		 StrMessage=StrMessage+"\n"+"完全相同的 "+Normaldata.T_Line.Name+"，不需要更新";
							    	 }else{
							    		 StrMessage="完全相同的 "+Normaldata.T_Line.Name+"，不需要更新";
							    	 }
							    	 continue;
							     }else if(isExit==2){
									 SystemUtil.writeFileToSD(filePath, planjson);
									 dao.insertNormalLineInfo(Normaldata.T_Line.Name,filePath,Normaldata.T_Line.T_Line_Guid,
											 Normaldata.getItemCounts(0,0,false,false),
											 Normaldata.getItemCounts(0,0,false,true),Normaldata.getItemCounts(0,0,true,true),
											 Normaldata.T_Worker,Normaldata.T_Turn,Normaldata.T_Period,Normaldata.T_Organization,isSpecialLine,Normaldata.T_Line.T_Line_Content_Guid);
									 if(StrMessage.length()>0){
										 StrMessage=StrMessage+"\n"+ "日常巡检 "+Normaldata.T_Line.Name+" 下载更新成功!";
									 }else{
									 StrMessage="日常巡检 "+Normaldata.T_Line.Name+" 下载更新成功!";
									 }
									 //Message msg = handler.obtainMessage(LocalData_Init_Success);
									// msg.obj=StrMessage;
									// handler.sendMessage(msg);
									 continue;
								 }else if(isExit==1){
									 if(StrMessage.length()>0){
										 StrMessage=StrMessage+"\n"+"已有相同的日常巡检名 "+Normaldata.T_Line.Name +",是否要更新?";
									 }else{
										 StrMessage="已有相同的日常巡检名 "+Normaldata.T_Line.Name +",是否要更新?";
									 }
									
									 //先保存为临时文件，等用户选择是否覆盖，如果选择是的话，再更改文件
									 filePath=filePath+"temp";
									 SystemUtil.writeFileToSD(filePath, planjson);
									 
									 //绝对路径+*+日常巡检总数+*+特殊巡检总数+巡检路线guid+线路contentguid
									 final String StrObj=filePath+"*"+Normaldata.getItemCounts(0, 0, false, true) +"*"+Normaldata.getItemCounts(0, 0, true, true)
											 +"*"+Normaldata.T_Line.T_Line_Guid+"*"+Normaldata.T_Line.T_Line_Content_Guid;
									 Message msg = handler.obtainMessage(GetNewRouteLine_Message);
									 msg.obj=StrObj;
									 handler.sendMessage(msg);
									continue;
								 }
							//	}
							}else if(ci.Name.equals("DeliverTempPlan")){
								//ci.Data
								DeliverTempPlanRequestArgs args = JSON.parseObject(ci.Data, DeliverTempPlanRequestArgs.class);
								//处理Base64之后的巡检计划
								 planjson =  new String(Base64.decode(args.PlanData, Base64.DEFAULT),"utf-8");
								//对着C#的临检计划建Class，写一个对应的Java类，然后完成JSON对象
								 T_Temporary_Line tempdata=JSON.parseObject(planjson,T_Temporary_Line.class);
								 if(StrMessage.length()>0){
									 StrMessage=StrMessage+"\n"+"临时路线 "+tempdata.Content+"更新成功!";
								 }else{
									 StrMessage="临时路线 "+tempdata.Content+"更新成功!";
								 }
																 
								// Message msg = handler.obtainMessage(TEMP_ROUTELINE_DOWNLOAD_MSG);
								// msg.obj=response.Info.Code;
								// handler.sendMessage(msg);
								 continue;
							}else{
								//其他消息：ClearAllPlan、ClearNormalPlan、ClearTempPlan。。。	
								Log.i(TAG,"command Name :"+ci.Name);
								StrMessage="其他信息!";
							}
							Log.d(TAG,"parseAic:"+planjson);
						}
					} else {
						Log.e(TAG,"ParseAic fail:"+response.Info.Code);
						StrMessage="服务器上无数据可下载!";
					}
					Message msg = handler.obtainMessage(NetWork_MSG_Tips);
					msg.obj = StrMessage +","+response.Info.Code;
					handler.sendMessage(msg);
				} catch (SocketTimeoutException  e) {
					// TODO Auto-generated catch block
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "Socket连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectTimeoutException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "网络连接异常，请检查网络";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(Exception e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "其他异常"+e.toString();
					handler.sendMessage(msg);
					e.printStackTrace();
				}
				} else {
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					String planjson = SystemUtil.openFile(Environment.getExternalStorageDirectory() + "/AICLine.txt");
					boolean isSpecialLine = false;
					if (planjson == null) {
						if (handler != null) {
							handler.sendEmptyMessage(LocalData_Init_Failed);
						}
						return;
					}
					// parse json data for insert databases
					final DownloadNormalRootData Normaldata = JSON.parseObject(planjson, DownloadNormalRootData.class);
					for (int i = 0; i < Normaldata.StationInfo.size(); i++) {
						if (isSpecialLine) {
							break;
						}
						for (int j = 0; j < Normaldata.StationInfo.get(i).DeviceItem.size(); j++) {
							if (Normaldata.StationInfo.get(i).DeviceItem.get(j).Is_Special_Inspection > 0) {
								isSpecialLine = true;
								break;
							}
						}
					}
					Log.d(TAG, "name:" + Normaldata.T_Line.Name+ ",guid:" + Normaldata.T_Line.T_Line_Guid	+ ",T_Line_Content_Guid:"
							+ Normaldata.T_Line.T_Line_Content_Guid);
					final LineDao dao = LineDao.getInstance(activity.getApplicationContext());
					String filePath =setting.getData_Media_Director(CommonDef.FILE_TYPE_OriginaJson) + Normaldata.T_Line.T_Line_Guid + ".txt";
					boolean isFileExist= SystemUtil.isFileExist(filePath);
					int isExit = dao.isOriginalLineExist(Normaldata.T_Line.T_Line_Guid,Normaldata.T_Line.T_Line_Content_Guid,filePath);
					if(isExit==0){
						if (handler != null) {
							Message msg = handler.obtainMessage(LocalData_Init_Success);
							msg.obj="完全相同，不需要更新";
							handler.sendMessage(msg);
						}
					}else if(isExit==2){
					
						try {
							SystemUtil.writeFileToSD(filePath, planjson);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// insert line information to correspondence databases
						dao.insertNormalLineInfo(Normaldata.T_Line.Name,
								filePath, Normaldata.T_Line.T_Line_Guid,
								Normaldata.getItemCounts(0, 0, false, false),
								Normaldata.getItemCounts(0, 0, false, true),
								Normaldata.getItemCounts(0, 0, true, true),
								Normaldata.T_Worker, Normaldata.T_Turn,
								Normaldata.T_Period, Normaldata.T_Organization,
								isSpecialLine,Normaldata.T_Line.T_Line_Content_Guid);

						if (handler != null) {
							Message msg = handler.obtainMessage(LocalData_Init_Success);
							msg.obj="本地数据更新成功!";
							handler.sendMessage(msg);
						}
					}else if(isExit==1){
						 filePath=filePath+"temp";
						 //绝对路径+*+日常巡检总数+*+特殊巡检总数+*+巡检路线guid
						 final String StrObj=filePath+"*"+Normaldata.getItemCounts(0, 0, false, true) +"*"+Normaldata.getItemCounts(0, 0, true, true)
								 +"*"+Normaldata.T_Line.T_Line_Guid+"*"+Normaldata.T_Line.T_Line_Content_Guid;

						 try {
							SystemUtil.writeFileToSD(filePath, planjson);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 Message msg = handler.obtainMessage(GetNewRouteLine_Message);
						 msg.obj=StrObj;
						 handler.sendMessage(msg);
					}
						
				}
			}
		}).start();
	}
	
	
	public void QueryCommandWithLock_Event(View view,final Activity activity,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.getServiceIP();//;
				sp.Port = Config.getServicePort();//;

				QueryServerCommandRequest request = new QueryServerCommandRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = Config.getMACAddress();//"F9-2C-15-00-12-FC";// 本机MAC
				// request.User = "";当前使用用户，保留
				request.Args = new QueryServerCommandRequestArgs();
				request.Args.LockSeconds = 30;

				SocketCallTimeout timeout = new SocketCallTimeout();
				timeout.ConnectTimeoutSeconds = 30;
				timeout.ReceiveTimeoutSeconds = 60;
				timeout.SendTimeoutSeconds = 60;

				try {
					QueryServerCommandResponse response = sp.Execute(request,
							timeout);
					if (response.Info.Code.equals(ResponseCode.OK)) {
						// 正确
						NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
						for (CommandInfo ci : response.Args.Commands) {
							int icon = android.R.drawable.sym_action_email;
							Notification notification = new Notification.Builder(
									activity.getApplicationContext()).setSmallIcon(icon)
									.setAutoCancel(true)
									.setContentText(ci.Data.length() <= 10?ci.Data : ci.Data.substring(0, 10))
									.setContentTitle(ci.Name).build();
							mNotificationManager.notify(ci.Name.hashCode(), notification);
						}

						synchronized (this) {
							_lockCommandIds = new int[response.Args.Commands.length];
							for (int i = 0; i < response.Args.Commands.length; i++) {
								_lockCommandIds[i] = response.Args.Commands[i].ID;
							}
						}

					} else {

					}
					Message msg = new Message();
					msg.what = 0;
					msg.obj = response.Info.Code;
					handler.sendMessage(msg);
				} catch (SocketTimeoutException  e) {
					// TODO Auto-generated catch block
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "Socket连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectTimeoutException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接异常，不能连接到服务器";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(Exception e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "其他异常"+e.toString();
					handler.sendMessage(msg);
					e.printStackTrace();
				}

			}
		}).start();
	}

	public void AckCommand_Event(View view,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				synchronized (this) {
					if (_lockCommandIds != null && _lockCommandIds.length != 0) {
						ServiceProvider sp = new ServiceProvider();
						sp.ServerIP = Config.getServiceIP();//;
						sp.Port = Config.getServicePort();//;

						AckServerCommandRequest request = new AckServerCommandRequest();
						request.CreateTime = DateUtil.getCurrentDate();
						request.Source_MAC = Config.getMACAddress();//"F9-2C-15-00-12-FC";// 本机MAC
						// request.User = "";当前使用用户，保留
						request.Args = new AckServerCommandRequestArgs();
						request.Args.IDs = _lockCommandIds;

						SocketCallTimeout timeout = new SocketCallTimeout();
						timeout.ConnectTimeoutSeconds = 30;
						timeout.ReceiveTimeoutSeconds = 60;
						timeout.SendTimeoutSeconds = 60;

						try {
							AckServerCommandResponse response = sp.Execute(
									request, timeout);
							Message msg = new Message();
							msg.what = 0;
							msg.obj = response.Info.Code;
							handler.sendMessage(msg);
						 } catch (SocketTimeoutException  e) {
							// TODO Auto-generated catch block
							Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
							msg.obj = "Socket连接超时";
							handler.sendMessage(msg);
							e.printStackTrace();
						}catch(ConnectTimeoutException  e){
							Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
							msg.obj = "连接超时";
							handler.sendMessage(msg);
							e.printStackTrace();
						}catch(ConnectException  e){
							Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
							msg.obj = "连接异常，不能连接到服务器";
							handler.sendMessage(msg);
							e.printStackTrace();
						}catch(Exception e){
							Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
							msg.obj = "其他异常"+e.toString();
							handler.sendMessage(msg);
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	/**
	 * APP查询服务器信息的时间信息
	 * @param view
	 * @param handler
	 */
	public void QueryServerInfo_Event(View view,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.getServiceIP();//;
				sp.Port = Config.getServicePort();//;

				QueryServerInfoRequest request = new QueryServerInfoRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = Config.getMACAddress();//"F9-2C-15-00-12-FC";// 本机MAC

				SocketCallTimeout timeout = new SocketCallTimeout();
				timeout.ConnectTimeoutSeconds = 30;
				timeout.ReceiveTimeoutSeconds = 60;
				timeout.SendTimeoutSeconds = 60;

				try {
					QueryServerInfoResponse response = sp.Execute(request,
							timeout);
					Message msg = new Message();
					msg.what = 0;
					msg.obj = response.Info.Code;
					handler.sendMessage(msg);
				} catch (SocketTimeoutException  e) {
					// TODO Auto-generated catch block
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "Socket连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectTimeoutException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接异常，不能连接到服务器";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(Exception e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "其他异常"+e.toString();
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 1.APP查询自身三级组织机构信息（总厂、分厂、车间）
	 * @param view
	 * @param activity
	 * @param handler
	 */
	public void QueryOrganization_Event(View view,final Activity activity,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.getServiceIP();//;
				sp.Port = Config.getServicePort();//;

				QueryOrganizationRequest request = new QueryOrganizationRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = Config.getMACAddress();//"F9-2C-15-00-12-FC";// 本机MAC

				SocketCallTimeout timeout = new SocketCallTimeout();
				timeout.ConnectTimeoutSeconds = 30;
				timeout.ReceiveTimeoutSeconds = 60;
				timeout.SendTimeoutSeconds = 60;

				try {
					QueryOrganizationResponse response = sp.Execute(request,
							timeout);

					if (response.Info.Code.equals(ResponseCode.OK)) {
						NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
						int icon = android.R.drawable.sym_contact_card;
						Notification notification = new Notification.Builder(
								activity.getApplicationContext())
								.setSmallIcon(icon)
								.setAutoCancel(true)
								.setContentText(
										String.format(
												"%s|%s|%s",
												response.Args.GroupName,
												response.Args.CorporationName,
												response.Args.WorkShopName))
								.setContentTitle("Organization").build();
						mNotificationManager.notify("Organization".hashCode(), notification);
					} else {

					}

					Message msg = new Message();
					msg.what = 0;
					msg.obj = response.Info.Code;
					handler.sendMessage(msg);
				} catch (SocketTimeoutException  e) {
					// TODO Auto-generated catch block
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "Socket连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectTimeoutException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接异常，不能连接到服务器";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(Exception e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "其他异常"+e.toString();
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void UploadMobilePhoneInfo_Event(View view,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.getServiceIP();//;
				sp.Port = Config.getServicePort();//;

				UploadMobilePhoneInfoRequest request = new UploadMobilePhoneInfoRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = Config.getMACAddress();//"F9-2C-15-00-12-FC";// 本机MAC
				request.Source_IP =Config.getLocalIPAddress();
				request.Args = new UploadMobilePhoneInfoRequestArgs();
				request.Args.Name = "TestAndroidAPP";

				SocketCallTimeout timeout = new SocketCallTimeout();
				timeout.ConnectTimeoutSeconds = 30;
				timeout.ReceiveTimeoutSeconds = 60;
				timeout.SendTimeoutSeconds = 60;

				try {
					UploadMobilePhoneInfoResponse response = sp.Execute(
							request, timeout);
					Message msg = new Message();
					msg.what = 0;
					msg.obj = response.Info.Code;
					handler.sendMessage(msg);
				} catch (SocketTimeoutException  e) {
					// TODO Auto-generated catch block
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "Socket连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectTimeoutException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接异常，不能连接到服务器";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(Exception e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "其他异常"+e.toString();
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}
	//UploadNormalPlanResultRequest
	public synchronized void  UploadNormalPlanResultInfo_Event(View view,final Handler handler,final String UploadData,final String filepath) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				//sp.ServerIP = ;//公网
				
				sp.ServerIP = Config.getServiceIP();//;
				sp.Port = Config.getServicePort();//;

				UploadNormalPlanResultRequest request = new UploadNormalPlanResultRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = Config.getMACAddress();// 本机MAC
				request.Source_IP = Config.getLocalIPAddress();
				request.Args = new UploadNormalPlanResultRequestArgs();
				
				String planjson=null;
				try {
					planjson = new String(Base64.encode(UploadData.getBytes(), Base64.DEFAULT),"utf-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				request.Args.UploadData = planjson;;

				SocketCallTimeout timeout = new SocketCallTimeout();
				timeout.ConnectTimeoutSeconds = 30;
				timeout.ReceiveTimeoutSeconds = 60;
				timeout.SendTimeoutSeconds = 60;

				try {
					UploadNormalPlanResultResponse response = sp.Execute(
							request, timeout);
					Message msg = new Message();
					if(response.Info.Code.equals(ResponseCode.OK)){
						msg.what = Upload_Data_Success;
					}else{
						msg.what = Upload_Data_Failed;
					}
					msg.obj = filepath;
					if(handler!=null){
					handler.sendMessage(msg);
					}
				} catch (SocketTimeoutException  e) {
					// TODO Auto-generated catch block
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "Socket连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectTimeoutException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接超时";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(ConnectException  e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "连接异常，不能连接到服务器";
					handler.sendMessage(msg);
					e.printStackTrace();
				}catch(Exception e){
					Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
					msg.obj = "其他异常"+e.toString();
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	//UploadWaveDataRequest
	/**
	 * 上传波形、音频、图片等2进制数据
	 * @param view
	 * @param handler
	 * @param UploadData
	 * @param RecordLab
	 */
		public static void  UploadWaveDataRequestInfo_Event(View view,final Handler handler,final byte[] UploadData,final String RecordLab,final String filePath) {

			new Thread(new Runnable() {

				@Override
				public void run() {

					ServiceProvider sp = new ServiceProvider();
					sp.ServerIP = Config.getServiceIP();//;
					sp.Port = Config.getServicePort();//;

					UploadWaveDataRequest request = new UploadWaveDataRequest();
					request.CreateTime = DateUtil.getCurrentDate();
					request.Source_MAC = Config.getMACAddress();// 本机MAC
					request.Source_IP = Config.getLocalIPAddress();
					request.Args = new UploadWaveDataRequestArgs();
					
					String planjson=null;
					try {
						planjson = new String(Base64.encode(UploadData, Base64.DEFAULT),"utf-8");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					HashMap<String,String> wavedatas = new HashMap<String,String>();
					wavedatas.put(RecordLab, planjson);
					request.Args.WaveDatas = wavedatas;

					SocketCallTimeout timeout = new SocketCallTimeout();
					timeout.ConnectTimeoutSeconds = 30;
					timeout.ReceiveTimeoutSeconds = 60;
					timeout.SendTimeoutSeconds = 60;

					try {
						UploadWaveDataResponse response = sp.Execute(
								request, timeout);
						Message msg = new Message();
						if(response.Info.Code.equals(ResponseCode.OK)){
							msg.what = Upload_Data_Success;
						}else{
							msg.what = Upload_Data_Failed;
						}
						msg.obj = filePath;
						if(handler!=null){
						handler.sendMessage(msg);
						}
					} catch (SocketTimeoutException  e) {
						// TODO Auto-generated catch block
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "Socket连接超时";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(ConnectTimeoutException  e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "连接超时";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(ConnectException  e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "连接异常，不能连接到服务器";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(Exception e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "其他异常"+e.toString();
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
			}).start();
		}	
		
		
		/**
		 * 上传读取到的RFID（扣）
		 * @param view
		 * @param handler
		 * @param RFIDStr
		 * @param ReadTimeStr,可为空，表示服务器端的时间
		 */
		public static void  UploadFirstOpenTempPlanRequest_Event(View view,final Handler handler,final String RFIDStr,final String ReadTimeStr) {

			new Thread(new Runnable() {

				@Override
				public void run() {

					ServiceProvider sp = new ServiceProvider();
					sp.ServerIP = Config.getServiceIP();//;
					sp.Port = Config.getServicePort();//;

					UploadFirstOpenTempPlanRequest request = new UploadFirstOpenTempPlanRequest();
					request.CreateTime = DateUtil.getCurrentDate();
					request.Source_MAC = Config.getMACAddress();// 本机MAC
					request.Source_IP = Config.getLocalIPAddress();
					request.Args = new UploadFirstOpenTempPlanRequestArgs();
					
					String rfid=null;
					try {
						rfid = new String(Base64.encode(RFIDStr.getBytes(), Base64.DEFAULT),"utf-8");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String RTimeStr=null;
					try {
						RTimeStr = new String(Base64.encode(ReadTimeStr.getBytes(), Base64.DEFAULT),"utf-8");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					request.Args.Feedback_Time = RTimeStr;
					request.Args.Guid=rfid;
					SocketCallTimeout timeout = new SocketCallTimeout();
					timeout.ConnectTimeoutSeconds = 30;
					timeout.ReceiveTimeoutSeconds = 60;
					timeout.SendTimeoutSeconds = 60;

					try {
						UploadFirstOpenTempPlanResponse response = sp.Execute(
								request, timeout);
						Message msg = new Message();
						msg.what = LocalData_Init_Success;
						msg.obj = response.Info.Code;
						if(handler!=null){
						handler.sendMessage(msg);
						}
					} catch (SocketTimeoutException  e) {
						// TODO Auto-generated catch block
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "Socket连接超时";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(ConnectTimeoutException  e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "连接超时";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(ConnectException  e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "连接异常，不能连接到服务器";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(Exception e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "其他异常"+e.toString();
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
			}).start();
		}	
		
		/**
		 * 上传临时巡检结果
		 * @param view
		 * @param handler
		 * @param UploadData
		 */
		public void UploadTempPlanResultRequest_Event(View view,final Handler handler,final TempPlanUploadResult UploadData) {

			new Thread(new Runnable() {

				@Override
				public void run() {

					ServiceProvider sp = new ServiceProvider();
					
					sp.ServerIP = Config.getServiceIP();
					sp.Port = Config.getServicePort();

					UploadTempPlanResultRequest request = new UploadTempPlanResultRequest();
					request.CreateTime = DateUtil.getCurrentDate();
					request.Source_MAC = Config.getMACAddress();// 本机MAC
					request.Source_IP = Config.getLocalIPAddress();
					request.Args = new UploadTempPlanResultRequestArgs();
					
//					String planjson=null;
//					try {
//						planjson = new String(Base64.encode(UploadData, Base64.DEFAULT),"utf-8");
//					} catch (UnsupportedEncodingException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}

					request.Args.UploadData = UploadData;;

					SocketCallTimeout timeout = new SocketCallTimeout();
					timeout.ConnectTimeoutSeconds = 30;
					timeout.ReceiveTimeoutSeconds = 60;
					timeout.SendTimeoutSeconds = 60;

					try {
						UploadTempPlanResultResponse response = sp.Execute(
								request, timeout);
						Message msg = new Message();
						if(response.Info.Code.equals(ResponseCode.OK)){
							msg.arg1=1;	
						}else{
							msg.arg1=0;
						}
						msg.what = LocalData_Init_Success;
						msg.obj = response.Info.Code;
						if(handler!=null){
						handler.sendMessage(msg);
						}
					} catch (SocketTimeoutException  e) {
						// TODO Auto-generated catch block
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "Socket连接超时";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(ConnectTimeoutException  e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "连接超时";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(ConnectException  e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "连接异常，不能连接到服务器";
						handler.sendMessage(msg);
						e.printStackTrace();
					}catch(Exception e){
						Message msg = handler.obtainMessage(NetWork_Connecte_Timeout);
						msg.obj = "其他异常"+e.toString();
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
			}).start();
		}
}
