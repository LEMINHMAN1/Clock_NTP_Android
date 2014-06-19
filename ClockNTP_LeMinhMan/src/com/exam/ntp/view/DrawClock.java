package com.exam.ntp.view;

import java.util.Date;

import com.exam.ntp.model.TimeModel;
import com.exam.ntp.utils.DateUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;

/**
 * 
 * @author manle
 *
 */
public class DrawClock extends View {

	private final int xcenter = 225; 
	private final int ycenter = 225;

	private final Paint paint = new Paint();

	private final Paint styleSecond = new Paint();
	private final Paint styleMinute = new Paint();
	private final Paint styleHour = new Paint();
	
	private Date date;
	
	public DrawClock(Context context, Date date) {
		super(context);
		this.date = date;
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(3);
		paint.setTextSize(30);
		paint.setStyle(Paint.Style.STROKE);
		
		styleSecond.setColor(Color.RED);
		styleSecond.setStrokeWidth(3);
		
		styleMinute.setColor(Color.GRAY);
		styleMinute.setStrokeWidth(4);
		
		styleHour.setColor(Color.GRAY);
		styleHour.setStrokeWidth(5);
	}

	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		
		canvas.drawCircle(xcenter, ycenter, 165, paint);
		
//		Bitmap imgClock = BitmapFactory.decodeResource(getResources(), R.drawable.clock);
//		canvas.drawBitmap(imgClock,0,0, paint);
		
		canvas.drawText("9", xcenter-145,ycenter, paint);
		canvas.drawText("3", xcenter+135,ycenter, paint);
		canvas.drawText("12", xcenter-10,ycenter-130, paint);
		canvas.drawText("6", xcenter-10,ycenter+145, paint);
		
		int xsecond = 0;
		int ysecond =0;
		int xminute =0;
		int yminute =0;
		int xhour =0;
		int yhour =0;

		TimeModel timeModel = DateUtils.adaptTime(date);
		if(timeModel!=null){
			xsecond =(int) (Math.cos(timeModel.getSecond() * 3.14f / 30 - 3.14f / 2) * 140 + xcenter);
			ysecond =(int) (Math.sin(timeModel.getSecond() * 3.14f / 30 - 3.14f / 2) * 140 + ycenter);
			xminute =(int) (Math.cos(timeModel.getMinute() * 3.14f / 30 - 3.14f / 2) * 120 + xcenter);
			yminute =(int) (Math.sin(timeModel.getMinute() * 3.14f / 30 - 3.14f / 2) * 120 + ycenter);
			xhour =(int) (Math.cos((timeModel.getHour() * 30 + 10 / 2) * 3.14f / 180 - 3.14f / 2) * 80 + xcenter);
			yhour =(int) (Math.sin((timeModel.getHour() * 30 + 10 / 2) * 3.14f / 180 - 3.14f / 2) * 80 + ycenter);	
		}
		
		// second
		canvas.drawLine(xcenter, ycenter, xsecond, ysecond, styleSecond);
		
		// minute
		canvas.drawLine(xcenter, ycenter, xminute, yminute, styleMinute);
		
		// hour
		canvas.drawLine(xcenter, ycenter, xhour, yhour, styleHour);
		
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
