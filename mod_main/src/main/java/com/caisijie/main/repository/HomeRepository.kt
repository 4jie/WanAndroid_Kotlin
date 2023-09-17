package com.caisijie.main.repository

import com.caisijie.common.model.Banner
import com.caisijie.network.repository.BaseRepository
import com.caisijie.network.response.BaseResponse

/**
 * @author caisijie
 * @date 2023/9/17
 * @desc 首页请求仓库
 */
class HomeRepository :BaseRepository(){
    /**
     * 首页banner轮播图
     */
    suspend fun getHomeBanner():MutableList<Banner>?{
        return requestResponse {

        }
    }


}