package com.caisijie.banner

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager2.widget.ViewPager2
import com.caisijie.banner.base.BaseViewHolder
import com.caisijie.banner.indicator.IIndicator
import java.text.FieldPosition

/**
 * @author caisijie
 * @date 2023/10/6 11:51
 * @desc BannerViewPager
 */
open class BannerViewPager<T, H : BaseViewHolder<T>> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs), LifecycleObserver {
    private var currentPosition = 0

    private var isCustomIndicator = false

    /**
     * 表示的是是否手动控制轮播
     */
    private var isLooping = false

    private var mOnPageClickLayout: OnPageClickListener?=null

    private var mIndicatorView:IIndicator?=null

    private var mIndicatorLayout: RelativeLayout? = null

    private var mViewPager: ViewPager2? = null

    private var mBannerManager: BannerManager = BannerManager()

    private val mHandler = Handler(Looper.getMainLooper())



    interface OnPageClickListener {
        fun onPageClick(clickedView: View?, position: Int)
    }
}