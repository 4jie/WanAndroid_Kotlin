package com.caisijie.main.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.view.View
import com.caisijie.common.manager.FileManager
import java.io.File

/**
 * @author caisijie
 * @date 2023/8/28
 * @description 图片工具类
 */
object ImageUtils {
    const val TAG = "ImageUtil"

    /**
     * 获取View的bitmap对象
     */
    fun viewToBitmap(view: View, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
        //创建一个Bitmap对象
        val screenshot: Bitmap = Bitmap.createBitmap(view.width, view.height, config)
        //创建一个与Bitmap绑定的画布
        val canvas = Canvas(screenshot)
        //手动将这个View渲染到画布上
        view.draw(canvas)
        //返回已经拥有View绘制的Canvas的Bitmap对象
        return screenshot
    }

    /**
     * 判断bitmap对象是否为空
     * @param bitmap 源图片
     */
    private fun isEmptyBitmap(bitmap: Bitmap?): Boolean {
        return bitmap == null || bitmap.width == 0 || bitmap.height == 0
    }
    /**
     * 保存图片
     * @param bitmap 源图片
     * @param file 要保存到的文件
     * @param format bitmap可以压缩成的格式：比如png，webp
     * @param recycle 是否回收
     */
    fun save(bitmap:Bitmap,file:File?,format: CompressFormat?,vararg recycle:Boolean):Boolean{
        //位图为空或保存到的文件不存在则返回false
        if(isEmptyBitmap(bitmap)||!FileManager.createOrExitsFile(file)){
            return false
        }
        println(bitmap.width.toString()+","+bitmap.height)

    }


}