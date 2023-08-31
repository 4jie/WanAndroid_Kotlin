@file:Suppress("UNCHECKED_CAST")

package com.caisijie.framework.ext

/**
 * 内联函数，
 * 1、内联函数可以减少函数调用开销，当调用一个函数时，会产生一定的开销，包括参数传递栈帧的创建销毁等
 * 内联函数可以在编译时将函数体的代码直接复制到调用处，避免了函数调用所带来的开销，从而提高程序的执行效率
 * 2、支持泛型操作，内联函数可以与泛型函数一起使用，解决了在运行时无法获取具体泛型类型的问题。
 * 通过内联函数，可以在编译时获取类型信息，并在函数体中根据类型参数进行相应的操作
 *
 * <reified T>表示泛型参数T是具体化的，允许在运行时获取泛型类型信息
 * Any是函数接收者类型，表示该函数可以在任意对象上使用
 * as是强转，将调用者对象强转为泛型T并返回
 */
inline fun <reified T> Any.saveAs(): T {
    return this as T
}

fun <T> Any.saveAsUnChecked(): T {
    return this as T
}

/**
 * is是判断调用者对象是否是类型T的实例，并将检查结果返回
 */
inline fun <reified T> Any.isEqualType(): Boolean {
    return this is T
}