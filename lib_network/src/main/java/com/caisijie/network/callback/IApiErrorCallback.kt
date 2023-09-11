package com.caisijie.network.callback

import com.caisijie.framework.toast.TipsToast

/**
 * @author caisijie
 * @date 2023/9/10
 * @description 接口请求错误回调
 */
interface IApiErrorCallback {
    /**
     * 错误回调处理
     */
    fun onError(errorCode: Int?, errorMsg: String?) {
        TipsToast.showTips(errorMsg)
    }

    /**
     * 登录失效处理
     */
    fun onLoginFail(code: Int?, error: String?) {
        TipsToast.showTips(error)
    }
}