package com.ml;

import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;  
public class PayNotifyListThread extends Thread {
	private static final Logger log = (Logger) LoggerFactory.getLogger(PayNotifyListThread.class);
	public void run() {  
			   long i=0;
        try{
        	int sleepTime = Integer.parseInt(CommonUtil.getProperty("SLEEP_TIME"));
        	while(true)
        	{
        	  try{
        		  CheckPayNofify.getPayNotifyList();
        		  CheckPayNofify.updatetPayNotifyStatus();
						//间隔指定时间之后从新检查
						Thread.sleep(sleepTime);
						//log.info("取得数据开始:"+i++);
        	  }catch(Exception ex){
							  log.error("#ERROR# :获取支付通知信息记录失败！", ex); 
						}
        	}
		
		} catch (Exception e) {
			
			log.error("获取支付通知信息记录失败：" +e.getMessage());
		}
		
	}
}
