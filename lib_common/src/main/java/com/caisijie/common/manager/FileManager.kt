package com.caisijie.common.manager

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.caisijie.framework.helper.AppHelper
import com.caisijie.framework.utils.log.LogUtils
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.net.URI
import java.nio.file.Paths

/**
 * @author caisijie
 * @date 2023/8/28
 * @description 文件管理类
 *
 * getCacheDir():获取 /data/data/包名/cache 目录
 * getFilesDir():获取 /data/data/包名>/files 目录
 * getExternalFilesDir():获取 SDCard/Android/data/包名/files/ 目录，一般放一些长时间保存的数据
 * getExternalCacheDir():获取 SDCard/Android/data/包名/cache/ 目录，一般存放临时缓存数据
 */
object FileManager {
    //媒体模块根目录
    private val SAVE_MEDIA_ROOT_DIR = Environment.DIRECTORY_DCIM

    //媒体模块存储路径
    private val SAVE_MEDIA_DIR = "$SAVE_MEDIA_ROOT_DIR/4jie"

    //头像保存路径
    private const val AVATAR_DIR = "/avatar"

    //图片缓存等保存路径
    private const val JIE_IMAGE = "JieImage"

    //JPG后缀
    const val JPG_SUFFIX = ".jpg"

    //PNG后缀
    const val PNG_SUFFIX = ".png"

    //MP4后缀
    const val MP4_SUFFIX = ".mp4"

    //YUV后缀
    const val YUV_SUFFIX = ".yuv"

    //h264后缀
    const val H264_SUFFIX = ".h264"

    /**
     * 获取jpg图片输出路径
     * 用到了四大组件的ContentProvider内容提供器
     * @return 输出路径
     */
    fun getHeadJpgFile(): Any? {
        val fileName = "${System.currentTimeMillis() / 1000}$JPG_SUFFIX"
        val headPath: String = getAvatarPath(fileName)
        val imgFile: File
        //高于Android11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            imgFile = File(headPath)
            //通过MediaStore API插入file为了拿到系统裁剪要保存到的uri（因为APP没有权限不能访问公共存储空概念，需要通过MediaStore API来操作）
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, imgFile.absolutePath)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            return AppHelper.getApplication().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )
        } else {
            imgFile = File(headPath)
        }
        return imgFile
    }

    /**
     * 通过媒体文件Uri获取文件-Android11兼容
     */
    fun getMediaUri2File(fileUri: Uri): File? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = AppHelper.getApplication().contentResolver.query(
            fileUri, projection, null, null, null
        )
        if (cursor != null) {
            if (cursor.moveToNext()) {
                //MediaStore.Images.Media.DATA 是一个字符串常量，表示图片数据所在的列名。
                // getColumnIndexOrThrow() 方法会根据给定的列名返回相应的索引值。
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val path = cursor.getString(columnIndex)
                cursor.close()
                return File(path)
            }
        }
        return null
    }

    /**
     * 获取头像地址
     */
    fun getAvatarPath(fileName: String): String {
        val path: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getFolderDirPath(SAVE_MEDIA_DIR + AVATAR_DIR)
        } else {
            getSaveDir(AVATAR_DIR)
        }
        return path + File.separator + fileName
    }


    /**
     * 获取文件路径
     */
    fun getFolderDirPath(dstDirPathToCreate: String): String? {
        val dstFileDir = File(Environment.getExternalStorageDirectory(), dstDirPathToCreate)
        if (!dstFileDir.exists() && !dstFileDir.mkdirs()) {
            return null
        }
        return dstFileDir.absolutePath
    }

    /**
     * 获取具体模块存储目录
     */
    fun getSaveDir(directory: String): String {
        var path = ""
        path = if (TextUtils.isEmpty(directory) || "/" == directory) {
            ""
        } else if (directory.startsWith("/")) {
            directory
        } else {
            "/$directory"
        }
        path = getAppRootDir() + path
        val file = if (isSpace(path)) null else File(path)
        createOrExistsDir(file)
        return path
    }

    /**
     * 获取APP存储根目录
     */
    fun getAppRootDir(): String {
        val path: String = getStorageRootDir()
        val file = if (isSpace(path)) null else File(path)
        LogUtils.i("getAppRootDir:$path")
        createOrExistsDir(file)
        return path
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return `true`: 存在或创建成功<br> `false`: 不存在或创建失败
     */
    fun createOrExistsDir(file: File?): Boolean {
        //如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 判断该文件是否存在
     * @return `true`:存在或创建成功<br></br> `false`：不存在或创建失败
     */
    fun createOrExitsFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        //如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) {
            return file.isFile
        }
        //在创建新文件之前使用createOrExistsDis()创建或检查是否存在当前文件的父目录
        //如果不存在或创建失败直接返回true表示无法创建文件，存在或创建父目录成功则创建文件
        return if (!createOrExistsDir(file.parentFile)) false else
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return false
    }

    /**
     * 获取文件存储根目录
     */
    fun getStorageRootDir(): String {
        //getExternalFilesDir():用于获取特定于应用程序的目录的绝对路径。如果共享存储当前不可用，可能返回null。
        val filePath: File? = AppHelper.getApplication().getExternalFilesDir("")
        val path: String = if (filePath != null) {
            filePath.absolutePath
        } else {
            //filesDir():存放应用程序文件的目录路径
            AppHelper.getApplication().filesDir.absolutePath
        }
        LogUtils.i("getStorageRootDir:$path")
        return path
    }

    /**
     * 创建图片换成目录
     */
    fun getImageDirectory(context: Context): File {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            //拿到SD卡路径
            var directory = Environment.getExternalStorageDirectory()
            //大于API28，Android9
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
            //创建一个文件夹
            val imageCache = File(directory, JIE_IMAGE)
            if (!imageCache.exists()) {
                imageCache.mkdirs()
            }
            return if (imageCache.exists())
                imageCache else context.filesDir
        }
        return context.filesDir
    }

    /**
     * 获取文件大小
     * Context.getExternalFilesDir()->SDCard/Android/data/你应用的包名/files/
     * 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir()-->
     * SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            var size2 = 0
            if (fileList != null) {
                size2 = fileList.size
                for (i in 0 until size2) {
                    //如果下面还有文件
                    size = if (fileList[i].isDirectory) {
                        size + getFolderSize(fileList[i])
                    } else {
                        size + fileList[i].length()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }
    //==================================缓存处理=========================================
    /**
     * 获取应用总缓存大小
     */
    fun getTotalCacheSize(context: Context, vararg dirPaths: String?): String? {
        return try {
            var cacheSize: Long = 0    //= FileUtils.getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val file = context.externalCacheDir
                cacheSize += if (file == null) 0 else getFolderSize(file)
                for (dirPath in dirPaths) {
                    dirPaths?.let {
                        val current = File(dirPath)
                        cacheSize += getFolderSize(current)
                    }
                }
            }
            getFormatSize(cacheSize.toDouble())
        } catch (e: Exception) {
            e.printStackTrace()
            "0KB"
        }
    }

    /**
     * 格式化单位
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return if (kiloByte == 0.0) {
                "0KB"
            } else {
                size.toString() + "KB"
            }
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            //setScale()设置精度为 2，并采用四舍五入的方式进行舍入。`toPlainString()` 将结果转换为不带指数字段的字符串表示。
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     *删除目录下的所有文件
     * @param path 文件目录
     * @return 是否删除
     */
    fun deleteAllFile(path: String): Boolean {
        var flag = false
        val file = File(path)
        if (!file.exists()) {
            return flag
        }
        //isDirectory()返回文件路径是否存在
        if (!file.isDirectory) {
            return flag
        }
        val tempList = file.list() ?: return false
        var temp: File?
        for (i in tempList.indices) {
            temp = if (path.endsWith(File.separator)) {
                File(path + tempList[i])
            } else {
                File(path + File.separator + tempList[i])
            }
            if (temp.isFile) {
                temp.delete()
            }
            if (temp.isDirectory) {
                //先删除文件夹里面的文件
                deleteAllFile(path + "/" + tempList[i])
                //再删除空文件夹
                deleteFolder(path + "/" + tempList[i])
                flag = true
            }
        }
        return flag
    }

    /**
     * 删除独立空文件夹
     * @param path 文件地址
     */
    private fun deleteFolder(path: String) {
        try {
            val file = File(path)
            file.delete()
        } catch (e: Exception) {
            e.message?.let {
                Log.e(it, "$e")
            }
        }
    }

    /**
     * 获取缓存目录
     */
    fun getDiskCacheDir(context: Context): String? {
        //var类型必须初始化或注明具体数据类型
        val cachePath: String?
        cachePath =
                //如果外部存储可用（Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()）或者外部存储不可移除（!Environment.isExternalStorageRemovable()），
                // 则使用 context.externalCacheDir?.path 获取外部缓存目录的路径。
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                context.externalCacheDir?.path
            } else {
                //否则使用context.cacheDir.path获取内部缓存路径
                context.cacheDir.path
            }
        return cachePath
    }
}