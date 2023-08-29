package com.caisijie.lib_framework.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebSettings
import com.caisijie.lib_framework.helper.AppHelper
import com.tencent.smtt.sdk.WebView

/**
 * 腾讯X5WebView
 * 使用了腾讯的 X5 内核，该内核在性能和兼容性方面相对于 Android系统自带的 WebView 有一定的优势。
 * 可以提供更快的页面加载速度和更好的网页渲染效果，同时支持更多的 HTML5 特性和最新的 Web 标准。
 * 也提供了一系列丰富的功能扩展，例如支持多窗口、视频播放、文件上传、缓存控制、JavaScript 交互等。
 * 还提供了一些自定义的接口和回调，可以满足更灵活的需求。
 */
open class BaseWebView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null) :
    WebView(context, attr) {
    /**
     * 滑动距离监听
     */
    private var mOnWebScrollListener: OnWebScrollListener? = null

    init {
        initSetting()
        /**
         * WebView在安卓5.0之前默认允许其加载混合网络协议内容
         * 5.0之后，默认不允许加载http与https混合内容
         * 需要设置webView允许其加载混合网络协议内容
         */
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
//        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initSetting() {
        overScrollMode = View.OVER_SCROLL_ALWAYS
        settings.apply {
            defaultTextEncodingName = "utf-8"
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            //适应手机屏幕
            useWideViewPort = true
            loadWithOverviewMode = true
            //设置页面步算法为窄列布局
            layoutAlgorithm = com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            //禁用缩放
            setSupportZoom(false)
            builtInZoomControls = false
            //启用缓存和地理位置
            setAppCacheEnabled(true)
            setGeolocationEnabled(true)
            setAppCacheMaxSize(Long.MAX_VALUE)
            //启用DOM存储
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
//            mixedContentMode = com.tencent.smtt.sdk.WebSettings.LOAD_NORMAL
            if (AppHelper.isDebug()) {
                setWebContentsDebuggingEnabled(true)
            }
            //不使用缓存
            settings.mixedContentMode = WebSettings.LOAD_NO_CACHE
            // 添加userAgent
//            settings.setUserAgentString(
//                settings.getUserAgentString() + " tuoduni-android-" + AppUtil.getVersionName(
//                    mContext.getApplicationContext()
//                )
//            )
        }
        x5WebViewExtension?.apply {
            //禁用水平和垂直滚动条。关闭滚动条淡出效果
            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
            setScrollBarFadingEnabled(false)
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        mOnWebScrollListener?.onWebScrollChanged(l, t, oldl, oldt)
    }

    /**
     * 设置滑动监听
     */
    fun setOnWebScrollListener(onWebScrollListener: OnWebScrollListener) {
        mOnWebScrollListener = onWebScrollListener
    }

    /**
     * 滑动距离监听
     */
    interface OnWebScrollListener {
        fun onWebScrollChanged(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int)
    }

}