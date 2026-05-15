# 我的账本 - Android 消费记录 App

## 项目简介

「我的账本」是一款面向学生群体的简单消费记录工具，帮助用户方便地记录日常支出、查看消费记录，并统计总消费金额。应用界面简洁美观，适合 Android 初学者学习和参考。

## 主要功能

| 功能 | 说明 |
|------|------|
| **添加消费** | 输入金额并选择分类（餐饮、交通、购物、娱乐、其他），一键记录支出 |
| **查询记录** | 查看所有消费记录，最新记录排在前面，实时更新总消费金额 |
| **记账提醒** | 通过系统通知发送记账提醒，点击通知可跳转回应用 |
| **后台服务** | 启动后台 Service，模拟定时任务（每隔 2 秒输出日志） |
| **广播通知** | 查询记录时通过 BroadcastReceiver 发送账目更新广播 |
| **数据持久化** | 使用 SQLite 数据库存储消费记录，应用重启数据不丢失 |
| **文件存储** | 支持将数据保存到内部存储文件并读取显示 |

## 使用技术

| 技术 | 说明 |
|------|------|
| **开发语言** | Java |
| **开发工具** | Android Studio |
| **最低 SDK** | Android 10 (API 29) |
| **目标 SDK** | Android 14 (API 34) |
| **UI 框架** | Material Design 3（Material3 Light 主题） |
| **布局方式** | XML 布局 + CoordinatorLayout + NestedScrollView |
| **列表组件** | RecyclerView + 自定义 Adapter |
| **数据存储** | SQLite + SQLiteOpenHelper |
| **组件通信** | BroadcastReceiver（自定义广播） |
| **后台任务** | Service + Handler + Runnable |
| **系统通知** | NotificationManager + NotificationChannel + PendingIntent |
| **数据共享** | ContentProvider + ContentResolver |
| **文件操作** | FileOutputStream + FileInputStream（内部存储） |

## 项目结构

```
MyApplication/
├── app/
│   ├── build.gradle.kts            # 应用级构建配置
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml  # 应用清单（Activity、Service、Provider注册）
│   │       ├── java/com/example/myapplication/
│   │       │   ├── MainActivity.java           # 主活动（UI界面 + 业务入口）
│   │       │   ├── adapter/
│   │       │   │   ├── RecordAdapter.java      # 消费记录列表适配器
│   │       │   │   └── TransactionAdapter.java # 交易列表适配器
│   │       │   ├── broadcast/
│   │       │   │   └── ExpenseUpdateReceiver.java  # 账目更新广播接收器
│   │       │   ├── constant/
│   │       │   │   └── BroadcastConstants.java # 广播常量（Action字符串）
│   │       │   ├── database/
│   │       │   │   └── RecordDatabaseHelper.java   # SQLite数据库Helper
│   │       │   ├── model/
│   │       │   │   └── Transaction.java        # 交易数据模型
│   │       │   ├── notification/
│   │       │   │   └── NotificationHelper.java # 通知管理工具类
│   │       │   ├── provider/
│   │       │   │   └── RecordProvider.java     # ContentProvider（数据访问接口层）
│   │       │   ├── repository/
│   │       │   │   └── TransactionRepository.java  # 数据仓库
│   │       │   ├── service/
│   │       │   │   └── ExpenseMonitorService.java   # 后台监控Service
│   │       │   └── viewmodel/
│   │       │       └── ExpenseViewModel.java   # 视图模型
│   │       └── res/
│   │           ├── drawable/           # 自定义drawable资源（渐变、圆角、卡片背景等）
│   │           ├── layout/
│   │           │   ├── activity_main.xml   # 主页面布局
│   │           │   ├── item_record.xml     # 消费记录列表项布局
│   │           │   └── item_transaction.xml    # 交易列表项布局
│   │           ├── mipmap-*/             # 应用图标
│   │           └── values/
│   │               ├── colors.xml         # 颜色配置
│   │               ├── strings.xml        # 字符串资源
│   │               └── themes.xml         # 主题配置
├── build.gradle.kts                 # 项目级构建配置
└── README.md                        # 项目说明文档
```

## 运行方式

### 前提条件
- 安装 [Android Studio](https://developer.android.com/studio)（推荐最新稳定版）
- Android SDK Platform 34 及以上
- Java JDK 8 或更高版本

### 步骤
1. **克隆或下载项目**
   ```bash
   git clone <仓库地址>
   ```
   或者直接打开 `d:\yidongyy\MyApplication` 目录。

2. **用 Android Studio 打开项目**
   - 启动 Android Studio
   - 点击 `File → Open...`
   - 选择项目根目录 `MyApplication`
   - 等待 Gradle 同步完成

3. **创建虚拟设备（AVD）**
   - 点击工具栏 AVD Manager 图标
   - 点击 `Create Virtual Device`
   - 选择设备型号（推荐 Pixel 6）
   - 选择系统镜像（推荐 API 34）
   - 完成创建

4. **运行应用**
   - 在工具栏选择已创建的虚拟设备
   - 点击绿色 ▶ 运行按钮
   - 或使用快捷键 `Shift + F10`

5. **使用应用**
   - 在金额输入框输入消费金额
   - 从下拉菜单选择消费分类
   - 点击 **添加消费** 记录支出
   - 点击 **查询记录** 查看所有记录并发送广播
   - 点击 **记账提醒** 发送系统通知
   - 点击 **启动后台服务** 开启后台定时任务

### 命令行构建（可选）
```bash
# Windows
.\gradlew.bat assembleDebug

# macOS / Linux
./gradlew assembleDebug
```
APK 文件生成在 `app/build/outputs/apk/debug/app-debug.apk`。

6.**开发过程**
本项目使用 Git 进行版本管理，主要提交记录包括：
1. 提交GitHub项目；
2. 优化按钮与列表的ui；
3. 实现app的整体背景优化；
4. 建立 README。
5.修复并完善README

7.**作者信息**
姓名：黄浩忠
班级：23数字媒体技术01
学号：23120031033
