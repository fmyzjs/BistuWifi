package com.fmy.bistuwifi;


/**
 * Created by fmy on 13-7-15.
 */

import java.io.IOException;
import java.security.KeyStore;
import java.util.List;



import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class HTTPThread extends Thread {
    ProgressDialog pdDialog = null;
    String urlString = null;
    List<NameValuePair> params = null;
    Handler handler = null;

    public HTTPThread(Handler handler) {
        this.handler = handler;
    }

    @SuppressWarnings({ "deprecation" })
	public void doStart(String urlString, List<NameValuePair> params,
            Context context) {//进行一些初始化工作然后调用start()让线程运行
        this.urlString = urlString;
        this.params = params;
    	
        pdDialog = new ProgressDialog(context);
        pdDialog.setTitle("请稍候...");
        pdDialog.setMessage("正在连接...");
        pdDialog.setIndeterminate(true);
        pdDialog.setButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                pdDialog.cancel();
            }
        });

        pdDialog.show();
        this.start();
        

    }

    @Override
    public void run() {
        Message msg = new Message();
        Bundle data = new Bundle();
        try {
            String result = webServiceLogin();
            Log.i("tt", result);
            int a=result.indexOf("statusCode=");
            if (a!=-1){
            a=a+11;
            String number = result.substring(a,a+1);
            msg.what= Integer.parseInt(number);
            }
            else{msg.what=0;}

            data.putString("info", "normal");
            msg.setData(data);
        } catch (ParseException e) {
            msg.what=6;
            data.putString("info", "解析异常");
            msg.setData(data);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            msg.what=6;
            data.putString("info", "连接超时,请检查网络");
            msg.setData(data);
            e.printStackTrace();
        } catch (IOException e) {
            msg.what=6;
            data.putString("info", "程序异常,请检查网络");
            msg.setData(data);
            e.printStackTrace();
        } finally {
            pdDialog.dismiss();
            handler.sendMessage(msg);
        }
  }
        
    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private String  webServiceLogin() throws ParseException,ConnectTimeoutException,
            ClientProtocolException, IOException {
    	HttpClient httpclient = getNewHttpClient();
        
        httpclient.getParams().setIntParameter(
        		HttpConnectionParams.SO_TIMEOUT, 50000); // 超时设置
        httpclient.getParams().setIntParameter(
        		HttpConnectionParams.CONNECTION_TIMEOUT, 50000);// 连接超时
       
        HttpPost httpPost = new HttpPost(urlString);//建立HttpPost对象		
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));//设置参数
        String res = EntityUtils.toString(httpclient.execute(httpPost)
                .getEntity());//执行并返回结果：
        Log.i("ss", res);
        return res;
        
    }
}