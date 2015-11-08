package com.aic.aicdetactor.comm;

public class PartItemContact {

	private final static int START= 10;
	public final static int PARTITEM_CAMERA_RESULT =START;
	public final static int PARTITEM_NOTEPAD_RESULT =START+1;
	public final static int PARTITEM_SOUNDRECORD_RESULT =START+2;
	
	
	/**
	 * activity 与fragment 之间的数据协议，OnButtonDown中的第四个参数
	 */
	public final static int MEASURE_DATA=1;
	public final static int SAVE_DATA=2;
}
