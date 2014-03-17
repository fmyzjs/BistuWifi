package com.baidu.mobstat.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;

public class DemoActivity1 extends Activity {
	/** Called when the activity is first created. */

	private Button btn_pre;
	private Button btn_next;
	private Button btn_exception;
	private Button btn_event;
	private Button btn_event_duration;
	private Button btn_event_start;
	private Button btn_event_end;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.w(Conf.TAG, "Activity1.OnCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout1);

		//设置AppKey以及ppChannel
		//StatService.setAppKey("abc1234");//appkey必须在mtj网站上注册生成，该设置也可以在AndroidManifest.xml中填写
		//StatService.setAppChannel("Baidu Market");//appChannel是应用的发布渠道，不需要在mtj网站上注册，直接填写就可以
		
		//setOn也可以在AndroidManifest.xml文件中填写，BaiduMobAd_EXCEPTION_LOG
		//StatService.setOn(this,StatService.EXCEPTION_LOG);
		
		/*
		 * 设置启动时日志发送延时的秒数<br/>
		 * 单位为秒，大小为0s到30s之间<br/>
		 * 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
		 * 
		 * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/>
		 * 这个参数的设置暂时只支持代码加入， 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
		 * 
		 * 
		 */
		//StatService.setLogSenderDelayed(10);
		
		/*
		 * 用于设置日志发送策略<br />
		 * 嵌入位置：Activity的onCreate()函数中 <br />
		 * 
		 * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum.
		 * SET_TIME_INTERVAL, 1); 
		 * 
		 * 第二个参数可选：
		 *  SendStrategyEnum.APP_START
		 *  SendStrategyEnum.ONCE_A_DAY
		 *  SendStrategyEnum.SET_TIME_INTERVAL
		 * 第三个参数：
		 * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、
		 * 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位
		 */
		//StatService.setSendLogStrategy(this,SendStrategyEnum.APP_START, 1,false);
				
		btn_pre = (Button) findViewById(R.id.layout1_btn1);
		btn_next = (Button) findViewById(R.id.layout1_btn2);
		btn_exception = (Button) findViewById(R.id.layout1_btn_excep);
		btn_event = (Button) findViewById(R.id.layout1_btn_event);
		btn_event_duration = (Button) findViewById(R.id.layout1_btn_event);
		
		btn_event_start = (Button) findViewById(R.id.layout1_btn_event_start);
		btn_event_end = (Button) findViewById(R.id.layout1_btn_event_end);

		btn_pre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(DemoActivity1.this, DemoActivity3.class);

				DemoActivity1.this.startActivity(intent);
			}
		});

		btn_next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DemoActivity1.this, DemoActivity2.class);
				DemoActivity1.this.startActivity(intent);
			}
		});

		btn_exception.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.w(Conf.TAG, 10 / 0 + "");
			} 
		}); 

		btn_event.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// "registered id"必须在mtj网站的自定义事件中添加， “pass”是该注册事件下的事件
				StatService.onEvent(DemoActivity1.this, "registered id", "pass", 1);
				
			} 
		});
		
		/**
		 * 自定义事件的第一种方法，写入某个事件的持续时长
		 */
		btn_event_duration.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//事件id（"registered id"）的事件pass，其时长持续100毫秒
				StatService.onEventDuration(DemoActivity1.this, "registered id", "pass", 100);

			} 
		});
		
		/*
		 * 自定义事件的第二种方法，自己定义该事件的起始时间和结束时间
		 */
		btn_event_start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//事件id（"registered id"）的事件pass，其时长持续10毫秒
				StatService.onEventStart(DemoActivity1.this, "registered id", "pass");//必须和onEventEnd共用才行

			} 
		});
		
		/*
		 * 自定义事件的第二种方法，自己定义该事件的起始时间和结束时间
		 */
		btn_event_end.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//事件id（"registered id"）的事件pass，其时长持续10毫秒
				StatService.onEventEnd(DemoActivity1.this, "registered id", "pass");//必须和onEventStart共用才行

			} 
		});
	}
 
	    
	public void onResume() {
		Log.w(Conf.TAG, "Activity1.OnResume()");
		super.onResume();

		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 */
		StatService.onResume(this);
	}

	public void onPause() {
		Log.w(Conf.TAG, "Activity1.onPause()");
		super.onPause();

		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 */
		StatService.onPause(this);
	}
}

//      /~ 