package com.fmy.bistuwifi;

/**
 * Created by fmy on 13-7-15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
/**欢迎动画activity*/
public class loading extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.w(Conf.TAG, "loading.OnCreate()");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading);
        
        final Intent intent = new Intent(loading.this, Main.class);
        
        //系统会为需要启动的activity寻找与当前activity不同的task;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
					//获取应用的上下文，生命周期是整个应用，应用结束才会结束
					getApplicationContext().startActivity(intent);
					finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
    }
    
    
    
    
}