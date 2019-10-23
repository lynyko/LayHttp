package com.lay.http;

import com.lay.http.BaseRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetClient {
    private static final int MAX_TASK = 64;
    public static ThreadPoolExecutor executor;
    public static List<AsyncTask> runningTaskList = new LinkedList<>();
    public static List<AsyncTask> readyTaskList = new LinkedList<>();
    public static void init(){
        if(executor == null){
            executor = new ThreadPoolExecutor(1, MAX_TASK, 60,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        }
    }

    public void dispatch(BaseRequest request){
        AsyncTask task = new AsyncTask(request);
        if(runningTaskList.size() >= MAX_TASK){
            readyTaskList.add(task);
        } else {
            runningTaskList.add(task);
            executor.execute(task);
        }
    }

    class AsyncTask implements Runnable {
        BaseRequest request;
        public AsyncTask(BaseRequest request){
            this.request = request;
        }

        @Override
        public void run() {
            request.send();
            runningTaskList.remove(request);
            for(;;){
                if(runningTaskList.size() >= MAX_TASK || readyTaskList.size() == 0){
                    break;
                }
                AsyncTask task = readyTaskList.remove(0);
                runningTaskList.add(task);
                executor.execute(task);
            }
        }
    }
}
