package com.caisijie.network.error

import android.util.MalformedJsonException
import com.caisijie.framework.toast.TipsToast
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLException

/**
 * @author caisijie
 * @date 2023/9/10
 * @description 统一处理异常的工具类
 */
object ExceptionHandler {
    fun handleException(e: Throwable): ApiException {
        val ex: ApiException
        when (e) {
            is ApiException -> {
                ex = ApiException(e.errorCode, e.errMsg, e)
                /*if (ex.errorCode == ERROR.UN_LOGIN.code) {
                    //登录失效
                }*/
            }

            is NoNetWorkException -> {
                TipsToast.showTips("网络异常，请尝试刷新")
                ex = ApiException(ERROR.NETWORK_ERROR, e)
            }

            is HttpException -> {
                ex = when (e.code()) {
                    ERROR.UNAUTHORIZED.code -> ApiException(ERROR.UNAUTHORIZED, e)
                    ERROR.FORBIDDEN.code -> ApiException(ERROR.FORBIDDEN, e)
                    ERROR.NOT_FOUND.code -> ApiException(ERROR.NOT_FOUND, e)
                    ERROR.REQUEST_TIMEOUT.code -> ApiException(ERROR.REQUEST_TIMEOUT, e)
                    ERROR.GATEWAY_TIMEOUT.code -> ApiException(ERROR.GATEWAY_TIMEOUT, e)
                    ERROR.INTERNAL_SERVER_ERROR.code -> ApiException(ERROR.INTERNAL_SERVER_ERROR, e)
                    ERROR.BAD_GATEWAY.code -> ApiException(ERROR.BAD_GATEWAY)
                    ERROR.SERVICE_UNAVAILABLE.code -> ApiException(ERROR.SERVICE_UNAVAILABLE, e)
                    else -> ApiException(e.code(), e.message(), e)
                }
            }

            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                ex = ApiException(ERROR.PARSE_ERROR, e)
            }

            is ConnectException -> {
                ex = ApiException(ERROR.NETWORK_ERROR, e)
            }

            is SSLException -> {
                ex = ApiException(ERROR.SSL_ERROR, e)
            }

            is SocketException, is SocketTimeoutException -> {
                ex = ApiException(ERROR.TIMEOUT_ERROR, e)
            }

            is UnknownHostException -> {
                ex = ApiException(ERROR.UNKNOWN_HOST, e)
            }

            else -> {
                ex = if (!e.message.isNullOrEmpty()) ApiException(1000, e.message!!, e)
                else ApiException(ERROR.UNKNOWN, e)
            }
        }
        return ex
    }
}