package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.RecordAdapter;
import com.example.myapplication.broadcast.ExpenseUpdateReceiver;
import com.example.myapplication.constant.BroadcastConstants;
import com.example.myapplication.database.RecordDatabaseHelper;
import com.example.myapplication.notification.NotificationHelper;
import com.example.myapplication.service.ExpenseMonitorService;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecordDatabaseHelper dbHelper;
    private EditText amountEditText;
    private Spinner categorySpinner;
    private Button addButton, queryButton, sendNotificationButton, backgroundServiceButton;
    private TextView totalAmountText, serviceStatusText;
    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private String[] categories = {"餐饮", "交通", "购物", "娱乐", "其他"};
    private String selectedCategory;
    private ExpenseUpdateReceiver expenseUpdateReceiver;
    private boolean isServiceRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库Helper
        dbHelper = new RecordDatabaseHelper(this);

        // 初始化通知渠道
        NotificationHelper.createNotificationChannel(this);

        // 初始化UI组件
        amountEditText = findViewById(R.id.amount_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        addButton = findViewById(R.id.add_button);
        queryButton = findViewById(R.id.query_button);
        sendNotificationButton = findViewById(R.id.send_notification_button);
        backgroundServiceButton = findViewById(R.id.background_service_button);
        totalAmountText = findViewById(R.id.total_amount_text);
        serviceStatusText = findViewById(R.id.service_status_text);
        recyclerView = findViewById(R.id.record_list);

        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 设置分类Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // 监听分类选择
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = categories[0]; // 默认选择第一个
            }
        });

        // 添加记录按钮点击事件
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });

        // 查询记录按钮点击事件
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryRecords();
                updateTotalAmount();
                // 发送账目更新广播
                sendExpenseUpdatedBroadcast();
            }
        });

        // 发送通知按钮点击事件
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.sendExpenseReminderNotification(MainActivity.this);
                Toast.makeText(MainActivity.this, "记账提醒通知已发送", Toast.LENGTH_SHORT).show();
            }
        });

        // 后台服务按钮点击事件
        backgroundServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceRunning) {
                    // 停止Service
                    stopService(new Intent(MainActivity.this, ExpenseMonitorService.class));
                    isServiceRunning = false;
                    backgroundServiceButton.setText("启动后台服务");
                    serviceStatusText.setText("后台任务已停止");
                    Toast.makeText(MainActivity.this, "后台服务已停止", Toast.LENGTH_SHORT).show();
                } else {
                    // 启动Service
                    startService(new Intent(MainActivity.this, ExpenseMonitorService.class));
                    isServiceRunning = true;
                    backgroundServiceButton.setText("停止后台服务");
                    serviceStatusText.setText("后台任务已启动");
                    Toast.makeText(MainActivity.this, "后台服务已启动", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 动态注册广播接收器
        registerExpenseUpdateReceiver();

        // 应用启动时自动读取总消费金额和记录
        updateTotalAmount();
        queryRecords();
    }

    /**
     * 注册广播接收器
     */
    private void registerExpenseUpdateReceiver() {
        expenseUpdateReceiver = new ExpenseUpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConstants.ACTION_EXPENSE_UPDATED);
        registerReceiver(expenseUpdateReceiver, filter);
    }

    /**
     * 添加一条记录到数据库
     */
    private void addRecord() {
        // 获取输入内容
        String amountStr = amountEditText.getText().toString().trim();

        // 检查输入是否为空
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }

        // 转换金额为double类型
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取可写数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 创建ContentValues对象，存储要插入的数据
        ContentValues values = new ContentValues();
        values.put(RecordDatabaseHelper.COLUMN_NAME, selectedCategory);
        values.put(RecordDatabaseHelper.COLUMN_AMOUNT, amount);

        // 插入数据到表中
        long id = db.insert(RecordDatabaseHelper.TABLE_NAME, null, values);

        // 关闭数据库连接
        db.close();

        // 显示插入结果
        if (id != -1) {
            Toast.makeText(this, "记录添加成功", Toast.LENGTH_SHORT).show();
            // 清空输入框
            amountEditText.setText("");
            // 添加消费后更新总消费金额
            updateTotalAmount();
            // 实时更新消费记录显示
            queryRecords();
        } else {
            Toast.makeText(this, "记录添加失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 发送账目更新广播
     */
    private void sendExpenseUpdatedBroadcast() {
        // 获取总金额
        double totalAmount = getTotalAmount();
        // 创建广播Intent
        Intent intent = new Intent(BroadcastConstants.ACTION_EXPENSE_UPDATED);
        // 添加额外数据
        intent.putExtra(BroadcastConstants.EXTRA_TOTAL_AMOUNT, totalAmount);
        // 发送广播
        sendBroadcast(intent);
    }

    /**
     * 获取总消费金额
     */
    private double getTotalAmount() {
        // 获取可读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // 执行查询，计算总金额
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + RecordDatabaseHelper.TABLE_NAME, null);
        double totalAmount = 0;
        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }
        // 关闭Cursor和数据库连接
        cursor.close();
        db.close();
        return totalAmount;
    }

    /**
     * 查询所有记录并显示在RecyclerView中
     */
    private void queryRecords() {
        // 获取可读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 定义要查询的列
        String[] projection = {
                RecordDatabaseHelper.COLUMN_ID,
                RecordDatabaseHelper.COLUMN_NAME,
                RecordDatabaseHelper.COLUMN_AMOUNT
        };

        // 执行查询，获取Cursor对象，按id降序排列（最新的在前面）
        Cursor cursor = db.query(
                RecordDatabaseHelper.TABLE_NAME,  // 表名
                projection,  // 要查询的列
                null,        // WHERE子句
                null,        // WHERE子句的参数
                null,        // GROUP BY子句
                null,        // HAVING子句
                RecordDatabaseHelper.COLUMN_ID + " DESC"         // ORDER BY子句，按id降序排列
        );

        // 转换Cursor数据为RecordItem列表
        List<RecordAdapter.RecordItem> recordList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(RecordDatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(RecordDatabaseHelper.COLUMN_NAME));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(RecordDatabaseHelper.COLUMN_AMOUNT));
            recordList.add(new RecordAdapter.RecordItem(id, name, amount));
        }

        // 关闭Cursor
        cursor.close();
        // 关闭数据库连接
        db.close();

        // 创建适配器并设置到RecyclerView
        adapter = new RecordAdapter(this, recordList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 更新总消费金额
     */
    private void updateTotalAmount() {
        // 获取可读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 执行查询，计算总金额
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + RecordDatabaseHelper.TABLE_NAME, null);

        double totalAmount = 0;
        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }

        // 更新总金额显示
        totalAmountText.setText(String.format("¥%.2f", totalAmount));

        // 关闭Cursor和数据库连接
        cursor.close();
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播接收器
        if (expenseUpdateReceiver != null) {
            unregisterReceiver(expenseUpdateReceiver);
        }
        // 关闭数据库连接
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}