package com.ml;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
	"yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取YYYY格式
	 * 
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	* @Title: compareDate
	* @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author 
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日�?
	 * 
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 格式化日期时�?
	 * 
	 * @return
	 */
	public static Date fomatDateTime(String datetime) {
		DateFormat fmt = sdfTime;
		try {
			return fmt.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或�?�NullPointerException，就说明格式不对
			return false;
		}
	}
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long aa=0;
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或�?�NullPointerException，就说明格式不对
			return 0;
		}
	}
	public static int getDiffTime(String startTime,String endTime) {

		try {
			int second=(int) (sdfTime.parse(endTime).getTime()-sdfTime.parse(startTime).getTime());
			return second;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或�?�NullPointerException，就说明格式不对
			return 0;
		}
	}
	  /**
     * <li>功能描述：时间相减得到天�?
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天�?="+day);
      
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util�?
        canlendar.add(Calendar.DATE, daysInt); // 日期�? 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        
        return dateStr;
    }
    
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util�?
        canlendar.add(Calendar.DATE, daysInt); // 日期�? 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        
        return dateStr;
    }
    
    public static void main(String[] args) {
    	System.out.println(getDays());
    	System.out.println(getAfterDayWeek("3"));
    }
    
    /**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDateTime(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或�?�NullPointerException，就说明格式不对
			return false;
		}
	}
    
    /**
     * 取当�? 00:00:00
     * 
     * @param day
     * @return
     */
    public static String getBeginTime(String day) {
    	StringBuffer beginTime = new StringBuffer("");
    	if(isValidDateTime(day)) {
    		beginTime.append(day.substring(0,10));
    		beginTime.append(" 00:00:00");
    	}
    	return beginTime.toString();
    }
    
    /**
     * 取当�? 23:59:59
     * 
     * @param day
     * @return
     */
    public static String getEndTime(String day) {
    	StringBuffer endTime = new StringBuffer("");
    	if(isValidDateTime(day)) {
    		endTime.append(day.substring(0,10));
    		endTime.append(" 23:59:59");
    	}
    	return endTime.toString();
    }
    public static String getIntervalTime(String time, int interval) {
        String pattern = "yyyy-MM-dd HH:mm:ss";

        Calendar calendar = Calendar.getInstance();
        Date clickdate = null;
        try {
            clickdate = new SimpleDateFormat(pattern).parse(time);
            calendar.setTime(clickdate);
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)/interval *interval);
            calendar.set(Calendar.SECOND, 0);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sdfTime.format(calendar.getTime());
    }
    public static String getAddTime(String time, int interval) {
        String pattern = "yyyy-MM-dd HH:mm:ss";

        Calendar calendar = Calendar.getInstance();
        Date clickdate = null;
        try {
            clickdate = new SimpleDateFormat(pattern).parse(time);
            calendar.setTime(clickdate);
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+interval);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sdfTime.format(calendar.getTime());
    }
}
