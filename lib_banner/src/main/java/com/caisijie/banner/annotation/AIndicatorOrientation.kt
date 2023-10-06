package com.caisijie.banner.annotation

import androidx.annotation.IntDef
import com.caisijie.banner.mode.IndicatorOrientation

/**
 * 指示器方向
 */
//@IntDef 相当于定义枚举类型，是对整型取值范围的限定，限制只能取定义的常量，相比枚举类还更加灵活，轻量，不用专门定义一个枚举类
@IntDef(
    IndicatorOrientation.INDICATOR_HORIZONTAL,
    IndicatorOrientation.INDICATOR_VERTICAL,
    IndicatorOrientation.INDICATOR_RTL
)
//指定注解的保留策略为源码级别，意味着注解仅在源代码中可见，编译后的字节码中不包含注解信息。注解只在编译时起作用，运行时无影响
@Retention(AnnotationRetention.SOURCE)
//指定注解应用的目标元素，这里指定为可以用在函数参数和成员变量上
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class AIndicatorOrientation
