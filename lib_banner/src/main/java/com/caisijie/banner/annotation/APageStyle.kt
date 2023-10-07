package com.caisijie.banner.annotation

import androidx.annotation.IntDef
import com.caisijie.banner.mode.PageStyle
import com.caisijie.banner.mode.PageStyle.MULTI_PAGE
import com.caisijie.banner.mode.PageStyle.MULTI_PAGE_OVERLAP
import com.caisijie.banner.mode.PageStyle.MULTI_PAGE_SCALE
import com.caisijie.banner.mode.PageStyle.NORMAL
import java.text.Normalizer

/**
 * @author caisijie
 * @date 2023/10/7 22:34
 * @desc 指示器页面样式
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@IntDef(NORMAL, MULTI_PAGE, MULTI_PAGE_OVERLAP, MULTI_PAGE_SCALE)
@Retention(AnnotationRetention.SOURCE)
annotation class APageStyle()
