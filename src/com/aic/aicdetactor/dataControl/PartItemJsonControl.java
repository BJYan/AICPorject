package com.aic.aicdetactor.dataControl;

import com.aic.aicdetactor.data.PartItemJsonUp;

public class PartItemJsonControl extends PartItemJsonUp {
	//选择运行/停机/备用等后需生成的一个PartItem数据结构
	//private PartItemJsonUp mPartItemAfterItemDef =null;
	/**
	 * 选择运行/停机/备用后生成的PartItem对象
	 */
	public PartItemJsonUp genPartItemDataAfterItemDef(String Name){
		Check_Content=Name;
		Extra_Information="设备级";
		T_Item_Abnormal_Grade_Id=2;
		T_Item_Abnormal_Grade_Code="01";
		Item_Define=Item_Define;
		return this;
	}
}
