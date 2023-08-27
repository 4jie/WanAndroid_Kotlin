package com.caisijie.lib_framework.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.caisijie.lib_framework.R
import com.caisijie.lib_framework.toast.TipsToast
import com.caisijie.lib_framework.utils.LoadingUtils

/**
 * @author caisijie
 * @Time 2023/8/23
 * @Description Activity基类
 */
abstract class BaseActivity : AppCompatActivity() {
    //protected作用域为同包下所有类以及不同包下子孙类可访问，这样在访问安全和访问范围之前做了折中
    protected val TAG: String? = this::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val dialogUtil by lazy {
        LoadingUtils(this)
    }

    open fun setContentLayout() {
        setContentView(getLayoutResId())
    }

    /**
     * 初始化视图
     *  仅用于不继承BaseDataBindingActivity类的的传递布局文件
     */
    abstract fun getLayoutResId(): Int

    /**
     * 初始化控件
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    open fun initData() {

    }

    /**
     * 加载中……弹窗
     */
    fun showLoading() {
        showLoading(getString(R.string.loading))
    }

    /**
     * 加载提示框
     */
    private fun showLoading(msg: String?) {
        dialogUtil.showLoading(msg)
    }

    /**
     * 加载提示框
     * @StringRes:表示一个整型参数、字段或方法返回值预计是一个String资源引用
     */
    fun showLoading(@StringRes res: Int) {
        showLoading(getString(res))
    }

    /**
     * 关闭提示框
     */
    fun dismissLoading() {
        dialogUtil.dismissLoading()
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