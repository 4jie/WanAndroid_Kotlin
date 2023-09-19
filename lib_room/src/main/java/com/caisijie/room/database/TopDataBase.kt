package com.caisijie.room.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.caisijie.framework.helper.AppHelper
import com.caisijie.room.dao.VideoListCacheDao
import com.caisijie.room.entity.VideoInfo

/**
 * @author caisijie
 * @date 2023/9/18 23:38
 * @desc 数据库操作类
 * 指定有哪些表，version必须指定版本，exportSchema生成一个json文件，方法排查问题，还需要在build.gradle文件中配置
 */
@Database(entities = [VideoInfo::class], version = 1, exportSchema = false)
abstract class TopDataBase : RoomDatabase() {
    //抽象方法或者抽象类标记
    abstract fun videoListDao(): VideoListCacheDao

    companion object {
        private var dataBase: TopDataBase? = null

        //同步锁，多线程使用时保证线程安全
        @Synchronized
        fun getInstance(): TopDataBase {
            return dataBase ?: Room.databaseBuilder(
                AppHelper.getApplication(),
                TopDataBase::class.java,
                "sijie_DB"
            )
                //是否允许在主线程查询，默认false，因为查询耗时
                .allowMainThreadQueries()
                //数据库被创建或者被打开时的回调
                //.addCallback(callBack)
                //指定数据查询的线程池，不指定会有个默认的
                //.setQueryExecutor{ }
                //任何数据库有变更版本都需要升级，升级的同时需要指定migration，如果不指定则会报错
                //数据库升级1-->2，怎么升级，以什么规则升级
                .addMigrations()
                //设置数据库工厂，用来链接room和SQLite，可以利用自行创建SupportSQLiteOpenHelper来实现数据库加密
                //.openHelperFactory()
                .build()
        }
    }
}