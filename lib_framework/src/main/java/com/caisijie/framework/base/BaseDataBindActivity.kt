package com.caisijie.lib_framework.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.caisijie.lib_framework.ext.saveAs
import com.caisijie.lib_framework.ext.saveAsUnChecked
import java.lang.reflect.ParameterizedType

/**
 * @author caisijie
 * @date 2023/8/24
 * @description dataBinding Activity基类
 */
abstract class BaseDataBindActivity<DB : ViewBinding> : BaseActivity() {
    private lateinit var mBinding: DB

    override fun setContentLayout() {
        //获取当前类的直接超类的类型
        val type = javaClass.genericSuperclass
        //反射获取泛型参数DB的实际类型
        val vbClass: Class<DB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        //获取vbClass类中名为"inflate",参数类型为LayoutInflater的方法
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        //反射调用inflate方法，传入layoutInflater参数，并将结果赋值给mBinding属性
        mBinding = method.invoke(this, layoutInflater)!!.saveAsUnChecked()
        setContentView(mBinding.root)
    }

    override fun getLayoutResId(): Int = 0
}