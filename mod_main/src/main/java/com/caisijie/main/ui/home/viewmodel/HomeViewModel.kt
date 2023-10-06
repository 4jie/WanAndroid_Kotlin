package com.caisijie.main.ui.home.viewmodel

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.caisijie.common.constant.FILE_VIDEO_LIST
import com.caisijie.common.model.ArticleList
import com.caisijie.common.model.Banner
import com.caisijie.common.model.ProjectSubInfo
import com.caisijie.common.model.ProjectTabItem
import com.caisijie.framework.toast.TipsToast
import com.caisijie.main.repository.HomeRepository
import com.caisijie.main.utils.ParseFileUtils
import com.caisijie.network.flow.requestFlow
import com.caisijie.network.manager.ApiManager
import com.caisijie.network.viewmodel.BaseViewModel
import com.caisijie.room.entity.VideoInfo
import com.caisijie.room.manager.VideoCacheManager
import kotlinx.coroutines.launch

/**
 * @author caisijie
 * @date 2023/9/7
 * @description 首页的ViewModel
 */
class HomeViewModel : BaseViewModel() {
    val projectItemLiveData = MutableLiveData<MutableList<ProjectSubInfo>?>()
    val bannersLiveData = MutableLiveData<MutableList<Banner>?>()

    val homeRepository by lazy {
        HomeRepository()
    }

    /**
     * 首页banner
     */
    fun getBannerList(): LiveData<MutableList<Banner>?> {
//        launchUI(errorBlock = { code, errorMsg ->
//            TipsToast.showTips(errorMsg)
//            bannersLiveData.value = null
//        }) {
//            val data = homeRepository.getHomeBanner()
//            bannersLiveData.value = data
//        }
//        return bannersLiveData
        //通过flow来请求
        viewModelScope.launch {
            val data = requestFlow(requestCall = {
                ApiManager.api.getHomeBanner()
            }, errorBlock = { code, error ->
                TipsToast.showTips(error)
                bannersLiveData.value = null
            })
            bannersLiveData.value = data
        }
        return bannersLiveData
    }

    /**
     * 首页列表
     * @param page 页码
     */
    fun getHomeInfoList(page: Int): LiveData<ArticleList> {
        return liveData {
            val response = safeApiCall(errorBlock = { code, errorMsg ->
                TipsToast.showTips(errorMsg)
            }) {
                homeRepository.getHomeInfoList(page)
            }
            response?.let {
                emit(it)
            }
        }
    }

    /**
     * 首页Project tab
     */
    fun getProjectTab(): LiveData<MutableList<ProjectTabItem>?> {
        return liveData {
            val response = safeApiCall(errorBlock = { code, errorMsg ->
                TipsToast.showTips(errorMsg)
            }) {
                homeRepository.getProjectTab()
            }
            emit(response)
        }
    }

    /**
     * 获取项目列表数据
     * @param page
     * @param cid
     */
    fun getProjectList(page: Int, cid: Int): LiveData<MutableList<ProjectSubInfo>?> {
        launchUI(errorBlock = { code, errorMsg ->
            TipsToast.showTips(errorMsg)
            projectItemLiveData.value = null
        }) {
            val data = homeRepository.getProjectList(page, cid)
            projectItemLiveData.value = data?.datas
        }
        return projectItemLiveData
    }

    /**
     * 首页视频列表
     */
    fun getVideoList(assetManager: AssetManager): LiveData<MutableList<VideoInfo>?> {
        return liveData {
            val response = safeApiCall(errorBlock = { code, errorMsg ->
                TipsToast.showTips(errorMsg)
            }) {
                var list = homeRepository.getVideoListCache()
                //缓存为空则创建视频数据
                if (list.isNullOrEmpty()) {
                    list = ParseFileUtils.parseAssetsFile(assetManager, FILE_VIDEO_LIST)
                    VideoCacheManager.saveVideoList(list)
                }
                list
            }
            emit(response)
        }
    }
}
