package com.caisijie.main.repository

import com.caisijie.common.model.ArticleList
import com.caisijie.common.model.Banner
import com.caisijie.common.model.ProjectSubList
import com.caisijie.common.model.ProjectTabItem
import com.caisijie.network.manager.ApiManager
import com.caisijie.network.repository.BaseRepository
import com.caisijie.network.response.BaseResponse
import com.caisijie.room.entity.VideoInfo
import com.caisijie.room.manager.VideoCacheManager

/**
 * @author caisijie
 * @date 2023/9/17
 * @desc 首页请求仓库
 */
class HomeRepository : BaseRepository() {
    /**
     * 首页banner轮播图
     */
    suspend fun getHomeBanner(): MutableList<Banner>? {
        return requestResponse {
            ApiManager.api.getHomeBanner()
        }
    }

    /**
     * 首页列表
     * @param page 页码
     * @param pageSize 每页数量
     */
    suspend fun getHomeInfoList(page: Int): ArticleList? {
        return requestResponse {
            ApiManager.api.getHomeList(page, 20)
        }
    }

    /**
     * 项目tab
     */
    suspend fun getProjectTab(): MutableList<ProjectTabItem>? {
        return requestResponse {
            ApiManager.api.getProjectTab()
        }
    }

    /**
     * 项目列表
     * @param page
     * @param cid
     */
    suspend fun getProjectList(page: Int, cid: Int): ProjectSubList? {
        return requestResponse {
            ApiManager.api.getProjectList(page, cid)
        }
    }

    /**
     * 获取视频列表数据
     */
    suspend fun getVideoListCache(): MutableList<VideoInfo>? {
        return VideoCacheManager.getVideoList()
    }

}