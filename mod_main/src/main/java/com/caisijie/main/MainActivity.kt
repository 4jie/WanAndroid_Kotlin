package com.caisijie.main


import android.os.Bundle
import com.caisijie.framework.base.BaseDataBindActivity
import com.caisijie.main.databinding.ActivityMainBinding

@Route
class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}