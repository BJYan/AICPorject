package com.aic.aicdetactor.activity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogCancelListener;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogOKListener;
import com.aic.aicdetactor.service.DataService;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BlueToothRenameActivity extends CommonActivity implements OnClickListener,
		AltDialogOKListener,AltDialogCancelListener{
	protected static final String TAG = "BlueToothRenameActivity";
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	Button bindBtn;
	boolean isbinded = false;
	Intent btDevIntent;
	BluetoothDevice Dev;
	BluetoothSocket btSocket; 
	//BluetoothLeControl mBTControl;
	private BroadcastReceiver searchDevices = new BroadcastReceiver() {  
		  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  

            BluetoothDevice device = null;  
            // 搜索设备时，取得设备的MAC地址  
            if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){  
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                switch (device.getBondState()) {  
                case BluetoothDevice.BOND_BONDING:  
                    Log.d(TAG, "正在配对......");
                    Toast.makeText(BlueToothRenameActivity.this, "正在绑定请稍后…", Toast.LENGTH_SHORT).show();
                    break;  
                case BluetoothDevice.BOND_BONDED:  
                    Log.d(TAG, "完成配对");  
                    //connect(device);//连接设备  
        			isbinded = true;
        			bindBtn.setText("取消绑定");
        			Toast.makeText(BlueToothRenameActivity.this, "绑定成功！", Toast.LENGTH_SHORT).show();
                    break;  
                case BluetoothDevice.BOND_NONE:  
                    Log.d(TAG, "取消配对");  
        			isbinded = false;
        			bindBtn.setText("绑定");
        			Toast.makeText(BlueToothRenameActivity.this, "已取消配对！！", Toast.LENGTH_SHORT).show();
                default:  
                    break;  
                }  
            }  
              
        }  
    };  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_sensor_rename);
		
		setActionBar("传感器设置",true); 
		btDevIntent = getIntent();
		Dev = btDevIntent.getParcelableExtra("BluetoothDev");
		bindBtn = (Button) findViewById(R.id.bluetooth_bind_btn);
		bindBtn.setOnClickListener(this);
		TextView DevName = (TextView) findViewById(R.id.bluetooth_dev_name);
		DevName.setText(Dev.getName());
		DevName.setOnClickListener(this);
		if(Dev.getBondState()==BluetoothDevice.BOND_BONDED) {
			isbinded = true;
			bindBtn.setText("取消绑定");
		}
		if(Dev.getBondState()==BluetoothDevice.BOND_NONE) {
			isbinded = false;
			bindBtn.setText("绑定");
		}
		//mBTControl = BluetoothLeControl.getInstance(this.getApplicationContext());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		registerReceiver(searchDevices, filter);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(searchDevices);
	}
	
    private void connect(BluetoothDevice btDev) {  
        UUID uuid = UUID.fromString(SPP_UUID);  
        try {  
            btSocket = btDev.createRfcommSocketToServiceRecord(uuid);  
            Log.d("BlueToothTestActivity", "开始连接...");  
            btSocket.connect();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    } 

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bluetooth_bind_btn:
			if(!isbinded) showAlterDialog(this,"是否绑定？","原传感器将解除绑定",this,this);
			else showAlterDialog(this,"是否取消绑定？",null,this,this);
			break;
		case R.id.bluetooth_dev_name:
			showReNameDialog();
			break;
			
		default:
			break;
		}
	}

	private void showReNameDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("设备重命名");
		View renameDialog = getLayoutInflater().inflate(R.layout.dialog_rename_layout, null);
		EditText DevRename = (EditText) renameDialog.findViewById(R.id.bluetooth_rename);
		DevRename.setText(Dev.getName());
		builder.setView(renameDialog);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.show();
		//window.setContentView(R.layout.dialog_rename_layout);
	}

	@Override
	public void onComDialogCancelListener(CommonAlterDialog dialog) {
		// TODO Auto-generated method stub
		dialog.dismiss();
	}

	@Override
	public void onComDialogOKListener(CommonAlterDialog dialog) {
		// TODO Auto-generated method stub
		if(isbinded) { 
            if (Dev.getBondState() == BluetoothDevice.BOND_BONDED) {  
                //利用反射方法调用BluetoothDevice.createBond(BluetoothDevice remoteDevice);  
                try {
					Method createBondMethod = BluetoothDevice.class  
					        .getMethod("removeBond");  
					Log.d(TAG, "取消配对");  
					isbinded = !(Boolean) createBondMethod.invoke(Dev);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
                  
            }else if(Dev.getBondState() == BluetoothDevice.BOND_BONDED){  
                //connect(Dev);
            //	mBTControl.Connection(Dev.getAddress());
            	//mBTControl.setParamates(mHandle);
            	//mBTControl.Communication2Bluetooth(BluetoothLeControl.fta,BluetoothLeControl.fta.length);
            }
			//if(!isbinded) bindBtn.setText("绑定");
		} else {  
            if (Dev.getBondState() == BluetoothDevice.BOND_NONE) {  
                //利用反射方法调用BluetoothDevice.createBond(BluetoothDevice remoteDevice);  
                try {
					Method createBondMethod = BluetoothDevice.class  
					        .getMethod("createBond");  
					Log.d(TAG, "开始配对");  
					isbinded = (Boolean) createBondMethod.invoke(Dev);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
                  
            }else if(Dev.getBondState() == BluetoothDevice.BOND_BONDED){  
                //connect(Dev);  
            	Log.d(TAG," BANDDING");
            } 
			//if(isbinded) bindBtn.setText("取消绑定");
		}
		dialog.dismiss();
	}
	
	Handler mHandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case BluetoothLeControl.MessageReadDataFromBT:
				Log.d(TAG, "HandleMessage() " +(msg.obj).toString());
				break;
			}
			super.handleMessage(msg);
		}
		
	};

}
