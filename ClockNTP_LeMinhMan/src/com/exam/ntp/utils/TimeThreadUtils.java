package com.exam.ntp.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.time.DateUtils;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

import com.exam.ntp.presenter.MainActivity;
import com.exam.ntp.view.DrawClock;

/**
 * 
 * @author manle
 *
 */
public class TimeThreadUtils implements Runnable{
	
	private DrawClock drawClock = null;
	private Activity activity = null;
	private Calendar calendar =Calendar.getInstance();
	
	public TimeThreadUtils(Activity activity,DrawClock drawClock){
		this.activity = activity;
		this.drawClock = drawClock;
	}
	
	@Override
	public void run() {
		calTimeAfterSync();
	}

	/**
	 * After get time from NTP Server, calculate time
	 */
	private void calTimeAfterSync(){
		
		while(true){
        	activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					int second = calendar.get(Calendar.SECOND);
					second+=1;
					calendar.set(Calendar.SECOND,second);
					drawClock.setDate(calendar.getTime());
    	        	drawClock.invalidate();
				}
			});
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
	
	public void setDate(Date date) {
		calendar.setTime(date);
	}

	public Date getDate() {
		return calendar.getTime();
	}
}
