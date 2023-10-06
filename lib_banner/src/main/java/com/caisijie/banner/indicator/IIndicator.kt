package com.caisijie.banner.indicator

import androidx.viewpager.widget.ViewPager
import com.caisijie.banner.options.IndicatorOptions

/**
 * @author caisijie
 * @date 2023/10/6 14:53
 * @desc 指示器接口
 */
interface IIndicator : ViewPager.OnPageChangeListener {
    fun notifyDataChanged()
    fun setIndicatorOptions(options: IndicatorOptions)
}