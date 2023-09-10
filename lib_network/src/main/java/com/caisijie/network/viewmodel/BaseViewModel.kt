package com.caisijie.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caisijie.framework.utils.log.LogUtils
import com.caisijie.network.error.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author caisijie
 * @date 2023/9/7
 * @description viewModel基类
 */
open class BaseViewModel : ViewModel() {
    /**
     * 运行在主线程中，可直接调用
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    fun launchUI(errorBlock: (Int?, String?) -> Unit, responseBlock: suspend () -> Unit) {
        //Dispatchers.Main：创建的协程只运行在子线程
        viewModelScope.launch(Dispatchers.Main) {

        }
    }

    /**
     * 运行在协程作用域中
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    suspend fun <T> safeApiCall(errorBlock: suspend (Int?, String?) -> Unit,responseBlock:suspend()->T?):T?{
        try {
            return responseBlock()
        }catch (e:Exception){
            e.printStackTrace()
            LogUtils.e(e)
            val exception=ExceptionHandler.handleException(e)
        }
    }
}