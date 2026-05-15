package com.example.myapplication.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class NotificationHelper {

    // 通知渠道ID
    private static final String CHANNEL_ID = "expense_reminder_channel";
    // 通知渠道名称
    private static final String CHANNEL_NAME = "记账提醒";
    // 通知渠道描述
    private static final String CHANNEL_DESCRIPTION = "记账应用的提醒通知";
    // 通知ID
    private static final int NOTIFICATION_ID = 1001;

    /**
     * 初始化通知渠道
     * 适用于Android 8.0及以上版本
     */
    public static void createNotificationChannel(Context context) {
        // 检查Android版本是否为8.0或更高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建通知渠道
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            // 设置渠道描述
            channel.setDescription(CHANNEL_DESCRIPTION);
            // 设置通知灯光颜色
            channel.setLightColor(android.R.color.holo_blue_light);
            // 设置通知振动模式
            channel.enableVibration(true);
            // 获取通知管理器
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            // 注册通知渠道
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * 发送记账提醒通知
     */
    public static void sendExpenseReminderNotification(Context context) {
        // 创建通知意图，点击通知后跳转到MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        // 设置标志，确保每次点击通知都会打开一个新的活动实例
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // 创建PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // 构建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                // 设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                // 设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                // 设置通知标题
                .setContentTitle("记账提醒")
                // 设置通知内容
                .setContentText("今天还没有记录支出，点击立即记账")
                // 设置通知副标题
                .setSubText("养成良好的记账习惯")
                // 设置通知优先级
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // 设置点击通知后的跳转意图
                .setContentIntent(pendingIntent)
                // 设置点击通知后自动取消
                .setAutoCancel(true)
                // 设置通知声音
                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
                // 设置通知振动
                .setVibrate(new long[]{100, 200, 300, 400, 500})
                // 设置通知灯光
                .setLights(android.R.color.holo_blue_light, 1000, 1000)
                // 设置通知样式为大文本样式
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("今天还没有记录支出，点击立即记账\n养成良好的记账习惯，让理财变得更简单！"))
                // 添加操作按钮
                .addAction(R.mipmap.ic_launcher, "立即记账", pendingIntent);

        // 获取通知管理器
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 发送通知
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}