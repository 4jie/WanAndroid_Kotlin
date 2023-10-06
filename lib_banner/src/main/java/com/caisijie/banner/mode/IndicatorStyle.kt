package com.caisijie.banner.mode

/**
 * 指示器样式
 */
object IndicatorStyle {
    const val CIRCLE = 0

    //shl是左移运算符，表示1向左移动1位，即0010，十进制中的2
    const val DASH = 1 shl 1
    const val ROUND_RECT = 1 shl 2
}