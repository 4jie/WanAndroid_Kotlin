package com.caisijie.framework.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager

/**
 * @author caisijie
 * @date 2023/9/18 9:25
 * @desc 网络请求工具类
 * 包括判断当前网络是否已连接，网络类型是几G，运营商等信息
 */
@SuppressLint("MissingPermission")
object NetworkUtil {
    /**
     * 网络可访问
     */
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            //只是判断有网络连接
            cm.activeNetworkInfo?.isConnected == true
        }
    }

    /**
     * 网络类型
     */
    fun getNetworkType(context: Context): NetworkType {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isWiFi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        } else {
            cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
        }
        if (isWiFi) {
            return NetworkType.NETWORK_WIFI
        }

        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return when (tm.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_GSM,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN -> NetworkType.NETWORK_2G

            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_HSPAP,
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> NetworkType.NETWORK_3G

            TelephonyManager.NETWORK_TYPE_LTE -> NetworkType.NETWORK_4G

            TelephonyManager.NETWORK_TYPE_IWLAN -> NetworkType.NETWORK_WIFI

            TelephonyManager.NETWORK_TYPE_NR -> NetworkType.NETWORK_5G

            else -> NetworkType.NETWORK_UNKNOWN

        }
    }

    /**
     * 获取运营商
     */
    fun getOperationName(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simOperatorName
    }

    /**
     * 判断是否是网络类型低于4代（4G）
     */
    fun isWeakNetwork(ctx: Context): Boolean {
        if (!isConnected(ctx)) {
            return true
        }
        val type = getNetworkType(ctx)
        return type != NetworkType.NETWORK_WIFI && type != NetworkType.NETWORK_4G && type != NetworkType.NETWORK_5G
    }
}

/**
 * 枚举网络类型
 * 枚举类型可以避免使用魔法数字，从而使代码更加易于理解。
 * 代码规范化：使用枚举类型可以将常量组织在一起，从而使代码更加规范化。从而使代码更易于理解和维护。
 * 总之使用枚举类型可以提高代码的可读性、可维护性、可扩展性和规范化程度。
 */
enum class NetworkType {
    NETWORK_UNKNOWN, NETWORK_WIFI, NETWORK_2G, NETWORK_3G, NETWORK_4G, NETWORK_5G
}