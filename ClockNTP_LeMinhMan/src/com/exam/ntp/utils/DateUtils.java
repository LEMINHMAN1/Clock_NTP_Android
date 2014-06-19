package com.exam.ntp.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;

import android.annotation.SuppressLint;
import android.util.Log;

import com.exam.ntp.model.TimeModel;

/**
 * 
 * @author manle
 * 
 * Contains function adapt date times
 */

public class DateUtils {

	/**
	 * Get date after sync from NTP
	 * @param hosts {@link String[]} hosts server NTP
	 * @param timeOut {@link Integer} milisecod
	 * @return Date
	 */
	public static Date getNTPDate(String[] hosts, int timeOut) {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(timeOut);
        for (String host : hosts) {
            try {
                InetAddress hostAddr = InetAddress.getByName(host);
                TimeInfo info = client.getTime(hostAddr);
                NtpV3Packet message = info.getMessage();
                long serverTime = message.getTransmitTimeStamp().getTime();
                
                Date date = new Date(serverTime);
                return date;

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        client.close();
        return null;
    }
	
	/**
	 * Adapt time with seperate second number, minute number, hour number; and put all into model
	 * @param currentDate {@link Date}
	 * @return {@link TimeModel}
	 */
	@SuppressLint("SimpleDateFormat")
	public static TimeModel adaptTime(final Date currentDate){
		TimeModel timeModel = null;
		
		if(currentDate!=null){
			timeModel = new TimeModel();
			
            String time = formatTime(currentDate);
            if(StringUtils.isNotBlank(time)){
            	String[] splitTime = time.split(":");
            	int hour = Integer.parseInt(splitTime[0]);
            	int minute = Integer.parseInt(splitTime[1]);
            	int second = Integer.parseInt(splitTime[2]);

            	timeModel.setHour(hour);
            	timeModel.setMinute(minute);
            	timeModel.setSecond(second);
            }
		}
		
		return timeModel;
	}
	
	/**
	 * Format time
	 * @param date
	 * @return
	 */
	public static String formatTime(final Date date){
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String time = timeFormat.format(date);
		return time;
		
	}
}
