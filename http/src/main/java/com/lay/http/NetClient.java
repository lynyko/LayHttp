package com.lay.http;

import android.os.Handler;
import android.os.Looper;

import com.lay.http.core.DownloadRequest;
import com.lay.http.core.GetRequest;
import com.lay.http.core.PostRequest;
import com.lay.http.core.RequestBody;
import com.lay.http.core.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetClient {
    private final int DEF_TIME_OUT = 30000;
    private final int MAX_TASK = 64;
    public ThreadPoolExecutor executor;
    public List<AsyncTask> runningTaskList = new LinkedList<>();
    public List<AsyncTask> readyTaskList = new LinkedList<>();
    private List<Intercetor> intercetorList = new ArrayList<>();
    private int connectTimeout = DEF_TIME_OUT;
    private int readTimeout = DEF_TIME_OUT;

    private static NetClient client = new NetClient();
    public static NetClient getClient(){
        return client;
    }

    public void init(){
        if(executor == null){
            executor = new ThreadPoolExecutor(1, MAX_TASK, 60,
                    TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        }
    }

    public void setConnectTimeout(int timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        connectTimeout = timeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        readTimeout = timeout;
    }

    public void addIntercetor(Intercetor intercetor){
        intercetorList.add(intercetor);
    }

    public void sendAsync(RequestBody requestBody){
        if(requestBody.getConnectTimeout() == 0){
            requestBody.setConnectTimeout(connectTimeout);
        }
        if(requestBody.getReadTimeout() == 0){
            requestBody.setReadTimeout(readTimeout);
        }
        for(Intercetor intercetor : intercetorList) {
            requestBody.addIntercetor(intercetor);
        }
        requestBody.setH(new Handler());
        AsyncTask task = new AsyncTask(requestBody, true);
        if(runningTaskList.size() >= MAX_TASK){
            readyTaskList.add(task);
        } else {
            runningTaskList.add(task);
            executor.execute(task);
        }
    }

    public void sendSync(RequestBody requestBody){
        if(requestBody.getConnectTimeout() == 0){
            requestBody.setConnectTimeout(connectTimeout);
        }
        if(requestBody.getReadTimeout() == 0){
            requestBody.setReadTimeout(readTimeout);
        }
        for(Intercetor intercetor : intercetorList) {
            requestBody.addIntercetor(intercetor);
        }
        AsyncTask task = new AsyncTask(requestBody, false);
        if(Looper.getMainLooper() == Looper.myLooper()){
            throw new IllegalArgumentException("不能运行在主线程");
        }
        task.run();
    }

    public RequestBody newGetRequest(String url){
        RequestBody requestBody = new GetRequest(url);
        requestBody.setConnectTimeout(connectTimeout);
        requestBody.setReadTimeout(readTimeout);
        return requestBody;
    }

    public RequestBody newPostRequest(String url){
        RequestBody requestBody = new PostRequest(url);
        requestBody.setConnectTimeout(connectTimeout);
        requestBody.setReadTimeout(readTimeout);
        return requestBody;
    }

    public RequestBody newDownloadRequest(String url){
        RequestBody requestBody = new DownloadRequest(url);
        requestBody.setConnectTimeout(connectTimeout);
        requestBody.setReadTimeout(readTimeout);
        return requestBody;
    }

    class AsyncTask implements Runnable {
        RequestBody requestBody;
        long startTime;
        boolean async;
        public AsyncTask(RequestBody requestBody, boolean async){
            this.requestBody = requestBody;
            startTime = System.currentTimeMillis();
            this.async = async;
        }

        @Override
        public void run() {
            final ResponseBody response = requestBody.process();
            if (requestBody.responseListener != null) {
                if(requestBody.getH() != null){
                    requestBody.getH().post(new Runnable() {
                        @Override
                        public void run() {
                            requestBody.responseListener.response(response);
                        }
                    });
                } else {
                    requestBody.responseListener.response(response);
                }
            }
            if(async) {
                runningTaskList.remove(this);
                for (; ; ) {
                    if (runningTaskList.size() >= MAX_TASK || readyTaskList.size() == 0) {
                        break;
                    }
                    AsyncTask task = readyTaskList.remove(0);
                    runningTaskList.add(task);
                    executor.execute(task);
                }
            }
        }
    }
}