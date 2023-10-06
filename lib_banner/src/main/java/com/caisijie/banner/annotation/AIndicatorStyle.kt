package com.caisijie.banner.annotation

import androidx.annotation.IntDef
import com.caisijie.banner.mode.IndicatorStyle

/**
 * @author caisjie
 * @date 2023/10/6 15:39
 * @desc 指示器样式
 */
@IntDef(IndicatorStyle.CIRCLE, IndicatorStyle.DASH, IndicatorStyle.ROUND_RECT)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER,AnnotationTarget.FIELD)
annotation class AIndicatorStyle
