package com.caisijie.framework.ext

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * @author caisijie
 * @date 2023/9/21 11:01
 * @desc 定义gson解析的相关的扩展函数以方便调用
 */
fun gsonJsonObjectOf(vararg args: Pair<String, Any?>) = JsonObject().append(*args)

fun JsonObject.append(vararg args: Pair<String, Any?>): JsonObject {
    args.forEach { (name, value) ->
        value?.let {
            when (it) {
                is String -> append(name, it)
                is Number -> append(name, it)
                is Boolean -> append(name, it)
                is Char -> append(name, it)
                is JsonElement -> append(name, it)
                else -> append(name, it.toString())
            }
        }
    }
    return this
}

fun JsonObject.append(other: JsonObject?): JsonObject {
    other?.entrySet()?.forEach { (name, value) ->
        append(name, value)
    }
    return this
}

fun JsonObject.append(name: String, value: String?): JsonObject {
    value?.let {
        addProperty(name, it)
    }
    return this
}

fun JsonObject.append(name: String, value: Number?): JsonObject {
    value?.let {
        addProperty(name, it)
    }
    return this
}

fun JsonObject.append(name: String, value: Boolean?): JsonObject {
    value?.let {
        addProperty(name, it)
    }
    return this
}

fun JsonObject.append(name: String, value: Char?): JsonObject {
    value?.let {
        addProperty(name, it)
    }
    return this
}

fun JsonObject.append(name: String, value: JsonElement?): JsonObject {
    value?.let {
        add(name, it)
    }
    return this
}

/**
 * 运算符重载
 */
operator fun JsonObject?.plus(other: JsonObject?): JsonObject = gsonJsonObjectOf().also {
    it.append(this).append(other)
}

fun String.toJsonObject(): JsonObject? {
    return toJsonElement()?.asJsonObject
}

fun String.toJsonElement(): JsonElement? {
    if (!isJSONValid()) return null
    return JsonParser().parse(this)
}

fun String.isJSONValid(): Boolean {
    try {
        JSONObject(this)
    } catch (e: JSONException) {
        // edited, to include @Arthur's comment
        // e.g. in case JSONArray is valid as well...
        try {
            JSONArray(this)
        } catch (ex: JSONException) {
            return false
        }
    }
    return true
}

inline fun <reified T> JsonElement.toBeanOrNull(includeNulls: Boolean): T? =
    runCatching { toBean<T>() }.getOrNull()

val JsonElement.asJsonObjectOrNull: JsonObject?
    get() = runCatching { asJsonObject }.getOrNull()

val JsonElement.asJsonArrayOrNull: JsonArray?
    get() = runCatching { asJsonArray }.getOrNull()

val JsonElement.asStringOrNull: String?
    get() = runCatching { asString }.getOrNull()

val JsonElement.asJsonIntOrNull: Int?
    get() = runCatching { asInt }.getOrNull()

val JsonElement.asJsonBooleanOrNull: Boolean?
    get() = runCatching { asBoolean }.getOrNull()

val String.prettyJson: String
    get() {
        if (!isJSONValid()) return ""
        return JsonParser().parse(this).toPrettyJson()
    }

inline fun <reified T> JsonElement.asBean(): T? {
    return if (isJsonObject) toBean() else null
}


/**
 * 将Bean对象转换成可读性高的json字符串
 *
 * @receiver 任意Bean对象
 * @receiver 用于给扩展函数或属性指定接收者类型，用于扩展函数的定义中，
 * 通过将函数定义放在接收者类型的作用域内来扩展该类型的功能。
 * @return 转换后的json字符串
 */
fun Any.toPrettyJson(): String {
    return GSON_PRETTY.toJson(this)
}

/**
 * 将Bean对象转换成json字符串
 *
 * @receiver 任意Bean对象
 * @param includeNulls 是否包含值为空的字段（默认包含）
 * @return 转换后的json字符串
 */
fun Any.toJson(includeNulls: Boolean = true): String {
    return gson(includeNulls).toJson(this)
}


/**
 * 将Bean对象转成[JsonObject]
 */
inline fun <reified T> T.toJsonObject(includeNulls: Boolean = true): JsonObject {
    return gson(includeNulls).toJsonTree(this, object : TypeToken<T>() {}.type).asJsonObject
}

/**
 * 有些json串有引号，可以去除
 */
fun String.parseJson() = JsonParser().parse(this)

private val GSON by lazy {
    GsonBuilder().create()
}

private val GSON_NO_NULLS by lazy {
    //serializeNulls():配置Gson序列化空字段。默认情况下，Gson省略序列化期间为空的所有字段
    GsonBuilder().serializeNulls().create()
}
private val GSON_PRETTY by lazy {
    //setPrettyPrinting():配置Gson输出适合页面的Json，以实现美观的打印。此选项仅影响Json序列化
    GsonBuilder().setPrettyPrinting().create()
}

/**
 * 将json字符串转换成目标Bean对象
 *
 * @receiver json元素
 * @param includeNulls 是否包含值为空的字段（默认包含）
 * @return 转换后的Bean对象
 */
inline fun <reified T> String.toBean(includeNulls: Boolean = true): T {
    return gson(includeNulls).fromJson(this, object : TypeToken<T>() {}.type)
}

//将字符串转换为指定类型的对象，并返回转换后的对象。如果转换过程中发生异常，将打印异常信息，并返回 null。
// 通过使用 runCatching 函数，可以避免转换过程中的异常导致程序崩溃，而是提供了一个安全的方式来处理异常情况。
inline fun <reified T> String.toBeanOrNull(): T? =
    runCatching { toBean<T>() }.onFailure { it.printStackTrace() }.getOrNull()

inline fun <reified T> JsonElement.toBean(includeNulls: Boolean = true): T {
    //TypeToken<T>() {}.type 是创建一个匿名内部类并获取其类型
    // TypeToken 是 Gson 库中的一个类，用于获取泛型类型的信息
    // 通过 object : TypeToken<T>() {} 创建一个匿名内部类，并调用 .type 方法获取其类型
    return gson(includeNulls).fromJson(this, object : TypeToken<T>() {}.type)
}

fun gson(includeNulls: Boolean): Gson = if (includeNulls) GSON else GSON_NO_NULLS