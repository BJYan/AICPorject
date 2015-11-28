package com.aic.aicdetactor.adapter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.BlueToothRenameActivity;
import com.aic.aicdetactor.bluetooth.BluetoothLeControl;
import com.aic.aicdetactor.view.BluetoothViewHolder;
import com.aic.aicdetactor.view.GroupViewHolder;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class BlueToothBindDevListAdapter extends BaseExpandableListAdapter {
	Context context;
	private LayoutInflater mInflater;
	List<BluetoothDevice> bondedDevices;
    Handler mHandler;
    BluetoothLeControl mBTControl;
	
	public BlueToothBindDevListAdapter(Context context, List<BluetoothDevice> bondedDevices,Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.bondedDevices = bondedDevices;
		mHandler= handler;
		mBTControl = BluetoothLeControl.getInstance(context);
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		if(arg3==null) arg3 = mInflater.inflate(R.layout.bluetooth_binded_devlist_child_item, null);
		
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return bondedDevices.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return bondedDevices.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	//
	boolean bBTConnected=false;
	@Override
	public View getGroupView(final int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
	 final	int BTIndex = arg0;
		BluetoothViewHolder viewHolder=null;
		if(arg2==null) {
			arg2 = mInflater.inflate(R.layout.bluetooth_binded_devlist_group_item, null);
			viewHolder = new BluetoothViewHolder();
			viewHolder.image = (ImageView) arg2.findViewById(R.id.bluetooth_bind_dev_arrow);
			viewHolder.BTSwitch = (Switch) arg2.findViewById(R.id.bluetooth_device_status);
			viewHolder.NameText = (TextView) arg2.findViewById(R.id.bluetooth_device_name);			
			viewHolder.MacAddr = (TextView) arg2.findViewById(R.id.bluetooth_device_mac);
			arg2.setTag(viewHolder);

		}else{
			viewHolder=(BluetoothViewHolder) arg2.getTag();
			
		}
		viewHolder.index=arg0;
		viewHolder.image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("BluetoothDev", bondedDevices.get(arg0));
				intent.setClass(context, BlueToothRenameActivity.class);
				context.startActivity(intent);
			}
		});
		
		viewHolder.BTSwitch.setChecked(viewHolder.bconnected);
		viewHolder.BTSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub		
				Message msg = mHandler.obtainMessage(BluetoothLeControl.Message_Connected_BLE_Address);
				if(arg1){
					mBTControl.setParamates(mHandler);
					String Address = bondedDevices.get(BTIndex).getAddress();
				//	Address="B0:B4:48:D9:AF:7D";
					mBTControl.Connection(Address);	
					msg.arg1=BluetoothLeControl.Message_Connect_Status_Connected;
					msg.obj=Address;
					mHandler.sendMessage(msg);
	            	bBTConnected=true;
				}else{
					mBTControl.disconnection();
					bBTConnected=false;
					msg.arg1=BluetoothLeControl.Message_Connect_Status_DisConnected;
					msg.obj="";
					mHandler.sendMessage(msg);
				}
			}
			
		});
		
		viewHolder.MacAddr.setText(bondedDevices.get(arg0).getAddress());
		viewHolder.NameText.setText(bondedDevices.get(arg0).getName());
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
		return false;
	}

	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	public void  sendCommmand2BLE(byte[]cmdByte){
		if(cmdByte==null){return ;}
		mBTControl.Communication2Bluetooth(mBTControl.getSupportedGattServices(),cmdByte);
	}
	
}
