package com.caisijie.network.response

/**
 * @author caisijie
 * @date 2023/9/10
 * @description 通用数据类
 */
data class BaseResponse<out T>(
    val data: T?,
    val errorCode: Int = 0,//服务器状态码，0表示请求成功
    val errorMsg: String = ""//错误消息
) {
    /**
     * 判定接口返回是否正常
     */
    fun isFailed(): Boolean {
        return errorCode != 0
    }
}