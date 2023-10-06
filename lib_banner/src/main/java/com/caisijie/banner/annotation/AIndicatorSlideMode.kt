package com.caisijie.banner.annotation

import androidx.annotation.IntDef
import com.caisijie.banner.mode.IndicatorSlideMode
import com.caisijie.banner.mode.IndicatorSlideMode.COLOR
import com.caisijie.banner.mode.IndicatorSlideMode.NORMAL
import com.caisijie.banner.mode.IndicatorSlideMode.SCALE
import com.caisijie.banner.mode.IndicatorSlideMode.SMOOTH
import com.caisijie.banner.mode.IndicatorSlideMode.WORM
import java.text.Normalizer

/**
 * @author caisijie
 * @date 2023/10/6 16:00
 * @desc 指示器滑动模式
 */
@IntDef(NORMAL, SMOOTH, WORM, COLOR, SCALE)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class AIndicatorSlideMode()
