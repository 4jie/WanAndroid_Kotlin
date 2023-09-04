package com.caisijie.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.caisijie.common.constant.KEY_INDEX
import com.caisijie.framework.base.BaseDataBindActivity
import com.caisijie.main.databinding.ActivityMainBinding
import com.caisijie.main.navigator.NewFragmentNavigator

@Route
class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {
    private lateinit var navController: NavController

    companion object {
        fun start(context: Context, index: Int = 0) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(KEY_INDEX, index)
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        val navView = mBinding.navView
        //1.寻找出路由控制对象，它是路由跳转的唯一入口，找到宿主NavHostFragment
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        //2.自定义FragmentNavigator，mobile_navigation.xml文件中的fragment标识改为
        val fragmentNavigator=NewFragmentNavigator(this,navHostFragment.childFragmentManager,navHostFragment.id)
        //3.注册到Navigator里面，这样才找得到
        navController.navigatorProvider.addNavigator(fragmentNavigator)
        //4.设置Graph，需要将activity_main.xml文件中的app:navGraph="@navigation/mobile_navigation"移除
        navController.setGraph(R.navigation.)
    }
}