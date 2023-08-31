package com.caisijie.framework.loading

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.caisijie.framework.R
import com.caisijie.framework.databinding.DialogLoadingBinding

/**
 * 自定义一个通用等待加载中弹窗View
 */
class CenterLoadingView(context: Context, theme: Int) : Dialog(context, R.style.loading_dialog) {
    private var mBinding: DialogLoadingBinding

    private var animation: Animation? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mBinding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(mBinding.root)
        initAnim()
    }

    private fun initAnim() {
        animation = RotateAnimation(
            0f,
            360f,
            //自相关
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation?.duration = 2000
        animation?.repeatCount = 40
        animation?.fillAfter = true
    }

    override fun show() {
        super.show()
        mBinding.ivImage.startAnimation(animation)
    }

    /**
     * 关闭此对话框，将其从屏幕上移除。线程安全。
     * 当对话框被取消时，不应该重写此方法来执行清理，而是在onStop中实现它
     */
    override fun dismiss() {
        super.dismiss()
        mBinding.ivImage.clearAnimation()
    }

    override fun setTitle(title: CharSequence?) {
        if (!title.isNullOrEmpty()) {
            mBinding.tvMsg.text = title
        }
    }
}