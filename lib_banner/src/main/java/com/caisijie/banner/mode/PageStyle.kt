package com.caisijie.banner.mode

/**
 * @author caisijie
 * @date 2023/10/6 16:57
 * @desc 指示器页面样式
 */
object PageStyle {
    const val NORMAL = 0

    @Deprecated("please use {@link BannerViewPager#setRevealWidth(int)} instead.")
    const val MULTI_PAGE = 1 shl 1

    /**
     * Requires Api Version >= 21
     * OVERLAP 重叠
     */
    const val MULTI_PAGE_OVERLAP = 1 shl 2

    const val MULTI_PAGE_SCALE = 1 shl 3
}