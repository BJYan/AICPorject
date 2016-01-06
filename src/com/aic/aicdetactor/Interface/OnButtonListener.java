package com.aic.aicdetactor.Interface;

import com.aic.aicdetactor.adapter.PartItemListAdapter;
import com.aic.aicdetactor.comm.ParamsPartItemFragment;

public interface OnButtonListener {
	public static int  ModifyData=7;
	public static int  PictureDataType=ModifyData+1;
	public static int  AudioDataType=ModifyData+2;
	public static int  WaveDataType=ModifyData+4;
	
	public static int  PictureDataId=PictureDataType+1;
	public static int  AudioDataId=AudioDataType+1;
	public static int  WaveDataId=WaveDataType+1;
	/**
	 * 保存 0,新增 >0,1 新增图片,2新增音频,3 振动波形数据 
	 * @param buttonId
	 * @param object
	 * @param Value ,如果 buttonId>0，表示是新增数据的文件路径
	 * @param measureOrSave 1 measure,2 save data
	 */
	void OnButtonDown(int buttonId,PartItemListAdapter object,String Value,int measureOrSave,int CaiYangDian,int CaiyangPinLv);
	/**
	 * 新增加媒体数据 例如 图片，音频 数据partItemData	 
	 */
	public void addNewMediaPartItem(ParamsPartItemFragment params,PartItemListAdapter object);
	
	public boolean canSave();

}
