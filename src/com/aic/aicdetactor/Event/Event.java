package com.aic.aicdetactor.Event;

import java.io.IOException;

import network.aic.xj.client.ServiceProvider;
import network.aic.xj.common.ResponseCode;
import network.aic.xj.common.context.request.AckServerCommandRequestArgs;
import network.aic.xj.common.context.request.DeliverNormalPlanRequestArgs;
import network.aic.xj.common.context.request.DeliverTempPlanRequestArgs;
import network.aic.xj.common.context.request.QueryServerCommandRequestArgs;
import network.aic.xj.common.context.request.UploadMobilePhoneInfoRequestArgs;
import network.aic.xj.common.context.request.UploadRFIDRequestArgs;
import network.aic.xj.common.context.response.CommandInfo;
import network.aic.xj.common.request.AckServerCommandRequest;
import network.aic.xj.common.request.QueryOrganizationRequest;
import network.aic.xj.common.request.QueryServerCommandRequest;
import network.aic.xj.common.request.QueryServerInfoRequest;
import network.aic.xj.common.request.UploadMobilePhoneInfoRequest;
import network.aic.xj.common.request.UploadRFIDRequest;
import network.aic.xj.common.response.AckServerCommandResponse;
import network.aic.xj.common.response.QueryOrganizationResponse;
import network.aic.xj.common.response.QueryServerCommandResponse;
import network.aic.xj.common.response.QueryServerInfoResponse;
import network.aic.xj.common.response.UploadMobilePhoneInfoResponse;
import network.aic.xj.common.response.UploadRFIDResponse;
import network.com.citizensoft.common.util.DateUtil;
import network.com.citizensoft.network.SocketCallTimeout;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.aic.aicdetactor.Config.Config;
import com.aic.aicdetactor.data.AICData;
import com.aic.aicdetactor.data.DownloadNormalData;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.SystemUtil;
import com.alibaba.fastjson.JSON;

public class Event {
	private int[] _lockCommandIds = null;
	public static void UploadRFID_Event(View view,final Handler handler,final String MacStr) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.server_Ip;//"222.128.3.208";
				sp.Port = Config.server_port;//10000;

				UploadRFIDRequest request = new UploadRFIDRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = MacStr;//"F9-2C-15-00-12-FC";// 本机MAC
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

	public static void QueryCommand_Event(View view,final Activity activity,final String MacStr,final Handler handler) {
		final boolean isLocalDebug =true;
		new Thread(new Runnable() {

			@Override
			public void run() {
      if(!isLocalDebug){
				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.server_Ip;//"222.128.3.208";
				sp.Port = Config.server_port;//10000;
				String planjson="";
				QueryServerCommandRequest request = new QueryServerCommandRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = MacStr;//"F9-2C-15-00-12-FC";// 本机MAC
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
							
							//zhengyangyong 2015-10-06
							if(ci.Name.equals("DeliverNormalPlan"))
							{ boolean isSpecialLine = false;
								 //ci.Data
								DeliverNormalPlanRequestArgs args = JSON.parseObject(ci.Data, DeliverNormalPlanRequestArgs.class);
								//处理Base64之后的巡检计划
								 planjson =  new String(Base64.decode(args.PlanData, Base64.DEFAULT),"utf-8");
								 //parse json data for insert databases
								 DownloadNormalData Normaldata=JSON.parseObject(planjson,DownloadNormalData.class);
								 for(int i=0;i< Normaldata.StationInfo.size();i++){
									 if(isSpecialLine){break;}
									 for(int j=0;j<Normaldata.StationInfo.get(i).DeviceItem.size();j++){
										 if(Normaldata.StationInfo.get(i).DeviceItem.get(j).Is_Special_Inspection>0){
											 isSpecialLine=true;
											 break;
										 }
									 }
								 }
								 Log.d("AICtest","name:"+ Normaldata.T_Line.Name+",guid:"+Normaldata.T_Line.T_Line_Guid+",T_Line_Content_Guid:"+Normaldata.T_Line.T_Line_Content_Guid);
								//save data as local file
								 String filePath = Environment.getExternalStorageDirectory()+"/"+Normaldata.T_Line.T_Line_Guid+".txt";
								 SystemUtil.writeFileToSD(filePath, planjson);
								 //insert line information to correspondence databases
								 RouteDao dao = RouteDao.getInstance(activity.getApplicationContext());
								 dao.insertNormalLineInfo(Normaldata.T_Line.Name,filePath,Normaldata.T_Line.T_Line_Guid,
										 Normaldata.getItemCounts(0,0,false),
										 Normaldata.getItemCounts(0,0,true),Normaldata.T_Worker,Normaldata.T_Turn,Normaldata.T_Organization,isSpecialLine);
							}
							else if(ci.Name.equals("DeliverTempPlan"))
							{
								//ci.Data
								DeliverTempPlanRequestArgs args = JSON.parseObject(ci.Data, DeliverTempPlanRequestArgs.class);
								//处理Base64之后的巡检计划
								 planjson =  new String(Base64.decode(args.PlanData, Base64.DEFAULT),"utf-8");
								//对着C#的临检计划建Class，写一个对应的Java类，然后完成JSON对象
								 AICData.DownloadTemporaryData tempdata=JSON.parseObject(planjson,AICData.DownloadTemporaryData.class);
							}
							else
							{
								//其他消息：ClearAllPlan、ClearNormalPlan、ClearTempPlan。。。	
								Log.i("aicCommand","command Name :"+ci.Name);
								
							}
							Log.d("parseAic","parseAic:"+planjson);
						}
					} else {
						Log.e("parseAic","ParseAic fail:"+response.Info.Code);
					}
					Message msg = new Message();
					msg.what = 0;
					msg.obj = response.Info.Code;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      }else{
    	 String  planjson = SystemUtil.openFile(Environment.getExternalStorageDirectory()+"/AICLine.txt");
    	 boolean isSpecialLine = false;
			 //parse json data for insert databases
			 DownloadNormalData Normaldata=JSON.parseObject(planjson,DownloadNormalData.class);
			 for(int i=0;i< Normaldata.StationInfo.size();i++){
				 if(isSpecialLine){break;}
				 for(int j=0;j<Normaldata.StationInfo.get(i).DeviceItem.size();j++){
					 if(Normaldata.StationInfo.get(i).DeviceItem.get(j).Is_Special_Inspection>0){
						 isSpecialLine=true;
						 break;
					 }
				 }
			 }
			 Log.d("AICtest","name:"+ Normaldata.T_Line.Name+",guid:"+Normaldata.T_Line.T_Line_Guid+",T_Line_Content_Guid:"+Normaldata.T_Line.T_Line_Content_Guid);
			//save data as local file
			 String filePath = Environment.getExternalStorageDirectory()+"/"+Normaldata.T_Line.T_Line_Guid+".txt";
			 try {
				SystemUtil.writeFileToSD(filePath, planjson);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 //insert line information to correspondence databases
			 RouteDao dao = RouteDao.getInstance(activity.getApplicationContext());
			 dao.insertNormalLineInfo(Normaldata.T_Line.Name,filePath,Normaldata.T_Line.T_Line_Guid,
					 Normaldata.getItemCounts(0,0,false),
					 Normaldata.getItemCounts(0,0,true),Normaldata.T_Worker,Normaldata.T_Turn,Normaldata.T_Organization,isSpecialLine);
			 
			 
			 Message msg = new Message();
				msg.what = 0;
				msg.obj = "#ok";
				handler.sendMessage(msg);
      }
			}
		}).start();
	}
	
	public void QueryCommandWithLock_Event(View view,final Activity activity,final String MACStr,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.server_Ip;//"222.128.3.208";
				sp.Port = Config.server_port;//10000;

				QueryServerCommandRequest request = new QueryServerCommandRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = MACStr;//"F9-2C-15-00-12-FC";// 本机MAC
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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	public void AckCommand_Event(View view,final String MacStr,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				synchronized (this) {
					if (_lockCommandIds != null && _lockCommandIds.length != 0) {
						ServiceProvider sp = new ServiceProvider();
						sp.ServerIP = Config.server_Ip;//"222.128.3.208";
						sp.Port = Config.server_port;//10000;

						AckServerCommandRequest request = new AckServerCommandRequest();
						request.CreateTime = DateUtil.getCurrentDate();
						request.Source_MAC = MacStr;//"F9-2C-15-00-12-FC";// 本机MAC
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
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	public void QueryServerInfo_Event(View view,final String MacStr,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.server_Ip;//"222.128.3.208";
				sp.Port = Config.server_port;//10000;

				QueryServerInfoRequest request = new QueryServerInfoRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = MacStr;//"F9-2C-15-00-12-FC";// 本机MAC

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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void QueryOrganization_Event(View view,final Activity activity,final String MacStr,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.server_Ip;//"222.128.3.208";
				sp.Port = Config.server_port;//10000;

				QueryOrganizationRequest request = new QueryOrganizationRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = MacStr;//"F9-2C-15-00-12-FC";// 本机MAC

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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void UploadMobilePhoneInfo_Event(View view,final String MacStr,final String IpStr,final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = Config.server_Ip;//"222.128.3.208";
				sp.Port = Config.server_port;//10000;

				UploadMobilePhoneInfoRequest request = new UploadMobilePhoneInfoRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = MacStr;//"F9-2C-15-00-12-FC";// 本机MAC
				request.Source_IP = IpStr;//"2.2.2.2";
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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
