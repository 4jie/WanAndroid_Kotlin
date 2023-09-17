package com.caisijie.network.manager

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author caisijie
 * @date 2023/9/17
 * @desc 网络请求管理类，封装Retrofit的请求调用
 */
object HttpManager {
    private val mRetrofit:Retrofit

    init {
        mRetrofit=Retrofit.Builder()
            .client()
    }


    /**
     * 初始化okHttp
     */
    private fun initOkHttpClient():OkHttpClient{
        val build=OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12,TimeUnit.SECONDS)
            .readTimeout(12,TimeUnit.SECONDS)
        //添加参数拦截器
        val interceptors= mutableListOf<Interceptor>()
        build.addInterceptor(Cookies)
    }
}