package com.caisijie.lib_framework.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.caisijie.lib_framework.toast.TipsToast
import com.caisijie.lib_framework.utils.LoadingUtils
import com.caisijie.lib_framework.utils.log.LogUtils

/**
 * @author caisijie
 * @date 2023/8/24
 * @description Fragment基类
 */
abstract class BaseFragment : Fragment() {
    protected val TAG: String? = this::class.java.simpleName
    protected var mIsViewCreate = false

    private val dialogUtils by lazy {
        LoadingUtils(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getContentView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIsViewCreate = true
        initView(view, savedInstanceState)
        initData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //手动切换首页tab时，先调用此方法设置fragment的可见性
        if (mIsViewCreate) {
            onFragmentVisible(isVisibleToUser)
        }
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            onFragmentVisible(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if (userVisibleHint) {
            onFragmentVisible(false)
        }
    }

    open fun onFragmentVisible(isVisibleToUser: Boolean) {
        LogUtils.w("onFragmentVisible-${TAG}-isVisibleToUser:$isVisibleToUser")
    }

    /**
     * 设置布局View
     * open关键字：在 Kotlin 中，默认情况下，类和类中的成员（方法、属性等）都是不可继承和重写的。
     * 为了允许子类对父类的函数进行重写，需要使用 open 关键字来修饰父类中的函数。
     */
    open fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(getLayoutResId(), null)
    }

    /**
     * 仅用于不继承BaseDataBindingFragment类的传递布局文件
     */
    abstract fun getLayoutResId(): Int

    open fun initData() {}

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 加载提示框
     */
    fun showLoading(msg: String?) {
        dialogUtils.showLoading(msg)
    }

    fun showLoading(@StringRes resId: Int) {
        showLoading(getString(resId))
    }

    /**
     * 关闭提示框
     */
    fun dismissLoading() {
        dialogUtils.dismissLoading()
    }

    /**
     * Toast
     */
    fun showToast(msg: String?) {
        TipsToast.showTips(msg)
    }

    fun showToast(@StringRes resId: Int) {
        TipsToast.showTips(resId)
    }

}