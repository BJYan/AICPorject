package com.aic.aicdetactor.comm;
/**
 * 定义activity 间传递的参数变量信息
 * @author Administrator
 *
 */
public  class  CommonDef {
public static String TAG = "luotest";
	/*
	 * 站点索引
	 */	
public static String STATION_INDEX ="stationIndex";
/*
 * 巡检计划名
 */
public static String PLANNAME="planName";

/* 巡检路线名 */
public static String ROUTENAME="routeName";
/*
 * 站点名
 */
public static String STATIONNAME="stationName";
/*
 * 设备名
 */
public static String DEVICENAME ="deviceName";
/*
 * 设备索引
 */
public static String DEVICE_INDEX = "deviceIndex";
/*
 * ListView Item Index
 */
public static String LISTITEM_INDEX = "listIndex";
/*
 * 检查项名
 */
public static String CHECKNAME = "checkName";
/*
 * 计划巡检 或临时巡检的标签
 */
public static String ONE_CATALOG="oneCatalog";
/*
 * root note name
 */
public static String ROOTNAME= "rootName";
/*
 * root Index
 */
public static String ROUTE_INDEX = "routeIndex";
/*
 * partItemIndex
 */
public static String PARTITEM_INDEX = "partItemIndex";

/*
 * 是否反向巡检
 */
public static String ISREVERSECHECKING = "isReverseChecking";

/*
/*GUID 用于route文件的存储文件名字
 * 
 */
public static String GUID="guid";

/*
 * 巡检文件存储位置目录
 */
public static String PATH_DIRECTOR="datapathDirector";

//以下是巡检项的数据种类
/**
 * =”00000002” 表示测量温度
=“00000003” 表示记录项，用户即可从上位机事先编好的多个选项里选择一项，也可编辑一些新的信息，项与项之间用“/”隔开。另外每项字符串末尾有额外“0”或“1”单字节控制信息，“0”代表正常，“1”代表“异常”，如：“正常0/微亏1/严亏1”，巡检仪界面上只会显示“正常/微亏/严亏”，如用户选择了“正常”，上传的巡检项末尾会添加“0”，表示设备正常，如选择了“微亏”或“严亏”，上传的巡检项末尾会添加“1”，表示设备异常。
=“00000004” 表示测量加速度
=“00000005” 表示测量速度
=“00000006” 表示测量位移
=“00000007” 表示测量转速
=“00000008” 表示预设状况项，用户即从上位机事先编好的多个选项里选择多项，也可编辑。如从编辑好的项中选择或编辑选择项，上传的巡检项末尾会添加“1”，表示异常；如用户输入 “正常”字符串，上传的巡检项末尾会添加“0”，表示正常。
=“00000009” 表示图片
=“00000010” 表示振动矢量波形
 */

//温度
public static final int TEMPERATURE =2;
//记录
public static final int RECORD =3;
//加速度
public static final int ACCELERATION = 4;
//速度
public static final int SPEED = 5;	
//位移
public static final int DISPLACEMENT =6;
//转速
public static final int ROTATIONAL_SPEED =7;
//预设状况项
public static final int DEFAULT_CONDITION = 8;
//图片
public static final int PICTURE = 9;
//振动矢量波形
//Vibration vector wave
public static final int VIBRATION_VECTOR_WAVE =10;
}
