package com.caisijie.lib_framework.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.caisijie.lib_framework.ext.saveAs
import com.caisijie.lib_framework.ext.saveAsUnChecked
import java.lang.reflect.ParameterizedType

/**
 * @author caisijie
 * @date 2023/8/26
 */
abstract class BaseDataBindFragment<DB : ViewBinding> : BaseFragment() {
    //This property（属性） is only valid between onCreateView and onDestroyView
    var mBinding: DB? = null
    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
//        mBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        val type = javaClass.genericSuperclass
        val vbClass: Class<DB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        mBinding = method.invoke(this, layoutInflater)!!.saveAsUnChecked()
        return mBinding!!.root
    }

    override fun getLayoutResId(): Int = 0
    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}