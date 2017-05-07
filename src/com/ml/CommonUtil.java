package com.ml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;

import net.sf.json.JSONObject;




public class CommonUtil {
	private static final Log log = LogFactory.getLog(SqlHelper.class); 
	public static String PROPERTY_FILE = "activity.properties";
	public static String getProperty(String path,String keyName)   {
		String value = null;
		try{
	
			InputStream ins = CommonUtil.class.getClassLoader().getResourceAsStream(path);
			
			Properties properties = new Properties();
			properties.load(ins);
			value = properties.getProperty(keyName);
			ins.close();
		}catch(Exception ex) {
			 log.error("#ERROR# :系统加载"+path+"配置文件异常，请检查！", ex); 
		}
		return value;
	}
	public static String getProperty(String keyName) throws IOException  {

		return getProperty(PROPERTY_FILE,keyName);
	}
	public static String round2String(double data,int digits){
		   NumberFormat nf = NumberFormat.getNumberInstance();   
	       nf.setMaximumFractionDigits(digits);   
	      return nf.format(data);  
	}
	public static String round2String(float data,int digits){
		   NumberFormat nf = NumberFormat.getNumberInstance();   
	       nf.setMaximumFractionDigits(digits);   
	      return nf.format(data);  
	}
	public static long getStringNum(String str){
		long lRes = 0;
		try{
				
				String res ="";
				String regex = "\\d*";
	       Pattern p = Pattern.compile(regex);
	       Matcher m = p.matcher(str);
	       while(m.find()) {
	           if(!"".equals(m.group()))
	        	   res+=m.group();
	       }
	       lRes = Long.parseLong(res);
		}catch(Exception ex){
			
		}
	   return  lRes;
	}
	public static String getStringOfStr(String str){
		String res ="";
		try{
				String regex = "\\d*";
	       Pattern p = Pattern.compile(regex);
	       Matcher m = p.matcher(str);
	       while(m.find()) {
	           if(!"".equals(m.group()))
	        	   res+=m.group();
	           
	       }
	       res =  str.replace(res, "");
		}catch(Exception ex){
			
		}
	   return  res;
	}

	//生成随机字符串
	public static String getRandomString(int min,int max,int len){
			 String strRet ="";
       Random random = new Random();
       for(int i=0;i<len;i++){
    	   int rand = random.nextInt(max)%(max-min+1) + min;
    	   strRet +=rand;
       }
       if(strRet.length() > len){
    	   strRet = strRet.substring(0,len-1);
       }
       return strRet;
	}
	public static int getRandomInt(int min,int max){
		Random random = new Random();
  return random.nextInt(max)%(max-min+1) + min;
}

public static  String postURL(String strURL,String jsonData){

	    
	    PrintWriter out = null;
        BufferedReader in = null;
	    HttpClient httpClient = new HttpClient();
	    PostMethod method = new PostMethod(strURL);
	    try {
	      if(jsonData != null && !"".equals(jsonData.trim())) {
	        RequestEntity requestEntity = new StringRequestEntity(jsonData,"application/json","UTF-8");
	        method.setRequestEntity(requestEntity);
	      }
	      method.releaseConnection();
	      httpClient.executeMethod(method);
	      String responses= method.getResponseBodyAsString();
	      return responses;

	    } catch (IOException e) {
	      e.printStackTrace();
	    }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return "" ;
     
	}
	public static  String  requestURL(String strURL){
	    String  responseMessage = "";
	     BufferedReader in = null;
	     StringBuffer sb = new StringBuffer();  
	    try{
			URL url = new URL(strURL);
			URLConnection conn = url.openConnection();
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String strData = null;
			while ((strData = in.readLine()) != null) {
				sb.append(strData);
			}
		} catch (Exception ex) {
			 ex.printStackTrace();

		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		responseMessage = sb.toString();

	    return responseMessage;
	}

	public static String doGet(String url)  {
		StringBuffer resultBuffer = new StringBuffer();
		try{
		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("Content-Type", "application/json");

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		
		String tempLine = null;

/*		if (httpURLConnection.getResponseCode() >= 300) {
			throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
		}
*/
		try {
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}

		} finally {

			if (reader != null) {
				reader.close();
			}

			if (inputStreamReader != null) {
				inputStreamReader.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

		}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return resultBuffer.toString();
	

	}
	
    public static String getWebService(String webServiceUrl,String funName,Map<String,String> paramMap ) {  
        URL url;  
        BufferedReader bin = null;  
        StringBuilder result = new StringBuilder();  
        try {  
             webServiceUrl = webServiceUrl+ "/" +funName + "?";  
             boolean isFirst =true;
             if(paramMap !=null){
	     		for (String paramKey : paramMap.keySet()) {
	     			if(!isFirst){
	     				webServiceUrl+="&";
	     			}
	     			webServiceUrl+=paramKey+"=" + paramMap.get(paramKey);
	     			isFirst =false;
	    		}
             }
            url = new URL(webServiceUrl);  
            InputStream in = url.openStream(); // 请求  
            bin = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
            String s = null;  
            while ((s = bin.readLine()) != null) {  
                result.append(s);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (null != bin) {  
                try {  
                    bin.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return result.toString();  
    }

	/**
	 * 是否是电信用户
	 * @param context
	 * @return
	 */
	public static boolean isTele(String IMSI)
	{

		if (IMSI.startsWith("46003") || IMSI.startsWith("20404") || IMSI.startsWith("46005") || IMSI.startsWith("46011")) {  
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 是否是联通用户
	 * @param context
	 * @return
	 */
	public static boolean isUni(String IMSI)
	{

		if (IMSI.startsWith("46001")||IMSI.startsWith("46010")
				||IMSI.startsWith("46006") ||IMSI.startsWith("46009"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 是否是移动用户
	 * @param context
	 * @return
	 */
	public static boolean isMove(String IMSI)
	{
		 if (IMSI.startsWith("46000")||IMSI.startsWith("46002")
					||IMSI.startsWith("46007")||IMSI.startsWith("46020"))
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }
	}

	public static String doPost(String url, Map<String, String> params, String charset) {
		StringBuffer response = new StringBuffer();

		PostMethod method = null;
		try {
			HttpClient client = new HttpClient();
			method = new PostMethod(url);
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,charset); 
			// 设置Http Post数据
			if (params != null) {
				NameValuePair[] parametersBody = new NameValuePair[params.size()];
				int index = 0;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					parametersBody[index++] = new NameValuePair(entry.getKey(), entry.getValue());
				}

				method.setRequestBody(parametersBody);
			}
			method.releaseConnection();
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return response.toString();
	}

	public static String doGet(String url, Map<String, String> params, String charset) {
		StringBuffer response = new StringBuffer();

		GetMethod method = null;
		try {
			HttpClient client = new HttpClient();
			String mParams = "";
			// 设置Http Get数据
			if (params != null) {
				int iStart = 0;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (iStart++ == 0) {
						if(entry.getValue()!=null && !"".equals(entry.getValue())){
							mParams += entry.getKey() + "=" +  URLEncoder.encode(URLEncoder.encode(entry.getValue(), charset), charset);
						}
					} else {
						if(entry.getValue()!=null && !"".equals(entry.getValue())){
							mParams += "&" + entry.getKey() + "=" +  URLEncoder.encode(URLEncoder.encode(entry.getValue(), charset), charset);
						}
					}
				}
			}
			method = new GetMethod(url + "?" + mParams);

			method.releaseConnection();
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return response.toString();
	}

	public static String doRequest(String url, Map<String,String> params) {
		String ret = doPost(url, params, "utf-8");
		if (ret == null || "".equals(ret)) {
			ret = doGet(url, params, "utf-8");
		}
		return ret;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, DocumentException {
/*		getStringOfStr("m_ios290");
		System.out.println(getRandomString(0,9,10));*/
		for(int i=0;i<50000;i++){
		
			System.out.println("i="+i);
		}
	}
}
