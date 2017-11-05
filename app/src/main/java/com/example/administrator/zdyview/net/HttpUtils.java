  package com.example.administrator.zdyview.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by peng on 2017/10/30.
 */

public class HttpUtils {
    //    private static HttpUtils httpUtils = new HttpUtils();//饿汉式，直接创建对象
//    public static HttpUtils getHttpUtils() {
//        return httpUtils;
//    }
    private static HttpUtils httpUtils;
    private final OkHttpClient client;

    private HttpUtils() {
        /*
        1.先创建OkHttpClient
        2.创建一个请求 Request
        3.发送请求
         */
        //1.先创建OkHttpClient
        client = new OkHttpClient();
    }

    public static HttpUtils getHttpUtils() {
        //线程安全
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    public void doGet(String url, final OnNetListener onNetListener) {
        // 2.创建一个请求 Request
        final Request request = new Request.Builder().url(url).build();
        //3.发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onNetListener.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                onNetListener.onSuccess(string);
            }
        });
    }

}
