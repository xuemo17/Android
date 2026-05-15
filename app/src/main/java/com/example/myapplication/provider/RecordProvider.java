package com.example.myapplication.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myapplication.database.RecordDatabaseHelper;

public class RecordProvider extends ContentProvider {

    // 定义Authority
    public static final String AUTHORITY = "com.example.myapplication.provider";

    // 定义URI路径
    public static final String PATH_RECORDS = "records";

    // 定义URI匹配码
    private static final int RECORDS = 1;
    private static final int RECORD_ID = 2;

    // 创建UriMatcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // 添加匹配规则
        uriMatcher.addURI(AUTHORITY, PATH_RECORDS, RECORDS);
        uriMatcher.addURI(AUTHORITY, PATH_RECORDS + "/#", RECORD_ID);
    }

    private RecordDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        // 初始化数据库Helper
        dbHelper = new RecordDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // 获取可读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        // 根据URI匹配结果执行不同的查询
        switch (uriMatcher.match(uri)) {
            case RECORDS:
                // 查询所有记录
                cursor = db.query(
                        RecordDatabaseHelper.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case RECORD_ID:
                // 查询单条记录
                String recordId = uri.getPathSegments().get(1);
                cursor = db.query(
                        RecordDatabaseHelper.TABLE_NAME,
                        projection,
                        RecordDatabaseHelper.COLUMN_ID + "=?",
                        new String[]{recordId},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new IllegalArgumentException("未知URI: " + uri);
        }

        // 设置通知URI，数据变化时通知观察者
        if (getContext() != null && cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // 只处理RECORDS类型的URI
        if (uriMatcher.match(uri) != RECORDS) {
            throw new IllegalArgumentException("无效的插入URI: " + uri);
        }

        // 获取可写数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 插入数据
        long id = db.insert(RecordDatabaseHelper.TABLE_NAME, null, values);

        // 构造返回的URI
        Uri resultUri = ContentUris.withAppendedId(uri, id);

        // 通知数据变化
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(resultUri, null);
        }

        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // 本示例简化，不实现删除操作
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                        @Nullable String[] selectionArgs) {
        // 本示例简化，不实现更新操作
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // 返回MIME类型
        switch (uriMatcher.match(uri)) {
            case RECORDS:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH_RECORDS;
            case RECORD_ID:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PATH_RECORDS;
            default:
                throw new IllegalArgumentException("未知URI: " + uri);
        }
    }
}