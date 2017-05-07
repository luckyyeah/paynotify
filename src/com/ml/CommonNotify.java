package com.ml;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class CommonNotify {
	static Logger logger = Logger.getLogger(CommonNotify.class);
	public static String doPayNotify(MchOrderData userPayNotifyData,String mch_id) throws Exception{

		String StrUrl = null;
		String mchSecretKey = userPayNotifyData.getMch_secret_key();
		if (userPayNotifyData != null) {
			StrUrl = userPayNotifyData.getNotify_url();
			String signCentent = userPayNotifyData.getOut_trade_no()+userPayNotifyData.getTransaction_id()+userPayNotifyData.getTime_end()+mch_id;
			String sign= MD5.sign(signCentent, mchSecretKey, "utf-8");
			userPayNotifyData.setSign(sign);
		}
		//JSONObject getObj = JSONObject.fromObject(userPayNotifyData);

		Map<String,String> mapDataContent =getMapDataContent(userPayNotifyData);
		// 做同步通知
		if (StrUrl != null && !"".equals(StrUrl)) {
			String ret = CommonUtil.doRequest(StrUrl, mapDataContent);
			//String ret = CommonUtil.sendRequest(StrUrl, dataContent);
			logger.info("notify mchSecretKey=" + mchSecretKey);
			logger.info("notify user_out_trade_no=" + userPayNotifyData.getOut_trade_no());
			logger.info("通知内容：" + JSONObject.fromObject(userPayNotifyData).toString());
			if ("success".equals(ret)) {
				logger.info("通知内容：success" + JSONObject.fromObject(userPayNotifyData).toString());
			}
			return ret;
		}
		return "";
	}
	private static Map getMapDataContent(MchOrderData userPayNotifyData) throws Exception{
		Map<String,String> mapContent =new HashMap<String,String>();
		mapContent.put("out_trade_no",userPayNotifyData.getOut_trade_no());
		mapContent.put("transaction_id",userPayNotifyData.getTransaction_id());
		mapContent.put("total_fee",userPayNotifyData.getTotal_fee());
		mapContent.put("time_end",userPayNotifyData.getTime_end());
		mapContent.put("status",userPayNotifyData.getStatus());
		mapContent.put("attach",userPayNotifyData.getAttach());
		mapContent.put("sign",userPayNotifyData.getSign());
		mapContent.put("pay_type",userPayNotifyData.getPay_type());
		return mapContent;
	}
}
