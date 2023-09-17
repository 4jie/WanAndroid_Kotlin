package com.caisijie.main.ui.home

import androidx.lifecycle.MutableLiveData
import com.caisijie.common.model.Banner
import com.caisijie.common.model.ProjectSubInfo
import com.caisijie.network.viewmodel.BaseViewModel

/**
 * @author caisijie
 * @date 2023/9/7
 * @description 首页的ViewModel
 */
class HomeViewModel : BaseViewModel(){
    val projectItemLiveData=MutableLiveData<MutableList<ProjectSubInfo>?>()
    val bannersLiveData=MutableLiveData<MutableList<Banner>?>()

    val homwRepository by lazy {

    }
}