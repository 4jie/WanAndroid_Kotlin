package com.caisijie.network.flow

import com.caisijie.framework.utils.log.LogUtils
import com.caisijie.network.error.ApiException
import com.caisijie.network.error.ExceptionHandler
import com.caisijie.network.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeout

/**
 * @author caisijie
 * @date 2023/9/10
 * @description flow请求扩展
 */

/**
 * 通过flow执行请求，需要在协程作用域中执行
 * @param errorBlock 错误回调
 * @param requestCall 执行的请求
 * @param showLoading 开启和关闭加载框
 */
suspend fun <T> requestFlow(
    errorBlock: ((Int?, String?) -> Unit)? = null,
    requestCall: suspend () -> BaseResponse<T>?,
    showLoading: ((Boolean) -> Unit)? = null
): T? {
    var data: T? = null
    val flow = requestFlowResponse(errorBlock, requestCall,showLoading)
    //7.调用collect获取emit()回调的结果，就是请求最后的结果
    flow.collect{
        data=it?.data
    }
    return data
}

/**
 * 通过flow执行请求，需要在协程作用域中执行
 * @param errorBlock 错误回调
 * @param requestCall 执行的请求
 * @param showLoading 开启和关闭加载框
 */
suspend fun <T> requestFlowResponse(
    errorBlock: ((Int?, String?) -> Unit)? = null,
    requestCall: suspend () -> BaseResponse<T>?,
    showLoading: ((Boolean) -> Unit)? = null
): Flow<BaseResponse<T>?>{
    //1.执行请求
    val flow = flow {
        //设置超时时间
        val response = withTimeout(10 * 1000) {
            requestCall()
        }

        if (response?.isFailed() == true) {
            throw ApiException(response.errorCode, response.errorMsg)
        }
        //2.发送网络请求结果回调
        emit(response)
        //3.指定运行的线程，flow{}执行的线程
    }.flowOn(Dispatchers.IO)
        .onStart {
            //4.请求开始，展示加载框
            showLoading?.invoke(true)
        }
        //5.捕获异常
        .catch { e ->
            e.printStackTrace()
            LogUtils.e(e)
            val exception=ExceptionHandler.handleException(e)
            errorBlock?.invoke(exception.errorCode,exception.errMsg)
        }
    //6.请求完成，包括成功和失败
        .onCompletion {
            showLoading?.invoke(false)
        }
    return flow
}