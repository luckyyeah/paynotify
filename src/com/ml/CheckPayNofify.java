package com.ml;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CheckPayNofify {
	public static final String SQL_NOT_NOTIFY = "SELECT A.*, B.NOTIFY_URL FROM transaction_order A INNER JOIN  mch_base B  ON LEFT(A.OUT_TRADE_NO,5)=B.MCH_ID  WHERE SYNCHRONIZE_STATUS=0 AND TIME(A.ADD_TIME) >= DATE_SUB( CURRENT_TIME (), INTERVAL CHECK_INTERVAL_TIMES MINUTE )";
	public static final String SQL_UPDATE_NOTIFY_FLAG = "UPDATE transaction_order SET SYNCHRONIZE_STATUS=@SYNCHRONIZE_STATUS WHERE TRANSACTION_ORDER_ID='@TRANSACTION_ORDER_ID' AND SYNCHRONIZE_STATUS=0 ;";
	private static final Logger log = (Logger) LoggerFactory.getLogger(CheckPayNofify.class);
	public static final int STATUS_NO =1; 
	public static final int STATUS_INIT =0; 
	public static final int STATUS_FAIL =-1; 
	public static final int STATUS_NO_URL =-2; 
	static List <MchOrderData> mchOrderDataSaveList =new ArrayList<MchOrderData>();
	static List <MchOrderData> mchOrderDataList =new ArrayList<MchOrderData>();
	static List <MchOrderData> mchOrderDataStatusList =new ArrayList<MchOrderData>();	
	public static boolean getPayNotifyList(){
		boolean flag =false;
		Connection conn = null;
			try{
			//连接数据库
		 conn = SqlHelper.getConnection(); 
		 
		 String sql = SQL_NOT_NOTIFY.replaceAll("CHECK_INTERVAL_TIMES", CommonUtil.getProperty("CHECK_INTERVAL_TIMES"));
			//获取设备记录集
			ResultSet rs=SqlHelper.executeQuery(conn, sql);
			if(rs !=null) {
				while(rs.next()){
					MchOrderData mchOrderData =new MchOrderData();
					mchOrderData.setTransaction_order_id(rs.getString("TRANSACTION_ORDER_ID"));
					mchOrderData.setOut_trade_no(rs.getString("USER_OUT_TRADE_NO"));
					mchOrderData.setTransaction_id(rs.getString("OUT_TRADE_NO"));
					mchOrderData.setTotal_fee(rs.getString("TOTAL_FEE"));
					mchOrderData.setBody(rs.getString("BODY"));
					mchOrderData.setAttach(rs.getString("ATTACH"));
					mchOrderData.setTime_end(rs.getString("TIME_END"));
					mchOrderData.setStatus(rs.getString("RESULT_CODE"));
					mchOrderData.setSign(rs.getString("SIGN"));
					mchOrderData.setNotify_url(rs.getString("NOTIFY_URL"));
					mchOrderData.setFail_times(0);
					if(mchOrderDataList.size() ==0 && mchOrderDataStatusList.size()==0){
						mchOrderDataSaveList =new ArrayList<MchOrderData>();
					}
					if(!mchOrderDataSaveList.contains(mchOrderData)){
						  if(mchOrderDataList==null){
							  mchOrderDataList =new ArrayList<MchOrderData>();
						  }
							mchOrderDataList.add(mchOrderData);
							mchOrderDataSaveList.add(mchOrderData);
							log.info("TRANSACTION_ORDER_ID="+mchOrderData.getTransaction_order_id());
					}
				}
			}
			rs.close();
			SqlHelper.closeConnection(conn);
			flag =true;
		}
		catch(Exception ex){
			  log.error("#ERROR# :获取支付通知信息记录失败！", ex); 
				if (conn != null) {
					try {
						conn.rollback();
						conn.close();
					} catch (SQLException e) {

					}
				}
		}
			return flag;
		}
	
	public static boolean updatetPayNotifyStatus(){
		boolean flag =false;
		Connection conn = null;
			try{
			List<String> sqlList = new ArrayList<String>();
       int listSize = mchOrderDataStatusList.size();
			for(int i=0;i<listSize;i++ ){
					MchOrderData mchOrderData = mchOrderDataStatusList.get(i);
					if(mchOrderData !=null){
						 String sql = SQL_UPDATE_NOTIFY_FLAG.replaceAll("@SYNCHRONIZE_STATUS",String.valueOf(mchOrderData.getSynchronize_status()));
						 sql = sql.replaceAll("@TRANSACTION_ORDER_ID", mchOrderData.getTransaction_order_id());
						 log.info("sql="+sql);
						 sqlList.add(sql);
						// mchOrderDataStatusList.get(i).setSynchronize_status(CheckPayNofify.STATUS_NO);
					}
			}
			if(sqlList!=null && sqlList.size()>0){
				// 连接数据库
				conn = SqlHelper.getConnection();
				conn.setAutoCommit(false);
				// 获取设备记录集
				SqlHelper.executeBatchSQL(conn, sqlList);
				conn.commit();
				SqlHelper.closeConnection(conn);
			}
			for(int i=listSize-1;i>=0;i-- ){
				log.info("reomove status TRANSACTION_ORDER_ID="+mchOrderDataStatusList.get(i).getTransaction_order_id());
				//移除已更新状态的数据
				if(mchOrderDataStatusList.get(i).getSynchronize_status()==CheckPayNofify.STATUS_NO||mchOrderDataStatusList.get(i).getSynchronize_status()<=CheckPayNofify.STATUS_FAIL){
					mchOrderDataStatusList.remove(i);
				}
			}

			flag =true;
		}
		catch(Exception ex){
			  log.error("#ERROR# :更新同步状态记录失败！", ex); 
				if (conn != null) {
					try {
						conn.rollback();
						conn.close();
					} catch (SQLException e) {

					}
				}
		}
			return flag;
		}	  
}
