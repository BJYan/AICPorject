package com.aic.aicdetactor.comm;
/**
 * 定义activity 间传递的参数变量信息
 * @author Administrator
 *
 */
public class CommonDef {
	public static String TAG = "luotest";	
	public static String ROUTE_CLASS_NAME = "route_Class";	
	public static String GUID = "guid";
	/*
	 * 巡检文件存储位置目录
	 */
	public static String PATH_DIRECTOR = "datapathDirector";

	//checkItemData 以*隔开的数据编号
	public class partItemData_Index{
    //轮次
    public  final static int PARTITEM_TURN_NAME =0;
    //部件名
    public  final static int PARTITEM_UNIT_NAME =1;
    //巡检项目名
    public  final static int PARTITEM_CHECKPOINT_NAME =2;
    //巡检数据种类
    public  final static int PARTITEM_DATA_TYPE_NAME =3;
  //测量单位
    public  final static int PARTITEM_MEASUREMENT_UNIT_NAME =4;
    //状态标识
    public  final static int PARTITEM_STATE_MARK_NAME =5;
    
    //上限数值
    public  final static int PARTITEM_MAX_VALUE_NAME =6;
    
    //中限数值
    public  final static int PARTITEM_MIDDLE_VALUE_NAME =7;
    
    //下限数值
    public  final static int PARTITEM_MIN_VALUE_NAME =8;
    
    //额外信息
    //测量结果值、用户录入或选择的字符串、照片或振动波形文件名
    public  final static int PARTITEM_ADDITIONAL_INFO_NAME =9;    
  
    //巡检时间
    public  final static int PARTITEM_CHECKED_TIME =10;
    //巡检最终结果，正常还是异常
    public  final int PARTITEM_LAST_RESULT =11;
    
	}
    
	// 以下是巡检项的数据种类
	public class checkUnit_Type{
	
	/**
	 * =”00000002” 表示测量温度 =“00000003”
	 * 表示记录项，用户即可从上位机事先编好的多个选项里选择一项，也可编辑一些新的信息，项与项之间用
	 * “/”隔开。另外每项字符串末尾有额外“0”或“1”单字节控制信息
	 * ，“0”代表正常，“1”代表“异常”，如：“正常0/微亏1/严亏1”，巡检仪界面上只会显示
	 * “正常/微亏/严亏”，如用户选择了“正常”，上传的巡检项末尾会添加
	 * “0”，表示设备正常，如选择了“微亏”或“严亏”，上传的巡检项末尾会添加“1”，表示设备异常。 =“00000004” 表示测量加速度
	 * =“00000005” 表示测量速度 =“00000006” 表示测量位移 =“00000007” 表示测量转速 =“00000008”
	 * 表示预设状况项
	 * ，用户即从上位机事先编好的多个选项里选择多项，也可编辑。如从编辑好的项中选择或编辑选择项，上传的巡检项末尾会添加“1”，表示异常；如用户输入
	 * “正常”字符串，上传的巡检项末尾会添加“0”，表示正常。 =“00000009” 表示图片 =“00000010” 表示振动矢量波形
	 */

	// 温度
	public static final int TEMPERATURE = 2;
	// 记录
	public static final int RECORD = 3;
	// 加速度
	public static final int ACCELERATION = 4;
	// 速度
	public static final int SPEED = 5;
	// 位移
	public static final int DISPLACEMENT = 6;
	// 转速
	public static final int ROTATIONAL_SPEED = 7;
	// 预设状况项
	public static final int DEFAULT_CONDITION = 8;
	// 图片
	public static final int PICTURE = 9;
	// 振动矢量波形
	// Vibration vector wave
	public static final int VIBRATION_VECTOR_WAVE = 10;
	}
	/**
	 * 
	 * @author Administrator
	 *
	 */
	
	public class turn_info {
		public static final String JSON_INDEX = "1";
	}

	public class worker_info {
		public static final String JSON_INDEX = "2";
	}

	public class route_info {
		public static final String NAME = "Route_Name";
		public static final String INDEX = "Route_Index";
		public static final String LISTVIEW_ITEM_INDEX = "Route_ListView_ItemIndex";
		public static final String DEADLINE = "Route_DeadLine";
		public static final String STATUS = "Route_Check_Status";
		public static final String PROGRESS = "Route_Progress";
		public static final String JSON_INDEX = "7";

	}

	public class station_info {
		public static final String NAME = "Station_Name";
		public static final String INDEX = "Station_Index";
		public static final String LISTVIEW_ITEM_INDEX = "Station_ListView_Item";
		public static final String DEADLINE = "Station_DeadLine";
		public static final String STATUS = "Station_Check_Status";
		public static final String PROGRESS = "Station_Progress";
		public static final String JSON_INDEX = "4";

	}
	
	public class organization_info{
		public static final String JSON_INDEX = "3";
	}

	public class device_info {
		public static final String NAME = "Device_Name";
		public static final String INDEX = "Device_Index";
		public static final String LISTVIEW_ITEM_INDEX = "Device_ListView_Item";
		public static final String DEADLINE = "Device_DeadLine";
		public static final String STATUS = "Device_Check_Status";
		public static final String PROGRESS = "Device_Progress";
	}

	public class check_unit_info {
		public static final String NAME = "CheckUnit_Name";
		public static final String INDEX = "CheckUnit_Index";
		public static final String LISTVIEW_ITEM_INDEX = "CheckUnit_ListView_Item";
		public static final String DEADLINE = "CheckUnit_DeadLine";
		public static final String STATUS = "CheckUnit_Check_Status";
		public static final String PROGRESS = "CheckUnit_Progress";
	}

	public class check_item_info {
		public static final String NAME = "CheckItem_Name";
		public static final String UNIT_NAME = "CheckUnit_Name";
		public static final String DATA_TYPE = "CheckItem_Type";
		public static final String VALUE = "CheckItem_Value";
		public static final String INDEX = "CheckItem_Index";
		public static final String LISTVIEW_ITEM_INDEX = "CheckItem_ListView_Item";
		
		public static final String STATUS = "CheckItem_Check_Status";
		public static final String PROGRESS = "CheckItem_Progress";
		public static final String IS_REVERSE_CHECKING = "is_reverse_checking";
		public static final String DEADLINE = "CheckItem_DeadLine";
		public static final String ISCHECKED = "IsChecked";
		public static final String CHECKED_RESULT = "Value";
		public static final String ITEM_COUNTS= "counts";
	}
	
	public static final int  FILE_TYPE_PICTRUE  =0;
	public static final int  FILE_TYPE_AUDIO  =1;
	public static final int  FILE_TYPE_TEXTRECORD  =2;
	public static final int  FILE_TYPE_WORKER  =3;

}
