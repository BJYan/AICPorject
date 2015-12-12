package com.aic.aicdetactor.comm;

public class ParamsPartItemFragment {
	
	/**
	 * 新增加媒体数据 例如 图片，音频 数据partItemData
	 * @param typecode
	 * @param SaveLab  uuid,如果是三轴的话，表示是同一组的数据，三轴振动还需要SaveLab找到其它两轴数据，
	 * @param RecordLab uuid, 数据对应关系中UUID
	 * @param validValue
	 * @param isNormal
	 * @param abNormalCode  如果 typecode ==11 即波形数据时采用，否则填写为null即可
	 */
public	int TypeCode;
public String SaveLab;
public String RecordLab;
public float ValidValue;
public String Path;
public boolean IsNormal;
public String AbnormalCode;
//二进制数据
public Object object;
}
