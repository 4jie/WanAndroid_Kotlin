package com.caisijie.framework.base

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.caisijie.framework.base.dialog.BaseDialog
import com.caisijie.framework.utils.log.LogUtils

class BaseDialogFragment : AppCompatDialogFragment() {
    private var mDialog: BaseDialog? = null

    companion object {
        private var sShowTag: String? = null
        private var sLastTime: Long = 0
    }

    /**
     * 父类同名方法的简化
     */
    fun show(fragment: Fragment?) {
        if (fragment != null && fragment.activity != null && !(fragment.requireActivity().isFinishing) && fragment.isAdded) {
            show(fragment.parentFragmentManager, fragment.javaClass.name)
        }
    }

    /**
     * 父类同名方法简化
     */
    fun show(activity: FragmentActivity?) {
        if (activity != null && !activity.isFinishing) {
            show(activity.supportFragmentManager, activity.javaClass.name)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!manager.isDestroyed) {
            if (!isRepeatedShow(tag)) {
                try {
                    super.show(manager, tag)
                } catch (e: Exception) {
                    LogUtils.e(e)
                }
            }
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (!isRepeatedShow(tag)) {
            try {
                return super.show(transaction, tag)
            } catch (e: Exception) {
                LogUtils.e(e)
            }
        }
        return -1
    }

    /**
     * 根据这个tag判断这个Dialog是否重复显示了
     */
    private fun isRepeatedShow(tag: String?): Boolean {
        val result = tag == sShowTag && SystemClock.uptimeMillis() - sLastTime < 500
        sShowTag = tag
        sLastTime = SystemClock.uptimeMillis()
        return result
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (mDialog != null) {
            //明确表示该值不应为空，如果为空，则会抛出 NullPointerException 异常。
            mDialog!!
        } else {
            //不使用Dialog，替换成BaseDialog对象
            //创建并初始化一个ｂａｓｅＤｉａｌｏｇ对象
            BaseDialog(requireActivity()).also { mDialog = it }
        }
    }

    override fun getDialog(): Dialog? {
        return if (mDialog != null) {
            mDialog
        } else super.getDialog()
    }

    fun setDialog(dialog: BaseDialog?) {
        mDialog = dialog
    }

    open class Builder<B : BaseDialog.Builder<B>>(
        /**
         * 获取当前Activity对象（仅供子类调用）
         */
        protected val activity: FragmentActivity
    ) : BaseDialog.Builder<B>(activity) {
        /**
         * 获取当前DialogFragment对象（仅供子类调用）
         */
        private var dialogFragment: BaseDialogFragment? = null

        /**
         * 获取Fragment标记
         */
        private val fragmentTag: String
            get() = javaClass.name

        /*
        // 重写父类的方法（仅供子类调用）
        @Override
        protected void dismiss() {
            try {
                mDialogFragment.dismiss();
            } catch (IllegalStateException e) {
                // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                // 这里不能调用 DialogFragment 的 dismiss 方法，因为在前台 show 之后却在后台 dismiss 会导致崩溃
                // 使用 Dialog 的 dismiss 方法却不会出现这种情况，除此之外没有更好的解决方案，故此这句 API 被注释
            }
        }
        */
        override fun show(): BaseDialog? {
            val dialog = create()
            try {
                dialogFragment = initDialogFragment()
                dialogFragment?.setDialog(dialog)
                if (!activity.isFinishing) {
                    dialogFragment?.show(activity.supportFragmentManager, fragmentTag)
                }
                //解决Dialog设置了而DialogFragment没有生效的问题
                dialogFragment?.isCancelable = isCancelable
            } catch (e: Exception) {
                LogUtils.e("@BaseDialog,弹窗show失败$e")
            }
            return dialog
        }

        private fun initDialogFragment(): BaseDialogFragment {
            return BaseDialogFragment()
        }
    }
}