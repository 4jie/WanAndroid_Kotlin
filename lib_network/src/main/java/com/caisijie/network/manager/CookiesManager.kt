package com.caisijie.network.manager

import com.caisijie.common.constant.HTTP_COOKIES_INFO
import com.caisijie.framework.utils.log.LogUtils
import com.tencent.mmkv.MMKV
import java.lang.StringBuilder

/**
 * @author caisijie
 * @date 2023/9/18 0:09
 * @desc Cookies管理类
 */
object CookiesManager {
    /**
     * 保存cookies
     * @param cookies
     */
    fun saveCookies(cookies:String){
        val mmkv=MMKV.defaultMMKV()
        mmkv.encode(HTTP_COOKIES_INFO,cookies)
    }

    /**
     * 获取cookies
     * @return cookies
     */
    fun getCookies():String?{
        val mmkv=MMKV.defaultMMKV()
        return mmkv.decodeString(HTTP_COOKIES_INFO,"")
    }

    /**
     * 清除cookies:逻辑其实是调用了saveCookies传入参数空字符串
     * @param cookies
     */
    fun clearCookies(){
        saveCookies("")
    }
    /**
     * 解析cookies
     * @param cookies
     */
    fun encodeCookie(cookies:List<String>?):String{
        val sb=StringBuilder()
        val set=HashSet<String>()
        cookies?.map {
            cookies->cookies.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
            ?.forEach{
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }
        LogUtils.e("cookiesList:$cookies",tag="sijie")
        val ite=set.iterator()
        while (ite.hasNext()){
            val cookie=ite.next()
            sb.append(cookie).append(";")
        }
        val last=sb.lastIndexOf(";")
        if (sb.length-1==last){
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }
}