package com.caisijie.lib_framework.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnCancelListener
import android.content.DialogInterface.OnClickListener
import android.content.DialogInterface.OnDismissListener
import android.content.DialogInterface.OnShowListener
import android.graphics.drawable.Drawable
import android.net.IpSecManager.ResourceUnavailableException
import android.os.Looper
import androidx.appcompat.app.AppCompatDialog
import com.caisijie.lib_framework.R
import android.os.Handler
import android.os.SystemClock
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.caisijie.lib_framework.utils.log.LogUtils.e

/**
 * @author caisijie
 * @date 2023/8/26
 */
class BaseDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.BaseDialogStyle
) :
    AppCompatDialog(context, if (themeResId > 0) themeResId else R.style.BaseDialogStyle),
    DialogInterface.OnShowListener,
    DialogInterface.OnCancelListener,
    DialogInterface.OnDismissListener {
    companion object {
        private val handler = Handler(Looper.getMainLooper())
    }

    private var mOnShowListeners: MutableList<OnShowListener?>? = null
    private var mOnCancelListeners: MutableList<OnCancelListener?>? = null
    private var mOnDismissListeners: MutableList<OnDismissListener?>? = null

    /**
     * 添加一个显示监听器
     * @param listener 监听器对象
     */
    fun addOnShowListener(listener: OnShowListener?) {
        if (mOnShowListeners == null) {
            mOnShowListeners = ArrayList()
            super.setOnShowListener(this)
        }
        mOnShowListeners?.add(listener)
    }

    /**
     * 添加一个取消监听器
     * @param listener 监听器对象
     */
    fun addOnCancelListener(listener: OnCancelListener?) {
        if (mOnCancelListeners == null) {
            mOnCancelListeners = ArrayList()
            super.setOnCancelListener(this)
        }
        mOnCancelListeners?.add(listener)
    }

    /**
     * 添加一个销毁监听器
     * @param listener 监听器对象
     */
    fun addOnDismissListener(listener: OnDismissListener?) {
        if (mOnDismissListeners == null) {
            mOnDismissListeners = ArrayList()
            super.setOnDismissListener(this)
        }
        mOnDismissListeners?.add(listener)
    }

    /**
     * 设置显示监听器集合
     */
    private fun setOnShowListener(listeners: MutableList<OnShowListener?>?) {
        super.setOnShowListener(this)
        mOnShowListeners = listeners
    }

    /**
     * 设置取消监听器集合
     */
    private fun setOnCancelListener(listeners: MutableList<OnCancelListener?>?) {
        super.setOnCancelListener(this)
        mOnCancelListeners = listeners
    }

    /**
     * 设置销毁监听器集合
     */
    private fun setDismissListener(listeners: MutableList<OnDismissListener?>?) {
        super.setOnDismissListener(this)
        mOnDismissListeners = listeners
    }

    override fun onShow(dialog: DialogInterface?) {
        try {
            mOnShowListeners?.let {
                //数组查找性能更好
                val objects: Array<*> = it.toTypedArray()
                for (obj in objects) {
                    if (obj is OnShowListener) {
                        obj.onShow(this)
                    }
                }
            }
        } catch (e: Exception) {
            e(e)
        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        try {
            mOnCancelListeners?.let {
                val objects: Array<*> = it.toTypedArray()
                for (obj in objects) {
                    if (obj is OnCancelListener) {
                        obj.onCancel(this)
                    }
                }
            }
        } catch (e: Exception) {
            e(e)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        try {
            //移除和这个Dialog相关的消息回调
            handler.removeCallbacksAndMessages(this)
            mOnDismissListeners?.let {
                val objects: Array<*> = it.toTypedArray()
                for (obj in objects) {
                    //判断监听器是否实现了这个接口
                    if (obj is OnDismissListener) {
                        obj.onDismiss(this)
                    }
                }
            }
        } catch (e: Exception) {
            e(e)
        }
    }

    /**
     * 延迟执行
     */
    fun post(r: Runnable): Boolean {
        return postDelayed(r, 0)
    }

    /**
     * 延迟一段时间执行
     */
    fun postDelayed(r: Runnable, delayMills: Long): Boolean {
        var millis = delayMills
        if (millis < 0) {
            millis = 0
        }
        //SystemClock.uptimeMillis():返回自启动以来不包括深度睡眠的毫秒数
        return postAtTime(r, SystemClock.uptimeMillis() + millis)
    }

    /**
     * 在指定的时间执行
     */
    fun postAtTime(r: Runnable, uptimeMillis: Long): Boolean {
        return handler.postAtTime(r, this, uptimeMillis)
    }

    interface onClickListener {
        fun onClick(dialog: BaseDialog?, view: View)
    }

    interface onShowListener {
        fun onShow(dialog: BaseDialog?)
    }

    interface onCancelListener {
        fun onCancel(dialog: BaseDialog?)
    }

    interface onDismissListener {
        fun onDismiss(dialog: BaseDialog?)
    }

    /**
     * Dialog动画样式
     */
    object AnimStyle {
        //缩放动画
        val SCALE = R.style.ScaleAnimStyle

        //IOS动画
        val IOS = R.style.IOSAnimStyle

        //Toast动画
        val TOAST = android.R.style.Animation_Toast

        //顶部弹出动画
        val TOP = R.style.TopAnimStyle

        //底部弹出动画
        val BOTTOM = R.style.BottomAnimStyle

        //左边弹出动画
        val LEFT = R.style.LeftAnimStyle

        //右边弹出动画
        val RIGHT = R.style.RightAnimStyle

        //默认动画效果
        val DEFAULT = R.style.ScaleAnimStyle
    }

    /**
     * Builder<B : Builder<B>>：这是类的名称和泛型类型参数的声明。
     * Builder 是类的名称，B 是泛型类型参数的名称。
     * <B : Builder<B>> 表示泛型类型参数 B 必须是 Builder<B> 类型或其子类。
     * 这种类型参数的约束称为递归类型参数（recursive type parameter），它允许在子类中使用正确的子类类型。
     * 就是设置泛型可选类型的边界，这类设置为Builder为上界，也就是只能使用同类或其子类，同java中的extends，也就是协变
     */
    open class Builder<B : Builder<B>>(protected val context: Context) {
        protected var mContentView: View? = null

        /**
         * 获取当前Dialog对象（仅供子类调用）
         */
        protected var dialog: BaseDialog? = null
            private set

        //Dialog Show监听
        private var mOnShowListeners: MutableList<onShowListener?>? = null

        private var mOnCancelListeners: MutableList<onCancelListener?>? = null

        private var mOnDismissListeners: MutableList<onDismissListener?>? = null

        //密钥监听器，回调将在密钥事件传递给对话框之前调用
        private var mOnKeyListener: DialogInterface.OnKeyListener? = null

        /**
         * 是否设置了取消（仅供子类调用）
         * 点击空白是否能够取消，默认点击阴影能够取消
         */
        protected var isCancelable = true
            //设置只能在类内部调用，也就是限制在子类该属性只读不能写
            private set

        //点击返回键时候能够取消
        private var mTouchOutside = true

        //稀疏数组主要用于存储索引不连续的数据，存储方式是键值对存储，有点类似于Map
        private val mTextArray = SparseArray<CharSequence>()
        private val mVisibilityArray = SparseIntArray()
        private val mBackgroundArray = SparseArray<Drawable>()
        private val mImageArray = SparseArray<Drawable>()
        private val mClickArray = SparseArray<OnClickListener>()

        //主题
        private var mThemeResId = -1

        //动画
        private var mAnimations = -1

        /**
         * 获取Dialog重心（仅供子类调用）
         */
        companion object {
            const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
            const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        //位置
        protected var gravity = Gravity.CENTER

        //宽度和高度
        private var mWidth = WRAP_CONTENT
        private var mHeight = WRAP_CONTENT

        //垂直边距和水平边距
        private var mVerticalMargin = 0
        private var mHorizontalMargin = 0

        /**
         * 延迟执行，一定要在创建了Dialog之后调用（供子类调用）
         */
        fun post(r: Runnable): Boolean {
            return postDelayed(r, 0)
        }

        protected fun post(r: Runnable): Boolean {
            //Elvis 运算符的备用值
            // 如果 dialog?.post(r) 表达式返回 null（即 dialog 为 null 或调用 post(r) 方法返回 null），那么整个表达式的结果将是 false。
            return dialog?.post(r) ?: false
        }

        /**
         * 延迟一段时间执行，一定要在创建了Dialog之后调用（仅供子类调用）
         */
        protected fun postDelayed(r: Runnable, delayMills: Long): Boolean {
            return dialog?.postDelayed(r, delayMills) ?: false
        }

        fun postDelayed(r: Runnable, delayMills: Long): Boolean {
            var millis = delayMills
            if (millis < 0) {
                millis = 0
            }
            return postAtTime(r, SystemClock.uptimeMillis())
        }

        /**
         * 在指定的时间执行
         */
        fun postAtTime(r: Runnable, uptimeMillis: Long): Boolean {
            return dialog?.postAtTime(r, uptimeMillis) ?: false
        }


        /**
         * 设置是否可以取消
         */
        fun setCancelable(cancelable: Boolean): B {
            isCancelable = cancelable
            return this as B
        }
    }

}