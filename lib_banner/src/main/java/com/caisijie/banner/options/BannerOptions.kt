package com.caisijie.banner.options

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.caisijie.banner.mode.PageStyle
import com.caisijie.banner.transform.ScaleInTransformer
import com.caisijie.framework.ext.dpToPx

/**
 * @author caisijie
 * @date 2023/10/6 16:52
 * @desc Banner的配置参数
 */
class BannerOptions {
    companion object {
        const val DEFAULT_REVEAL_WIDTH = -1000
    }

    private var offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
    private var interval = 0L
    private var isCanLoop = false
    private var isAutoPlay = false
    private var indicatorGravity = 0
    private var pageMargin = dpToPx(20f).toInt()
    private var rightRevealWidth = DEFAULT_REVEAL_WIDTH
    private var leftRevealWidth = DEFAULT_REVEAL_WIDTH
    private var pageStyle = PageStyle.NORMAL
    private var pageScale: Float = ScaleInTransformer.DEFAULT_MIN_SCALE
    private var mIndicatorMargin: IndicatorMargin? = null
    private var mIndicatorVisibility=View.VISIBLE
    private var scrollDuration=0
    private var roundRadius=0
    private var userInputEnabled=true
    private var orientation=ViewPager2.ORIENTATION_HORIZONTAL
    private var rtl=false
    private var disallowParentInterceptDownEvent=false
    private var stopLoopWhenDetachedFromWindow=true
    private var mIndicatorOptions:IndicatorOptions=IndicatorOptions()
    class IndicatorMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)
}