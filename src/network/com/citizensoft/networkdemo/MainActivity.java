package network.com.citizensoft.networkdemo;

import com.alibaba.fastjson.JSON;
import network.com.citizensoft.common.util.DateUtil;
import network.com.citizensoft.network.SocketCallTimeout;

import network.aic.xj.client.ServiceProvider;
import network.aic.xj.common.ResponseCode;
import network.aic.xj.common.context.request.*;
import network.aic.xj.common.context.response.*;
import network.aic.xj.common.request.*;
import network.aic.xj.common.response.*;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Handler _handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

		_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Toast.makeText(MainActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	private int[] _lockCommandIds = null;

	public void UploadRFID_Event(View view) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = "222.128.3.208";
				sp.Port = 10000;

				UploadRFIDRequest request = new UploadRFIDRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = "F9-2C-15-00-12-FC";// 本机MAC
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
					_handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	public void QueryCommand_Event(View view) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = "222.128.3.208";
				sp.Port = 10000;
				String planjson="";
				QueryServerCommandRequest request = new QueryServerCommandRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = "F9-2C-15-00-12-FC";// 本机MAC
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
						NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						for (CommandInfo ci : response.Args.Commands) {
							int icon = android.R.drawable.sym_action_email;
							Notification notification = new Notification.Builder(
									getApplicationContext()).setSmallIcon(icon)
									.setAutoCancel(true)
									.setContentText(ci.Data.length() <= 10?ci.Data : ci.Data.substring(0, 10))
									.setContentTitle(ci.Name).build();
							mNotificationManager.notify(ci.Name.hashCode(), notification);
							
							//zhengyangyong 2015-10-06
							if(ci.Name.equals("DeliverNormalPlan"))
							{
								 //ci.Data
								DeliverNormalPlanRequestArgs args = JSON.parseObject(ci.Data, DeliverNormalPlanRequestArgs.class);
								//处理Base64之后的巡检计划
								 planjson =  new String(Base64.decode(args.PlanData, Base64.DEFAULT),"utf-8");
								//对着C#的巡检计划建Class，写一个对应的Java类，然后完成JSON对象
							}
							else if(ci.Name.equals("DeliverTempPlan"))
							{
								//ci.Data
								DeliverTempPlanRequestArgs args = JSON.parseObject(ci.Data, DeliverTempPlanRequestArgs.class);
								//处理Base64之后的巡检计划
								 planjson =  new String(Base64.decode(args.PlanData, Base64.DEFAULT),"utf-8");
								//对着C#的临检计划建Class，写一个对应的Java类，然后完成JSON对象
							}
							else
							{
								//其他消息：ClearAllPlan、ClearNormalPlan、ClearTempPlan。。。			
							}
						}
					} else {

					}
					Message msg = new Message();
					msg.what = 0;
					msg.obj = response.Info.Code;
					_handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	public void QueryCommandWithLock_Event(View view) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = "222.128.3.208";
				sp.Port = 10000;

				QueryServerCommandRequest request = new QueryServerCommandRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = "F9-2C-15-00-12-FC";// 本机MAC
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
						NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						for (CommandInfo ci : response.Args.Commands) {
							int icon = android.R.drawable.sym_action_email;
							Notification notification = new Notification.Builder(
									getApplicationContext()).setSmallIcon(icon)
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
					_handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	public void AckCommand_Event(View view) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				synchronized (this) {
					if (_lockCommandIds != null && _lockCommandIds.length != 0) {
						ServiceProvider sp = new ServiceProvider();
						sp.ServerIP = "222.128.3.208";
						sp.Port = 10000;

						AckServerCommandRequest request = new AckServerCommandRequest();
						request.CreateTime = DateUtil.getCurrentDate();
						request.Source_MAC = "F9-2C-15-00-12-FC";// 本机MAC
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
							_handler.sendMessage(msg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	public void QueryServerInfo_Event(View view) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = "222.128.3.208";
				sp.Port = 10000;

				QueryServerInfoRequest request = new QueryServerInfoRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = "F9-2C-15-00-12-FC";// 本机MAC

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
					_handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void QueryOrganization_Event(View view) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = "222.128.3.208";
				sp.Port = 10000;

				QueryOrganizationRequest request = new QueryOrganizationRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = "F9-2C-15-00-12-FC";// 本机MAC

				SocketCallTimeout timeout = new SocketCallTimeout();
				timeout.ConnectTimeoutSeconds = 30;
				timeout.ReceiveTimeoutSeconds = 60;
				timeout.SendTimeoutSeconds = 60;

				try {
					QueryOrganizationResponse response = sp.Execute(request,
							timeout);

					if (response.Info.Code.equals(ResponseCode.OK)) {
						NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						int icon = android.R.drawable.sym_contact_card;
						Notification notification = new Notification.Builder(
								getApplicationContext())
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
					_handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void UploadMobilePhoneInfo_Event(View view) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ServiceProvider sp = new ServiceProvider();
				sp.ServerIP = "222.128.3.208";
				sp.Port = 10000;

				UploadMobilePhoneInfoRequest request = new UploadMobilePhoneInfoRequest();
				request.CreateTime = DateUtil.getCurrentDate();
				request.Source_MAC = "F9-2C-15-00-12-FC";// 本机MAC
				request.Source_IP = "2.2.2.2";
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
					_handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
