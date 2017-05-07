package com.ml;

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;  
public class PayNotifyRetryThread extends Thread {
	private static final Logger log = (Logger) LoggerFactory.getLogger(PayNotifyRetryThread.class);
	private static int []RETRY_TIME = {15,30,60,180,1800,1800,1800,3600};
	public void run() {  
		
        try{
        	int sleepTime = Integer.parseInt(CommonUtil.getProperty("SLEEP_TIME"));
        	while(true)
        	{
        	  try{
	        		for(int i=0;i<CheckPayNofify.mchOrderDataList.size();i++){
	        			PayNotifyData payNotifyData =new PayNotifyData();
	        			MchOrderData mchOrderStatusData =new MchOrderData();
	        			MchOrderData mchOrderData=CheckPayNofify.mchOrderDataList.get(i);
	        			//BeanUtils.copyProperties(mchOrderData,payNotifyData);
	        			//BeanUtils.copyProperties(mchOrderData,mchOrderStatusData);
	        			//回调数据对象
	        			payNotifyData.setOut_trade_no(mchOrderData.getOut_trade_no());
	        			payNotifyData.setTransaction_id(mchOrderData.getTransaction_id());
	        			payNotifyData.setTotal_fee(mchOrderData.getTotal_fee());
	        			payNotifyData.setBody(mchOrderData.getBody());
	        			payNotifyData.setAttach(mchOrderData.getAttach());
	        			payNotifyData.setTime_end(mchOrderData.getTime_end());
	        			payNotifyData.setStatus(mchOrderData.getStatus());
	        			payNotifyData.setSign(mchOrderData.getSign());
	        			//状态数据对象
	        			mchOrderStatusData.setTransaction_order_id(mchOrderData.getTransaction_order_id());
	        			mchOrderStatusData.setOut_trade_no(mchOrderData.getOut_trade_no());
	        			mchOrderStatusData.setTransaction_id(mchOrderData.getTransaction_id());
	        			mchOrderStatusData.setTotal_fee(mchOrderData.getTotal_fee());
	        			mchOrderStatusData.setBody(mchOrderData.getBody());
	        			mchOrderStatusData.setAttach(mchOrderData.getAttach());
								mchOrderStatusData.setTime_end(mchOrderData.getTime_end());
								mchOrderStatusData.setStatus(mchOrderData.getStatus());
								mchOrderStatusData.setSign(mchOrderData.getSign());
								mchOrderStatusData.setNotify_url(mchOrderData.getNotify_url());
								mchOrderStatusData.setFail_times(mchOrderData.getFail_times());
	        			//JSONObject getObj = JSONObject.fromObject(payNotifyData);
	        			String StrUrl =mchOrderData.getNotify_url();
	        			//没同步的做同步通知
	        			if(StrUrl!=null && !"".equals(StrUrl) && mchOrderData.getSynchronize_status()==CheckPayNofify.STATUS_FAIL){
	        				
	        				int failTimes=CheckPayNofify.mchOrderDataList.get(i).getFail_times();
	        				String startFailTime=CheckPayNofify.mchOrderDataList.get(i).getStart_fail_time();
	        				int retryInterval= DateUtil.getDiffTime(startFailTime, DateUtil.getTime())/1000;
	        				//判断是否到了时间间隔
	        				if(failTimes<RETRY_TIME.length &&  retryInterval>=RETRY_TIME[failTimes-1]){
		        				//String ret= CommonUtil.postURL(StrUrl, getObj.toString());
	        					String ret= CommonNotify.doPayNotify(mchOrderData, mchOrderData.getMch_id());
		        				if(CheckPayNofify.mchOrderDataList.size()>0){
			        				if("success".equals(ret)){
			        					CheckPayNofify.mchOrderDataList.get(i).setSynchronize_status(CheckPayNofify.STATUS_NO);
			        					CheckPayNofify.mchOrderDataList.get(i).setFail_retry_end(CheckPayNofify.STATUS_NO);
			        					//更新状态队列
					  						 if(CheckPayNofify.mchOrderDataStatusList==null){
					  							CheckPayNofify.mchOrderDataStatusList =new ArrayList<MchOrderData>();
											  }
					  						mchOrderStatusData.setSynchronize_status(CheckPayNofify.STATUS_NO);
			        					CheckPayNofify.mchOrderDataStatusList.add(mchOrderStatusData);
			        				} else {
			        					if(failTimes<RETRY_TIME.length-1){
				        					CheckPayNofify.mchOrderDataList.get(i).setSynchronize_status(CheckPayNofify.STATUS_FAIL);
				        					CheckPayNofify.mchOrderDataList.get(i).setFail_times(CheckPayNofify.mchOrderDataList.get(i).getFail_times()+1);
			        					} else{
				        					CheckPayNofify.mchOrderDataList.get(i).setSynchronize_status(CheckPayNofify.STATUS_FAIL);
				        					CheckPayNofify.mchOrderDataList.get(i).setFail_times(CheckPayNofify.mchOrderDataList.get(i).getFail_times()+1);
				        					CheckPayNofify.mchOrderDataList.get(i).setFail_retry_end(CheckPayNofify.STATUS_NO);
				        					//更新状态队列
						  						if(CheckPayNofify.mchOrderDataStatusList==null){
						  							CheckPayNofify.mchOrderDataStatusList =new ArrayList<MchOrderData>();
												  }
				        					mchOrderStatusData.setSynchronize_status(CheckPayNofify.STATUS_FAIL);
				        					CheckPayNofify.mchOrderDataStatusList.add(mchOrderStatusData);
			        					}
			        				}
		        				}
	        				}
	        			}
	        			
	        		}
	        		removeMchOrderDataList();
        	  }catch (Exception e) {
        			
        			log.error("同步消息通知失败：" +e.getMessage());
        		}
						//间隔指定时间之后从新检查
						Thread.sleep(sleepTime);
        	}
		
		} catch (Exception e) {
			
			log.error("同步消息通知失败：" +e.getMessage());
		}
		
	}
	public synchronized void removeMchOrderDataList() {
		//在队列中删除已同步数据
		for(int i=CheckPayNofify.mchOrderDataList.size()-1;i>=0;i--){
			MchOrderData mchOrderData=CheckPayNofify.mchOrderDataList.get(i);
			if(mchOrderData.getSynchronize_status()==CheckPayNofify.STATUS_FAIL&& mchOrderData.getFail_retry_end()==CheckPayNofify.STATUS_NO){
				CheckPayNofify.mchOrderDataList.remove(i);
			}
		}	
	}
}
