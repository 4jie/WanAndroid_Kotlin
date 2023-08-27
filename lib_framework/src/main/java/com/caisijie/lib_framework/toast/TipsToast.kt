package com.caisijie.lib_framework.toast

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.caisijie.lib_framework.R
import com.caisijie.lib_framework.databinding.WidgetTipsToastBinding

/**
 * 自定义一个用于显示Toast的工具类
 */
object TipsToast {
    private var toast: Toast? = null

    private lateinit var mContext: Application

    /**
     * 如果Looper.myLooper()返回的结果不为null，则执行let中的代码块，也就是创建一个Handler对象
     * 并将前面myLooper()所获取到的当前线程的Looper对象传递给其构造方法
     * 这里使用Handler主要是为了延迟Toast创建的代码块执行，防止重复创建多个Toast
     */
    private val mToastHandler = Looper.myLooper()?.let { Handler(it) }

    /**
     * by lazy{...}起到一个惰性初始化的作用，在需要用到的时候才初始化mBinding
     * 并且后续访问mBinding是不会再创建，而是返回之前缓存的初始值，优化了性能和资源消耗
     */
    private val mBinding by lazy {
        WidgetTipsToastBinding.inflate(LayoutInflater.from(mContext), null, false)
    }

    fun init(context: Application) {
        mContext = context
    }

    fun showTips(msg: String?) {
        showTipsImpl(msg, Toast.LENGTH_SHORT)
    }

    fun showTips(@StringRes stringId: Int) {
        val msg = mContext.getString(stringId)
        showTipsImpl(msg, Toast.LENGTH_SHORT)
    }

    fun showTips(@StringRes stringId: Int, duration: Int) {
        val msg = mContext.getString(stringId)
        showTipsImpl(msg, duration)
    }

    fun showSuccessTips(msg: String?) {
        showTipsImpl(msg, Toast.LENGTH_SHORT, R.mipmap.widget_toast_success)
    }

    fun showSuccessTips(@StringRes stringId: Int) {
        val msg = mContext.getString(stringId)
        showTipsImpl(msg, Toast.LENGTH_SHORT, R.mipmap.widget_toast_success)
    }

    fun showWarningTips(msg: String?) {
        showTipsImpl(msg, Toast.LENGTH_SHORT, R.mipmap.widget_toast_warning)
    }

    fun showWarningTips(@StringRes stringId: Int) {
        val msg = mContext.getString(stringId)
        showTipsImpl(msg, Toast.LENGTH_SHORT, R.mipmap.widget_toast_warning)
    }

    /**
     * 先检查消息时候为空，不为空就检查之前是否已经存在一个Toast在显示，若有，取消之前的Toast
     * 再创建新的Toast，延迟50毫秒再执行该逻辑，可以避免快速连续连续调用该函数时出现Toast重叠显示的问题
     *  @DrawableRes：这类注解可以帮助开发者再编译时捕获一些常见的资源类型错误，例如将错误类型的资源传递给期望的参数
     * 打上@DrawableRes注解可以明确指定参数应该接受Drawable资源的ID
     * Int=0是默认值，假如调用函数没有提供drawableId参数的值将默认为0，也就是不提供
     */
    private fun showTipsImpl(msg: String?, duration: Int, @DrawableRes drawableId: Int = 0) {
        if (msg.isNullOrEmpty()) {
            return
        }
        toast?.let {
            //toast已经存在，调用cancel()取消当前Toast，并将Toast变量设为null，以便后续重新创建Toast
            cancel()
            toast = null
        }
        mToastHandler?.postDelayed({
            try {
                toast = Toast(mContext)
                toast?.view = mBinding.root
                mBinding.tipToastTxt.text = msg
                mBinding.tipToastTxt.setCompoundDrawablesWithIntrinsicBounds(
                    drawableId, 0, 0, 0
                )
                toast?.setGravity(Gravity.CENTER, 0, 0)
                toast?.duration = duration
                toast?.show()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("show tips error", "$e ")
            }
        }, 50)
    }

    private fun cancel() {
        toast?.cancel()
        mToastHandler?.removeCallbacksAndMessages(null)
    }
}