package com.caisijie.main.ui.home

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.fragment.app.Fragment
import com.caisijie.common.model.ProjectTabItem
import com.caisijie.framework.adapter.ViewPage2FragmentAdapter
import com.caisijie.framework.base.BaseMvvmFragment
import com.caisijie.main.databinding.FragmentHomeBinding
import com.caisijie.main.ui.home.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 * @author caisijie
 * @date 2023/9/4
 * @description 首页的fragment
 */
class HomeFragment :BaseMvvmFragment<FragmentHomeBinding,HomeViewModel>(),OnRefreshListener{
    private val mArrayTabFragments=SparseArray<Fragment>()
    private var mTabLayoutModel:TabLayoutMediator?=null
    private var mFragmentAdapter:ViewPage2FragmentAdapter?=null
    private var mProjectTabs:MutableList<ProjectTabItem> = mutableListOf()
    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        TODO("Not yet implemented")
    }

}