package com.caisijie.common.model

/**
 * @author caisijie
 * @date 2023/9/17
 * @desc 轮播图
 */
data class Banner(
    val id: Int? = 0,
    val url: String? = "",//网站地址
    val imagePath: String? = "",//图片地址
    val title: String? = "",
    val desc: String? = "",
    val isVisible: Int? = 0,
    val order: Int? = 0,
    val type: Int? = 0
)