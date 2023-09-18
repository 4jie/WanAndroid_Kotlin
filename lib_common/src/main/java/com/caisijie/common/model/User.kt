package com.caisijie.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author caisijie
 * @date 2023/9/18 15:01
 * @desc 用户信息model类
 */
@Parcelize
data class User(
    val id: Int? = 0,
    val username: String?,
    val nickname: String?,
    val token: String?,
    val icon: String? = "",
    val email: String? = "",
    var password: String?,
    var signature: String?,
    var sex: String?,
    var birthday: String? = ""
) : Parcelable {
    fun getName(): String? {
        return if (!nickname.isNullOrEmpty()) {
            nickname
        } else {
            username
        }
    }
}