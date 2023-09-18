package com.caisijie.network.error

import java.io.IOException

/**
 * @author caisijie
 * @date 2023/9/10
 * @description 结果异常类，服务器非200状态对应的异常
 */
open class ApiException : Exception {
    val errorCode: Int
    val errMsg: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        errorCode = error.code
        errMsg = error.errMsg
    }

    constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
        errorCode = code
        errMsg = msg
    }
}

/**
 * 无网络连接异常类
 */
class NoNetWorkException(error: ERROR, e: Throwable? = null) : IOException(e) {
    private val errCode: Int
    private val errMsg: String

    init {
        errCode = error.code
        errMsg = error.errMsg
    }
}