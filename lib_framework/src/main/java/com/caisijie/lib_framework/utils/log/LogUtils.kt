package com.caisijie.lib_framework.utils.log

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import com.caisijie.lib_framework.utils.DeviceInfoUtils

/**
 * 对{@link android.util.Log}做了再封装，现在可以打印的不只是字符串，还可以打印异常
 */
object LogUtils {
    //val只读，但是运行时有可能可以通过调用其他方法来修改常量值
    // const是编译时就能确定的常量，运行时不可修改，所以加上const会更安全
    private const val TAG = "LogUtils"
    var application: Application? = null

    private var isDebug = false;

    private var logger: XLogger = XLogger()

    private var mLogPath: String? = null

    fun init(
        application: Application,
        logPath: String,
        namePrefix: String = "SRM",
        isDebug: Boolean = false
    ) {
        LogUtils.application = application
        LogUtils.isDebug = isDebug
        mLogPath = logPath
        // logger.init(application, isDebug, logPath, namePrefix)
        // persist env info
        val metrics = DisplayMetrics()
        (application.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getMetrics(metrics)
        val envInfo = """
            *************************
            最终生成的字符串可能类似于：Samsung_Galaxy S20_1080x2340_31，其中包含了手机制造商、型号、屏幕分辨率和 Android SDK 版本等信息。
            ${DeviceInfoUtils.phoneManufacturer}_${DeviceInfoUtils.phoneModel}_${metrics.widthPixels}x${metrics.heightPixels}_${Build.VERSION.SDK_INT}
            *************************
        """.trimIndent()
        //        logger.e(TAG, envInfo)
    }

    @JvmOverloads
    fun v(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
        saveLog: Boolean = false
    ) {
        prepareLog(Log.VERBOSE, tag, message, throwable, saveLog)
    }

    @JvmOverloads
    fun d(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
        saveLog: Boolean = false
    ) {
        prepareLog(Log.DEBUG, tag, message, throwable, saveLog)
    }

    @JvmOverloads
    fun i(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
        saveLog: Boolean = false
    ) {
        prepareLog(Log.INFO, tag, message, throwable, saveLog)
    }

    @JvmOverloads
    fun w(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
        saveLog: Boolean = false
    ) {
        prepareLog(Log.WARN, tag, message, throwable, saveLog)
    }

    @JvmOverloads
    //@JvmOverloads 注解的作用是在编译时生成多个重载的方法
    // 每个重载方法都缺省一个或多个参数。
    // 这样，在 Java 代码中就不需要手动创建多个重载方法，而是可以直接使用默认参数值的 Kotlin 函数。
    fun e(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
        saveLog: Boolean = false
    ) {
        prepareLog(Log.ERROR, tag, message, throwable, saveLog)
    }

    //少了tag和msg参数的warn和error重载
    @JvmOverloads
    fun w(throwable: Throwable? = null, saveLog: Boolean = false) {
        prepareLog(Log.WARN, "", "", throwable, saveLog)
    }

    @JvmOverloads
    fun e(throwable: Throwable? = null, saveLog: Boolean = false) {
        prepareLog(Log.ERROR, "", "", throwable, saveLog)
    }

    private fun prepareLog(
        priority: Int,
        tag: String?,
        message: String,
        throwable: Throwable?,
        saveLog: Boolean
    ) {
        //?: 是 Elvis 运算符，也称为 null 合并运算符
        // 作用是判断 tag 是否为 null
        // 如果不为 null，则将 tag 的值赋给 logTag；
        // 如果 tag 为 null，则将 TAG的值赋给 logTag。
        val logTag = tag ?: TAG
        throwable?.let {
//            logger.logThrowable(logTag, it, message)
        } ?: run {
            // 按照日志级别打印，线上版本在warn及以上级别会打印到日志文件中
//            when (priority) {
//                Log.VERBOSE -> logger.v(logTag, message)
//                Log.DEBUG -> logger.d(logTag, message)
//                Log.INFO -> logger.i(logTag, message)
//                Log.WARN -> logger.w(logTag, message)
//                Log.ERROR -> logger.e(logTag, message)
//                else -> logger.v(logTag, message)
//            }
            when (priority) {
                Log.VERBOSE -> Log.v(logTag, message)
                Log.DEBUG -> Log.d(logTag, message)
                Log.INFO -> Log.i(logTag, message)
                Log.WARN -> Log.w(logTag, message)
                Log.ERROR -> Log.e(logTag, message)
                else -> Log.v(logTag, message)
            }
        }
    }

    /**
     * 将缓存中的日志刷新到文件里去
     */
    fun flushLog() {
//        logger.flushlog()
    }

    /**
     * 获取日志存放路径
     */
    fun getLogPath(): String? {
        return mLogPath
    }

}