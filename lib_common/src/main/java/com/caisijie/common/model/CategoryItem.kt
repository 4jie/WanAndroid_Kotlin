package com.caisijie.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale.Category

/**
 * @author caisijie
 * @date 2023/9/18 14:15
 * @desc 分类tab信息，第三个fragment的信息
 */
data class CategoryItem(
    val cid: Int?,
    val name: String?,
    var isSelected: Boolean?,
    val articles: MutableList<CategorySecondItem>? = mutableListOf()
)

/**
 * @Parcelize 注解标记时，编译器会自动生成必要的代码，使该类能够实现 Parcelable 接口。
 * Parcelable 接口允许对象在不同组件之间进行序列化和反序列化，例如在不同的 Activity 或 Fragment 之间传递数据。
 * 使用 @Parcelize 注解可以避免手动实现 Parcelable 接口从而简化了代码的编写过程。
 * 它自动处理了对象的序列化和反序列化，无需手动编写 writeToParcel() 和 createFromParcel() 等方法。
 */
@Parcelize
data class CategorySecondItem(
    val id: Int?,
    val link: String?,
    val title: String?,
    val chapterId: Int?
) : Parcelable