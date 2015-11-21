package com.aic.aicdetactor.bluetooth;


import com.aic.aicdetactor.bluetooth.analysis.CaiYangValues;
import com.aic.aicdetactor.util.SystemUtil;

public class BluetoothPrivateProxyAbortd {

	String Value;
	final static int MinLenth =10;
	private byte mDLCMDType=0;
	final String TAG="BluetoothPrivateProxy";
	public BluetoothPrivateProxyAbortd( byte downLoadCMDType,String strValue){
		Value=strValue;
		mDLCMDType = downLoadCMDType;
	}
	
	/**
	 * 验证接受到的数据是否是有效数值，通过CRC32进行重新校验对比校验码，相等即为有效，否则为无效数据。
	 * 注意，要对获取的数据去掉后8个字符进行校验。
	 * @return
	 */
	public int  isValidate(){
		int returnValue =-1;
		if(Value!=null && Value.length()>MinLenth){
			switch(mDLCMDType){
			case BluetoothConstast.CMD_Type_CaiJi:
			{
				if(Value.substring(0, 8).toLowerCase().equals("7d7d7d7d")){
					//CRC32校验
//					byte[]data2= new byte[mStrValue.length-4];
//					System.arraycopy(mStrValue,mStrValue.length-4,data2,0,mStrValue.length-4);
//					byte[]bytecrc32=SystemUtil.getCRC32(data2).getBytes();
//					if(bytecrc32.length>=4){
//						for(int i=0;i<bytecrc32.length;i++){
//							if(bytecrc32[i]!=mStrValue[mStrValue.length-4-i]){
//								return -2;
//							}
//						}
//					
//				}else{
//					return 1;
//				}
					if(Value.length() == (20+	getDataPointCount()*4 +(9+9+4*3)*2))
					{
						returnValue=0;
					}else{
						returnValue= Value.length();
					}
			}
			break;
			}
			case BluetoothConstast.CMD_Type_ReadSensorParams:
			{
				if(Value.substring(0, 6).toLowerCase().equals("7d14d2")){
					returnValue= 0;
				}else{
					returnValue= 1;
				}
			}
				break;
			}
		}
		
		return returnValue ;
	}
	/**
	 * 获取轴数
	 * @return
	 */
	public int getAXCount(){
		if(Value.length()<MinLenth){return 0;}
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
			return Integer.valueOf(Value.substring(8, 10));
			}else if(BluetoothConstast.CMD_Type_ReadSensorParams==mDLCMDType){
				String a = Value.substring(6, 8);
				int b= Integer.valueOf(a,16);
				return b;
			}
		
		return 0;
	}
	/**
	 * 获取采样点数
	 * @return
	 */
	public int getDataPointCount(){
		if(Value.length()<MinLenth){return 0;}
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
			String a= Value.substring(12, 16);
			
			int b= Integer.valueOf(a,16);
			return b;
			}
		return 0;
	}
	
	/**
	 * 获取采样频率
	 * @return
	 */
	public int getCaiYangFrequency(){
		if(Value.length()<MinLenth){return 0;}
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){
			return Integer.valueOf(Value.substring(16, 20),16);
			}
		return 0;
	}
	
	/**
	 * 获取波形数据
	 * @return
	 */
	public String getWaveData(){
		if(Value.length()<MinLenth){return null;}
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
		int count = getDataPointCount();
		String waveDataB = Value.substring(20,count*4+20);
		
		return waveDataB;
		}
		return null;
	}
	/**
	 * 获取温度数值
	 * @return
	 */
	public float getTemperatorValue(){
		if(Value.length()<MinLenth){return 0;}
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
		String tmp=Value.substring(Value.length()-12*2,Value.length()-8*2);
		float value =SystemUtil.ByteArrayToFloat(tmp.getBytes());
		return value;
		}
		return 0;
	}
	
	
	/**
	 * 获取电量数值
	 *  @return
	 */
    public float getChargeValue(){
    	if(Value.length()<MinLenth){return 0;}
    	if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
		String ChargeV=Value.substring(Value.length()-8*2,Value.length()-4*2);
		float value =SystemUtil.ByteArrayToFloat(ChargeV.getBytes());
		return value;
    	}
    	return 0;
	}
    
    /**
     * 获取数据的校验码，以String 类型的数据返回方便比较
     * @return
     */
    public String getCRC32(){
    	if(Value.length()<MinLenth){return null;}
		String StrCRC32=Value.substring(Value.length()-4*2,Value.length());
		return StrCRC32;
    }
    
    /**
     * 波形后面的两个数据
     * @param index  0 or 1
     * @return
     */
    public CaiYangValues getExternalValues(int index){
    	if(Value.length()<MinLenth){return null;}
    	CaiYangValues value=null;
    	if(index>1){return value;}
    	if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
    		int count = getDataPointCount();
    		value= new CaiYangValues(Value.substring(20+(count+index)*2,20+(count+9+index)*2));
    	}
    	return value;
    }
    
}
