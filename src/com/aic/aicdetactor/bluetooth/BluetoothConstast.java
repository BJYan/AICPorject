package com.aic.aicdetactor.bluetooth;

public class BluetoothConstast {

	//采集 加速度 速度 位移的下发命令
	public final static byte CMD_Type_CaiJi=(byte) 0xD1;
	public final static byte CMD_Type_ReadSensorParams=(byte) 0xD2;
	public final static byte CMD_Type_CaiJiZhuanSu=(byte) 0xD3;
	public final static byte CMD_Type_SetSensorName=(byte) 0xD4;
	public final static byte CMD_Type_SetSensorParams=(byte) 0xD5;
	public final static byte CMD_Type_GetTemper=(byte) 0xD6;
	public final static byte CMD_Type_GetCharge=(byte) 0xD7;
	//单轴
	public final static byte Sensor_AX_Type_One=0x01;
	//双轴
	public final static byte Sensor_AX_Type_Two=0x02;
	//三轴
	public final static byte Sensor_AX_Type_Thress=0x03;
	
	
	//
	public final static byte CMD_Sensor_Type_JiaSuDu=0x01;
	public final static byte CMD_Sensor_Type_SuDu=0x02;
	public final static byte CMD_Sensor_Type_WeiYi=0x03;
	//代表加速度、速度、位移都有，数据顺序也相同
	public final static byte CMD_Sensor_Type_All=0x04;
	
}
