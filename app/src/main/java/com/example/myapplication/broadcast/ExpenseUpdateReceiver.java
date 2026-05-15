package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.example.myapplication.constant.BroadcastConstants;

public class ExpenseUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 检查广播的 Action
        if (BroadcastConstants.ACTION_EXPENSE_UPDATED.equals(intent.getAction())) {
            // 从广播中获取总金额
            double totalAmount = intent.getDoubleExtra(BroadcastConstants.EXTRA_TOTAL_AMOUNT, 0);
            // 在 app 顶部显示提示
            Toast.makeText(context, "账目已更新，总金额：¥" + String.format("%.2f", totalAmount), Toast.LENGTH_SHORT).show();
        }
    }
}