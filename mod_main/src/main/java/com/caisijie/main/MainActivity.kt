package com.caisijie.main


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import com.caisijie.common.constant.KEY_INDEX
import com.caisijie.framework.base.BaseDataBindActivity
import com.caisijie.main.databinding.ActivityMainBinding

@Route
class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {
    private lateinit var navController:NavController

    companion object{
        fun start(context: Context, index:Int=0){
            val intent=Intent(context,MainActivity::class.java)
            intent.putExtra(KEY_INDEX,index)
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
    }
}