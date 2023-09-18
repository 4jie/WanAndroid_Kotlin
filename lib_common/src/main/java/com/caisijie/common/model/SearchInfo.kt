package com.caisijie.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author caisijie
 * @date 2023/9/18 15:37
 * @desc 搜索关键词和热门搜索数据类
 */
@Parcelize
data class KeyWord(
    val id: String?,
    val keyWord: String
) : Parcelable

/**
 * 热搜
 */
@Parcelize
data class HotSearch(
    val id: Int?,
    val link: String?,
    val name: String? = "",
    val order: Int?,
    val visible: Int?
) : Parcelable
