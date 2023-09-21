package com.caisijie.main.utils

import android.content.res.AssetManager
import com.caisijie.framework.ext.toBean
import com.caisijie.framework.utils.log.LogUtils
import com.caisijie.room.entity.VideoInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.resumeWithException

/**
 * @author caisijie
 * @date 2023/9/21 10:52
 * @desc 解析JSON文件的工具类
 * 以异步方式读取assets目录下的文件，并且适配协程的写法，让他真正挂起函数
 * 方便调用，可以直接以同步的方法拿到返回值
 */
object ParseFileUtils {
    suspend fun parseAssetsFile(
        assetManager: AssetManager,
        fileName: String
    ): MutableList<VideoInfo> {
        //suspendCancellableCoroutine:将基于回调的异步操作转换为协程的挂起操作，使得异步代码更加简洁和易于理解。
        return suspendCancellableCoroutine { continuation ->
            try {
                val inputStream = assetManager.open(fileName)
                //逐行读取，不用逐个字节读取
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                val stringBuilder = StringBuilder()
                //kotlin中不允许这样写
//                while ((line = bufferedReader.readLine()) != null) {
//
//              }
                do {
                    line = bufferedReader.readLine()
                    if (line != null) stringBuilder.append(line) else break
                } while (true)
                inputStream.close()
                bufferedReader.close()

                val list = stringBuilder.toString().toBean<MutableList<VideoInfo>>()
                LogUtils.e("Coroutine==${list.size}", tag = "sijie")
                //作用是在挂起操作中恢复操作的执行，并将成功的结果 list 传递给挂起操作的调用方
                //这样，调用方便可以继续执行下一步的逻辑，处理挂起操作的结果。
                continuation.resumeWith(Result.success(list))
            } catch (e: Exception) {
                e.printStackTrace()
                continuation.resumeWithException(e)
                LogUtils.e(e)
            }
        }
    }
}