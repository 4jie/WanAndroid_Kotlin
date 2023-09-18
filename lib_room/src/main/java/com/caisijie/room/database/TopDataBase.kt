package com.caisijie.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.caisijie.room.entity.VideoInfo

/**
 * @author caisijie
 * @date 2023/9/18 23:38
 * @desc 数据库操作类
 * 指定有哪些表，version必须指定版本，exportSchema生成一个json文件，方法排查问题，还需要在build.gradle文件中配置
 */
@Database(entities = [VideoInfo::class],version=1, exportSchema = false)
abstract class TopDataBase :RoomDatabase(){
    //抽象方法或者抽象类标记
    abstract fun videoListDao():

}