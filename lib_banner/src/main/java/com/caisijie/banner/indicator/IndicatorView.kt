package com.caisijie.banner.indicator

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.caisijie.banner.annotation.AIndicatorOrientation
import com.caisijie.banner.base.BaseIndicatorView
import com.caisijie.banner.controller.AttrsController
import com.caisijie.banner.drawer.DrawerProxy
import com.caisijie.banner.mode.IndicatorOrientation
import com.caisijie.banner.options.IndicatorOptions

/**
 * @author caisijie
 * @date 2023/10/7 14:25
 * @desc 轮播图下方的指示小图标 The Indicator in BannerViewPager，this include three indicator styles,as below:
 * [com.caisijie.banner.mode.IndicatorStyle.CIRCLE]
 * [com.caisijie.banner.mode.IndicatorStyle.DASH]
 * [com.caisijie.banner.mode.IndicatorStyle.ROUND_RECT]
 */
class IndicatorView @JvmOverloads constructor(
    context: Context,
    //指定视图的默认样式
    attrs:AttributeSet?=null,
    defStyleAttr:Int=0
) :BaseIndicatorView(context,attrs, defStyleAttr){
    private var mDrawerProxy: DrawerProxy

    init {
        AttrsController.initAttrs(context, attrs, mIndicatorOptions)
        mDrawerProxy = DrawerProxy(mIndicatorOptions)
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        mDrawerProxy.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureResult = mDrawerProxy.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureResult.measureWidth, measureResult.measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rotateCanvas(canvas)
        mDrawerProxy.onDraw(canvas)
    }

    override fun setIndicatorOptions(options: IndicatorOptions) {
        super.setIndicatorOptions(options)
        mDrawerProxy.setIndicatorOptions(options)
    }


    override fun notifyDataChanged() {
        mDrawerProxy = DrawerProxy(mIndicatorOptions)
        super.notifyDataChanged()
    }

    private fun rotateCanvas(canvas: Canvas) {
        if (mIndicatorOptions.orientation == IndicatorOrientation.INDICATOR_VERTICAL) {
            canvas.rotate(90f, width / 2f, width / 2f)
        } else if (mIndicatorOptions.orientation == IndicatorOrientation.INDICATOR_RTL) {
            canvas.rotate(180f, width / 2f, height / 2f)
        }
    }

    fun setOrientation(@AIndicatorOrientation orientation: Int) {
        mIndicatorOptions.orientation = orientation;
    }
}