package com.lay.http;

import com.drinkfriends.framework.log.L;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "网络访问日志拦截器";

    @Override
    public Response intercept(Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();

        long t1 = System.nanoTime();//请求发起的时间
//        Log.i(TAG, "intercept: 请求地址||||||||"+request.url()+"/n请求头是||||||||"+request.headers()+"/n发起时间是|||||"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        L.i("请求地址 " + request.url()
                + "\n请求头是 " + request.headers()
                + "\n请求时间是 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + "\n请求体 : \n" + getBody(request)
        );

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();//收到响应的时间

        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一 
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
//        Log.i(TAG, "intercept: 响应地址|||||||"+ response.request().url()+"/n响应头是|||||||"+response.headers()+"/n响应体是|||||||"+responseBody.string()+"/n持续时间|||||"+(t2 - t1) / 1e6d);

        L.i("响应地址 " + response.request().url()
        + "\n响应头" + response.headers()
        + "\n持续时间是" + (t2 - t1) / 1e6d
        + "\n响应体 ： \n" + responseBody.string());
        return response;
    }

    private String getBody(Request request) {
        if ("POST".equals(request.method())) {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.name(i) + "=" + body.value(i) + ",");
                }
                if(sb.length() > 0) {
                    sb.delete(sb.length() - 1, sb.length());
                }
                return String.format("{%s}",sb.toString());
            }
        }
        return "";
    }
}