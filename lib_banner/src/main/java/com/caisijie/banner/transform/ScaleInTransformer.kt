package com.caisijie.banner.transform

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * @author caisijie
 * @date 2023/10/6 17:05
 * @desc 缩放Transformer
 */
class ScaleInTransformer(var mMinScale: Float = 0f) : ViewPager2.PageTransformer {
    companion object {
        private const val DEFAULT_CENTER = 0.5f
        const val DEFAULT_MIN_SCALE = 0.85f
    }

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height
        //中心点
        page.pivotX = pageWidth / 2f
        page.pivotY = pageHeight / 2f
        if (position < -1) {
            //缩放点
            page.scaleX = mMinScale
            page.scaleY = mMinScale
            page.pivotX = pageWidth.toFloat()
        } else if (position <= 1) {
            // Modify the default slide transition to shrink the page as well
            if (position < 0) {
                val scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale
                //缩放因子
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                page.pivotX = pageWidth * (DEFAULT_CENTER + DEFAULT_CENTER * -position)
            } else {
                val scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                page.pivotX = pageWidth * ((1 - position) * DEFAULT_CENTER)
            }
        } else {
            page.pivotX = 0f
            page.scaleX = mMinScale
            page.scaleY = mMinScale
        }
    }
}