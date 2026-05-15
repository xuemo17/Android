package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ExpenseMonitorService extends Service {

    private static final String TAG = "ExpenseMonitorService";
    private Handler handler;
    private Runnable task;
    private int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service 创建");
        // 初始化 Handler 和后台任务
        handler = new Handler();
        task = new Runnable() {
            @Override
            public void run() {
                // 模拟后台任务：输出日志
                counter++;
                Log.d(TAG, "后台任务执行中，次数：" + counter);
                // 每隔2秒执行一次
                handler.postDelayed(this, 2000);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service 启动");
        // 开始执行后台任务
        handler.post(task);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service 销毁");
        // 停止后台任务
        if (handler != null && task != null) {
            handler.removeCallbacks(task);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 本示例使用的是无绑定的 Service
        return null;
    }
}