package com.fmy.bistuwifi;


/**
 * Created by fmy on 13-7-15.
 */
import android.net.TrafficStats;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.baidu.mobstat.StatService;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class count extends Activity{

	TextView TotalRxBytes;
	TextView TotalTxBytes;
    Button buttonlogout;
    /** Called when the activity is first created. */
    @Override

    public void onCreate(Bundle savedInstanceState) {
    	Log.w(Conf.TAG, "count.OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count);
        TotalRxBytes=(TextView) findViewById(R.id.TotalRxBytes);
        TotalTxBytes=(TextView) findViewById(R.id.TotalTxBytes);
        buttonlogout=(Button) findViewById(R.id.buttonlogout);
        Long a = getTotalRxBytes();
        String stringa =a.toString();
        TotalRxBytes.setText("总接受："+stringa + "KB");
        Long b = getTotalTxBytes();
        String stringb =b.toString();
        TotalTxBytes.setText("总发送："+ stringb + "KB");
        buttonlogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("logout", "logout"));                 
                params.add(new BasicNameValuePair("userStatus", "1"));  
                HTTPThread httpThread=new HTTPThread(loginHandler);
                //在工作线程中执行耗时任务，防止UI线程阻塞
                httpThread.doStart("https://6.6.6.6/logout.html", params, count.this);

                //6.6.6.6。
            }

        });
    }
    @SuppressLint("NewApi")
	public long getTotalRxBytes(){  //获取总的接受字节数，包含Mobile和WiFi等  
        return TrafficStats.getTotalRxBytes()==TrafficStats.UNSUPPORTED?0:(TrafficStats.getTotalRxBytes()/1024);  
    } 
    @SuppressLint("NewApi")
	public long getTotalTxBytes(){  //总的发送字节数，包含Mobile和WiFi等  
        return TrafficStats.getTotalTxBytes()==TrafficStats.UNSUPPORTED?0:(TrafficStats.getTotalTxBytes()/1024);
    }
    @SuppressLint("HandlerLeak")
	private Handler loginHandler=new Handler(){
        public void handleMessage(Message message) {
        	System.out.println(message.what);
            switch (message.what) {
            case 0://程序执行正常
                Toast.makeText(count.this, "成功退出。", Toast.LENGTH_LONG).show();
                Intent count = new Intent();
                count.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			Intent intent = new Intent(count.this,Main.class);
    			startActivity(intent);
    			overridePendingTransition(0, 0); //设置不要动画
    			
    			count.this.finish();
                break;
            case 6://程序执行正常
                Toast.makeText(count.this, "程序异常。", Toast.LENGTH_LONG).show();
                break;
            }
        }
    };
    
	@Override
	public void onResume() {
		Log.w(Conf.TAG, "count.OnResume()");
		super.onResume();

		StatService.onResume(this);
	}
	@Override
	public void onPause() {
		Log.w(Conf.TAG, "count.OnResume()");
		super.onPause();

		StatService.onPause(this);
	}
    
}



