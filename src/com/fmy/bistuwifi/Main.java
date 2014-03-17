package com.fmy.bistuwifi;

/**
 * Created by fmy on 13-7-15.
 */

import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.baidu.mobstat.StatService;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends Activity {
	private EditText editTextAccount=null;
	private EditText  editTextPsw=null;
	private Button buttonLogin;
	private CheckBox auto = null;
	SharedPreferences sp = null;
	public static final int MENU_HELP = 1;
	public static final int MENU_ABOUT = 2;
	public static final int MENU_EXIT = 3;



    /** Called when the activity is first created. */
    @Override

    public void onCreate(Bundle savedInstanceState) {
    	Log.w(Conf.TAG, "Main.OnCreate()");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
  
        init();
        testConnectivityManager();
    }
    public void testConnectivityManager()
    {
            ConnectivityManager connManager = (ConnectivityManager) this
                            .getSystemService(CONNECTIVITY_SERVICE);
//            // 获取代表联网状态的NetWorkInfo对象
//            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
//            // 获取当前的网络连接是否可用
//            if (null == networkInfo)
//            {
//                    Toast.makeText(this, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
//                    //当网络不可用时，跳转到网络设置页面
//                    startActivityForResult(new Intent(
//                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS), 1);
//
//            } else
//            {
//                    boolean available = networkInfo.isAvailable();
//                    if (available)
//                    {
//                            Log.i("通知", "当前的网络连接可用");
//                            Toast.makeText(this, "当前的网络连接可用", Toast.LENGTH_SHORT).show();
//                    } else
//                    {
//                            Log.i("通知", "当前的网络连接不可用");
//                            Toast.makeText(this, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
//                    }
//            }
//
//            State state = connManager.getNetworkInfo(
//                            ConnectivityManager.TYPE_MOBILE).getState();
//            if (State.CONNECTED == state)
//            {
//                    Log.i("通知", "GPRS网络已连接");
//                    Toast.makeText(this, "GPRS网络已连接", Toast.LENGTH_SHORT).show();
//            }

            State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                            .getState();
            if (State.CONNECTED == state)
            {
            	
//                    Log.i("通知", "WIFI网络");
//                    Toast.makeText(this, "WIFI网络已连接", Toast.LENGTH_SHORT).show();
            }

            //// 跳转到无线网络设置界面
            // startActivity(new
            // Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            //// 跳转到无限wifi网络设置界面
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);   
                builder.setTitle("警告！");
                builder.setMessage("请打开并连接校园 WIFI!");
                builder.setCancelable(false);
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() { 
                	public void onClick(DialogInterface dialog, int id) { 
                		dialog.cancel();
                        startActivity(new
                                Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                		} 
                		}); 
                builder.show();

             
            }

    }


    


    public void init()
	{
        editTextAccount=(EditText) findViewById(R.id.editTextAccount);
        editTextPsw=(EditText) findViewById(R.id.editTextPsw);
        buttonLogin=(Button) findViewById(R.id.buttonLogin);
        auto = (CheckBox) findViewById(R.id.auto);
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
	    if (sp.getBoolean("auto", false))
	  		{
	    	editTextAccount.setText(sp.getString("editTextAccount", null));
	    	editTextPsw.setText(sp.getString("editTextPsw", null)); 
	  	    	auto.setChecked(true);
	  	     
	  		}
        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
    			String editTextAccount1 = editTextAccount.getText().toString();
    		 	String editTextPsw1 = editTextPsw.getText().toString();
    		 	if(editTextAccount1.trim().equals("")){
    		 		Toast.makeText(Main.this, "请输入用户名！", Toast.LENGTH_LONG).show();
    				return;
        		}
        		if(editTextPsw1.trim().equals("")){
        			Toast.makeText(Main.this, "请您输入密码！", Toast.LENGTH_SHORT).show();
    				return;
        		}
    			boolean autoLogin = auto.isChecked();
    			if (autoLogin)
    			{
    					Editor editor = sp.edit();
    					editor.putString("editTextAccount", editTextAccount1);
    					editor.putString("editTextPsw", editTextPsw1);
    					editor.putBoolean("auto", true);
    					editor.commit();
    			}
    			else
    			{  
    				Editor editor = sp.edit();
    				editor.putString("editTextAccount", null);
    				editor.putString("editTextPsw", null);
    				editor.putBoolean("auto", false);
    				editor.commit();}   
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("redirect_url", "http://www.baidu.com"));                 
                params.add(new BasicNameValuePair("buttonClicked", "4"));  
                params.add(new BasicNameValuePair("username", editTextAccount1));
                params.add(new BasicNameValuePair("password", editTextPsw1));//传递参数
                HTTPThread httpThread=new HTTPThread(loginHandler);
                //在工作线程中执行耗时任务，防止UI线程阻塞
                httpThread.doStart("https://6.6.6.6/login.html", params, Main.this);

            }
        });
               
   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//创建系统功能菜单
		// TODO Auto-generated method stub
		
		menu.add(0,MENU_HELP,1,"帮助").setIcon(R.drawable.menu_setting);
		menu.add(0, MENU_ABOUT, 2, "关于").setIcon(R.drawable.menu_about);
		menu.add(0, MENU_EXIT, 3, "退出").setIcon(R.drawable.menu_exit);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_HELP:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);   
            builder.setTitle("帮助");
            builder.setMessage("请再三确认您已经连接校园WIFI\n其余参见提示\n最后你只能找我了。。。");
            builder.setCancelable(false);
            builder.setPositiveButton("确定",new DialogInterface.OnClickListener() { 
            	public void onClick(DialogInterface dialog, int id) { 
            		dialog.cancel(); 
            		} 
            		});
            builder.show();
            break;
		case MENU_ABOUT:
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);   
            builder1.setTitle("关于我");
            builder1.setMessage("北京信息科技大学\n计科1203\n朱劲寿\nhttp://www.idev.pw");
            builder1.setCancelable(false);
            builder1.setPositiveButton("确定",new DialogInterface.OnClickListener() { 
            	public void onClick(DialogInterface dialog, int id) { 
            		dialog.cancel(); 
            		} 
            		});
            builder1.show();
            break;
		case MENU_EXIT:
            AlertDialog.Builder builder11 = new AlertDialog.Builder(this);   
            builder11.setTitle("退出BistuWifi");
            builder11.setMessage("确定要退出？");
            builder11.setCancelable(false);
            builder11.setPositiveButton("确定", new DialogInterface.OnClickListener() { 
            		public void onClick(DialogInterface dialog, int id) { 
            	Main.this.finish(); 
            	} 
            	}); 
            builder11.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
            	public void onClick(DialogInterface dialog, int id) { 
            	dialog.cancel(); 
            	} 
            	}); 
            builder11.show();

			
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onResume() {
		Log.w(Conf.TAG, "Main.OnResume()");
		super.onResume();

		StatService.onResume(this);
	}
	@Override
	public void onPause() {
		Log.w(Conf.TAG, "Main.OnResume()");
		super.onPause();

		StatService.onPause(this);
	}

    
    @SuppressLint("HandlerLeak")
	private Handler loginHandler=new Handler(){
        public void handleMessage(Message message) {
        	System.out.println(message.what);
        	Bundle bundle = message.getData(); 
        	String data=bundle.getString("info");//读出数据 
            switch (message.what) {
            case 0://程序执行正常
                Toast.makeText(Main.this, "成功登录！", Toast.LENGTH_LONG).show();
                Intent count = new Intent();
                count.setClass(Main.this,count.class);
                startActivity(count) ;
                overridePendingTransition(0, 0); //设置不要动画
                Main.this.finish();
                break;
            case 1://程序执行正常
                Toast.makeText(Main.this, "已经登录，不能重复登录。", Toast.LENGTH_LONG).show();
                Intent count1 = new Intent();
                count1.setClass(Main.this,count.class);
                startActivity(count1) ;
                overridePendingTransition(0, 0); //设置不要动画
                Main.this.finish();
                break;
            case 2://程序执行正常
                Toast.makeText(Main.this, "服务器拒绝，请稍后再试。", Toast.LENGTH_LONG).show();
                break;
            case 3://程序执行正常
                Toast.makeText(Main.this, "该用户已经在其他系统登录。", Toast.LENGTH_LONG).show();
                break;
            case 4://程序执行正常
                Toast.makeText(Main.this, "多次登录错误，请稍候登录。", Toast.LENGTH_LONG).show();
                break;
            case 5://程序执行正常
                Toast.makeText(Main.this, "登录错误。可能原因：用户名、密码错误；没有退出外网情况下，退出内网，请10分钟之后再尝试登录。", Toast.LENGTH_LONG).show();
                break;
            default://
                Toast.makeText(Main.this, data, Toast.LENGTH_LONG).show();
                break;
            }
        }
    };
    
    
    
}
