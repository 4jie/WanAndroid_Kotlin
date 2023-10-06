package com.caisijie.banner.options

import android.graphics.Color
import com.caisijie.banner.annotation.AIndicatorOrientation
import com.caisijie.banner.annotation.AIndicatorSlideMode
import com.caisijie.banner.annotation.AIndicatorStyle
import com.caisijie.banner.mode.IndicatorOrientation
import com.caisijie.banner.mode.IndicatorSlideMode
import com.caisijie.framework.ext.dpToPx

/**
 * @author caisijie
 * @date 2023/10/6 14:55
 * @desc 指示器的配置参数
 */
class IndicatorOptions {
    @AIndicatorOrientation
    var orientation = IndicatorOrientation.INDICATOR_HORIZONTAL

    @AIndicatorStyle
    var indicatorStyle = 0

    /**
     * Indicator滑动模式，目前仅支持两种
     * @see注解可以实现在注释中持有类或者方法的引用，方便查看源代码
     * @see IndicatorSlideMode.NORMAL
     *
     * @see IndicatorSlideMode.SMOOTH
     */
    @AIndicatorSlideMode
    var slideMode = 0

    /**
     * 页面size
     */
    var pageSize = 0

    /**
     * 未选中时Indicator颜色
     */
    var normalSliderColor = 0

    /**
     * 选中时Indicator颜色
     */
    var checkedSliderColor = 0

    /**
     * Indicator间距
     */
    var sliderGap = 0f
    var sliderHeight = 0f
        get() = if (field > 0) field else normalSliderWidth / 2

    var normalSliderWidth = 0f

    var checkedSliderWidth = 0f

    /**
     * 指示器当前位置
     */
    var currentPosition = 0

    /**
     * 从一个点滑动到另一个点的进度
     */
    var slideProgress = 0f

    var showIndicatorOneItem = false

    init {
        normalSliderWidth = dpToPx(8f)
        checkedSliderWidth = normalSliderWidth
        sliderGap = normalSliderWidth
        normalSliderColor = Color.parseColor("#8C18171C")
        checkedSliderColor = Color.parseColor("#8C6C6D72")
        slideMode = IndicatorSlideMode.NORMAL
    }

    fun setCheckedColor(checkedColor: Int) {
        this.checkedSliderColor = checkedColor
    }

    fun setSliderWidth(
        normalIndicatorWidth: Float,
        checkedIndicatorWidth: Float
    ) {
        this.normalSliderWidth = normalIndicatorWidth
        this.checkedSliderWidth = checkedIndicatorWidth
    }

    fun setSliderWidth(sliderWidth: Float) {
        setSliderWidth(sliderWidth, sliderWidth)
    }

    fun setSliderColor(
        normalColor: Int,
        checkedColor: Int
    ) {
        this.normalSliderColor = normalColor
        this.checkedSliderColor = checkedColor
    }
}