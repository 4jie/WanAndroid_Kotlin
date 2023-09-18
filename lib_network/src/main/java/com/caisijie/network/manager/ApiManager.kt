package com.caisijie.network.manager

import com.caisijie.network.api.ApiInterface

/**
 * @author caisijie
 * @date 2023/9/17
 * @desc API管理器
 */
object ApiManager {
    //使用 Retrofit 创建一个实现了 ApiInterface 接口的具体类的实例，
    // 该实例具备发送网络请求的能力并赋值给变量api
    val api by lazy { HttpManager.create(ApiInterface::class.java) }
}