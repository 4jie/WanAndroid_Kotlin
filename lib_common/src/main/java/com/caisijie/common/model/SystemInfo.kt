package com.caisijie.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author caisijie
 * @date 2023/9/18 14:49
 * @desc 体系
 */
data class SystemList(
    val id: Int?,
    val courseId: Int?,
    val name: String?,
    val children: MutableList<SystemSecondList>? = mutableListOf(),
    val visible: Int?
)

/**
 * 二级列表
 */
@Parcelize
data class SystemSecondList(
    val id: Int?,
    val name: String?,
    val visible: Int?
) : Parcelable