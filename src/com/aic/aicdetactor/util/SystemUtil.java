package com.aic.aicdetactor.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.aic.aicdetactor.setting.Setting;

import android.util.Log;



public class  SystemUtil {

	static String TAG ="luotest";
	public  static String getSystemTime(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = df.format(new Date());
		Log.d(TAG, "getSystemTime time is " + str);
		return str;
	}
	public static String createGUID(){
		String str = null;
		UUID uuid = UUID.randomUUID();		
		str = uuid.toString();
		Log.d(TAG, "createGUID create a new GUID is  " + str);
		return str;
	}
	
	public static List<Map<String, Object>> reverseListData(List<Map<String, Object>> inList){
		
		if(inList == null) return null;
		List<Map<String, Object>> OutList = new ArrayList<Map<String, Object>>();
		for(int i = inList.size()-1;i>=0;i--){
			OutList.add(inList.get(i));
		}
		return OutList;
	}
	public static String getDataRootStoragePath(){
		
		Setting setting = new Setting();
	
		return setting.getData_Root_Director();
	}
	
	/**
	 * 根据文件类型，返回不同的文件路径
	 * @param fileType 0：拍照图片路径，1：录音文件路径，2：快速记录文件类型
	 * @return
	 */
	public static String getDataMediaStoragePath(int type){
		
		Setting setting = new Setting();
	
		return setting.getData_Media_Director(type);
	}
	
	public static boolean deleteFile(String filePath) {
	    File file = new File(filePath);
	        if (file.isFile() && file.exists()) {
	        return file.delete();
	        }
	        return false;
	    }
	
	public static boolean deleteDirectory(String filePath) {
	    boolean flag = false;
	        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
	        if (!filePath.endsWith(File.separator)) {
	            filePath = filePath + File.separator;
	        }
	        File dirFile = new File(filePath);
	        if (!dirFile.exists() || !dirFile.isDirectory()) {
	            return false;
	        }
	        flag = true;
	        File[] files = dirFile.listFiles();
	        //遍历删除文件夹下的所有文件(包括子目录)
	        for (int i = 0; i < files.length; i++) {
	            if (files[i].isFile()) {
	            //删除子文件
	                flag = deleteFile(files[i].getAbsolutePath());
	                if (!flag) break;
	            } else {
	            //删除子目录
	                flag = deleteDirectory(files[i].getAbsolutePath());
	                if (!flag) break;
	            }
	        }
	        if (!flag) return false;
	        //删除当前空目录
	        return dirFile.delete();
	    }

	    /**
	     *  根据路径删除指定的目录或文件，无论存在与否
	     *@param filePath  要删除的目录或文件
	     *@return 删除成功返回 true，否则返回 false。
	     */
	public static boolean DeleteFolder(String filePath) {
	    File file = new File(filePath);
	        if (!file.exists()) {
	            return false;
	        } else {
	            if (file.isFile()) {
	            // 为文件时调用删除文件方法
	                return deleteFile(filePath);
	            } else {
	            // 为目录时调用删除目录方法
	                return deleteDirectory(filePath);
	            }
	        }
	    }
	private static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	private static float ByteArrayToFloat(byte[] bytes) {
		int i = ((((bytes[0] & 0xff) << 8 | (bytes[1] & 0xff)) << 8) | (bytes[2] & 0xff)) << 8 | (bytes[3] & 0xff);
		return Float.intBitsToFloat(i);
	}
	
	public static float getTemperature(String s){
		byte[] bytes =hexStringToByteArray(s);
		return ByteArrayToFloat(bytes);
	}

}
