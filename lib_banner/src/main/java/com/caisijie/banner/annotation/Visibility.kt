package com.caisijie.banner.annotation

import android.view.View
import androidx.annotation.IntDef

/**
 * @author caisijie
 * @date 2023/10/7 22:42
 * @desc 指示器可见性
 */
@IntDef(View.VISIBLE, View.INVISIBLE, View.GONE)
@Retention(AnnotationRetention.SOURCE)
annotation class Visibility()
