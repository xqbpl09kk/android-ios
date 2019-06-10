package com.mh.anko.layout

import android.view.View
import android.widget.FrameLayout

/**
 * Created by tinytitan on 16/10/10.
 * MARK:相对布局 - 扩展到函数上面,进行标准化设置
 */
private val defaultInit: Any.() -> Unit = {}

fun <T: View> T.frameLayout(
		c: android.content.Context?,
		attrs: android.util.AttributeSet?,
		init: FrameLayout.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = FrameLayout.LayoutParams(c!!, attrs!!)
	layoutParams.init()
	this@frameLayout.layoutParams = layoutParams
	return this
}

fun <T: View> T.frameLayout(
		width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		init: FrameLayout.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = FrameLayout.LayoutParams(width, height)
	layoutParams.init()
	this@frameLayout.layoutParams = layoutParams
	return this
}

fun <T: View> T.frameLayout(
		source: android.view.ViewGroup.LayoutParams?,
		init: FrameLayout.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = FrameLayout.LayoutParams(source!!)
	layoutParams.init()
	this@frameLayout.layoutParams = layoutParams
	return this
}

fun <T: View> T.frameLayout(
		source: android.view.ViewGroup.MarginLayoutParams?,
		init: FrameLayout.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = FrameLayout.LayoutParams(source!!)
	layoutParams.init()
	this@frameLayout.layoutParams = layoutParams
	return this
}
