package com.caisijie.lib_framework.help

import android.app.Application
import android.nfc.tech.IsoDep

/**
 * @author caisijie
 * @date  2023/8/27
 * @description 提供应用环境
 */
object AppHelper {
    private lateinit var app: Application
    private var isDebug = false

    fun init(application: Application, isDebug: Boolean) {
        this.app = application
        this.isDebug = isDebug
    }

    /**
     * 获取全局应用
     */
    fun getApplication() = app

    /**
     * 是否为debug环境
     */
    fun isDebug() = isDebug
}