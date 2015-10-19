package com.aic.aicdetactor.bluetooth.analysis;

public class DataHead {
	public int[] head;
	public int sensorType;
	public int dataType;
	public int dataNum;
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
