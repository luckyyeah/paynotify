package com.ml;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloopen.rest.sdk.CCPRestSDK;

public class PayNotify {
	public static final String ACCOUNT_SID = "aaf98f895350b68801535e2db0a61646";
	public static final String AUTH_TOKEN = "65c4340185684dd09651cc1421d9521f";
	public static final String REST_URL = "sandboxapp.cloopen.com";
	public static final String REST_PORT = "8883";
	public static final String APPID = "8aaf070854db9afc0154dc1d4980002d";
	public static final String MEDIA_NAME = "alarm.wav";
	public static final String TEMPLETE_ID_ACTIVITY = "140436";	
	public static final String TEMPLETE_ID_PAY = "140435";	
	private static final Logger log = (Logger) LoggerFactory.getLogger(PayNotify.class);

	public static void main(String[] args) {
		log.info("同步数据开始");
		try {
			 int  notifyThreadNum = Integer.parseInt(CommonUtil.getProperty("NOTIFY_THREAD_NUM"));
			//数据取得线程
			PayNotifyListThread payNotifyListThread =new PayNotifyListThread();
			payNotifyListThread.start();;
			//通知线程
			for(int i=0;i<notifyThreadNum;i++){
				PayNotifyThread payNotifyThread =new PayNotifyThread();
				payNotifyThread.start();			
			}
			PayNotifyRetryThread payNotifyRetryThread=new PayNotifyRetryThread();
			payNotifyRetryThread.start();
		} catch (Exception ex) {
			log.info(ex.toString());
		}
	}
}
