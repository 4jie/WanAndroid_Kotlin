package com.caisijie.main.banner

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.caisijie.banner.BannerViewPager
import com.caisijie.common.model.Banner
import com.caisijie.framework.ext.dpToPx

/**
 * @author caisijie
 * @date 2023/10/7 23:24
 * @desc 首页Banner
 */
class HomeBannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) :
    BannerViewPager<Banner, BannerImageHolder>(context, attrs) {
    /**
     * BannerAdapter
     */
    private val mAdapter = HomeBannerAdapter()

    init {
//        ViewUtils.setClipViewCornerRadius(this, dpToPx(12))
//        setPageMargin(mPageMargin)
//        setIndicatorView(
//            DrawableIndicator(getContext())
//                    .setIndicatorGap(mIndicatorSpace)
//                    .setIndicatorDrawable(
//                        R.drawable.viewpager_indicator_unfocused,
//                        R.drawable.viewpager_indicator_focused
//                    )
//                    .setIndicatorSize(mIndicatorSize, mIndicatorSize, mIndicatorSize, mIndicatorSize)
//        )
        setAdapter(mAdapter) // 设置适配器
            .setAutoPlay(true) // 自动播放
            .setScrollDuration(500) // 滑动的时间
            .setCanLoop(true) // 可循环滑动
            .setInterval(2000L) // 循环时间间隔(下一页时间)
            .setIndicatorSliderWidth(dpToPx(6))
            .setIndicatorSliderColor(Color.parseColor("#8F8E94"), Color.parseColor("#0165b8"))
            .create() // 创建

        mAdapter.setPageClickListener(object : OnPageClickListener {
            override fun onPageClick(clickedView: View?, position: Int) {
                val item = mAdapter.getData()[position]
                if (!item.url.isNullOrEmpty()) {
                    MainServiceProvider.toArticleDetail(
                        context = context,
                        url = item.url!!,
                        title = item.title ?: ""
                    )
                }
            }
        })
    }

    /**
     * 设置数据
     * @param list
     */
    fun setData(list: MutableList<Banner>) {
        refreshData(list)
    }
}