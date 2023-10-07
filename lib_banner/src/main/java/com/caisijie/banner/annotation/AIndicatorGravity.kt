package com.caisijie.banner.annotation

import android.view.Gravity.CENTER
import android.view.Gravity.END
import android.view.Gravity.START
import androidx.annotation.IntDef
import com.caisijie.banner.mode.IndicatorGravity

/**
 * @author caisijie
 * @date 2023/10/7 22:45
 * @desc 指示器位置
 */
@IntDef(CENTER, START, END)
@Retention(AnnotationRetention.SOURCE)
annotation class AIndicatorGravity()
