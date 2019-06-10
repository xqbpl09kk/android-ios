package com.mh.ca

/**
 * Created by tinytitan on 2017/3/12.
 */
// Kotlin 可以直接对各种类型进行扩展,这个就是为了兼容 iOS的Swift
inline fun <T: Any> T.pipely(block: (it: T)->  Unit): T{
	block(this)
	return this
}

