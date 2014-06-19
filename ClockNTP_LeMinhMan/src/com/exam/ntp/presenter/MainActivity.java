package com.exam.ntp.presenter;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exam.ntp.utils.DateUtils;
import com.exam.ntp.utils.TimeThreadUtils;
import com.exam.ntp.view.DrawClock;
import com.exam.ntp.view.R;

public class MainActivity extends Activity implements OnClickListener{
	
	private final int minuteContants = 9;
	private final int secondContants = 60;
	
	private TextView txtvSyncComming;
	private TextView txtvTimeText;
	private Button btnSyncNow;
	private Date dateNTP;
	
	private int minute=0;
	private int second =0;
	private final Handler myHandler = new Handler();
	private DrawClock drawClock = null;
	
	Thread threadRunClock = null;
	TimeThreadUtils startRunClock = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        drawClock = new DrawClock(this,new Date());
        LinearLayout lLayout = (LinearLayout) findViewById(R.id.clockLayout);
        lLayout.addView(drawClock);
        
        startRunClock = new TimeThreadUtils(MainActivity.this, drawClock);
        startRunClock.setDate(new Date());
        threadRunClock = new Thread(startRunClock);
        threadRunClock.start();
        
        txtvSyncComming = (TextView) findViewById(R.id.txtvSyncComming);
        txtvTimeText  = (TextView) findViewById(R.id.txtvTimeText);
        btnSyncNow = (Button) findViewById(R.id.btnSynNow);
        btnSyncNow.setOnClickListener(this);
        
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
           public void run() {syncComming();}
        }, 0, 1000); 
    }
    
    private void syncComming() {
    	
    	// Always sync time from NTP server
    	dateNTP = syncNow();
    	
        if(second>0){
        	second=second-1;
        }
        if(second<=0){
        	if(minute>0)minute=minute-1;
        	second=secondContants;
        }
        
        if(minute==0){
        	changeDateFromNTPServer();
        }
        
        myHandler.post(myRunnable);
     }
    
    private Date syncNow(){
    	String hostsNTP = getString(R.string.ntpHosts);
    	String[] hostsArrayNTP  = null;
    	if(StringUtils.isNotBlank(hostsNTP)){
    		hostsArrayNTP =hostsNTP.trim().split(",");
    	}
    	
    	String timeOutNTP = getString(R.string.ntpTmeOut);
    	Date dateNTP = DateUtils.getNTPDate(hostsArrayNTP, Integer.parseInt(timeOutNTP));
    	if(dateNTP==null){
    		dateNTP = new Date();
    	}

    	return dateNTP;
    }
    
    private void changeDateFromNTPServer(){
    	startRunClock.setDate(dateNTP);
    	second=secondContants;
    	minute = minuteContants;
    }
   
    final Runnable myRunnable = new Runnable() {
        public void run() {
        	txtvSyncComming.setText("Will sync in "+minute+":"+second);
        	txtvTimeText.setText(DateUtils.formatTime(startRunClock.getDate()));
        }
     };

	@Override
	public void onClick(View v) {
		changeDateFromNTPServer();
		myHandler.post(myRunnable);
	}
    
}