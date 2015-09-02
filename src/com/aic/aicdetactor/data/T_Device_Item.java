package com.aic.aicdetactor.data;

public class T_Device_Item {
	//根节点名
	public static String  RootNode_DeviceItem = "DeviceItem";
	
	//partItem根节点名
	public static String  RootNode_PartItem = "PartItem";
	
	//是否反向巡检
	public boolean mIsReverseCheck = false;

	/**
	 * 每个数组的成员变量
	 * @author Administrator
	 *
	 */
	public class Device_Array_Item{
		public String Name="";
		public String Code="";
		public String English_Name="";
		public String Model="";
		public String Chart_Number="";
		public String Manufacturer="";
		public String Serial_Number="";
		public String Material="";
		public String Vendor="";
		public String Grade="";
		public String Status="";
		public String Class="";
		public String Remarks="";
		public String Installation_Site="";
		public String Person_In_Charge="";
		public String Date_Of_Production="";
		public String Date_Of_Entering="";
		public String Date_Of_Start="";
		public String Processing_Size="";
		public String Capacity="";
		public String Rated_Power="";
		public String Energy_Consumption="";
		public String Precision="";
		public String Asset_Number="";
		public String Asset_Type="";
		public String Safety_Coefficient="";
		public String Price="";
		public String First_Maintenance="";
		public String Second_Maintenance="";
		public String Third_Maintenance="";
		public String Classification_Number="";
		public String Rated_RPM="";
		public String Rated_Current="";
		public String Rated_Voltage="";
		public String Start_Check_Datetime="";
		public String End_Check_Datetime="";		
		public String Total_Check_Time="";
		public String Item_Define="";
		public String Is_Omission_Check="";
		public String Is_Special_Inspection="";
		public String Is_Device_Checked="";
		public String Is_In_Place="";
		public String Is_RFID_Checked="";
		public String Data_Exist_Guid="";
	}
	
	/**
	 * Device Item 下的KEY Name
	 * @author Administrator
	 *
	 */
	public class Device_Array_Item_Const{
		public static final String Key_Name="Name";
		public static final String Key_Code="Code";
		public static final String Key_English_Name="English_Name";
		public static final String Key_Model="Model";
		public static final String Key_Chart_Number="Chart_Number";
		public static final String Key_Manufacturer="Manufacturer";
		public static final String Key_Serial_Number="Serial_Number";
		public static final String Key_Material="Material";
		public static final String Key_Vendor="Vendor";
		public static final String Key_Grade="Grade";
		public static final String Key_Status="Status";
		public static final String Key_Class="Class";
		public static final String Key_Remarks="Remarks";
		public static final String Key_Installation_Site="Installation_Site";
		public static final String Key_Person_In_Charge="Person_In_Charge";
		public static final String Key_Date_Of_Production="Date_Of_Production";
		public static final String Key_Date_Of_Entering="Date_Of_Entering";
		public static final String Key_Date_Of_Start="Date_Of_Start";
		public static final String Key_Processing_Size="Processing_Size";
		public static final String Key_Capacity="Capacity";
		public static final String Key_Rated_Power="Rated_Power";
		public static final String Key_Energy_Consumption="Energy_Consumption";
		public static final String Key_Precision="Precision";
		public static final String Key_Asset_Number="Asset_Number";
		public static final String Key_Asset_Type="Asset_Type";
		public static final String Key_Safety_Coefficient="Safety_Coefficient";
		public static final String Key_Price="Price";
		public static final String Key_First_Maintenance="First_Maintenance";
		public static final String Key_Second_Maintenance="Second_Maintenance";
		public static final String Key_Third_Maintenance="Third_Maintenance";
		public static final String Key_Classification_Number="Classification_Number";
		public static final String Key_Rated_RPM="Rated_RPM";
		public static final String Key_Rated_Current="Rated_Current";
		public static final String Key_Rated_Voltage="Rated_Voltage";
		public static final String Key_Start_Check_Datetime="Start_Check_Datetime";
		public static final String Key_End_Check_Datetime="End_Check_Datetime";		
		public static final String Key_Total_Check_Time="Total_Check_Time";
		public static final String Key_Item_Define="Item_Define";
		public static final String Key_Is_Omission_Check="Is_Omission_Check";
		public static final String Key_Is_Special_Inspection="Is_Special_Inspection";
		public static final String Key_Is_Device_Checked="Is_Device_Checked";
		public static final String Key_Is_In_Place="Is_In_Place";
		public static final String Key_Is_RFID_Checked="Is_RFID_Checked";
		public static final String Key_Data_Exist_Guid="Data_Exist_Guid";
		public static final String Key_PartItem="PartItem";
	}
	
	/**
	 * PartItem
	 * @author Administrator
	 *
	 */
	public class PartItem_Arrary_Item{
		public String Fast_Record_Item_Name="";
		public String PartItemData="";
	}
	/**
	 * partItem 对应的KEY Name
	 * @author Administrator
	 *
	 */
	public class PartItem_Arrary_Item_Const{
		public static final String Key_Fast_Record_Item_Name="Fast_Record_Item_Name";
		public static final String Key_PartItemData="PartItemData";
	}
}
