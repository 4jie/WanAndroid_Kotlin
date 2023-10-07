package com.caisijie.banner.drawer

import com.caisijie.banner.mode.IndicatorStyle
import com.caisijie.banner.options.IndicatorOptions
import com.caisijie.banner.utils.IndicatorUtils

/**
 * @author caisijie
 * @date 2023/10/7 21:43
 * @desc Indicator Draw Factor
 */
internal object DrawerFactory {
    fun createDrawer(indicatorOptions: IndicatorOptions): IDrawer {
        return when (indicatorOptions.indicatorStyle) {
            IndicatorStyle.DASH -> DashDrawer(indicatorOptions)
            IndicatorStyle.ROUND_RECT -> RoundRectDrawer(indicatorOptions)
            else -> CircleDrawer(indicatorOptions)
        }
    }
}