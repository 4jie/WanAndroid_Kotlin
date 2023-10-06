//用于生成兼容Java的类，类名位DisplayUtil
@file:JvmName("DisplayUtil")
//用于合并多个顶层文件的声明同样为DisplayUtil的类，两个注解只能打在文件最顶部
@file:JvmMultifileClass

package com.caisijie.framework.ext

import android.content.Context
import android.util.TypedValue
import androidx.annotation.Dimension
import com.caisijie.framework.helper.AppHelper

/**
 * @author caisijie
 * @date 2023/10/6 16:37
 * @desc 定义一些顶层方法用于转换dp，sp为物理像素
 */
fun dpToPx(dpValue: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dpValue,
        AppHelper.getApplication().resources.displayMetrics
    )
}

fun dpToPx(dpValue: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dpValue.toFloat(),
        AppHelper.getApplication().resources.displayMetrics
    ).toInt()
}

fun dpToPx(context: Context, @Dimension(unit = Dimension.DP) dp: Int): Float {
    val r = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics)
}

/**
 * 所有字体均使用dp
 */
fun spToPx(spValue: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        spValue,
        AppHelper.getApplication().resources.displayMetrics
    )
}