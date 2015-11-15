package com.aic.aicdetactor.bluetooth;

import java.lang.reflect.Array;

import com.aic.aicdetactor.util.SystemUtil;

public class BluetoothPrivateProxy {

	private byte[]  mStrValue;;
	private byte mDLCMDType=0;
	public BluetoothPrivateProxy( byte downLoadCMDType,byte[] strValue){
		mStrValue=strValue;
		mDLCMDType = downLoadCMDType;
	}
	
	public int  isValidate(){
		if(mStrValue!=null && mStrValue.length>10){
			switch(mDLCMDType){
			case BluetoothConstast.CMD_Type_CaiJi:
			{
				if(mStrValue[0]==0x7d
						&&mStrValue[1]==0x7d
						&&mStrValue[2]==0x7d
						&&mStrValue[3]==0x7d
						){
					//CRC32校验
					byte[]data2= new byte[mStrValue.length-4];
					System.arraycopy(mStrValue,mStrValue.length-4,data2,0,mStrValue.length-4);
					byte[]bytecrc32=SystemUtil.getCRC32(data2).getBytes();
					if(bytecrc32.length>=4){
						for(int i=0;i<bytecrc32.length;i++){
							if(bytecrc32[i]!=mStrValue[mStrValue.length-4-i]){
								return -2;
							}
						}
					
				}else{
					return 1;
				}
			}
			break;
			}
			}
			return 0;
		}else{
			return -1;
		}
	}
	
	public int getAXCount(){
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){		return mStrValue[4];}
		
		return 0;
	}
	
	public int getDataPointCount(){
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	return (mStrValue[6]<<8)|mStrValue[7];}
		return 0;
	}
	
	public int getCaiYangPointCount(){
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	 return (mStrValue[8]<<8)|mStrValue[9];}
		return 0;
	}
	
	/**
	 * 获取波形数据
	 * @return
	 */
	public byte[]getWaveData(){
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
		int count = getDataPointCount();
		byte[]data=new byte[count];
		System.arraycopy(mStrValue,mStrValue.length-4,data,10,mStrValue.length-4);
		return data;
		}
		return null;
	}
	/**
	 * 获取温度数值
	 * @return
	 */
	public float getTemperatorValue(){
		if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
		byte[]tbyte=new byte[4];
		tbyte[0]=mStrValue[mStrValue.length-12];
		tbyte[1]=mStrValue[mStrValue.length-11];
		tbyte[2]=mStrValue[mStrValue.length-10];
		tbyte[3]=mStrValue[mStrValue.length-9];
		float value =SystemUtil.ByteArrayToFloat(tbyte);
		return value;
		}
		return 0;
	}
	
	/**
	 * 获取电量数值
	 *  @return
	 */
    public float getChargeValue(){
    	if(BluetoothConstast.CMD_Type_CaiJi==mDLCMDType){	
    	byte[]tbyte=new byte[4];
		tbyte[0]=mStrValue[mStrValue.length-8];
		tbyte[1]=mStrValue[mStrValue.length-7];
		tbyte[2]=mStrValue[mStrValue.length-6];
		tbyte[3]=mStrValue[mStrValue.length-5];
		float value =SystemUtil.ByteArrayToFloat(tbyte);
		return value;
    	}
    	return 0;
	}
    
    /**
     * 获取数据的校验码，以String 类型的数据返回方便比较
     * @return
     */
    public String getCRC32(){
    	byte[]tbyte=new byte[4];
		tbyte[0]=mStrValue[mStrValue.length-4];
		tbyte[1]=mStrValue[mStrValue.length-3];
		tbyte[2]=mStrValue[mStrValue.length-4];
		tbyte[3]=mStrValue[mStrValue.length-1];
		return tbyte.toString();
    }
}
