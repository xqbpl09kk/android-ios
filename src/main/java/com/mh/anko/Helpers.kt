
package com.mh.anko

// MARK：  添加 guard 关键字 == 没有 let 语法
inline fun guard( condition: Boolean, body: () -> Void ){
    if( !condition )
        body()
}

// 模拟 iOS 中的 guard 语意
inline fun <reified T : Any> guardList( value: Any?, body: () -> Unit ): List<T>{
    val result = value?.checkList<T>()
    if( result != null ) {
        return result
    }else {
        body()
        return value as List<T> // 一定不能到这里 == body 里面一定要返回， 否则 crash
    }
}

// guard else 语法糖 的模拟
inline fun <reified T : Any> guardValue( value: Any?, body: () -> Unit ): T{
    if( value is T ) {
        return value
    }else {
        body()
        return value as T // 一定不能到这里 == body 里面一定要返回， 否则 crash
    }
}

// 检测列表中是否全部是某种类型的元素， 是的话 强制转换成 某种列表类型，不是的话返回空
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> List<*>.checkItemsAre() =
        if (all { it is T })
            this as List<T>
        else null

// 加上了判断逻辑 == 这样的代码更加的健壮 == 不容易出错！！
inline fun <reified T : Any> Any.checkList(): List<T>? {
    val list = this as? List<*> ?: return null
    return list.checkItemsAre<T>()
}


/**
 * Return the grayscale color with the zero opacity using the single color value.
 * E.g., 0xC0 will be translated to 0xC0C0C0.
 */
val Int.gray: Int
    get() = this or (this shl 8) or (this shl 16)

/**
 * Return the color with 0xFF opacity.
 * E.g., 0xabcdef will be translated to 0xFFabcdef.
 */
val Int.opaque: Int
    get() = this or 0xff000000.toInt()

/**
 * 获取对应透明度的颜色 == 就这个比较重要和常用
 */
fun Int.withAlphaComponent(alpha: Int): Int {
    require(alpha in 0..0xFF)
    return this and 0x00FFFFFF or (alpha shl 24)
}

/**
 * 兼容 iOS 风格的 设置方式 == 更加的人性化， 设置 double 就是为了长得一样！！！
 */
fun Int.withAlphaComponent(alpha: Double): Int {
    require(alpha in 0..1)
    return (withAlphaComponent((alpha * 255).toInt()))
}