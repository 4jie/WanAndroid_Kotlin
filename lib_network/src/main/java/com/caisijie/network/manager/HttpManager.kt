package com.caisijie.network.manager

import android.util.Log
import com.caisijie.framework.helper.AppHelper
import com.caisijie.framework.utils.NetworkUtil
import com.caisijie.network.error.ERROR
import com.caisijie.network.constant.BASE_URL
import com.caisijie.network.error.NoNetWorkException
import com.caisijie.network.interceptor.CookiesInterceptor
import com.caisijie.network.interceptor.HeaderInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author caisijie
 * @date 2023/9/17
 * @desc 网络请求管理类，封装Retrofit的请求调用
 */
object HttpManager {
    private val mRetrofit: Retrofit

    init {
        mRetrofit = Retrofit.Builder()
            .client(initOkHttpClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 获取apiService
     * 使用 Retrofit创建一个实现了 ApiInterface 接口的具体类的实例
     * 该实例具备发送网络请求的能力
     */
    fun <T> create(apiService: Class<T>): T {
        return mRetrofit.create(apiService)
    }

    /**
     * 初始化okHttp
     */
    private fun initOkHttpClient(): OkHttpClient {
        val build = OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
        //添加参数拦截器
        val interceptors = mutableListOf<Interceptor>()
        build.addInterceptor(CookiesInterceptor())
        build.addInterceptor(HeaderInterceptor())

        //日志拦截器
        val logInterceptor = HttpLoggingInterceptor{
            message:String->Log.i("okhttp","data:$message")
        }
        if (AppHelper.isDebug()){
            logInterceptor.level=HttpLoggingInterceptor.Level.BODY
        }else{
            logInterceptor.level=HttpLoggingInterceptor.Level.BASIC
        }
        build.addInterceptor(logInterceptor)
        //网络状态拦截
        build.addInterceptor(object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                if (NetworkUtil.isConnected(AppHelper.getApplication())){
                    val request=chain.request()
                    return chain.proceed(request)
                }else{
                    throw NoNetWorkException(ERROR.NETWORK_ERROR)
                }
            }
        })
        return build.build()
    }
}