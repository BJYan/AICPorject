package com.aic.aicdetactor.bluetooth.analysis;

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


	public DataHead(String str) {
		// TODO Auto-generated constructor stub
		head = new int[4];
		for(int i=0;i<8;i+=2){
			head[i/2] = Integer.valueOf(str.substring(i, i+2),16);
		}
		sensorType = Integer.valueOf(str.substring(8, 10),16);
		dataType =  Integer.valueOf(str.substring(10, 12),16);
		dataNum = Integer.valueOf(str.substring(12, 16),16);
		frequency = Integer.valueOf(str.substring(16, 20),16);
	}
}
