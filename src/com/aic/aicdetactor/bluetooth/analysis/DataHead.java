package com.aic.aicdetactor.bluetooth.analysis;

import com.aic.aicdetactor.bluetooth.BluetoothConstast;

public class DataHead {
	public int[] head;
	/**
	 * 轴数
	 * 单轴 1，双轴2,三轴3
	 */
	public int sensorType;
	/**
	 * 数据类型
	 *  加速度1,速度2,位移3
	 */
	public int dataType;
	/**
	 * 采样点个数
	 */
	public int dataNum;
	/**
	 * 采样点频率
	 */
	public int frequency;


	public DataHead(String str,byte DLCMD) {
		// TODO Auto-generated constructor stub
		switch(DLCMD){
		case BluetoothConstast.CMD_Type_CaiJi:
		{
			head = new int[4];
			for(int i=0;i<8;i+=2){
				head[i/2] = Integer.valueOf(str.substring(i, i+2),16);
			}
			sensorType = Integer.valueOf(str.substring(8, 10),16);
			dataType =  Integer.valueOf(str.substring(10, 12),16);
			dataNum = Integer.valueOf(str.substring(12, 16),16);
			frequency = Integer.valueOf(str.substring(16, 20),16);
		}
		break;
		case BluetoothConstast.CMD_Type_ReadSensorParams:
		{
			head = new int[2];
			for(int i=0;i<4;i+=2){
				head[i/2] = Integer.valueOf(str.substring(i, i+2),16);
			}
			sensorType = Integer.valueOf(str.substring(6,8),16);
			dataType =  0;
			dataNum = 0;
			frequency = 0;
		}
		break;
		
		case BluetoothConstast.CMD_Type_GetTemper:
		case BluetoothConstast.CMD_Type_GetCharge:
		{
			head = new int[3];
			for(int i=0;i<6;i+=2){
				head[i/2] = Integer.valueOf(str.substring(i, i+2),16);
			}
			sensorType = 0;
			dataType =  0;
			dataNum = 0;
			frequency = 0;
		}
		break;
		}
	}
}
