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
   
    //巡检项目名
    public  final static int PARTITEM_CHECKPOINT_NAME =0;
    //巡检项编号
    public  final static int PARTITEM_CODE =1;
    //数据类型ID
    public  final static int PARTITEM_DATA_TYPE_ID =2;
    //巡检数据种类
    public  final static int PARTITEM_DATA_TYPE =3;
  //测量单位
    public  final static int PARTITEM_MEASUREMENT_UNIT =4;
    //启停状态标识控制码
    public  final static int PARTITEM_START_STOP_STATUS_FLAG =5;
    
    //上限数值
    public  final static int PARTITEM_MAX_VALUE =6;
    
    //中限数值
    public  final static int PARTITEM_MIDDLE_VALUE =7;
    
    //下限数值
    public  final static int PARTITEM_MIN_VALUE =8;
    //发射率
    public  final static int PARTITEM_RF_RATE =9;
    //提示标志
    public  final static int PARTITEM_TIPS_FLAG =10;
    //轴数
    public  final static int PARTITEM_RELAX_COUNT =11;
    //检查方法，格式：目视/手摸/…,用法选几项(始终为空，APP不用处理)
    public  final static int PARTITEM_CHECK_METHOD =12;
   
    /**额外信息
     * 不为空。有两个作用：
	1. 巡检完后的结果值
	  温度、录入、加速度（有效值）、速度（有效值）、位移（有效值）、转速、观察项里的备注、选择”录入项“里某一项、选择“预设状况”里的一项或多项。	
	2.图片，音频，振动波形的文件名也放在此项。
     */
    public  final static int PARTITEM_ADDITIONAL_INFO =13;
    //维修状态
    public  final static int PARTITEM_FIX_STATUS =14; 
    
    //第十八项之后 ，是PartItemData上传数据时添加的项目
    //启停状态 新添加
    public  final static int PARTITEM_ADD_ITEMDEF_18 =18; 
    //开始巡检时间 新添加
    public  final static int PARTITEM_ADD_START_DATE_19 =19; 
    //结束巡检时间  新添加
    public  final static int PARTITEM_ADD_END_DATE_20 =20; 
    //总耗时	添加	整个巡检项耗时，单位S  新添加
    public  final static int PARTITEM_ADD_TIME_DIFF_21 =21; 
   
    
	}
    
	// 以下是巡检项的数据种类
	public class checkUnit_Type{
	
	/**
	两字节，不能空，总共有12种类型，分别为：
		="00", 表示温度。		
		="01", 录入项， 		
		="02", 测量，只能输入数字，小数点，正负号，如压力、流量等。		
		="03"，表示测量加速度		
		="04"，表示测量速度		
		="05"，表示测量位移		
		="06"，表示测量转速		
		="07"，表示预设状况项， 		
		="08"，表示图片		
		="09"，表示音频		
		="10"，表示观察
		在APP端自动启动照相、音频、备注。		
		="11",  振动波形		
		注：8,9,11类型在下载的JSON格式里不存在
	 */

	// 温度
	public static final int TEMPERATURE = 0;
	// 录入项
	public static final int RECORD = 1;
	// 测量，只能输入数字，小数点，正负号，如压力、流量等。	
	public static final int MEASUREMENT = 2;	
	// 加速度
	public static final int ACCELERATION = 3;
	// 速度
	public static final int SPEED = 4;
	// 位移
	public static final int DISPLACEMENT = 5;
	// 转速
	public static final int ROTATIONAL_SPEED = 6;
	// 预设状况项
	public static final int DEFAULT_CONDITION = 7;
	// 图片
	public static final int PICTURE = 8;
	// 音频
	public static final int AUDIO = 9;
	// 观察
	public static final int OBSERVER = 10;
	// 振动矢量波形
	// Vibration vector wave
	public static final int VIBRATION_VECTOR_WAVE = 11;
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
		//public static final String UNIT_NAME = "CheckUnit_Name";
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
	
	public class PartItemData_Shered_info {
		public static final String Time = "shered_Time";
		public static final String Content = "Shered_Content";
	}
	
	
	public class Temporary_Check_info {
		public static final String type = "type";
		public static final String past_time = "past_tiem";
		public static final String receive_date = "receive_date";
		public static final String title = "title";
	}
	
	
	
	public static final int  FILE_TYPE_PICTRUE  =0;
	public static final int  FILE_TYPE_AUDIO  =1;
	public static final int  FILE_TYPE_TEXTRECORD  =2;
	public static final int  FILE_TYPE_WORKER  =3;
	public static final String AUDIO_PATH= "audioPath";
	public static final String TEXT_RECORD_PATH= "textPath";
}
