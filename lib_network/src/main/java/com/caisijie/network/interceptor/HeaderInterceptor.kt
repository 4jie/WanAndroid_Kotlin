package com.caisijie.network.interceptor

import com.caisijie.framework.utils.log.LogUtils
import com.caisijie.network.constant.ARTICLE_WEBSITE
import com.caisijie.network.constant.COIN_WEBSITE
import com.caisijie.network.constant.COLLECTION_WEBSITE
import com.caisijie.network.constant.KEY_COOKIE
import com.caisijie.network.constant.NOT_COLLECTION_WEBSITE
import com.caisijie.network.manager.CookiesManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author caisijie
 * @date 2023/9/18 8:57
 * @desc http头部信息拦截器
 * 添加头信息
 */
class HeaderInterceptor :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request()
        val newBuilder=request.newBuilder()
        newBuilder.addHeader("Content-type","application/json; charset=utf-8")

        val host=request.url().host()
        val url=request.url().toString()

        //给有需要的接口添加cookies
        if (!host.isNullOrEmpty()&&(url.contains(COLLECTION_WEBSITE)
                    ||url.contains(NOT_COLLECTION_WEBSITE)
                    ||url.contains(ARTICLE_WEBSITE)
                    ||url.contains(COIN_WEBSITE))){
            val cookies=CookiesManager.getCookies()
            LogUtils.e("HeaderInterceptor:cookies:$cookies",tag="sijie")
            if(!cookies.isNullOrEmpty()){
                newBuilder.addHeader(KEY_COOKIE,cookies)
            }
        }
        return chain.proceed(newBuilder.build())
    }
}