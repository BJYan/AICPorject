package com.aic.aicdetactor.setting;

import java.io.File;

import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.util.SystemUtil;

import android.os.Environment;

/**
 * 本文件主要是针对软件app的相关设置属性
 * 数据分为两类，
 * 第一类是从服务器上获取的计划巡检数据
 * 第二类数据是本地生成的数据，例如 拍照、录音、快速记录等用户生成的数据相关设置。
 * @author Administrator
 *
 */
public class Setting {

	/*
	 * 相关文件存储的默认根目录
	 */
	private String mDataDirector = null;

	/**
	 * 根目录
	 */
	public  static final String RootPath="/AIC/";
	/**
	 * 从服务器上下载的日常巡检文件路径
	 */
	public  static final String OriginalJson="OriginalJson/";
	/**
	 * 日常巡检后的文件存储位置
	 */
	public  static final String UploadJson="UploadJson/";
		
	/**
	 * 巡检过程中产生的振动 音频、图片 数据文件存储位置 
	 */
	public  static final String Extral="Extral/";
	
	public Setting() {
		mDataDirector = Environment.getExternalStorageDirectory() + RootPath;

		
		//mDataDirector = "/sdcard" + "/AIC/";
		File destDir = new File(mDataDirector);
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}

		/**
		 * 存放巡检的数据文件路径
		 */
		destDir = new File(mDataDirector + UploadJson);
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}
		/**
		 * 存放从服务器上获取的原始巡检数据 
		 */
		destDir = new File(mDataDirector + OriginalJson);
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}
		
		destDir = new File(mDataDirector + Extral);
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}

//		destDir = new File(mDataDirector + Audio);
//		if (!destDir.exists()) {
//			destDir.mkdirs();
//			destDir = null;
//		}
//
//		destDir = new File(mDataDirector + Image);
//		if (!destDir.exists()) {
//			destDir.mkdirs();
//			destDir = null;
//		}
//
//		destDir = new File(mDataDirector + Text);
//		if (!destDir.exists()) {
//			destDir.mkdirs();
//			destDir = null;
//		}
		
	}
//	public void setData_Root_Director(String director){
//		if(director == null){
//			return;
//		}
//		mDataDirector = director;
//	}
	
	public String getData_Root_Director(){
		return mDataDirector;
	}
	
	/**
	 * 根据文件类型，返回不同的文件路径
	 * @param fileType 0：拍照图片路径，1：录音文件路径，2：快速记录文件类型
	 * @return
	 */
	public String getData_Media_Director(int fileType){
		String path = null;
		switch(fileType){
		case CommonDef.FILE_TYPE_PICTRUE:
			path = mDataDirector +Extral;
			break;
		case CommonDef.FILE_TYPE_AUDIO:
			path = mDataDirector +Extral;
			break;
		case CommonDef.FILE_TYPE_TEXTRECORD:
			path = mDataDirector +Extral;
			break;
		case CommonDef.FILE_TYPE_OriginaJson:
			path = mDataDirector +OriginalJson;
			break;
		case CommonDef.FILE_TYPE_UploadJson:
			path = mDataDirector +UploadJson;
			break;
		default :
			path = mDataDirector;
			break;
		}
		
		File destDir = new File(path);
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}
		
		return path;
	}
	
	public void clearMediaDirector(int fileType){
		String path = null;
		switch(fileType){
		case CommonDef.FILE_TYPE_PICTRUE:
			path = mDataDirector +Extral;
			break;
		case CommonDef.FILE_TYPE_AUDIO:
			path = mDataDirector +Extral;
			break;
		case CommonDef.FILE_TYPE_TEXTRECORD:
			path = mDataDirector +Extral;
			break;
		case CommonDef.FILE_TYPE_OriginaJson:
			path = mDataDirector +OriginalJson;
			break;
		case CommonDef.FILE_TYPE_UploadJson:
			path = mDataDirector +UploadJson;
			break;
		default :
			path = mDataDirector;
			break;
		}
		SystemUtil.DeleteFolder(path);
	}
	
	public static String getUpLoadJsonPath(){
		String path = Environment.getExternalStorageDirectory() + RootPath;
		return path+UploadJson;
	}
	
	public static String getExtralDataPath(){
		String path = Environment.getExternalStorageDirectory() + RootPath;
		return path+Extral;
	}
	
	
}
