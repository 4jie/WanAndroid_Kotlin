package com.caisijie.framework.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.caisijie.framework.utils.log.LogUtils
import java.io.FileInputStream
import java.net.NetworkInterface

/**
 * 设备信息的单例工具类
 */
object DeviceInfoUtils {
    private lateinit var appContext: Context

    //设备imei号
    var imei: String = ""
        //通过将 setter 方法设置为私有，外部代码无法直接修改 imei 的值，
        // 只能在类内部进行修改。这种设置可以控制对变量的赋值行为，
        // 使其只能在类内部进行，并提供了对变量的封装和保护。
        private set
    var imsi: String = ""
        private set

    //androidId安卓版本号
    var androidId: String = ""
        private set

    //设备MAC地址
    var mac: String = ""
        private set

    //WiFi mac地址
    var wifiMacAddress: String = ""
        private set

    //WiFi ssid号
    var wifiSSID: String = ""
        private set

    //最终产品的用户可见名称
    var phoneModel: String = ""
        private set

    //与产品硬件相关的品牌
    var phoneBrand: String = ""
        private set

    //生产硬件的制造商
    var phoneManufacturer: String = ""
        private set

    //设备
    var phoneDevice: String = ""
        private set

    fun init(context: Context) {
        appContext = context.applicationContext
        initDeviceInfo()
    }

    /**
     * 刷新设备信息
     */
    private fun initDeviceInfo() {
        initImei()
        initMac()
        initAndroidId()
        phoneModel = Build.MODEL
        phoneBrand = Build.BRAND
        phoneManufacturer = Build.MANUFACTURER
        phoneDevice = Build.DEVICE
    }

    /**
     * 初始化imei、imsi
     */
    @SuppressLint("HardwareIds")
    private fun initImei() {
        if (imei.isNotEmpty() || imsi.isNotEmpty()) {
            return
        }
        if (appContext.packageManager.checkPermission(
                android.Manifest.permission.READ_PHONE_STATE, appContext.packageName
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        try {
            LogUtils.d("init device imei and imsi")
            val tm= appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tmpImei=tm.deviceId
            if(tmpImei.isNotEmpty()){
                imei=tmpImei.lowercase()
            }
            val tmpImsi=tm.subscriberId
            if(!tmpImsi.isNullOrEmpty()){
                imsi=tmpImsi.lowercase()
            }
        }catch (e:Exception){
            LogUtils.d( "initImei exception,msg=${e.message}")
        }
    }

    private fun initMac() {
        if (mac.isNotEmpty() || wifiMacAddress.isNotEmpty() || wifiSSID.isNotEmpty()) {
            return
        }
        try {
            LogUtils.d("init device max,wifiMac and ssid")
            val wm =
                appContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            initMacAddress(wm)
            if (!wm.connectionInfo.bssid.isNullOrEmpty()) {
                wifiMacAddress = wm.connectionInfo.bssid
            }
            if (!wm.connectionInfo.ssid.isNullOrEmpty()) {
                wifiSSID = wm.connectionInfo.ssid
            }
        } catch (e: Exception) {
            LogUtils.d("init mac failure", throwable = e)
        }
    }

    private fun initMacAddress(wm: WifiManager) {
        try {
            //6.0-7.0读取设备文件获取
            val arrString = arrayOf(
                "/sys/class/net/wlan0/address",
                "/sys/devices/virtual/net/wlan0/address"
            )
            for (str in arrString) {
                mac = readFile(str)
                break
            }
            if (mac.isNotEmpty()) {
                return
            }
            //7.0及以上通过以下方式获取
            val interfaces = NetworkInterface.getNetworkInterfaces() ?: return
            while (interfaces.hasMoreElements()) {
                val netInfo = interfaces.nextElement()
                if ("wlan0" == netInfo.name) {
                    val addresses = netInfo.hardwareAddress
                    if (addresses == null || addresses.isEmpty()) {
                        continue
                    }
                    mac = macByte2String(addresses)
                    break
                }
            }
        } catch (e: Exception) {
            LogUtils.d("read mac failure", throwable = e)
        }
    }

    @SuppressLint("HardwareIds")
    private fun initAndroidId() {
        if (androidId.isNotEmpty()) {
            return
        }
        LogUtils.d("init device androidId")
        val tmpAndroidId = Settings.Secure.getString(
            appContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        if (!tmpAndroidId.isNullOrEmpty()) {
            androidId = tmpAndroidId.lowercase()
        }
    }

    /**
     * 将mac的byte数组转化为string
     */
    private fun macByte2String(bytes: ByteArray): String {
        val buf = StringBuffer()
        for (b in bytes) {
            //%02X是一个格式化字符串，其中 %02X 是格式化指令的一部分。
            // 其中 %X 表示将整数参数格式化为大写的十六进制数，而 02表示输出的十六进制数占两位，不足两位时在前面补零。
            //:：这是一个普通字符，表示在格式化后的字符串末尾添加一个冒号。
            //因此，String.format("%02X:", b) 将整数 b 格式化为一个两位的大写十六进制字符串，并在末尾添加一个冒号。例如，如果 b 的值为 10，那么格式化后的字符串将为 "0A:"。
            buf.append(String.format("%02X:", b))
        }
        if (buf.isNotEmpty()) {
            //删除最末尾的冒号:
            buf.deleteCharAt(buf.length - 1)
        }
        return buf.toString()
    }

    /**
     * 从指定文件中读取内容
     */
    private fun readFile(filePath: String): String {
        var res = ""
        var fin: FileInputStream? = null
        try {
            fin = FileInputStream(filePath)
            val length = fin.available()
            val buffer = ByteArray(length)
            val count = fin.read(buffer)
            if (count > 0) {
                res = String(buffer, Charsets.UTF_8)
            }
            // Kotlin 中的异常处理不是强制性的，但为了确保程序的健壮性和可靠性
            // 建议在可能发生异常的代码块中使用 try-catch 块或声明可能抛出的异常。
        } catch (e: Exception) {
            LogUtils.d("read file exception", throwable = e)
        } finally {
            fin?.close()
        }
        return res
    }
}