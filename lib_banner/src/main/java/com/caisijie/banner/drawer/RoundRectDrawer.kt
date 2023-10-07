package com.caisijie.banner.drawer

import android.graphics.Canvas
import com.caisijie.banner.base.BaseDrawer
import com.caisijie.banner.options.IndicatorOptions

/**
 * @author caisijie
 * @date 2023/10/7 22:03
 * @desc 圆角Drawer
 */
class RoundRectDrawer internal constructor(indicatorOptions: IndicatorOptions) :
    RectDrawer(indicatorOptions) {
    override fun drawRoundRect(canvas: Canvas, rx: Float, ry: Float) {
        canvas.drawRoundRect(mRectF, rx, ry, mPaint)
    }
}