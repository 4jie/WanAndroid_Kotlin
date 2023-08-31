package com.caisijie.main.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.caisijie.common.manager.FileManager
import com.caisijie.framework.helper.AppHelper
import com.caisijie.framework.utils.log.LogUtils
import java.io.*
import java.net.URLConnection

/**
 * @author caisijie
 * @date 2023/8/28
 * @description 图片工具类
 */
object ImageUtils {
    private const val TAG = "ImageUtil"

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
    fun save(
        bitmap: Bitmap,
        file: File?,
        format: CompressFormat?,
        //vararg：标记可变参数/定义可变数组
        vararg recycle: Boolean
    ): Boolean {
        //位图为空或保存到的文件不存在则返回false
        if (isEmptyBitmap(bitmap) || !FileManager.createOrExitsFile(file)) {
            return false
        }
        println(bitmap.width.toString() + "," + bitmap.height)
        var os: OutputStream? = null
        var ret = false
        var isInsertSystemPictures = true
        if (recycle.size > 1) {
            isInsertSystemPictures = recycle[1]
        }
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            //compress():方法的参数
            //format：指定压缩后的格式，是一个枚举类型 Bitmap.CompressFormat，可以是 JPEG、PNG 或 WEBP。
            //quality：指定压缩的质量，取值范围为 0-100。0 表示压缩至最小的质量，100 表示不压缩。
            //stream：输出流，用于接收压缩后的位图数据。
            ret = bitmap.compress(format, 90, os)
            if (recycle[0] && !bitmap.isRecycled) bitmap.recycle()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (isInsertSystemPictures) {
            val out: FileOutputStream
            try {
                out = FileOutputStream(file)
                //格式为JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
                if (bitmap.compress(CompressFormat.JPEG, 90, out)) {
                    out.flush()
                    out.close()
                    //插入图库
                    insertSystemImage(file)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.i(e.message.toString(), tag = TAG)
            }
        }
        return ret
    }

    /**
     * 插入到系统相册图库中
     *
     * @param file
     */
    private fun insertSystemImage(file: File?): String? {
        if (file == null) {
            LogUtils.i("file==null", tag = TAG)
            return ""
        }
        val bitName = file.name
        LogUtils.i(
            "bitName==$bitName|file.getAbsolutePath:${file.absolutePath}|path==${file.path}",
            tag = TAG
        )
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val mimeType: String? = getMimeType(file)
                val fileName = file.name
                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                val contentResolver: ContentResolver =
                    AppHelper.getApplication().applicationContext.contentResolver
                val uri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri == null) {
                    LogUtils.i("图片保存失败", tag = TAG)
                    return ""
                }
                try {
                    val out = contentResolver.openOutputStream(uri)
                    val fis = FileInputStream(file)
                    copyStreamFile(fis, out)
                    fis.close()
                    out?.close()
                    val resultPath =
                        Environment.getExternalStorageDirectory().absolutePath + "/" + Environment.DIRECTORY_DCIM + "/" + fileName
                    LogUtils.i("图片保存成功：$resultPath", tag = TAG)
                    resultPath
                } catch (e: Exception) {
                    LogUtils.i("图片保存失败", tag = TAG)
                    e.printStackTrace()
                    ""
                }
            } else {
                //其次把文件插入到系统图库
                //MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), fileName, null);
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                val uri: Uri? = AppHelper.getApplication().applicationContext.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri == null) {
                    LogUtils.i("文件插入到系统图库失败",tag= TAG)
                    return ""
                }
                //最后通过广播通知图库更新
                AppHelper.getApplication().applicationContext.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + file.absolutePath)
                    )
                )
                file.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取文件mimeType
     */
    private fun getMimeType(file: File): String? {
        val fileNameMap = URLConnection.getFileNameMap()
        return fileNameMap.getContentTypeFor(file.name)
    }

    /**
     * 文件拷贝到指定的Stream
     */
    private fun copyStreamFile(fis: InputStream, out: OutputStream?) {
        try {
            val buffer = ByteArray(1024)
            var byteRead: Int
            while (-1 != fis.read(buffer).also { byteRead = it }) {
                out?.write(buffer, 0, byteRead)
            }
            fis.close()
            out?.flush()
            out?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}