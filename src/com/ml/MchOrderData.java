package com.ml;

public class MchOrderData {
	String   transaction_order_id;//平台使用的订单号
	String   out_trade_no;//商户订单号
	String   transaction_id;//平台订单号（我们）
	String   total_fee;//交易金额
	String   body;//商品描述
	String   attach;//透传参数
	String   sign;//签名
	String   time_end;//支付完成时间
	String   status;//订单状态
	int    synchronize_status;//同步状态
	String   mch_id;//商户号(我们）
	String   notify_url;//通知地址
	String   mch_secret_key;//商户秘钥
	String   pay_type;//支付类型	
	int fail_times;//失败次数
	String start_fail_time;//失败开始时间
	int fail_retry_end;
	public String getTransaction_order_id() {
		return transaction_order_id;
	}
	public void setTransaction_order_id(String transaction_order_id) {
		this.transaction_order_id = transaction_order_id;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTime_end() {
		return time_end;
	}
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getSynchronize_status() {
		return synchronize_status;
	}
	public void setSynchronize_status(int synchronize_status) {
		this.synchronize_status = synchronize_status;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public int getFail_times() {
		return fail_times;
	}
	public void setFail_times(int fail_times) {
		this.fail_times = fail_times;
	}
	
	
	 public String getStart_fail_time() {
		return start_fail_time;
	}
	public void setStart_fail_time(String start_fail_time) {
		this.start_fail_time = start_fail_time;
	}
	
	public int getFail_retry_end() {
		return fail_retry_end;
	}
	public void setFail_retry_end(int fail_retry_end) {
		this.fail_retry_end = fail_retry_end;
	}
	
	public String getMch_secret_key() {
		return mch_secret_key;
	}
	public void setMch_secret_key(String mch_secret_key) {
		this.mch_secret_key = mch_secret_key;
	}
	
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public boolean equals(Object obj) { 
		 MchOrderData mchOrderData =(MchOrderData)obj;
         if (this.transaction_order_id !=null && this.transaction_order_id.equals(mchOrderData.getTransaction_order_id())){
        	 return true;
         }

         return false; 
    } 
	
}
