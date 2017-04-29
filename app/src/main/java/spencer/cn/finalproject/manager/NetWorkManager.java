package spencer.cn.finalproject.manager;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import spencer.cn.finalproject.iexport.NewsCallBack;

/**
 * Created by Administrator on 2017/4/9.
 */

public class NetWorkManager {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    ////////////////////////////////////请求新闻
    public static void requestNews(final String head, final String type, final NewsCallBack callBack){
        new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = head + "?type=" + type;
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String gsonStrings = response.body().string();
                    if (callBack != null){
                        callBack.onNewsReturn(gsonStrings);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    ////////////////////////////////////post请求封装参数
    public static String maptogstring(HashMap<String, String> params){
        StringBuilder gstring = new StringBuilder("{");
        Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = iter.next();
            if (iter.hasNext() == false){
                gstring.append("\""+entry.getKey() + "\":\"" + entry.getValue()+"\"");
            }else{
                gstring.append("\""+entry.getKey() + "\":\"" + entry.getValue() + "\",");
            }
        }
        gstring.append("}");
        Log.e("XXXXX", gstring.toString());
        return gstring.toString();
    }
    //////////////////////////////////////////////////////通用的发post请求的方法
    public static void doPost(final String url, final HashMap<String, String> params, final NewsCallBack callBack){
        new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String str = NetWorkManager.maptogstring(params);
                RequestBody body = RequestBody.create(JSON, str);
                Request request = new Request.Builder()
                        .post(body)
                        .url(url)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String result = response.body().string();
                    if (callBack != null){
                        callBack.onNewsReturn(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public static void doPost(final String url, final String params, final NewsCallBack callBack){
        new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, params);
                Request request = new Request.Builder()
                        .post(body)
                        .url(url)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String result = response.body().string();
                    if (callBack != null){
                        callBack.onNewsReturn(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
    ///////////////////////////////////////////////通用get请求
    public static void doGet(final String url, final NewsCallBack callBack){
        new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String gsonStrings = response.body().string();
                    if (callBack != null){
                        callBack.onNewsReturn(gsonStrings);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}
